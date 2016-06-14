package org.recipnet.site.content.lab;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.recipnet.site.shared.bl.SampleWorkflowBL;
import org.recipnet.site.content.rncontrols.FileField;
import org.recipnet.site.content.rncontrols.HeldFileOption;
import org.recipnet.site.content.rncontrols.SampleHistoryField;
import org.recipnet.site.content.rncontrols.UploaderPage;

public final class uploadedfileconflicts_jsp extends org.apache.jasper.runtime.HttpJspBase
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

  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_uploaderPage_workflowActionCode_uploadConfirmationPageHref_title_loginPageUrl_heldFilesPageHref_editSamplePageHref_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_selfForm;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_phaseEvent_onPhases;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_heldFileIterator_keyParamName_id_fileRetrieveServletHref;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_fileField_fieldCode_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_radioButtonGroup;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_fileContextTranslator_translateProvisionalToSettled;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_fileField_label_fieldCode_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleHistoryField_requireHistory_noHistoryText_fieldCode_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_fileChecker_includeOnlyIfFileHasDescription;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_heldFileOption_fileToKeep_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_wapSaveButton_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_wapCancelButton_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_styleBlock;

  public java.util.List getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _jspx_tagPool_rn_uploaderPage_workflowActionCode_uploadConfirmationPageHref_title_loginPageUrl_heldFilesPageHref_editSamplePageHref_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_selfForm = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_phaseEvent_onPhases = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_heldFileIterator_keyParamName_id_fileRetrieveServletHref = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_fileField_fieldCode_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_radioButtonGroup = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_fileContextTranslator_translateProvisionalToSettled = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_fileField_label_fieldCode_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleHistoryField_requireHistory_noHistoryText_fieldCode_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_fileChecker_includeOnlyIfFileHasDescription = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_heldFileOption_fileToKeep_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_wapSaveButton_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_wapCancelButton_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_styleBlock = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
  }

  public void _jspDestroy() {
    _jspx_tagPool_rn_uploaderPage_workflowActionCode_uploadConfirmationPageHref_title_loginPageUrl_heldFilesPageHref_editSamplePageHref_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName.release();
    _jspx_tagPool_ctl_selfForm.release();
    _jspx_tagPool_ctl_phaseEvent_onPhases.release();
    _jspx_tagPool_rn_heldFileIterator_keyParamName_id_fileRetrieveServletHref.release();
    _jspx_tagPool_rn_fileField_fieldCode_nobody.release();
    _jspx_tagPool_ctl_radioButtonGroup.release();
    _jspx_tagPool_rn_fileContextTranslator_translateProvisionalToSettled.release();
    _jspx_tagPool_rn_fileField_label_fieldCode_nobody.release();
    _jspx_tagPool_rn_sampleHistoryField_requireHistory_noHistoryText_fieldCode_nobody.release();
    _jspx_tagPool_rn_fileChecker_includeOnlyIfFileHasDescription.release();
    _jspx_tagPool_rn_heldFileOption_fileToKeep_nobody.release();
    _jspx_tagPool_rn_wapSaveButton_nobody.release();
    _jspx_tagPool_rn_wapCancelButton_nobody.release();
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
      //  rn:uploaderPage
      org.recipnet.site.content.rncontrols.UploaderPage _jspx_th_rn_uploaderPage_0 = (org.recipnet.site.content.rncontrols.UploaderPage) _jspx_tagPool_rn_uploaderPage_workflowActionCode_uploadConfirmationPageHref_title_loginPageUrl_heldFilesPageHref_editSamplePageHref_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName.get(org.recipnet.site.content.rncontrols.UploaderPage.class);
      _jspx_th_rn_uploaderPage_0.setPageContext(_jspx_page_context);
      _jspx_th_rn_uploaderPage_0.setParent(null);
      _jspx_th_rn_uploaderPage_0.setTitle("Resolve conflicts in uploaded files");
      _jspx_th_rn_uploaderPage_0.setWorkflowActionCode(SampleWorkflowBL.MULTIPLE_FILES_ADDED_OR_REPLACED);
      _jspx_th_rn_uploaderPage_0.setEditSamplePageHref("/lab/managefiles.jsp");
      _jspx_th_rn_uploaderPage_0.setHeldFilesPageHref("/lab/uploadedfileconflicts.jsp");
      _jspx_th_rn_uploaderPage_0.setUploadConfirmationPageHref("/lab/uploadcompleted.jsp");
      _jspx_th_rn_uploaderPage_0.setLoginPageUrl("/login.jsp");
      _jspx_th_rn_uploaderPage_0.setCurrentPageReinvocationUrlParamName("origUrl");
      _jspx_th_rn_uploaderPage_0.setAuthorizationFailedReasonParamName("authorizationFailedReason");
      int _jspx_eval_rn_uploaderPage_0 = _jspx_th_rn_uploaderPage_0.doStartTag();
      if (_jspx_eval_rn_uploaderPage_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        org.recipnet.site.content.rncontrols.UploaderPage htmlPage = null;
        if (_jspx_eval_rn_uploaderPage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
          out = _jspx_page_context.pushBody();
          _jspx_th_rn_uploaderPage_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
          _jspx_th_rn_uploaderPage_0.doInitBody();
        }
        htmlPage = (org.recipnet.site.content.rncontrols.UploaderPage) _jspx_page_context.findAttribute("htmlPage");
        do {
          out.write('\n');
          out.write(' ');
          out.write(' ');
          //  ctl:selfForm
          org.recipnet.common.controls.FormHtmlElement _jspx_th_ctl_selfForm_0 = (org.recipnet.common.controls.FormHtmlElement) _jspx_tagPool_ctl_selfForm.get(org.recipnet.common.controls.FormHtmlElement.class);
          _jspx_th_ctl_selfForm_0.setPageContext(_jspx_page_context);
          _jspx_th_ctl_selfForm_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_uploaderPage_0);
          int _jspx_eval_ctl_selfForm_0 = _jspx_th_ctl_selfForm_0.doStartTag();
          if (_jspx_eval_ctl_selfForm_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_ctl_selfForm_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_ctl_selfForm_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_ctl_selfForm_0.doInitBody();
            }
            do {
              out.write("\n    <p class=\"pageInstructions\">\n      At least one of the files you just uploaded has the same name as an\n      existing data file.  For each file listed below, select which version(s)\n      you wish to keep.\n    </p>\n\n    <table class=\"fileTable\" border=\"0\" cellspacing=\"0\">\n      <tr>\n        <th class=\"headerFont\">File name:</th>\n        <th>&nbsp;</th>\n        <th class=\"headerFont\">\n          Existing file:<br />(abandon uploaded file)\n        </th>\n        <th>&nbsp;</th>\n        <th class=\"headerFont\">\n          Uploaded file:<br />(replace existing file)\n        </th>\n      </tr>    \n\n      ");
              if (_jspx_meth_ctl_phaseEvent_0(_jspx_th_ctl_selfForm_0, _jspx_page_context))
                return;
              out.write("\n      ");
              //  rn:heldFileIterator
              org.recipnet.site.content.rncontrols.HeldFileIterator hfit = null;
              org.recipnet.site.content.rncontrols.HeldFileIterator _jspx_th_rn_heldFileIterator_0 = (org.recipnet.site.content.rncontrols.HeldFileIterator) _jspx_tagPool_rn_heldFileIterator_keyParamName_id_fileRetrieveServletHref.get(org.recipnet.site.content.rncontrols.HeldFileIterator.class);
              _jspx_th_rn_heldFileIterator_0.setPageContext(_jspx_page_context);
              _jspx_th_rn_heldFileIterator_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_heldFileIterator_0.setId("hfit");
              _jspx_th_rn_heldFileIterator_0.setFileRetrieveServletHref("/servlet/fileretrieve");
              _jspx_th_rn_heldFileIterator_0.setKeyParamName("key");
              int _jspx_eval_rn_heldFileIterator_0 = _jspx_th_rn_heldFileIterator_0.doStartTag();
              if (_jspx_eval_rn_heldFileIterator_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_rn_heldFileIterator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_rn_heldFileIterator_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_rn_heldFileIterator_0.doInitBody();
                }
                hfit = (org.recipnet.site.content.rncontrols.HeldFileIterator) _jspx_page_context.findAttribute("hfit");
                do {
                  out.write("\n        ");
                  org.apache.jasper.runtime.JspRuntimeLibrary.handleSetPropertyExpression(_jspx_page_context.findAttribute("rowClass"), "string", "${rn:testParity(hfit.iterationCountSinceThisPhaseBegan,\n                                   'rowColorEven', 'rowColorOdd')}", _jspx_page_context, _jspx_fnmap_0);
                  out.write("\n        <tr class=\"");
                  out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${rowClass.string}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
                  out.write("\">\n           <td colspan=\"6\" class=\"spacerCell\">&nbsp;</td>\n        </tr>\n        <tr class=\"");
                  out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${rowClass.string}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
                  out.write("\">\n        <td class=\"fileNameCell\">\n  \t  ");
                  //  rn:fileField
                  org.recipnet.site.content.rncontrols.FileField _jspx_th_rn_fileField_0 = (org.recipnet.site.content.rncontrols.FileField) _jspx_tagPool_rn_fileField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.FileField.class);
                  _jspx_th_rn_fileField_0.setPageContext(_jspx_page_context);
                  _jspx_th_rn_fileField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_heldFileIterator_0);
                  _jspx_th_rn_fileField_0.setFieldCode(FileField.FILENAME);
                  int _jspx_eval_rn_fileField_0 = _jspx_th_rn_fileField_0.doStartTag();
                  if (_jspx_th_rn_fileField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_fileField_fieldCode_nobody.reuse(_jspx_th_rn_fileField_0);
                  out.write("\n        </td>\n        <td width=\"5\">&nbsp;</td>\n        ");
                  //  ctl:radioButtonGroup
                  org.recipnet.common.controls.RadioButtonGroupHtmlControl _jspx_th_ctl_radioButtonGroup_0 = (org.recipnet.common.controls.RadioButtonGroupHtmlControl) _jspx_tagPool_ctl_radioButtonGroup.get(org.recipnet.common.controls.RadioButtonGroupHtmlControl.class);
                  _jspx_th_ctl_radioButtonGroup_0.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_radioButtonGroup_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_heldFileIterator_0);
                  int _jspx_eval_ctl_radioButtonGroup_0 = _jspx_th_ctl_radioButtonGroup_0.doStartTag();
                  if (_jspx_eval_ctl_radioButtonGroup_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_radioButtonGroup_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_radioButtonGroup_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_radioButtonGroup_0.doInitBody();
                    }
                    do {
                      out.write("\n        <td class=\"fileDetailCell\">\n          ");
                      //  rn:fileContextTranslator
                      org.recipnet.site.content.rncontrols.FileContextTranslator _jspx_th_rn_fileContextTranslator_0 = (org.recipnet.site.content.rncontrols.FileContextTranslator) _jspx_tagPool_rn_fileContextTranslator_translateProvisionalToSettled.get(org.recipnet.site.content.rncontrols.FileContextTranslator.class);
                      _jspx_th_rn_fileContextTranslator_0.setPageContext(_jspx_page_context);
                      _jspx_th_rn_fileContextTranslator_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_radioButtonGroup_0);
                      _jspx_th_rn_fileContextTranslator_0.setTranslateProvisionalToSettled(true);
                      int _jspx_eval_rn_fileContextTranslator_0 = _jspx_th_rn_fileContextTranslator_0.doStartTag();
                      if (_jspx_eval_rn_fileContextTranslator_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_rn_fileContextTranslator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_rn_fileContextTranslator_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_rn_fileContextTranslator_0.doInitBody();
                        }
                        do {
                          out.write("\n            ");
                          //  rn:fileField
                          org.recipnet.site.content.rncontrols.FileField _jspx_th_rn_fileField_1 = (org.recipnet.site.content.rncontrols.FileField) _jspx_tagPool_rn_fileField_label_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.FileField.class);
                          _jspx_th_rn_fileField_1.setPageContext(_jspx_page_context);
                          _jspx_th_rn_fileField_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_fileContextTranslator_0);
                          _jspx_th_rn_fileField_1.setFieldCode(FileField.LINKED_FILENAME);
                          _jspx_th_rn_fileField_1.setLabel("existing file");
                          int _jspx_eval_rn_fileField_1 = _jspx_th_rn_fileField_1.doStartTag();
                          if (_jspx_th_rn_fileField_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          _jspx_tagPool_rn_fileField_label_fieldCode_nobody.reuse(_jspx_th_rn_fileField_1);
                          out.write("\n            (");
                          //  rn:fileField
                          org.recipnet.site.content.rncontrols.FileField _jspx_th_rn_fileField_2 = (org.recipnet.site.content.rncontrols.FileField) _jspx_tagPool_rn_fileField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.FileField.class);
                          _jspx_th_rn_fileField_2.setPageContext(_jspx_page_context);
                          _jspx_th_rn_fileField_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_fileContextTranslator_0);
                          _jspx_th_rn_fileField_2.setFieldCode(FileField.FILE_SIZE);
                          int _jspx_eval_rn_fileField_2 = _jspx_th_rn_fileField_2.doStartTag();
                          if (_jspx_th_rn_fileField_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          _jspx_tagPool_rn_fileField_fieldCode_nobody.reuse(_jspx_th_rn_fileField_2);
                          out.write(")\n            <br />\n            last modified:\n            ");
                          //  rn:sampleHistoryField
                          org.recipnet.site.content.rncontrols.SampleHistoryField _jspx_th_rn_sampleHistoryField_0 = (org.recipnet.site.content.rncontrols.SampleHistoryField) _jspx_tagPool_rn_sampleHistoryField_requireHistory_noHistoryText_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleHistoryField.class);
                          _jspx_th_rn_sampleHistoryField_0.setPageContext(_jspx_page_context);
                          _jspx_th_rn_sampleHistoryField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_fileContextTranslator_0);
                          _jspx_th_rn_sampleHistoryField_0.setFieldCode(SampleHistoryField.FieldCode.ACTION_DATE);
                          _jspx_th_rn_sampleHistoryField_0.setRequireHistory(false);
                          _jspx_th_rn_sampleHistoryField_0.setNoHistoryText("unknown");
                          int _jspx_eval_rn_sampleHistoryField_0 = _jspx_th_rn_sampleHistoryField_0.doStartTag();
                          if (_jspx_th_rn_sampleHistoryField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          _jspx_tagPool_rn_sampleHistoryField_requireHistory_noHistoryText_fieldCode_nobody.reuse(_jspx_th_rn_sampleHistoryField_0);
                          out.write("<br />\n            by:\n            ");
                          //  rn:sampleHistoryField
                          org.recipnet.site.content.rncontrols.SampleHistoryField _jspx_th_rn_sampleHistoryField_1 = (org.recipnet.site.content.rncontrols.SampleHistoryField) _jspx_tagPool_rn_sampleHistoryField_requireHistory_noHistoryText_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleHistoryField.class);
                          _jspx_th_rn_sampleHistoryField_1.setPageContext(_jspx_page_context);
                          _jspx_th_rn_sampleHistoryField_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_fileContextTranslator_0);
                          _jspx_th_rn_sampleHistoryField_1.setFieldCode(
                   SampleHistoryField.FieldCode.
                   USER_FULLNAME_THAT_PERFORMED_ACTION);
                          _jspx_th_rn_sampleHistoryField_1.setRequireHistory(false);
                          _jspx_th_rn_sampleHistoryField_1.setNoHistoryText("unknown");
                          int _jspx_eval_rn_sampleHistoryField_1 = _jspx_th_rn_sampleHistoryField_1.doStartTag();
                          if (_jspx_th_rn_sampleHistoryField_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          _jspx_tagPool_rn_sampleHistoryField_requireHistory_noHistoryText_fieldCode_nobody.reuse(_jspx_th_rn_sampleHistoryField_1);
                          out.write("\n            ");
                          //  rn:fileChecker
                          org.recipnet.site.content.rncontrols.FileChecker _jspx_th_rn_fileChecker_0 = (org.recipnet.site.content.rncontrols.FileChecker) _jspx_tagPool_rn_fileChecker_includeOnlyIfFileHasDescription.get(org.recipnet.site.content.rncontrols.FileChecker.class);
                          _jspx_th_rn_fileChecker_0.setPageContext(_jspx_page_context);
                          _jspx_th_rn_fileChecker_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_fileContextTranslator_0);
                          _jspx_th_rn_fileChecker_0.setIncludeOnlyIfFileHasDescription(true);
                          int _jspx_eval_rn_fileChecker_0 = _jspx_th_rn_fileChecker_0.doStartTag();
                          if (_jspx_eval_rn_fileChecker_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            if (_jspx_eval_rn_fileChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_rn_fileChecker_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_rn_fileChecker_0.doInitBody();
                            }
                            do {
                              out.write("\n              <br />\n              description:\n                ");
                              //  rn:fileField
                              org.recipnet.site.content.rncontrols.FileField _jspx_th_rn_fileField_3 = (org.recipnet.site.content.rncontrols.FileField) _jspx_tagPool_rn_fileField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.FileField.class);
                              _jspx_th_rn_fileField_3.setPageContext(_jspx_page_context);
                              _jspx_th_rn_fileField_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_fileChecker_0);
                              _jspx_th_rn_fileField_3.setFieldCode(FileField.DESCRIPTION);
                              int _jspx_eval_rn_fileField_3 = _jspx_th_rn_fileField_3.doStartTag();
                              if (_jspx_th_rn_fileField_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                              return;
                              _jspx_tagPool_rn_fileField_fieldCode_nobody.reuse(_jspx_th_rn_fileField_3);
                              out.write("\n            ");
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
                          out.write("\n          ");
                          int evalDoAfterBody = _jspx_th_rn_fileContextTranslator_0.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_rn_fileContextTranslator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                          out = _jspx_page_context.popBody();
                      }
                      if (_jspx_th_rn_fileContextTranslator_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_rn_fileContextTranslator_translateProvisionalToSettled.reuse(_jspx_th_rn_fileContextTranslator_0);
                      out.write("\n          <center>\n            ");
                      //  rn:heldFileOption
                      org.recipnet.site.content.rncontrols.HeldFileOption _jspx_th_rn_heldFileOption_0 = (org.recipnet.site.content.rncontrols.HeldFileOption) _jspx_tagPool_rn_heldFileOption_fileToKeep_nobody.get(org.recipnet.site.content.rncontrols.HeldFileOption.class);
                      _jspx_th_rn_heldFileOption_0.setPageContext(_jspx_page_context);
                      _jspx_th_rn_heldFileOption_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_radioButtonGroup_0);
                      _jspx_th_rn_heldFileOption_0.setFileToKeep(HeldFileOption.FileToKeep.EXISTING);
                      int _jspx_eval_rn_heldFileOption_0 = _jspx_th_rn_heldFileOption_0.doStartTag();
                      if (_jspx_th_rn_heldFileOption_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_rn_heldFileOption_fileToKeep_nobody.reuse(_jspx_th_rn_heldFileOption_0);
                      out.write("\n            keep this file\n          </center>\n        </td>\n        <td width=\"20\">&nbsp;</td>\n        <td class=\"fileDetailCell\">\n          ");
                      //  rn:fileField
                      org.recipnet.site.content.rncontrols.FileField _jspx_th_rn_fileField_4 = (org.recipnet.site.content.rncontrols.FileField) _jspx_tagPool_rn_fileField_label_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.FileField.class);
                      _jspx_th_rn_fileField_4.setPageContext(_jspx_page_context);
                      _jspx_th_rn_fileField_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_radioButtonGroup_0);
                      _jspx_th_rn_fileField_4.setFieldCode(FileField.LINKED_FILENAME);
                      _jspx_th_rn_fileField_4.setLabel("uploaded file");
                      int _jspx_eval_rn_fileField_4 = _jspx_th_rn_fileField_4.doStartTag();
                      if (_jspx_th_rn_fileField_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_rn_fileField_label_fieldCode_nobody.reuse(_jspx_th_rn_fileField_4);
                      out.write("\n          (");
                      //  rn:fileField
                      org.recipnet.site.content.rncontrols.FileField _jspx_th_rn_fileField_5 = (org.recipnet.site.content.rncontrols.FileField) _jspx_tagPool_rn_fileField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.FileField.class);
                      _jspx_th_rn_fileField_5.setPageContext(_jspx_page_context);
                      _jspx_th_rn_fileField_5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_radioButtonGroup_0);
                      _jspx_th_rn_fileField_5.setFieldCode(FileField.FILE_SIZE);
                      int _jspx_eval_rn_fileField_5 = _jspx_th_rn_fileField_5.doStartTag();
                      if (_jspx_th_rn_fileField_5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_rn_fileField_fieldCode_nobody.reuse(_jspx_th_rn_fileField_5);
                      out.write(")\n          ");
                      //  rn:fileChecker
                      org.recipnet.site.content.rncontrols.FileChecker _jspx_th_rn_fileChecker_1 = (org.recipnet.site.content.rncontrols.FileChecker) _jspx_tagPool_rn_fileChecker_includeOnlyIfFileHasDescription.get(org.recipnet.site.content.rncontrols.FileChecker.class);
                      _jspx_th_rn_fileChecker_1.setPageContext(_jspx_page_context);
                      _jspx_th_rn_fileChecker_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_radioButtonGroup_0);
                      _jspx_th_rn_fileChecker_1.setIncludeOnlyIfFileHasDescription(true);
                      int _jspx_eval_rn_fileChecker_1 = _jspx_th_rn_fileChecker_1.doStartTag();
                      if (_jspx_eval_rn_fileChecker_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_rn_fileChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_rn_fileChecker_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_rn_fileChecker_1.doInitBody();
                        }
                        do {
                          out.write("\n            <br />\n            description:\n              ");
                          //  rn:fileField
                          org.recipnet.site.content.rncontrols.FileField _jspx_th_rn_fileField_6 = (org.recipnet.site.content.rncontrols.FileField) _jspx_tagPool_rn_fileField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.FileField.class);
                          _jspx_th_rn_fileField_6.setPageContext(_jspx_page_context);
                          _jspx_th_rn_fileField_6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_fileChecker_1);
                          _jspx_th_rn_fileField_6.setFieldCode(FileField.DESCRIPTION);
                          int _jspx_eval_rn_fileField_6 = _jspx_th_rn_fileField_6.doStartTag();
                          if (_jspx_th_rn_fileField_6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          _jspx_tagPool_rn_fileField_fieldCode_nobody.reuse(_jspx_th_rn_fileField_6);
                          out.write("\n          ");
                          int evalDoAfterBody = _jspx_th_rn_fileChecker_1.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_rn_fileChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                          out = _jspx_page_context.popBody();
                      }
                      if (_jspx_th_rn_fileChecker_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_rn_fileChecker_includeOnlyIfFileHasDescription.reuse(_jspx_th_rn_fileChecker_1);
                      out.write("\n          <center>\n            ");
                      //  rn:heldFileOption
                      org.recipnet.site.content.rncontrols.HeldFileOption _jspx_th_rn_heldFileOption_1 = (org.recipnet.site.content.rncontrols.HeldFileOption) _jspx_tagPool_rn_heldFileOption_fileToKeep_nobody.get(org.recipnet.site.content.rncontrols.HeldFileOption.class);
                      _jspx_th_rn_heldFileOption_1.setPageContext(_jspx_page_context);
                      _jspx_th_rn_heldFileOption_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_radioButtonGroup_0);
                      _jspx_th_rn_heldFileOption_1.setFileToKeep(HeldFileOption.FileToKeep.UPLOADED);
                      int _jspx_eval_rn_heldFileOption_1 = _jspx_th_rn_heldFileOption_1.doStartTag();
                      if (_jspx_th_rn_heldFileOption_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_rn_heldFileOption_fileToKeep_nobody.reuse(_jspx_th_rn_heldFileOption_1);
                      out.write("\n            keep this file\n          </center>\n        </td>\n        ");
                      int evalDoAfterBody = _jspx_th_ctl_radioButtonGroup_0.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_ctl_radioButtonGroup_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_ctl_radioButtonGroup_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_ctl_radioButtonGroup.reuse(_jspx_th_ctl_radioButtonGroup_0);
                  out.write("\n        <td width=\"10\">&nbsp;</td>\n        </tr>\n        <tr class=\"");
                  out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${rowClass.string}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
                  out.write("\">\n          <td colspan=\"6\" class=\"spacerCell\">&nbsp;</td>\n        </tr>        \n      ");
                  int evalDoAfterBody = _jspx_th_rn_heldFileIterator_0.doAfterBody();
                  hfit = (org.recipnet.site.content.rncontrols.HeldFileIterator) _jspx_page_context.findAttribute("hfit");
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_rn_heldFileIterator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_rn_heldFileIterator_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              hfit = (org.recipnet.site.content.rncontrols.HeldFileIterator) _jspx_page_context.findAttribute("hfit");
              _jspx_tagPool_rn_heldFileIterator_keyParamName_id_fileRetrieveServletHref.reuse(_jspx_th_rn_heldFileIterator_0);
              out.write("\n    </table>\n    &nbsp;<br />\n    ");
              if (_jspx_meth_rn_wapSaveButton_0(_jspx_th_ctl_selfForm_0, _jspx_page_context))
                return;
              out.write("\n    ");
              if (_jspx_meth_rn_wapCancelButton_0(_jspx_th_ctl_selfForm_0, _jspx_page_context))
                return;
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
          out.write('\n');
          out.write(' ');
          out.write(' ');
          if (_jspx_meth_ctl_styleBlock_0(_jspx_th_rn_uploaderPage_0, _jspx_page_context))
            return;
          out.write('\n');
          int evalDoAfterBody = _jspx_th_rn_uploaderPage_0.doAfterBody();
          htmlPage = (org.recipnet.site.content.rncontrols.UploaderPage) _jspx_page_context.findAttribute("htmlPage");
          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
            break;
        } while (true);
        if (_jspx_eval_rn_uploaderPage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
          out = _jspx_page_context.popBody();
      }
      if (_jspx_th_rn_uploaderPage_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
        return;
      _jspx_tagPool_rn_uploaderPage_workflowActionCode_uploadConfirmationPageHref_title_loginPageUrl_heldFilesPageHref_editSamplePageHref_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName.reuse(_jspx_th_rn_uploaderPage_0);
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

  private boolean _jspx_meth_ctl_phaseEvent_0(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_selfForm_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    HttpSession session = _jspx_page_context.getSession();
    ServletContext application = _jspx_page_context.getServletContext();
    HttpServletRequest request = (HttpServletRequest)_jspx_page_context.getRequest();
    //  ctl:phaseEvent
    org.recipnet.common.controls.HtmlPagePhaseEvent _jspx_th_ctl_phaseEvent_0 = (org.recipnet.common.controls.HtmlPagePhaseEvent) _jspx_tagPool_ctl_phaseEvent_onPhases.get(org.recipnet.common.controls.HtmlPagePhaseEvent.class);
    _jspx_th_ctl_phaseEvent_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_phaseEvent_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
    _jspx_th_ctl_phaseEvent_0.setOnPhases("registration");
    int _jspx_eval_ctl_phaseEvent_0 = _jspx_th_ctl_phaseEvent_0.doStartTag();
    if (_jspx_eval_ctl_phaseEvent_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_phaseEvent_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_phaseEvent_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_phaseEvent_0.doInitBody();
      }
      do {
        out.write("\n        ");
        org.recipnet.site.wrapper.StringBean rowClass = null;
        synchronized (_jspx_page_context) {
          rowClass = (org.recipnet.site.wrapper.StringBean) _jspx_page_context.getAttribute("rowClass", PageContext.PAGE_SCOPE);
          if (rowClass == null){
            rowClass = new org.recipnet.site.wrapper.StringBean();
            _jspx_page_context.setAttribute("rowClass", rowClass, PageContext.PAGE_SCOPE);
          }
        }
        out.write("\n      ");
        int evalDoAfterBody = _jspx_th_ctl_phaseEvent_0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_phaseEvent_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_phaseEvent_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_phaseEvent_onPhases.reuse(_jspx_th_ctl_phaseEvent_0);
    return false;
  }

  private boolean _jspx_meth_rn_wapSaveButton_0(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_selfForm_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:wapSaveButton
    org.recipnet.site.content.rncontrols.WapSaveButton _jspx_th_rn_wapSaveButton_0 = (org.recipnet.site.content.rncontrols.WapSaveButton) _jspx_tagPool_rn_wapSaveButton_nobody.get(org.recipnet.site.content.rncontrols.WapSaveButton.class);
    _jspx_th_rn_wapSaveButton_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_wapSaveButton_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
    int _jspx_eval_rn_wapSaveButton_0 = _jspx_th_rn_wapSaveButton_0.doStartTag();
    if (_jspx_th_rn_wapSaveButton_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_wapSaveButton_nobody.reuse(_jspx_th_rn_wapSaveButton_0);
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

  private boolean _jspx_meth_ctl_styleBlock_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_uploaderPage_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:styleBlock
    org.recipnet.common.controls.HtmlPageStyleBlock _jspx_th_ctl_styleBlock_0 = (org.recipnet.common.controls.HtmlPageStyleBlock) _jspx_tagPool_ctl_styleBlock.get(org.recipnet.common.controls.HtmlPageStyleBlock.class);
    _jspx_th_ctl_styleBlock_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_styleBlock_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_uploaderPage_0);
    int _jspx_eval_ctl_styleBlock_0 = _jspx_th_ctl_styleBlock_0.doStartTag();
    if (_jspx_eval_ctl_styleBlock_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_styleBlock_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_styleBlock_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_styleBlock_0.doInitBody();
      }
      do {
        out.write("\n    .headerFont { font-size: medium; font-weight: bold; }\n    .fileNameCell { font-weight: bold; padding: 0.1in }\n    .fileDetailCell { border-style: solid; border-width: thin; \n            border-color: #CCCCCC; padding: 0.1in; }\n    .spacerCell { font-size: 1%; padding: 0.03in }\n    .rowColorEven { background-color: #E6E6E6 }\n    .rowColorOdd  { background-color: #FFFFFF }\n    .fileTable { border-style: solid; border-width: thin;\n            border-color: #CCCCCC; padding: 0.01in; }\n    .pageInstructions { font-weight: bold; font-style: normal; font-size:\n            medium; text-decoration: none; }\n  ");
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
