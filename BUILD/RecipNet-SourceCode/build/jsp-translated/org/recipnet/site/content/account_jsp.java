package org.recipnet.site.content;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.recipnet.common.controls.HtmlPage;
import org.recipnet.site.content.rncontrols.PreferenceField;
import org.recipnet.site.content.rncontrols.ProviderField;
import org.recipnet.site.content.rncontrols.UserContext;
import org.recipnet.site.content.rncontrols.UserField;
import org.recipnet.site.content.rncontrols.UserPage;

public final class account_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static java.util.Vector _jspx_dependants;

  static {
    _jspx_dependants = new java.util.Vector(2);
    _jspx_dependants.add("/WEB-INF/controls.tld");
    _jspx_dependants.add("/WEB-INF/rncontrols.tld");
  }

  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_userPage_title_loginPageUrl_currentPageReinvocationUrlParamName_completionUrlParamName_cancellationUrlParamName_authorizationFailedReasonParamName;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_errorMessage_errorFilter;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_selfForm;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_userField_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_userField_fieldCode_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_a_href;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_currentPageLinkParam_name_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_preferenceField_preferenceType_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_userChecker_includeIfProviderUser;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_providerField_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_authorizationChecker_canSeeLabSummary;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_preferenceField_preferenceType_id_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_userActionButton_userFunction_label_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_styleBlock;

  public java.util.List getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _jspx_tagPool_rn_userPage_title_loginPageUrl_currentPageReinvocationUrlParamName_completionUrlParamName_cancellationUrlParamName_authorizationFailedReasonParamName = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_errorMessage_errorFilter = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_selfForm = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_userField_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_userField_fieldCode_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_a_href = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_currentPageLinkParam_name_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_preferenceField_preferenceType_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_userChecker_includeIfProviderUser = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_providerField_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_authorizationChecker_canSeeLabSummary = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_preferenceField_preferenceType_id_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_userActionButton_userFunction_label_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_styleBlock = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
  }

  public void _jspDestroy() {
    _jspx_tagPool_rn_userPage_title_loginPageUrl_currentPageReinvocationUrlParamName_completionUrlParamName_cancellationUrlParamName_authorizationFailedReasonParamName.release();
    _jspx_tagPool_ctl_errorMessage_errorFilter.release();
    _jspx_tagPool_ctl_selfForm.release();
    _jspx_tagPool_rn_userField_nobody.release();
    _jspx_tagPool_rn_userField_fieldCode_nobody.release();
    _jspx_tagPool_ctl_a_href.release();
    _jspx_tagPool_ctl_currentPageLinkParam_name_nobody.release();
    _jspx_tagPool_rn_preferenceField_preferenceType_nobody.release();
    _jspx_tagPool_rn_userChecker_includeIfProviderUser.release();
    _jspx_tagPool_rn_providerField_nobody.release();
    _jspx_tagPool_rn_authorizationChecker_canSeeLabSummary.release();
    _jspx_tagPool_rn_preferenceField_preferenceType_id_nobody.release();
    _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.release();
    _jspx_tagPool_rn_userActionButton_userFunction_label_nobody.release();
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

      out.write("\n\n\n\n\n\n\n\n\n");
      //  rn:userPage
      org.recipnet.site.content.rncontrols.UserPage _jspx_th_rn_userPage_0 = (org.recipnet.site.content.rncontrols.UserPage) _jspx_tagPool_rn_userPage_title_loginPageUrl_currentPageReinvocationUrlParamName_completionUrlParamName_cancellationUrlParamName_authorizationFailedReasonParamName.get(org.recipnet.site.content.rncontrols.UserPage.class);
      _jspx_th_rn_userPage_0.setPageContext(_jspx_page_context);
      _jspx_th_rn_userPage_0.setParent(null);
      _jspx_th_rn_userPage_0.setTitle("Manage account");
      _jspx_th_rn_userPage_0.setCompletionUrlParamName("origPage");
      _jspx_th_rn_userPage_0.setCancellationUrlParamName("origPage");
      _jspx_th_rn_userPage_0.setLoginPageUrl("/login.jsp");
      _jspx_th_rn_userPage_0.setCurrentPageReinvocationUrlParamName("origUrl");
      _jspx_th_rn_userPage_0.setAuthorizationFailedReasonParamName("authorizationFailedReason");
      int _jspx_eval_rn_userPage_0 = _jspx_th_rn_userPage_0.doStartTag();
      if (_jspx_eval_rn_userPage_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        org.recipnet.site.content.rncontrols.UserPage htmlPage = null;
        if (_jspx_eval_rn_userPage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
          out = _jspx_page_context.pushBody();
          _jspx_th_rn_userPage_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
          _jspx_th_rn_userPage_0.doInitBody();
        }
        htmlPage = (org.recipnet.site.content.rncontrols.UserPage) _jspx_page_context.findAttribute("htmlPage");
        do {
          out.write('\n');
          out.write(' ');
          out.write(' ');
          //  ctl:errorMessage
          org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_0 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
          _jspx_th_ctl_errorMessage_0.setPageContext(_jspx_page_context);
          _jspx_th_ctl_errorMessage_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_userPage_0);
          _jspx_th_ctl_errorMessage_0.setErrorFilter(HtmlPage.NESTED_TAG_REPORTED_VALIDATION_ERROR);
          int _jspx_eval_ctl_errorMessage_0 = _jspx_th_ctl_errorMessage_0.doStartTag();
          if (_jspx_eval_ctl_errorMessage_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_ctl_errorMessage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_ctl_errorMessage_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_ctl_errorMessage_0.doInitBody();
            }
            do {
              out.write("\n    <p class=\"errorMessage\">\n      Validation Error -- please check your entries and resubmit\n    </p>\n  ");
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
          out.write("\n  <br />\n  ");
          //  ctl:selfForm
          org.recipnet.common.controls.FormHtmlElement _jspx_th_ctl_selfForm_0 = (org.recipnet.common.controls.FormHtmlElement) _jspx_tagPool_ctl_selfForm.get(org.recipnet.common.controls.FormHtmlElement.class);
          _jspx_th_ctl_selfForm_0.setPageContext(_jspx_page_context);
          _jspx_th_ctl_selfForm_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_userPage_0);
          int _jspx_eval_ctl_selfForm_0 = _jspx_th_ctl_selfForm_0.doStartTag();
          if (_jspx_eval_ctl_selfForm_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_ctl_selfForm_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_ctl_selfForm_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_ctl_selfForm_0.doInitBody();
            }
            do {
              out.write("\n    <table cellspacing=\"0\">\n      <tr class=\"userInfoHeader\">\n        <td colspan=\"2\" class=\"threeColumnRight\">\n          <strong>User Information:</strong>\n        </td>\n      </tr>\n      <tr class=\"userInfoRow\">\n        <td class=\"threeColumnLeft\">name:</td>\n        <td class=\"threeColumnRight\">");
              if (_jspx_meth_rn_userField_0(_jspx_th_ctl_selfForm_0, _jspx_page_context))
                return;
              out.write("</td>\n      </tr>\n      <tr class=\"userInfoRow\">\n        <td class=\"threeColumnLeft\">username:</td>\n        <td class=\"threeColumnRight\">\n          ");
              //  rn:userField
              org.recipnet.site.content.rncontrols.UserField _jspx_th_rn_userField_1 = (org.recipnet.site.content.rncontrols.UserField) _jspx_tagPool_rn_userField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.UserField.class);
              _jspx_th_rn_userField_1.setPageContext(_jspx_page_context);
              _jspx_th_rn_userField_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_userField_1.setFieldCode(UserField.USER_NAME);
              int _jspx_eval_rn_userField_1 = _jspx_th_rn_userField_1.doStartTag();
              if (_jspx_th_rn_userField_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_userField_fieldCode_nobody.reuse(_jspx_th_rn_userField_1);
              out.write("\n        </td>\n      </tr>\n      <tr class=\"userInfoRow\">\n        <td class=\"threeColumnCenter\" colspan=\"2\">\n          ");
              if (_jspx_meth_ctl_a_0(_jspx_th_ctl_selfForm_0, _jspx_page_context))
                return;
              out.write("\n        </td>\n      </tr>\n    </table>\n\n    <br />\n    <table cellspacing=\"0\" width=\"75%\">\n      <tr class=\"headerRow\">\n        <td colspan=\"3\" class=\"threeColumnRight\">\n          <strong>Sample Viewing Preferences:</strong>\n        </tr>\n      </tr>\n      <tr class=\"oddRow\">\n        <td class=\"threeColumnLeft\">applet:</td>\n        <td class=\"threeColumnCenter\">\n          ");
              //  rn:preferenceField
              org.recipnet.site.content.rncontrols.PreferenceField _jspx_th_rn_preferenceField_0 = (org.recipnet.site.content.rncontrols.PreferenceField) _jspx_tagPool_rn_preferenceField_preferenceType_nobody.get(org.recipnet.site.content.rncontrols.PreferenceField.class);
              _jspx_th_rn_preferenceField_0.setPageContext(_jspx_page_context);
              _jspx_th_rn_preferenceField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_preferenceField_0.setPreferenceType(PreferenceField.PrefType.APPLET);
              int _jspx_eval_rn_preferenceField_0 = _jspx_th_rn_preferenceField_0.doStartTag();
              if (_jspx_th_rn_preferenceField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_preferenceField_preferenceType_nobody.reuse(_jspx_th_rn_preferenceField_0);
              out.write("\n        </td>\n        <td class=\"threeColumnRight\">\n          <span class=\"description\">\n            The preferred applet will be displayed for samples with CRT files.\n            &nbsp;&nbsp;This setting may also be changed on any page where JaMM\n            is visible by selecting a different applet..\n          </span>\n        </td>\n      </tr>\n      <tr class=\"evenRow\">\n        <td class=\"threeColumnLeft\"><nobr>sample view:</nobr></td>\n        <td class=\"threeColumnCenter\">\n          ");
              //  rn:preferenceField
              org.recipnet.site.content.rncontrols.PreferenceField _jspx_th_rn_preferenceField_1 = (org.recipnet.site.content.rncontrols.PreferenceField) _jspx_tagPool_rn_preferenceField_preferenceType_nobody.get(org.recipnet.site.content.rncontrols.PreferenceField.class);
              _jspx_th_rn_preferenceField_1.setPageContext(_jspx_page_context);
              _jspx_th_rn_preferenceField_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_preferenceField_1.setPreferenceType(PreferenceField.PrefType.SAMPLE_VIEW);
              int _jspx_eval_rn_preferenceField_1 = _jspx_th_rn_preferenceField_1.doStartTag();
              if (_jspx_th_rn_preferenceField_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_preferenceField_preferenceType_nobody.reuse(_jspx_th_rn_preferenceField_1);
              out.write("\n        </td>\n        <td class=\"threeColumnRight\">\n          <span class=\"description\">\n            This is the page that is used to display a sample that is the\n            single result of a search or quick search.&nbsp;&nbsp; This option\n            has no effect when you are not authorized to edit a sample, in\n            which case the \"Sample details\" page will be used.\n          </span>\n        </td>\n      </tr>\n      <tr class=\"oddRow\">\n        <td class=\"threeColumnLeft\"><nobr>sample details page view:</nobr></td>\n        <td class=\"threeColumnCenter\">\n          ");
              //  rn:preferenceField
              org.recipnet.site.content.rncontrols.PreferenceField _jspx_th_rn_preferenceField_2 = (org.recipnet.site.content.rncontrols.PreferenceField) _jspx_tagPool_rn_preferenceField_preferenceType_nobody.get(org.recipnet.site.content.rncontrols.PreferenceField.class);
              _jspx_th_rn_preferenceField_2.setPageContext(_jspx_page_context);
              _jspx_th_rn_preferenceField_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_preferenceField_2.setPreferenceType(PreferenceField.PrefType.SHOWSAMPLE_VIEW);
              int _jspx_eval_rn_preferenceField_2 = _jspx_th_rn_preferenceField_2.doStartTag();
              if (_jspx_th_rn_preferenceField_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_preferenceField_preferenceType_nobody.reuse(_jspx_th_rn_preferenceField_2);
              out.write("\n        </td>\n        <td class=\"threeColumnRight\">\n          <span class=\"description\">\n            The sample details page has multiple view modes.&nbsp;&nbsp; Basic\n            mode represents a simplified view of a sample ideal for those\n            with little or no chemistry background, while detailed mode\n            contains lots of specific crystallographic data.\n          </span>\n        </td>\n      </tr>\n      <tr class=\"evenRow\">\n        <td class=\"threeColumnLeft\">\n          <nobr>suppress file descriptions:</nobr>\n        </td>\n        <td class=\"threeColumnCenter\">\n          ");
              //  rn:preferenceField
              org.recipnet.site.content.rncontrols.PreferenceField _jspx_th_rn_preferenceField_3 = (org.recipnet.site.content.rncontrols.PreferenceField) _jspx_tagPool_rn_preferenceField_preferenceType_nobody.get(org.recipnet.site.content.rncontrols.PreferenceField.class);
              _jspx_th_rn_preferenceField_3.setPageContext(_jspx_page_context);
              _jspx_th_rn_preferenceField_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_preferenceField_3.setPreferenceType(
              PreferenceField.PrefType.SUPPRESS_FILE_DESCRIPTIONS);
              int _jspx_eval_rn_preferenceField_3 = _jspx_th_rn_preferenceField_3.doStartTag();
              if (_jspx_th_rn_preferenceField_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_preferenceField_preferenceType_nobody.reuse(_jspx_th_rn_preferenceField_3);
              out.write("\n        </td>\n        <td class=\"threeColumnRight\">\n          <span class=\"description\">\n            When checked, file descriptions will be suppressed on the\n            show sample page as well as the edit sample page.\n          </span>\n        </td>\n      </tr>\n      <tr class=\"oddRow\">\n        <td class=\"threeColumnLeft\">\n          <nobr>implicitly set preferences:</nobr>\n        </td>\n        <td class=\"threeColumnCenter\">\n          ");
              //  rn:preferenceField
              org.recipnet.site.content.rncontrols.PreferenceField _jspx_th_rn_preferenceField_4 = (org.recipnet.site.content.rncontrols.PreferenceField) _jspx_tagPool_rn_preferenceField_preferenceType_nobody.get(org.recipnet.site.content.rncontrols.PreferenceField.class);
              _jspx_th_rn_preferenceField_4.setPageContext(_jspx_page_context);
              _jspx_th_rn_preferenceField_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_preferenceField_4.setPreferenceType(
              PreferenceField.PrefType.ALLOW_IMPLICIT_PREF_CHANGES);
              int _jspx_eval_rn_preferenceField_4 = _jspx_th_rn_preferenceField_4.doStartTag();
              if (_jspx_th_rn_preferenceField_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_preferenceField_preferenceType_nobody.reuse(_jspx_th_rn_preferenceField_4);
              out.write("\n        </td>\n        <td class=\"threeColumnRight\">\n          <span class=\"description\">\n            When checked, the sample details page mode preference as well as\n            the preferred visualization applet will be updated by switching\n            between page modes or applet types.  In other words, the last\n            JaMM applet viewed or sample details page mode will be considered\n            to be the preferred one.\n          </span>\n        </td>\n      </tr>\n    </table>\n\n    <br />\n    <table cellspacing=\"0\" width=\"75%\">\n      <tr class=\"headerRow\">\n        <td colspan=\"3\" class=\"threeColumnRight\">\n          <strong>Search Parameter Preferences:</strong>\n        </tr>\n      </tr>\n      <tr class=\"oddRow\">\n        <td class=\"threeColumnLeft\">\n          <nobr>restrict searches to non-retracted samples:</nobr>\n        </td>\n        <td class=\"threeColumnCenter\">\n          ");
              //  rn:preferenceField
              org.recipnet.site.content.rncontrols.PreferenceField _jspx_th_rn_preferenceField_5 = (org.recipnet.site.content.rncontrols.PreferenceField) _jspx_tagPool_rn_preferenceField_preferenceType_nobody.get(org.recipnet.site.content.rncontrols.PreferenceField.class);
              _jspx_th_rn_preferenceField_5.setPageContext(_jspx_page_context);
              _jspx_th_rn_preferenceField_5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_preferenceField_5.setPreferenceType(
              PreferenceField.PrefType.SEARCH_NON_RETRACTED);
              int _jspx_eval_rn_preferenceField_5 = _jspx_th_rn_preferenceField_5.doStartTag();
              if (_jspx_th_rn_preferenceField_5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_preferenceField_preferenceType_nobody.reuse(_jspx_th_rn_preferenceField_5);
              out.write("\n        </td>\n        <td class=\"threeColumnRight\">\n          <span class=\"description\">\n            When this box is checked the search page option to restrict\n            searches to those samples which have not be retracted previously\n            will be selected by default.\n          </span>\n        </td>\n      </tr>\n      <tr class=\"evenRow\">\n        <td class=\"threeColumnLeft\">\n          <nobr>restrict searches to finished samples:</nobr>\n        </td>\n        <td class=\"threeColumnCenter\">\n          ");
              //  rn:preferenceField
              org.recipnet.site.content.rncontrols.PreferenceField _jspx_th_rn_preferenceField_6 = (org.recipnet.site.content.rncontrols.PreferenceField) _jspx_tagPool_rn_preferenceField_preferenceType_nobody.get(org.recipnet.site.content.rncontrols.PreferenceField.class);
              _jspx_th_rn_preferenceField_6.setPageContext(_jspx_page_context);
              _jspx_th_rn_preferenceField_6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_preferenceField_6.setPreferenceType(PreferenceField.PrefType.SEARCH_FINISHED);
              int _jspx_eval_rn_preferenceField_6 = _jspx_th_rn_preferenceField_6.doStartTag();
              if (_jspx_th_rn_preferenceField_6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_preferenceField_preferenceType_nobody.reuse(_jspx_th_rn_preferenceField_6);
              out.write("\n        </td>\n        <td class=\"threeColumnRight\">\n          <span class=\"description\">\n            When this box is checked, the search page option to restrict\n            searches to those samples for which processing has finished will be\n            selected by default.\n          </span>\n        </td>\n      </tr>\n      <tr class=\"oddRow\">\n        <td class=\"threeColumnLeft\">\n          <nobr>restrict searches to local samples:</nobr>\n        </td>\n        <td class=\"threeColumnCenter\">\n          ");
              //  rn:preferenceField
              org.recipnet.site.content.rncontrols.PreferenceField _jspx_th_rn_preferenceField_7 = (org.recipnet.site.content.rncontrols.PreferenceField) _jspx_tagPool_rn_preferenceField_preferenceType_nobody.get(org.recipnet.site.content.rncontrols.PreferenceField.class);
              _jspx_th_rn_preferenceField_7.setPageContext(_jspx_page_context);
              _jspx_th_rn_preferenceField_7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_preferenceField_7.setPreferenceType(PreferenceField.PrefType.SEARCH_LOCAL);
              int _jspx_eval_rn_preferenceField_7 = _jspx_th_rn_preferenceField_7.doStartTag();
              if (_jspx_th_rn_preferenceField_7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_preferenceField_preferenceType_nobody.reuse(_jspx_th_rn_preferenceField_7);
              out.write("\n        </td>\n        <td class=\"threeColumnRight\">\n          <span class=\"description\">\n            When this box is checked, the search page option to restrict\n            searches to samples with data at the local site will be selected by\n            default.\n          </span>\n        </td>\n      </tr>\n      ");
              //  rn:userChecker
              org.recipnet.site.content.rncontrols.UserChecker _jspx_th_rn_userChecker_0 = (org.recipnet.site.content.rncontrols.UserChecker) _jspx_tagPool_rn_userChecker_includeIfProviderUser.get(org.recipnet.site.content.rncontrols.UserChecker.class);
              _jspx_th_rn_userChecker_0.setPageContext(_jspx_page_context);
              _jspx_th_rn_userChecker_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_userChecker_0.setIncludeIfProviderUser(true);
              int _jspx_eval_rn_userChecker_0 = _jspx_th_rn_userChecker_0.doStartTag();
              if (_jspx_eval_rn_userChecker_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_rn_userChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_rn_userChecker_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_rn_userChecker_0.doInitBody();
                }
                do {
                  out.write("\n        <tr class=\"evenRow\">\n          <td class=\"threeColumnLeft\">\n            <nobr>restrict searches to samples from my provider:</nobr>\n            <br />\n            (");
                  if (_jspx_meth_rn_providerField_0(_jspx_th_rn_userChecker_0, _jspx_page_context))
                    return;
                  out.write(")\n          </td>\n          <td class=\"threeColumnCenter\">\n            ");
                  //  rn:preferenceField
                  org.recipnet.site.content.rncontrols.PreferenceField _jspx_th_rn_preferenceField_8 = (org.recipnet.site.content.rncontrols.PreferenceField) _jspx_tagPool_rn_preferenceField_preferenceType_nobody.get(org.recipnet.site.content.rncontrols.PreferenceField.class);
                  _jspx_th_rn_preferenceField_8.setPageContext(_jspx_page_context);
                  _jspx_th_rn_preferenceField_8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_userChecker_0);
                  _jspx_th_rn_preferenceField_8.setPreferenceType(
                PreferenceField.PrefType.SEARCH_MY_PROVIDER);
                  int _jspx_eval_rn_preferenceField_8 = _jspx_th_rn_preferenceField_8.doStartTag();
                  if (_jspx_th_rn_preferenceField_8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_preferenceField_preferenceType_nobody.reuse(_jspx_th_rn_preferenceField_8);
                  out.write("\n          </td>\n          <td class=\"threeColumnRight\">\n            <span class=\"description\">\n              When this box is checked, the search page option to restrict\n              searches to samples originating from my provider will be selected\n              by default.\n            </span>\n          </td>\n        </tr>\n      ");
                  int evalDoAfterBody = _jspx_th_rn_userChecker_0.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_rn_userChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_rn_userChecker_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_userChecker_includeIfProviderUser.reuse(_jspx_th_rn_userChecker_0);
              out.write("\n    </table>\n\n    ");
              //  rn:authorizationChecker
              org.recipnet.site.content.rncontrols.AuthorizationChecker _jspx_th_rn_authorizationChecker_0 = (org.recipnet.site.content.rncontrols.AuthorizationChecker) _jspx_tagPool_rn_authorizationChecker_canSeeLabSummary.get(org.recipnet.site.content.rncontrols.AuthorizationChecker.class);
              _jspx_th_rn_authorizationChecker_0.setPageContext(_jspx_page_context);
              _jspx_th_rn_authorizationChecker_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_authorizationChecker_0.setCanSeeLabSummary(true);
              int _jspx_eval_rn_authorizationChecker_0 = _jspx_th_rn_authorizationChecker_0.doStartTag();
              if (_jspx_eval_rn_authorizationChecker_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_rn_authorizationChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_rn_authorizationChecker_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_rn_authorizationChecker_0.doInitBody();
                }
                do {
                  out.write("\n      <br />\n      <table cellspacing=\"0\" width=\"75%\">\n        <tr class=\"headerRow\">\n          <td colspan=\"3\" class=\"threeColumnRight\">\n            <strong>Lab Summary Preferences:</strong>\n          </tr>\n        </tr>\n        <tr class=\"oddRow\">\n          <td class=\"threeColumnLeft\">Summary Days:</td>\n          <td class=\"threeColumnCenter\">\n            ");
                  //  rn:preferenceField
                  org.recipnet.site.content.rncontrols.PreferenceField summaryDays = null;
                  org.recipnet.site.content.rncontrols.PreferenceField _jspx_th_rn_preferenceField_9 = (org.recipnet.site.content.rncontrols.PreferenceField) _jspx_tagPool_rn_preferenceField_preferenceType_id_nobody.get(org.recipnet.site.content.rncontrols.PreferenceField.class);
                  _jspx_th_rn_preferenceField_9.setPageContext(_jspx_page_context);
                  _jspx_th_rn_preferenceField_9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_0);
                  _jspx_th_rn_preferenceField_9.setId("summaryDays");
                  _jspx_th_rn_preferenceField_9.setPreferenceType(PreferenceField.PrefType.SUMMARY_DAYS);
                  int _jspx_eval_rn_preferenceField_9 = _jspx_th_rn_preferenceField_9.doStartTag();
                  if (_jspx_th_rn_preferenceField_9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  summaryDays = (org.recipnet.site.content.rncontrols.PreferenceField) _jspx_page_context.findAttribute("summaryDays");
                  _jspx_tagPool_rn_preferenceField_preferenceType_id_nobody.reuse(_jspx_th_rn_preferenceField_9);
                  out.write("\n            ");
                  //  ctl:errorMessage
                  org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_1 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
                  _jspx_th_ctl_errorMessage_1.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_errorMessage_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_0);
                  _jspx_th_ctl_errorMessage_1.setErrorSupplier(summaryDays);
                  _jspx_th_ctl_errorMessage_1.setErrorFilter(PreferenceField.VALUE_IS_NOT_A_NUMBER);
                  int _jspx_eval_ctl_errorMessage_1 = _jspx_th_ctl_errorMessage_1.doStartTag();
                  if (_jspx_eval_ctl_errorMessage_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_errorMessage_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_errorMessage_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_errorMessage_1.doInitBody();
                    }
                    do {
                      out.write("\n              <br>\n              <span class=\"error\">\n                The value must be a number!\n              </span>\n            ");
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
                  out.write("\n            ");
                  //  ctl:errorMessage
                  org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_2 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
                  _jspx_th_ctl_errorMessage_2.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_errorMessage_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_0);
                  _jspx_th_ctl_errorMessage_2.setErrorSupplier(summaryDays);
                  _jspx_th_ctl_errorMessage_2.setErrorFilter(PreferenceField.VALUE_IS_TOO_HIGH);
                  int _jspx_eval_ctl_errorMessage_2 = _jspx_th_ctl_errorMessage_2.doStartTag();
                  if (_jspx_eval_ctl_errorMessage_2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_errorMessage_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_errorMessage_2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_errorMessage_2.doInitBody();
                    }
                    do {
                      out.write("\n              <br />\n              <span class=\"error\">\n                The value is too high!<br /> (value must be between 0 and 511)\n              </span>\n            ");
                      int evalDoAfterBody = _jspx_th_ctl_errorMessage_2.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_ctl_errorMessage_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_ctl_errorMessage_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.reuse(_jspx_th_ctl_errorMessage_2);
                  out.write("\n            ");
                  //  ctl:errorMessage
                  org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_3 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
                  _jspx_th_ctl_errorMessage_3.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_errorMessage_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_0);
                  _jspx_th_ctl_errorMessage_3.setErrorSupplier(summaryDays);
                  _jspx_th_ctl_errorMessage_3.setErrorFilter(PreferenceField.VALUE_IS_TOO_LOW);
                  int _jspx_eval_ctl_errorMessage_3 = _jspx_th_ctl_errorMessage_3.doStartTag();
                  if (_jspx_eval_ctl_errorMessage_3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_errorMessage_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_errorMessage_3.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_errorMessage_3.doInitBody();
                    }
                    do {
                      out.write("\n              <br />\n              <span class=\"error\">\n                The value is too low!<br /> (value must be between 0 and 511)\n              </span>\n            ");
                      int evalDoAfterBody = _jspx_th_ctl_errorMessage_3.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_ctl_errorMessage_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_ctl_errorMessage_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.reuse(_jspx_th_ctl_errorMessage_3);
                  out.write("\n          </td>\n          <td class=\"threeColumnRight\">\n            <span class=\"description\">\n              The Lab Summary page will display samples that have last been\n              modified no more than this number of days ago.\n            </span>\n          </td>\n        </tr>\n        <tr class=\"evenRow\">\n          <td class=\"threeColumnLeft\">Summary Samples:</td>\n          <td class=\"threeColumnRight\">\n            ");
                  //  rn:preferenceField
                  org.recipnet.site.content.rncontrols.PreferenceField summarySamples = null;
                  org.recipnet.site.content.rncontrols.PreferenceField _jspx_th_rn_preferenceField_10 = (org.recipnet.site.content.rncontrols.PreferenceField) _jspx_tagPool_rn_preferenceField_preferenceType_id_nobody.get(org.recipnet.site.content.rncontrols.PreferenceField.class);
                  _jspx_th_rn_preferenceField_10.setPageContext(_jspx_page_context);
                  _jspx_th_rn_preferenceField_10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_0);
                  _jspx_th_rn_preferenceField_10.setId("summarySamples");
                  _jspx_th_rn_preferenceField_10.setPreferenceType(
                PreferenceField.PrefType.SUMMARY_SAMPLES);
                  int _jspx_eval_rn_preferenceField_10 = _jspx_th_rn_preferenceField_10.doStartTag();
                  if (_jspx_th_rn_preferenceField_10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  summarySamples = (org.recipnet.site.content.rncontrols.PreferenceField) _jspx_page_context.findAttribute("summarySamples");
                  _jspx_tagPool_rn_preferenceField_preferenceType_id_nobody.reuse(_jspx_th_rn_preferenceField_10);
                  out.write("\n            ");
                  //  ctl:errorMessage
                  org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_4 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
                  _jspx_th_ctl_errorMessage_4.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_errorMessage_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_0);
                  _jspx_th_ctl_errorMessage_4.setErrorSupplier(summarySamples);
                  _jspx_th_ctl_errorMessage_4.setErrorFilter(PreferenceField.VALUE_IS_NOT_A_NUMBER);
                  int _jspx_eval_ctl_errorMessage_4 = _jspx_th_ctl_errorMessage_4.doStartTag();
                  if (_jspx_eval_ctl_errorMessage_4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_errorMessage_4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_errorMessage_4.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_errorMessage_4.doInitBody();
                    }
                    do {
                      out.write("\n              <br>\n              <span class=\"error\">\n                The value must be a number!\n              </span>\n            ");
                      int evalDoAfterBody = _jspx_th_ctl_errorMessage_4.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_ctl_errorMessage_4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_ctl_errorMessage_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.reuse(_jspx_th_ctl_errorMessage_4);
                  out.write("\n            ");
                  //  ctl:errorMessage
                  org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_5 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
                  _jspx_th_ctl_errorMessage_5.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_errorMessage_5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_0);
                  _jspx_th_ctl_errorMessage_5.setErrorSupplier(summarySamples);
                  _jspx_th_ctl_errorMessage_5.setErrorFilter(PreferenceField.VALUE_IS_TOO_HIGH);
                  int _jspx_eval_ctl_errorMessage_5 = _jspx_th_ctl_errorMessage_5.doStartTag();
                  if (_jspx_eval_ctl_errorMessage_5 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_errorMessage_5 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_errorMessage_5.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_errorMessage_5.doInitBody();
                    }
                    do {
                      out.write("\n              <br />\n              <span class=\"error\">\n                The value is too high!<br /> (value must be between 1 and 255)\n              </span>\n            ");
                      int evalDoAfterBody = _jspx_th_ctl_errorMessage_5.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_ctl_errorMessage_5 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_ctl_errorMessage_5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.reuse(_jspx_th_ctl_errorMessage_5);
                  out.write("\n            ");
                  //  ctl:errorMessage
                  org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_6 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
                  _jspx_th_ctl_errorMessage_6.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_errorMessage_6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_0);
                  _jspx_th_ctl_errorMessage_6.setErrorSupplier(summarySamples);
                  _jspx_th_ctl_errorMessage_6.setErrorFilter(PreferenceField.VALUE_IS_TOO_LOW);
                  int _jspx_eval_ctl_errorMessage_6 = _jspx_th_ctl_errorMessage_6.doStartTag();
                  if (_jspx_eval_ctl_errorMessage_6 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_errorMessage_6 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_errorMessage_6.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_errorMessage_6.doInitBody();
                    }
                    do {
                      out.write("\n              <br />\n              <span class=\"error\">\n                The value is too low!<br /> (value must be between 1 and 255)\n              </span>\n            ");
                      int evalDoAfterBody = _jspx_th_ctl_errorMessage_6.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_ctl_errorMessage_6 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_ctl_errorMessage_6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.reuse(_jspx_th_ctl_errorMessage_6);
                  out.write("\n          </td>\n          <td class=\"threeColumnRight\">\n            <span class=\"description\">\n              The Lab Summary page will display no more than this number of\n              samples for each finished sample status.\n            </span>\n          </td>\n        </tr>\n      </table>\n    ");
                  int evalDoAfterBody = _jspx_th_rn_authorizationChecker_0.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_rn_authorizationChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_rn_authorizationChecker_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_authorizationChecker_canSeeLabSummary.reuse(_jspx_th_rn_authorizationChecker_0);
              out.write("\n\n    <br />\n    <table cellspacing=\"0\" width=\"75%\">\n      <tr class=\"headerRow\">\n        <td colspan=\"3\" class=\"threeColumnRight\">\n          <strong>Action Boxes on the Edit Sample page:</strong>\n          <br />\n          <i>(only applicable when you are authorized to edit a sample)</i>\n        </tr>\n      </tr>\n      <tr class=\"oddRow\">\n        <td class=\"threeColumnLeft\"><nobr>suppress blank fields:</nobr></td>\n        <td class=\"threeColumnCenter\">\n          ");
              //  rn:preferenceField
              org.recipnet.site.content.rncontrols.PreferenceField _jspx_th_rn_preferenceField_11 = (org.recipnet.site.content.rncontrols.PreferenceField) _jspx_tagPool_rn_preferenceField_preferenceType_nobody.get(org.recipnet.site.content.rncontrols.PreferenceField.class);
              _jspx_th_rn_preferenceField_11.setPageContext(_jspx_page_context);
              _jspx_th_rn_preferenceField_11.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_preferenceField_11.setPreferenceType(
              PreferenceField.PrefType.SUPPRESS_BLANK_FIELD);
              int _jspx_eval_rn_preferenceField_11 = _jspx_th_rn_preferenceField_11.doStartTag();
              if (_jspx_th_rn_preferenceField_11.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_preferenceField_preferenceType_nobody.reuse(_jspx_th_rn_preferenceField_11);
              out.write("\n        </td>\n          <td class=\"threeColumnRight\">\n            <span class=\"description\">\n              When checked, data fields for which no data has been entered will\n              not be visible.\n            </span>\n          </td>\n      </tr>\n      <tr class=\"evenRow\">\n        <td class=\"threeColumnLeft\"><nobr>suppress comments:</nobr></td>\n        <td class=\"threeColumnCenter\">\n          ");
              //  rn:preferenceField
              org.recipnet.site.content.rncontrols.PreferenceField _jspx_th_rn_preferenceField_12 = (org.recipnet.site.content.rncontrols.PreferenceField) _jspx_tagPool_rn_preferenceField_preferenceType_nobody.get(org.recipnet.site.content.rncontrols.PreferenceField.class);
              _jspx_th_rn_preferenceField_12.setPageContext(_jspx_page_context);
              _jspx_th_rn_preferenceField_12.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_preferenceField_12.setPreferenceType(
              PreferenceField.PrefType.SUPPRESS_COMMENTS);
              int _jspx_eval_rn_preferenceField_12 = _jspx_th_rn_preferenceField_12.doStartTag();
              if (_jspx_th_rn_preferenceField_12.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_preferenceField_preferenceType_nobody.reuse(_jspx_th_rn_preferenceField_12);
              out.write("\n        </td>\n          <td class=\"threeColumnRight\">\n            <span class=\"description\">\n              When checked, comments will not be included at the bottom of each\n              action box.\n            </span>\n          </td>\n      </tr>\n      <tr class=\"oddRow\">\n        <td class=\"threeColumnLeft\">\n          <nobr>suppress empty file boxes:</nobr>\n        </td>\n        <td class=\"threeColumnCenter\">\n          ");
              //  rn:preferenceField
              org.recipnet.site.content.rncontrols.PreferenceField _jspx_th_rn_preferenceField_13 = (org.recipnet.site.content.rncontrols.PreferenceField) _jspx_tagPool_rn_preferenceField_preferenceType_nobody.get(org.recipnet.site.content.rncontrols.PreferenceField.class);
              _jspx_th_rn_preferenceField_13.setPageContext(_jspx_page_context);
              _jspx_th_rn_preferenceField_13.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_preferenceField_13.setPreferenceType(
              PreferenceField.PrefType.SUPPRESS_EMPTY_FILE_ACTIONS);
              int _jspx_eval_rn_preferenceField_13 = _jspx_th_rn_preferenceField_13.doStartTag();
              if (_jspx_th_rn_preferenceField_13.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_preferenceField_preferenceType_nobody.reuse(_jspx_th_rn_preferenceField_13);
              out.write("\n        </td>\n        <td class=\"threeColumnRight\">\n          <span class=\"description\">\n            When checked, empty file action boxes will not be displayed.\n          </span>\n        </td>\n      </tr>\n      <tr class=\"evenRow\">\n        <td class=\"threeColumnLeft\">\n          <nobr>suppress empty correction boxes:</nobr>\n        </td>\n        <td class=\"threeColumnCenter\">\n          ");
              //  rn:preferenceField
              org.recipnet.site.content.rncontrols.PreferenceField _jspx_th_rn_preferenceField_14 = (org.recipnet.site.content.rncontrols.PreferenceField) _jspx_tagPool_rn_preferenceField_preferenceType_nobody.get(org.recipnet.site.content.rncontrols.PreferenceField.class);
              _jspx_th_rn_preferenceField_14.setPageContext(_jspx_page_context);
              _jspx_th_rn_preferenceField_14.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_preferenceField_14.setPreferenceType(
              PreferenceField.PrefType.SUPPRESS_EMPTY_CORRECTION_ACTIONS);
              int _jspx_eval_rn_preferenceField_14 = _jspx_th_rn_preferenceField_14.doStartTag();
              if (_jspx_th_rn_preferenceField_14.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_preferenceField_preferenceType_nobody.reuse(_jspx_th_rn_preferenceField_14);
              out.write("\n        </td>\n        <td class=\"threeColumnRight\">\n          <span class=\"description\">\n            When checked, boxes for correction actions where no new data was\n            added will not be displayed.\n          </span>\n        </td>\n      </tr>\n      <tr class=\"oddRow\">\n        <td class=\"threeColumnLeft\">\n          <nobr>suppress other empty boxes:</nobr>\n        </td>\n        <td class=\"threeColumnCenter\">\n          ");
              //  rn:preferenceField
              org.recipnet.site.content.rncontrols.PreferenceField _jspx_th_rn_preferenceField_15 = (org.recipnet.site.content.rncontrols.PreferenceField) _jspx_tagPool_rn_preferenceField_preferenceType_nobody.get(org.recipnet.site.content.rncontrols.PreferenceField.class);
              _jspx_th_rn_preferenceField_15.setPageContext(_jspx_page_context);
              _jspx_th_rn_preferenceField_15.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_preferenceField_15.setPreferenceType(
              PreferenceField.PrefType.SUPPRESS_EMPTY_OTHER_ACTIONS);
              int _jspx_eval_rn_preferenceField_15 = _jspx_th_rn_preferenceField_15.doStartTag();
              if (_jspx_th_rn_preferenceField_15.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_preferenceField_preferenceType_nobody.reuse(_jspx_th_rn_preferenceField_15);
              out.write("\n        </td>\n        <td class=\"threeColumnRight\">\n          <span class=\"description\">\n            When checked, boxes for actions where no new data was added will\n            not be displayed (excludes file actions and correction actions).\n          </span>\n        </td>\n      </tr>\n      <tr class=\"evenRow\">\n        <td class=\"threeColumnLeft\">\n          <nobr>suppress skipped action boxes:</nobr>\n        </td>\n        <td class=\"threeColumnCenter\">\n          ");
              //  rn:preferenceField
              org.recipnet.site.content.rncontrols.PreferenceField _jspx_th_rn_preferenceField_16 = (org.recipnet.site.content.rncontrols.PreferenceField) _jspx_tagPool_rn_preferenceField_preferenceType_nobody.get(org.recipnet.site.content.rncontrols.PreferenceField.class);
              _jspx_th_rn_preferenceField_16.setPageContext(_jspx_page_context);
              _jspx_th_rn_preferenceField_16.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_preferenceField_16.setPreferenceType(
              PreferenceField.PrefType.SUPPRESS_SKIPPED_ACTIONS);
              int _jspx_eval_rn_preferenceField_16 = _jspx_th_rn_preferenceField_16.doStartTag();
              if (_jspx_th_rn_preferenceField_16.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_preferenceField_preferenceType_nobody.reuse(_jspx_th_rn_preferenceField_16);
              out.write("\n        </td>\n        <td class=\"threeColumnRight\">\n          <span class=\"description\">\n            When checked, boxes for actions that were undone by a reversion\n            will not be displayed.\n          </span>\n        </td>\n      </tr>\n      <tr class=\"oddRow\">\n        <td class=\"threeColumnLeft\">\n          <nobr>suppress blank file lists:</nobr>\n        </td>\n        <td class=\"threeColumnCenter\">\n          ");
              //  rn:preferenceField
              org.recipnet.site.content.rncontrols.PreferenceField _jspx_th_rn_preferenceField_17 = (org.recipnet.site.content.rncontrols.PreferenceField) _jspx_tagPool_rn_preferenceField_preferenceType_nobody.get(org.recipnet.site.content.rncontrols.PreferenceField.class);
              _jspx_th_rn_preferenceField_17.setPageContext(_jspx_page_context);
              _jspx_th_rn_preferenceField_17.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_preferenceField_17.setPreferenceType(
              PreferenceField.PrefType.SUPPRESS_BLANK_FILE_LISTINGS);
              int _jspx_eval_rn_preferenceField_17 = _jspx_th_rn_preferenceField_17.doStartTag();
              if (_jspx_th_rn_preferenceField_17.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_preferenceField_preferenceType_nobody.reuse(_jspx_th_rn_preferenceField_17);
              out.write("\n        </td>\n        <td class=\"threeColumnRight\">\n          <span class=\"description\">\n            When checked, the lists of files added, removed or modified for\n            each action box will be suppressed when they contains no files.\n          </span>\n        </td>\n      </tr>\n      <tr class=\"evenRow\">\n        <td class=\"threeColumnLeft\">\n          <nobr>suppress suppression options:</nobr>\n        </td>\n        <td class=\"threeColumnCenter\">\n          ");
              //  rn:preferenceField
              org.recipnet.site.content.rncontrols.PreferenceField _jspx_th_rn_preferenceField_18 = (org.recipnet.site.content.rncontrols.PreferenceField) _jspx_tagPool_rn_preferenceField_preferenceType_nobody.get(org.recipnet.site.content.rncontrols.PreferenceField.class);
              _jspx_th_rn_preferenceField_18.setPageContext(_jspx_page_context);
              _jspx_th_rn_preferenceField_18.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_preferenceField_18.setPreferenceType(
              PreferenceField.PrefType.SUPPRESS_SUPPRESSION);
              int _jspx_eval_rn_preferenceField_18 = _jspx_th_rn_preferenceField_18.doStartTag();
              if (_jspx_th_rn_preferenceField_18.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_preferenceField_preferenceType_nobody.reuse(_jspx_th_rn_preferenceField_18);
              out.write("\n        </td>\n        <td class=\"threeColumnRight\">\n          <span class=\"description\">\n            When checked, the edit sample page will not expose these settings\n            for modification.  &nbsp;&nbsp;Instead, you will have to return to\n            this page to change the settings.\n          </span>\n        </td>\n      </tr>\n    </table>\n\n    <br />\n    <table cellspacing=\"0\" width=\"75%\">\n      <tr class=\"headerRow\">\n        <td colspan=\"3\" class=\"threeColumnRight\">\n          <strong>File Upload Preferences:</strong>\n          <br />\n          <i>\n            (Only applicable when you are authorized to upload files for a\n            sample)\n          </i>\n        </tr>\n      </tr>\n      <tr class=\"oddRow\">\n        <td class=\"threeColumnLeft\">\n          <nobr>default file overwrite option:</nobr>\n        </td>\n        <td class=\"threeColumnCenter\">\n          ");
              //  rn:preferenceField
              org.recipnet.site.content.rncontrols.PreferenceField _jspx_th_rn_preferenceField_19 = (org.recipnet.site.content.rncontrols.PreferenceField) _jspx_tagPool_rn_preferenceField_preferenceType_nobody.get(org.recipnet.site.content.rncontrols.PreferenceField.class);
              _jspx_th_rn_preferenceField_19.setPageContext(_jspx_page_context);
              _jspx_th_rn_preferenceField_19.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_preferenceField_19.setPreferenceType(
                  PreferenceField.PrefType.DEFAULT_FILE_OVERWRITE);
              int _jspx_eval_rn_preferenceField_19 = _jspx_th_rn_preferenceField_19.doStartTag();
              if (_jspx_th_rn_preferenceField_19.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_preferenceField_preferenceType_nobody.reuse(_jspx_th_rn_preferenceField_19);
              out.write("\n        </td>\n        <td class=\"threeColumnRight\">\n          <span class=\"description\">\n            When this box is checked, the file upload page will default to\n            allowing existing files in the repository to be overwritten. \n            &nbsp;&nbsp;Note: sample data files are versioned, so such\n            overwriting can be undone.\n          </span>\n        </td>\n      </tr>\n      <tr class=\"evenRow\">\n        <td class=\"threeColumnLeft\">\n          <nobr>default file upload mechanism:</nobr>\n        </td>\n        <td class=\"threeColumnCenter\">\n          ");
              //  rn:preferenceField
              org.recipnet.site.content.rncontrols.PreferenceField _jspx_th_rn_preferenceField_20 = (org.recipnet.site.content.rncontrols.PreferenceField) _jspx_tagPool_rn_preferenceField_preferenceType_nobody.get(org.recipnet.site.content.rncontrols.PreferenceField.class);
              _jspx_th_rn_preferenceField_20.setPageContext(_jspx_page_context);
              _jspx_th_rn_preferenceField_20.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_preferenceField_20.setPreferenceType(
                  PreferenceField.PrefType.FILE_UPLOAD_MECHANISM);
              int _jspx_eval_rn_preferenceField_20 = _jspx_th_rn_preferenceField_20.doStartTag();
              if (_jspx_th_rn_preferenceField_20.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_preferenceField_preferenceType_nobody.reuse(_jspx_th_rn_preferenceField_20);
              out.write("\n        </td>\n        <td class=\"threeColumnRight\">\n          <span class=\"description\">\n            Indicates the preferred mechanism for uploading files.&nbsp;&nbsp;\n            \"Form-based\" indicates an HTML form that allows the user to select\n            files to be uploaded and optionally include comments and file\n            descriptions.&nbsp;&nbsp; \"Drag-and-drop\" indicates a java applet\n            onto which multiple files may be be dragged and uploaded.\n          </span>\n        </td>\n      </tr>\n    </table>\n\n    <br />\n    <table cellspacing=\"0\" width=\"75%\">\n      <tr class=\"headerRow\">\n        <td colspan=\"3\" class=\"threeColumnRight\">\n          <strong>Display warnings about:</strong>\n        </tr>\n      </tr>\n      <tr class=\"oddRow\">\n        <td class=\"threeColumnLeft\">\n          <nobr>non-standard space group symbols:</nobr>\n        </td>\n        <td class=\"threeColumnCenter\">\n          ");
              //  rn:preferenceField
              org.recipnet.site.content.rncontrols.PreferenceField _jspx_th_rn_preferenceField_21 = (org.recipnet.site.content.rncontrols.PreferenceField) _jspx_tagPool_rn_preferenceField_preferenceType_nobody.get(org.recipnet.site.content.rncontrols.PreferenceField.class);
              _jspx_th_rn_preferenceField_21.setPageContext(_jspx_page_context);
              _jspx_th_rn_preferenceField_21.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_preferenceField_21.setPreferenceType(
                  PreferenceField.PrefType.VALIDATE_SPACE_GROUP);
              int _jspx_eval_rn_preferenceField_21 = _jspx_th_rn_preferenceField_21.doStartTag();
              if (_jspx_th_rn_preferenceField_21.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_preferenceField_preferenceType_nobody.reuse(_jspx_th_rn_preferenceField_21);
              out.write("\n        </td>\n        <td class=\"threeColumnRight\">\n          <span class=\"description\">\n            When this box is checked, space group symbols will be parsed and\n            validated when entered.  Unrecognized space group symbols will\n            trigger a warning message and must be resolved.  This option is\n            recommended because only standard, parsible space group symbols\n            will be considered in searches.\n          </span>\n        </td>\n      </tr>\n    </table>\n\n    <br />\n    <table width=\"75%\">\n      <rn>\n        <td align=\"center\">\n          ");
              //  rn:userActionButton
              org.recipnet.site.content.rncontrols.UserActionButton _jspx_th_rn_userActionButton_0 = (org.recipnet.site.content.rncontrols.UserActionButton) _jspx_tagPool_rn_userActionButton_userFunction_label_nobody.get(org.recipnet.site.content.rncontrols.UserActionButton.class);
              _jspx_th_rn_userActionButton_0.setPageContext(_jspx_page_context);
              _jspx_th_rn_userActionButton_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_userActionButton_0.setLabel("Save");
              _jspx_th_rn_userActionButton_0.setUserFunction(UserPage.UserFunction.CHANGE_PREFERENCES);
              int _jspx_eval_rn_userActionButton_0 = _jspx_th_rn_userActionButton_0.doStartTag();
              if (_jspx_th_rn_userActionButton_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_userActionButton_userFunction_label_nobody.reuse(_jspx_th_rn_userActionButton_0);
              out.write("\n          ");
              //  rn:userActionButton
              org.recipnet.site.content.rncontrols.UserActionButton _jspx_th_rn_userActionButton_1 = (org.recipnet.site.content.rncontrols.UserActionButton) _jspx_tagPool_rn_userActionButton_userFunction_label_nobody.get(org.recipnet.site.content.rncontrols.UserActionButton.class);
              _jspx_th_rn_userActionButton_1.setPageContext(_jspx_page_context);
              _jspx_th_rn_userActionButton_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_userActionButton_1.setLabel("Cancel");
              _jspx_th_rn_userActionButton_1.setUserFunction(UserPage.UserFunction.CANCEL);
              int _jspx_eval_rn_userActionButton_1 = _jspx_th_rn_userActionButton_1.doStartTag();
              if (_jspx_th_rn_userActionButton_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_userActionButton_userFunction_label_nobody.reuse(_jspx_th_rn_userActionButton_1);
              out.write("\n        </td>\n      </rn>\n    </table>\n  ");
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
          if (_jspx_meth_ctl_styleBlock_0(_jspx_th_rn_userPage_0, _jspx_page_context))
            return;
          out.write('\n');
          int evalDoAfterBody = _jspx_th_rn_userPage_0.doAfterBody();
          htmlPage = (org.recipnet.site.content.rncontrols.UserPage) _jspx_page_context.findAttribute("htmlPage");
          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
            break;
        } while (true);
        if (_jspx_eval_rn_userPage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
          out = _jspx_page_context.popBody();
      }
      if (_jspx_th_rn_userPage_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
        return;
      _jspx_tagPool_rn_userPage_title_loginPageUrl_currentPageReinvocationUrlParamName_completionUrlParamName_cancellationUrlParamName_authorizationFailedReasonParamName.reuse(_jspx_th_rn_userPage_0);
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

  private boolean _jspx_meth_rn_userField_0(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_selfForm_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:userField
    org.recipnet.site.content.rncontrols.UserField _jspx_th_rn_userField_0 = (org.recipnet.site.content.rncontrols.UserField) _jspx_tagPool_rn_userField_nobody.get(org.recipnet.site.content.rncontrols.UserField.class);
    _jspx_th_rn_userField_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_userField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
    int _jspx_eval_rn_userField_0 = _jspx_th_rn_userField_0.doStartTag();
    if (_jspx_th_rn_userField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_userField_nobody.reuse(_jspx_th_rn_userField_0);
    return false;
  }

  private boolean _jspx_meth_ctl_a_0(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_selfForm_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:a
    org.recipnet.common.controls.LinkHtmlElement _jspx_th_ctl_a_0 = (org.recipnet.common.controls.LinkHtmlElement) _jspx_tagPool_ctl_a_href.get(org.recipnet.common.controls.LinkHtmlElement.class);
    _jspx_th_ctl_a_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_a_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
    _jspx_th_ctl_a_0.setHref("/password.jsp");
    int _jspx_eval_ctl_a_0 = _jspx_th_ctl_a_0.doStartTag();
    if (_jspx_eval_ctl_a_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_a_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_a_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_a_0.doInitBody();
      }
      do {
        out.write("\n            ");
        if (_jspx_meth_ctl_currentPageLinkParam_0(_jspx_th_ctl_a_0, _jspx_page_context))
          return true;
        out.write("\n            change your password...\n          ");
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

  private boolean _jspx_meth_ctl_currentPageLinkParam_0(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_a_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:currentPageLinkParam
    org.recipnet.common.controls.CurrentPageLinkParam _jspx_th_ctl_currentPageLinkParam_0 = (org.recipnet.common.controls.CurrentPageLinkParam) _jspx_tagPool_ctl_currentPageLinkParam_name_nobody.get(org.recipnet.common.controls.CurrentPageLinkParam.class);
    _jspx_th_ctl_currentPageLinkParam_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_currentPageLinkParam_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_0);
    _jspx_th_ctl_currentPageLinkParam_0.setName("editUserPage");
    int _jspx_eval_ctl_currentPageLinkParam_0 = _jspx_th_ctl_currentPageLinkParam_0.doStartTag();
    if (_jspx_th_ctl_currentPageLinkParam_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_currentPageLinkParam_name_nobody.reuse(_jspx_th_ctl_currentPageLinkParam_0);
    return false;
  }

  private boolean _jspx_meth_rn_providerField_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_userChecker_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:providerField
    org.recipnet.site.content.rncontrols.ProviderField _jspx_th_rn_providerField_0 = (org.recipnet.site.content.rncontrols.ProviderField) _jspx_tagPool_rn_providerField_nobody.get(org.recipnet.site.content.rncontrols.ProviderField.class);
    _jspx_th_rn_providerField_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_providerField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_userChecker_0);
    int _jspx_eval_rn_providerField_0 = _jspx_th_rn_providerField_0.doStartTag();
    if (_jspx_th_rn_providerField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_providerField_nobody.reuse(_jspx_th_rn_providerField_0);
    return false;
  }

  private boolean _jspx_meth_ctl_styleBlock_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_userPage_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:styleBlock
    org.recipnet.common.controls.HtmlPageStyleBlock _jspx_th_ctl_styleBlock_0 = (org.recipnet.common.controls.HtmlPageStyleBlock) _jspx_tagPool_ctl_styleBlock.get(org.recipnet.common.controls.HtmlPageStyleBlock.class);
    _jspx_th_ctl_styleBlock_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_styleBlock_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_userPage_0);
    int _jspx_eval_ctl_styleBlock_0 = _jspx_th_ctl_styleBlock_0.doStartTag();
    if (_jspx_eval_ctl_styleBlock_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_styleBlock_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_styleBlock_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_styleBlock_0.doInitBody();
      }
      do {
        out.write("\n    .description { font-size: x-small; color: #505060; }\n    .error { font-size: x-small; color: #FF0000; }\n    .errorMessage  { color: red; font-weight: bold;}\n    .headerRow {background-color: #909090; color: #FFFFFF;\n            text-align: left; } \n    .oddRow { background-color: #D6D6D6; }\n    .evenRow { background-color: #F0F0F0; }\n    .threeColumnLeft { text-align: right; vertical-align: top; padding: 4px; }\n    .threeColumnCenter { text-align: left; vertical-align: top; padding: 4px; }\n    .threeColumnRight { text-align: left; vertical-align: top; padding: 4px; }\n    .userInfoHeader { background-color: #8888A0; color: #FFFFFF;\n            text-align: left; }\n    .userInfoRow { background-color: #DCDCF6; }\n  ");
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
