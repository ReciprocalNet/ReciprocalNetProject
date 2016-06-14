package org.recipnet.site.content;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class logout_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static java.util.Vector _jspx_dependants;

  static {
    _jspx_dependants = new java.util.Vector(2);
    _jspx_dependants.add("/WEB-INF/controls.tld");
    _jspx_dependants.add("/WEB-INF/rncontrols.tld");
  }

  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_page;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_logout_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_redirect_target_nobody;

  public java.util.List getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _jspx_tagPool_ctl_page = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_logout_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_redirect_target_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
  }

  public void _jspDestroy() {
    _jspx_tagPool_ctl_page.release();
    _jspx_tagPool_rn_logout_nobody.release();
    _jspx_tagPool_ctl_redirect_target_nobody.release();
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

      out.write('\n');
      out.write('\n');
      out.write('\n');
      //  ctl:page
      org.recipnet.common.controls.HtmlPage _jspx_th_ctl_page_0 = (org.recipnet.common.controls.HtmlPage) _jspx_tagPool_ctl_page.get(org.recipnet.common.controls.HtmlPage.class);
      _jspx_th_ctl_page_0.setPageContext(_jspx_page_context);
      _jspx_th_ctl_page_0.setParent(null);
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
          if (_jspx_meth_rn_logout_0(_jspx_th_ctl_page_0, _jspx_page_context))
            return;
          out.write('\n');
          out.write(' ');
          out.write(' ');
          if (_jspx_meth_ctl_redirect_0(_jspx_th_ctl_page_0, _jspx_page_context))
            return;
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
      _jspx_tagPool_ctl_page.reuse(_jspx_th_ctl_page_0);
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

  private boolean _jspx_meth_rn_logout_0(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_page_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:logout
    org.recipnet.site.content.rncontrols.Logout _jspx_th_rn_logout_0 = (org.recipnet.site.content.rncontrols.Logout) _jspx_tagPool_rn_logout_nobody.get(org.recipnet.site.content.rncontrols.Logout.class);
    _jspx_th_rn_logout_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_logout_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_page_0);
    int _jspx_eval_rn_logout_0 = _jspx_th_rn_logout_0.doStartTag();
    if (_jspx_th_rn_logout_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_logout_nobody.reuse(_jspx_th_rn_logout_0);
    return false;
  }

  private boolean _jspx_meth_ctl_redirect_0(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_page_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:redirect
    org.recipnet.common.controls.HtmlRedirect _jspx_th_ctl_redirect_0 = (org.recipnet.common.controls.HtmlRedirect) _jspx_tagPool_ctl_redirect_target_nobody.get(org.recipnet.common.controls.HtmlRedirect.class);
    _jspx_th_ctl_redirect_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_redirect_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_page_0);
    _jspx_th_ctl_redirect_0.setTarget("/index.jsp");
    int _jspx_eval_ctl_redirect_0 = _jspx_th_ctl_redirect_0.doStartTag();
    if (_jspx_th_ctl_redirect_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_redirect_target_nobody.reuse(_jspx_th_ctl_redirect_0);
    return false;
  }
}
