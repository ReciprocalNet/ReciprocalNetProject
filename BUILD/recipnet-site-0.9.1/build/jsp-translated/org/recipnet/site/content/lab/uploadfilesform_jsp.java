package org.recipnet.site.content.lab;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.recipnet.common.controls.HtmlPageIterator;
import org.recipnet.site.content.rncontrols.AuthorizationReasonMessage;
import org.recipnet.site.content.servlet.MultipartUploadAccepter;
import org.recipnet.site.shared.bl.SampleWorkflowBL;
import org.recipnet.site.shared.db.SampleInfo;

public final class uploadfilesform_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static java.util.Vector _jspx_dependants;

  static {
    _jspx_dependants = new java.util.Vector(2);
    _jspx_dependants.add("/WEB-INF/controls.tld");
    _jspx_dependants.add("/WEB-INF/rncontrols.tld");
  }

  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_samplePage_title_loginPageUrl_ignoreSampleHistoryId_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_authorizationChecker_suppressRenderingOnly_invert_canEditSample;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_redirectToLogin_target_returnUrlParamName_authorizationReasonParamName_authorizationReasonCode_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleChecker_includeIfRetracted;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleChecker_invert_includeIfRetracted;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_form_pageForm_method_enctype_action;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_uploadOp_workflowAction_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_iterator_iterations_id;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_link_href;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_styleBlock;

  public java.util.List getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _jspx_tagPool_rn_samplePage_title_loginPageUrl_ignoreSampleHistoryId_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_authorizationChecker_suppressRenderingOnly_invert_canEditSample = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_redirectToLogin_target_returnUrlParamName_authorizationReasonParamName_authorizationReasonCode_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleChecker_includeIfRetracted = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleChecker_invert_includeIfRetracted = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_form_pageForm_method_enctype_action = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_uploadOp_workflowAction_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_iterator_iterations_id = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_link_href = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_styleBlock = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
  }

  public void _jspDestroy() {
    _jspx_tagPool_rn_samplePage_title_loginPageUrl_ignoreSampleHistoryId_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName.release();
    _jspx_tagPool_rn_authorizationChecker_suppressRenderingOnly_invert_canEditSample.release();
    _jspx_tagPool_rn_redirectToLogin_target_returnUrlParamName_authorizationReasonParamName_authorizationReasonCode_nobody.release();
    _jspx_tagPool_rn_sampleChecker_includeIfRetracted.release();
    _jspx_tagPool_rn_sampleChecker_invert_includeIfRetracted.release();
    _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.release();
    _jspx_tagPool_ctl_form_pageForm_method_enctype_action.release();
    _jspx_tagPool_rn_uploadOp_workflowAction_nobody.release();
    _jspx_tagPool_ctl_iterator_iterations_id.release();
    _jspx_tagPool_rn_link_href.release();
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

      out.write("\n\n\n\n\n\n\n\n");
      //  rn:samplePage
      org.recipnet.site.content.rncontrols.SamplePage _jspx_th_rn_samplePage_0 = (org.recipnet.site.content.rncontrols.SamplePage) _jspx_tagPool_rn_samplePage_title_loginPageUrl_ignoreSampleHistoryId_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName.get(org.recipnet.site.content.rncontrols.SamplePage.class);
      _jspx_th_rn_samplePage_0.setPageContext(_jspx_page_context);
      _jspx_th_rn_samplePage_0.setParent(null);
      _jspx_th_rn_samplePage_0.setTitle("Upload Files");
      _jspx_th_rn_samplePage_0.setLoginPageUrl("/login.jsp");
      _jspx_th_rn_samplePage_0.setIgnoreSampleHistoryId(true);
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
          out.write("\n  <div class=\"pageBody\">\n  ");
          //  rn:authorizationChecker
          org.recipnet.site.content.rncontrols.AuthorizationChecker _jspx_th_rn_authorizationChecker_0 = (org.recipnet.site.content.rncontrols.AuthorizationChecker) _jspx_tagPool_rn_authorizationChecker_suppressRenderingOnly_invert_canEditSample.get(org.recipnet.site.content.rncontrols.AuthorizationChecker.class);
          _jspx_th_rn_authorizationChecker_0.setPageContext(_jspx_page_context);
          _jspx_th_rn_authorizationChecker_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_samplePage_0);
          _jspx_th_rn_authorizationChecker_0.setCanEditSample(true);
          _jspx_th_rn_authorizationChecker_0.setInvert(true);
          _jspx_th_rn_authorizationChecker_0.setSuppressRenderingOnly(true);
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
            AuthorizationReasonMessage.CANNOT_EDIT_SAMPLE);
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
          _jspx_tagPool_rn_authorizationChecker_suppressRenderingOnly_invert_canEditSample.reuse(_jspx_th_rn_authorizationChecker_0);
          out.write('\n');
          out.write(' ');
          out.write(' ');
          if (_jspx_meth_rn_sampleChecker_0(_jspx_th_rn_samplePage_0, _jspx_page_context))
            return;
          out.write('\n');
          out.write(' ');
          out.write(' ');
          //  rn:sampleChecker
          org.recipnet.site.content.rncontrols.SampleChecker _jspx_th_rn_sampleChecker_1 = (org.recipnet.site.content.rncontrols.SampleChecker) _jspx_tagPool_rn_sampleChecker_invert_includeIfRetracted.get(org.recipnet.site.content.rncontrols.SampleChecker.class);
          _jspx_th_rn_sampleChecker_1.setPageContext(_jspx_page_context);
          _jspx_th_rn_sampleChecker_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_samplePage_0);
          _jspx_th_rn_sampleChecker_1.setIncludeIfRetracted(true);
          _jspx_th_rn_sampleChecker_1.setInvert(true);
          int _jspx_eval_rn_sampleChecker_1 = _jspx_th_rn_sampleChecker_1.doStartTag();
          if (_jspx_eval_rn_sampleChecker_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_rn_sampleChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_rn_sampleChecker_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_rn_sampleChecker_1.doInitBody();
            }
            do {
              out.write("\n    ");
              if (_jspx_meth_ctl_extraHtmlAttribute_0(_jspx_th_rn_sampleChecker_1, _jspx_page_context))
                return;
              out.write("\n      ");
              //  ctl:form
              org.recipnet.common.controls.FormHtmlElement _jspx_th_ctl_form_0 = (org.recipnet.common.controls.FormHtmlElement) _jspx_tagPool_ctl_form_pageForm_method_enctype_action.get(org.recipnet.common.controls.FormHtmlElement.class);
              _jspx_th_ctl_form_0.setPageContext(_jspx_page_context);
              _jspx_th_ctl_form_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleChecker_1);
              _jspx_th_ctl_form_0.setMethod("POST");
              _jspx_th_ctl_form_0.setEnctype("multipart/form-data");
              _jspx_th_ctl_form_0.setPageForm(false);
              _jspx_th_ctl_form_0.setAction("/servlet/multipartuploadaccepter");
              int _jspx_eval_ctl_form_0 = _jspx_th_ctl_form_0.doStartTag();
              if (_jspx_eval_ctl_form_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_ctl_form_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_ctl_form_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_ctl_form_0.doInitBody();
                }
                do {
                  out.write("\n        ");
                  //  rn:uploadOp
                  org.recipnet.site.content.rncontrols.UploaderOperationCreationTag _jspx_th_rn_uploadOp_0 = (org.recipnet.site.content.rncontrols.UploaderOperationCreationTag) _jspx_tagPool_rn_uploadOp_workflowAction_nobody.get(org.recipnet.site.content.rncontrols.UploaderOperationCreationTag.class);
                  _jspx_th_rn_uploadOp_0.setPageContext(_jspx_page_context);
                  _jspx_th_rn_uploadOp_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_form_0);
                  _jspx_th_rn_uploadOp_0.setWorkflowAction(SampleWorkflowBL.FILE_ADDED);
                  int _jspx_eval_rn_uploadOp_0 = _jspx_th_rn_uploadOp_0.doStartTag();
                  if (_jspx_th_rn_uploadOp_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_uploadOp_workflowAction_nobody.reuse(_jspx_th_rn_uploadOp_0);
                  out.write("\n        <table class=\"bodyTable\" cellspacing=\"0\"> \n          <tr>\n            <th class=\"tableHeader\" colspan=\"3\">\n              File upload:\n            </th>\n          </tr>\n          <tr>\n           <th colspan=\"3\" style=\"padding-top: 0.25em;\">\n             Comments: (optional)\n           </th>\n          </tr>\n          <tr>\n            <td colspan=\"3\">\n              <textarea rows=\"2\" cols=\"36\" name=\"");
                  out.print(
                  MultipartUploadAccepter.COMMENTS_PARAM_NAME);
                  out.write("\"\n                  wrap=\"virtual\"></textarea>\n            </td>\n          </tr>\n          <tr>\n            <th colspan=\"2\" class=\"mainHeading\">\n              Filename:\n            </th>\n            <th class=\"mainHeading\">\n              Description:\n            </th>\n          </tr>\n          <tr>\n            <td colspan=\"3\">\n              <hr size=\"1\" color=\"#32357D\" />\n            </td>\n          </tr>\n          ");
                  //  ctl:iterator
                  org.recipnet.common.controls.SimpleIterator files = null;
                  org.recipnet.common.controls.SimpleIterator _jspx_th_ctl_iterator_0 = (org.recipnet.common.controls.SimpleIterator) _jspx_tagPool_ctl_iterator_iterations_id.get(org.recipnet.common.controls.SimpleIterator.class);
                  _jspx_th_ctl_iterator_0.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_iterator_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_form_0);
                  _jspx_th_ctl_iterator_0.setId("files");
                  _jspx_th_ctl_iterator_0.setIterations(5);
                  int _jspx_eval_ctl_iterator_0 = _jspx_th_ctl_iterator_0.doStartTag();
                  if (_jspx_eval_ctl_iterator_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_iterator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_iterator_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_iterator_0.doInitBody();
                    }
                    files = (org.recipnet.common.controls.SimpleIterator) _jspx_page_context.findAttribute("files");
                    do {
                      out.write("\n          <tr>\n            <th>\n              Remote: (optional)\n            </th>\n            <td>\n              <input type=\"text\" size=\"32\"\n                  name=\"");
                      out.print(MultipartUploadAccepter.FILE_NAME_PREFIX);
                      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${\n                      files.iterationCountSinceThisPhaseBegan}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
                      out.write("\"\n                  onChange=\"updatedField('");
                      out.print(
                      MultipartUploadAccepter.FILE_NAME_PREFIX);
                      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${\n                          files.iterationCountSinceThisPhaseBegan}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
                      out.write("')\" />\n            </td>\n            <td rowspan=\"2\">\n              <textarea columns=\"32\" rows=\"2\" name=\"");
                      out.print(
                MultipartUploadAccepter.DESCRIPTION_PREFIX);
                      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${\n                    files.iterationCountSinceThisPhaseBegan}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
                      out.write("\"></textarea>\n            </td>\n          </tr>\n          <tr>\n            <th>\n              Local:\n            </th>\n            <td>\n            ");
                      out.write("\n              <input type=\"file\" size=\"22\"\n                  name=\"");
                      out.print(MultipartUploadAccepter.FILE_PREFIX);
                      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${\n                      files.iterationCountSinceThisPhaseBegan}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
                      out.write("\"\n                  onChange=\"setRemoteName('");
                      out.print(
                      MultipartUploadAccepter.FILE_NAME_PREFIX);
                      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${\n                          files.iterationCountSinceThisPhaseBegan\n                      }", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
                      out.write('\'');
                      out.write(',');
                      out.write('\'');
                      out.print(MultipartUploadAccepter.FILE_PREFIX);
                      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${\n                          files.iterationCountSinceThisPhaseBegan}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
                      out.write("')\"\n                  onBlur=\"setRemoteName('");
                      out.print(
                      MultipartUploadAccepter.FILE_NAME_PREFIX);
                      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${\n                          files.iterationCountSinceThisPhaseBegan\n                      }", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
                      out.write('\'');
                      out.write(',');
                      out.write('\'');
                      out.print(MultipartUploadAccepter.FILE_PREFIX);
                      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${\n                          files.iterationCountSinceThisPhaseBegan}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
                      out.write("')\">\n            </td>\n          </tr>\n          <tr>\n            <td colspan=\"3\">\n              <hr size=\"1\" color=\"#32357D\" />\n            </td>\n          </tr>\n          ");
                      int evalDoAfterBody = _jspx_th_ctl_iterator_0.doAfterBody();
                      files = (org.recipnet.common.controls.SimpleIterator) _jspx_page_context.findAttribute("files");
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_ctl_iterator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_ctl_iterator_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  files = (org.recipnet.common.controls.SimpleIterator) _jspx_page_context.findAttribute("files");
                  _jspx_tagPool_ctl_iterator_iterations_id.reuse(_jspx_th_ctl_iterator_0);
                  out.write("\n          <tr>\n            <td colspan=\"3\" class=\"formButtons\" style=\"padding: 0.25em;\">\n              <input type=\"submit\" value=\"Upload now\" />\n            </td>\n          </tr>\n        </table>\n      ");
                  int evalDoAfterBody = _jspx_th_ctl_form_0.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_ctl_form_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_ctl_form_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_ctl_form_pageForm_method_enctype_action.reuse(_jspx_th_ctl_form_0);
              out.write("\n    <p class=\"navLinks\">\n      ");
              if (_jspx_meth_rn_link_0(_jspx_th_rn_sampleChecker_1, _jspx_page_context))
                return;
              out.write("\n      <br />\n      ");
              if (_jspx_meth_rn_link_1(_jspx_th_rn_sampleChecker_1, _jspx_page_context))
                return;
              out.write("\n      <br />\n      ");
              if (_jspx_meth_rn_link_2(_jspx_th_rn_sampleChecker_1, _jspx_page_context))
                return;
              out.write("\n      <br />\n      ");
              if (_jspx_meth_rn_link_3(_jspx_th_rn_sampleChecker_1, _jspx_page_context))
                return;
              out.write("\n    </p>\n  ");
              int evalDoAfterBody = _jspx_th_rn_sampleChecker_1.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
            if (_jspx_eval_rn_sampleChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
              out = _jspx_page_context.popBody();
          }
          if (_jspx_th_rn_sampleChecker_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
            return;
          _jspx_tagPool_rn_sampleChecker_invert_includeIfRetracted.reuse(_jspx_th_rn_sampleChecker_1);
          out.write("\n  </div>\n  ");
          if (_jspx_meth_ctl_styleBlock_0(_jspx_th_rn_samplePage_0, _jspx_page_context))
            return;
          out.write("\n  <script language=\"JavaScript\">\n    <!-- // begin hiding javascript from older browsers\n\n        var manuallyUpdatedFieldNames = \"\";\n\n        function updatedField(fieldName) {\n            if (manuallyUpdatedFieldNames.indexOf(fieldName)==-1) {\n                manuallyUpdatedFieldNames=manuallyUpdatedFieldNames+fieldName;\n            }\n        }\n\n        // This function sets the form field with the name given as the\n        // remoteNameBoxId parameter to the filename to the filename entered\n        // in the localNameBoxId box (with the path removed) if there is no\n        // remote filename entered.\n\n        // Netscape 4.8 does not trigger onChange() when the \"Browse\"\n        // button of the FileUpload element is clicked, so it is\n        // neccessary to include a call to this function for the onBlur()\n        // event as well, to approximate the desired behavior\n        // onClick is triggered when the \"browse\" button of a FileUpload\n        // element is clicked for some browsers, though there is variance\n");
          out.write("        // in whether it is proccessed before or after the field is\n        // updated by the file selection dialog box.\n\t//\n\t// IE likes to give us a whole file path beginning with C:\\, while\n\t// Firefox 3.0.7 likes to give us just the file name with no slashes or\n\t// other reference to directories.  The function below accommodates\n\t// both.\n        function setRemoteName(remoteNameBoxId, localNameBoxId) {\n            if (manuallyUpdatedFieldNames.indexOf(remoteNameBoxId)!=-1) {\n              // the box shouldn't be updated\n              return;\n            } else {\n              var localPath = getElementById(localNameBoxId).value;\n              var lastSlash = 0;\n                for (var j = 0; j < localPath.length; j ++) {\n                  if (localPath.charAt(j) == '/'\n                      || localPath.charAt(j) == '\\\\') {\n                    lastSlash = j + 1;\n                  }\n                }\n              getElementById(remoteNameBoxId).value\n                  = localPath.substring(lastSlash);\n            }\n");
          out.write("        }\n\n        function getElementById(id) {\n          for (var fi = 0; fi < document.forms.length; fi ++) {\n            var index = 0;\n            while (index < document.forms[fi].elements.length) {\n              if (document.forms[fi].elements[index].name == id) {\n                return document.forms[fi].elements[index];\n              } else {\n                  index ++;\n              }\n            }\n          }\n        }\n\n        function getElementByIdPart(idPart) {\n          for (var fi = 0; fi < document.forms.length; fi ++) {\n            var index = 0;\n            while (index < document.forms[fi].elements.length) {\n              if (document.forms[fi].elements[index].name.indexOf(idPart)==-1) {\n                index++;\n              } else {\n                return document.forms[fi].elements[index];\n              }\n            }\n          }\n        }\n\n        // if any checkbox element in the page with the name 'checkboxName'\n        // is checked, the button 'buttonName' will be enabled, otherwise it\n");
          out.write("        // will be disabled\n        function enableDisableButton(buttonNamePart, checkboxNamePart) {\n          for (var fi = 0; fi < document.forms.length; fi ++) {\n            var index = 0;\n            while (index < document.forms[fi].elements.length) {\n              if (document.forms[fi].elements[index].name.indexOf(\n                  checkboxNamePart)!=-1) {\n                if (document.forms[fi].elements[index].checked==true) {\n                  setDisabled(buttonNamePart, false);\n                  return;\n                }\n              }\n              index ++;\n            }\n          }\n          setDisabled(buttonNamePart, true);\n        }\n\n        function setDisabled(buttonNamePart, isDisabled) {\n          for (var fi = 0; fi < document.forms.length; fi ++) {\n            for (var index = 0; index < document.forms[fi].elements.length;\n                    index++) {\n              if (document.forms[fi].elements[index].name.indexOf(\n                      buttonNamePart)==-1) {\n              } else {\n                document.forms[fi].elements[index].disabled=isDisabled;\n");
          out.write("              }\n            }\n          }\n        }\n        // stop hiding -->\n  </script>\n");
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
      _jspx_tagPool_rn_samplePage_title_loginPageUrl_ignoreSampleHistoryId_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName.reuse(_jspx_th_rn_samplePage_0);
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

  private boolean _jspx_meth_rn_sampleChecker_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_samplePage_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:sampleChecker
    org.recipnet.site.content.rncontrols.SampleChecker _jspx_th_rn_sampleChecker_0 = (org.recipnet.site.content.rncontrols.SampleChecker) _jspx_tagPool_rn_sampleChecker_includeIfRetracted.get(org.recipnet.site.content.rncontrols.SampleChecker.class);
    _jspx_th_rn_sampleChecker_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_sampleChecker_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_samplePage_0);
    _jspx_th_rn_sampleChecker_0.setIncludeIfRetracted(true);
    int _jspx_eval_rn_sampleChecker_0 = _jspx_th_rn_sampleChecker_0.doStartTag();
    if (_jspx_eval_rn_sampleChecker_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_sampleChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_sampleChecker_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_sampleChecker_0.doInitBody();
      }
      do {
        out.write("\n    <p class=errorMessage\">\n      This sample has been retracted by its lab and no more files may be\n      uploaded for it.\n    </p>\n  ");
        int evalDoAfterBody = _jspx_th_rn_sampleChecker_0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_sampleChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_sampleChecker_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_sampleChecker_includeIfRetracted.reuse(_jspx_th_rn_sampleChecker_0);
    return false;
  }

  private boolean _jspx_meth_ctl_extraHtmlAttribute_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleChecker_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:extraHtmlAttribute
    org.recipnet.common.controls.ExtraHtmlAttribute _jspx_th_ctl_extraHtmlAttribute_0 = (org.recipnet.common.controls.ExtraHtmlAttribute) _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.get(org.recipnet.common.controls.ExtraHtmlAttribute.class);
    _jspx_th_ctl_extraHtmlAttribute_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_extraHtmlAttribute_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleChecker_1);
    _jspx_th_ctl_extraHtmlAttribute_0.setName("onLoad");
    _jspx_th_ctl_extraHtmlAttribute_0.setValue("enableDisableButton('Button', 'selectedFiles')");
    int _jspx_eval_ctl_extraHtmlAttribute_0 = _jspx_th_ctl_extraHtmlAttribute_0.doStartTag();
    if (_jspx_th_ctl_extraHtmlAttribute_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.reuse(_jspx_th_ctl_extraHtmlAttribute_0);
    return false;
  }

  private boolean _jspx_meth_rn_link_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleChecker_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:link
    org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_0 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_href.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
    _jspx_th_rn_link_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_link_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleChecker_1);
    _jspx_th_rn_link_0.setHref("/lab/sample.jsp");
    int _jspx_eval_rn_link_0 = _jspx_th_rn_link_0.doStartTag();
    if (_jspx_eval_rn_link_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_link_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_link_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_link_0.doInitBody();
      }
      do {
        out.write("Back to \"Edit Sample\"...");
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

  private boolean _jspx_meth_rn_link_1(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleChecker_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:link
    org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_1 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_href.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
    _jspx_th_rn_link_1.setPageContext(_jspx_page_context);
    _jspx_th_rn_link_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleChecker_1);
    _jspx_th_rn_link_1.setHref("/lab/managefiles.jsp");
    int _jspx_eval_rn_link_1 = _jspx_th_rn_link_1.doStartTag();
    if (_jspx_eval_rn_link_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_link_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_link_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_link_1.doInitBody();
      }
      do {
        out.write("Manage files...");
        int evalDoAfterBody = _jspx_th_rn_link_1.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_link_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_link_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_link_href.reuse(_jspx_th_rn_link_1);
    return false;
  }

  private boolean _jspx_meth_rn_link_2(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleChecker_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:link
    org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_2 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_href.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
    _jspx_th_rn_link_2.setPageContext(_jspx_page_context);
    _jspx_th_rn_link_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleChecker_1);
    _jspx_th_rn_link_2.setHref("/jamm.jsp");
    int _jspx_eval_rn_link_2 = _jspx_th_rn_link_2.doStartTag();
    if (_jspx_eval_rn_link_2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_link_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_link_2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_link_2.doInitBody();
      }
      do {
        out.write("JaMM visualization...");
        int evalDoAfterBody = _jspx_th_rn_link_2.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_link_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_link_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_link_href.reuse(_jspx_th_rn_link_2);
    return false;
  }

  private boolean _jspx_meth_rn_link_3(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleChecker_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:link
    org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_3 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_href.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
    _jspx_th_rn_link_3.setPageContext(_jspx_page_context);
    _jspx_th_rn_link_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleChecker_1);
    _jspx_th_rn_link_3.setHref("/lab/uploadfilesapplet.jsp");
    int _jspx_eval_rn_link_3 = _jspx_th_rn_link_3.doStartTag();
    if (_jspx_eval_rn_link_3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_link_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_link_3.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_link_3.doInitBody();
      }
      do {
        out.write("\n        File Upload (Drag and Drop)...\n      ");
        int evalDoAfterBody = _jspx_th_rn_link_3.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_link_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_link_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_link_href.reuse(_jspx_th_rn_link_3);
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
        out.write("\n    table.bodyTable { margin-top: 1em; border: 3px solid #32357D;\n        background: #F0F0F0; color: #000050; }\n    table.bodyTable th,\n    table.bodyTable td { padding: 0 0.25em 0 0.25em; text-align: left;\n        font-weight: normal; font-style: normal; }\n    table.bodyTable th.tableHeader { background: #32357D; color: #FFFFFF;\n        padding: 0.25em; font-weight: bold; }\n    table.bodyTable th.mainHeading { padding-top: 0.5em; font-weight: bold; }\n    .navLinks { text-align: right; }\n  ");
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
