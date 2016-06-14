package org.recipnet.site.content;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class showsample_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static java.util.Vector _jspx_dependants;

  static {
    _jspx_dependants = new java.util.Vector(1);
    _jspx_dependants.add("/WEB-INF/rncontrols.tld");
  }

  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_showSamplePage_loginPageUrl_jumpSitePageUrl_detailedPageUrl_currentPageReinvocationUrlParamName_basicPageUrl_authorizationFailedReasonParamName;

  public java.util.List getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _jspx_tagPool_rn_showSamplePage_loginPageUrl_jumpSitePageUrl_detailedPageUrl_currentPageReinvocationUrlParamName_basicPageUrl_authorizationFailedReasonParamName = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
  }

  public void _jspDestroy() {
    _jspx_tagPool_rn_showSamplePage_loginPageUrl_jumpSitePageUrl_detailedPageUrl_currentPageReinvocationUrlParamName_basicPageUrl_authorizationFailedReasonParamName.release();
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
      //  rn:showSamplePage
      org.recipnet.site.content.rncontrols.ShowSamplePage _jspx_th_rn_showSamplePage_0 = (org.recipnet.site.content.rncontrols.ShowSamplePage) _jspx_tagPool_rn_showSamplePage_loginPageUrl_jumpSitePageUrl_detailedPageUrl_currentPageReinvocationUrlParamName_basicPageUrl_authorizationFailedReasonParamName.get(org.recipnet.site.content.rncontrols.ShowSamplePage.class);
      _jspx_th_rn_showSamplePage_0.setPageContext(_jspx_page_context);
      _jspx_th_rn_showSamplePage_0.setParent(null);
      _jspx_th_rn_showSamplePage_0.setBasicPageUrl("/showsamplebasic.jsp");
      _jspx_th_rn_showSamplePage_0.setDetailedPageUrl("/showsampledetailed.jsp");
      _jspx_th_rn_showSamplePage_0.setJumpSitePageUrl("/showsamplejumpsite.jsp");
      _jspx_th_rn_showSamplePage_0.setLoginPageUrl("/login.jsp");
      _jspx_th_rn_showSamplePage_0.setCurrentPageReinvocationUrlParamName("origUrl");
      _jspx_th_rn_showSamplePage_0.setAuthorizationFailedReasonParamName("authorizationFailedReason");
      int _jspx_eval_rn_showSamplePage_0 = _jspx_th_rn_showSamplePage_0.doStartTag();
      if (_jspx_eval_rn_showSamplePage_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        org.recipnet.site.content.rncontrols.ShowSamplePage htmlPage = null;
        if (_jspx_eval_rn_showSamplePage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
          out = _jspx_page_context.pushBody();
          _jspx_th_rn_showSamplePage_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
          _jspx_th_rn_showSamplePage_0.doInitBody();
        }
        htmlPage = (org.recipnet.site.content.rncontrols.ShowSamplePage) _jspx_page_context.findAttribute("htmlPage");
        do {
          out.write("\n\n\n  ");
          out.write('\n');
          out.write('\n');
          out.write('\n');
          int evalDoAfterBody = _jspx_th_rn_showSamplePage_0.doAfterBody();
          htmlPage = (org.recipnet.site.content.rncontrols.ShowSamplePage) _jspx_page_context.findAttribute("htmlPage");
          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
            break;
        } while (true);
        if (_jspx_eval_rn_showSamplePage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
          out = _jspx_page_context.popBody();
      }
      if (_jspx_th_rn_showSamplePage_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
        return;
      _jspx_tagPool_rn_showSamplePage_loginPageUrl_jumpSitePageUrl_detailedPageUrl_currentPageReinvocationUrlParamName_basicPageUrl_authorizationFailedReasonParamName.reuse(_jspx_th_rn_showSamplePage_0);
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
}
