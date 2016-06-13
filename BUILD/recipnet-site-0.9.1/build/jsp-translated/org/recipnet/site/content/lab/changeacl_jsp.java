package org.recipnet.site.content.lab;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.recipnet.site.content.rncontrols.WapPage;
import org.recipnet.site.shared.db.SampleAccessInfo;
import org.recipnet.site.shared.bl.SampleWorkflowBL;

public final class changeacl_jsp extends org.apache.jasper.runtime.HttpJspBase
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

  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_wapPage_workflowActionCodeCorrected_workflowActionCode_title_loginPageUrl_editSamplePageHref_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_errorMessage_errorFilter;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_selfForm;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_userIterator_sortByFullName;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_userField_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_userChecker_includeIfLabUser;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_labField_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_userChecker_includeIfProviderUser;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_providerField_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_radioButtonGroup;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleAccessSelector_accessLevel_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_userChecker_invert_includeIfCurrentlyLoggedInUser;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_userChecker_includeIfCurrentlyLoggedInUser;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_wapComments_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_wapSaveButton_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_wapCancelButton_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_styleBlock;

  public java.util.List getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _jspx_tagPool_rn_wapPage_workflowActionCodeCorrected_workflowActionCode_title_loginPageUrl_editSamplePageHref_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_errorMessage_errorFilter = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_selfForm = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_userIterator_sortByFullName = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_userField_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_userChecker_includeIfLabUser = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_labField_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_userChecker_includeIfProviderUser = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_providerField_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_radioButtonGroup = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleAccessSelector_accessLevel_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_userChecker_invert_includeIfCurrentlyLoggedInUser = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_userChecker_includeIfCurrentlyLoggedInUser = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_wapComments_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_wapSaveButton_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_wapCancelButton_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_styleBlock = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
  }

  public void _jspDestroy() {
    _jspx_tagPool_rn_wapPage_workflowActionCodeCorrected_workflowActionCode_title_loginPageUrl_editSamplePageHref_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName.release();
    _jspx_tagPool_ctl_errorMessage_errorFilter.release();
    _jspx_tagPool_ctl_selfForm.release();
    _jspx_tagPool_rn_userIterator_sortByFullName.release();
    _jspx_tagPool_rn_userField_nobody.release();
    _jspx_tagPool_rn_userChecker_includeIfLabUser.release();
    _jspx_tagPool_rn_labField_nobody.release();
    _jspx_tagPool_rn_userChecker_includeIfProviderUser.release();
    _jspx_tagPool_rn_providerField_nobody.release();
    _jspx_tagPool_ctl_radioButtonGroup.release();
    _jspx_tagPool_rn_sampleAccessSelector_accessLevel_nobody.release();
    _jspx_tagPool_rn_userChecker_invert_includeIfCurrentlyLoggedInUser.release();
    _jspx_tagPool_rn_userChecker_includeIfCurrentlyLoggedInUser.release();
    _jspx_tagPool_rn_wapComments_nobody.release();
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

      out.write("\n\n\n\n\n\n");
      //  rn:wapPage
      org.recipnet.site.content.rncontrols.WapPage _jspx_th_rn_wapPage_0 = (org.recipnet.site.content.rncontrols.WapPage) _jspx_tagPool_rn_wapPage_workflowActionCodeCorrected_workflowActionCode_title_loginPageUrl_editSamplePageHref_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName.get(org.recipnet.site.content.rncontrols.WapPage.class);
      _jspx_th_rn_wapPage_0.setPageContext(_jspx_page_context);
      _jspx_th_rn_wapPage_0.setParent(null);
      _jspx_th_rn_wapPage_0.setTitle("Change Access Level");
      _jspx_th_rn_wapPage_0.setWorkflowActionCode( SampleWorkflowBL.CHANGED_ACL );
      _jspx_th_rn_wapPage_0.setWorkflowActionCodeCorrected(SampleWorkflowBL.CHANGED_ACL);
      _jspx_th_rn_wapPage_0.setEditSamplePageHref("/lab/sample.jsp");
      _jspx_th_rn_wapPage_0.setLoginPageUrl("/login.jsp");
      _jspx_th_rn_wapPage_0.setCurrentPageReinvocationUrlParamName("origUrl");
      _jspx_th_rn_wapPage_0.setAuthorizationFailedReasonParamName("authorizationFailedReason");
      int _jspx_eval_rn_wapPage_0 = _jspx_th_rn_wapPage_0.doStartTag();
      if (_jspx_eval_rn_wapPage_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        if (_jspx_eval_rn_wapPage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
          out = _jspx_page_context.pushBody();
          _jspx_th_rn_wapPage_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
          _jspx_th_rn_wapPage_0.doInitBody();
        }
        do {
          out.write("\n  <div class=\"pageBody\">\n    <p class=\"pageInstructions\">\n      Every active user account on this site is listed below. To change\n      the access level of any user or users to this particular sample, select\n      the radio button corresponding to the desired access level(s), then\n      click the \"Save\" button to record the change(s).\n      ");
          //  ctl:errorMessage
          org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_0 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
          _jspx_th_ctl_errorMessage_0.setPageContext(_jspx_page_context);
          _jspx_th_ctl_errorMessage_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_wapPage_0);
          _jspx_th_ctl_errorMessage_0.setErrorFilter( WapPage.NESTED_TAG_REPORTED_VALIDATION_ERROR );
          int _jspx_eval_ctl_errorMessage_0 = _jspx_th_ctl_errorMessage_0.doStartTag();
          if (_jspx_eval_ctl_errorMessage_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_ctl_errorMessage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_ctl_errorMessage_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_ctl_errorMessage_0.doInitBody();
            }
            do {
              out.write("<br/>\n        <span class=\"errorMessage\"\n              style=\"font-weight: normal; font-style: italic;\">\n          You must address the flagged validation errors before the data\n          will be accepted.\n        </span>\n      ");
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
          out.write("\n    </p>\n    ");
          //  ctl:selfForm
          org.recipnet.common.controls.FormHtmlElement _jspx_th_ctl_selfForm_0 = (org.recipnet.common.controls.FormHtmlElement) _jspx_tagPool_ctl_selfForm.get(org.recipnet.common.controls.FormHtmlElement.class);
          _jspx_th_ctl_selfForm_0.setPageContext(_jspx_page_context);
          _jspx_th_ctl_selfForm_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_wapPage_0);
          int _jspx_eval_ctl_selfForm_0 = _jspx_th_ctl_selfForm_0.doStartTag();
          if (_jspx_eval_ctl_selfForm_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_ctl_selfForm_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_ctl_selfForm_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_ctl_selfForm_0.doInitBody();
            }
            do {
              out.write("\n      <table class=\"bodyTable\" cellspacing=\"0\">\n        <thead class=\"bordered\">\n        <tr>\n          <th class=\"leadSectionHead\" style=\"text-align: center\"\n            colspan=\"2\">User Information</th>\n          <th class=\"leadSectionHead\" style=\"text-align: center\"\n            colspan=\"3\">Access Level</th>\n        </tr>\n        <tr>\n          <th class=\"textSubhead\">User Name</th>\n          <th class=\"textSubhead\">Affiliation</th>\n          <th class=\"radioSubhead\">Read/Write</th>\n          <th class=\"radioSubhead\">Read Only</th>\n          <th class=\"radioSubhead\">No Access</th>\n        </tr>\n        </thead>\n        <tbody class=\"bordered\">\n        ");
              //  rn:userIterator
              org.recipnet.site.content.rncontrols.UserIterator _jspx_th_rn_userIterator_0 = (org.recipnet.site.content.rncontrols.UserIterator) _jspx_tagPool_rn_userIterator_sortByFullName.get(org.recipnet.site.content.rncontrols.UserIterator.class);
              _jspx_th_rn_userIterator_0.setPageContext(_jspx_page_context);
              _jspx_th_rn_userIterator_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_userIterator_0.setSortByFullName(true);
              int _jspx_eval_rn_userIterator_0 = _jspx_th_rn_userIterator_0.doStartTag();
              if (_jspx_eval_rn_userIterator_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_rn_userIterator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_rn_userIterator_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_rn_userIterator_0.doInitBody();
                }
                do {
                  out.write("\n          <tr class=\"");
                  out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${rn:testParity(ortRowCounter.count, 'evenRow', 'oddRow')}", java.lang.String.class, (PageContext)_jspx_page_context, _jspx_fnmap_0, false));
                  out.write("\">\n            <td style=\"padding-right: 1em;\">");
                  if (_jspx_meth_rn_userField_0(_jspx_th_rn_userIterator_0, _jspx_page_context))
                    return;
                  out.write("</td>\n            <td>\n              ");
                  if (_jspx_meth_rn_userChecker_0(_jspx_th_rn_userIterator_0, _jspx_page_context))
                    return;
                  out.write("\n              ");
                  if (_jspx_meth_rn_userChecker_1(_jspx_th_rn_userIterator_0, _jspx_page_context))
                    return;
                  out.write("\n            </td>\n            ");
                  //  ctl:radioButtonGroup
                  org.recipnet.common.controls.RadioButtonGroupHtmlControl _jspx_th_ctl_radioButtonGroup_0 = (org.recipnet.common.controls.RadioButtonGroupHtmlControl) _jspx_tagPool_ctl_radioButtonGroup.get(org.recipnet.common.controls.RadioButtonGroupHtmlControl.class);
                  _jspx_th_ctl_radioButtonGroup_0.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_radioButtonGroup_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_userIterator_0);
                  int _jspx_eval_ctl_radioButtonGroup_0 = _jspx_th_ctl_radioButtonGroup_0.doStartTag();
                  if (_jspx_eval_ctl_radioButtonGroup_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_radioButtonGroup_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_radioButtonGroup_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_radioButtonGroup_0.doInitBody();
                    }
                    do {
                      out.write("\n            <td class=\"radio\">\n              ");
                      //  rn:sampleAccessSelector
                      org.recipnet.site.content.rncontrols.SampleAccessSelector _jspx_th_rn_sampleAccessSelector_0 = (org.recipnet.site.content.rncontrols.SampleAccessSelector) _jspx_tagPool_rn_sampleAccessSelector_accessLevel_nobody.get(org.recipnet.site.content.rncontrols.SampleAccessSelector.class);
                      _jspx_th_rn_sampleAccessSelector_0.setPageContext(_jspx_page_context);
                      _jspx_th_rn_sampleAccessSelector_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_radioButtonGroup_0);
                      _jspx_th_rn_sampleAccessSelector_0.setAccessLevel(SampleAccessInfo.READ_WRITE);
                      int _jspx_eval_rn_sampleAccessSelector_0 = _jspx_th_rn_sampleAccessSelector_0.doStartTag();
                      if (_jspx_th_rn_sampleAccessSelector_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_rn_sampleAccessSelector_accessLevel_nobody.reuse(_jspx_th_rn_sampleAccessSelector_0);
                      out.write("\n            </td>\n            ");
                      //  rn:userChecker
                      org.recipnet.site.content.rncontrols.UserChecker _jspx_th_rn_userChecker_2 = (org.recipnet.site.content.rncontrols.UserChecker) _jspx_tagPool_rn_userChecker_invert_includeIfCurrentlyLoggedInUser.get(org.recipnet.site.content.rncontrols.UserChecker.class);
                      _jspx_th_rn_userChecker_2.setPageContext(_jspx_page_context);
                      _jspx_th_rn_userChecker_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_radioButtonGroup_0);
                      _jspx_th_rn_userChecker_2.setIncludeIfCurrentlyLoggedInUser(true);
                      _jspx_th_rn_userChecker_2.setInvert(true);
                      int _jspx_eval_rn_userChecker_2 = _jspx_th_rn_userChecker_2.doStartTag();
                      if (_jspx_eval_rn_userChecker_2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_rn_userChecker_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_rn_userChecker_2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_rn_userChecker_2.doInitBody();
                        }
                        do {
                          out.write("\n              <td class=\"radio\">\n                ");
                          //  rn:sampleAccessSelector
                          org.recipnet.site.content.rncontrols.SampleAccessSelector _jspx_th_rn_sampleAccessSelector_1 = (org.recipnet.site.content.rncontrols.SampleAccessSelector) _jspx_tagPool_rn_sampleAccessSelector_accessLevel_nobody.get(org.recipnet.site.content.rncontrols.SampleAccessSelector.class);
                          _jspx_th_rn_sampleAccessSelector_1.setPageContext(_jspx_page_context);
                          _jspx_th_rn_sampleAccessSelector_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_userChecker_2);
                          _jspx_th_rn_sampleAccessSelector_1.setAccessLevel(SampleAccessInfo.READ_ONLY);
                          int _jspx_eval_rn_sampleAccessSelector_1 = _jspx_th_rn_sampleAccessSelector_1.doStartTag();
                          if (_jspx_th_rn_sampleAccessSelector_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          _jspx_tagPool_rn_sampleAccessSelector_accessLevel_nobody.reuse(_jspx_th_rn_sampleAccessSelector_1);
                          out.write("\n              </td>\n              <td class=\"radio\">\n                ");
                          //  rn:sampleAccessSelector
                          org.recipnet.site.content.rncontrols.SampleAccessSelector _jspx_th_rn_sampleAccessSelector_2 = (org.recipnet.site.content.rncontrols.SampleAccessSelector) _jspx_tagPool_rn_sampleAccessSelector_accessLevel_nobody.get(org.recipnet.site.content.rncontrols.SampleAccessSelector.class);
                          _jspx_th_rn_sampleAccessSelector_2.setPageContext(_jspx_page_context);
                          _jspx_th_rn_sampleAccessSelector_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_userChecker_2);
                          _jspx_th_rn_sampleAccessSelector_2.setAccessLevel(SampleAccessInfo.INVALID_ACCESS);
                          int _jspx_eval_rn_sampleAccessSelector_2 = _jspx_th_rn_sampleAccessSelector_2.doStartTag();
                          if (_jspx_th_rn_sampleAccessSelector_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          _jspx_tagPool_rn_sampleAccessSelector_accessLevel_nobody.reuse(_jspx_th_rn_sampleAccessSelector_2);
                          out.write("\n              </td>\n            ");
                          int evalDoAfterBody = _jspx_th_rn_userChecker_2.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_rn_userChecker_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                          out = _jspx_page_context.popBody();
                      }
                      if (_jspx_th_rn_userChecker_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_rn_userChecker_invert_includeIfCurrentlyLoggedInUser.reuse(_jspx_th_rn_userChecker_2);
                      out.write("\n            ");
                      if (_jspx_meth_rn_userChecker_3(_jspx_th_ctl_radioButtonGroup_0, _jspx_page_context))
                        return;
                      out.write("\n            ");
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
                  out.write("\n          </tr>\n        ");
                  int evalDoAfterBody = _jspx_th_rn_userIterator_0.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_rn_userIterator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_rn_userIterator_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_userIterator_sortByFullName.reuse(_jspx_th_rn_userIterator_0);
              out.write("\n        </tbody>\n        <tbody>\n        <tr>\n          <th class=\"sectionHead\" colspan=\"5\">Comments</th>\n        </tr>\n        ");
              out.write("\n        <tr>\n          <td colspan=\"5\" style=\"text-align: center;\">\n            ");
              if (_jspx_meth_rn_wapComments_0(_jspx_th_ctl_selfForm_0, _jspx_page_context))
                return;
              out.write("\n          </td>\n        </tr>\n        <tr>\n          <td class=\"formButtons\" colspan=\"5\">\n            ");
              if (_jspx_meth_rn_wapSaveButton_0(_jspx_th_ctl_selfForm_0, _jspx_page_context))
                return;
              out.write("\n            ");
              if (_jspx_meth_rn_wapCancelButton_0(_jspx_th_ctl_selfForm_0, _jspx_page_context))
                return;
              out.write("\n          </td>\n        </tr>\n        </tbody>\n      </table>\n    ");
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
          out.write("\n  </div>\n  ");
          if (_jspx_meth_ctl_styleBlock_0(_jspx_th_rn_wapPage_0, _jspx_page_context))
            return;
          out.write('\n');
          int evalDoAfterBody = _jspx_th_rn_wapPage_0.doAfterBody();
          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
            break;
        } while (true);
        if (_jspx_eval_rn_wapPage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
          out = _jspx_page_context.popBody();
      }
      if (_jspx_th_rn_wapPage_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
        return;
      _jspx_tagPool_rn_wapPage_workflowActionCodeCorrected_workflowActionCode_title_loginPageUrl_editSamplePageHref_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName.reuse(_jspx_th_rn_wapPage_0);
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

  private boolean _jspx_meth_rn_userField_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_userIterator_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:userField
    org.recipnet.site.content.rncontrols.UserField _jspx_th_rn_userField_0 = (org.recipnet.site.content.rncontrols.UserField) _jspx_tagPool_rn_userField_nobody.get(org.recipnet.site.content.rncontrols.UserField.class);
    _jspx_th_rn_userField_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_userField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_userIterator_0);
    int _jspx_eval_rn_userField_0 = _jspx_th_rn_userField_0.doStartTag();
    if (_jspx_th_rn_userField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_userField_nobody.reuse(_jspx_th_rn_userField_0);
    return false;
  }

  private boolean _jspx_meth_rn_userChecker_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_userIterator_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:userChecker
    org.recipnet.site.content.rncontrols.UserChecker _jspx_th_rn_userChecker_0 = (org.recipnet.site.content.rncontrols.UserChecker) _jspx_tagPool_rn_userChecker_includeIfLabUser.get(org.recipnet.site.content.rncontrols.UserChecker.class);
    _jspx_th_rn_userChecker_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_userChecker_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_userIterator_0);
    _jspx_th_rn_userChecker_0.setIncludeIfLabUser(true);
    int _jspx_eval_rn_userChecker_0 = _jspx_th_rn_userChecker_0.doStartTag();
    if (_jspx_eval_rn_userChecker_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_userChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_userChecker_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_userChecker_0.doInitBody();
      }
      do {
        out.write("\n                ");
        if (_jspx_meth_rn_labField_0(_jspx_th_rn_userChecker_0, _jspx_page_context))
          return true;
        out.write("\n              ");
        int evalDoAfterBody = _jspx_th_rn_userChecker_0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_userChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_userChecker_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_userChecker_includeIfLabUser.reuse(_jspx_th_rn_userChecker_0);
    return false;
  }

  private boolean _jspx_meth_rn_labField_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_userChecker_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:labField
    org.recipnet.site.content.rncontrols.LabField _jspx_th_rn_labField_0 = (org.recipnet.site.content.rncontrols.LabField) _jspx_tagPool_rn_labField_nobody.get(org.recipnet.site.content.rncontrols.LabField.class);
    _jspx_th_rn_labField_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_labField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_userChecker_0);
    int _jspx_eval_rn_labField_0 = _jspx_th_rn_labField_0.doStartTag();
    if (_jspx_th_rn_labField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_labField_nobody.reuse(_jspx_th_rn_labField_0);
    return false;
  }

  private boolean _jspx_meth_rn_userChecker_1(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_userIterator_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:userChecker
    org.recipnet.site.content.rncontrols.UserChecker _jspx_th_rn_userChecker_1 = (org.recipnet.site.content.rncontrols.UserChecker) _jspx_tagPool_rn_userChecker_includeIfProviderUser.get(org.recipnet.site.content.rncontrols.UserChecker.class);
    _jspx_th_rn_userChecker_1.setPageContext(_jspx_page_context);
    _jspx_th_rn_userChecker_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_userIterator_0);
    _jspx_th_rn_userChecker_1.setIncludeIfProviderUser(true);
    int _jspx_eval_rn_userChecker_1 = _jspx_th_rn_userChecker_1.doStartTag();
    if (_jspx_eval_rn_userChecker_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_userChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_userChecker_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_userChecker_1.doInitBody();
      }
      do {
        out.write("\n                ");
        if (_jspx_meth_rn_providerField_0(_jspx_th_rn_userChecker_1, _jspx_page_context))
          return true;
        out.write("\n              ");
        int evalDoAfterBody = _jspx_th_rn_userChecker_1.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_userChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_userChecker_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_userChecker_includeIfProviderUser.reuse(_jspx_th_rn_userChecker_1);
    return false;
  }

  private boolean _jspx_meth_rn_providerField_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_userChecker_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:providerField
    org.recipnet.site.content.rncontrols.ProviderField _jspx_th_rn_providerField_0 = (org.recipnet.site.content.rncontrols.ProviderField) _jspx_tagPool_rn_providerField_nobody.get(org.recipnet.site.content.rncontrols.ProviderField.class);
    _jspx_th_rn_providerField_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_providerField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_userChecker_1);
    int _jspx_eval_rn_providerField_0 = _jspx_th_rn_providerField_0.doStartTag();
    if (_jspx_th_rn_providerField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_providerField_nobody.reuse(_jspx_th_rn_providerField_0);
    return false;
  }

  private boolean _jspx_meth_rn_userChecker_3(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_radioButtonGroup_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:userChecker
    org.recipnet.site.content.rncontrols.UserChecker _jspx_th_rn_userChecker_3 = (org.recipnet.site.content.rncontrols.UserChecker) _jspx_tagPool_rn_userChecker_includeIfCurrentlyLoggedInUser.get(org.recipnet.site.content.rncontrols.UserChecker.class);
    _jspx_th_rn_userChecker_3.setPageContext(_jspx_page_context);
    _jspx_th_rn_userChecker_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_radioButtonGroup_0);
    _jspx_th_rn_userChecker_3.setIncludeIfCurrentlyLoggedInUser(true);
    int _jspx_eval_rn_userChecker_3 = _jspx_th_rn_userChecker_3.doStartTag();
    if (_jspx_eval_rn_userChecker_3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_userChecker_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_userChecker_3.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_userChecker_3.doInitBody();
      }
      do {
        out.write("\n              <td colspan=\"2\" style=\"text-align: center;\">\n                <span class=\"note\">\n                  (you may not reduce your own access level)\n                </span>\n              </td>\n            ");
        int evalDoAfterBody = _jspx_th_rn_userChecker_3.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_userChecker_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_userChecker_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_userChecker_includeIfCurrentlyLoggedInUser.reuse(_jspx_th_rn_userChecker_3);
    return false;
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

  private boolean _jspx_meth_ctl_styleBlock_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_wapPage_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:styleBlock
    org.recipnet.common.controls.HtmlPageStyleBlock _jspx_th_ctl_styleBlock_0 = (org.recipnet.common.controls.HtmlPageStyleBlock) _jspx_tagPool_ctl_styleBlock.get(org.recipnet.common.controls.HtmlPageStyleBlock.class);
    _jspx_th_ctl_styleBlock_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_styleBlock_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_wapPage_0);
    int _jspx_eval_ctl_styleBlock_0 = _jspx_th_ctl_styleBlock_0.doStartTag();
    if (_jspx_eval_ctl_styleBlock_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_styleBlock_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_styleBlock_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_styleBlock_0.doInitBody();
      }
      do {
        out.write("\n    table.bodyTable th.textSubhead {\n        text-align: left; padding-right: 1em; }\n    table.bodyTable th.radioSubhead {\n        text-align: center; padding: 0 1em 0 1em; }\n    table.bodyTable td.radio { text-align: center; }\n    table.bodyTable td { white-space: nowrap; }\n    thead.bordered { background: #32357D; color: #FFFFFF;\n         border-left: 3px solid #32357D;\n         border-right: 3px solid #32357D; border-top: 3px solid #32357D; }\n    tbody.bordered { border-left: 3px solid #32357D;\n         border-right: 3px solid #32357D; border-bottom: 3px solid #32357D; }\n    .note { color: #505050; font-style: italic; font-size: x-small; }\n  ");
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
