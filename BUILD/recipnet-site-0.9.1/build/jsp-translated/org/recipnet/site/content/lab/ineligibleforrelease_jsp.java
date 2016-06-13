package org.recipnet.site.content.lab;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.recipnet.site.shared.bl.SampleTextBL;
import org.recipnet.site.shared.bl.SampleWorkflowBL;
import org.recipnet.site.content.rncontrols.SampleHistoryField;
import org.recipnet.site.content.rncontrols.WapPage;

public final class ineligibleforrelease_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static java.util.Vector _jspx_dependants;

  static {
    _jspx_dependants = new java.util.Vector(2);
    _jspx_dependants.add("/WEB-INF/controls.tld");
    _jspx_dependants.add("/WEB-INF/rncontrols.tld");
  }

  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_samplePage_title_loginPageUrl_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_selfForm;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleTextIterator_restrictByTextType;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleField_displayAsLabel_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleHistoryField_fieldCode_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_buttonLink_target_label_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_styleBlock;

  public java.util.List getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _jspx_tagPool_rn_samplePage_title_loginPageUrl_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_selfForm = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleTextIterator_restrictByTextType = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleField_displayAsLabel_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleHistoryField_fieldCode_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_buttonLink_target_label_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_styleBlock = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
  }

  public void _jspDestroy() {
    _jspx_tagPool_rn_samplePage_title_loginPageUrl_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName.release();
    _jspx_tagPool_ctl_selfForm.release();
    _jspx_tagPool_rn_sampleTextIterator_restrictByTextType.release();
    _jspx_tagPool_rn_sampleField_displayAsLabel_nobody.release();
    _jspx_tagPool_rn_sampleHistoryField_fieldCode_nobody.release();
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

      out.write("\n\n\n\n");
      //  rn:samplePage
      org.recipnet.site.content.rncontrols.SamplePage _jspx_th_rn_samplePage_0 = (org.recipnet.site.content.rncontrols.SamplePage) _jspx_tagPool_rn_samplePage_title_loginPageUrl_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName.get(org.recipnet.site.content.rncontrols.SamplePage.class);
      _jspx_th_rn_samplePage_0.setPageContext(_jspx_page_context);
      _jspx_th_rn_samplePage_0.setParent(null);
      _jspx_th_rn_samplePage_0.setTitle("Release Sample to Public");
      _jspx_th_rn_samplePage_0.setLoginPageUrl("/login.jsp");
      _jspx_th_rn_samplePage_0.setCurrentPageReinvocationUrlParamName("origUrl");
      _jspx_th_rn_samplePage_0.setAuthorizationFailedReasonParamName("authorizationFailedReason");
      int _jspx_eval_rn_samplePage_0 = _jspx_th_rn_samplePage_0.doStartTag();
      if (_jspx_eval_rn_samplePage_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        org.recipnet.site.content.rncontrols.SamplePage samplePage = null;
        if (_jspx_eval_rn_samplePage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
          out = _jspx_page_context.pushBody();
          _jspx_th_rn_samplePage_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
          _jspx_th_rn_samplePage_0.doInitBody();
        }
        samplePage = (org.recipnet.site.content.rncontrols.SamplePage) _jspx_page_context.findAttribute("samplePage");
        do {
          out.write("\n  <p class=\"errorMessage\">\n    <font size=\"+1\">\n      This sample has been flagged ineligible for release for the following\n      reason(s):\n    </font>\n  </p>\n  <br />\n  ");
          //  ctl:selfForm
          org.recipnet.common.controls.FormHtmlElement _jspx_th_ctl_selfForm_0 = (org.recipnet.common.controls.FormHtmlElement) _jspx_tagPool_ctl_selfForm.get(org.recipnet.common.controls.FormHtmlElement.class);
          _jspx_th_ctl_selfForm_0.setPageContext(_jspx_page_context);
          _jspx_th_ctl_selfForm_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_samplePage_0);
          int _jspx_eval_ctl_selfForm_0 = _jspx_th_ctl_selfForm_0.doStartTag();
          if (_jspx_eval_ctl_selfForm_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_ctl_selfForm_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_ctl_selfForm_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_ctl_selfForm_0.doInitBody();
            }
            do {
              out.write("\n    <ul>\n      ");
              //  rn:sampleTextIterator
              org.recipnet.site.content.rncontrols.SampleTextIterator _jspx_th_rn_sampleTextIterator_0 = (org.recipnet.site.content.rncontrols.SampleTextIterator) _jspx_tagPool_rn_sampleTextIterator_restrictByTextType.get(org.recipnet.site.content.rncontrols.SampleTextIterator.class);
              _jspx_th_rn_sampleTextIterator_0.setPageContext(_jspx_page_context);
              _jspx_th_rn_sampleTextIterator_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_sampleTextIterator_0.setRestrictByTextType(SampleTextBL.INELIGIBLE_FOR_RELEASE);
              int _jspx_eval_rn_sampleTextIterator_0 = _jspx_th_rn_sampleTextIterator_0.doStartTag();
              if (_jspx_eval_rn_sampleTextIterator_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_rn_sampleTextIterator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_rn_sampleTextIterator_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_rn_sampleTextIterator_0.doInitBody();
                }
                do {
                  out.write("\n        <li>\n          ");
                  if (_jspx_meth_rn_sampleField_0(_jspx_th_rn_sampleTextIterator_0, _jspx_page_context))
                    return;
                  out.write("\n          <br />\n          <span class=\"ancillaryText\">\n            ");
                  //  rn:sampleHistoryField
                  org.recipnet.site.content.rncontrols.SampleHistoryField _jspx_th_rn_sampleHistoryField_0 = (org.recipnet.site.content.rncontrols.SampleHistoryField) _jspx_tagPool_rn_sampleHistoryField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleHistoryField.class);
                  _jspx_th_rn_sampleHistoryField_0.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleHistoryField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleTextIterator_0);
                  _jspx_th_rn_sampleHistoryField_0.setFieldCode(
                    SampleHistoryField.FieldCode.USER_FULLNAME_THAT_PERFORMED_ACTION);
                  int _jspx_eval_rn_sampleHistoryField_0 = _jspx_th_rn_sampleHistoryField_0.doStartTag();
                  if (_jspx_th_rn_sampleHistoryField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_sampleHistoryField_fieldCode_nobody.reuse(_jspx_th_rn_sampleHistoryField_0);
                  out.write("\n            on ");
                  //  rn:sampleHistoryField
                  org.recipnet.site.content.rncontrols.SampleHistoryField _jspx_th_rn_sampleHistoryField_1 = (org.recipnet.site.content.rncontrols.SampleHistoryField) _jspx_tagPool_rn_sampleHistoryField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleHistoryField.class);
                  _jspx_th_rn_sampleHistoryField_1.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleHistoryField_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleTextIterator_0);
                  _jspx_th_rn_sampleHistoryField_1.setFieldCode(
                    SampleHistoryField.FieldCode.ACTION_DATE);
                  int _jspx_eval_rn_sampleHistoryField_1 = _jspx_th_rn_sampleHistoryField_1.doStartTag();
                  if (_jspx_th_rn_sampleHistoryField_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_sampleHistoryField_fieldCode_nobody.reuse(_jspx_th_rn_sampleHistoryField_1);
                  out.write("\n          </span>\n        </li>\n        <br />\n        <br />\n      ");
                  int evalDoAfterBody = _jspx_th_rn_sampleTextIterator_0.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_rn_sampleTextIterator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_rn_sampleTextIterator_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_sampleTextIterator_restrictByTextType.reuse(_jspx_th_rn_sampleTextIterator_0);
              out.write("\n    </ul>\n    This sample cannot be released to the public until the errors above are\n    corrected.\n    <blockquote>\n      <table>\n        <tr>\n          <td>\n            ");
              if (_jspx_meth_rn_buttonLink_0(_jspx_th_ctl_selfForm_0, _jspx_page_context))
                return;
              out.write("\n          </td>\n          <td>\n            ");
              if (_jspx_meth_rn_buttonLink_1(_jspx_th_ctl_selfForm_0, _jspx_page_context))
                return;
              out.write("\n          </td>\n        </tr>\n      </table>\n    </blockquote>\n  ");
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
          if (_jspx_meth_ctl_styleBlock_0(_jspx_th_rn_samplePage_0, _jspx_page_context))
            return;
          out.write('\n');
          int evalDoAfterBody = _jspx_th_rn_samplePage_0.doAfterBody();
          samplePage = (org.recipnet.site.content.rncontrols.SamplePage) _jspx_page_context.findAttribute("samplePage");
          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
            break;
        } while (true);
        if (_jspx_eval_rn_samplePage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
          out = _jspx_page_context.popBody();
      }
      if (_jspx_th_rn_samplePage_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
        return;
      _jspx_tagPool_rn_samplePage_title_loginPageUrl_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName.reuse(_jspx_th_rn_samplePage_0);
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

  private boolean _jspx_meth_rn_sampleField_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleTextIterator_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:sampleField
    org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_0 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_displayAsLabel_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
    _jspx_th_rn_sampleField_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_sampleField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleTextIterator_0);
    _jspx_th_rn_sampleField_0.setDisplayAsLabel(true);
    int _jspx_eval_rn_sampleField_0 = _jspx_th_rn_sampleField_0.doStartTag();
    if (_jspx_th_rn_sampleField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_sampleField_displayAsLabel_nobody.reuse(_jspx_th_rn_sampleField_0);
    return false;
  }

  private boolean _jspx_meth_rn_buttonLink_0(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_selfForm_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:buttonLink
    org.recipnet.site.content.rncontrols.ContextPreservingButton _jspx_th_rn_buttonLink_0 = (org.recipnet.site.content.rncontrols.ContextPreservingButton) _jspx_tagPool_rn_buttonLink_target_label_nobody.get(org.recipnet.site.content.rncontrols.ContextPreservingButton.class);
    _jspx_th_rn_buttonLink_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_buttonLink_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
    _jspx_th_rn_buttonLink_0.setTarget("/lab/sample.jsp");
    _jspx_th_rn_buttonLink_0.setLabel("Cancel");
    int _jspx_eval_rn_buttonLink_0 = _jspx_th_rn_buttonLink_0.doStartTag();
    if (_jspx_th_rn_buttonLink_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_buttonLink_target_label_nobody.reuse(_jspx_th_rn_buttonLink_0);
    return false;
  }

  private boolean _jspx_meth_rn_buttonLink_1(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_selfForm_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:buttonLink
    org.recipnet.site.content.rncontrols.ContextPreservingButton _jspx_th_rn_buttonLink_1 = (org.recipnet.site.content.rncontrols.ContextPreservingButton) _jspx_tagPool_rn_buttonLink_target_label_nobody.get(org.recipnet.site.content.rncontrols.ContextPreservingButton.class);
    _jspx_th_rn_buttonLink_1.setPageContext(_jspx_page_context);
    _jspx_th_rn_buttonLink_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
    _jspx_th_rn_buttonLink_1.setTarget("/lab/modifytext.jsp");
    _jspx_th_rn_buttonLink_1.setLabel("Modify text ...");
    int _jspx_eval_rn_buttonLink_1 = _jspx_th_rn_buttonLink_1.doStartTag();
    if (_jspx_th_rn_buttonLink_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_buttonLink_target_label_nobody.reuse(_jspx_th_rn_buttonLink_1);
    return false;
  }

  private boolean _jspx_meth_ctl_styleBlock_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_samplePage_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:styleBlock
    org.recipnet.common.controls.HtmlPageStyleBlock _jspx_th_ctl_styleBlock_0 = (org.recipnet.common.controls.HtmlPageStyleBlock) _jspx_tagPool_ctl_styleBlock.get(org.recipnet.common.controls.HtmlPageStyleBlock.class);
    _jspx_th_ctl_styleBlock_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_styleBlock_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_samplePage_0);
    int _jspx_eval_ctl_styleBlock_0 = _jspx_th_ctl_styleBlock_0.doStartTag();
    if (_jspx_eval_ctl_styleBlock_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_styleBlock_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_styleBlock_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_styleBlock_0.doInitBody();
      }
      do {
        out.write("\n    .errorMessage  { color: red; font-weight: bold; }\n    .ancillaryText { font-size: x-small; color: #444444; }\n  ");
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
