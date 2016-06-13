package org.recipnet.site.content.admin;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.recipnet.site.content.rncontrols.SiteField;
import org.recipnet.site.content.rncontrols.SiteIterator;

public final class replstatus_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

static private org.apache.jasper.runtime.ProtectedFunctionMapper _jspx_fnmap_0;

static {
  _jspx_fnmap_0= org.apache.jasper.runtime.ProtectedFunctionMapper.getMapForFunction("rn:testParity", org.recipnet.site.content.rncontrols.ElFunctions.class, "testParity", new Class[] {int.class, java.lang.String.class, java.lang.String.class});
}

  private static java.util.Vector _jspx_dependants;

  static {
    _jspx_dependants = new java.util.Vector(2);
    _jspx_dependants.add("/WEB-INF/controls.tld");
    _jspx_dependants.add("/WEB-INF/rncontrols.tld");
  }

  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_page_title;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_siteIterator_id;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_siteField_fieldCode_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_a_href;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_styleBlock;

  public java.util.List getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _jspx_tagPool_rn_page_title = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_siteIterator_id = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_siteField_fieldCode_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_a_href = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_styleBlock = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
  }

  public void _jspDestroy() {
    _jspx_tagPool_rn_page_title.release();
    _jspx_tagPool_rn_siteIterator_id.release();
    _jspx_tagPool_rn_siteField_fieldCode_nobody.release();
    _jspx_tagPool_ctl_a_href.release();
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
      _jspx_th_rn_page_0.setTitle("Replication Status");
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
          out.write("\n  <p class=\"pageInstructions\">\n    This page is intended for debugging purposes only.  The information below\n    describes this site's replication status with respect to the rest of the\n    Reciprocal Net Site Network.\n  </p>\n  <p class=\"headerFont1\">Sites table:</p>\n  <table class=\"adminFormTable\" border=\"0\" cellspacing=\"0\">\n    <tr class='rowColorfalse'>\n      <th class=\"threeColumnLeft\">ID</th>\n      <th class=\"threeColumnMiddle\">Name</th>\n      <th class=\"threeColumnMiddle\">Short Name</th>\n      <th class=\"threeColumnMiddle\">Public Seq. Num.</th>\n      <th class=\"threeColumnMiddle\">Private Seq. Num.</th>\n      <th class=\"threeColumnMiddle\">Final Seq. Num.</th>\n      <th class=\"threeColumnMiddle\">Active</th>\n      <th class=\"threeColumnRight\">Base URL</th>\n    </tr>\n    ");
          //  rn:siteIterator
          org.recipnet.site.content.rncontrols.SiteIterator sit = null;
          org.recipnet.site.content.rncontrols.SiteIterator _jspx_th_rn_siteIterator_0 = (org.recipnet.site.content.rncontrols.SiteIterator) _jspx_tagPool_rn_siteIterator_id.get(org.recipnet.site.content.rncontrols.SiteIterator.class);
          _jspx_th_rn_siteIterator_0.setPageContext(_jspx_page_context);
          _jspx_th_rn_siteIterator_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_page_0);
          _jspx_th_rn_siteIterator_0.setId("sit");
          int _jspx_eval_rn_siteIterator_0 = _jspx_th_rn_siteIterator_0.doStartTag();
          if (_jspx_eval_rn_siteIterator_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_rn_siteIterator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_rn_siteIterator_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_rn_siteIterator_0.doInitBody();
            }
            sit = (org.recipnet.site.content.rncontrols.SiteIterator) _jspx_page_context.findAttribute("sit");
            do {
              out.write("\n      <tr class=\"");
              out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${rn:testParity(sit.iterationCountSinceThisPhaseBegan,\n                                     'rowColortrue', 'rowColorfalse')}", java.lang.String.class, (PageContext)_jspx_page_context, _jspx_fnmap_0, false));
              out.write("\">\n        <td class=\"threeColumnLeft\">\n          ");
              //  rn:siteField
              org.recipnet.site.content.rncontrols.SiteField _jspx_th_rn_siteField_0 = (org.recipnet.site.content.rncontrols.SiteField) _jspx_tagPool_rn_siteField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SiteField.class);
              _jspx_th_rn_siteField_0.setPageContext(_jspx_page_context);
              _jspx_th_rn_siteField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_siteIterator_0);
              _jspx_th_rn_siteField_0.setFieldCode(SiteField.ID);
              int _jspx_eval_rn_siteField_0 = _jspx_th_rn_siteField_0.doStartTag();
              if (_jspx_th_rn_siteField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_siteField_fieldCode_nobody.reuse(_jspx_th_rn_siteField_0);
              out.write("\n        </td>\n        <td class=\"threeColumnMiddle\">\n          ");
              //  rn:siteField
              org.recipnet.site.content.rncontrols.SiteField _jspx_th_rn_siteField_1 = (org.recipnet.site.content.rncontrols.SiteField) _jspx_tagPool_rn_siteField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SiteField.class);
              _jspx_th_rn_siteField_1.setPageContext(_jspx_page_context);
              _jspx_th_rn_siteField_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_siteIterator_0);
              _jspx_th_rn_siteField_1.setFieldCode(SiteField.NAME);
              int _jspx_eval_rn_siteField_1 = _jspx_th_rn_siteField_1.doStartTag();
              if (_jspx_th_rn_siteField_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_siteField_fieldCode_nobody.reuse(_jspx_th_rn_siteField_1);
              out.write("\n        </td>\n        <td class=\"threeColumnMiddle\">\n          ");
              //  rn:siteField
              org.recipnet.site.content.rncontrols.SiteField _jspx_th_rn_siteField_2 = (org.recipnet.site.content.rncontrols.SiteField) _jspx_tagPool_rn_siteField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SiteField.class);
              _jspx_th_rn_siteField_2.setPageContext(_jspx_page_context);
              _jspx_th_rn_siteField_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_siteIterator_0);
              _jspx_th_rn_siteField_2.setFieldCode(SiteField.SHORT_NAME);
              int _jspx_eval_rn_siteField_2 = _jspx_th_rn_siteField_2.doStartTag();
              if (_jspx_th_rn_siteField_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_siteField_fieldCode_nobody.reuse(_jspx_th_rn_siteField_2);
              out.write("\n        </td>\n        <td class=\"threeColumnMiddle\">\n          ");
              //  rn:siteField
              org.recipnet.site.content.rncontrols.SiteField _jspx_th_rn_siteField_3 = (org.recipnet.site.content.rncontrols.SiteField) _jspx_tagPool_rn_siteField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SiteField.class);
              _jspx_th_rn_siteField_3.setPageContext(_jspx_page_context);
              _jspx_th_rn_siteField_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_siteIterator_0);
              _jspx_th_rn_siteField_3.setFieldCode(SiteField.PUBLIC_SEQ_NUM);
              int _jspx_eval_rn_siteField_3 = _jspx_th_rn_siteField_3.doStartTag();
              if (_jspx_th_rn_siteField_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_siteField_fieldCode_nobody.reuse(_jspx_th_rn_siteField_3);
              out.write("\n        </td>\n        <td class=\"threeColumnMiddle\">\n          ");
              //  rn:siteField
              org.recipnet.site.content.rncontrols.SiteField _jspx_th_rn_siteField_4 = (org.recipnet.site.content.rncontrols.SiteField) _jspx_tagPool_rn_siteField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SiteField.class);
              _jspx_th_rn_siteField_4.setPageContext(_jspx_page_context);
              _jspx_th_rn_siteField_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_siteIterator_0);
              _jspx_th_rn_siteField_4.setFieldCode(SiteField.PRIVATE_SEQ_NUM);
              int _jspx_eval_rn_siteField_4 = _jspx_th_rn_siteField_4.doStartTag();
              if (_jspx_th_rn_siteField_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_siteField_fieldCode_nobody.reuse(_jspx_th_rn_siteField_4);
              out.write("\n        </td>\n        <td class=\"threeColumnMiddle\">\n          ");
              //  rn:siteField
              org.recipnet.site.content.rncontrols.SiteField _jspx_th_rn_siteField_5 = (org.recipnet.site.content.rncontrols.SiteField) _jspx_tagPool_rn_siteField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SiteField.class);
              _jspx_th_rn_siteField_5.setPageContext(_jspx_page_context);
              _jspx_th_rn_siteField_5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_siteIterator_0);
              _jspx_th_rn_siteField_5.setFieldCode(SiteField.FINAL_SEQ_NUM);
              int _jspx_eval_rn_siteField_5 = _jspx_th_rn_siteField_5.doStartTag();
              if (_jspx_th_rn_siteField_5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_siteField_fieldCode_nobody.reuse(_jspx_th_rn_siteField_5);
              out.write("\n        </td>\n        <td class=\"threeColumnMiddle\">\n          ");
              //  rn:siteField
              org.recipnet.site.content.rncontrols.SiteField _jspx_th_rn_siteField_6 = (org.recipnet.site.content.rncontrols.SiteField) _jspx_tagPool_rn_siteField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SiteField.class);
              _jspx_th_rn_siteField_6.setPageContext(_jspx_page_context);
              _jspx_th_rn_siteField_6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_siteIterator_0);
              _jspx_th_rn_siteField_6.setFieldCode(SiteField.IS_ACTIVE);
              int _jspx_eval_rn_siteField_6 = _jspx_th_rn_siteField_6.doStartTag();
              if (_jspx_th_rn_siteField_6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_siteField_fieldCode_nobody.reuse(_jspx_th_rn_siteField_6);
              out.write("\n        </td>\n        <td class=\"threeColumnRight\">\n          ");
              //  rn:siteField
              org.recipnet.site.content.rncontrols.SiteField _jspx_th_rn_siteField_7 = (org.recipnet.site.content.rncontrols.SiteField) _jspx_tagPool_rn_siteField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SiteField.class);
              _jspx_th_rn_siteField_7.setPageContext(_jspx_page_context);
              _jspx_th_rn_siteField_7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_siteIterator_0);
              _jspx_th_rn_siteField_7.setFieldCode(SiteField.BASE_URL);
              int _jspx_eval_rn_siteField_7 = _jspx_th_rn_siteField_7.doStartTag();
              if (_jspx_th_rn_siteField_7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_siteField_fieldCode_nobody.reuse(_jspx_th_rn_siteField_7);
              out.write("\n        </td>\n      </tr>\n    ");
              int evalDoAfterBody = _jspx_th_rn_siteIterator_0.doAfterBody();
              sit = (org.recipnet.site.content.rncontrols.SiteIterator) _jspx_page_context.findAttribute("sit");
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
            if (_jspx_eval_rn_siteIterator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
              out = _jspx_page_context.popBody();
          }
          if (_jspx_th_rn_siteIterator_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
            return;
          sit = (org.recipnet.site.content.rncontrols.SiteIterator) _jspx_page_context.findAttribute("sit");
          _jspx_tagPool_rn_siteIterator_id.reuse(_jspx_th_rn_siteIterator_0);
          out.write("\n  </table>\n  <br />&nbsp;<br />\n  <center>\n    ");
          if (_jspx_meth_ctl_a_0(_jspx_th_rn_page_0, _jspx_page_context))
            return;
          out.write("\n  </center>\n  ");
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

  private boolean _jspx_meth_ctl_a_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_page_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:a
    org.recipnet.common.controls.LinkHtmlElement _jspx_th_ctl_a_0 = (org.recipnet.common.controls.LinkHtmlElement) _jspx_tagPool_ctl_a_href.get(org.recipnet.common.controls.LinkHtmlElement.class);
    _jspx_th_ctl_a_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_a_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_page_0);
    _jspx_th_ctl_a_0.setHref("/admin/index.jsp");
    int _jspx_eval_ctl_a_0 = _jspx_th_ctl_a_0.doStartTag();
    if (_jspx_eval_ctl_a_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_a_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_a_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_a_0.doInitBody();
      }
      do {
        out.write("Back to the Administration Tools");
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
        out.write("\n    .headerFont1  { font-size:medium; font-weight: bold; }\n    .rowColorfalse { background-color: #E6E6E6 }\n    .rowColortrue  { background-color: #FFFFFF }\n    .threeColumnLeft   { text-align: left; padding: 0.05in; \n                         border-style: solid; border-width: thin; \n                         border-color: #CCCCCC; }\n    .threeColumnMiddle { text-align: left; padding: 0.05in; \n                         border-style: solid; border-width: thin; \n                         border-color: #CCCCCC; }\n    .threeColumnRight  { text-align: left; padding: 0.05in; \n                         border-style: solid; border-width: thin; \n                         border-color: #CCCCCC; }\n    .adminFormTable { border-style: solid; border-width: thin;\n                      border-color: #CCCCCC; padding: 0.01in;}\n  ");
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
