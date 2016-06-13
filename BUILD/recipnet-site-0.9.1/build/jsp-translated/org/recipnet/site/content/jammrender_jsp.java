package org.recipnet.site.content;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class jammrender_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static java.util.Vector _jspx_dependants;

  static {
    _jspx_dependants = new java.util.Vector(2);
    _jspx_dependants.add("/WEB-INF/controls.tld");
    _jspx_dependants.add("/WEB-INF/rncontrols.tld");
  }

  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_page_title;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_requestAttributeChecker_includeIfAttributeIsPresent_attributeName;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_redirect_target_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_displayRequestAttributeValue_attributeName_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_fileRetrieverImg_servletUrl_keyRequestAttributeName_alt_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_a_href;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_requestAttributeParam_name_attributeName_nobody;

  public java.util.List getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _jspx_tagPool_rn_page_title = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_requestAttributeChecker_includeIfAttributeIsPresent_attributeName = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_redirect_target_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_displayRequestAttributeValue_attributeName_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_fileRetrieverImg_servletUrl_keyRequestAttributeName_alt_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_a_href = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_requestAttributeParam_name_attributeName_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
  }

  public void _jspDestroy() {
    _jspx_tagPool_rn_page_title.release();
    _jspx_tagPool_ctl_requestAttributeChecker_includeIfAttributeIsPresent_attributeName.release();
    _jspx_tagPool_ctl_redirect_target_nobody.release();
    _jspx_tagPool_ctl_displayRequestAttributeValue_attributeName_nobody.release();
    _jspx_tagPool_rn_fileRetrieverImg_servletUrl_keyRequestAttributeName_alt_nobody.release();
    _jspx_tagPool_ctl_a_href.release();
    _jspx_tagPool_ctl_requestAttributeParam_name_attributeName_nobody.release();
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
      //  rn:page
      org.recipnet.site.content.rncontrols.RecipnetPage _jspx_th_rn_page_0 = (org.recipnet.site.content.rncontrols.RecipnetPage) _jspx_tagPool_rn_page_title.get(org.recipnet.site.content.rncontrols.RecipnetPage.class);
      _jspx_th_rn_page_0.setPageContext(_jspx_page_context);
      _jspx_th_rn_page_0.setParent(null);
      _jspx_th_rn_page_0.setTitle("Rendered Image");
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
          if (_jspx_meth_ctl_requestAttributeChecker_0(_jspx_th_rn_page_0, _jspx_page_context))
            return;
          out.write("\n  <br />\n  <h1>\n    Rendered Image of\n    ");
          if (_jspx_meth_ctl_displayRequestAttributeValue_0(_jspx_th_rn_page_0, _jspx_page_context))
            return;
          out.write(" sample\n    ");
          if (_jspx_meth_ctl_displayRequestAttributeValue_1(_jspx_th_rn_page_0, _jspx_page_context))
            return;
          out.write("\n  </h1>\n  ");
          if (_jspx_meth_rn_fileRetrieverImg_0(_jspx_th_rn_page_0, _jspx_page_context))
            return;
          out.write("\n  <hr />\n  <ul>\n    <li>\n      ");
          if (_jspx_meth_ctl_a_0(_jspx_th_rn_page_0, _jspx_page_context))
            return;
          out.write("\n    </li>\n  </ul>\n");
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

  private boolean _jspx_meth_ctl_requestAttributeChecker_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_page_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:requestAttributeChecker
    org.recipnet.common.controls.RequestAttributeChecker _jspx_th_ctl_requestAttributeChecker_0 = (org.recipnet.common.controls.RequestAttributeChecker) _jspx_tagPool_ctl_requestAttributeChecker_includeIfAttributeIsPresent_attributeName.get(org.recipnet.common.controls.RequestAttributeChecker.class);
    _jspx_th_ctl_requestAttributeChecker_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_requestAttributeChecker_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_page_0);
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
        out.write("\n    ");
        if (_jspx_meth_ctl_redirect_0(_jspx_th_ctl_requestAttributeChecker_0, _jspx_page_context))
          return true;
        out.write('\n');
        out.write(' ');
        out.write(' ');
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

  private boolean _jspx_meth_ctl_redirect_0(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_requestAttributeChecker_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:redirect
    org.recipnet.common.controls.HtmlRedirect _jspx_th_ctl_redirect_0 = (org.recipnet.common.controls.HtmlRedirect) _jspx_tagPool_ctl_redirect_target_nobody.get(org.recipnet.common.controls.HtmlRedirect.class);
    _jspx_th_ctl_redirect_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_redirect_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_requestAttributeChecker_0);
    _jspx_th_ctl_redirect_0.setTarget("/index.jsp");
    int _jspx_eval_ctl_redirect_0 = _jspx_th_ctl_redirect_0.doStartTag();
    if (_jspx_th_ctl_redirect_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_redirect_target_nobody.reuse(_jspx_th_ctl_redirect_0);
    return false;
  }

  private boolean _jspx_meth_ctl_displayRequestAttributeValue_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_page_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:displayRequestAttributeValue
    org.recipnet.common.controls.DisplayRequestAttributeValue _jspx_th_ctl_displayRequestAttributeValue_0 = (org.recipnet.common.controls.DisplayRequestAttributeValue) _jspx_tagPool_ctl_displayRequestAttributeValue_attributeName_nobody.get(org.recipnet.common.controls.DisplayRequestAttributeValue.class);
    _jspx_th_ctl_displayRequestAttributeValue_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_displayRequestAttributeValue_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_page_0);
    _jspx_th_ctl_displayRequestAttributeValue_0.setAttributeName("labShortName");
    int _jspx_eval_ctl_displayRequestAttributeValue_0 = _jspx_th_ctl_displayRequestAttributeValue_0.doStartTag();
    if (_jspx_th_ctl_displayRequestAttributeValue_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_displayRequestAttributeValue_attributeName_nobody.reuse(_jspx_th_ctl_displayRequestAttributeValue_0);
    return false;
  }

  private boolean _jspx_meth_ctl_displayRequestAttributeValue_1(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_page_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:displayRequestAttributeValue
    org.recipnet.common.controls.DisplayRequestAttributeValue _jspx_th_ctl_displayRequestAttributeValue_1 = (org.recipnet.common.controls.DisplayRequestAttributeValue) _jspx_tagPool_ctl_displayRequestAttributeValue_attributeName_nobody.get(org.recipnet.common.controls.DisplayRequestAttributeValue.class);
    _jspx_th_ctl_displayRequestAttributeValue_1.setPageContext(_jspx_page_context);
    _jspx_th_ctl_displayRequestAttributeValue_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_page_0);
    _jspx_th_ctl_displayRequestAttributeValue_1.setAttributeName("sampleLocalLabId");
    int _jspx_eval_ctl_displayRequestAttributeValue_1 = _jspx_th_ctl_displayRequestAttributeValue_1.doStartTag();
    if (_jspx_th_ctl_displayRequestAttributeValue_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_displayRequestAttributeValue_attributeName_nobody.reuse(_jspx_th_ctl_displayRequestAttributeValue_1);
    return false;
  }

  private boolean _jspx_meth_rn_fileRetrieverImg_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_page_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:fileRetrieverImg
    org.recipnet.site.content.rncontrols.FileRetrieverImg _jspx_th_rn_fileRetrieverImg_0 = (org.recipnet.site.content.rncontrols.FileRetrieverImg) _jspx_tagPool_rn_fileRetrieverImg_servletUrl_keyRequestAttributeName_alt_nobody.get(org.recipnet.site.content.rncontrols.FileRetrieverImg.class);
    _jspx_th_rn_fileRetrieverImg_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_fileRetrieverImg_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_page_0);
    _jspx_th_rn_fileRetrieverImg_0.setAlt("photorealistic molecule image");
    _jspx_th_rn_fileRetrieverImg_0.setServletUrl("/servlet/fileretrieve");
    _jspx_th_rn_fileRetrieverImg_0.setKeyRequestAttributeName("jpegKey");
    int _jspx_eval_rn_fileRetrieverImg_0 = _jspx_th_rn_fileRetrieverImg_0.doStartTag();
    if (_jspx_th_rn_fileRetrieverImg_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_fileRetrieverImg_servletUrl_keyRequestAttributeName_alt_nobody.reuse(_jspx_th_rn_fileRetrieverImg_0);
    return false;
  }

  private boolean _jspx_meth_ctl_a_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_page_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:a
    org.recipnet.common.controls.LinkHtmlElement _jspx_th_ctl_a_0 = (org.recipnet.common.controls.LinkHtmlElement) _jspx_tagPool_ctl_a_href.get(org.recipnet.common.controls.LinkHtmlElement.class);
    _jspx_th_ctl_a_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_a_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_page_0);
    _jspx_th_ctl_a_0.setHref("/servlet/fileretrieve");
    int _jspx_eval_ctl_a_0 = _jspx_th_ctl_a_0.doStartTag();
    if (_jspx_eval_ctl_a_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_a_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_a_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_a_0.doInitBody();
      }
      do {
        out.write("\n        ");
        if (_jspx_meth_ctl_requestAttributeParam_0(_jspx_th_ctl_a_0, _jspx_page_context))
          return true;
        out.write("\n        JPEG Version\n      ");
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

  private boolean _jspx_meth_ctl_requestAttributeParam_0(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_a_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:requestAttributeParam
    org.recipnet.common.controls.RequestAttributeParam _jspx_th_ctl_requestAttributeParam_0 = (org.recipnet.common.controls.RequestAttributeParam) _jspx_tagPool_ctl_requestAttributeParam_name_attributeName_nobody.get(org.recipnet.common.controls.RequestAttributeParam.class);
    _jspx_th_ctl_requestAttributeParam_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_requestAttributeParam_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_0);
    _jspx_th_ctl_requestAttributeParam_0.setName("key");
    _jspx_th_ctl_requestAttributeParam_0.setAttributeName("jpegKey");
    int _jspx_eval_ctl_requestAttributeParam_0 = _jspx_th_ctl_requestAttributeParam_0.doStartTag();
    if (_jspx_th_ctl_requestAttributeParam_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_requestAttributeParam_name_attributeName_nobody.reuse(_jspx_th_ctl_requestAttributeParam_0);
    return false;
  }
}
