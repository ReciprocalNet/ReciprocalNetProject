package org.recipnet.site.content;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.recipnet.site.content.rncontrols.FileField;
import org.recipnet.site.content.rncontrols.JammModelElement;

public final class jamm_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static java.util.Vector _jspx_dependants;

  static {
    _jspx_dependants = new java.util.Vector(2);
    _jspx_dependants.add("/WEB-INF/controls.tld");
    _jspx_dependants.add("/WEB-INF/rncontrols.tld");
  }

  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_samplePage_title_loginPageUrl_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_jammModel_repositoryFile_id;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_jammApplet_width_id_height_appletParam_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_errorMessage_errorFilter;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_fileIterator_id_filterByExtension;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_parityChecker_includeOnlyOnFirst;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_jammAppletPreservingLink_useCrtFileAsLinkText_switchCrtFileUsingFileContext_jammAppletTag_disableWhenLinkMatchesAppletTag_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_fileChecker_includeOnlyIfFileHasDescription;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_fileField_fieldCode_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_parityChecker_includeOnlyOnLast;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_errorMessage_invertFilter_errorSupplier_errorFilter;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_showsampleLink_sampleIsKnownToBeLocal_detailedPageUrl_basicPageUrl;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleParam_name_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleHistoryParam_name_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_authorizationChecker_suppressRenderingOnly_canEditSample;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_link_href;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_jammAppletPreservingLink_switchApplet_jammAppletTag_disableWhenLinkMatchesAppletTag;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_styleBlock;

  public java.util.List getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _jspx_tagPool_rn_samplePage_title_loginPageUrl_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_jammModel_repositoryFile_id = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_jammApplet_width_id_height_appletParam_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_errorMessage_errorFilter = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_fileIterator_id_filterByExtension = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_parityChecker_includeOnlyOnFirst = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_jammAppletPreservingLink_useCrtFileAsLinkText_switchCrtFileUsingFileContext_jammAppletTag_disableWhenLinkMatchesAppletTag_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_fileChecker_includeOnlyIfFileHasDescription = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_fileField_fieldCode_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_parityChecker_includeOnlyOnLast = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_errorMessage_invertFilter_errorSupplier_errorFilter = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_showsampleLink_sampleIsKnownToBeLocal_detailedPageUrl_basicPageUrl = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleParam_name_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleHistoryParam_name_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_authorizationChecker_suppressRenderingOnly_canEditSample = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_link_href = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_jammAppletPreservingLink_switchApplet_jammAppletTag_disableWhenLinkMatchesAppletTag = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_styleBlock = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
  }

  public void _jspDestroy() {
    _jspx_tagPool_rn_samplePage_title_loginPageUrl_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName.release();
    _jspx_tagPool_rn_jammModel_repositoryFile_id.release();
    _jspx_tagPool_rn_jammApplet_width_id_height_appletParam_nobody.release();
    _jspx_tagPool_ctl_errorMessage_errorFilter.release();
    _jspx_tagPool_rn_fileIterator_id_filterByExtension.release();
    _jspx_tagPool_ctl_parityChecker_includeOnlyOnFirst.release();
    _jspx_tagPool_rn_jammAppletPreservingLink_useCrtFileAsLinkText_switchCrtFileUsingFileContext_jammAppletTag_disableWhenLinkMatchesAppletTag_nobody.release();
    _jspx_tagPool_rn_fileChecker_includeOnlyIfFileHasDescription.release();
    _jspx_tagPool_rn_fileField_fieldCode_nobody.release();
    _jspx_tagPool_ctl_parityChecker_includeOnlyOnLast.release();
    _jspx_tagPool_ctl_errorMessage_invertFilter_errorSupplier_errorFilter.release();
    _jspx_tagPool_rn_showsampleLink_sampleIsKnownToBeLocal_detailedPageUrl_basicPageUrl.release();
    _jspx_tagPool_rn_sampleParam_name_nobody.release();
    _jspx_tagPool_rn_sampleHistoryParam_name_nobody.release();
    _jspx_tagPool_rn_authorizationChecker_suppressRenderingOnly_canEditSample.release();
    _jspx_tagPool_rn_link_href.release();
    _jspx_tagPool_rn_jammAppletPreservingLink_switchApplet_jammAppletTag_disableWhenLinkMatchesAppletTag.release();
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
      //  rn:samplePage
      org.recipnet.site.content.rncontrols.SamplePage _jspx_th_rn_samplePage_0 = (org.recipnet.site.content.rncontrols.SamplePage) _jspx_tagPool_rn_samplePage_title_loginPageUrl_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName.get(org.recipnet.site.content.rncontrols.SamplePage.class);
      _jspx_th_rn_samplePage_0.setPageContext(_jspx_page_context);
      _jspx_th_rn_samplePage_0.setParent(null);
      _jspx_th_rn_samplePage_0.setTitle("JaMM Viewer");
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
          out.write("\n  <div class=\"pageBody\">\n  <table style=\"width: 100%;\">\n    <tr>\n      <td style=\"width: 600px;\"> \n        ");
          //  rn:jammModel
          org.recipnet.site.content.rncontrols.JammModelElement jammModel = null;
          org.recipnet.site.content.rncontrols.JammModelElement _jspx_th_rn_jammModel_0 = (org.recipnet.site.content.rncontrols.JammModelElement) _jspx_tagPool_rn_jammModel_repositoryFile_id.get(org.recipnet.site.content.rncontrols.JammModelElement.class);
          _jspx_th_rn_jammModel_0.setPageContext(_jspx_page_context);
          _jspx_th_rn_jammModel_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_samplePage_0);
          _jspx_th_rn_jammModel_0.setId("jammModel");
          _jspx_th_rn_jammModel_0.setRepositoryFile((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${param['crtFile']}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
          int _jspx_eval_rn_jammModel_0 = _jspx_th_rn_jammModel_0.doStartTag();
          if (_jspx_eval_rn_jammModel_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_rn_jammModel_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_rn_jammModel_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_rn_jammModel_0.doInitBody();
            }
            jammModel = (org.recipnet.site.content.rncontrols.JammModelElement) _jspx_page_context.findAttribute("jammModel");
            do {
              out.write("\n          ");
              //  rn:jammApplet
              org.recipnet.site.content.rncontrols.JammAppletTag jamm = null;
              org.recipnet.site.content.rncontrols.JammAppletTag _jspx_th_rn_jammApplet_0 = (org.recipnet.site.content.rncontrols.JammAppletTag) _jspx_tagPool_rn_jammApplet_width_id_height_appletParam_nobody.get(org.recipnet.site.content.rncontrols.JammAppletTag.class);
              _jspx_th_rn_jammApplet_0.setPageContext(_jspx_page_context);
              _jspx_th_rn_jammApplet_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_jammModel_0);
              _jspx_th_rn_jammApplet_0.setId("jamm");
              _jspx_th_rn_jammApplet_0.setWidth("600");
              _jspx_th_rn_jammApplet_0.setHeight("500");
              _jspx_th_rn_jammApplet_0.setAppletParam("jamm");
              int _jspx_eval_rn_jammApplet_0 = _jspx_th_rn_jammApplet_0.doStartTag();
              if (_jspx_th_rn_jammApplet_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              jamm = (org.recipnet.site.content.rncontrols.JammAppletTag) _jspx_page_context.findAttribute("jamm");
              _jspx_tagPool_rn_jammApplet_width_id_height_appletParam_nobody.reuse(_jspx_th_rn_jammApplet_0);
              out.write("\n          ");
              //  ctl:errorMessage
              org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_0 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
              _jspx_th_ctl_errorMessage_0.setPageContext(_jspx_page_context);
              _jspx_th_ctl_errorMessage_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_jammModel_0);
              _jspx_th_ctl_errorMessage_0.setErrorFilter( JammModelElement.NO_CRT_FILE_AVAILABLE );
              int _jspx_eval_ctl_errorMessage_0 = _jspx_th_ctl_errorMessage_0.doStartTag();
              if (_jspx_eval_ctl_errorMessage_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_ctl_errorMessage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_ctl_errorMessage_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_ctl_errorMessage_0.doInitBody();
                }
                do {
                  out.write("\n            <p class=\"errorMessage\">\n              No .crt file for this sample is currently available.\n            </p>\n          ");
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
              out.write("\n          ");
              //  ctl:errorMessage
              org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_1 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
              _jspx_th_ctl_errorMessage_1.setPageContext(_jspx_page_context);
              _jspx_th_ctl_errorMessage_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_jammModel_0);
              _jspx_th_ctl_errorMessage_1.setErrorFilter( JammModelElement.REPOSITORY_DIRECTORY_NOT_FOUND );
              int _jspx_eval_ctl_errorMessage_1 = _jspx_th_ctl_errorMessage_1.doStartTag();
              if (_jspx_eval_ctl_errorMessage_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_ctl_errorMessage_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_ctl_errorMessage_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_ctl_errorMessage_1.doInitBody();
                }
                do {
                  out.write("\n            <p class=\"errorMessage\">\n              The repository directory for this sample was not found.\n            </p>\n          ");
                  int evalDoAfterBody = _jspx_th_ctl_errorMessage_1.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_ctl_errorMessage_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_ctl_errorMessage_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_ctl_errorMessage_errorFilter.reuse(_jspx_th_ctl_errorMessage_1);
              out.write("\n        ");
              int evalDoAfterBody = _jspx_th_rn_jammModel_0.doAfterBody();
              jammModel = (org.recipnet.site.content.rncontrols.JammModelElement) _jspx_page_context.findAttribute("jammModel");
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
            if (_jspx_eval_rn_jammModel_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
              out = _jspx_page_context.popBody();
          }
          if (_jspx_th_rn_jammModel_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
            return;
          jammModel = (org.recipnet.site.content.rncontrols.JammModelElement) _jspx_page_context.findAttribute("jammModel");
          _jspx_tagPool_rn_jammModel_repositoryFile_id.reuse(_jspx_th_rn_jammModel_0);
          out.write("\n      </td>\n      <td style=\"padding-left: 1em;\">\n        ");
          //  rn:fileIterator
          org.recipnet.site.content.rncontrols.FileIterator fileIt = null;
          org.recipnet.site.content.rncontrols.FileIterator _jspx_th_rn_fileIterator_0 = (org.recipnet.site.content.rncontrols.FileIterator) _jspx_tagPool_rn_fileIterator_id_filterByExtension.get(org.recipnet.site.content.rncontrols.FileIterator.class);
          _jspx_th_rn_fileIterator_0.setPageContext(_jspx_page_context);
          _jspx_th_rn_fileIterator_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_samplePage_0);
          _jspx_th_rn_fileIterator_0.setId("fileIt");
          _jspx_th_rn_fileIterator_0.setFilterByExtension("crt");
          int _jspx_eval_rn_fileIterator_0 = _jspx_th_rn_fileIterator_0.doStartTag();
          if (_jspx_eval_rn_fileIterator_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_rn_fileIterator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_rn_fileIterator_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_rn_fileIterator_0.doInitBody();
            }
            fileIt = (org.recipnet.site.content.rncontrols.FileIterator) _jspx_page_context.findAttribute("fileIt");
            do {
              out.write("\n          ");
              if (_jspx_meth_ctl_parityChecker_0(_jspx_th_rn_fileIterator_0, _jspx_page_context))
                return;
              out.write("\n            <tr>\n              <td>\n                ");
              if (_jspx_meth_rn_jammAppletPreservingLink_0(_jspx_th_rn_fileIterator_0, _jspx_page_context))
                return;
              out.write("\n              </td>\n              ");
              //  rn:fileChecker
              org.recipnet.site.content.rncontrols.FileChecker _jspx_th_rn_fileChecker_0 = (org.recipnet.site.content.rncontrols.FileChecker) _jspx_tagPool_rn_fileChecker_includeOnlyIfFileHasDescription.get(org.recipnet.site.content.rncontrols.FileChecker.class);
              _jspx_th_rn_fileChecker_0.setPageContext(_jspx_page_context);
              _jspx_th_rn_fileChecker_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_fileIterator_0);
              _jspx_th_rn_fileChecker_0.setIncludeOnlyIfFileHasDescription(true);
              int _jspx_eval_rn_fileChecker_0 = _jspx_th_rn_fileChecker_0.doStartTag();
              if (_jspx_eval_rn_fileChecker_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_rn_fileChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_rn_fileChecker_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_rn_fileChecker_0.doInitBody();
                }
                do {
                  out.write("\n                <td>-</td>\n                <td>\n                  ");
                  //  rn:fileField
                  org.recipnet.site.content.rncontrols.FileField _jspx_th_rn_fileField_0 = (org.recipnet.site.content.rncontrols.FileField) _jspx_tagPool_rn_fileField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.FileField.class);
                  _jspx_th_rn_fileField_0.setPageContext(_jspx_page_context);
                  _jspx_th_rn_fileField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_fileChecker_0);
                  _jspx_th_rn_fileField_0.setFieldCode(FileField.DESCRIPTION);
                  int _jspx_eval_rn_fileField_0 = _jspx_th_rn_fileField_0.doStartTag();
                  if (_jspx_th_rn_fileField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_fileField_fieldCode_nobody.reuse(_jspx_th_rn_fileField_0);
                  out.write("\n                </td>\n              ");
                  int evalDoAfterBody = _jspx_th_rn_fileChecker_0.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_rn_fileChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_rn_fileChecker_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_fileChecker_includeOnlyIfFileHasDescription.reuse(_jspx_th_rn_fileChecker_0);
              out.write("\n            </tr>\n          ");
              if (_jspx_meth_ctl_parityChecker_1(_jspx_th_rn_fileIterator_0, _jspx_page_context))
                return;
              out.write("\n        ");
              int evalDoAfterBody = _jspx_th_rn_fileIterator_0.doAfterBody();
              fileIt = (org.recipnet.site.content.rncontrols.FileIterator) _jspx_page_context.findAttribute("fileIt");
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
            if (_jspx_eval_rn_fileIterator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
              out = _jspx_page_context.popBody();
          }
          if (_jspx_th_rn_fileIterator_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
            return;
          fileIt = (org.recipnet.site.content.rncontrols.FileIterator) _jspx_page_context.findAttribute("fileIt");
          _jspx_tagPool_rn_fileIterator_id_filterByExtension.reuse(_jspx_th_rn_fileIterator_0);
          out.write("\n        ");
          //  ctl:errorMessage
          org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_2 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_invertFilter_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
          _jspx_th_ctl_errorMessage_2.setPageContext(_jspx_page_context);
          _jspx_th_ctl_errorMessage_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_samplePage_0);
          _jspx_th_ctl_errorMessage_2.setErrorSupplier((org.recipnet.common.controls.ErrorSupplier) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${jammModel}", org.recipnet.common.controls.ErrorSupplier.class, (PageContext)_jspx_page_context, null, false));
          _jspx_th_ctl_errorMessage_2.setInvertFilter(true);
          _jspx_th_ctl_errorMessage_2.setErrorFilter((JammModelElement.NO_CRT_FILE_AVAILABLE
                    | JammModelElement.REPOSITORY_DIRECTORY_NOT_FOUND));
          int _jspx_eval_ctl_errorMessage_2 = _jspx_th_ctl_errorMessage_2.doStartTag();
          if (_jspx_eval_ctl_errorMessage_2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_ctl_errorMessage_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_ctl_errorMessage_2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_ctl_errorMessage_2.doInitBody();
            }
            do {
              out.write("\n          <div style=\"margin-top: 2.5em;\">\n            ");
              if (_jspx_meth_rn_showsampleLink_0(_jspx_th_ctl_errorMessage_2, _jspx_page_context))
                return;
              out.write("<br />\n            ");
              if (_jspx_meth_rn_authorizationChecker_0(_jspx_th_ctl_errorMessage_2, _jspx_page_context))
                return;
              out.write("\n          </div>\n        ");
              int evalDoAfterBody = _jspx_th_ctl_errorMessage_2.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
            if (_jspx_eval_ctl_errorMessage_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
              out = _jspx_page_context.popBody();
          }
          if (_jspx_th_ctl_errorMessage_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
            return;
          _jspx_tagPool_ctl_errorMessage_invertFilter_errorSupplier_errorFilter.reuse(_jspx_th_ctl_errorMessage_2);
          out.write("\n      </td>\n    </tr>\n  </table>\n  ");
          //  ctl:errorMessage
          org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_3 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_invertFilter_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
          _jspx_th_ctl_errorMessage_3.setPageContext(_jspx_page_context);
          _jspx_th_ctl_errorMessage_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_samplePage_0);
          _jspx_th_ctl_errorMessage_3.setErrorSupplier((org.recipnet.common.controls.ErrorSupplier) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${jammModel}", org.recipnet.common.controls.ErrorSupplier.class, (PageContext)_jspx_page_context, null, false));
          _jspx_th_ctl_errorMessage_3.setInvertFilter(true);
          _jspx_th_ctl_errorMessage_3.setErrorFilter((JammModelElement.NO_CRT_FILE_AVAILABLE
              | JammModelElement.REPOSITORY_DIRECTORY_NOT_FOUND));
          int _jspx_eval_ctl_errorMessage_3 = _jspx_th_ctl_errorMessage_3.doStartTag();
          if (_jspx_eval_ctl_errorMessage_3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_ctl_errorMessage_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_ctl_errorMessage_3.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_ctl_errorMessage_3.doInitBody();
            }
            do {
              out.write("\n    <hr />\n    ");
              if (_jspx_meth_rn_jammAppletPreservingLink_1(_jspx_th_ctl_errorMessage_3, _jspx_page_context))
                return;
              out.write("\n    - works with any Java-capable browser<br />\n    ");
              if (_jspx_meth_rn_jammAppletPreservingLink_2(_jspx_th_ctl_errorMessage_3, _jspx_page_context))
                return;
              out.write("\n    - requires Sun Java plug-in 1.2 or higher (1.4 recommended)<br />\n    ");
              if (_jspx_meth_rn_jammAppletPreservingLink_3(_jspx_th_ctl_errorMessage_3, _jspx_page_context))
                return;
              out.write("\n    - works with any Java-capable browser<br />\n  ");
              int evalDoAfterBody = _jspx_th_ctl_errorMessage_3.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
            if (_jspx_eval_ctl_errorMessage_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
              out = _jspx_page_context.popBody();
          }
          if (_jspx_th_ctl_errorMessage_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
            return;
          _jspx_tagPool_ctl_errorMessage_invertFilter_errorSupplier_errorFilter.reuse(_jspx_th_ctl_errorMessage_3);
          out.write("\n  </div>\n  ");
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

  private boolean _jspx_meth_ctl_parityChecker_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_fileIterator_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:parityChecker
    org.recipnet.common.controls.ParityChecker _jspx_th_ctl_parityChecker_0 = (org.recipnet.common.controls.ParityChecker) _jspx_tagPool_ctl_parityChecker_includeOnlyOnFirst.get(org.recipnet.common.controls.ParityChecker.class);
    _jspx_th_ctl_parityChecker_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_parityChecker_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_fileIterator_0);
    _jspx_th_ctl_parityChecker_0.setIncludeOnlyOnFirst(true);
    int _jspx_eval_ctl_parityChecker_0 = _jspx_th_ctl_parityChecker_0.doStartTag();
    if (_jspx_eval_ctl_parityChecker_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_parityChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_parityChecker_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_parityChecker_0.doInitBody();
      }
      do {
        out.write("\n          <table>\n          ");
        int evalDoAfterBody = _jspx_th_ctl_parityChecker_0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_parityChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_parityChecker_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_parityChecker_includeOnlyOnFirst.reuse(_jspx_th_ctl_parityChecker_0);
    return false;
  }

  private boolean _jspx_meth_rn_jammAppletPreservingLink_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_fileIterator_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:jammAppletPreservingLink
    org.recipnet.site.content.rncontrols.JammAppletPreservingLink _jspx_th_rn_jammAppletPreservingLink_0 = (org.recipnet.site.content.rncontrols.JammAppletPreservingLink) _jspx_tagPool_rn_jammAppletPreservingLink_useCrtFileAsLinkText_switchCrtFileUsingFileContext_jammAppletTag_disableWhenLinkMatchesAppletTag_nobody.get(org.recipnet.site.content.rncontrols.JammAppletPreservingLink.class);
    _jspx_th_rn_jammAppletPreservingLink_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_jammAppletPreservingLink_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_fileIterator_0);
    _jspx_th_rn_jammAppletPreservingLink_0.setJammAppletTag((org.recipnet.site.content.rncontrols.JammAppletTag) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${jamm}", org.recipnet.site.content.rncontrols.JammAppletTag.class, (PageContext)_jspx_page_context, null, false));
    _jspx_th_rn_jammAppletPreservingLink_0.setSwitchCrtFileUsingFileContext((org.recipnet.site.content.rncontrols.FileContext) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${fileIt}", org.recipnet.site.content.rncontrols.FileContext.class, (PageContext)_jspx_page_context, null, false));
    _jspx_th_rn_jammAppletPreservingLink_0.setUseCrtFileAsLinkText(true);
    _jspx_th_rn_jammAppletPreservingLink_0.setDisableWhenLinkMatchesAppletTag(true);
    int _jspx_eval_rn_jammAppletPreservingLink_0 = _jspx_th_rn_jammAppletPreservingLink_0.doStartTag();
    if (_jspx_th_rn_jammAppletPreservingLink_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_jammAppletPreservingLink_useCrtFileAsLinkText_switchCrtFileUsingFileContext_jammAppletTag_disableWhenLinkMatchesAppletTag_nobody.reuse(_jspx_th_rn_jammAppletPreservingLink_0);
    return false;
  }

  private boolean _jspx_meth_ctl_parityChecker_1(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_fileIterator_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:parityChecker
    org.recipnet.common.controls.ParityChecker _jspx_th_ctl_parityChecker_1 = (org.recipnet.common.controls.ParityChecker) _jspx_tagPool_ctl_parityChecker_includeOnlyOnLast.get(org.recipnet.common.controls.ParityChecker.class);
    _jspx_th_ctl_parityChecker_1.setPageContext(_jspx_page_context);
    _jspx_th_ctl_parityChecker_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_fileIterator_0);
    _jspx_th_ctl_parityChecker_1.setIncludeOnlyOnLast(true);
    int _jspx_eval_ctl_parityChecker_1 = _jspx_th_ctl_parityChecker_1.doStartTag();
    if (_jspx_eval_ctl_parityChecker_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_parityChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_parityChecker_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_parityChecker_1.doInitBody();
      }
      do {
        out.write("\n          </table>\n          ");
        int evalDoAfterBody = _jspx_th_ctl_parityChecker_1.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_parityChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_parityChecker_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_parityChecker_includeOnlyOnLast.reuse(_jspx_th_ctl_parityChecker_1);
    return false;
  }

  private boolean _jspx_meth_rn_showsampleLink_0(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_errorMessage_2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:showsampleLink
    org.recipnet.site.content.rncontrols.ShowsampleLink _jspx_th_rn_showsampleLink_0 = (org.recipnet.site.content.rncontrols.ShowsampleLink) _jspx_tagPool_rn_showsampleLink_sampleIsKnownToBeLocal_detailedPageUrl_basicPageUrl.get(org.recipnet.site.content.rncontrols.ShowsampleLink.class);
    _jspx_th_rn_showsampleLink_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_showsampleLink_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_errorMessage_2);
    _jspx_th_rn_showsampleLink_0.setBasicPageUrl("/showsamplebasic.jsp");
    _jspx_th_rn_showsampleLink_0.setDetailedPageUrl("/showsampledetailed.jsp");
    _jspx_th_rn_showsampleLink_0.setSampleIsKnownToBeLocal(true);
    int _jspx_eval_rn_showsampleLink_0 = _jspx_th_rn_showsampleLink_0.doStartTag();
    if (_jspx_eval_rn_showsampleLink_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_showsampleLink_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_showsampleLink_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_showsampleLink_0.doInitBody();
      }
      do {
        out.write("\n              ");
        if (_jspx_meth_rn_sampleParam_0(_jspx_th_rn_showsampleLink_0, _jspx_page_context))
          return true;
        out.write("\n              ");
        if (_jspx_meth_rn_sampleHistoryParam_0(_jspx_th_rn_showsampleLink_0, _jspx_page_context))
          return true;
        out.write("\n              Sample Information...\n            ");
        int evalDoAfterBody = _jspx_th_rn_showsampleLink_0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_showsampleLink_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_showsampleLink_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_showsampleLink_sampleIsKnownToBeLocal_detailedPageUrl_basicPageUrl.reuse(_jspx_th_rn_showsampleLink_0);
    return false;
  }

  private boolean _jspx_meth_rn_sampleParam_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_showsampleLink_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:sampleParam
    org.recipnet.site.content.rncontrols.SampleParam _jspx_th_rn_sampleParam_0 = (org.recipnet.site.content.rncontrols.SampleParam) _jspx_tagPool_rn_sampleParam_name_nobody.get(org.recipnet.site.content.rncontrols.SampleParam.class);
    _jspx_th_rn_sampleParam_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_sampleParam_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_showsampleLink_0);
    _jspx_th_rn_sampleParam_0.setName("sampleId");
    int _jspx_eval_rn_sampleParam_0 = _jspx_th_rn_sampleParam_0.doStartTag();
    if (_jspx_th_rn_sampleParam_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_sampleParam_name_nobody.reuse(_jspx_th_rn_sampleParam_0);
    return false;
  }

  private boolean _jspx_meth_rn_sampleHistoryParam_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_showsampleLink_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:sampleHistoryParam
    org.recipnet.site.content.rncontrols.SampleHistoryParam _jspx_th_rn_sampleHistoryParam_0 = (org.recipnet.site.content.rncontrols.SampleHistoryParam) _jspx_tagPool_rn_sampleHistoryParam_name_nobody.get(org.recipnet.site.content.rncontrols.SampleHistoryParam.class);
    _jspx_th_rn_sampleHistoryParam_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_sampleHistoryParam_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_showsampleLink_0);
    _jspx_th_rn_sampleHistoryParam_0.setName("sampleHistoryId");
    int _jspx_eval_rn_sampleHistoryParam_0 = _jspx_th_rn_sampleHistoryParam_0.doStartTag();
    if (_jspx_th_rn_sampleHistoryParam_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_sampleHistoryParam_name_nobody.reuse(_jspx_th_rn_sampleHistoryParam_0);
    return false;
  }

  private boolean _jspx_meth_rn_authorizationChecker_0(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_errorMessage_2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:authorizationChecker
    org.recipnet.site.content.rncontrols.AuthorizationChecker _jspx_th_rn_authorizationChecker_0 = (org.recipnet.site.content.rncontrols.AuthorizationChecker) _jspx_tagPool_rn_authorizationChecker_suppressRenderingOnly_canEditSample.get(org.recipnet.site.content.rncontrols.AuthorizationChecker.class);
    _jspx_th_rn_authorizationChecker_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_authorizationChecker_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_errorMessage_2);
    _jspx_th_rn_authorizationChecker_0.setCanEditSample(true);
    _jspx_th_rn_authorizationChecker_0.setSuppressRenderingOnly(true);
    int _jspx_eval_rn_authorizationChecker_0 = _jspx_th_rn_authorizationChecker_0.doStartTag();
    if (_jspx_eval_rn_authorizationChecker_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_authorizationChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_authorizationChecker_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_authorizationChecker_0.doInitBody();
      }
      do {
        out.write("\n              ");
        if (_jspx_meth_rn_link_0(_jspx_th_rn_authorizationChecker_0, _jspx_page_context))
          return true;
        out.write("\n            ");
        int evalDoAfterBody = _jspx_th_rn_authorizationChecker_0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_authorizationChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_authorizationChecker_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_authorizationChecker_suppressRenderingOnly_canEditSample.reuse(_jspx_th_rn_authorizationChecker_0);
    return false;
  }

  private boolean _jspx_meth_rn_link_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_authorizationChecker_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:link
    org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_0 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_href.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
    _jspx_th_rn_link_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_link_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_0);
    _jspx_th_rn_link_0.setHref("/lab/sample.jsp");
    int _jspx_eval_rn_link_0 = _jspx_th_rn_link_0.doStartTag();
    if (_jspx_eval_rn_link_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_link_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_link_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_link_0.doInitBody();
      }
      do {
        out.write("Edit Sample...");
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

  private boolean _jspx_meth_rn_jammAppletPreservingLink_1(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_errorMessage_3, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:jammAppletPreservingLink
    org.recipnet.site.content.rncontrols.JammAppletPreservingLink _jspx_th_rn_jammAppletPreservingLink_1 = (org.recipnet.site.content.rncontrols.JammAppletPreservingLink) _jspx_tagPool_rn_jammAppletPreservingLink_switchApplet_jammAppletTag_disableWhenLinkMatchesAppletTag.get(org.recipnet.site.content.rncontrols.JammAppletPreservingLink.class);
    _jspx_th_rn_jammAppletPreservingLink_1.setPageContext(_jspx_page_context);
    _jspx_th_rn_jammAppletPreservingLink_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_errorMessage_3);
    _jspx_th_rn_jammAppletPreservingLink_1.setJammAppletTag((org.recipnet.site.content.rncontrols.JammAppletTag) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${jamm}", org.recipnet.site.content.rncontrols.JammAppletTag.class, (PageContext)_jspx_page_context, null, false));
    _jspx_th_rn_jammAppletPreservingLink_1.setSwitchApplet("JaMM1");
    _jspx_th_rn_jammAppletPreservingLink_1.setDisableWhenLinkMatchesAppletTag(true);
    int _jspx_eval_rn_jammAppletPreservingLink_1 = _jspx_th_rn_jammAppletPreservingLink_1.doStartTag();
    if (_jspx_eval_rn_jammAppletPreservingLink_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_jammAppletPreservingLink_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_jammAppletPreservingLink_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_jammAppletPreservingLink_1.doInitBody();
      }
      do {
        out.write("JaMM1");
        int evalDoAfterBody = _jspx_th_rn_jammAppletPreservingLink_1.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_jammAppletPreservingLink_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_jammAppletPreservingLink_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_jammAppletPreservingLink_switchApplet_jammAppletTag_disableWhenLinkMatchesAppletTag.reuse(_jspx_th_rn_jammAppletPreservingLink_1);
    return false;
  }

  private boolean _jspx_meth_rn_jammAppletPreservingLink_2(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_errorMessage_3, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:jammAppletPreservingLink
    org.recipnet.site.content.rncontrols.JammAppletPreservingLink _jspx_th_rn_jammAppletPreservingLink_2 = (org.recipnet.site.content.rncontrols.JammAppletPreservingLink) _jspx_tagPool_rn_jammAppletPreservingLink_switchApplet_jammAppletTag_disableWhenLinkMatchesAppletTag.get(org.recipnet.site.content.rncontrols.JammAppletPreservingLink.class);
    _jspx_th_rn_jammAppletPreservingLink_2.setPageContext(_jspx_page_context);
    _jspx_th_rn_jammAppletPreservingLink_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_errorMessage_3);
    _jspx_th_rn_jammAppletPreservingLink_2.setJammAppletTag((org.recipnet.site.content.rncontrols.JammAppletTag) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${jamm}", org.recipnet.site.content.rncontrols.JammAppletTag.class, (PageContext)_jspx_page_context, null, false));
    _jspx_th_rn_jammAppletPreservingLink_2.setSwitchApplet("JaMM2");
    _jspx_th_rn_jammAppletPreservingLink_2.setDisableWhenLinkMatchesAppletTag(true);
    int _jspx_eval_rn_jammAppletPreservingLink_2 = _jspx_th_rn_jammAppletPreservingLink_2.doStartTag();
    if (_jspx_eval_rn_jammAppletPreservingLink_2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_jammAppletPreservingLink_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_jammAppletPreservingLink_2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_jammAppletPreservingLink_2.doInitBody();
      }
      do {
        out.write("JaMM2");
        int evalDoAfterBody = _jspx_th_rn_jammAppletPreservingLink_2.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_jammAppletPreservingLink_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_jammAppletPreservingLink_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_jammAppletPreservingLink_switchApplet_jammAppletTag_disableWhenLinkMatchesAppletTag.reuse(_jspx_th_rn_jammAppletPreservingLink_2);
    return false;
  }

  private boolean _jspx_meth_rn_jammAppletPreservingLink_3(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_errorMessage_3, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:jammAppletPreservingLink
    org.recipnet.site.content.rncontrols.JammAppletPreservingLink _jspx_th_rn_jammAppletPreservingLink_3 = (org.recipnet.site.content.rncontrols.JammAppletPreservingLink) _jspx_tagPool_rn_jammAppletPreservingLink_switchApplet_jammAppletTag_disableWhenLinkMatchesAppletTag.get(org.recipnet.site.content.rncontrols.JammAppletPreservingLink.class);
    _jspx_th_rn_jammAppletPreservingLink_3.setPageContext(_jspx_page_context);
    _jspx_th_rn_jammAppletPreservingLink_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_errorMessage_3);
    _jspx_th_rn_jammAppletPreservingLink_3.setJammAppletTag((org.recipnet.site.content.rncontrols.JammAppletTag) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${jamm}", org.recipnet.site.content.rncontrols.JammAppletTag.class, (PageContext)_jspx_page_context, null, false));
    _jspx_th_rn_jammAppletPreservingLink_3.setSwitchApplet("miniJaMM");
    _jspx_th_rn_jammAppletPreservingLink_3.setDisableWhenLinkMatchesAppletTag(true);
    int _jspx_eval_rn_jammAppletPreservingLink_3 = _jspx_th_rn_jammAppletPreservingLink_3.doStartTag();
    if (_jspx_eval_rn_jammAppletPreservingLink_3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_jammAppletPreservingLink_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_jammAppletPreservingLink_3.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_jammAppletPreservingLink_3.doInitBody();
      }
      do {
        out.write("miniJaMM");
        int evalDoAfterBody = _jspx_th_rn_jammAppletPreservingLink_3.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_jammAppletPreservingLink_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_jammAppletPreservingLink_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_jammAppletPreservingLink_switchApplet_jammAppletTag_disableWhenLinkMatchesAppletTag.reuse(_jspx_th_rn_jammAppletPreservingLink_3);
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
        out.write("\n    div.pageBody { text-align: left; }\n    .indent { margin-left: 1em; }\n  ");
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
