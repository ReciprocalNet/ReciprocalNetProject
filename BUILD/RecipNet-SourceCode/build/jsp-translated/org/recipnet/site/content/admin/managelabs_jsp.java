package org.recipnet.site.content.admin;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.recipnet.site.content.rncontrols.AuthorizationReasonMessage;
import org.recipnet.site.content.rncontrols.LabField;
import org.recipnet.site.content.rncontrols.LabContext;

public final class managelabs_jsp extends org.apache.jasper.runtime.HttpJspBase
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
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_authorizationChecker_invert_canAdministerLabs;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_redirectToLogin_target_returnUrlParamName_authorizationReasonParamName_authorizationReasonCode_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_authorizationChecker_canAdministerLabs;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_selfForm;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_labIterator_id;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_labField_fieldCode_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_labChecker_requireLocal_requireActive;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_link_href_contextType;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_labChecker_requireLocal_requireInactive;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_labChecker_requireNonlocal;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_a_href;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_styleBlock;

  public java.util.List getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _jspx_tagPool_rn_page_title = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_authorizationChecker_invert_canAdministerLabs = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_redirectToLogin_target_returnUrlParamName_authorizationReasonParamName_authorizationReasonCode_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_authorizationChecker_canAdministerLabs = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_selfForm = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_labIterator_id = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_labField_fieldCode_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_labChecker_requireLocal_requireActive = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_link_href_contextType = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_labChecker_requireLocal_requireInactive = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_labChecker_requireNonlocal = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_a_href = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_styleBlock = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
  }

  public void _jspDestroy() {
    _jspx_tagPool_rn_page_title.release();
    _jspx_tagPool_rn_authorizationChecker_invert_canAdministerLabs.release();
    _jspx_tagPool_rn_redirectToLogin_target_returnUrlParamName_authorizationReasonParamName_authorizationReasonCode_nobody.release();
    _jspx_tagPool_rn_authorizationChecker_canAdministerLabs.release();
    _jspx_tagPool_ctl_selfForm.release();
    _jspx_tagPool_rn_labIterator_id.release();
    _jspx_tagPool_rn_labField_fieldCode_nobody.release();
    _jspx_tagPool_rn_labChecker_requireLocal_requireActive.release();
    _jspx_tagPool_rn_link_href_contextType.release();
    _jspx_tagPool_rn_labChecker_requireLocal_requireInactive.release();
    _jspx_tagPool_rn_labChecker_requireNonlocal.release();
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
      //  rn:page
      org.recipnet.site.content.rncontrols.RecipnetPage _jspx_th_rn_page_0 = (org.recipnet.site.content.rncontrols.RecipnetPage) _jspx_tagPool_rn_page_title.get(org.recipnet.site.content.rncontrols.RecipnetPage.class);
      _jspx_th_rn_page_0.setPageContext(_jspx_page_context);
      _jspx_th_rn_page_0.setParent(null);
      _jspx_th_rn_page_0.setTitle("Manage Labs");
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
          //  rn:authorizationChecker
          org.recipnet.site.content.rncontrols.AuthorizationChecker _jspx_th_rn_authorizationChecker_0 = (org.recipnet.site.content.rncontrols.AuthorizationChecker) _jspx_tagPool_rn_authorizationChecker_invert_canAdministerLabs.get(org.recipnet.site.content.rncontrols.AuthorizationChecker.class);
          _jspx_th_rn_authorizationChecker_0.setPageContext(_jspx_page_context);
          _jspx_th_rn_authorizationChecker_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_page_0);
          _jspx_th_rn_authorizationChecker_0.setCanAdministerLabs(true);
          _jspx_th_rn_authorizationChecker_0.setInvert(true);
          int _jspx_eval_rn_authorizationChecker_0 = _jspx_th_rn_authorizationChecker_0.doStartTag();
          if (_jspx_eval_rn_authorizationChecker_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_rn_authorizationChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_rn_authorizationChecker_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_rn_authorizationChecker_0.doInitBody();
            }
            do {
              out.write("\n    ");
              //  rn:redirectToLogin
              org.recipnet.site.content.rncontrols.RedirectToLogin _jspx_th_rn_redirectToLogin_0 = (org.recipnet.site.content.rncontrols.RedirectToLogin) _jspx_tagPool_rn_redirectToLogin_target_returnUrlParamName_authorizationReasonParamName_authorizationReasonCode_nobody.get(org.recipnet.site.content.rncontrols.RedirectToLogin.class);
              _jspx_th_rn_redirectToLogin_0.setPageContext(_jspx_page_context);
              _jspx_th_rn_redirectToLogin_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_0);
              _jspx_th_rn_redirectToLogin_0.setTarget("/login.jsp");
              _jspx_th_rn_redirectToLogin_0.setReturnUrlParamName("origUrl");
              _jspx_th_rn_redirectToLogin_0.setAuthorizationReasonParamName("authorizationFailedReason");
              _jspx_th_rn_redirectToLogin_0.setAuthorizationReasonCode(
          AuthorizationReasonMessage.CANNOT_ADMINISTER_LABS);
              int _jspx_eval_rn_redirectToLogin_0 = _jspx_th_rn_redirectToLogin_0.doStartTag();
              if (_jspx_th_rn_redirectToLogin_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_redirectToLogin_target_returnUrlParamName_authorizationReasonParamName_authorizationReasonCode_nobody.reuse(_jspx_th_rn_redirectToLogin_0);
              out.write('\n');
              out.write(' ');
              out.write(' ');
              int evalDoAfterBody = _jspx_th_rn_authorizationChecker_0.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
            if (_jspx_eval_rn_authorizationChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
              out = _jspx_page_context.popBody();
          }
          if (_jspx_th_rn_authorizationChecker_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
            return;
          _jspx_tagPool_rn_authorizationChecker_invert_canAdministerLabs.reuse(_jspx_th_rn_authorizationChecker_0);
          out.write('\n');
          out.write(' ');
          out.write(' ');
          //  rn:authorizationChecker
          org.recipnet.site.content.rncontrols.AuthorizationChecker _jspx_th_rn_authorizationChecker_1 = (org.recipnet.site.content.rncontrols.AuthorizationChecker) _jspx_tagPool_rn_authorizationChecker_canAdministerLabs.get(org.recipnet.site.content.rncontrols.AuthorizationChecker.class);
          _jspx_th_rn_authorizationChecker_1.setPageContext(_jspx_page_context);
          _jspx_th_rn_authorizationChecker_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_page_0);
          _jspx_th_rn_authorizationChecker_1.setCanAdministerLabs(true);
          int _jspx_eval_rn_authorizationChecker_1 = _jspx_th_rn_authorizationChecker_1.doStartTag();
          if (_jspx_eval_rn_authorizationChecker_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_rn_authorizationChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_rn_authorizationChecker_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_rn_authorizationChecker_1.doInitBody();
            }
            do {
              out.write("\n    <p class=\"headerFont1\">Reciprocal Net Labs:</p>\n    ");
              //  ctl:selfForm
              org.recipnet.common.controls.FormHtmlElement _jspx_th_ctl_selfForm_0 = (org.recipnet.common.controls.FormHtmlElement) _jspx_tagPool_ctl_selfForm.get(org.recipnet.common.controls.FormHtmlElement.class);
              _jspx_th_ctl_selfForm_0.setPageContext(_jspx_page_context);
              _jspx_th_ctl_selfForm_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_1);
              int _jspx_eval_ctl_selfForm_0 = _jspx_th_ctl_selfForm_0.doStartTag();
              if (_jspx_eval_ctl_selfForm_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_ctl_selfForm_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_ctl_selfForm_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_ctl_selfForm_0.doInitBody();
                }
                do {
                  out.write("\n      <table class=\"adminFormTable\" border=\"0\" cellspacing=\"0\">\n        ");
                  //  rn:labIterator
                  org.recipnet.site.content.rncontrols.LabIterator lit = null;
                  org.recipnet.site.content.rncontrols.LabIterator _jspx_th_rn_labIterator_0 = (org.recipnet.site.content.rncontrols.LabIterator) _jspx_tagPool_rn_labIterator_id.get(org.recipnet.site.content.rncontrols.LabIterator.class);
                  _jspx_th_rn_labIterator_0.setPageContext(_jspx_page_context);
                  _jspx_th_rn_labIterator_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
                  _jspx_th_rn_labIterator_0.setId("lit");
                  int _jspx_eval_rn_labIterator_0 = _jspx_th_rn_labIterator_0.doStartTag();
                  if (_jspx_eval_rn_labIterator_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_rn_labIterator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_rn_labIterator_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_rn_labIterator_0.doInitBody();
                    }
                    lit = (org.recipnet.site.content.rncontrols.LabIterator) _jspx_page_context.findAttribute("lit");
                    do {
                      out.write("\n          <tr class=\"");
                      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${rn:testParity(lit.iterationCountSinceThisPhaseBegan,\n                                     'rowColortrue', 'rowColorfalse')}", java.lang.String.class, (PageContext)_jspx_page_context, _jspx_fnmap_0, false));
                      out.write("\">\n          <th class=\"threeColumnLeft\">\n            ");
                      //  rn:labField
                      org.recipnet.site.content.rncontrols.LabField _jspx_th_rn_labField_0 = (org.recipnet.site.content.rncontrols.LabField) _jspx_tagPool_rn_labField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.LabField.class);
                      _jspx_th_rn_labField_0.setPageContext(_jspx_page_context);
                      _jspx_th_rn_labField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_labIterator_0);
                      _jspx_th_rn_labField_0.setFieldCode(LabField.NAME);
                      int _jspx_eval_rn_labField_0 = _jspx_th_rn_labField_0.doStartTag();
                      if (_jspx_th_rn_labField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_rn_labField_fieldCode_nobody.reuse(_jspx_th_rn_labField_0);
                      out.write("\n          </th>\n          ");
                      //  rn:labChecker
                      org.recipnet.site.content.rncontrols.LabChecker _jspx_th_rn_labChecker_0 = (org.recipnet.site.content.rncontrols.LabChecker) _jspx_tagPool_rn_labChecker_requireLocal_requireActive.get(org.recipnet.site.content.rncontrols.LabChecker.class);
                      _jspx_th_rn_labChecker_0.setPageContext(_jspx_page_context);
                      _jspx_th_rn_labChecker_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_labIterator_0);
                      _jspx_th_rn_labChecker_0.setRequireLocal(true);
                      _jspx_th_rn_labChecker_0.setRequireActive(true);
                      int _jspx_eval_rn_labChecker_0 = _jspx_th_rn_labChecker_0.doStartTag();
                      if (_jspx_eval_rn_labChecker_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_rn_labChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_rn_labChecker_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_rn_labChecker_0.doInitBody();
                        }
                        do {
                          out.write("\n              <td class=\"threeColumnMiddle\">\n                ");
                          //  rn:link
                          org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_0 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_href_contextType.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
                          _jspx_th_rn_link_0.setPageContext(_jspx_page_context);
                          _jspx_th_rn_link_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_labChecker_0);
                          _jspx_th_rn_link_0.setContextType(LabContext.class);
                          _jspx_th_rn_link_0.setHref("/admin/editlab.jsp");
                          int _jspx_eval_rn_link_0 = _jspx_th_rn_link_0.doStartTag();
                          if (_jspx_eval_rn_link_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            if (_jspx_eval_rn_link_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_rn_link_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_rn_link_0.doInitBody();
                            }
                            do {
                              out.write("Edit this Lab");
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
                          out.write("\n              </td>\n              <td class=\"threeColumnRight\">\n                ");
                          //  rn:link
                          org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_1 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_href_contextType.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
                          _jspx_th_rn_link_1.setPageContext(_jspx_page_context);
                          _jspx_th_rn_link_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_labChecker_0);
                          _jspx_th_rn_link_1.setContextType(LabContext.class);
                          _jspx_th_rn_link_1.setHref("/admin/manageproviders.jsp");
                          int _jspx_eval_rn_link_1 = _jspx_th_rn_link_1.doStartTag();
                          if (_jspx_eval_rn_link_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            if (_jspx_eval_rn_link_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_rn_link_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_rn_link_1.doInitBody();
                            }
                            do {
                              out.write("Manage this lab's provider(s)");
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
                          out.write("\n              </td>\n          ");
                          int evalDoAfterBody = _jspx_th_rn_labChecker_0.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_rn_labChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                          out = _jspx_page_context.popBody();
                      }
                      if (_jspx_th_rn_labChecker_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_rn_labChecker_requireLocal_requireActive.reuse(_jspx_th_rn_labChecker_0);
                      out.write("\n            ");
                      //  rn:labChecker
                      org.recipnet.site.content.rncontrols.LabChecker _jspx_th_rn_labChecker_1 = (org.recipnet.site.content.rncontrols.LabChecker) _jspx_tagPool_rn_labChecker_requireLocal_requireInactive.get(org.recipnet.site.content.rncontrols.LabChecker.class);
                      _jspx_th_rn_labChecker_1.setPageContext(_jspx_page_context);
                      _jspx_th_rn_labChecker_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_labIterator_0);
                      _jspx_th_rn_labChecker_1.setRequireLocal(true);
                      _jspx_th_rn_labChecker_1.setRequireInactive(true);
                      int _jspx_eval_rn_labChecker_1 = _jspx_th_rn_labChecker_1.doStartTag();
                      if (_jspx_eval_rn_labChecker_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_rn_labChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_rn_labChecker_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_rn_labChecker_1.doInitBody();
                        }
                        do {
                          out.write("\n              <td class=\"threeColumnMiddle\">\n                ");
                          //  rn:link
                          org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_2 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_href_contextType.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
                          _jspx_th_rn_link_2.setPageContext(_jspx_page_context);
                          _jspx_th_rn_link_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_labChecker_1);
                          _jspx_th_rn_link_2.setContextType(LabContext.class);
                          _jspx_th_rn_link_2.setHref("/admin/deactivatedlab.jsp");
                          int _jspx_eval_rn_link_2 = _jspx_th_rn_link_2.doStartTag();
                          if (_jspx_eval_rn_link_2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            if (_jspx_eval_rn_link_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_rn_link_2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_rn_link_2.doInitBody();
                            }
                            do {
                              out.write("This lab is deactivated.");
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
                          out.write("\n              </td>\n              <td class=\"threeColumnRight\">\n                ");
                          //  rn:link
                          org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_3 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_href_contextType.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
                          _jspx_th_rn_link_3.setPageContext(_jspx_page_context);
                          _jspx_th_rn_link_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_labChecker_1);
                          _jspx_th_rn_link_3.setContextType(LabContext.class);
                          _jspx_th_rn_link_3.setHref("/admin/manageproviders.jsp");
                          int _jspx_eval_rn_link_3 = _jspx_th_rn_link_3.doStartTag();
                          if (_jspx_eval_rn_link_3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            if (_jspx_eval_rn_link_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_rn_link_3.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_rn_link_3.doInitBody();
                            }
                            do {
                              out.write("This lab's provider(s) are deactivated.");
                              int evalDoAfterBody = _jspx_th_rn_link_3.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                            } while (true);
                            if (_jspx_eval_rn_link_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                              out = _jspx_page_context.popBody();
                          }
                          if (_jspx_th_rn_link_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          _jspx_tagPool_rn_link_href_contextType.reuse(_jspx_th_rn_link_3);
                          out.write("\n              </td>\n            ");
                          int evalDoAfterBody = _jspx_th_rn_labChecker_1.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_rn_labChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                          out = _jspx_page_context.popBody();
                      }
                      if (_jspx_th_rn_labChecker_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_rn_labChecker_requireLocal_requireInactive.reuse(_jspx_th_rn_labChecker_1);
                      out.write("\n          ");
                      if (_jspx_meth_rn_labChecker_2(_jspx_th_rn_labIterator_0, _jspx_page_context))
                        return;
                      out.write("\n          </tr>\n        ");
                      int evalDoAfterBody = _jspx_th_rn_labIterator_0.doAfterBody();
                      lit = (org.recipnet.site.content.rncontrols.LabIterator) _jspx_page_context.findAttribute("lit");
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_rn_labIterator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_rn_labIterator_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  lit = (org.recipnet.site.content.rncontrols.LabIterator) _jspx_page_context.findAttribute("lit");
                  _jspx_tagPool_rn_labIterator_id.reuse(_jspx_th_rn_labIterator_0);
                  out.write("\n      </table>\n    ");
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
              out.write("\n    <br />&nbsp;<br />\n    <center>\n      ");
              if (_jspx_meth_ctl_a_0(_jspx_th_rn_authorizationChecker_1, _jspx_page_context))
                return;
              out.write("\n    </center>\n  ");
              int evalDoAfterBody = _jspx_th_rn_authorizationChecker_1.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
            if (_jspx_eval_rn_authorizationChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
              out = _jspx_page_context.popBody();
          }
          if (_jspx_th_rn_authorizationChecker_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
            return;
          _jspx_tagPool_rn_authorizationChecker_canAdministerLabs.reuse(_jspx_th_rn_authorizationChecker_1);
          out.write('\n');
          out.write(' ');
          out.write(' ');
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

  private boolean _jspx_meth_rn_labChecker_2(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_labIterator_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:labChecker
    org.recipnet.site.content.rncontrols.LabChecker _jspx_th_rn_labChecker_2 = (org.recipnet.site.content.rncontrols.LabChecker) _jspx_tagPool_rn_labChecker_requireNonlocal.get(org.recipnet.site.content.rncontrols.LabChecker.class);
    _jspx_th_rn_labChecker_2.setPageContext(_jspx_page_context);
    _jspx_th_rn_labChecker_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_labIterator_0);
    _jspx_th_rn_labChecker_2.setRequireNonlocal(true);
    int _jspx_eval_rn_labChecker_2 = _jspx_th_rn_labChecker_2.doStartTag();
    if (_jspx_eval_rn_labChecker_2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_labChecker_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_labChecker_2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_labChecker_2.doInitBody();
      }
      do {
        out.write("\n            <td colspan=\"2\">This lab may not managed from this site.</td>\n          ");
        int evalDoAfterBody = _jspx_th_rn_labChecker_2.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_labChecker_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_labChecker_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_labChecker_requireNonlocal.reuse(_jspx_th_rn_labChecker_2);
    return false;
  }

  private boolean _jspx_meth_ctl_a_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_authorizationChecker_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:a
    org.recipnet.common.controls.LinkHtmlElement _jspx_th_ctl_a_0 = (org.recipnet.common.controls.LinkHtmlElement) _jspx_tagPool_ctl_a_href.get(org.recipnet.common.controls.LinkHtmlElement.class);
    _jspx_th_ctl_a_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_a_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_1);
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
        out.write("\n    .headerFont1  { font-size:medium; font-weight: bold; }\n    .rowColorfalse { background-color: #E6E6E6 }\n    .rowColortrue  { background-color: #FFFFFF }\n    .threeColumnLeft   { text-align: left; padding: 0.05in; border-width: 0; }\n    .threeColumnMiddle { text-align: left;  padding: 0.05in; border-width: 0;}\n    .threeColumnRight  { text-align: left;  padding: 0.05in; border-width: 0; }\n    .adminFormTable { border-style: solid; border-width: thin;\n                      border-color: #CCCCCC; padding: 0.01in;}\n  ");
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
