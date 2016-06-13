package org.recipnet.site.content;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.recipnet.site.content.rncontrols.LabField;
import org.recipnet.site.shared.db.SampleInfo;

public final class showsamplejumpsite_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static java.util.Vector _jspx_dependants;

  static {
    _jspx_dependants = new java.util.Vector(2);
    _jspx_dependants.add("/WEB-INF/controls.tld");
    _jspx_dependants.add("/WEB-INF/rncontrols.tld");
  }

  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_showSamplePage_useLabAndNumberAsTitlePrefix_titleSuffix_loginPageUrl_labAndNumberSeparator_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_holdingsIterator;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_link_href_considerSiteContext;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_siteSponsorImage_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_labField_fieldCode_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleField_fieldCode_displayAsLabel_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_siteField_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_holdingLevel_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_styleBlock;

  public java.util.List getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _jspx_tagPool_rn_showSamplePage_useLabAndNumberAsTitlePrefix_titleSuffix_loginPageUrl_labAndNumberSeparator_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_holdingsIterator = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_link_href_considerSiteContext = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_siteSponsorImage_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_labField_fieldCode_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleField_fieldCode_displayAsLabel_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_siteField_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_holdingLevel_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_styleBlock = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
  }

  public void _jspDestroy() {
    _jspx_tagPool_rn_showSamplePage_useLabAndNumberAsTitlePrefix_titleSuffix_loginPageUrl_labAndNumberSeparator_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName.release();
    _jspx_tagPool_rn_holdingsIterator.release();
    _jspx_tagPool_rn_link_href_considerSiteContext.release();
    _jspx_tagPool_rn_siteSponsorImage_nobody.release();
    _jspx_tagPool_rn_labField_fieldCode_nobody.release();
    _jspx_tagPool_rn_sampleField_fieldCode_displayAsLabel_nobody.release();
    _jspx_tagPool_rn_siteField_nobody.release();
    _jspx_tagPool_rn_holdingLevel_nobody.release();
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

      out.write("\n\n\n\n\n");
      //  rn:showSamplePage
      org.recipnet.site.content.rncontrols.ShowSamplePage _jspx_th_rn_showSamplePage_0 = (org.recipnet.site.content.rncontrols.ShowSamplePage) _jspx_tagPool_rn_showSamplePage_useLabAndNumberAsTitlePrefix_titleSuffix_loginPageUrl_labAndNumberSeparator_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName.get(org.recipnet.site.content.rncontrols.ShowSamplePage.class);
      _jspx_th_rn_showSamplePage_0.setPageContext(_jspx_page_context);
      _jspx_th_rn_showSamplePage_0.setParent(null);
      _jspx_th_rn_showSamplePage_0.setUseLabAndNumberAsTitlePrefix(true);
      _jspx_th_rn_showSamplePage_0.setLabAndNumberSeparator(" sample ");
      _jspx_th_rn_showSamplePage_0.setTitleSuffix(" - Reciprocal Net");
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
          out.write("\n  <table width=\"100%\" cellspacing=\"5\" cellpadding=\"10\">\n    <tr>\n      <td bgcolor=\"#ebebeb\" width=\"65%\" valign=\"top\">\n        <p class=\"jumpSiteInstructions\">\n          <center>\n            <div class=\"jumpSiteHeader\">\n              The requested sample is hosted at another site...\n            </div>\n            <img src=\"images/longarrow.gif\" />\n          </center>\n        </p>\n        <p class=\"jumpSiteInstructions\">\n          The Reciprocal Net sample you requested is not available on\n          this site.&nbsp;&nbsp;The sample is available on other\n          Reciprocal Net sites, however.&nbsp;&nbsp;Please access it at\n          one of the partner sites listed to the right.\n        </p>\n        <p class=\"jumpSiteInstructions\">\n          The Reciprocal Net Site Network is a distributed database;\n          samples are distributed across the Internet for accuracy,\n          efficiency, and high availability.&nbsp;&nbsp;See\n          <a class=\"jumpSiteLink\"\n              href=\"http://www.reciprocalnet.org/master/sitelist.html\">\n");
          out.write("            Reciprocalnet.org\n          </a> for more information about Reciprocal Net's distributed\n          features.\n        </p>\n      </td>\n      <td bgcolor=\"#e7ecf3\" width=\"35%\" align=\"center\">\n        ");
          //  rn:holdingsIterator
          org.recipnet.site.content.rncontrols.HoldingsIterator _jspx_th_rn_holdingsIterator_0 = (org.recipnet.site.content.rncontrols.HoldingsIterator) _jspx_tagPool_rn_holdingsIterator.get(org.recipnet.site.content.rncontrols.HoldingsIterator.class);
          _jspx_th_rn_holdingsIterator_0.setPageContext(_jspx_page_context);
          _jspx_th_rn_holdingsIterator_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_showSamplePage_0);
          int _jspx_eval_rn_holdingsIterator_0 = _jspx_th_rn_holdingsIterator_0.doStartTag();
          if (_jspx_eval_rn_holdingsIterator_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_rn_holdingsIterator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_rn_holdingsIterator_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_rn_holdingsIterator_0.doInitBody();
            }
            do {
              out.write("\n          <p class=\"jumpSiteInstructions\">\n            ");
              if (_jspx_meth_rn_link_0(_jspx_th_rn_holdingsIterator_0, _jspx_page_context))
                return;
              out.write("\n            <br />\n            ");
              //  rn:labField
              org.recipnet.site.content.rncontrols.LabField _jspx_th_rn_labField_0 = (org.recipnet.site.content.rncontrols.LabField) _jspx_tagPool_rn_labField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.LabField.class);
              _jspx_th_rn_labField_0.setPageContext(_jspx_page_context);
              _jspx_th_rn_labField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_holdingsIterator_0);
              _jspx_th_rn_labField_0.setFieldCode(LabField.SHORT_NAME);
              int _jspx_eval_rn_labField_0 = _jspx_th_rn_labField_0.doStartTag();
              if (_jspx_th_rn_labField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_labField_fieldCode_nobody.reuse(_jspx_th_rn_labField_0);
              out.write(" sample\n            ");
              //  rn:sampleField
              org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_0 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_fieldCode_displayAsLabel_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
              _jspx_th_rn_sampleField_0.setPageContext(_jspx_page_context);
              _jspx_th_rn_sampleField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_holdingsIterator_0);
              _jspx_th_rn_sampleField_0.setFieldCode(SampleInfo.LOCAL_LAB_ID);
              _jspx_th_rn_sampleField_0.setDisplayAsLabel(true);
              int _jspx_eval_rn_sampleField_0 = _jspx_th_rn_sampleField_0.doStartTag();
              if (_jspx_th_rn_sampleField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_sampleField_fieldCode_displayAsLabel_nobody.reuse(_jspx_th_rn_sampleField_0);
              out.write(" hosted at \n            ");
              if (_jspx_meth_rn_link_1(_jspx_th_rn_holdingsIterator_0, _jspx_page_context))
                return;
              out.write(' ');
              if (_jspx_meth_rn_holdingLevel_0(_jspx_th_rn_holdingsIterator_0, _jspx_page_context))
                return;
              out.write("\n          </p>\n          <br />\n        ");
              int evalDoAfterBody = _jspx_th_rn_holdingsIterator_0.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
            if (_jspx_eval_rn_holdingsIterator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
              out = _jspx_page_context.popBody();
          }
          if (_jspx_th_rn_holdingsIterator_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
            return;
          _jspx_tagPool_rn_holdingsIterator.reuse(_jspx_th_rn_holdingsIterator_0);
          out.write("\n      </td>\n    </tr>\n  </table>\n  ");
          if (_jspx_meth_ctl_styleBlock_0(_jspx_th_rn_showSamplePage_0, _jspx_page_context))
            return;
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
      _jspx_tagPool_rn_showSamplePage_useLabAndNumberAsTitlePrefix_titleSuffix_loginPageUrl_labAndNumberSeparator_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName.reuse(_jspx_th_rn_showSamplePage_0);
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

  private boolean _jspx_meth_rn_link_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_holdingsIterator_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:link
    org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_0 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_href_considerSiteContext.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
    _jspx_th_rn_link_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_link_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_holdingsIterator_0);
    _jspx_th_rn_link_0.setHref("/showsample.jsp");
    _jspx_th_rn_link_0.setConsiderSiteContext(true);
    int _jspx_eval_rn_link_0 = _jspx_th_rn_link_0.doStartTag();
    if (_jspx_eval_rn_link_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_link_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_link_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_link_0.doInitBody();
      }
      do {
        out.write("\n              ");
        if (_jspx_meth_rn_siteSponsorImage_0(_jspx_th_rn_link_0, _jspx_page_context))
          return true;
        out.write("\n            ");
        int evalDoAfterBody = _jspx_th_rn_link_0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_link_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_link_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_link_href_considerSiteContext.reuse(_jspx_th_rn_link_0);
    return false;
  }

  private boolean _jspx_meth_rn_siteSponsorImage_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_link_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:siteSponsorImage
    org.recipnet.site.content.rncontrols.SiteSponsorImage _jspx_th_rn_siteSponsorImage_0 = (org.recipnet.site.content.rncontrols.SiteSponsorImage) _jspx_tagPool_rn_siteSponsorImage_nobody.get(org.recipnet.site.content.rncontrols.SiteSponsorImage.class);
    _jspx_th_rn_siteSponsorImage_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_siteSponsorImage_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_link_0);
    int _jspx_eval_rn_siteSponsorImage_0 = _jspx_th_rn_siteSponsorImage_0.doStartTag();
    if (_jspx_th_rn_siteSponsorImage_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_siteSponsorImage_nobody.reuse(_jspx_th_rn_siteSponsorImage_0);
    return false;
  }

  private boolean _jspx_meth_rn_link_1(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_holdingsIterator_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:link
    org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_1 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_href_considerSiteContext.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
    _jspx_th_rn_link_1.setPageContext(_jspx_page_context);
    _jspx_th_rn_link_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_holdingsIterator_0);
    _jspx_th_rn_link_1.setHref("/showsample.jsp");
    _jspx_th_rn_link_1.setConsiderSiteContext(true);
    int _jspx_eval_rn_link_1 = _jspx_th_rn_link_1.doStartTag();
    if (_jspx_eval_rn_link_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_link_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_link_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_link_1.doInitBody();
      }
      do {
        out.write("\n              <nobr>");
        if (_jspx_meth_rn_siteField_0(_jspx_th_rn_link_1, _jspx_page_context))
          return true;
        out.write("</nobr>\n            ");
        int evalDoAfterBody = _jspx_th_rn_link_1.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_link_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_link_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_link_href_considerSiteContext.reuse(_jspx_th_rn_link_1);
    return false;
  }

  private boolean _jspx_meth_rn_siteField_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_link_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:siteField
    org.recipnet.site.content.rncontrols.SiteField _jspx_th_rn_siteField_0 = (org.recipnet.site.content.rncontrols.SiteField) _jspx_tagPool_rn_siteField_nobody.get(org.recipnet.site.content.rncontrols.SiteField.class);
    _jspx_th_rn_siteField_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_siteField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_link_1);
    int _jspx_eval_rn_siteField_0 = _jspx_th_rn_siteField_0.doStartTag();
    if (_jspx_th_rn_siteField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_siteField_nobody.reuse(_jspx_th_rn_siteField_0);
    return false;
  }

  private boolean _jspx_meth_rn_holdingLevel_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_holdingsIterator_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:holdingLevel
    org.recipnet.site.content.rncontrols.HoldingLevel _jspx_th_rn_holdingLevel_0 = (org.recipnet.site.content.rncontrols.HoldingLevel) _jspx_tagPool_rn_holdingLevel_nobody.get(org.recipnet.site.content.rncontrols.HoldingLevel.class);
    _jspx_th_rn_holdingLevel_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_holdingLevel_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_holdingsIterator_0);
    int _jspx_eval_rn_holdingLevel_0 = _jspx_th_rn_holdingLevel_0.doStartTag();
    if (_jspx_th_rn_holdingLevel_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_holdingLevel_nobody.reuse(_jspx_th_rn_holdingLevel_0);
    return false;
  }

  private boolean _jspx_meth_ctl_styleBlock_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_showSamplePage_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:styleBlock
    org.recipnet.common.controls.HtmlPageStyleBlock _jspx_th_ctl_styleBlock_0 = (org.recipnet.common.controls.HtmlPageStyleBlock) _jspx_tagPool_ctl_styleBlock.get(org.recipnet.common.controls.HtmlPageStyleBlock.class);
    _jspx_th_ctl_styleBlock_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_styleBlock_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_showSamplePage_0);
    int _jspx_eval_ctl_styleBlock_0 = _jspx_th_ctl_styleBlock_0.doStartTag();
    if (_jspx_eval_ctl_styleBlock_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_styleBlock_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_styleBlock_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_styleBlock_0.doInitBody();
      }
      do {
        out.write("\n    a.jumpSiteLink:visited    { font-weight: normal; font-style: normal;\n        font-size: 16px; text-decoration: none; color: #0033CC; }\n    a.jumpSiteLink:hover      { font-weight: normal; font-style: normal;\n        font-size: 16px; text-decoration: underline; color: #0033CC; }\n    a.jumpSiteLink:link       { font-weight: normal; font-style: normal;\n        font-size: 16px; text-decoration: none; color: #0033CC; }\n\n    .jumpSiteInstructions {\n        font-family: Arial, Verdana, Helvetica, sans-serif;\n        font-size: 16px;\n        color: #000000;\n        font-weight: normal;\n        margin: 20px;\n    }\n\n    .jumpSiteHeader {\n        font-family: Arial, Verdana, Helvetica, sans-serif;\n        font-size: 16px;\n        color: #0033CC;\n        font-weight: bold;\n        margin: 10px;\n    }\n  ");
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
