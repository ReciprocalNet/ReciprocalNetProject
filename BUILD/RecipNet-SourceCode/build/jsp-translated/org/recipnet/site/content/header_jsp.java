package org.recipnet.site.content;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.recipnet.site.content.rncontrols.UserField;

public final class header_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static java.util.Vector _jspx_dependants;

  static {
    _jspx_dependants = new java.util.Vector(2);
    _jspx_dependants.add("/WEB-INF/controls.tld");
    _jspx_dependants.add("/WEB-INF/rncontrols.tld");
  }

  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_page_suppressGeneratedHtml;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_currentUserContext;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_requestAttributeChecker_includeIfAttributeIsPresent_attributeName;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_img_src_border_alt_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_img_width_src_border_alt_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_a_styleClass_href;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_authorizationChecker_canSeeLabSummary;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_authorizationChecker_canSubmitSamples;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_authorizationChecker_canAdministerLabs;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_form_pageForm_method_action;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_displayRequestAttributeValue_attributeName_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_authorizationChecker_invert;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_a_href;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_currentPageLinkParam_name_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_authorizationChecker;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_userField_id_fieldCode_displayAsLabel_nobody;

  public java.util.List getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _jspx_tagPool_ctl_page_suppressGeneratedHtml = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_currentUserContext = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_requestAttributeChecker_includeIfAttributeIsPresent_attributeName = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_img_src_border_alt_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_img_width_src_border_alt_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_a_styleClass_href = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_authorizationChecker_canSeeLabSummary = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_authorizationChecker_canSubmitSamples = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_authorizationChecker_canAdministerLabs = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_form_pageForm_method_action = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_displayRequestAttributeValue_attributeName_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_authorizationChecker_invert = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_a_href = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_currentPageLinkParam_name_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_authorizationChecker = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_userField_id_fieldCode_displayAsLabel_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
  }

  public void _jspDestroy() {
    _jspx_tagPool_ctl_page_suppressGeneratedHtml.release();
    _jspx_tagPool_rn_currentUserContext.release();
    _jspx_tagPool_ctl_requestAttributeChecker_includeIfAttributeIsPresent_attributeName.release();
    _jspx_tagPool_ctl_img_src_border_alt_nobody.release();
    _jspx_tagPool_ctl_img_width_src_border_alt_nobody.release();
    _jspx_tagPool_ctl_a_styleClass_href.release();
    _jspx_tagPool_rn_authorizationChecker_canSeeLabSummary.release();
    _jspx_tagPool_rn_authorizationChecker_canSubmitSamples.release();
    _jspx_tagPool_rn_authorizationChecker_canAdministerLabs.release();
    _jspx_tagPool_ctl_form_pageForm_method_action.release();
    _jspx_tagPool_ctl_displayRequestAttributeValue_attributeName_nobody.release();
    _jspx_tagPool_rn_authorizationChecker_invert.release();
    _jspx_tagPool_ctl_a_href.release();
    _jspx_tagPool_ctl_currentPageLinkParam_name_nobody.release();
    _jspx_tagPool_rn_authorizationChecker.release();
    _jspx_tagPool_rn_userField_id_fieldCode_displayAsLabel_nobody.release();
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
      //  ctl:page
      org.recipnet.common.controls.HtmlPage _jspx_th_ctl_page_0 = (org.recipnet.common.controls.HtmlPage) _jspx_tagPool_ctl_page_suppressGeneratedHtml.get(org.recipnet.common.controls.HtmlPage.class);
      _jspx_th_ctl_page_0.setPageContext(_jspx_page_context);
      _jspx_th_ctl_page_0.setParent(null);
      _jspx_th_ctl_page_0.setSuppressGeneratedHtml(true);
      int _jspx_eval_ctl_page_0 = _jspx_th_ctl_page_0.doStartTag();
      if (_jspx_eval_ctl_page_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        org.recipnet.common.controls.HtmlPage htmlPage = null;
        if (_jspx_eval_ctl_page_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
          out = _jspx_page_context.pushBody();
          _jspx_th_ctl_page_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
          _jspx_th_ctl_page_0.doInitBody();
        }
        htmlPage = (org.recipnet.common.controls.HtmlPage) _jspx_page_context.findAttribute("htmlPage");
        do {
          out.write('\n');
          out.write(' ');
          out.write(' ');
          //  rn:currentUserContext
          org.recipnet.site.content.rncontrols.CurrentUserContext _jspx_th_rn_currentUserContext_0 = (org.recipnet.site.content.rncontrols.CurrentUserContext) _jspx_tagPool_rn_currentUserContext.get(org.recipnet.site.content.rncontrols.CurrentUserContext.class);
          _jspx_th_rn_currentUserContext_0.setPageContext(_jspx_page_context);
          _jspx_th_rn_currentUserContext_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_page_0);
          int _jspx_eval_rn_currentUserContext_0 = _jspx_th_rn_currentUserContext_0.doStartTag();
          if (_jspx_eval_rn_currentUserContext_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_rn_currentUserContext_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_rn_currentUserContext_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_rn_currentUserContext_0.doInitBody();
            }
            do {
              out.write("\n    <table class=\"header\" border=\"0\" cellspacing=\"0\" width=\"100%\">\n      <tr style=\"background: black\">\n        <td>");
              if (_jspx_meth_ctl_requestAttributeChecker_0(_jspx_th_rn_currentUserContext_0, _jspx_page_context))
                return;
              if (_jspx_meth_ctl_img_0(_jspx_th_rn_currentUserContext_0, _jspx_page_context))
                return;
              if (_jspx_meth_ctl_requestAttributeChecker_1(_jspx_th_rn_currentUserContext_0, _jspx_page_context))
                return;
              out.write("</td>\n        <td align=\"right\">");
              if (_jspx_meth_ctl_requestAttributeChecker_2(_jspx_th_rn_currentUserContext_0, _jspx_page_context))
                return;
              if (_jspx_meth_ctl_img_1(_jspx_th_rn_currentUserContext_0, _jspx_page_context))
                return;
              if (_jspx_meth_ctl_requestAttributeChecker_3(_jspx_th_rn_currentUserContext_0, _jspx_page_context))
                return;
              out.write("</td>\n      </tr>\n      ");
              //  ctl:requestAttributeChecker
              org.recipnet.common.controls.RequestAttributeChecker _jspx_th_ctl_requestAttributeChecker_4 = (org.recipnet.common.controls.RequestAttributeChecker) _jspx_tagPool_ctl_requestAttributeChecker_includeIfAttributeIsPresent_attributeName.get(org.recipnet.common.controls.RequestAttributeChecker.class);
              _jspx_th_ctl_requestAttributeChecker_4.setPageContext(_jspx_page_context);
              _jspx_th_ctl_requestAttributeChecker_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_currentUserContext_0);
              _jspx_th_ctl_requestAttributeChecker_4.setAttributeName("suppressNavigationLinks");
              _jspx_th_ctl_requestAttributeChecker_4.setIncludeIfAttributeIsPresent(false);
              int _jspx_eval_ctl_requestAttributeChecker_4 = _jspx_th_ctl_requestAttributeChecker_4.doStartTag();
              if (_jspx_eval_ctl_requestAttributeChecker_4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_ctl_requestAttributeChecker_4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_ctl_requestAttributeChecker_4.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_ctl_requestAttributeChecker_4.doInitBody();
                }
                do {
                  out.write("\n        <tr style=\"background: #969696\">\n          <td colspan=\"2\">\n            <table border=\"0\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\">\n              <tr>\n                <td>\n                  &nbsp;&nbsp;&nbsp;");
                  if (_jspx_meth_ctl_a_0(_jspx_th_ctl_requestAttributeChecker_4, _jspx_page_context))
                    return;
                  out.write("\n                  &nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;\n                  ");
                  if (_jspx_meth_ctl_a_1(_jspx_th_ctl_requestAttributeChecker_4, _jspx_page_context))
                    return;
                  out.write("\n                  ");
                  if (_jspx_meth_rn_authorizationChecker_0(_jspx_th_ctl_requestAttributeChecker_4, _jspx_page_context))
                    return;
                  out.write("\n                  ");
                  if (_jspx_meth_rn_authorizationChecker_1(_jspx_th_ctl_requestAttributeChecker_4, _jspx_page_context))
                    return;
                  out.write("\n                  ");
                  if (_jspx_meth_rn_authorizationChecker_2(_jspx_th_ctl_requestAttributeChecker_4, _jspx_page_context))
                    return;
                  out.write("\n                </td>\n                <td align=\"right\">\n                  ");
                  if (_jspx_meth_ctl_form_0(_jspx_th_ctl_requestAttributeChecker_4, _jspx_page_context))
                    return;
                  out.write("\n                </td>\n              </tr>\n            </table>\n          </td>\n        </tr>\n        <tr style=\"background: #C8C8C8; font-style: bold\">\n          <td>\n            ");
                  if (_jspx_meth_ctl_displayRequestAttributeValue_0(_jspx_th_ctl_requestAttributeChecker_4, _jspx_page_context))
                    return;
                  out.write("\n          </td>\n          <td align=\"right\">\n            ");
                  if (_jspx_meth_rn_authorizationChecker_3(_jspx_th_ctl_requestAttributeChecker_4, _jspx_page_context))
                    return;
                  out.write("\n            ");
                  //  rn:authorizationChecker
                  org.recipnet.site.content.rncontrols.AuthorizationChecker _jspx_th_rn_authorizationChecker_4 = (org.recipnet.site.content.rncontrols.AuthorizationChecker) _jspx_tagPool_rn_authorizationChecker.get(org.recipnet.site.content.rncontrols.AuthorizationChecker.class);
                  _jspx_th_rn_authorizationChecker_4.setPageContext(_jspx_page_context);
                  _jspx_th_rn_authorizationChecker_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_requestAttributeChecker_4);
                  int _jspx_eval_rn_authorizationChecker_4 = _jspx_th_rn_authorizationChecker_4.doStartTag();
                  if (_jspx_eval_rn_authorizationChecker_4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_rn_authorizationChecker_4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_rn_authorizationChecker_4.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_rn_authorizationChecker_4.doInitBody();
                    }
                    do {
                      out.write("\n              ");
                      if (_jspx_meth_ctl_a_6(_jspx_th_rn_authorizationChecker_4, _jspx_page_context))
                        return;
                      out.write(" &nbsp;\n              ");
                      if (_jspx_meth_ctl_a_7(_jspx_th_rn_authorizationChecker_4, _jspx_page_context))
                        return;
                      out.write("&nbsp;\n              ");
                      //  rn:userField
                      org.recipnet.site.content.rncontrols.UserField HEADER_USER_FULLNAME = null;
                      org.recipnet.site.content.rncontrols.UserField _jspx_th_rn_userField_0 = (org.recipnet.site.content.rncontrols.UserField) _jspx_tagPool_rn_userField_id_fieldCode_displayAsLabel_nobody.get(org.recipnet.site.content.rncontrols.UserField.class);
                      _jspx_th_rn_userField_0.setPageContext(_jspx_page_context);
                      _jspx_th_rn_userField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_4);
                      _jspx_th_rn_userField_0.setFieldCode( UserField.FULL_NAME );
                      _jspx_th_rn_userField_0.setId("HEADER_USER_FULLNAME");
                      _jspx_th_rn_userField_0.setDisplayAsLabel(true);
                      int _jspx_eval_rn_userField_0 = _jspx_th_rn_userField_0.doStartTag();
                      if (_jspx_th_rn_userField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      HEADER_USER_FULLNAME = (org.recipnet.site.content.rncontrols.UserField) _jspx_page_context.findAttribute("HEADER_USER_FULLNAME");
                      _jspx_tagPool_rn_userField_id_fieldCode_displayAsLabel_nobody.reuse(_jspx_th_rn_userField_0);
                      out.write("\n            ");
                      int evalDoAfterBody = _jspx_th_rn_authorizationChecker_4.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_rn_authorizationChecker_4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_rn_authorizationChecker_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_authorizationChecker.reuse(_jspx_th_rn_authorizationChecker_4);
                  out.write("\n          </td>\n        </tr>\n      ");
                  int evalDoAfterBody = _jspx_th_ctl_requestAttributeChecker_4.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_ctl_requestAttributeChecker_4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_ctl_requestAttributeChecker_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_ctl_requestAttributeChecker_includeIfAttributeIsPresent_attributeName.reuse(_jspx_th_ctl_requestAttributeChecker_4);
              out.write("\n    </table>\n  ");
              int evalDoAfterBody = _jspx_th_rn_currentUserContext_0.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
            if (_jspx_eval_rn_currentUserContext_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
              out = _jspx_page_context.popBody();
          }
          if (_jspx_th_rn_currentUserContext_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
            return;
          _jspx_tagPool_rn_currentUserContext.reuse(_jspx_th_rn_currentUserContext_0);
          out.write('\n');
          int evalDoAfterBody = _jspx_th_ctl_page_0.doAfterBody();
          htmlPage = (org.recipnet.common.controls.HtmlPage) _jspx_page_context.findAttribute("htmlPage");
          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
            break;
        } while (true);
        if (_jspx_eval_ctl_page_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
          out = _jspx_page_context.popBody();
      }
      if (_jspx_th_ctl_page_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
        return;
      _jspx_tagPool_ctl_page_suppressGeneratedHtml.reuse(_jspx_th_ctl_page_0);
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

  private boolean _jspx_meth_ctl_requestAttributeChecker_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_currentUserContext_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:requestAttributeChecker
    org.recipnet.common.controls.RequestAttributeChecker _jspx_th_ctl_requestAttributeChecker_0 = (org.recipnet.common.controls.RequestAttributeChecker) _jspx_tagPool_ctl_requestAttributeChecker_includeIfAttributeIsPresent_attributeName.get(org.recipnet.common.controls.RequestAttributeChecker.class);
    _jspx_th_ctl_requestAttributeChecker_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_requestAttributeChecker_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_currentUserContext_0);
    _jspx_th_ctl_requestAttributeChecker_0.setAttributeName("suppressNavigationLinks");
    _jspx_th_ctl_requestAttributeChecker_0.setIncludeIfAttributeIsPresent(false);
    int _jspx_eval_ctl_requestAttributeChecker_0 = _jspx_th_ctl_requestAttributeChecker_0.doStartTag();
    if (_jspx_eval_ctl_requestAttributeChecker_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_requestAttributeChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_requestAttributeChecker_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_requestAttributeChecker_0.doInitBody();
      }
      do {
        out.write("<a href=\"http://www.reciprocalnet.org\">");
        int evalDoAfterBody = _jspx_th_ctl_requestAttributeChecker_0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_requestAttributeChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_requestAttributeChecker_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_requestAttributeChecker_includeIfAttributeIsPresent_attributeName.reuse(_jspx_th_ctl_requestAttributeChecker_0);
    return false;
  }

  private boolean _jspx_meth_ctl_img_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_currentUserContext_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:img
    org.recipnet.common.controls.ImageHtmlElement _jspx_th_ctl_img_0 = (org.recipnet.common.controls.ImageHtmlElement) _jspx_tagPool_ctl_img_src_border_alt_nobody.get(org.recipnet.common.controls.ImageHtmlElement.class);
    _jspx_th_ctl_img_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_img_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_currentUserContext_0);
    _jspx_th_ctl_img_0.setAlt("Reciprocal Net");
    _jspx_th_ctl_img_0.setBorder(0);
    _jspx_th_ctl_img_0.setSrc("/images/banner.jpg");
    int _jspx_eval_ctl_img_0 = _jspx_th_ctl_img_0.doStartTag();
    if (_jspx_th_ctl_img_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_img_src_border_alt_nobody.reuse(_jspx_th_ctl_img_0);
    return false;
  }

  private boolean _jspx_meth_ctl_requestAttributeChecker_1(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_currentUserContext_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:requestAttributeChecker
    org.recipnet.common.controls.RequestAttributeChecker _jspx_th_ctl_requestAttributeChecker_1 = (org.recipnet.common.controls.RequestAttributeChecker) _jspx_tagPool_ctl_requestAttributeChecker_includeIfAttributeIsPresent_attributeName.get(org.recipnet.common.controls.RequestAttributeChecker.class);
    _jspx_th_ctl_requestAttributeChecker_1.setPageContext(_jspx_page_context);
    _jspx_th_ctl_requestAttributeChecker_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_currentUserContext_0);
    _jspx_th_ctl_requestAttributeChecker_1.setAttributeName("suppressNavigationLinks");
    _jspx_th_ctl_requestAttributeChecker_1.setIncludeIfAttributeIsPresent(false);
    int _jspx_eval_ctl_requestAttributeChecker_1 = _jspx_th_ctl_requestAttributeChecker_1.doStartTag();
    if (_jspx_eval_ctl_requestAttributeChecker_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_requestAttributeChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_requestAttributeChecker_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_requestAttributeChecker_1.doInitBody();
      }
      do {
        out.write("</a>");
        int evalDoAfterBody = _jspx_th_ctl_requestAttributeChecker_1.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_requestAttributeChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_requestAttributeChecker_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_requestAttributeChecker_includeIfAttributeIsPresent_attributeName.reuse(_jspx_th_ctl_requestAttributeChecker_1);
    return false;
  }

  private boolean _jspx_meth_ctl_requestAttributeChecker_2(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_currentUserContext_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:requestAttributeChecker
    org.recipnet.common.controls.RequestAttributeChecker _jspx_th_ctl_requestAttributeChecker_2 = (org.recipnet.common.controls.RequestAttributeChecker) _jspx_tagPool_ctl_requestAttributeChecker_includeIfAttributeIsPresent_attributeName.get(org.recipnet.common.controls.RequestAttributeChecker.class);
    _jspx_th_ctl_requestAttributeChecker_2.setPageContext(_jspx_page_context);
    _jspx_th_ctl_requestAttributeChecker_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_currentUserContext_0);
    _jspx_th_ctl_requestAttributeChecker_2.setAttributeName("suppressNavigationLinks");
    _jspx_th_ctl_requestAttributeChecker_2.setIncludeIfAttributeIsPresent(false);
    int _jspx_eval_ctl_requestAttributeChecker_2 = _jspx_th_ctl_requestAttributeChecker_2.doStartTag();
    if (_jspx_eval_ctl_requestAttributeChecker_2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_requestAttributeChecker_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_requestAttributeChecker_2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_requestAttributeChecker_2.doInitBody();
      }
      do {
        out.write("<a href=\"/\">");
        int evalDoAfterBody = _jspx_th_ctl_requestAttributeChecker_2.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_requestAttributeChecker_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_requestAttributeChecker_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_requestAttributeChecker_includeIfAttributeIsPresent_attributeName.reuse(_jspx_th_ctl_requestAttributeChecker_2);
    return false;
  }

  private boolean _jspx_meth_ctl_img_1(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_currentUserContext_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:img
    org.recipnet.common.controls.ImageHtmlElement _jspx_th_ctl_img_1 = (org.recipnet.common.controls.ImageHtmlElement) _jspx_tagPool_ctl_img_width_src_border_alt_nobody.get(org.recipnet.common.controls.ImageHtmlElement.class);
    _jspx_th_ctl_img_1.setPageContext(_jspx_page_context);
    _jspx_th_ctl_img_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_currentUserContext_0);
    _jspx_th_ctl_img_1.setAlt("Site sponsor");
    _jspx_th_ctl_img_1.setBorder(2);
    _jspx_th_ctl_img_1.setWidth(256);
    _jspx_th_ctl_img_1.setSrc("/images/sitesponsor.gif");
    int _jspx_eval_ctl_img_1 = _jspx_th_ctl_img_1.doStartTag();
    if (_jspx_th_ctl_img_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_img_width_src_border_alt_nobody.reuse(_jspx_th_ctl_img_1);
    return false;
  }

  private boolean _jspx_meth_ctl_requestAttributeChecker_3(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_currentUserContext_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:requestAttributeChecker
    org.recipnet.common.controls.RequestAttributeChecker _jspx_th_ctl_requestAttributeChecker_3 = (org.recipnet.common.controls.RequestAttributeChecker) _jspx_tagPool_ctl_requestAttributeChecker_includeIfAttributeIsPresent_attributeName.get(org.recipnet.common.controls.RequestAttributeChecker.class);
    _jspx_th_ctl_requestAttributeChecker_3.setPageContext(_jspx_page_context);
    _jspx_th_ctl_requestAttributeChecker_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_currentUserContext_0);
    _jspx_th_ctl_requestAttributeChecker_3.setAttributeName("suppressNavigationLinks");
    _jspx_th_ctl_requestAttributeChecker_3.setIncludeIfAttributeIsPresent(false);
    int _jspx_eval_ctl_requestAttributeChecker_3 = _jspx_th_ctl_requestAttributeChecker_3.doStartTag();
    if (_jspx_eval_ctl_requestAttributeChecker_3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_requestAttributeChecker_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_requestAttributeChecker_3.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_requestAttributeChecker_3.doInitBody();
      }
      do {
        out.write("</a>");
        int evalDoAfterBody = _jspx_th_ctl_requestAttributeChecker_3.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_requestAttributeChecker_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_requestAttributeChecker_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_requestAttributeChecker_includeIfAttributeIsPresent_attributeName.reuse(_jspx_th_ctl_requestAttributeChecker_3);
    return false;
  }

  private boolean _jspx_meth_ctl_a_0(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_requestAttributeChecker_4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:a
    org.recipnet.common.controls.LinkHtmlElement _jspx_th_ctl_a_0 = (org.recipnet.common.controls.LinkHtmlElement) _jspx_tagPool_ctl_a_styleClass_href.get(org.recipnet.common.controls.LinkHtmlElement.class);
    _jspx_th_ctl_a_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_a_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_requestAttributeChecker_4);
    _jspx_th_ctl_a_0.setStyleClass("navItem");
    _jspx_th_ctl_a_0.setHref("/index.jsp");
    int _jspx_eval_ctl_a_0 = _jspx_th_ctl_a_0.doStartTag();
    if (_jspx_eval_ctl_a_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_a_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_a_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_a_0.doInitBody();
      }
      do {
        out.write("Site Info");
        int evalDoAfterBody = _jspx_th_ctl_a_0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_a_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_a_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_a_styleClass_href.reuse(_jspx_th_ctl_a_0);
    return false;
  }

  private boolean _jspx_meth_ctl_a_1(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_requestAttributeChecker_4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:a
    org.recipnet.common.controls.LinkHtmlElement _jspx_th_ctl_a_1 = (org.recipnet.common.controls.LinkHtmlElement) _jspx_tagPool_ctl_a_styleClass_href.get(org.recipnet.common.controls.LinkHtmlElement.class);
    _jspx_th_ctl_a_1.setPageContext(_jspx_page_context);
    _jspx_th_ctl_a_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_requestAttributeChecker_4);
    _jspx_th_ctl_a_1.setStyleClass("navItem");
    _jspx_th_ctl_a_1.setHref("/search.jsp");
    int _jspx_eval_ctl_a_1 = _jspx_th_ctl_a_1.doStartTag();
    if (_jspx_eval_ctl_a_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_a_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_a_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_a_1.doInitBody();
      }
      do {
        out.write("Search");
        int evalDoAfterBody = _jspx_th_ctl_a_1.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_a_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_a_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_a_styleClass_href.reuse(_jspx_th_ctl_a_1);
    return false;
  }

  private boolean _jspx_meth_rn_authorizationChecker_0(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_requestAttributeChecker_4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:authorizationChecker
    org.recipnet.site.content.rncontrols.AuthorizationChecker _jspx_th_rn_authorizationChecker_0 = (org.recipnet.site.content.rncontrols.AuthorizationChecker) _jspx_tagPool_rn_authorizationChecker_canSeeLabSummary.get(org.recipnet.site.content.rncontrols.AuthorizationChecker.class);
    _jspx_th_rn_authorizationChecker_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_authorizationChecker_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_requestAttributeChecker_4);
    _jspx_th_rn_authorizationChecker_0.setCanSeeLabSummary(true);
    int _jspx_eval_rn_authorizationChecker_0 = _jspx_th_rn_authorizationChecker_0.doStartTag();
    if (_jspx_eval_rn_authorizationChecker_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_authorizationChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_authorizationChecker_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_authorizationChecker_0.doInitBody();
      }
      do {
        out.write("\n                  &nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;\n                    ");
        if (_jspx_meth_ctl_a_2(_jspx_th_rn_authorizationChecker_0, _jspx_page_context))
          return true;
        out.write("\n                  ");
        int evalDoAfterBody = _jspx_th_rn_authorizationChecker_0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_authorizationChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_authorizationChecker_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_authorizationChecker_canSeeLabSummary.reuse(_jspx_th_rn_authorizationChecker_0);
    return false;
  }

  private boolean _jspx_meth_ctl_a_2(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_authorizationChecker_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:a
    org.recipnet.common.controls.LinkHtmlElement _jspx_th_ctl_a_2 = (org.recipnet.common.controls.LinkHtmlElement) _jspx_tagPool_ctl_a_styleClass_href.get(org.recipnet.common.controls.LinkHtmlElement.class);
    _jspx_th_ctl_a_2.setPageContext(_jspx_page_context);
    _jspx_th_ctl_a_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_0);
    _jspx_th_ctl_a_2.setStyleClass("navItem");
    _jspx_th_ctl_a_2.setHref("/lab/summary.jsp");
    int _jspx_eval_ctl_a_2 = _jspx_th_ctl_a_2.doStartTag();
    if (_jspx_eval_ctl_a_2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_a_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_a_2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_a_2.doInitBody();
      }
      do {
        out.write("Lab\n                      Summary");
        int evalDoAfterBody = _jspx_th_ctl_a_2.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_a_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_a_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_a_styleClass_href.reuse(_jspx_th_ctl_a_2);
    return false;
  }

  private boolean _jspx_meth_rn_authorizationChecker_1(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_requestAttributeChecker_4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:authorizationChecker
    org.recipnet.site.content.rncontrols.AuthorizationChecker _jspx_th_rn_authorizationChecker_1 = (org.recipnet.site.content.rncontrols.AuthorizationChecker) _jspx_tagPool_rn_authorizationChecker_canSubmitSamples.get(org.recipnet.site.content.rncontrols.AuthorizationChecker.class);
    _jspx_th_rn_authorizationChecker_1.setPageContext(_jspx_page_context);
    _jspx_th_rn_authorizationChecker_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_requestAttributeChecker_4);
    _jspx_th_rn_authorizationChecker_1.setCanSubmitSamples(true);
    int _jspx_eval_rn_authorizationChecker_1 = _jspx_th_rn_authorizationChecker_1.doStartTag();
    if (_jspx_eval_rn_authorizationChecker_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_authorizationChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_authorizationChecker_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_authorizationChecker_1.doInitBody();
      }
      do {
        out.write("\n                  &nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;\n                    ");
        if (_jspx_meth_ctl_a_3(_jspx_th_rn_authorizationChecker_1, _jspx_page_context))
          return true;
        out.write("\n                  ");
        int evalDoAfterBody = _jspx_th_rn_authorizationChecker_1.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_authorizationChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_authorizationChecker_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_authorizationChecker_canSubmitSamples.reuse(_jspx_th_rn_authorizationChecker_1);
    return false;
  }

  private boolean _jspx_meth_ctl_a_3(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_authorizationChecker_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:a
    org.recipnet.common.controls.LinkHtmlElement _jspx_th_ctl_a_3 = (org.recipnet.common.controls.LinkHtmlElement) _jspx_tagPool_ctl_a_styleClass_href.get(org.recipnet.common.controls.LinkHtmlElement.class);
    _jspx_th_ctl_a_3.setPageContext(_jspx_page_context);
    _jspx_th_ctl_a_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_1);
    _jspx_th_ctl_a_3.setStyleClass("navItem");
    _jspx_th_ctl_a_3.setHref("/lab/submit.jsp");
    int _jspx_eval_ctl_a_3 = _jspx_th_ctl_a_3.doStartTag();
    if (_jspx_eval_ctl_a_3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_a_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_a_3.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_a_3.doInitBody();
      }
      do {
        out.write("Submit\n                      Sample");
        int evalDoAfterBody = _jspx_th_ctl_a_3.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_a_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_a_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_a_styleClass_href.reuse(_jspx_th_ctl_a_3);
    return false;
  }

  private boolean _jspx_meth_rn_authorizationChecker_2(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_requestAttributeChecker_4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:authorizationChecker
    org.recipnet.site.content.rncontrols.AuthorizationChecker _jspx_th_rn_authorizationChecker_2 = (org.recipnet.site.content.rncontrols.AuthorizationChecker) _jspx_tagPool_rn_authorizationChecker_canAdministerLabs.get(org.recipnet.site.content.rncontrols.AuthorizationChecker.class);
    _jspx_th_rn_authorizationChecker_2.setPageContext(_jspx_page_context);
    _jspx_th_rn_authorizationChecker_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_requestAttributeChecker_4);
    _jspx_th_rn_authorizationChecker_2.setCanAdministerLabs(true);
    int _jspx_eval_rn_authorizationChecker_2 = _jspx_th_rn_authorizationChecker_2.doStartTag();
    if (_jspx_eval_rn_authorizationChecker_2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_authorizationChecker_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_authorizationChecker_2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_authorizationChecker_2.doInitBody();
      }
      do {
        out.write("\n                  &nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;\n                    ");
        if (_jspx_meth_ctl_a_4(_jspx_th_rn_authorizationChecker_2, _jspx_page_context))
          return true;
        out.write("\n                  ");
        int evalDoAfterBody = _jspx_th_rn_authorizationChecker_2.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_authorizationChecker_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_authorizationChecker_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_authorizationChecker_canAdministerLabs.reuse(_jspx_th_rn_authorizationChecker_2);
    return false;
  }

  private boolean _jspx_meth_ctl_a_4(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_authorizationChecker_2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:a
    org.recipnet.common.controls.LinkHtmlElement _jspx_th_ctl_a_4 = (org.recipnet.common.controls.LinkHtmlElement) _jspx_tagPool_ctl_a_styleClass_href.get(org.recipnet.common.controls.LinkHtmlElement.class);
    _jspx_th_ctl_a_4.setPageContext(_jspx_page_context);
    _jspx_th_ctl_a_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_2);
    _jspx_th_ctl_a_4.setStyleClass("navItem");
    _jspx_th_ctl_a_4.setHref("/admin/index.jsp");
    int _jspx_eval_ctl_a_4 = _jspx_th_ctl_a_4.doStartTag();
    if (_jspx_eval_ctl_a_4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_a_4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_a_4.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_a_4.doInitBody();
      }
      do {
        out.write("Admin\n                      Tools");
        int evalDoAfterBody = _jspx_th_ctl_a_4.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_a_4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_a_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_a_styleClass_href.reuse(_jspx_th_ctl_a_4);
    return false;
  }

  private boolean _jspx_meth_ctl_form_0(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_requestAttributeChecker_4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:form
    org.recipnet.common.controls.FormHtmlElement _jspx_th_ctl_form_0 = (org.recipnet.common.controls.FormHtmlElement) _jspx_tagPool_ctl_form_pageForm_method_action.get(org.recipnet.common.controls.FormHtmlElement.class);
    _jspx_th_ctl_form_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_form_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_requestAttributeChecker_4);
    _jspx_th_ctl_form_0.setMethod("GET");
    _jspx_th_ctl_form_0.setAction("/search.jsp");
    _jspx_th_ctl_form_0.setPageForm(false);
    int _jspx_eval_ctl_form_0 = _jspx_th_ctl_form_0.doStartTag();
    if (_jspx_eval_ctl_form_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_form_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_form_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_form_0.doInitBody();
      }
      do {
        out.write("\n                    <input name=\"quickSearch\" type=\"text\" size=\"22\"\n                           value=\"sample # (local search)\"\n                           onFocus=\"value=''\" />\n                    <input name=\"quickSearchSubmit\" type=\"submit\" value=\"Go\" />\n                  ");
        int evalDoAfterBody = _jspx_th_ctl_form_0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_form_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_form_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_form_pageForm_method_action.reuse(_jspx_th_ctl_form_0);
    return false;
  }

  private boolean _jspx_meth_ctl_displayRequestAttributeValue_0(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_requestAttributeChecker_4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:displayRequestAttributeValue
    org.recipnet.common.controls.DisplayRequestAttributeValue _jspx_th_ctl_displayRequestAttributeValue_0 = (org.recipnet.common.controls.DisplayRequestAttributeValue) _jspx_tagPool_ctl_displayRequestAttributeValue_attributeName_nobody.get(org.recipnet.common.controls.DisplayRequestAttributeValue.class);
    _jspx_th_ctl_displayRequestAttributeValue_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_displayRequestAttributeValue_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_requestAttributeChecker_4);
    _jspx_th_ctl_displayRequestAttributeValue_0.setAttributeName("pageTitle");
    int _jspx_eval_ctl_displayRequestAttributeValue_0 = _jspx_th_ctl_displayRequestAttributeValue_0.doStartTag();
    if (_jspx_th_ctl_displayRequestAttributeValue_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_displayRequestAttributeValue_attributeName_nobody.reuse(_jspx_th_ctl_displayRequestAttributeValue_0);
    return false;
  }

  private boolean _jspx_meth_rn_authorizationChecker_3(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_requestAttributeChecker_4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:authorizationChecker
    org.recipnet.site.content.rncontrols.AuthorizationChecker _jspx_th_rn_authorizationChecker_3 = (org.recipnet.site.content.rncontrols.AuthorizationChecker) _jspx_tagPool_rn_authorizationChecker_invert.get(org.recipnet.site.content.rncontrols.AuthorizationChecker.class);
    _jspx_th_rn_authorizationChecker_3.setPageContext(_jspx_page_context);
    _jspx_th_rn_authorizationChecker_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_requestAttributeChecker_4);
    _jspx_th_rn_authorizationChecker_3.setInvert(true);
    int _jspx_eval_rn_authorizationChecker_3 = _jspx_th_rn_authorizationChecker_3.doStartTag();
    if (_jspx_eval_rn_authorizationChecker_3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_authorizationChecker_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_authorizationChecker_3.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_authorizationChecker_3.doInitBody();
      }
      do {
        out.write("\n              ");
        if (_jspx_meth_ctl_a_5(_jspx_th_rn_authorizationChecker_3, _jspx_page_context))
          return true;
        out.write("\n            ");
        int evalDoAfterBody = _jspx_th_rn_authorizationChecker_3.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_authorizationChecker_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_authorizationChecker_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_authorizationChecker_invert.reuse(_jspx_th_rn_authorizationChecker_3);
    return false;
  }

  private boolean _jspx_meth_ctl_a_5(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_authorizationChecker_3, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:a
    org.recipnet.common.controls.LinkHtmlElement _jspx_th_ctl_a_5 = (org.recipnet.common.controls.LinkHtmlElement) _jspx_tagPool_ctl_a_href.get(org.recipnet.common.controls.LinkHtmlElement.class);
    _jspx_th_ctl_a_5.setPageContext(_jspx_page_context);
    _jspx_th_ctl_a_5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_3);
    _jspx_th_ctl_a_5.setHref("/login.jsp");
    int _jspx_eval_ctl_a_5 = _jspx_th_ctl_a_5.doStartTag();
    if (_jspx_eval_ctl_a_5 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_a_5 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_a_5.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_a_5.doInitBody();
      }
      do {
        out.write("\n                ");
        if (_jspx_meth_ctl_currentPageLinkParam_0(_jspx_th_ctl_a_5, _jspx_page_context))
          return true;
        out.write("\n                  Log in");
        int evalDoAfterBody = _jspx_th_ctl_a_5.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_a_5 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_a_5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_a_href.reuse(_jspx_th_ctl_a_5);
    return false;
  }

  private boolean _jspx_meth_ctl_currentPageLinkParam_0(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_a_5, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:currentPageLinkParam
    org.recipnet.common.controls.CurrentPageLinkParam _jspx_th_ctl_currentPageLinkParam_0 = (org.recipnet.common.controls.CurrentPageLinkParam) _jspx_tagPool_ctl_currentPageLinkParam_name_nobody.get(org.recipnet.common.controls.CurrentPageLinkParam.class);
    _jspx_th_ctl_currentPageLinkParam_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_currentPageLinkParam_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_5);
    _jspx_th_ctl_currentPageLinkParam_0.setName("origUrl");
    int _jspx_eval_ctl_currentPageLinkParam_0 = _jspx_th_ctl_currentPageLinkParam_0.doStartTag();
    if (_jspx_th_ctl_currentPageLinkParam_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_currentPageLinkParam_name_nobody.reuse(_jspx_th_ctl_currentPageLinkParam_0);
    return false;
  }

  private boolean _jspx_meth_ctl_a_6(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_authorizationChecker_4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:a
    org.recipnet.common.controls.LinkHtmlElement _jspx_th_ctl_a_6 = (org.recipnet.common.controls.LinkHtmlElement) _jspx_tagPool_ctl_a_href.get(org.recipnet.common.controls.LinkHtmlElement.class);
    _jspx_th_ctl_a_6.setPageContext(_jspx_page_context);
    _jspx_th_ctl_a_6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_4);
    _jspx_th_ctl_a_6.setHref("/logout.jsp");
    int _jspx_eval_ctl_a_6 = _jspx_th_ctl_a_6.doStartTag();
    if (_jspx_eval_ctl_a_6 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_a_6 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_a_6.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_a_6.doInitBody();
      }
      do {
        out.write("Log out");
        int evalDoAfterBody = _jspx_th_ctl_a_6.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_a_6 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_a_6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_a_href.reuse(_jspx_th_ctl_a_6);
    return false;
  }

  private boolean _jspx_meth_ctl_a_7(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_authorizationChecker_4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:a
    org.recipnet.common.controls.LinkHtmlElement _jspx_th_ctl_a_7 = (org.recipnet.common.controls.LinkHtmlElement) _jspx_tagPool_ctl_a_href.get(org.recipnet.common.controls.LinkHtmlElement.class);
    _jspx_th_ctl_a_7.setPageContext(_jspx_page_context);
    _jspx_th_ctl_a_7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_4);
    _jspx_th_ctl_a_7.setHref("/account.jsp");
    int _jspx_eval_ctl_a_7 = _jspx_th_ctl_a_7.doStartTag();
    if (_jspx_eval_ctl_a_7 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_a_7 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_a_7.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_a_7.doInitBody();
      }
      do {
        out.write("\n                ");
        if (_jspx_meth_ctl_currentPageLinkParam_1(_jspx_th_ctl_a_7, _jspx_page_context))
          return true;
        out.write("\n                  Preferences\n                ");
        int evalDoAfterBody = _jspx_th_ctl_a_7.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_a_7 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_a_7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_a_href.reuse(_jspx_th_ctl_a_7);
    return false;
  }

  private boolean _jspx_meth_ctl_currentPageLinkParam_1(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_a_7, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:currentPageLinkParam
    org.recipnet.common.controls.CurrentPageLinkParam _jspx_th_ctl_currentPageLinkParam_1 = (org.recipnet.common.controls.CurrentPageLinkParam) _jspx_tagPool_ctl_currentPageLinkParam_name_nobody.get(org.recipnet.common.controls.CurrentPageLinkParam.class);
    _jspx_th_ctl_currentPageLinkParam_1.setPageContext(_jspx_page_context);
    _jspx_th_ctl_currentPageLinkParam_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_7);
    _jspx_th_ctl_currentPageLinkParam_1.setName("origPage");
    int _jspx_eval_ctl_currentPageLinkParam_1 = _jspx_th_ctl_currentPageLinkParam_1.doStartTag();
    if (_jspx_th_ctl_currentPageLinkParam_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_currentPageLinkParam_name_nobody.reuse(_jspx_th_ctl_currentPageLinkParam_1);
    return false;
  }
}
