package org.recipnet.site.content.lab;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.recipnet.site.content.rncontrols.SampleHistoryField;
import org.recipnet.site.shared.db.SampleInfo;

public final class considerrevert_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static java.util.Vector _jspx_dependants;

  static {
    _jspx_dependants = new java.util.Vector(2);
    _jspx_dependants.add("/WEB-INF/controls.tld");
    _jspx_dependants.add("/WEB-INF/rncontrols.tld");
  }

  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_revertWapPage_title_loginPageUrl_editSamplePageHref_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_link_href;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_selfForm;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_wapCancelButton_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_form_method_action;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_button_label_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_buttonLink_target_label_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_styleBlock;

  public java.util.List getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _jspx_tagPool_rn_revertWapPage_title_loginPageUrl_editSamplePageHref_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_link_href = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_selfForm = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_wapCancelButton_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_form_method_action = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_button_label_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_buttonLink_target_label_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_styleBlock = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
  }

  public void _jspDestroy() {
    _jspx_tagPool_rn_revertWapPage_title_loginPageUrl_editSamplePageHref_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName.release();
    _jspx_tagPool_rn_link_href.release();
    _jspx_tagPool_ctl_selfForm.release();
    _jspx_tagPool_rn_wapCancelButton_nobody.release();
    _jspx_tagPool_ctl_form_method_action.release();
    _jspx_tagPool_ctl_button_label_nobody.release();
    _jspx_tagPool_rn_buttonLink_target_label_nobody.release();
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
      //  rn:revertWapPage
      org.recipnet.site.content.rncontrols.RevertWapPage _jspx_th_rn_revertWapPage_0 = (org.recipnet.site.content.rncontrols.RevertWapPage) _jspx_tagPool_rn_revertWapPage_title_loginPageUrl_editSamplePageHref_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName.get(org.recipnet.site.content.rncontrols.RevertWapPage.class);
      _jspx_th_rn_revertWapPage_0.setPageContext(_jspx_page_context);
      _jspx_th_rn_revertWapPage_0.setParent(null);
      _jspx_th_rn_revertWapPage_0.setTitle("Revert sample");
      _jspx_th_rn_revertWapPage_0.setEditSamplePageHref("/lab/samplehistory.jsp");
      _jspx_th_rn_revertWapPage_0.setLoginPageUrl("/login.jsp");
      _jspx_th_rn_revertWapPage_0.setCurrentPageReinvocationUrlParamName("origUrl");
      _jspx_th_rn_revertWapPage_0.setAuthorizationFailedReasonParamName("authorizationFailedReason");
      int _jspx_eval_rn_revertWapPage_0 = _jspx_th_rn_revertWapPage_0.doStartTag();
      if (_jspx_eval_rn_revertWapPage_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        org.recipnet.site.content.rncontrols.RevertWapPage htmlPage = null;
        if (_jspx_eval_rn_revertWapPage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
          out = _jspx_page_context.pushBody();
          _jspx_th_rn_revertWapPage_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
          _jspx_th_rn_revertWapPage_0.doInitBody();
        }
        htmlPage = (org.recipnet.site.content.rncontrols.RevertWapPage) _jspx_page_context.findAttribute("htmlPage");
        do {
          out.write("\n  <center>\n    <p class=\"errorMessage\">\n      Warning:&nbsp; You are about to revert this publicly visible sample to\n      a version that is not public.&nbsp;&nbsp;This action is strongly\n      discouraged because it may disrupt digital library harvesters or\n      invalidate third-party hyperlinks.&nbsp;&nbsp; A preferred approach is\n      to ");
          if (_jspx_meth_rn_link_0(_jspx_th_rn_revertWapPage_0, _jspx_page_context))
            return;
          out.write(" the sample\n      instead.\n    </p>\n    <table>\n      <tr>\n        <td>\n          ");
          if (_jspx_meth_ctl_selfForm_0(_jspx_th_rn_revertWapPage_0, _jspx_page_context))
            return;
          out.write("\n        </td>\n        <td>\n          ");
          if (_jspx_meth_ctl_form_0(_jspx_th_rn_revertWapPage_0, _jspx_page_context))
            return;
          out.write("\n        </td>\n        <td>\n          ");
          if (_jspx_meth_ctl_selfForm_1(_jspx_th_rn_revertWapPage_0, _jspx_page_context))
            return;
          out.write("\n        </td>\n      <tr>\n    </table>\n  </center>\n  ");
          if (_jspx_meth_ctl_styleBlock_0(_jspx_th_rn_revertWapPage_0, _jspx_page_context))
            return;
          out.write('\n');
          int evalDoAfterBody = _jspx_th_rn_revertWapPage_0.doAfterBody();
          htmlPage = (org.recipnet.site.content.rncontrols.RevertWapPage) _jspx_page_context.findAttribute("htmlPage");
          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
            break;
        } while (true);
        if (_jspx_eval_rn_revertWapPage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
          out = _jspx_page_context.popBody();
      }
      if (_jspx_th_rn_revertWapPage_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
        return;
      _jspx_tagPool_rn_revertWapPage_title_loginPageUrl_editSamplePageHref_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName.reuse(_jspx_th_rn_revertWapPage_0);
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

  private boolean _jspx_meth_rn_link_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_revertWapPage_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:link
    org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_0 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_href.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
    _jspx_th_rn_link_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_link_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_revertWapPage_0);
    _jspx_th_rn_link_0.setHref("/lab/retract.jsp");
    int _jspx_eval_rn_link_0 = _jspx_th_rn_link_0.doStartTag();
    if (_jspx_eval_rn_link_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_link_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_link_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_link_0.doInitBody();
      }
      do {
        out.write("retract");
        int evalDoAfterBody = _jspx_th_rn_link_0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_link_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_link_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_link_href.reuse(_jspx_th_rn_link_0);
    return false;
  }

  private boolean _jspx_meth_ctl_selfForm_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_revertWapPage_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:selfForm
    org.recipnet.common.controls.FormHtmlElement _jspx_th_ctl_selfForm_0 = (org.recipnet.common.controls.FormHtmlElement) _jspx_tagPool_ctl_selfForm.get(org.recipnet.common.controls.FormHtmlElement.class);
    _jspx_th_ctl_selfForm_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_selfForm_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_revertWapPage_0);
    int _jspx_eval_ctl_selfForm_0 = _jspx_th_ctl_selfForm_0.doStartTag();
    if (_jspx_eval_ctl_selfForm_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_selfForm_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_selfForm_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_selfForm_0.doInitBody();
      }
      do {
        out.write("\n            ");
        if (_jspx_meth_rn_wapCancelButton_0(_jspx_th_ctl_selfForm_0, _jspx_page_context))
          return true;
        out.write("\n          ");
        int evalDoAfterBody = _jspx_th_ctl_selfForm_0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_selfForm_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_selfForm_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_selfForm.reuse(_jspx_th_ctl_selfForm_0);
    return false;
  }

  private boolean _jspx_meth_rn_wapCancelButton_0(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_selfForm_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:wapCancelButton
    org.recipnet.site.content.rncontrols.WapCancelButton _jspx_th_rn_wapCancelButton_0 = (org.recipnet.site.content.rncontrols.WapCancelButton) _jspx_tagPool_rn_wapCancelButton_nobody.get(org.recipnet.site.content.rncontrols.WapCancelButton.class);
    _jspx_th_rn_wapCancelButton_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_wapCancelButton_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
    int _jspx_eval_rn_wapCancelButton_0 = _jspx_th_rn_wapCancelButton_0.doStartTag();
    if (_jspx_th_rn_wapCancelButton_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_wapCancelButton_nobody.reuse(_jspx_th_rn_wapCancelButton_0);
    return false;
  }

  private boolean _jspx_meth_ctl_form_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_revertWapPage_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:form
    org.recipnet.common.controls.FormHtmlElement _jspx_th_ctl_form_0 = (org.recipnet.common.controls.FormHtmlElement) _jspx_tagPool_ctl_form_method_action.get(org.recipnet.common.controls.FormHtmlElement.class);
    _jspx_th_ctl_form_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_form_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_revertWapPage_0);
    _jspx_th_ctl_form_0.setMethod("GET");
    _jspx_th_ctl_form_0.setAction("/lab/revert.jsp");
    int _jspx_eval_ctl_form_0 = _jspx_th_ctl_form_0.doStartTag();
    if (_jspx_eval_ctl_form_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_form_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_form_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_form_0.doInitBody();
      }
      do {
        out.write("\n            ");
        if (_jspx_meth_ctl_button_0(_jspx_th_ctl_form_0, _jspx_page_context))
          return true;
        out.write("\n          ");
        int evalDoAfterBody = _jspx_th_ctl_form_0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_form_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_form_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_form_method_action.reuse(_jspx_th_ctl_form_0);
    return false;
  }

  private boolean _jspx_meth_ctl_button_0(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_form_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:button
    org.recipnet.common.controls.ButtonHtmlControl _jspx_th_ctl_button_0 = (org.recipnet.common.controls.ButtonHtmlControl) _jspx_tagPool_ctl_button_label_nobody.get(org.recipnet.common.controls.ButtonHtmlControl.class);
    _jspx_th_ctl_button_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_button_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_form_0);
    _jspx_th_ctl_button_0.setLabel("Continue");
    int _jspx_eval_ctl_button_0 = _jspx_th_ctl_button_0.doStartTag();
    if (_jspx_th_ctl_button_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_button_label_nobody.reuse(_jspx_th_ctl_button_0);
    return false;
  }

  private boolean _jspx_meth_ctl_selfForm_1(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_revertWapPage_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:selfForm
    org.recipnet.common.controls.FormHtmlElement _jspx_th_ctl_selfForm_1 = (org.recipnet.common.controls.FormHtmlElement) _jspx_tagPool_ctl_selfForm.get(org.recipnet.common.controls.FormHtmlElement.class);
    _jspx_th_ctl_selfForm_1.setPageContext(_jspx_page_context);
    _jspx_th_ctl_selfForm_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_revertWapPage_0);
    int _jspx_eval_ctl_selfForm_1 = _jspx_th_ctl_selfForm_1.doStartTag();
    if (_jspx_eval_ctl_selfForm_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_selfForm_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_selfForm_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_selfForm_1.doInitBody();
      }
      do {
        out.write("\n            ");
        if (_jspx_meth_rn_buttonLink_0(_jspx_th_ctl_selfForm_1, _jspx_page_context))
          return true;
        out.write("\n          ");
        int evalDoAfterBody = _jspx_th_ctl_selfForm_1.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_selfForm_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_selfForm_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_selfForm.reuse(_jspx_th_ctl_selfForm_1);
    return false;
  }

  private boolean _jspx_meth_rn_buttonLink_0(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_selfForm_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:buttonLink
    org.recipnet.site.content.rncontrols.ContextPreservingButton _jspx_th_rn_buttonLink_0 = (org.recipnet.site.content.rncontrols.ContextPreservingButton) _jspx_tagPool_rn_buttonLink_target_label_nobody.get(org.recipnet.site.content.rncontrols.ContextPreservingButton.class);
    _jspx_th_rn_buttonLink_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_buttonLink_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_1);
    _jspx_th_rn_buttonLink_0.setLabel("Retract");
    _jspx_th_rn_buttonLink_0.setTarget("/lab/retract.jsp");
    int _jspx_eval_rn_buttonLink_0 = _jspx_th_rn_buttonLink_0.doStartTag();
    if (_jspx_th_rn_buttonLink_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_buttonLink_target_label_nobody.reuse(_jspx_th_rn_buttonLink_0);
    return false;
  }

  private boolean _jspx_meth_ctl_styleBlock_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_revertWapPage_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:styleBlock
    org.recipnet.common.controls.HtmlPageStyleBlock _jspx_th_ctl_styleBlock_0 = (org.recipnet.common.controls.HtmlPageStyleBlock) _jspx_tagPool_ctl_styleBlock.get(org.recipnet.common.controls.HtmlPageStyleBlock.class);
    _jspx_th_ctl_styleBlock_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_styleBlock_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_revertWapPage_0);
    int _jspx_eval_ctl_styleBlock_0 = _jspx_th_ctl_styleBlock_0.doStartTag();
    if (_jspx_eval_ctl_styleBlock_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_styleBlock_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_styleBlock_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_styleBlock_0.doInitBody();
      }
      do {
        out.write("\n    .navLinksRight { text-align: right; }\n    .errorMessage  { color: red; font-weight: bold;}\n  ");
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
