package org.recipnet.site.content.lab;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.recipnet.common.controls.HtmlPageIterator;
import org.recipnet.site.content.rncontrols.FileField;
import org.recipnet.site.content.rncontrols.SampleHistoryField;

public final class uploadcompleted_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static java.util.Vector _jspx_dependants;

  static {
    _jspx_dependants = new java.util.Vector(2);
    _jspx_dependants.add("/WEB-INF/controls.tld");
    _jspx_dependants.add("/WEB-INF/rncontrols.tld");
  }

  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_uploadConfirmationPage_title_secondsUntilRedirect_redirectTargetPageUrl;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_filenameIterator_id;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_fileField_fieldCode_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_parityChecker_includeOnlyOnLast;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleHistoryTranslator_translateFromFileContext;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_workflowCommentChecker;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleHistoryField_fieldCode_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_link_href;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_styleBlock;

  public java.util.List getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _jspx_tagPool_rn_uploadConfirmationPage_title_secondsUntilRedirect_redirectTargetPageUrl = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_filenameIterator_id = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_fileField_fieldCode_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_parityChecker_includeOnlyOnLast = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleHistoryTranslator_translateFromFileContext = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_workflowCommentChecker = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleHistoryField_fieldCode_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_link_href = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_styleBlock = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
  }

  public void _jspDestroy() {
    _jspx_tagPool_rn_uploadConfirmationPage_title_secondsUntilRedirect_redirectTargetPageUrl.release();
    _jspx_tagPool_rn_filenameIterator_id.release();
    _jspx_tagPool_rn_fileField_fieldCode_nobody.release();
    _jspx_tagPool_ctl_parityChecker_includeOnlyOnLast.release();
    _jspx_tagPool_rn_sampleHistoryTranslator_translateFromFileContext.release();
    _jspx_tagPool_rn_workflowCommentChecker.release();
    _jspx_tagPool_rn_sampleHistoryField_fieldCode_nobody.release();
    _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.release();
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

      out.write("\n\n\n\n\n\n");
      //  rn:uploadConfirmationPage
      org.recipnet.site.content.rncontrols.UploadConfirmationPage _jspx_th_rn_uploadConfirmationPage_0 = (org.recipnet.site.content.rncontrols.UploadConfirmationPage) _jspx_tagPool_rn_uploadConfirmationPage_title_secondsUntilRedirect_redirectTargetPageUrl.get(org.recipnet.site.content.rncontrols.UploadConfirmationPage.class);
      _jspx_th_rn_uploadConfirmationPage_0.setPageContext(_jspx_page_context);
      _jspx_th_rn_uploadConfirmationPage_0.setParent(null);
      _jspx_th_rn_uploadConfirmationPage_0.setTitle("Upload Completed");
      _jspx_th_rn_uploadConfirmationPage_0.setSecondsUntilRedirect(5);
      _jspx_th_rn_uploadConfirmationPage_0.setRedirectTargetPageUrl("/lab/managefiles.jsp");
      int _jspx_eval_rn_uploadConfirmationPage_0 = _jspx_th_rn_uploadConfirmationPage_0.doStartTag();
      if (_jspx_eval_rn_uploadConfirmationPage_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        org.recipnet.site.content.rncontrols.UploadConfirmationPage htmlPage = null;
        if (_jspx_eval_rn_uploadConfirmationPage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
          out = _jspx_page_context.pushBody();
          _jspx_th_rn_uploadConfirmationPage_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
          _jspx_th_rn_uploadConfirmationPage_0.doInitBody();
        }
        htmlPage = (org.recipnet.site.content.rncontrols.UploadConfirmationPage) _jspx_page_context.findAttribute("htmlPage");
        do {
          out.write("\n  <center>\n    <p>\n      <table class=\"table\" cellspacing=\"0\">\n        <tr>\n          <th colspan=\"2\" class=\"tableHeader\">\n            The upload operation has completed:\n          </th>\n        </tr>\n        ");
          //  rn:filenameIterator
          org.recipnet.site.content.rncontrols.FilenameIterator fileIt = null;
          org.recipnet.site.content.rncontrols.FilenameIterator _jspx_th_rn_filenameIterator_0 = (org.recipnet.site.content.rncontrols.FilenameIterator) _jspx_tagPool_rn_filenameIterator_id.get(org.recipnet.site.content.rncontrols.FilenameIterator.class);
          _jspx_th_rn_filenameIterator_0.setPageContext(_jspx_page_context);
          _jspx_th_rn_filenameIterator_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_uploadConfirmationPage_0);
          _jspx_th_rn_filenameIterator_0.setId("fileIt");
          int _jspx_eval_rn_filenameIterator_0 = _jspx_th_rn_filenameIterator_0.doStartTag();
          if (_jspx_eval_rn_filenameIterator_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_rn_filenameIterator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_rn_filenameIterator_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_rn_filenameIterator_0.doInitBody();
            }
            fileIt = (org.recipnet.site.content.rncontrols.FilenameIterator) _jspx_page_context.findAttribute("fileIt");
            do {
              out.write("\n          <tr>\n            <td class=\"tableInstructions\" align=\"left\" colspan=\"2\">\n              <strong>");
              //  rn:fileField
              org.recipnet.site.content.rncontrols.FileField _jspx_th_rn_fileField_0 = (org.recipnet.site.content.rncontrols.FileField) _jspx_tagPool_rn_fileField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.FileField.class);
              _jspx_th_rn_fileField_0.setPageContext(_jspx_page_context);
              _jspx_th_rn_fileField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_filenameIterator_0);
              _jspx_th_rn_fileField_0.setFieldCode(FileField.FILENAME);
              int _jspx_eval_rn_fileField_0 = _jspx_th_rn_fileField_0.doStartTag();
              if (_jspx_th_rn_fileField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_fileField_fieldCode_nobody.reuse(_jspx_th_rn_fileField_0);
              out.write("</strong> -\n              ");
              //  rn:fileField
              org.recipnet.site.content.rncontrols.FileField _jspx_th_rn_fileField_1 = (org.recipnet.site.content.rncontrols.FileField) _jspx_tagPool_rn_fileField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.FileField.class);
              _jspx_th_rn_fileField_1.setPageContext(_jspx_page_context);
              _jspx_th_rn_fileField_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_filenameIterator_0);
              _jspx_th_rn_fileField_1.setFieldCode(FileField.FILE_SIZE_EXACT);
              int _jspx_eval_rn_fileField_1 = _jspx_th_rn_fileField_1.doStartTag();
              if (_jspx_th_rn_fileField_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_fileField_fieldCode_nobody.reuse(_jspx_th_rn_fileField_1);
              out.write("\n            </td>\n          </tr>\n          ");
              //  ctl:parityChecker
              org.recipnet.common.controls.ParityChecker _jspx_th_ctl_parityChecker_0 = (org.recipnet.common.controls.ParityChecker) _jspx_tagPool_ctl_parityChecker_includeOnlyOnLast.get(org.recipnet.common.controls.ParityChecker.class);
              _jspx_th_ctl_parityChecker_0.setPageContext(_jspx_page_context);
              _jspx_th_ctl_parityChecker_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_filenameIterator_0);
              _jspx_th_ctl_parityChecker_0.setIncludeOnlyOnLast(true);
              int _jspx_eval_ctl_parityChecker_0 = _jspx_th_ctl_parityChecker_0.doStartTag();
              if (_jspx_eval_ctl_parityChecker_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_ctl_parityChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_ctl_parityChecker_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_ctl_parityChecker_0.doInitBody();
                }
                do {
                  out.write("\n            ");
                  //  rn:sampleHistoryTranslator
                  org.recipnet.site.content.rncontrols.SampleHistoryTranslator _jspx_th_rn_sampleHistoryTranslator_0 = (org.recipnet.site.content.rncontrols.SampleHistoryTranslator) _jspx_tagPool_rn_sampleHistoryTranslator_translateFromFileContext.get(org.recipnet.site.content.rncontrols.SampleHistoryTranslator.class);
                  _jspx_th_rn_sampleHistoryTranslator_0.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleHistoryTranslator_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_parityChecker_0);
                  _jspx_th_rn_sampleHistoryTranslator_0.setTranslateFromFileContext(true);
                  int _jspx_eval_rn_sampleHistoryTranslator_0 = _jspx_th_rn_sampleHistoryTranslator_0.doStartTag();
                  if (_jspx_eval_rn_sampleHistoryTranslator_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_rn_sampleHistoryTranslator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_rn_sampleHistoryTranslator_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_rn_sampleHistoryTranslator_0.doInitBody();
                    }
                    do {
                      out.write("\n              ");
                      //  rn:workflowCommentChecker
                      org.recipnet.site.content.rncontrols.WorkflowCommentChecker _jspx_th_rn_workflowCommentChecker_0 = (org.recipnet.site.content.rncontrols.WorkflowCommentChecker) _jspx_tagPool_rn_workflowCommentChecker.get(org.recipnet.site.content.rncontrols.WorkflowCommentChecker.class);
                      _jspx_th_rn_workflowCommentChecker_0.setPageContext(_jspx_page_context);
                      _jspx_th_rn_workflowCommentChecker_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleHistoryTranslator_0);
                      int _jspx_eval_rn_workflowCommentChecker_0 = _jspx_th_rn_workflowCommentChecker_0.doStartTag();
                      if (_jspx_eval_rn_workflowCommentChecker_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_rn_workflowCommentChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_rn_workflowCommentChecker_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_rn_workflowCommentChecker_0.doInitBody();
                        }
                        do {
                          out.write("\n                <tr>\n                  <td class=\"tableInstructions\" align=\"left\">\n                    <font class=\"label\">Comments</font>:\n                    <strong>\n                      ");
                          //  rn:sampleHistoryField
                          org.recipnet.site.content.rncontrols.SampleHistoryField _jspx_th_rn_sampleHistoryField_0 = (org.recipnet.site.content.rncontrols.SampleHistoryField) _jspx_tagPool_rn_sampleHistoryField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleHistoryField.class);
                          _jspx_th_rn_sampleHistoryField_0.setPageContext(_jspx_page_context);
                          _jspx_th_rn_sampleHistoryField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_workflowCommentChecker_0);
                          _jspx_th_rn_sampleHistoryField_0.setFieldCode(
                    SampleHistoryField.FieldCode.WORKFLOW_ACTION_COMMENTS);
                          int _jspx_eval_rn_sampleHistoryField_0 = _jspx_th_rn_sampleHistoryField_0.doStartTag();
                          if (_jspx_th_rn_sampleHistoryField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          _jspx_tagPool_rn_sampleHistoryField_fieldCode_nobody.reuse(_jspx_th_rn_sampleHistoryField_0);
                          out.write("\n                      &nbsp;       \n                    </strong>\n                  </td>\n                </tr>\n              ");
                          int evalDoAfterBody = _jspx_th_rn_workflowCommentChecker_0.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_rn_workflowCommentChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                          out = _jspx_page_context.popBody();
                      }
                      if (_jspx_th_rn_workflowCommentChecker_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_rn_workflowCommentChecker.reuse(_jspx_th_rn_workflowCommentChecker_0);
                      out.write("\n            ");
                      int evalDoAfterBody = _jspx_th_rn_sampleHistoryTranslator_0.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_rn_sampleHistoryTranslator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_rn_sampleHistoryTranslator_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_sampleHistoryTranslator_translateFromFileContext.reuse(_jspx_th_rn_sampleHistoryTranslator_0);
                  out.write("\n          ");
                  int evalDoAfterBody = _jspx_th_ctl_parityChecker_0.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_ctl_parityChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_ctl_parityChecker_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_ctl_parityChecker_includeOnlyOnLast.reuse(_jspx_th_ctl_parityChecker_0);
              out.write("\n        ");
              int evalDoAfterBody = _jspx_th_rn_filenameIterator_0.doAfterBody();
              fileIt = (org.recipnet.site.content.rncontrols.FilenameIterator) _jspx_page_context.findAttribute("fileIt");
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
            if (_jspx_eval_rn_filenameIterator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
              out = _jspx_page_context.popBody();
          }
          if (_jspx_th_rn_filenameIterator_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
            return;
          fileIt = (org.recipnet.site.content.rncontrols.FilenameIterator) _jspx_page_context.findAttribute("fileIt");
          _jspx_tagPool_rn_filenameIterator_id.reuse(_jspx_th_rn_filenameIterator_0);
          out.write("\n        ");
          //  ctl:errorMessage
          org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_0 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
          _jspx_th_ctl_errorMessage_0.setPageContext(_jspx_page_context);
          _jspx_th_ctl_errorMessage_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_uploadConfirmationPage_0);
          _jspx_th_ctl_errorMessage_0.setErrorSupplier(fileIt);
          _jspx_th_ctl_errorMessage_0.setErrorFilter(HtmlPageIterator.NO_ITERATIONS);
          int _jspx_eval_ctl_errorMessage_0 = _jspx_th_ctl_errorMessage_0.doStartTag();
          if (_jspx_eval_ctl_errorMessage_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_ctl_errorMessage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_ctl_errorMessage_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_ctl_errorMessage_0.doInitBody();
            }
            do {
              out.write("\n          <tr>\n            <td class=\"noFiles\" align=\"center\" colspan=\"2\">\n              <strong>No files were uploaded!</strong>\n            </td>\n        ");
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
          out.write("\n      </table>\n    </p>\n    <p align=\"right\">\n      ");
          if (_jspx_meth_rn_link_0(_jspx_th_rn_uploadConfirmationPage_0, _jspx_page_context))
            return;
          out.write("\n    </p>\n  </center>\n  ");
          if (_jspx_meth_ctl_styleBlock_0(_jspx_th_rn_uploadConfirmationPage_0, _jspx_page_context))
            return;
          out.write('\n');
          int evalDoAfterBody = _jspx_th_rn_uploadConfirmationPage_0.doAfterBody();
          htmlPage = (org.recipnet.site.content.rncontrols.UploadConfirmationPage) _jspx_page_context.findAttribute("htmlPage");
          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
            break;
        } while (true);
        if (_jspx_eval_rn_uploadConfirmationPage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
          out = _jspx_page_context.popBody();
      }
      if (_jspx_th_rn_uploadConfirmationPage_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
        return;
      _jspx_tagPool_rn_uploadConfirmationPage_title_secondsUntilRedirect_redirectTargetPageUrl.reuse(_jspx_th_rn_uploadConfirmationPage_0);
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

  private boolean _jspx_meth_rn_link_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_uploadConfirmationPage_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:link
    org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_0 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_href.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
    _jspx_th_rn_link_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_link_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_uploadConfirmationPage_0);
    _jspx_th_rn_link_0.setHref("/lab/managefiles.jsp");
    int _jspx_eval_rn_link_0 = _jspx_th_rn_link_0.doStartTag();
    if (_jspx_eval_rn_link_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_link_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_link_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_link_0.doInitBody();
      }
      do {
        out.write("Back to \"manage files\"...");
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

  private boolean _jspx_meth_ctl_styleBlock_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_uploadConfirmationPage_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:styleBlock
    org.recipnet.common.controls.HtmlPageStyleBlock _jspx_th_ctl_styleBlock_0 = (org.recipnet.common.controls.HtmlPageStyleBlock) _jspx_tagPool_ctl_styleBlock.get(org.recipnet.common.controls.HtmlPageStyleBlock.class);
    _jspx_th_ctl_styleBlock_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_styleBlock_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_uploadConfirmationPage_0);
    int _jspx_eval_ctl_styleBlock_0 = _jspx_th_ctl_styleBlock_0.doStartTag();
    if (_jspx_eval_ctl_styleBlock_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_styleBlock_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_styleBlock_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_styleBlock_0.doInitBody();
      }
      do {
        out.write("\n    table.table { border: 3px solid #32357D; }\n    td.noFiles { padding: 1em; background: #CADBFC; font-family: Arial,\n        Helvetica, Verdana; color: #FF0000; }\n    th.tableHeader { padding: 0.25em; background: #32357D; \n        font-family: Arial, Helvetica, Verdana; font-weight: bold; \n        font-style: normal; color: #FFFFFF; text-align: left }\n    td.tableLabels { padding: 0.25em; background: #656BFA; \n        font-family: Arial, Helvetica, Verdana; font-style: italic; \n        color: #FFFFFF; text-align: center; }\n    td.tableInstructions { padding: 0.25em; background: #CADBFC;\n        font-family: Arial, Helvetica, Verdana; color: #000050; }\n  ");
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
