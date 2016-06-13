package org.recipnet.site.content.admin;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.recipnet.site.content.rncontrols.LabField;
import org.recipnet.site.content.rncontrols.ProviderField;
import org.recipnet.site.content.rncontrols.ProviderPage;

public final class addprovider_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static java.util.Vector _jspx_dependants;

  static {
    _jspx_dependants = new java.util.Vector(2);
    _jspx_dependants.add("/WEB-INF/controls.tld");
    _jspx_dependants.add("/WEB-INF/rncontrols.tld");
  }

  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_providerPage_title_providerIdParamName_manageProvidersPageUrl_loginPageUrl_labIdParamName_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_selfForm;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_labField_fieldCode_displayAsLabel_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_labField_fieldCode_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_providerField_fieldCode_editable_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_errorMessage_errorFilter;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_providerActionButton_providerFunction_label_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_styleBlock;

  public java.util.List getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _jspx_tagPool_rn_providerPage_title_providerIdParamName_manageProvidersPageUrl_loginPageUrl_labIdParamName_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_selfForm = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_labField_fieldCode_displayAsLabel_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_labField_fieldCode_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_providerField_fieldCode_editable_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_errorMessage_errorFilter = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_providerActionButton_providerFunction_label_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_styleBlock = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
  }

  public void _jspDestroy() {
    _jspx_tagPool_rn_providerPage_title_providerIdParamName_manageProvidersPageUrl_loginPageUrl_labIdParamName_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName.release();
    _jspx_tagPool_ctl_selfForm.release();
    _jspx_tagPool_rn_labField_fieldCode_displayAsLabel_nobody.release();
    _jspx_tagPool_rn_labField_fieldCode_nobody.release();
    _jspx_tagPool_rn_providerField_fieldCode_editable_nobody.release();
    _jspx_tagPool_ctl_errorMessage_errorFilter.release();
    _jspx_tagPool_rn_providerActionButton_providerFunction_label_nobody.release();
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
      //  rn:providerPage
      org.recipnet.site.content.rncontrols.ProviderPage _jspx_th_rn_providerPage_0 = (org.recipnet.site.content.rncontrols.ProviderPage) _jspx_tagPool_rn_providerPage_title_providerIdParamName_manageProvidersPageUrl_loginPageUrl_labIdParamName_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName.get(org.recipnet.site.content.rncontrols.ProviderPage.class);
      _jspx_th_rn_providerPage_0.setPageContext(_jspx_page_context);
      _jspx_th_rn_providerPage_0.setParent(null);
      _jspx_th_rn_providerPage_0.setTitle("Add Provider");
      _jspx_th_rn_providerPage_0.setLabIdParamName("labId");
      _jspx_th_rn_providerPage_0.setProviderIdParamName("providerId");
      _jspx_th_rn_providerPage_0.setManageProvidersPageUrl("/admin/manageproviders.jsp");
      _jspx_th_rn_providerPage_0.setLoginPageUrl("/login.jsp");
      _jspx_th_rn_providerPage_0.setCurrentPageReinvocationUrlParamName("origUrl");
      _jspx_th_rn_providerPage_0.setAuthorizationFailedReasonParamName("authorizationFailedReason");
      int _jspx_eval_rn_providerPage_0 = _jspx_th_rn_providerPage_0.doStartTag();
      if (_jspx_eval_rn_providerPage_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        org.recipnet.site.content.rncontrols.ProviderPage htmlPage = null;
        if (_jspx_eval_rn_providerPage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
          out = _jspx_page_context.pushBody();
          _jspx_th_rn_providerPage_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
          _jspx_th_rn_providerPage_0.doInitBody();
        }
        htmlPage = (org.recipnet.site.content.rncontrols.ProviderPage) _jspx_page_context.findAttribute("htmlPage");
        do {
          out.write("\n  <br />\n  ");
          //  ctl:selfForm
          org.recipnet.common.controls.FormHtmlElement _jspx_th_ctl_selfForm_0 = (org.recipnet.common.controls.FormHtmlElement) _jspx_tagPool_ctl_selfForm.get(org.recipnet.common.controls.FormHtmlElement.class);
          _jspx_th_ctl_selfForm_0.setPageContext(_jspx_page_context);
          _jspx_th_ctl_selfForm_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_providerPage_0);
          int _jspx_eval_ctl_selfForm_0 = _jspx_th_ctl_selfForm_0.doStartTag();
          if (_jspx_eval_ctl_selfForm_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_ctl_selfForm_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_ctl_selfForm_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_ctl_selfForm_0.doInitBody();
            }
            do {
              out.write("\n    <table class=\"adminFormTable\">\n      <tr>\n        <th class=\"twoColumnLeft\">Lab Name (ID):</th>\n        <td  class=\"twoColumnRight\">\n          ");
              //  rn:labField
              org.recipnet.site.content.rncontrols.LabField _jspx_th_rn_labField_0 = (org.recipnet.site.content.rncontrols.LabField) _jspx_tagPool_rn_labField_fieldCode_displayAsLabel_nobody.get(org.recipnet.site.content.rncontrols.LabField.class);
              _jspx_th_rn_labField_0.setPageContext(_jspx_page_context);
              _jspx_th_rn_labField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_labField_0.setFieldCode(LabField.NAME);
              _jspx_th_rn_labField_0.setDisplayAsLabel(true);
              int _jspx_eval_rn_labField_0 = _jspx_th_rn_labField_0.doStartTag();
              if (_jspx_th_rn_labField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_labField_fieldCode_displayAsLabel_nobody.reuse(_jspx_th_rn_labField_0);
              out.write("\n          (");
              //  rn:labField
              org.recipnet.site.content.rncontrols.LabField _jspx_th_rn_labField_1 = (org.recipnet.site.content.rncontrols.LabField) _jspx_tagPool_rn_labField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.LabField.class);
              _jspx_th_rn_labField_1.setPageContext(_jspx_page_context);
              _jspx_th_rn_labField_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_labField_1.setFieldCode(LabField.ID);
              int _jspx_eval_rn_labField_1 = _jspx_th_rn_labField_1.doStartTag();
              if (_jspx_th_rn_labField_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_labField_fieldCode_nobody.reuse(_jspx_th_rn_labField_1);
              out.write(")\n        </td>\n      </tr>\n      <tr>\n        <th class=\"twoColumnLeft\">Provider Name:</th>\n        <td class=\"twoColumnRight\">\n          ");
              //  rn:providerField
              org.recipnet.site.content.rncontrols.ProviderField _jspx_th_rn_providerField_0 = (org.recipnet.site.content.rncontrols.ProviderField) _jspx_tagPool_rn_providerField_fieldCode_editable_nobody.get(org.recipnet.site.content.rncontrols.ProviderField.class);
              _jspx_th_rn_providerField_0.setPageContext(_jspx_page_context);
              _jspx_th_rn_providerField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_providerField_0.setFieldCode(ProviderField.NAME);
              _jspx_th_rn_providerField_0.setEditable(true);
              int _jspx_eval_rn_providerField_0 = _jspx_th_rn_providerField_0.doStartTag();
              if (_jspx_th_rn_providerField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_providerField_fieldCode_editable_nobody.reuse(_jspx_th_rn_providerField_0);
              out.write("\n        </td>\n      </tr>\n      <tr>\n        <th class=\"twoColumnLeft\">Provider Head Contact:</th>\n        <td class=\"twoColumnRight\">\n          ");
              //  rn:providerField
              org.recipnet.site.content.rncontrols.ProviderField _jspx_th_rn_providerField_1 = (org.recipnet.site.content.rncontrols.ProviderField) _jspx_tagPool_rn_providerField_fieldCode_editable_nobody.get(org.recipnet.site.content.rncontrols.ProviderField.class);
              _jspx_th_rn_providerField_1.setPageContext(_jspx_page_context);
              _jspx_th_rn_providerField_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_providerField_1.setFieldCode(ProviderField.HEAD_CONTACT);
              _jspx_th_rn_providerField_1.setEditable(true);
              int _jspx_eval_rn_providerField_1 = _jspx_th_rn_providerField_1.doStartTag();
              if (_jspx_th_rn_providerField_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_providerField_fieldCode_editable_nobody.reuse(_jspx_th_rn_providerField_1);
              out.write("\n        </td>\n      </tr>\n      <tr>\n        <th class=\"twoColumnLeft\">Comments:</th>\n        <td class=\"twoColumnRight\">\n          ");
              //  rn:providerField
              org.recipnet.site.content.rncontrols.ProviderField _jspx_th_rn_providerField_2 = (org.recipnet.site.content.rncontrols.ProviderField) _jspx_tagPool_rn_providerField_fieldCode_editable_nobody.get(org.recipnet.site.content.rncontrols.ProviderField.class);
              _jspx_th_rn_providerField_2.setPageContext(_jspx_page_context);
              _jspx_th_rn_providerField_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_providerField_2.setFieldCode(ProviderField.COMMENTS);
              _jspx_th_rn_providerField_2.setEditable(true);
              int _jspx_eval_rn_providerField_2 = _jspx_th_rn_providerField_2.doStartTag();
              if (_jspx_th_rn_providerField_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_providerField_fieldCode_editable_nobody.reuse(_jspx_th_rn_providerField_2);
              out.write("\n        </td>\n      </tr>\n      <tr>\n        <td colspan=\"2\">&nbsp;</td>\n      </tr>\n      ");
              //  ctl:errorMessage
              org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_0 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
              _jspx_th_ctl_errorMessage_0.setPageContext(_jspx_page_context);
              _jspx_th_ctl_errorMessage_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_ctl_errorMessage_0.setErrorFilter( ProviderPage.NESTED_TAG_REPORTED_VALIDATION_ERROR );
              int _jspx_eval_ctl_errorMessage_0 = _jspx_th_ctl_errorMessage_0.doStartTag();
              if (_jspx_eval_ctl_errorMessage_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_ctl_errorMessage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_ctl_errorMessage_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_ctl_errorMessage_0.doInitBody();
                }
                do {
                  out.write("\n        <tr>\n          <td colspan=\"2\" class=\"oneColumn\">\n            <div class=\"errorMessage\">\n              * Missing or invalid entry.&nbsp;&nbsp;\n              Please check these entries and\n              resubmit.\n            </div>\n          </td>\n        </tr>\n      ");
                  int evalDoAfterBody = _jspx_th_ctl_errorMessage_0.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_ctl_errorMessage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_ctl_errorMessage_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_ctl_errorMessage_errorFilter.reuse(_jspx_th_ctl_errorMessage_0);
              out.write("\n      <tr>\n        <td colspan=\"2\" class=\"oneColumn\">\n          ");
              //  rn:providerActionButton
              org.recipnet.site.content.rncontrols.ProviderActionButton _jspx_th_rn_providerActionButton_0 = (org.recipnet.site.content.rncontrols.ProviderActionButton) _jspx_tagPool_rn_providerActionButton_providerFunction_label_nobody.get(org.recipnet.site.content.rncontrols.ProviderActionButton.class);
              _jspx_th_rn_providerActionButton_0.setPageContext(_jspx_page_context);
              _jspx_th_rn_providerActionButton_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_providerActionButton_0.setProviderFunction(ProviderPage.ADD_NEW_PROVIDER);
              _jspx_th_rn_providerActionButton_0.setLabel("Add New Provider");
              int _jspx_eval_rn_providerActionButton_0 = _jspx_th_rn_providerActionButton_0.doStartTag();
              if (_jspx_th_rn_providerActionButton_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_providerActionButton_providerFunction_label_nobody.reuse(_jspx_th_rn_providerActionButton_0);
              out.write("\n          ");
              //  rn:providerActionButton
              org.recipnet.site.content.rncontrols.ProviderActionButton _jspx_th_rn_providerActionButton_1 = (org.recipnet.site.content.rncontrols.ProviderActionButton) _jspx_tagPool_rn_providerActionButton_providerFunction_label_nobody.get(org.recipnet.site.content.rncontrols.ProviderActionButton.class);
              _jspx_th_rn_providerActionButton_1.setPageContext(_jspx_page_context);
              _jspx_th_rn_providerActionButton_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_providerActionButton_1.setLabel("Cancel");
              _jspx_th_rn_providerActionButton_1.setProviderFunction(
                  ProviderPage.CANCEL_PROVIDER_FUNCTION);
              int _jspx_eval_rn_providerActionButton_1 = _jspx_th_rn_providerActionButton_1.doStartTag();
              if (_jspx_th_rn_providerActionButton_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_providerActionButton_providerFunction_label_nobody.reuse(_jspx_th_rn_providerActionButton_1);
              out.write("\n        </td>\n      </tr>\n    </table>\n  ");
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
          if (_jspx_meth_ctl_styleBlock_0(_jspx_th_rn_providerPage_0, _jspx_page_context))
            return;
          out.write('\n');
          int evalDoAfterBody = _jspx_th_rn_providerPage_0.doAfterBody();
          htmlPage = (org.recipnet.site.content.rncontrols.ProviderPage) _jspx_page_context.findAttribute("htmlPage");
          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
            break;
        } while (true);
        if (_jspx_eval_rn_providerPage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
          out = _jspx_page_context.popBody();
      }
      if (_jspx_th_rn_providerPage_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
        return;
      _jspx_tagPool_rn_providerPage_title_providerIdParamName_manageProvidersPageUrl_loginPageUrl_labIdParamName_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName.reuse(_jspx_th_rn_providerPage_0);
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

  private boolean _jspx_meth_ctl_styleBlock_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_providerPage_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:styleBlock
    org.recipnet.common.controls.HtmlPageStyleBlock _jspx_th_ctl_styleBlock_0 = (org.recipnet.common.controls.HtmlPageStyleBlock) _jspx_tagPool_ctl_styleBlock.get(org.recipnet.common.controls.HtmlPageStyleBlock.class);
    _jspx_th_ctl_styleBlock_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_styleBlock_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_providerPage_0);
    int _jspx_eval_ctl_styleBlock_0 = _jspx_th_ctl_styleBlock_0.doStartTag();
    if (_jspx_eval_ctl_styleBlock_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_styleBlock_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_styleBlock_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_styleBlock_0.doInitBody();
      }
      do {
        out.write("\n    .adminFormTable { border-style: solid; border-width: thin;\n            border-color: #CCCCCC; padding: 0.01in;}\n    .oneColumn { text-align: center; vertical-align: center; padding: 0.01in;\n            border-width: 0; }\n    .twoColumnLeft   { text-align: right; padding: 0.01in; border-width: 0;}\n    .twoColumnRight  { text-align: left;  padding: 0.01in;  border-width: 0;}\n    .errorMessage  { color: red; font-weight: normal; font-size: x-small; }\n  ");
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
