package org.recipnet.site.content;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class footer_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static java.util.Vector _jspx_dependants;

  static {
    _jspx_dependants = new java.util.Vector(1);
    _jspx_dependants.add("/WEB-INF/controls.tld");
  }

  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_page_suppressGeneratedHtml;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_img_src_height_border_alt_nobody;

  public java.util.List getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _jspx_tagPool_ctl_page_suppressGeneratedHtml = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_img_src_height_border_alt_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
  }

  public void _jspDestroy() {
    _jspx_tagPool_ctl_page_suppressGeneratedHtml.release();
    _jspx_tagPool_ctl_img_src_height_border_alt_nobody.release();
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
          out.write("\n<hr size=\"1\" style=\"color: #9E9E9E; clear: both;\"/>\n<div style=\"font-family: sans-serif; font-size: x-small; color: #9E9E9E;\n      text-align: center; \">\n  Reciprocal Net site software @BUILDNUMBER@,\n  copyright (c) 2002-2009, The Trustees of Indiana University<br />\n  Files and data presented via this software are property of their\n  respective owners.<br />\n  Reciprocal Net is funded by the U.S. National Science Foundation as part of\n  the National Science Digital Library project.\n  <a href=\"http://www.nsdl.org\" style=\"padding-left: 0.5em;\">");
          if (_jspx_meth_ctl_img_0(_jspx_th_ctl_page_0, _jspx_page_context))
            return;
          out.write("</a>\n</div>\n");
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

  private boolean _jspx_meth_ctl_img_0(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_page_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:img
    org.recipnet.common.controls.ImageHtmlElement _jspx_th_ctl_img_0 = (org.recipnet.common.controls.ImageHtmlElement) _jspx_tagPool_ctl_img_src_height_border_alt_nobody.get(org.recipnet.common.controls.ImageHtmlElement.class);
    _jspx_th_ctl_img_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_img_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_page_0);
    _jspx_th_ctl_img_0.setHeight(11);
    _jspx_th_ctl_img_0.setBorder(0);
    _jspx_th_ctl_img_0.setSrc("/images/nsdl_logo_small.gif");
    _jspx_th_ctl_img_0.setAlt("NSDL");
    int _jspx_eval_ctl_img_0 = _jspx_th_ctl_img_0.doStartTag();
    if (_jspx_th_ctl_img_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_img_src_height_border_alt_nobody.reuse(_jspx_th_ctl_img_0);
    return false;
  }
}
