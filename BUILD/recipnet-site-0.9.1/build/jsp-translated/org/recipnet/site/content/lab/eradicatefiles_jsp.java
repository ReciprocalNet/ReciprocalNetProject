package org.recipnet.site.content.lab;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.recipnet.site.content.rncontrols.FileField;
import org.recipnet.site.content.rncontrols.SampleHistoryField;
import org.recipnet.site.content.rncontrols.RemoveFilesWapPage;

public final class eradicatefiles_jsp extends org.apache.jasper.runtime.HttpJspBase
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

  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_eradicateFilesWapPage_title_loginPageUrl_filenameParamName_editSamplePageHref_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_selfForm;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_filenameIterator_id;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_fileField_fieldCode_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_iterationCount_htmlPageIterator_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_cumulativeByteCount_fileIterator_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_wapComments_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_cumulativeByteCount_useAggregateSize_fileIterator_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_wapCancelButton_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_wapSaveButton_label_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_styleBlock;

  public java.util.List getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _jspx_tagPool_rn_eradicateFilesWapPage_title_loginPageUrl_filenameParamName_editSamplePageHref_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_selfForm = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_filenameIterator_id = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_fileField_fieldCode_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_iterationCount_htmlPageIterator_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_cumulativeByteCount_fileIterator_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_wapComments_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_cumulativeByteCount_useAggregateSize_fileIterator_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_wapCancelButton_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_wapSaveButton_label_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_styleBlock = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
  }

  public void _jspDestroy() {
    _jspx_tagPool_rn_eradicateFilesWapPage_title_loginPageUrl_filenameParamName_editSamplePageHref_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName.release();
    _jspx_tagPool_ctl_selfForm.release();
    _jspx_tagPool_rn_filenameIterator_id.release();
    _jspx_tagPool_rn_fileField_fieldCode_nobody.release();
    _jspx_tagPool_ctl_iterationCount_htmlPageIterator_nobody.release();
    _jspx_tagPool_rn_cumulativeByteCount_fileIterator_nobody.release();
    _jspx_tagPool_rn_wapComments_nobody.release();
    _jspx_tagPool_rn_cumulativeByteCount_useAggregateSize_fileIterator_nobody.release();
    _jspx_tagPool_rn_wapCancelButton_nobody.release();
    _jspx_tagPool_rn_wapSaveButton_label_nobody.release();
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
      //  rn:eradicateFilesWapPage
      org.recipnet.site.content.rncontrols.EradicateFilesWapPage _jspx_th_rn_eradicateFilesWapPage_0 = (org.recipnet.site.content.rncontrols.EradicateFilesWapPage) _jspx_tagPool_rn_eradicateFilesWapPage_title_loginPageUrl_filenameParamName_editSamplePageHref_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName.get(org.recipnet.site.content.rncontrols.EradicateFilesWapPage.class);
      _jspx_th_rn_eradicateFilesWapPage_0.setPageContext(_jspx_page_context);
      _jspx_th_rn_eradicateFilesWapPage_0.setParent(null);
      _jspx_th_rn_eradicateFilesWapPage_0.setTitle("Eradicate File");
      _jspx_th_rn_eradicateFilesWapPage_0.setFilenameParamName("filename");
      _jspx_th_rn_eradicateFilesWapPage_0.setEditSamplePageHref("/lab/managefiles.jsp");
      _jspx_th_rn_eradicateFilesWapPage_0.setLoginPageUrl("/login.jsp");
      _jspx_th_rn_eradicateFilesWapPage_0.setCurrentPageReinvocationUrlParamName("origUrl");
      _jspx_th_rn_eradicateFilesWapPage_0.setAuthorizationFailedReasonParamName("authorizationFailedReason");
      int _jspx_eval_rn_eradicateFilesWapPage_0 = _jspx_th_rn_eradicateFilesWapPage_0.doStartTag();
      if (_jspx_eval_rn_eradicateFilesWapPage_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        org.recipnet.site.content.rncontrols.EradicateFilesWapPage htmlPage = null;
        if (_jspx_eval_rn_eradicateFilesWapPage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
          out = _jspx_page_context.pushBody();
          _jspx_th_rn_eradicateFilesWapPage_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
          _jspx_th_rn_eradicateFilesWapPage_0.doInitBody();
        }
        htmlPage = (org.recipnet.site.content.rncontrols.EradicateFilesWapPage) _jspx_page_context.findAttribute("htmlPage");
        do {
          out.write("\n  <br />\n  <center>\n    ");
          //  ctl:selfForm
          org.recipnet.common.controls.FormHtmlElement _jspx_th_ctl_selfForm_0 = (org.recipnet.common.controls.FormHtmlElement) _jspx_tagPool_ctl_selfForm.get(org.recipnet.common.controls.FormHtmlElement.class);
          _jspx_th_ctl_selfForm_0.setPageContext(_jspx_page_context);
          _jspx_th_ctl_selfForm_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_eradicateFilesWapPage_0);
          int _jspx_eval_ctl_selfForm_0 = _jspx_th_ctl_selfForm_0.doStartTag();
          if (_jspx_eval_ctl_selfForm_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_ctl_selfForm_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_ctl_selfForm_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_ctl_selfForm_0.doInitBody();
            }
            do {
              out.write("\n      <table class=\"warningTable\" cellspacing=\"0\" width=\"400\">\n        <tr>\n          <th colspan=\"2\" class=\"warningTableHeader\">\n            Eradicate selected files\n          </th>\n        </tr>\n        <tr>\n          <td class=\"warningTableInstructions\" colspan=\"2\" align=\"left\">\n            <p>\n              The following files have been selected for eradication.\n            </p>\n            If you are certain that you wish to eradicate these files enter\n            comments in the field below and press the 'Eradicate' button.\n          </td>\n        </tr>\n        <tr>\n          <td class=\"warningTableLabels\">filename</td>\n          <td class=\"warningTableLabels\">size</td>\n        </tr>\n        ");
              //  rn:filenameIterator
              org.recipnet.site.content.rncontrols.FilenameIterator fileIt = null;
              org.recipnet.site.content.rncontrols.FilenameIterator _jspx_th_rn_filenameIterator_0 = (org.recipnet.site.content.rncontrols.FilenameIterator) _jspx_tagPool_rn_filenameIterator_id.get(org.recipnet.site.content.rncontrols.FilenameIterator.class);
              _jspx_th_rn_filenameIterator_0.setPageContext(_jspx_page_context);
              _jspx_th_rn_filenameIterator_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
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
                  out.write("\n          <tr class=\"");
                  out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${rn:testParity(fileIt.iterationCountSinceThisPhaseBegan,\n                                     'evenRow', 'oddRow')}", java.lang.String.class, (PageContext)_jspx_page_context, _jspx_fnmap_0, false));
                  out.write("\">\n            <td>\n              <strong>\n                ");
                  //  rn:fileField
                  org.recipnet.site.content.rncontrols.FileField _jspx_th_rn_fileField_0 = (org.recipnet.site.content.rncontrols.FileField) _jspx_tagPool_rn_fileField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.FileField.class);
                  _jspx_th_rn_fileField_0.setPageContext(_jspx_page_context);
                  _jspx_th_rn_fileField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_filenameIterator_0);
                  _jspx_th_rn_fileField_0.setFieldCode(FileField.FILENAME);
                  int _jspx_eval_rn_fileField_0 = _jspx_th_rn_fileField_0.doStartTag();
                  if (_jspx_th_rn_fileField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_fileField_fieldCode_nobody.reuse(_jspx_th_rn_fileField_0);
                  out.write("\n              </strong>\n            </td>\n            <td>\n              ");
                  //  rn:fileField
                  org.recipnet.site.content.rncontrols.FileField _jspx_th_rn_fileField_1 = (org.recipnet.site.content.rncontrols.FileField) _jspx_tagPool_rn_fileField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.FileField.class);
                  _jspx_th_rn_fileField_1.setPageContext(_jspx_page_context);
                  _jspx_th_rn_fileField_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_filenameIterator_0);
                  _jspx_th_rn_fileField_1.setFieldCode(FileField.FILE_SIZE);
                  int _jspx_eval_rn_fileField_1 = _jspx_th_rn_fileField_1.doStartTag();
                  if (_jspx_th_rn_fileField_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_fileField_fieldCode_nobody.reuse(_jspx_th_rn_fileField_1);
                  out.write("\n            </td>\n          </tr>\n        ");
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
              out.write("\n        <tr>\n          <td class=\"warningTableLabels\" colspan=\"2\">\n            There are\n            <strong>\n              ");
              //  ctl:iterationCount
              org.recipnet.common.controls.IterationCount _jspx_th_ctl_iterationCount_0 = (org.recipnet.common.controls.IterationCount) _jspx_tagPool_ctl_iterationCount_htmlPageIterator_nobody.get(org.recipnet.common.controls.IterationCount.class);
              _jspx_th_ctl_iterationCount_0.setPageContext(_jspx_page_context);
              _jspx_th_ctl_iterationCount_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_ctl_iterationCount_0.setHtmlPageIterator(fileIt);
              int _jspx_eval_ctl_iterationCount_0 = _jspx_th_ctl_iterationCount_0.doStartTag();
              if (_jspx_th_ctl_iterationCount_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_ctl_iterationCount_htmlPageIterator_nobody.reuse(_jspx_th_ctl_iterationCount_0);
              out.write("\n            </strong> files, totalling\n            <strong>\n              ");
              //  rn:cumulativeByteCount
              org.recipnet.site.content.rncontrols.CumulativeByteCount _jspx_th_rn_cumulativeByteCount_0 = (org.recipnet.site.content.rncontrols.CumulativeByteCount) _jspx_tagPool_rn_cumulativeByteCount_fileIterator_nobody.get(org.recipnet.site.content.rncontrols.CumulativeByteCount.class);
              _jspx_th_rn_cumulativeByteCount_0.setPageContext(_jspx_page_context);
              _jspx_th_rn_cumulativeByteCount_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_cumulativeByteCount_0.setFileIterator(fileIt);
              int _jspx_eval_rn_cumulativeByteCount_0 = _jspx_th_rn_cumulativeByteCount_0.doStartTag();
              if (_jspx_th_rn_cumulativeByteCount_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_cumulativeByteCount_fileIterator_nobody.reuse(_jspx_th_rn_cumulativeByteCount_0);
              out.write("\n            </strong> bytes.\n          </td>\n        </tr>\n        <tr>\n          <td class=\"warningTableInstructions\" colspan=\"2\">\n            Comments: <br />\n            ");
              if (_jspx_meth_rn_wapComments_0(_jspx_th_ctl_selfForm_0, _jspx_page_context))
                return;
              out.write("\n          </td>\n        </tr>\n        <tr>\n          <td class=\"warningTableInstructions\" colspan=\"2\">\n            <font class=\"errorMessage\">\n              When a file is eradicated every version of it is\n              <strong>permanently removed</strong> from the repository and\n              the action cannot be undone.  This defeats Reciprocal Net's\n              sample versioning mechanism and it is strongly recommended that\n              you Remove the files instead.\n              <strong>\n                Eradicating these files will free\n                ");
              //  rn:cumulativeByteCount
              org.recipnet.site.content.rncontrols.CumulativeByteCount _jspx_th_rn_cumulativeByteCount_1 = (org.recipnet.site.content.rncontrols.CumulativeByteCount) _jspx_tagPool_rn_cumulativeByteCount_useAggregateSize_fileIterator_nobody.get(org.recipnet.site.content.rncontrols.CumulativeByteCount.class);
              _jspx_th_rn_cumulativeByteCount_1.setPageContext(_jspx_page_context);
              _jspx_th_rn_cumulativeByteCount_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_cumulativeByteCount_1.setFileIterator(fileIt);
              _jspx_th_rn_cumulativeByteCount_1.setUseAggregateSize(true);
              int _jspx_eval_rn_cumulativeByteCount_1 = _jspx_th_rn_cumulativeByteCount_1.doStartTag();
              if (_jspx_th_rn_cumulativeByteCount_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_cumulativeByteCount_useAggregateSize_fileIterator_nobody.reuse(_jspx_th_rn_cumulativeByteCount_1);
              out.write(" bytes in the repository.\n              </strong>\n            </font>\n          </td>\n        </tr>\n        <tr>\n          <td class=\"warningTableInstructions\" colspan=\"2\">\n            ");
              if (_jspx_meth_rn_wapCancelButton_0(_jspx_th_ctl_selfForm_0, _jspx_page_context))
                return;
              if (_jspx_meth_rn_wapSaveButton_0(_jspx_th_ctl_selfForm_0, _jspx_page_context))
                return;
              out.write("\n          </td>\n        </tr>\n      </table>\n    ");
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
          out.write("\n  </center>\n  ");
          if (_jspx_meth_ctl_styleBlock_0(_jspx_th_rn_eradicateFilesWapPage_0, _jspx_page_context))
            return;
          out.write('\n');
          int evalDoAfterBody = _jspx_th_rn_eradicateFilesWapPage_0.doAfterBody();
          htmlPage = (org.recipnet.site.content.rncontrols.EradicateFilesWapPage) _jspx_page_context.findAttribute("htmlPage");
          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
            break;
        } while (true);
        if (_jspx_eval_rn_eradicateFilesWapPage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
          out = _jspx_page_context.popBody();
      }
      if (_jspx_th_rn_eradicateFilesWapPage_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
        return;
      _jspx_tagPool_rn_eradicateFilesWapPage_title_loginPageUrl_filenameParamName_editSamplePageHref_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName.reuse(_jspx_th_rn_eradicateFilesWapPage_0);
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

  private boolean _jspx_meth_rn_wapComments_0(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_selfForm_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:wapComments
    org.recipnet.site.content.rncontrols.WapComments _jspx_th_rn_wapComments_0 = (org.recipnet.site.content.rncontrols.WapComments) _jspx_tagPool_rn_wapComments_nobody.get(org.recipnet.site.content.rncontrols.WapComments.class);
    _jspx_th_rn_wapComments_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_wapComments_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
    int _jspx_eval_rn_wapComments_0 = _jspx_th_rn_wapComments_0.doStartTag();
    if (_jspx_th_rn_wapComments_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_wapComments_nobody.reuse(_jspx_th_rn_wapComments_0);
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

  private boolean _jspx_meth_rn_wapSaveButton_0(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_selfForm_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:wapSaveButton
    org.recipnet.site.content.rncontrols.WapSaveButton _jspx_th_rn_wapSaveButton_0 = (org.recipnet.site.content.rncontrols.WapSaveButton) _jspx_tagPool_rn_wapSaveButton_label_nobody.get(org.recipnet.site.content.rncontrols.WapSaveButton.class);
    _jspx_th_rn_wapSaveButton_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_wapSaveButton_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
    _jspx_th_rn_wapSaveButton_0.setLabel("Eradicate");
    int _jspx_eval_rn_wapSaveButton_0 = _jspx_th_rn_wapSaveButton_0.doStartTag();
    if (_jspx_th_rn_wapSaveButton_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_wapSaveButton_label_nobody.reuse(_jspx_th_rn_wapSaveButton_0);
    return false;
  }

  private boolean _jspx_meth_ctl_styleBlock_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_eradicateFilesWapPage_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:styleBlock
    org.recipnet.common.controls.HtmlPageStyleBlock _jspx_th_ctl_styleBlock_0 = (org.recipnet.common.controls.HtmlPageStyleBlock) _jspx_tagPool_ctl_styleBlock.get(org.recipnet.common.controls.HtmlPageStyleBlock.class);
    _jspx_th_ctl_styleBlock_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_styleBlock_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_eradicateFilesWapPage_0);
    int _jspx_eval_ctl_styleBlock_0 = _jspx_th_ctl_styleBlock_0.doStartTag();
    if (_jspx_eval_ctl_styleBlock_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_styleBlock_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_styleBlock_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_styleBlock_0.doInitBody();
      }
      do {
        out.write("\n    .errorMessage { color: red; }\n    table.warningTable { border: 3px solid #FF0000;}\n    th.warningTableHeader { padding: 0.25em; background: #FF0000; \n        font-family: Arial, Helvetica, Verdana; font-weight: bold; \n        font-style: normal; color: #FFFFFF; text-align: left }\n    td.warningTableLabels { padding: 0.25em; background: #606060; \n        font-family: Arial, Helvetica, Verdana; font-style: italic; \n        color: #FFFFFF; text-align: center; }\n    tr.oddRow { background: #F0F0F0; text-align: center;\n        font-family: Arial, Helvetica, Verdana; color: #500000; }\n    tr.evenRow { padding: 0.25em; background: #D0D0D0; text-align: center;\n        font-family: Arial, Helvetica, Verdana; color: #500000; }\n    td.warningTableInstructions { padding: 0.25em; background: #000000;\n        font-family: Arial, Helvetica, Verdana; color: #FFF0F0; }\n  ");
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
