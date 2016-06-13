package org.recipnet.site.content.admin;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.recipnet.site.content.rncontrols.AuthorizationReasonMessage;

public final class index_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static java.util.Vector _jspx_dependants;

  static {
    _jspx_dependants = new java.util.Vector(2);
    _jspx_dependants.add("/WEB-INF/controls.tld");
    _jspx_dependants.add("/WEB-INF/rncontrols.tld");
  }

  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_page_title;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_authorizationChecker_invert_canAdministerLabs;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_redirectToLogin_target_returnUrlParamName_authorizationReasonParamName_authorizationReasonCode_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_authorizationChecker_canAdministerLabs;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_a_href;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_a_href_disabled;

  public java.util.List getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _jspx_tagPool_rn_page_title = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_authorizationChecker_invert_canAdministerLabs = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_redirectToLogin_target_returnUrlParamName_authorizationReasonParamName_authorizationReasonCode_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_authorizationChecker_canAdministerLabs = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_a_href = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_a_href_disabled = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
  }

  public void _jspDestroy() {
    _jspx_tagPool_rn_page_title.release();
    _jspx_tagPool_rn_authorizationChecker_invert_canAdministerLabs.release();
    _jspx_tagPool_rn_redirectToLogin_target_returnUrlParamName_authorizationReasonParamName_authorizationReasonCode_nobody.release();
    _jspx_tagPool_rn_authorizationChecker_canAdministerLabs.release();
    _jspx_tagPool_ctl_a_href.release();
    _jspx_tagPool_ctl_a_href_disabled.release();
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
      _jspx_th_rn_page_0.setTitle("Administrator Index");
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
          out.write(" \n  ");
          //  rn:authorizationChecker
          org.recipnet.site.content.rncontrols.AuthorizationChecker _jspx_th_rn_authorizationChecker_0 = (org.recipnet.site.content.rncontrols.AuthorizationChecker) _jspx_tagPool_rn_authorizationChecker_invert_canAdministerLabs.get(org.recipnet.site.content.rncontrols.AuthorizationChecker.class);
          _jspx_th_rn_authorizationChecker_0.setPageContext(_jspx_page_context);
          _jspx_th_rn_authorizationChecker_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_page_0);
          _jspx_th_rn_authorizationChecker_0.setCanAdministerLabs(true);
          _jspx_th_rn_authorizationChecker_0.setInvert(true);
          int _jspx_eval_rn_authorizationChecker_0 = _jspx_th_rn_authorizationChecker_0.doStartTag();
          if (_jspx_eval_rn_authorizationChecker_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_rn_authorizationChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_rn_authorizationChecker_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_rn_authorizationChecker_0.doInitBody();
            }
            do {
              out.write("\n    ");
              //  rn:redirectToLogin
              org.recipnet.site.content.rncontrols.RedirectToLogin _jspx_th_rn_redirectToLogin_0 = (org.recipnet.site.content.rncontrols.RedirectToLogin) _jspx_tagPool_rn_redirectToLogin_target_returnUrlParamName_authorizationReasonParamName_authorizationReasonCode_nobody.get(org.recipnet.site.content.rncontrols.RedirectToLogin.class);
              _jspx_th_rn_redirectToLogin_0.setPageContext(_jspx_page_context);
              _jspx_th_rn_redirectToLogin_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_0);
              _jspx_th_rn_redirectToLogin_0.setTarget("/login.jsp");
              _jspx_th_rn_redirectToLogin_0.setReturnUrlParamName("origUrl");
              _jspx_th_rn_redirectToLogin_0.setAuthorizationReasonParamName("authorizationFailedReason");
              _jspx_th_rn_redirectToLogin_0.setAuthorizationReasonCode(
          AuthorizationReasonMessage.CANNOT_ADMINISTER_LABS);
              int _jspx_eval_rn_redirectToLogin_0 = _jspx_th_rn_redirectToLogin_0.doStartTag();
              if (_jspx_th_rn_redirectToLogin_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_redirectToLogin_target_returnUrlParamName_authorizationReasonParamName_authorizationReasonCode_nobody.reuse(_jspx_th_rn_redirectToLogin_0);
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
            return;
          _jspx_tagPool_rn_authorizationChecker_invert_canAdministerLabs.reuse(_jspx_th_rn_authorizationChecker_0);
          out.write('\n');
          out.write(' ');
          out.write(' ');
          if (_jspx_meth_rn_authorizationChecker_1(_jspx_th_rn_page_0, _jspx_page_context))
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

  private boolean _jspx_meth_rn_authorizationChecker_1(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_page_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:authorizationChecker
    org.recipnet.site.content.rncontrols.AuthorizationChecker _jspx_th_rn_authorizationChecker_1 = (org.recipnet.site.content.rncontrols.AuthorizationChecker) _jspx_tagPool_rn_authorizationChecker_canAdministerLabs.get(org.recipnet.site.content.rncontrols.AuthorizationChecker.class);
    _jspx_th_rn_authorizationChecker_1.setPageContext(_jspx_page_context);
    _jspx_th_rn_authorizationChecker_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_page_0);
    _jspx_th_rn_authorizationChecker_1.setCanAdministerLabs(true);
    int _jspx_eval_rn_authorizationChecker_1 = _jspx_th_rn_authorizationChecker_1.doStartTag();
    if (_jspx_eval_rn_authorizationChecker_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_authorizationChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_authorizationChecker_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_authorizationChecker_1.doInitBody();
      }
      do {
        out.write("\n    ");
        if (_jspx_meth_ctl_a_0(_jspx_th_rn_authorizationChecker_1, _jspx_page_context))
          return true;
        out.write("<br />\n    ");
        if (_jspx_meth_ctl_a_1(_jspx_th_rn_authorizationChecker_1, _jspx_page_context))
          return true;
        out.write("<br />\n    ");
        if (_jspx_meth_ctl_a_2(_jspx_th_rn_authorizationChecker_1, _jspx_page_context))
          return true;
        out.write("<br />\n    ");
        if (_jspx_meth_ctl_a_3(_jspx_th_rn_authorizationChecker_1, _jspx_page_context))
          return true;
        out.write("<br />\n    ");
        if (_jspx_meth_ctl_a_4(_jspx_th_rn_authorizationChecker_1, _jspx_page_context))
          return true;
        out.write("<br />\n    <!--  Remove this table when we know what content to put here. --!>\n    <table width=\"100%\" height=\"400\">\n      <tr><td></td></tr>\n    </table>\n    <!--   End   -->\n  ");
        int evalDoAfterBody = _jspx_th_rn_authorizationChecker_1.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_authorizationChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_authorizationChecker_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_authorizationChecker_canAdministerLabs.reuse(_jspx_th_rn_authorizationChecker_1);
    return false;
  }

  private boolean _jspx_meth_ctl_a_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_authorizationChecker_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:a
    org.recipnet.common.controls.LinkHtmlElement _jspx_th_ctl_a_0 = (org.recipnet.common.controls.LinkHtmlElement) _jspx_tagPool_ctl_a_href.get(org.recipnet.common.controls.LinkHtmlElement.class);
    _jspx_th_ctl_a_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_a_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_1);
    _jspx_th_ctl_a_0.setHref("/admin/managelabs.jsp");
    int _jspx_eval_ctl_a_0 = _jspx_th_ctl_a_0.doStartTag();
    if (_jspx_eval_ctl_a_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_a_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_a_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_a_0.doInitBody();
      }
      do {
        out.write("Manage Labs");
        int evalDoAfterBody = _jspx_th_ctl_a_0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_a_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_a_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_a_href.reuse(_jspx_th_ctl_a_0);
    return false;
  }

  private boolean _jspx_meth_ctl_a_1(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_authorizationChecker_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:a
    org.recipnet.common.controls.LinkHtmlElement _jspx_th_ctl_a_1 = (org.recipnet.common.controls.LinkHtmlElement) _jspx_tagPool_ctl_a_href_disabled.get(org.recipnet.common.controls.LinkHtmlElement.class);
    _jspx_th_ctl_a_1.setPageContext(_jspx_page_context);
    _jspx_th_ctl_a_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_1);
    _jspx_th_ctl_a_1.setDisabled(true);
    _jspx_th_ctl_a_1.setHref("/admin/siteconfig.jsp");
    int _jspx_eval_ctl_a_1 = _jspx_th_ctl_a_1.doStartTag();
    if (_jspx_eval_ctl_a_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_a_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_a_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_a_1.doInitBody();
      }
      do {
        out.write("Configure Site");
        int evalDoAfterBody = _jspx_th_ctl_a_1.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_a_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_a_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_a_href_disabled.reuse(_jspx_th_ctl_a_1);
    return false;
  }

  private boolean _jspx_meth_ctl_a_2(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_authorizationChecker_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:a
    org.recipnet.common.controls.LinkHtmlElement _jspx_th_ctl_a_2 = (org.recipnet.common.controls.LinkHtmlElement) _jspx_tagPool_ctl_a_href_disabled.get(org.recipnet.common.controls.LinkHtmlElement.class);
    _jspx_th_ctl_a_2.setPageContext(_jspx_page_context);
    _jspx_th_ctl_a_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_1);
    _jspx_th_ctl_a_2.setDisabled(true);
    _jspx_th_ctl_a_2.setHref("/admin/replconfig.jsp");
    int _jspx_eval_ctl_a_2 = _jspx_th_ctl_a_2.doStartTag();
    if (_jspx_eval_ctl_a_2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_a_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_a_2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_a_2.doInitBody();
      }
      do {
        out.write("Configure Replication");
        int evalDoAfterBody = _jspx_th_ctl_a_2.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_a_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_a_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_a_href_disabled.reuse(_jspx_th_ctl_a_2);
    return false;
  }

  private boolean _jspx_meth_ctl_a_3(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_authorizationChecker_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:a
    org.recipnet.common.controls.LinkHtmlElement _jspx_th_ctl_a_3 = (org.recipnet.common.controls.LinkHtmlElement) _jspx_tagPool_ctl_a_href_disabled.get(org.recipnet.common.controls.LinkHtmlElement.class);
    _jspx_th_ctl_a_3.setPageContext(_jspx_page_context);
    _jspx_th_ctl_a_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_1);
    _jspx_th_ctl_a_3.setDisabled(true);
    _jspx_th_ctl_a_3.setHref("/admin/localtracking.jsp");
    int _jspx_eval_ctl_a_3 = _jspx_th_ctl_a_3.doStartTag();
    if (_jspx_eval_ctl_a_3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_a_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_a_3.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_a_3.doInitBody();
      }
      do {
        out.write("Configure Local Tracking");
        int evalDoAfterBody = _jspx_th_ctl_a_3.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_a_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_a_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_a_href_disabled.reuse(_jspx_th_ctl_a_3);
    return false;
  }

  private boolean _jspx_meth_ctl_a_4(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_authorizationChecker_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:a
    org.recipnet.common.controls.LinkHtmlElement _jspx_th_ctl_a_4 = (org.recipnet.common.controls.LinkHtmlElement) _jspx_tagPool_ctl_a_href.get(org.recipnet.common.controls.LinkHtmlElement.class);
    _jspx_th_ctl_a_4.setPageContext(_jspx_page_context);
    _jspx_th_ctl_a_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_1);
    _jspx_th_ctl_a_4.setHref("/admin/replstatus.jsp");
    int _jspx_eval_ctl_a_4 = _jspx_th_ctl_a_4.doStartTag();
    if (_jspx_eval_ctl_a_4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_a_4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_a_4.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_a_4.doInitBody();
      }
      do {
        out.write("Replication Status");
        int evalDoAfterBody = _jspx_th_ctl_a_4.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_a_4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_a_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_a_href.reuse(_jspx_th_ctl_a_4);
    return false;
  }
}
