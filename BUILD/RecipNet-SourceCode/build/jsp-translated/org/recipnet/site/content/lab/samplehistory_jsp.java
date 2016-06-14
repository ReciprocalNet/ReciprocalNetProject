package org.recipnet.site.content.lab;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.recipnet.site.content.rncontrols.AuthorizationReasonMessage;
import org.recipnet.site.content.rncontrols.SampleHistoryField;
import org.recipnet.site.shared.db.SampleInfo;

public final class samplehistory_jsp extends org.apache.jasper.runtime.HttpJspBase
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

  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_samplePage_title_requireSampleHistoryId_loginPageUrl_ignoreSampleHistoryId_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_authorizationChecker_suppressRenderingOnly_invert_canEditSample;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_redirectToLogin_target_returnUrlParamName_authorizationReasonParamName_authorizationReasonCode_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_labField_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_providerField_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleField_fieldCode_displayValueOnly_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleField_fieldCode_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleHistoryIterator_id;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_parityChecker_includeOnlyOnLast;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_parityChecker_invert_includeOnlyOnLast;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleHistoryField_fieldCode_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_showsampleLink_sampleIsKnownToBeLocal_detailedPageUrl_basicPageUrl;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleParam_name_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleHistoryParam_name_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleChecker_includeIfUnpublishedVersionOfPublicSample;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_a_href;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleHistoryParam_useMostRecentHistoryId_name_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleChecker_invert_includeIfUnpublishedVersionOfPublicSample;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_link_href;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_styleBlock;

  public java.util.List getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _jspx_tagPool_rn_samplePage_title_requireSampleHistoryId_loginPageUrl_ignoreSampleHistoryId_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_authorizationChecker_suppressRenderingOnly_invert_canEditSample = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_redirectToLogin_target_returnUrlParamName_authorizationReasonParamName_authorizationReasonCode_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_labField_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_providerField_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleField_fieldCode_displayValueOnly_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleField_fieldCode_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleHistoryIterator_id = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_parityChecker_includeOnlyOnLast = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_parityChecker_invert_includeOnlyOnLast = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleHistoryField_fieldCode_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_showsampleLink_sampleIsKnownToBeLocal_detailedPageUrl_basicPageUrl = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleParam_name_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleHistoryParam_name_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleChecker_includeIfUnpublishedVersionOfPublicSample = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_a_href = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleHistoryParam_useMostRecentHistoryId_name_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleChecker_invert_includeIfUnpublishedVersionOfPublicSample = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_link_href = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_styleBlock = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
  }

  public void _jspDestroy() {
    _jspx_tagPool_rn_samplePage_title_requireSampleHistoryId_loginPageUrl_ignoreSampleHistoryId_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName.release();
    _jspx_tagPool_rn_authorizationChecker_suppressRenderingOnly_invert_canEditSample.release();
    _jspx_tagPool_rn_redirectToLogin_target_returnUrlParamName_authorizationReasonParamName_authorizationReasonCode_nobody.release();
    _jspx_tagPool_rn_labField_nobody.release();
    _jspx_tagPool_rn_providerField_nobody.release();
    _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.release();
    _jspx_tagPool_rn_sampleField_fieldCode_displayValueOnly_nobody.release();
    _jspx_tagPool_rn_sampleField_fieldCode_nobody.release();
    _jspx_tagPool_rn_sampleHistoryIterator_id.release();
    _jspx_tagPool_ctl_parityChecker_includeOnlyOnLast.release();
    _jspx_tagPool_ctl_parityChecker_invert_includeOnlyOnLast.release();
    _jspx_tagPool_rn_sampleHistoryField_fieldCode_nobody.release();
    _jspx_tagPool_rn_showsampleLink_sampleIsKnownToBeLocal_detailedPageUrl_basicPageUrl.release();
    _jspx_tagPool_rn_sampleParam_name_nobody.release();
    _jspx_tagPool_rn_sampleHistoryParam_name_nobody.release();
    _jspx_tagPool_rn_sampleChecker_includeIfUnpublishedVersionOfPublicSample.release();
    _jspx_tagPool_ctl_a_href.release();
    _jspx_tagPool_rn_sampleHistoryParam_useMostRecentHistoryId_name_nobody.release();
    _jspx_tagPool_rn_sampleChecker_invert_includeIfUnpublishedVersionOfPublicSample.release();
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
      //  rn:samplePage
      org.recipnet.site.content.rncontrols.SamplePage _jspx_th_rn_samplePage_0 = (org.recipnet.site.content.rncontrols.SamplePage) _jspx_tagPool_rn_samplePage_title_requireSampleHistoryId_loginPageUrl_ignoreSampleHistoryId_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName.get(org.recipnet.site.content.rncontrols.SamplePage.class);
      _jspx_th_rn_samplePage_0.setPageContext(_jspx_page_context);
      _jspx_th_rn_samplePage_0.setParent(null);
      _jspx_th_rn_samplePage_0.setTitle("Sample History");
      _jspx_th_rn_samplePage_0.setRequireSampleHistoryId(false);
      _jspx_th_rn_samplePage_0.setIgnoreSampleHistoryId(true);
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
          out.write('\n');
          out.write(' ');
          out.write(' ');
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
            AuthorizationReasonMessage.CANNOT_SEE_LAB_SUMMARY);
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
          out.write("\n  <br />\n  <table>\n    <tr>\n      <th class=\"twoColumnLeft\">Lab:</th>\n      <td class=\"twoColumnRight\">\n        ");
          if (_jspx_meth_rn_labField_0(_jspx_th_rn_samplePage_0, _jspx_page_context))
            return;
          out.write("\n      </td>\n    </tr>\n    <tr>\n      <th class=\"twoColumnLeft\">Provider:</th>\n      <td class=\"twoColumnRight\">\n        ");
          if (_jspx_meth_rn_providerField_0(_jspx_th_rn_samplePage_0, _jspx_page_context))
            return;
          out.write("\n      </td>\n    </tr>\n    <tr>\n      <th class=\"twoColumnLeft\">\n        ");
          //  rn:sampleFieldLabel
          org.recipnet.site.content.rncontrols.SampleFieldLabel _jspx_th_rn_sampleFieldLabel_0 = (org.recipnet.site.content.rncontrols.SampleFieldLabel) _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldLabel.class);
          _jspx_th_rn_sampleFieldLabel_0.setPageContext(_jspx_page_context);
          _jspx_th_rn_sampleFieldLabel_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_samplePage_0);
          _jspx_th_rn_sampleFieldLabel_0.setFieldCode(SampleInfo.LOCAL_LAB_ID);
          int _jspx_eval_rn_sampleFieldLabel_0 = _jspx_th_rn_sampleFieldLabel_0.doStartTag();
          if (_jspx_th_rn_sampleFieldLabel_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
            return;
          _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.reuse(_jspx_th_rn_sampleFieldLabel_0);
          out.write(":\n      </th>\n      <td class=\"twoColumnRight\">\n        ");
          //  rn:sampleField
          org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_0 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_fieldCode_displayValueOnly_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
          _jspx_th_rn_sampleField_0.setPageContext(_jspx_page_context);
          _jspx_th_rn_sampleField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_samplePage_0);
          _jspx_th_rn_sampleField_0.setDisplayValueOnly(true);
          _jspx_th_rn_sampleField_0.setFieldCode(SampleInfo.LOCAL_LAB_ID);
          int _jspx_eval_rn_sampleField_0 = _jspx_th_rn_sampleField_0.doStartTag();
          if (_jspx_th_rn_sampleField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
            return;
          _jspx_tagPool_rn_sampleField_fieldCode_displayValueOnly_nobody.reuse(_jspx_th_rn_sampleField_0);
          out.write("\n      </td>\n    </tr>\n    <tr>\n      <th class=\"twoColumnLeft\">\n        ");
          //  rn:sampleFieldLabel
          org.recipnet.site.content.rncontrols.SampleFieldLabel _jspx_th_rn_sampleFieldLabel_1 = (org.recipnet.site.content.rncontrols.SampleFieldLabel) _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldLabel.class);
          _jspx_th_rn_sampleFieldLabel_1.setPageContext(_jspx_page_context);
          _jspx_th_rn_sampleFieldLabel_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_samplePage_0);
          _jspx_th_rn_sampleFieldLabel_1.setFieldCode(SampleInfo.STATUS);
          int _jspx_eval_rn_sampleFieldLabel_1 = _jspx_th_rn_sampleFieldLabel_1.doStartTag();
          if (_jspx_th_rn_sampleFieldLabel_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
            return;
          _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.reuse(_jspx_th_rn_sampleFieldLabel_1);
          out.write(":\n      </th>\n      <td class=\"twoColumnRight\">\n        ");
          //  rn:sampleField
          org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_1 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
          _jspx_th_rn_sampleField_1.setPageContext(_jspx_page_context);
          _jspx_th_rn_sampleField_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_samplePage_0);
          _jspx_th_rn_sampleField_1.setFieldCode(SampleInfo.STATUS);
          int _jspx_eval_rn_sampleField_1 = _jspx_th_rn_sampleField_1.doStartTag();
          if (_jspx_th_rn_sampleField_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
            return;
          _jspx_tagPool_rn_sampleField_fieldCode_nobody.reuse(_jspx_th_rn_sampleField_1);
          out.write("\n      </td> \n    </tr>\n  </table>\n  <br /> \n  <table>\n    <tr>\n      <th class=\"headerCell\">Date</th>\n      <th class=\"headerCell\">User</th>\n      <th class=\"headerCell\">Action</th>\n      <th class=\"headerCell\">New status</th>\n      <th class=\"headerCell\">Comments</th>\n      <th class=\"headerCell\">&nbsp;</th>\n      <th class=\"headerCell\">&nbsp;</th>\n    </tr>\n    ");
          //  rn:sampleHistoryIterator
          org.recipnet.site.content.rncontrols.SampleHistoryIterator hit = null;
          org.recipnet.site.content.rncontrols.SampleHistoryIterator _jspx_th_rn_sampleHistoryIterator_0 = (org.recipnet.site.content.rncontrols.SampleHistoryIterator) _jspx_tagPool_rn_sampleHistoryIterator_id.get(org.recipnet.site.content.rncontrols.SampleHistoryIterator.class);
          _jspx_th_rn_sampleHistoryIterator_0.setPageContext(_jspx_page_context);
          _jspx_th_rn_sampleHistoryIterator_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_samplePage_0);
          _jspx_th_rn_sampleHistoryIterator_0.setId("hit");
          int _jspx_eval_rn_sampleHistoryIterator_0 = _jspx_th_rn_sampleHistoryIterator_0.doStartTag();
          if (_jspx_eval_rn_sampleHistoryIterator_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_rn_sampleHistoryIterator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_rn_sampleHistoryIterator_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_rn_sampleHistoryIterator_0.doInitBody();
            }
            hit = (org.recipnet.site.content.rncontrols.SampleHistoryIterator) _jspx_page_context.findAttribute("hit");
            do {
              out.write("\n      ");
              if (_jspx_meth_ctl_parityChecker_0(_jspx_th_rn_sampleHistoryIterator_0, _jspx_page_context))
                return;
              out.write("\n      ");
              if (_jspx_meth_ctl_parityChecker_1(_jspx_th_rn_sampleHistoryIterator_0, _jspx_page_context))
                return;
              out.write("\n        <td class=\"bodyCell\">\n          ");
              //  rn:sampleHistoryField
              org.recipnet.site.content.rncontrols.SampleHistoryField _jspx_th_rn_sampleHistoryField_0 = (org.recipnet.site.content.rncontrols.SampleHistoryField) _jspx_tagPool_rn_sampleHistoryField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleHistoryField.class);
              _jspx_th_rn_sampleHistoryField_0.setPageContext(_jspx_page_context);
              _jspx_th_rn_sampleHistoryField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleHistoryIterator_0);
              _jspx_th_rn_sampleHistoryField_0.setFieldCode(SampleHistoryField.FieldCode.ACTION_DATE);
              int _jspx_eval_rn_sampleHistoryField_0 = _jspx_th_rn_sampleHistoryField_0.doStartTag();
              if (_jspx_th_rn_sampleHistoryField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_sampleHistoryField_fieldCode_nobody.reuse(_jspx_th_rn_sampleHistoryField_0);
              out.write("\n        </td>\n        <td class=\"bodyCell\">\n          ");
              //  rn:sampleHistoryField
              org.recipnet.site.content.rncontrols.SampleHistoryField _jspx_th_rn_sampleHistoryField_1 = (org.recipnet.site.content.rncontrols.SampleHistoryField) _jspx_tagPool_rn_sampleHistoryField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleHistoryField.class);
              _jspx_th_rn_sampleHistoryField_1.setPageContext(_jspx_page_context);
              _jspx_th_rn_sampleHistoryField_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleHistoryIterator_0);
              _jspx_th_rn_sampleHistoryField_1.setFieldCode(
         SampleHistoryField.FieldCode.USER_FULLNAME_THAT_PERFORMED_ACTION);
              int _jspx_eval_rn_sampleHistoryField_1 = _jspx_th_rn_sampleHistoryField_1.doStartTag();
              if (_jspx_th_rn_sampleHistoryField_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_sampleHistoryField_fieldCode_nobody.reuse(_jspx_th_rn_sampleHistoryField_1);
              out.write("\n        </td>\n        <td class=\"bodyCell\">\n          ");
              //  rn:sampleHistoryField
              org.recipnet.site.content.rncontrols.SampleHistoryField _jspx_th_rn_sampleHistoryField_2 = (org.recipnet.site.content.rncontrols.SampleHistoryField) _jspx_tagPool_rn_sampleHistoryField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleHistoryField.class);
              _jspx_th_rn_sampleHistoryField_2.setPageContext(_jspx_page_context);
              _jspx_th_rn_sampleHistoryField_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleHistoryIterator_0);
              _jspx_th_rn_sampleHistoryField_2.setFieldCode(
              SampleHistoryField.FieldCode.ACTION_PERFORMED);
              int _jspx_eval_rn_sampleHistoryField_2 = _jspx_th_rn_sampleHistoryField_2.doStartTag();
              if (_jspx_th_rn_sampleHistoryField_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_sampleHistoryField_fieldCode_nobody.reuse(_jspx_th_rn_sampleHistoryField_2);
              out.write("\n        </td>\n        <td class=\"bodyCell\">\n          ");
              //  rn:sampleHistoryField
              org.recipnet.site.content.rncontrols.SampleHistoryField _jspx_th_rn_sampleHistoryField_3 = (org.recipnet.site.content.rncontrols.SampleHistoryField) _jspx_tagPool_rn_sampleHistoryField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleHistoryField.class);
              _jspx_th_rn_sampleHistoryField_3.setPageContext(_jspx_page_context);
              _jspx_th_rn_sampleHistoryField_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleHistoryIterator_0);
              _jspx_th_rn_sampleHistoryField_3.setFieldCode(SampleHistoryField.FieldCode.NEW_STATUS);
              int _jspx_eval_rn_sampleHistoryField_3 = _jspx_th_rn_sampleHistoryField_3.doStartTag();
              if (_jspx_th_rn_sampleHistoryField_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_sampleHistoryField_fieldCode_nobody.reuse(_jspx_th_rn_sampleHistoryField_3);
              out.write("\n        </td>\n        <td class=\"bodyCell\">\n          ");
              //  rn:sampleHistoryField
              org.recipnet.site.content.rncontrols.SampleHistoryField _jspx_th_rn_sampleHistoryField_4 = (org.recipnet.site.content.rncontrols.SampleHistoryField) _jspx_tagPool_rn_sampleHistoryField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleHistoryField.class);
              _jspx_th_rn_sampleHistoryField_4.setPageContext(_jspx_page_context);
              _jspx_th_rn_sampleHistoryField_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleHistoryIterator_0);
              _jspx_th_rn_sampleHistoryField_4.setFieldCode(
              SampleHistoryField.FieldCode.WORKFLOW_ACTION_COMMENTS);
              int _jspx_eval_rn_sampleHistoryField_4 = _jspx_th_rn_sampleHistoryField_4.doStartTag();
              if (_jspx_th_rn_sampleHistoryField_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_sampleHistoryField_fieldCode_nobody.reuse(_jspx_th_rn_sampleHistoryField_4);
              out.write("\n          &nbsp;\n        </td> \n        <td class=\"bodyCell\">\n          ");
              if (_jspx_meth_rn_showsampleLink_0(_jspx_th_rn_sampleHistoryIterator_0, _jspx_page_context))
                return;
              out.write("\n        </td>\n        ");
              if (_jspx_meth_ctl_parityChecker_2(_jspx_th_rn_sampleHistoryIterator_0, _jspx_page_context))
                return;
              out.write("\n        ");
              if (_jspx_meth_ctl_parityChecker_3(_jspx_th_rn_sampleHistoryIterator_0, _jspx_page_context))
                return;
              out.write("\n      </tr>\n    ");
              int evalDoAfterBody = _jspx_th_rn_sampleHistoryIterator_0.doAfterBody();
              hit = (org.recipnet.site.content.rncontrols.SampleHistoryIterator) _jspx_page_context.findAttribute("hit");
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
            if (_jspx_eval_rn_sampleHistoryIterator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
              out = _jspx_page_context.popBody();
          }
          if (_jspx_th_rn_sampleHistoryIterator_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
            return;
          hit = (org.recipnet.site.content.rncontrols.SampleHistoryIterator) _jspx_page_context.findAttribute("hit");
          _jspx_tagPool_rn_sampleHistoryIterator_id.reuse(_jspx_th_rn_sampleHistoryIterator_0);
          out.write("\n  </table>\n  <table width=\"100%\">\n    <tr>\n      <td align=\"right\">\n        ");
          if (_jspx_meth_rn_showsampleLink_1(_jspx_th_rn_samplePage_0, _jspx_page_context))
            return;
          out.write("\n        <br />\n        ");
          if (_jspx_meth_rn_link_0(_jspx_th_rn_samplePage_0, _jspx_page_context))
            return;
          out.write("\n      </td>\n    </tr>\n  </table>\n  ");
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
      _jspx_tagPool_rn_samplePage_title_requireSampleHistoryId_loginPageUrl_ignoreSampleHistoryId_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName.reuse(_jspx_th_rn_samplePage_0);
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

  private boolean _jspx_meth_rn_labField_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_samplePage_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:labField
    org.recipnet.site.content.rncontrols.LabField _jspx_th_rn_labField_0 = (org.recipnet.site.content.rncontrols.LabField) _jspx_tagPool_rn_labField_nobody.get(org.recipnet.site.content.rncontrols.LabField.class);
    _jspx_th_rn_labField_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_labField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_samplePage_0);
    int _jspx_eval_rn_labField_0 = _jspx_th_rn_labField_0.doStartTag();
    if (_jspx_th_rn_labField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_labField_nobody.reuse(_jspx_th_rn_labField_0);
    return false;
  }

  private boolean _jspx_meth_rn_providerField_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_samplePage_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:providerField
    org.recipnet.site.content.rncontrols.ProviderField _jspx_th_rn_providerField_0 = (org.recipnet.site.content.rncontrols.ProviderField) _jspx_tagPool_rn_providerField_nobody.get(org.recipnet.site.content.rncontrols.ProviderField.class);
    _jspx_th_rn_providerField_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_providerField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_samplePage_0);
    int _jspx_eval_rn_providerField_0 = _jspx_th_rn_providerField_0.doStartTag();
    if (_jspx_th_rn_providerField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_providerField_nobody.reuse(_jspx_th_rn_providerField_0);
    return false;
  }

  private boolean _jspx_meth_ctl_parityChecker_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleHistoryIterator_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:parityChecker
    org.recipnet.common.controls.ParityChecker _jspx_th_ctl_parityChecker_0 = (org.recipnet.common.controls.ParityChecker) _jspx_tagPool_ctl_parityChecker_includeOnlyOnLast.get(org.recipnet.common.controls.ParityChecker.class);
    _jspx_th_ctl_parityChecker_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_parityChecker_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleHistoryIterator_0);
    _jspx_th_ctl_parityChecker_0.setIncludeOnlyOnLast(true);
    int _jspx_eval_ctl_parityChecker_0 = _jspx_th_ctl_parityChecker_0.doStartTag();
    if (_jspx_eval_ctl_parityChecker_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_parityChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_parityChecker_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_parityChecker_0.doInitBody();
      }
      do {
        out.write("\n        <tr class=\"lastRow\">\n      ");
        int evalDoAfterBody = _jspx_th_ctl_parityChecker_0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_parityChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_parityChecker_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_parityChecker_includeOnlyOnLast.reuse(_jspx_th_ctl_parityChecker_0);
    return false;
  }

  private boolean _jspx_meth_ctl_parityChecker_1(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleHistoryIterator_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:parityChecker
    org.recipnet.common.controls.ParityChecker _jspx_th_ctl_parityChecker_1 = (org.recipnet.common.controls.ParityChecker) _jspx_tagPool_ctl_parityChecker_invert_includeOnlyOnLast.get(org.recipnet.common.controls.ParityChecker.class);
    _jspx_th_ctl_parityChecker_1.setPageContext(_jspx_page_context);
    _jspx_th_ctl_parityChecker_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleHistoryIterator_0);
    _jspx_th_ctl_parityChecker_1.setIncludeOnlyOnLast(true);
    _jspx_th_ctl_parityChecker_1.setInvert(true);
    int _jspx_eval_ctl_parityChecker_1 = _jspx_th_ctl_parityChecker_1.doStartTag();
    if (_jspx_eval_ctl_parityChecker_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_parityChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_parityChecker_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_parityChecker_1.doInitBody();
      }
      do {
        out.write("\n        <tr class=\"");
        out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${rn:testParity(hit.iterationCountSinceThisPhaseBegan,\n                                   'evenRow', 'oddRow')}", java.lang.String.class, (PageContext)_jspx_page_context, _jspx_fnmap_0, false));
        out.write("\">\n      ");
        int evalDoAfterBody = _jspx_th_ctl_parityChecker_1.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_parityChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_parityChecker_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_parityChecker_invert_includeOnlyOnLast.reuse(_jspx_th_ctl_parityChecker_1);
    return false;
  }

  private boolean _jspx_meth_rn_showsampleLink_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleHistoryIterator_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:showsampleLink
    org.recipnet.site.content.rncontrols.ShowsampleLink _jspx_th_rn_showsampleLink_0 = (org.recipnet.site.content.rncontrols.ShowsampleLink) _jspx_tagPool_rn_showsampleLink_sampleIsKnownToBeLocal_detailedPageUrl_basicPageUrl.get(org.recipnet.site.content.rncontrols.ShowsampleLink.class);
    _jspx_th_rn_showsampleLink_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_showsampleLink_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleHistoryIterator_0);
    _jspx_th_rn_showsampleLink_0.setSampleIsKnownToBeLocal(true);
    _jspx_th_rn_showsampleLink_0.setBasicPageUrl("/showsamplebasic.jsp");
    _jspx_th_rn_showsampleLink_0.setDetailedPageUrl("/showsampledetailed.jsp");
    int _jspx_eval_rn_showsampleLink_0 = _jspx_th_rn_showsampleLink_0.doStartTag();
    if (_jspx_eval_rn_showsampleLink_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_showsampleLink_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_showsampleLink_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_showsampleLink_0.doInitBody();
      }
      do {
        out.write("\n            ");
        if (_jspx_meth_rn_sampleParam_0(_jspx_th_rn_showsampleLink_0, _jspx_page_context))
          return true;
        out.write("\n            ");
        if (_jspx_meth_rn_sampleHistoryParam_0(_jspx_th_rn_showsampleLink_0, _jspx_page_context))
          return true;
        out.write("\n            View\n          ");
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

  private boolean _jspx_meth_ctl_parityChecker_2(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleHistoryIterator_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:parityChecker
    org.recipnet.common.controls.ParityChecker _jspx_th_ctl_parityChecker_2 = (org.recipnet.common.controls.ParityChecker) _jspx_tagPool_ctl_parityChecker_includeOnlyOnLast.get(org.recipnet.common.controls.ParityChecker.class);
    _jspx_th_ctl_parityChecker_2.setPageContext(_jspx_page_context);
    _jspx_th_ctl_parityChecker_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleHistoryIterator_0);
    _jspx_th_ctl_parityChecker_2.setIncludeOnlyOnLast(true);
    int _jspx_eval_ctl_parityChecker_2 = _jspx_th_ctl_parityChecker_2.doStartTag();
    if (_jspx_eval_ctl_parityChecker_2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_parityChecker_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_parityChecker_2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_parityChecker_2.doInitBody();
      }
      do {
        out.write("\n          <td class=\"bodyCell\">Most recent version</td>\n        ");
        int evalDoAfterBody = _jspx_th_ctl_parityChecker_2.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_parityChecker_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_parityChecker_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_parityChecker_includeOnlyOnLast.reuse(_jspx_th_ctl_parityChecker_2);
    return false;
  }

  private boolean _jspx_meth_ctl_parityChecker_3(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleHistoryIterator_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:parityChecker
    org.recipnet.common.controls.ParityChecker _jspx_th_ctl_parityChecker_3 = (org.recipnet.common.controls.ParityChecker) _jspx_tagPool_ctl_parityChecker_invert_includeOnlyOnLast.get(org.recipnet.common.controls.ParityChecker.class);
    _jspx_th_ctl_parityChecker_3.setPageContext(_jspx_page_context);
    _jspx_th_ctl_parityChecker_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleHistoryIterator_0);
    _jspx_th_ctl_parityChecker_3.setIncludeOnlyOnLast(true);
    _jspx_th_ctl_parityChecker_3.setInvert(true);
    int _jspx_eval_ctl_parityChecker_3 = _jspx_th_ctl_parityChecker_3.doStartTag();
    if (_jspx_eval_ctl_parityChecker_3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_parityChecker_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_parityChecker_3.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_parityChecker_3.doInitBody();
      }
      do {
        out.write("\n          <td class=\"bodyCell\">\n            ");
        if (_jspx_meth_rn_sampleChecker_0(_jspx_th_ctl_parityChecker_3, _jspx_page_context))
          return true;
        out.write("\n            ");
        if (_jspx_meth_rn_sampleChecker_1(_jspx_th_ctl_parityChecker_3, _jspx_page_context))
          return true;
        out.write("\n          </td>\n        ");
        int evalDoAfterBody = _jspx_th_ctl_parityChecker_3.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_parityChecker_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_parityChecker_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_parityChecker_invert_includeOnlyOnLast.reuse(_jspx_th_ctl_parityChecker_3);
    return false;
  }

  private boolean _jspx_meth_rn_sampleChecker_0(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_parityChecker_3, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:sampleChecker
    org.recipnet.site.content.rncontrols.SampleChecker _jspx_th_rn_sampleChecker_0 = (org.recipnet.site.content.rncontrols.SampleChecker) _jspx_tagPool_rn_sampleChecker_includeIfUnpublishedVersionOfPublicSample.get(org.recipnet.site.content.rncontrols.SampleChecker.class);
    _jspx_th_rn_sampleChecker_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_sampleChecker_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_parityChecker_3);
    _jspx_th_rn_sampleChecker_0.setIncludeIfUnpublishedVersionOfPublicSample(true);
    int _jspx_eval_rn_sampleChecker_0 = _jspx_th_rn_sampleChecker_0.doStartTag();
    if (_jspx_eval_rn_sampleChecker_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_sampleChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_sampleChecker_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_sampleChecker_0.doInitBody();
      }
      do {
        out.write("\n              ");
        if (_jspx_meth_ctl_a_0(_jspx_th_rn_sampleChecker_0, _jspx_page_context))
          return true;
        out.write("\n            ");
        int evalDoAfterBody = _jspx_th_rn_sampleChecker_0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_sampleChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_sampleChecker_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_sampleChecker_includeIfUnpublishedVersionOfPublicSample.reuse(_jspx_th_rn_sampleChecker_0);
    return false;
  }

  private boolean _jspx_meth_ctl_a_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleChecker_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:a
    org.recipnet.common.controls.LinkHtmlElement _jspx_th_ctl_a_0 = (org.recipnet.common.controls.LinkHtmlElement) _jspx_tagPool_ctl_a_href.get(org.recipnet.common.controls.LinkHtmlElement.class);
    _jspx_th_ctl_a_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_a_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleChecker_0);
    _jspx_th_ctl_a_0.setHref("/lab/considerrevert.jsp");
    int _jspx_eval_ctl_a_0 = _jspx_th_ctl_a_0.doStartTag();
    if (_jspx_eval_ctl_a_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_a_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_a_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_a_0.doInitBody();
      }
      do {
        out.write("\n                ");
        if (_jspx_meth_rn_sampleParam_1(_jspx_th_ctl_a_0, _jspx_page_context))
          return true;
        out.write("\n                ");
        if (_jspx_meth_rn_sampleHistoryParam_1(_jspx_th_ctl_a_0, _jspx_page_context))
          return true;
        out.write("\n                ");
        if (_jspx_meth_rn_sampleHistoryParam_2(_jspx_th_ctl_a_0, _jspx_page_context))
          return true;
        out.write("\n                Revert to this version\n              ");
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

  private boolean _jspx_meth_rn_sampleParam_1(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_a_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:sampleParam
    org.recipnet.site.content.rncontrols.SampleParam _jspx_th_rn_sampleParam_1 = (org.recipnet.site.content.rncontrols.SampleParam) _jspx_tagPool_rn_sampleParam_name_nobody.get(org.recipnet.site.content.rncontrols.SampleParam.class);
    _jspx_th_rn_sampleParam_1.setPageContext(_jspx_page_context);
    _jspx_th_rn_sampleParam_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_0);
    _jspx_th_rn_sampleParam_1.setName("sampleId");
    int _jspx_eval_rn_sampleParam_1 = _jspx_th_rn_sampleParam_1.doStartTag();
    if (_jspx_th_rn_sampleParam_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_sampleParam_name_nobody.reuse(_jspx_th_rn_sampleParam_1);
    return false;
  }

  private boolean _jspx_meth_rn_sampleHistoryParam_1(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_a_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:sampleHistoryParam
    org.recipnet.site.content.rncontrols.SampleHistoryParam _jspx_th_rn_sampleHistoryParam_1 = (org.recipnet.site.content.rncontrols.SampleHistoryParam) _jspx_tagPool_rn_sampleHistoryParam_useMostRecentHistoryId_name_nobody.get(org.recipnet.site.content.rncontrols.SampleHistoryParam.class);
    _jspx_th_rn_sampleHistoryParam_1.setPageContext(_jspx_page_context);
    _jspx_th_rn_sampleHistoryParam_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_0);
    _jspx_th_rn_sampleHistoryParam_1.setName("sampleHistoryId");
    _jspx_th_rn_sampleHistoryParam_1.setUseMostRecentHistoryId(true);
    int _jspx_eval_rn_sampleHistoryParam_1 = _jspx_th_rn_sampleHistoryParam_1.doStartTag();
    if (_jspx_th_rn_sampleHistoryParam_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_sampleHistoryParam_useMostRecentHistoryId_name_nobody.reuse(_jspx_th_rn_sampleHistoryParam_1);
    return false;
  }

  private boolean _jspx_meth_rn_sampleHistoryParam_2(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_a_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:sampleHistoryParam
    org.recipnet.site.content.rncontrols.SampleHistoryParam _jspx_th_rn_sampleHistoryParam_2 = (org.recipnet.site.content.rncontrols.SampleHistoryParam) _jspx_tagPool_rn_sampleHistoryParam_name_nobody.get(org.recipnet.site.content.rncontrols.SampleHistoryParam.class);
    _jspx_th_rn_sampleHistoryParam_2.setPageContext(_jspx_page_context);
    _jspx_th_rn_sampleHistoryParam_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_0);
    _jspx_th_rn_sampleHistoryParam_2.setName("targetSampleHistoryId");
    int _jspx_eval_rn_sampleHistoryParam_2 = _jspx_th_rn_sampleHistoryParam_2.doStartTag();
    if (_jspx_th_rn_sampleHistoryParam_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_sampleHistoryParam_name_nobody.reuse(_jspx_th_rn_sampleHistoryParam_2);
    return false;
  }

  private boolean _jspx_meth_rn_sampleChecker_1(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_parityChecker_3, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:sampleChecker
    org.recipnet.site.content.rncontrols.SampleChecker _jspx_th_rn_sampleChecker_1 = (org.recipnet.site.content.rncontrols.SampleChecker) _jspx_tagPool_rn_sampleChecker_invert_includeIfUnpublishedVersionOfPublicSample.get(org.recipnet.site.content.rncontrols.SampleChecker.class);
    _jspx_th_rn_sampleChecker_1.setPageContext(_jspx_page_context);
    _jspx_th_rn_sampleChecker_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_parityChecker_3);
    _jspx_th_rn_sampleChecker_1.setIncludeIfUnpublishedVersionOfPublicSample(true);
    _jspx_th_rn_sampleChecker_1.setInvert(true);
    int _jspx_eval_rn_sampleChecker_1 = _jspx_th_rn_sampleChecker_1.doStartTag();
    if (_jspx_eval_rn_sampleChecker_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_sampleChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_sampleChecker_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_sampleChecker_1.doInitBody();
      }
      do {
        out.write("\n              ");
        if (_jspx_meth_ctl_a_1(_jspx_th_rn_sampleChecker_1, _jspx_page_context))
          return true;
        out.write("\n            ");
        int evalDoAfterBody = _jspx_th_rn_sampleChecker_1.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_sampleChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_sampleChecker_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_sampleChecker_invert_includeIfUnpublishedVersionOfPublicSample.reuse(_jspx_th_rn_sampleChecker_1);
    return false;
  }

  private boolean _jspx_meth_ctl_a_1(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleChecker_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:a
    org.recipnet.common.controls.LinkHtmlElement _jspx_th_ctl_a_1 = (org.recipnet.common.controls.LinkHtmlElement) _jspx_tagPool_ctl_a_href.get(org.recipnet.common.controls.LinkHtmlElement.class);
    _jspx_th_ctl_a_1.setPageContext(_jspx_page_context);
    _jspx_th_ctl_a_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleChecker_1);
    _jspx_th_ctl_a_1.setHref("/lab/revert.jsp");
    int _jspx_eval_ctl_a_1 = _jspx_th_ctl_a_1.doStartTag();
    if (_jspx_eval_ctl_a_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_a_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_a_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_a_1.doInitBody();
      }
      do {
        out.write("\n                ");
        if (_jspx_meth_rn_sampleParam_2(_jspx_th_ctl_a_1, _jspx_page_context))
          return true;
        out.write("\n                ");
        if (_jspx_meth_rn_sampleHistoryParam_3(_jspx_th_ctl_a_1, _jspx_page_context))
          return true;
        out.write("\n                ");
        if (_jspx_meth_rn_sampleHistoryParam_4(_jspx_th_ctl_a_1, _jspx_page_context))
          return true;
        out.write("\n                Revert to this version\n              ");
        int evalDoAfterBody = _jspx_th_ctl_a_1.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_a_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_a_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_a_href.reuse(_jspx_th_ctl_a_1);
    return false;
  }

  private boolean _jspx_meth_rn_sampleParam_2(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_a_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:sampleParam
    org.recipnet.site.content.rncontrols.SampleParam _jspx_th_rn_sampleParam_2 = (org.recipnet.site.content.rncontrols.SampleParam) _jspx_tagPool_rn_sampleParam_name_nobody.get(org.recipnet.site.content.rncontrols.SampleParam.class);
    _jspx_th_rn_sampleParam_2.setPageContext(_jspx_page_context);
    _jspx_th_rn_sampleParam_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_1);
    _jspx_th_rn_sampleParam_2.setName("sampleId");
    int _jspx_eval_rn_sampleParam_2 = _jspx_th_rn_sampleParam_2.doStartTag();
    if (_jspx_th_rn_sampleParam_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_sampleParam_name_nobody.reuse(_jspx_th_rn_sampleParam_2);
    return false;
  }

  private boolean _jspx_meth_rn_sampleHistoryParam_3(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_a_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:sampleHistoryParam
    org.recipnet.site.content.rncontrols.SampleHistoryParam _jspx_th_rn_sampleHistoryParam_3 = (org.recipnet.site.content.rncontrols.SampleHistoryParam) _jspx_tagPool_rn_sampleHistoryParam_useMostRecentHistoryId_name_nobody.get(org.recipnet.site.content.rncontrols.SampleHistoryParam.class);
    _jspx_th_rn_sampleHistoryParam_3.setPageContext(_jspx_page_context);
    _jspx_th_rn_sampleHistoryParam_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_1);
    _jspx_th_rn_sampleHistoryParam_3.setName("sampleHistoryId");
    _jspx_th_rn_sampleHistoryParam_3.setUseMostRecentHistoryId(true);
    int _jspx_eval_rn_sampleHistoryParam_3 = _jspx_th_rn_sampleHistoryParam_3.doStartTag();
    if (_jspx_th_rn_sampleHistoryParam_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_sampleHistoryParam_useMostRecentHistoryId_name_nobody.reuse(_jspx_th_rn_sampleHistoryParam_3);
    return false;
  }

  private boolean _jspx_meth_rn_sampleHistoryParam_4(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_a_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:sampleHistoryParam
    org.recipnet.site.content.rncontrols.SampleHistoryParam _jspx_th_rn_sampleHistoryParam_4 = (org.recipnet.site.content.rncontrols.SampleHistoryParam) _jspx_tagPool_rn_sampleHistoryParam_name_nobody.get(org.recipnet.site.content.rncontrols.SampleHistoryParam.class);
    _jspx_th_rn_sampleHistoryParam_4.setPageContext(_jspx_page_context);
    _jspx_th_rn_sampleHistoryParam_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_1);
    _jspx_th_rn_sampleHistoryParam_4.setName("targetSampleHistoryId");
    int _jspx_eval_rn_sampleHistoryParam_4 = _jspx_th_rn_sampleHistoryParam_4.doStartTag();
    if (_jspx_th_rn_sampleHistoryParam_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_sampleHistoryParam_name_nobody.reuse(_jspx_th_rn_sampleHistoryParam_4);
    return false;
  }

  private boolean _jspx_meth_rn_showsampleLink_1(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_samplePage_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:showsampleLink
    org.recipnet.site.content.rncontrols.ShowsampleLink _jspx_th_rn_showsampleLink_1 = (org.recipnet.site.content.rncontrols.ShowsampleLink) _jspx_tagPool_rn_showsampleLink_sampleIsKnownToBeLocal_detailedPageUrl_basicPageUrl.get(org.recipnet.site.content.rncontrols.ShowsampleLink.class);
    _jspx_th_rn_showsampleLink_1.setPageContext(_jspx_page_context);
    _jspx_th_rn_showsampleLink_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_samplePage_0);
    _jspx_th_rn_showsampleLink_1.setSampleIsKnownToBeLocal(true);
    _jspx_th_rn_showsampleLink_1.setBasicPageUrl("/showsamplebasic.jsp");
    _jspx_th_rn_showsampleLink_1.setDetailedPageUrl("/showsampledetailed.jsp");
    int _jspx_eval_rn_showsampleLink_1 = _jspx_th_rn_showsampleLink_1.doStartTag();
    if (_jspx_eval_rn_showsampleLink_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_showsampleLink_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_showsampleLink_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_showsampleLink_1.doInitBody();
      }
      do {
        out.write("\n          ");
        if (_jspx_meth_rn_sampleParam_3(_jspx_th_rn_showsampleLink_1, _jspx_page_context))
          return true;
        out.write("\n          ");
        if (_jspx_meth_rn_sampleHistoryParam_5(_jspx_th_rn_showsampleLink_1, _jspx_page_context))
          return true;
        out.write("\n          View this sample...\n        ");
        int evalDoAfterBody = _jspx_th_rn_showsampleLink_1.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_showsampleLink_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_showsampleLink_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_showsampleLink_sampleIsKnownToBeLocal_detailedPageUrl_basicPageUrl.reuse(_jspx_th_rn_showsampleLink_1);
    return false;
  }

  private boolean _jspx_meth_rn_sampleParam_3(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_showsampleLink_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:sampleParam
    org.recipnet.site.content.rncontrols.SampleParam _jspx_th_rn_sampleParam_3 = (org.recipnet.site.content.rncontrols.SampleParam) _jspx_tagPool_rn_sampleParam_name_nobody.get(org.recipnet.site.content.rncontrols.SampleParam.class);
    _jspx_th_rn_sampleParam_3.setPageContext(_jspx_page_context);
    _jspx_th_rn_sampleParam_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_showsampleLink_1);
    _jspx_th_rn_sampleParam_3.setName("sampleId");
    int _jspx_eval_rn_sampleParam_3 = _jspx_th_rn_sampleParam_3.doStartTag();
    if (_jspx_th_rn_sampleParam_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_sampleParam_name_nobody.reuse(_jspx_th_rn_sampleParam_3);
    return false;
  }

  private boolean _jspx_meth_rn_sampleHistoryParam_5(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_showsampleLink_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:sampleHistoryParam
    org.recipnet.site.content.rncontrols.SampleHistoryParam _jspx_th_rn_sampleHistoryParam_5 = (org.recipnet.site.content.rncontrols.SampleHistoryParam) _jspx_tagPool_rn_sampleHistoryParam_name_nobody.get(org.recipnet.site.content.rncontrols.SampleHistoryParam.class);
    _jspx_th_rn_sampleHistoryParam_5.setPageContext(_jspx_page_context);
    _jspx_th_rn_sampleHistoryParam_5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_showsampleLink_1);
    _jspx_th_rn_sampleHistoryParam_5.setName("sampleHistoryId");
    int _jspx_eval_rn_sampleHistoryParam_5 = _jspx_th_rn_sampleHistoryParam_5.doStartTag();
    if (_jspx_th_rn_sampleHistoryParam_5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_sampleHistoryParam_name_nobody.reuse(_jspx_th_rn_sampleHistoryParam_5);
    return false;
  }

  private boolean _jspx_meth_rn_link_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_samplePage_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:link
    org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_0 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_href.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
    _jspx_th_rn_link_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_link_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_samplePage_0);
    _jspx_th_rn_link_0.setHref("/lab/sample.jsp");
    int _jspx_eval_rn_link_0 = _jspx_th_rn_link_0.doStartTag();
    if (_jspx_eval_rn_link_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_link_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_link_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_link_0.doInitBody();
      }
      do {
        out.write("Edit this sample...");
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
        out.write("\n    .navLinksRight { text-align: right; }\n    .twoColumnLeft   { text-align: right; padding: 0.01in; border-width: 0;}\n    .twoColumnRight  { text-align: left;  padding: 0.01in;  border-width: 0;}\n    .evenRow { background-color: #D6D6D6; }\n    .oddRow { background-color: #F0F0F0; }\n    .bodyCell { padding: 3px; }\n    .lastRow { background-color: #8888A0; color: #FFFFFF;\n        text-align: left;}\n    th.headerCell { background-color: #909090; color: #FFFFFF;\n        text-align: left; padding: 5px; }\n  ");
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
