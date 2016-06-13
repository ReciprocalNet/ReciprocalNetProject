package org.recipnet.site.content.lab;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.recipnet.common.controls.HtmlControl;
import org.recipnet.site.content.rncontrols.FileField;
import org.recipnet.site.content.rncontrols.SampleHistoryField;

public final class renamefile_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static java.util.Vector _jspx_dependants;

  static {
    _jspx_dependants = new java.util.Vector(2);
    _jspx_dependants.add("/WEB-INF/controls.tld");
    _jspx_dependants.add("/WEB-INF/rncontrols.tld");
  }

  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_renameFileWapPage_title_loginPageUrl_fileNameParamName_editSamplePageHref_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_selfForm;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_fileField_fieldCode_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_newFileName_required_id_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_wapComments_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_wapCancelButton_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_wapSaveButton_label_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_styleBlock;

  public java.util.List getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _jspx_tagPool_rn_renameFileWapPage_title_loginPageUrl_fileNameParamName_editSamplePageHref_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_selfForm = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_fileField_fieldCode_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_newFileName_required_id_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_wapComments_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_wapCancelButton_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_wapSaveButton_label_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_styleBlock = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
  }

  public void _jspDestroy() {
    _jspx_tagPool_rn_renameFileWapPage_title_loginPageUrl_fileNameParamName_editSamplePageHref_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName.release();
    _jspx_tagPool_ctl_selfForm.release();
    _jspx_tagPool_rn_fileField_fieldCode_nobody.release();
    _jspx_tagPool_rn_newFileName_required_id_nobody.release();
    _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.release();
    _jspx_tagPool_rn_wapComments_nobody.release();
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
      //  rn:renameFileWapPage
      org.recipnet.site.content.rncontrols.RenameFileWapPage _jspx_th_rn_renameFileWapPage_0 = (org.recipnet.site.content.rncontrols.RenameFileWapPage) _jspx_tagPool_rn_renameFileWapPage_title_loginPageUrl_fileNameParamName_editSamplePageHref_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName.get(org.recipnet.site.content.rncontrols.RenameFileWapPage.class);
      _jspx_th_rn_renameFileWapPage_0.setPageContext(_jspx_page_context);
      _jspx_th_rn_renameFileWapPage_0.setParent(null);
      _jspx_th_rn_renameFileWapPage_0.setTitle("Rename File");
      _jspx_th_rn_renameFileWapPage_0.setFileNameParamName("selectedFile");
      _jspx_th_rn_renameFileWapPage_0.setEditSamplePageHref("/lab/managefiles.jsp");
      _jspx_th_rn_renameFileWapPage_0.setLoginPageUrl("/login.jsp");
      _jspx_th_rn_renameFileWapPage_0.setCurrentPageReinvocationUrlParamName("origUrl");
      _jspx_th_rn_renameFileWapPage_0.setAuthorizationFailedReasonParamName("authorizationFailedReason");
      int _jspx_eval_rn_renameFileWapPage_0 = _jspx_th_rn_renameFileWapPage_0.doStartTag();
      if (_jspx_eval_rn_renameFileWapPage_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        org.recipnet.site.content.rncontrols.RenameFileWapPage htmlPage = null;
        if (_jspx_eval_rn_renameFileWapPage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
          out = _jspx_page_context.pushBody();
          _jspx_th_rn_renameFileWapPage_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
          _jspx_th_rn_renameFileWapPage_0.doInitBody();
        }
        htmlPage = (org.recipnet.site.content.rncontrols.RenameFileWapPage) _jspx_page_context.findAttribute("htmlPage");
        do {
          out.write("\n  <center>\n    ");
          //  ctl:selfForm
          org.recipnet.common.controls.FormHtmlElement _jspx_th_ctl_selfForm_0 = (org.recipnet.common.controls.FormHtmlElement) _jspx_tagPool_ctl_selfForm.get(org.recipnet.common.controls.FormHtmlElement.class);
          _jspx_th_ctl_selfForm_0.setPageContext(_jspx_page_context);
          _jspx_th_ctl_selfForm_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_renameFileWapPage_0);
          int _jspx_eval_ctl_selfForm_0 = _jspx_th_ctl_selfForm_0.doStartTag();
          if (_jspx_eval_ctl_selfForm_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_ctl_selfForm_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_ctl_selfForm_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_ctl_selfForm_0.doInitBody();
            }
            do {
              out.write("\n      <p>\n        <table class=\"table\" cellspacing=\"0\">\n          <tr>\n            <th colspan=\"3\" class=\"tableHeader\">\n              Rename file:\n            </th>\n          </tr>\n          <tr>\n            <td class=\"tableLabels\">current file name</td>\n            <td class=\"tableLabels\">&nbsp;</td>\n            <td class=\"tableLabels\">new file name</td>\n          </tr>\n          <tr class=\"oddRow\">\n            <td class=\"padding\">\n              <strong>\n                ");
              //  rn:fileField
              org.recipnet.site.content.rncontrols.FileField _jspx_th_rn_fileField_0 = (org.recipnet.site.content.rncontrols.FileField) _jspx_tagPool_rn_fileField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.FileField.class);
              _jspx_th_rn_fileField_0.setPageContext(_jspx_page_context);
              _jspx_th_rn_fileField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_fileField_0.setFieldCode(FileField.FILENAME);
              int _jspx_eval_rn_fileField_0 = _jspx_th_rn_fileField_0.doStartTag();
              if (_jspx_th_rn_fileField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_fileField_fieldCode_nobody.reuse(_jspx_th_rn_fileField_0);
              out.write("\n              </strong>\n            </td>\n            <td class=\"padding\">\n              <img src=\"../images/rename-arrow.gif\" />\n            </td>\n            <td class=\"padding\">\n              ");
              //  rn:newFileName
              org.recipnet.site.content.rncontrols.RenameFileNewFileName newName = null;
              org.recipnet.site.content.rncontrols.RenameFileNewFileName _jspx_th_rn_newFileName_0 = (org.recipnet.site.content.rncontrols.RenameFileNewFileName) _jspx_tagPool_rn_newFileName_required_id_nobody.get(org.recipnet.site.content.rncontrols.RenameFileNewFileName.class);
              _jspx_th_rn_newFileName_0.setPageContext(_jspx_page_context);
              _jspx_th_rn_newFileName_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_newFileName_0.setRequired(true);
              _jspx_th_rn_newFileName_0.setId("newName");
              int _jspx_eval_rn_newFileName_0 = _jspx_th_rn_newFileName_0.doStartTag();
              if (_jspx_th_rn_newFileName_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              newName = (org.recipnet.site.content.rncontrols.RenameFileNewFileName) _jspx_page_context.findAttribute("newName");
              _jspx_tagPool_rn_newFileName_required_id_nobody.reuse(_jspx_th_rn_newFileName_0);
              out.write("\n            </td>\n          </tr>\n          <tr>\n            <td class=\"tableInstructions\" colspan=\"3\">\n              ");
              //  ctl:errorMessage
              org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_0 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
              _jspx_th_ctl_errorMessage_0.setPageContext(_jspx_page_context);
              _jspx_th_ctl_errorMessage_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_ctl_errorMessage_0.setErrorSupplier(newName);
              _jspx_th_ctl_errorMessage_0.setErrorFilter(HtmlControl.REQUIRED_VALUE_IS_MISSING);
              int _jspx_eval_ctl_errorMessage_0 = _jspx_th_ctl_errorMessage_0.doStartTag();
              if (_jspx_eval_ctl_errorMessage_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_ctl_errorMessage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_ctl_errorMessage_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_ctl_errorMessage_0.doInitBody();
                }
                do {
                  out.write("\n                <p class=\"errorMessage\">\n                  You must enter a new filename if you wish to rename the file.\n                </p>\n              ");
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
              out.write("\n              ");
              //  ctl:errorMessage
              org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_1 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
              _jspx_th_ctl_errorMessage_1.setPageContext(_jspx_page_context);
              _jspx_th_ctl_errorMessage_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_ctl_errorMessage_1.setErrorSupplier(newName);
              _jspx_th_ctl_errorMessage_1.setErrorFilter(HtmlControl.VALIDATOR_REJECTED_VALUE);
              int _jspx_eval_ctl_errorMessage_1 = _jspx_th_ctl_errorMessage_1.doStartTag();
              if (_jspx_eval_ctl_errorMessage_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_ctl_errorMessage_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_ctl_errorMessage_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_ctl_errorMessage_1.doInitBody();
                }
                do {
                  out.write("\n                <p class=\"errorMessage\">\n                  You have entered an invalid filename or one that contains\n                  invalid characters.\n                </p>\n              ");
                  int evalDoAfterBody = _jspx_th_ctl_errorMessage_1.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_ctl_errorMessage_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_ctl_errorMessage_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.reuse(_jspx_th_ctl_errorMessage_1);
              out.write("\n              Comments: <br />\n              ");
              if (_jspx_meth_rn_wapComments_0(_jspx_th_ctl_selfForm_0, _jspx_page_context))
                return;
              out.write("\n            </td>\n          </tr>\n          <tr>\n            <td class=\"tableInstructions\" colspan=\"3\">\n              ");
              if (_jspx_meth_rn_wapCancelButton_0(_jspx_th_ctl_selfForm_0, _jspx_page_context))
                return;
              if (_jspx_meth_rn_wapSaveButton_0(_jspx_th_ctl_selfForm_0, _jspx_page_context))
                return;
              out.write("\n            </td>\n          </tr>\n        </table>\n      </p>\n    </center>\n  ");
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
          if (_jspx_meth_ctl_styleBlock_0(_jspx_th_rn_renameFileWapPage_0, _jspx_page_context))
            return;
          out.write('\n');
          int evalDoAfterBody = _jspx_th_rn_renameFileWapPage_0.doAfterBody();
          htmlPage = (org.recipnet.site.content.rncontrols.RenameFileWapPage) _jspx_page_context.findAttribute("htmlPage");
          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
            break;
        } while (true);
        if (_jspx_eval_rn_renameFileWapPage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
          out = _jspx_page_context.popBody();
      }
      if (_jspx_th_rn_renameFileWapPage_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
        return;
      _jspx_tagPool_rn_renameFileWapPage_title_loginPageUrl_fileNameParamName_editSamplePageHref_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName.reuse(_jspx_th_rn_renameFileWapPage_0);
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
    _jspx_th_rn_wapSaveButton_0.setLabel("Rename");
    int _jspx_eval_rn_wapSaveButton_0 = _jspx_th_rn_wapSaveButton_0.doStartTag();
    if (_jspx_th_rn_wapSaveButton_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_wapSaveButton_label_nobody.reuse(_jspx_th_rn_wapSaveButton_0);
    return false;
  }

  private boolean _jspx_meth_ctl_styleBlock_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_renameFileWapPage_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:styleBlock
    org.recipnet.common.controls.HtmlPageStyleBlock _jspx_th_ctl_styleBlock_0 = (org.recipnet.common.controls.HtmlPageStyleBlock) _jspx_tagPool_ctl_styleBlock.get(org.recipnet.common.controls.HtmlPageStyleBlock.class);
    _jspx_th_ctl_styleBlock_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_styleBlock_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_renameFileWapPage_0);
    int _jspx_eval_ctl_styleBlock_0 = _jspx_th_ctl_styleBlock_0.doStartTag();
    if (_jspx_eval_ctl_styleBlock_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_styleBlock_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_styleBlock_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_styleBlock_0.doInitBody();
      }
      do {
        out.write("\n    p.errorMessage { color: red; }\n    table.table { border: 3px solid #32357D; }\n    th.tableHeader { padding: 0.25em; background: #32357D; \n        font-family: Arial, Helvetica, Verdana; font-weight: bold; \n        font-style: normal; color: #FFFFFF; text-align: left }\n    td.tableLabels { padding: 0.25em; background: #656BFA; \n        font-family: Arial, Helvetica, Verdana; font-style: italic; \n        color: #FFFFFF; text-align: center; }\n    td.tableInstructions { padding: 0.25em; background: #CADBFC;\n        font-family: Arial, Helvetica, Verdana; color: #000050; }\n    tr.oddRow { padding: 0.25em; background: #F0F0F0; text-align: center;\n        font-family: Arial, Helvetica, Verdana; color: #000050; }\n    td.padding { padding: 0.25em; }\n  ");
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
