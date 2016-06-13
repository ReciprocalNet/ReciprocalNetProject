package org.recipnet.site.content.admin;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.recipnet.site.content.rncontrols.ProviderContext;
import org.recipnet.site.content.rncontrols.ProviderIterator;
import org.recipnet.site.content.rncontrols.ProviderField;
import org.recipnet.site.content.rncontrols.LabContext;

public final class manageproviders_jsp extends org.apache.jasper.runtime.HttpJspBase
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

  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_labPage_title_loginPageUrl_labIdParamName_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_selfForm;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_labChecker_requireActive;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_labField_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_providerIterator_restrictToActiveProviders_id;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_providerField_fieldCode_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_link_href_contextType;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_providerIterator_restrictToInactiveProviders_id;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_parityChecker_invert_includeOnlyOnFirst;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_userField_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_a_href;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_styleBlock;

  public java.util.List getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _jspx_tagPool_rn_labPage_title_loginPageUrl_labIdParamName_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_selfForm = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_labChecker_requireActive = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_labField_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_providerIterator_restrictToActiveProviders_id = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_providerField_fieldCode_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_link_href_contextType = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_providerIterator_restrictToInactiveProviders_id = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_parityChecker_invert_includeOnlyOnFirst = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_userField_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_a_href = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_styleBlock = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
  }

  public void _jspDestroy() {
    _jspx_tagPool_rn_labPage_title_loginPageUrl_labIdParamName_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName.release();
    _jspx_tagPool_ctl_selfForm.release();
    _jspx_tagPool_rn_labChecker_requireActive.release();
    _jspx_tagPool_rn_labField_nobody.release();
    _jspx_tagPool_rn_providerIterator_restrictToActiveProviders_id.release();
    _jspx_tagPool_rn_providerField_fieldCode_nobody.release();
    _jspx_tagPool_rn_link_href_contextType.release();
    _jspx_tagPool_rn_providerIterator_restrictToInactiveProviders_id.release();
    _jspx_tagPool_ctl_parityChecker_invert_includeOnlyOnFirst.release();
    _jspx_tagPool_rn_userField_nobody.release();
    _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.release();
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

      out.write("\n\n\n\n\n\n\n");
      //  rn:labPage
      org.recipnet.site.content.rncontrols.LabPage _jspx_th_rn_labPage_0 = (org.recipnet.site.content.rncontrols.LabPage) _jspx_tagPool_rn_labPage_title_loginPageUrl_labIdParamName_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName.get(org.recipnet.site.content.rncontrols.LabPage.class);
      _jspx_th_rn_labPage_0.setPageContext(_jspx_page_context);
      _jspx_th_rn_labPage_0.setParent(null);
      _jspx_th_rn_labPage_0.setLabIdParamName("labId");
      _jspx_th_rn_labPage_0.setTitle("Manage Providers");
      _jspx_th_rn_labPage_0.setLoginPageUrl("/login.jsp");
      _jspx_th_rn_labPage_0.setCurrentPageReinvocationUrlParamName("origUrl");
      _jspx_th_rn_labPage_0.setAuthorizationFailedReasonParamName("authorizationFailedReason");
      int _jspx_eval_rn_labPage_0 = _jspx_th_rn_labPage_0.doStartTag();
      if (_jspx_eval_rn_labPage_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        org.recipnet.site.content.rncontrols.LabPage htmlPage = null;
        if (_jspx_eval_rn_labPage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
          out = _jspx_page_context.pushBody();
          _jspx_th_rn_labPage_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
          _jspx_th_rn_labPage_0.doInitBody();
        }
        htmlPage = (org.recipnet.site.content.rncontrols.LabPage) _jspx_page_context.findAttribute("htmlPage");
        do {
          out.write('\n');
          out.write(' ');
          out.write(' ');
          //  ctl:selfForm
          org.recipnet.common.controls.FormHtmlElement _jspx_th_ctl_selfForm_0 = (org.recipnet.common.controls.FormHtmlElement) _jspx_tagPool_ctl_selfForm.get(org.recipnet.common.controls.FormHtmlElement.class);
          _jspx_th_ctl_selfForm_0.setPageContext(_jspx_page_context);
          _jspx_th_ctl_selfForm_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_labPage_0);
          int _jspx_eval_ctl_selfForm_0 = _jspx_th_ctl_selfForm_0.doStartTag();
          if (_jspx_eval_ctl_selfForm_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_ctl_selfForm_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_ctl_selfForm_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_ctl_selfForm_0.doInitBody();
            }
            do {
              out.write("\n    ");
              //  rn:labChecker
              org.recipnet.site.content.rncontrols.LabChecker _jspx_th_rn_labChecker_0 = (org.recipnet.site.content.rncontrols.LabChecker) _jspx_tagPool_rn_labChecker_requireActive.get(org.recipnet.site.content.rncontrols.LabChecker.class);
              _jspx_th_rn_labChecker_0.setPageContext(_jspx_page_context);
              _jspx_th_rn_labChecker_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_labChecker_0.setRequireActive(true);
              int _jspx_eval_rn_labChecker_0 = _jspx_th_rn_labChecker_0.doStartTag();
              if (_jspx_eval_rn_labChecker_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_rn_labChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_rn_labChecker_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_rn_labChecker_0.doInitBody();
                }
                do {
                  out.write("\n      <p class=\"headerFont1\">\n        Active providers associated with ");
                  if (_jspx_meth_rn_labField_0(_jspx_th_rn_labChecker_0, _jspx_page_context))
                    return;
                  out.write(":\n      </p>\n      <table class=\"adminFormTable\" border=\"0\" cellspacing=\"0\">\n        ");
                  //  rn:providerIterator
                  org.recipnet.site.content.rncontrols.ProviderIterator pit = null;
                  org.recipnet.site.content.rncontrols.ProviderIterator _jspx_th_rn_providerIterator_0 = (org.recipnet.site.content.rncontrols.ProviderIterator) _jspx_tagPool_rn_providerIterator_restrictToActiveProviders_id.get(org.recipnet.site.content.rncontrols.ProviderIterator.class);
                  _jspx_th_rn_providerIterator_0.setPageContext(_jspx_page_context);
                  _jspx_th_rn_providerIterator_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_labChecker_0);
                  _jspx_th_rn_providerIterator_0.setId("pit");
                  _jspx_th_rn_providerIterator_0.setRestrictToActiveProviders(true);
                  int _jspx_eval_rn_providerIterator_0 = _jspx_th_rn_providerIterator_0.doStartTag();
                  if (_jspx_eval_rn_providerIterator_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_rn_providerIterator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_rn_providerIterator_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_rn_providerIterator_0.doInitBody();
                    }
                    pit = (org.recipnet.site.content.rncontrols.ProviderIterator) _jspx_page_context.findAttribute("pit");
                    do {
                      out.write("\n          <tr class=\"");
                      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${rn:testParity(pit.iterationCountSinceThisPhaseBegan,\n                                     'rowColorEven', 'rowColorOdd')}", java.lang.String.class, (PageContext)_jspx_page_context, _jspx_fnmap_0, false));
                      out.write("\">\n            <th class=\"threeColumnLeft\">\n              ");
                      //  rn:providerField
                      org.recipnet.site.content.rncontrols.ProviderField _jspx_th_rn_providerField_0 = (org.recipnet.site.content.rncontrols.ProviderField) _jspx_tagPool_rn_providerField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.ProviderField.class);
                      _jspx_th_rn_providerField_0.setPageContext(_jspx_page_context);
                      _jspx_th_rn_providerField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_providerIterator_0);
                      _jspx_th_rn_providerField_0.setFieldCode(ProviderField.NAME);
                      int _jspx_eval_rn_providerField_0 = _jspx_th_rn_providerField_0.doStartTag();
                      if (_jspx_th_rn_providerField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_rn_providerField_fieldCode_nobody.reuse(_jspx_th_rn_providerField_0);
                      out.write("\n            </th>\n            <td class=\"twoColumnRight\">\n              ");
                      //  rn:link
                      org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_0 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_href_contextType.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
                      _jspx_th_rn_link_0.setPageContext(_jspx_page_context);
                      _jspx_th_rn_link_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_providerIterator_0);
                      _jspx_th_rn_link_0.setContextType(ProviderContext.class);
                      _jspx_th_rn_link_0.setHref("/admin/editprovider.jsp");
                      int _jspx_eval_rn_link_0 = _jspx_th_rn_link_0.doStartTag();
                      if (_jspx_eval_rn_link_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_rn_link_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_rn_link_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_rn_link_0.doInitBody();
                        }
                        do {
                          out.write("Edit this Provider");
                          int evalDoAfterBody = _jspx_th_rn_link_0.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_rn_link_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                          out = _jspx_page_context.popBody();
                      }
                      if (_jspx_th_rn_link_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_rn_link_href_contextType.reuse(_jspx_th_rn_link_0);
                      out.write("\n            </td>\n          </tr>\n        ");
                      int evalDoAfterBody = _jspx_th_rn_providerIterator_0.doAfterBody();
                      pit = (org.recipnet.site.content.rncontrols.ProviderIterator) _jspx_page_context.findAttribute("pit");
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_rn_providerIterator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_rn_providerIterator_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  pit = (org.recipnet.site.content.rncontrols.ProviderIterator) _jspx_page_context.findAttribute("pit");
                  _jspx_tagPool_rn_providerIterator_restrictToActiveProviders_id.reuse(_jspx_th_rn_providerIterator_0);
                  out.write("\n        <tr>\n          <td colspan=\"2\" class=\"headerColor\">\n            ");
                  //  rn:link
                  org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_1 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_href_contextType.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
                  _jspx_th_rn_link_1.setPageContext(_jspx_page_context);
                  _jspx_th_rn_link_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_labChecker_0);
                  _jspx_th_rn_link_1.setContextType( LabContext.class );
                  _jspx_th_rn_link_1.setHref("/admin/addprovider.jsp");
                  int _jspx_eval_rn_link_1 = _jspx_th_rn_link_1.doStartTag();
                  if (_jspx_eval_rn_link_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_rn_link_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_rn_link_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_rn_link_1.doInitBody();
                    }
                    do {
                      out.write("Add a new provider...");
                      int evalDoAfterBody = _jspx_th_rn_link_1.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_rn_link_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_rn_link_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_link_href_contextType.reuse(_jspx_th_rn_link_1);
                  out.write("\n          </td>\n        </tr>\n      </table>\n      <br />\n      <br />\n    ");
                  int evalDoAfterBody = _jspx_th_rn_labChecker_0.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_rn_labChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_rn_labChecker_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_labChecker_requireActive.reuse(_jspx_th_rn_labChecker_0);
              out.write("\n    <p class=\"headerFont1\">\n      Deactivated providers associated with ");
              if (_jspx_meth_rn_labField_1(_jspx_th_ctl_selfForm_0, _jspx_page_context))
                return;
              out.write(".\n    </p>\n    ");
              //  rn:providerIterator
              org.recipnet.site.content.rncontrols.ProviderIterator inactiveProvs = null;
              org.recipnet.site.content.rncontrols.ProviderIterator _jspx_th_rn_providerIterator_1 = (org.recipnet.site.content.rncontrols.ProviderIterator) _jspx_tagPool_rn_providerIterator_restrictToInactiveProviders_id.get(org.recipnet.site.content.rncontrols.ProviderIterator.class);
              _jspx_th_rn_providerIterator_1.setPageContext(_jspx_page_context);
              _jspx_th_rn_providerIterator_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_providerIterator_1.setId("inactiveProvs");
              _jspx_th_rn_providerIterator_1.setRestrictToInactiveProviders(true);
              int _jspx_eval_rn_providerIterator_1 = _jspx_th_rn_providerIterator_1.doStartTag();
              if (_jspx_eval_rn_providerIterator_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_rn_providerIterator_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_rn_providerIterator_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_rn_providerIterator_1.doInitBody();
                }
                inactiveProvs = (org.recipnet.site.content.rncontrols.ProviderIterator) _jspx_page_context.findAttribute("inactiveProvs");
                do {
                  if (_jspx_meth_ctl_parityChecker_0(_jspx_th_rn_providerIterator_1, _jspx_page_context))
                    return;
                  out.write("\n      &nbsp;&nbsp;\n      ");
                  //  rn:link
                  org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_2 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_href_contextType.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
                  _jspx_th_rn_link_2.setPageContext(_jspx_page_context);
                  _jspx_th_rn_link_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_providerIterator_1);
                  _jspx_th_rn_link_2.setHref("/admin/deactivatedprovider.jsp");
                  _jspx_th_rn_link_2.setContextType(
              ProviderContext.class);
                  int _jspx_eval_rn_link_2 = _jspx_th_rn_link_2.doStartTag();
                  if (_jspx_eval_rn_link_2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_rn_link_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_rn_link_2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_rn_link_2.doInitBody();
                    }
                    do {
                      out.write("\n      ");
                      //  rn:providerField
                      org.recipnet.site.content.rncontrols.ProviderField _jspx_th_rn_providerField_1 = (org.recipnet.site.content.rncontrols.ProviderField) _jspx_tagPool_rn_providerField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.ProviderField.class);
                      _jspx_th_rn_providerField_1.setPageContext(_jspx_page_context);
                      _jspx_th_rn_providerField_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_link_2);
                      _jspx_th_rn_providerField_1.setFieldCode(ProviderField.NAME);
                      int _jspx_eval_rn_providerField_1 = _jspx_th_rn_providerField_1.doStartTag();
                      if (_jspx_th_rn_providerField_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_rn_providerField_fieldCode_nobody.reuse(_jspx_th_rn_providerField_1);
                      int evalDoAfterBody = _jspx_th_rn_link_2.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_rn_link_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_rn_link_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_link_href_contextType.reuse(_jspx_th_rn_link_2);
                  out.write("\n      <font class=\"ancillaryText\">(");
                  if (_jspx_meth_rn_userField_0(_jspx_th_rn_providerIterator_1, _jspx_page_context))
                    return;
                  out.write(")</font>");
                  int evalDoAfterBody = _jspx_th_rn_providerIterator_1.doAfterBody();
                  inactiveProvs = (org.recipnet.site.content.rncontrols.ProviderIterator) _jspx_page_context.findAttribute("inactiveProvs");
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_rn_providerIterator_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_rn_providerIterator_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              inactiveProvs = (org.recipnet.site.content.rncontrols.ProviderIterator) _jspx_page_context.findAttribute("inactiveProvs");
              _jspx_tagPool_rn_providerIterator_restrictToInactiveProviders_id.reuse(_jspx_th_rn_providerIterator_1);
              out.write("\n    ");
              //  ctl:errorMessage
              org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_0 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
              _jspx_th_ctl_errorMessage_0.setPageContext(_jspx_page_context);
              _jspx_th_ctl_errorMessage_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_ctl_errorMessage_0.setErrorSupplier(inactiveProvs);
              _jspx_th_ctl_errorMessage_0.setErrorFilter(ProviderIterator.NO_ITERATIONS);
              int _jspx_eval_ctl_errorMessage_0 = _jspx_th_ctl_errorMessage_0.doStartTag();
              if (_jspx_eval_ctl_errorMessage_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_ctl_errorMessage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_ctl_errorMessage_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_ctl_errorMessage_0.doInitBody();
                }
                do {
                  out.write("\n      There are no deactivated providers associated with this lab.\n    ");
                  int evalDoAfterBody = _jspx_th_ctl_errorMessage_0.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_ctl_errorMessage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_ctl_errorMessage_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.reuse(_jspx_th_ctl_errorMessage_0);
              out.write('\n');
              out.write(' ');
              out.write(' ');
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
          out.write("\n  <br />\n  <br />\n  <center>\n    ");
          if (_jspx_meth_ctl_a_0(_jspx_th_rn_labPage_0, _jspx_page_context))
            return;
          out.write("\n  </center>\n  ");
          if (_jspx_meth_ctl_styleBlock_0(_jspx_th_rn_labPage_0, _jspx_page_context))
            return;
          out.write('\n');
          int evalDoAfterBody = _jspx_th_rn_labPage_0.doAfterBody();
          htmlPage = (org.recipnet.site.content.rncontrols.LabPage) _jspx_page_context.findAttribute("htmlPage");
          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
            break;
        } while (true);
        if (_jspx_eval_rn_labPage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
          out = _jspx_page_context.popBody();
      }
      if (_jspx_th_rn_labPage_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
        return;
      _jspx_tagPool_rn_labPage_title_loginPageUrl_labIdParamName_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName.reuse(_jspx_th_rn_labPage_0);
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

  private boolean _jspx_meth_rn_labField_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_labChecker_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:labField
    org.recipnet.site.content.rncontrols.LabField _jspx_th_rn_labField_0 = (org.recipnet.site.content.rncontrols.LabField) _jspx_tagPool_rn_labField_nobody.get(org.recipnet.site.content.rncontrols.LabField.class);
    _jspx_th_rn_labField_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_labField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_labChecker_0);
    int _jspx_eval_rn_labField_0 = _jspx_th_rn_labField_0.doStartTag();
    if (_jspx_th_rn_labField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_labField_nobody.reuse(_jspx_th_rn_labField_0);
    return false;
  }

  private boolean _jspx_meth_rn_labField_1(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_selfForm_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:labField
    org.recipnet.site.content.rncontrols.LabField _jspx_th_rn_labField_1 = (org.recipnet.site.content.rncontrols.LabField) _jspx_tagPool_rn_labField_nobody.get(org.recipnet.site.content.rncontrols.LabField.class);
    _jspx_th_rn_labField_1.setPageContext(_jspx_page_context);
    _jspx_th_rn_labField_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
    int _jspx_eval_rn_labField_1 = _jspx_th_rn_labField_1.doStartTag();
    if (_jspx_th_rn_labField_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_labField_nobody.reuse(_jspx_th_rn_labField_1);
    return false;
  }

  private boolean _jspx_meth_ctl_parityChecker_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_providerIterator_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:parityChecker
    org.recipnet.common.controls.ParityChecker _jspx_th_ctl_parityChecker_0 = (org.recipnet.common.controls.ParityChecker) _jspx_tagPool_ctl_parityChecker_invert_includeOnlyOnFirst.get(org.recipnet.common.controls.ParityChecker.class);
    _jspx_th_ctl_parityChecker_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_parityChecker_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_providerIterator_1);
    _jspx_th_ctl_parityChecker_0.setIncludeOnlyOnFirst(true);
    _jspx_th_ctl_parityChecker_0.setInvert(true);
    int _jspx_eval_ctl_parityChecker_0 = _jspx_th_ctl_parityChecker_0.doStartTag();
    if (_jspx_eval_ctl_parityChecker_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_parityChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_parityChecker_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_parityChecker_0.doInitBody();
      }
      do {
        out.write(',');
        int evalDoAfterBody = _jspx_th_ctl_parityChecker_0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_parityChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_parityChecker_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_parityChecker_invert_includeOnlyOnFirst.reuse(_jspx_th_ctl_parityChecker_0);
    return false;
  }

  private boolean _jspx_meth_rn_userField_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_providerIterator_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:userField
    org.recipnet.site.content.rncontrols.UserField _jspx_th_rn_userField_0 = (org.recipnet.site.content.rncontrols.UserField) _jspx_tagPool_rn_userField_nobody.get(org.recipnet.site.content.rncontrols.UserField.class);
    _jspx_th_rn_userField_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_userField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_providerIterator_1);
    int _jspx_eval_rn_userField_0 = _jspx_th_rn_userField_0.doStartTag();
    if (_jspx_th_rn_userField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_userField_nobody.reuse(_jspx_th_rn_userField_0);
    return false;
  }

  private boolean _jspx_meth_ctl_a_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_labPage_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:a
    org.recipnet.common.controls.LinkHtmlElement _jspx_th_ctl_a_0 = (org.recipnet.common.controls.LinkHtmlElement) _jspx_tagPool_ctl_a_href.get(org.recipnet.common.controls.LinkHtmlElement.class);
    _jspx_th_ctl_a_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_a_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_labPage_0);
    _jspx_th_ctl_a_0.setHref("/admin/managelabs.jsp");
    int _jspx_eval_ctl_a_0 = _jspx_th_ctl_a_0.doStartTag();
    if (_jspx_eval_ctl_a_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_a_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_a_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_a_0.doInitBody();
      }
      do {
        out.write("Back to Lab Management");
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

  private boolean _jspx_meth_ctl_styleBlock_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_labPage_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:styleBlock
    org.recipnet.common.controls.HtmlPageStyleBlock _jspx_th_ctl_styleBlock_0 = (org.recipnet.common.controls.HtmlPageStyleBlock) _jspx_tagPool_ctl_styleBlock.get(org.recipnet.common.controls.HtmlPageStyleBlock.class);
    _jspx_th_ctl_styleBlock_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_styleBlock_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_labPage_0);
    int _jspx_eval_ctl_styleBlock_0 = _jspx_th_ctl_styleBlock_0.doStartTag();
    if (_jspx_eval_ctl_styleBlock_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_styleBlock_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_styleBlock_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_styleBlock_0.doInitBody();
      }
      do {
        out.write("\n    .adminFormTable { border-style: solid; border-width: thin;\n                      border-color: #CCCCCC; padding: 0.01in;}\n    .headerFont1  { font-size:medium; font-weight: bold; }\n    .rowColorEven { background-color: #E6E6E6 }\n    .rowColorOdd { background-color: #FFFFFF }\n    .headerColor   { background-color: #CCCCCC }\n    .threeColumnLeft   { text-align: left; padding: 0.05in; border-width: 0; }\n    .threeColumnRight  { text-align: left; padding: 0.05in; border-width: 0; }\n    .twoColumnRight  { text-align: left; padding: 0.01in; border-width: 0;}\n  ");
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
