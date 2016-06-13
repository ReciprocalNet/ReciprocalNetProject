package org.recipnet.site.content;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.recipnet.site.content.rncontrols.FileField;
import org.recipnet.site.content.rncontrols.FileIterator;
import org.recipnet.site.content.rncontrols.JammModelElement;
import org.recipnet.site.content.rncontrols.LabField;
import org.recipnet.site.content.rncontrols.ShowSamplePage;
import org.recipnet.site.shared.bl.SampleTextBL;
import org.recipnet.site.shared.bl.UserBL;
import org.recipnet.site.shared.db.SampleDataInfo;
import org.recipnet.site.shared.db.SampleInfo;

public final class showsampledetailed_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static java.util.Vector _jspx_dependants;

  static {
    _jspx_dependants = new java.util.Vector(2);
    _jspx_dependants.add("/WEB-INF/controls.tld");
    _jspx_dependants.add("/WEB-INF/rncontrols.tld");
  }

  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_showSamplePage_useLabAndNumberAsTitlePrefix_titleSuffix_setPreferenceTo_overrideReinvocationServletPath_loginPageUrl_labAndNumberSeparator_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleChecker_includeIfRetracted;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_jammModel_repositoryFile_id;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_jammApplet_width_id_height_appletParam_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_errorMessage_invertFilter_errorFilter;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_stringIterator_strings_id;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_jammChecker_jammAppletTag_includeOnlyIfCurrentAppletIs;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_jammChecker_jammAppletTag_invert_includeOnlyIfCurrentAppletIs;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_jammAppletPreservingLink_switchApplet_jammAppletTag_disableWhenLinkMatchesAppletTag;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_jammAppletPreservingLink_openInWindow_jammAppletTag_href_disableWhenLinkMatchesAppletTag;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_intIterator_ints_id;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleChecker_includeIfValueIsPresent;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleField_fieldCode_displayAsLabel_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleFieldUnits_fieldCode_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleTextIterator_restrictToTextTypesOtherThan_restrictToAttributes;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_authorizationChecker_suppressRenderingOnly_requireAuthentication_canSeeSampleText;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleFieldLabel_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleField_displayAsLabel_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleFieldUnits_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleTextIterator_restrictToAnnotations;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleTextChecker_includeOnlyForAnnotationsWithReferences;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleContextTranslator_translateFromAnnotationReferenceSample;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_showsampleLink_jumpSitePageUrl_detailedPageUrl_basicPageUrl;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleParam_name_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_labField_fieldCode_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleField_fieldCode_displayValueOnly_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_labField_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_providerField_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_ltaIterator_skipBlankValues_id_considerShowSampleVisibility;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleField_displayValueOnly_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleField_fieldCode_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_link_href;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_authorizationChecker_suppressRenderingOnly_canEditSample;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_fileIterator_requestUnavailableFilesParamName_id;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_parityChecker_includeOnlyOnFirst;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_parityChecker_includeOnlyOnMultiplesOf;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_fileField_id_fieldCode_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_fileChecker_includeOnlyIfFileHasDescription;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_preferenceChecker_invert_includeIfBooleanPrefIsTrue;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_fileField_fieldCode_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_parityChecker_includeOnlyOnLast;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_linkParam_value_name_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_styleBlock;

  public java.util.List getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _jspx_tagPool_rn_showSamplePage_useLabAndNumberAsTitlePrefix_titleSuffix_setPreferenceTo_overrideReinvocationServletPath_loginPageUrl_labAndNumberSeparator_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleChecker_includeIfRetracted = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_jammModel_repositoryFile_id = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_jammApplet_width_id_height_appletParam_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_errorMessage_invertFilter_errorFilter = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_stringIterator_strings_id = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_jammChecker_jammAppletTag_includeOnlyIfCurrentAppletIs = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_jammChecker_jammAppletTag_invert_includeOnlyIfCurrentAppletIs = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_jammAppletPreservingLink_switchApplet_jammAppletTag_disableWhenLinkMatchesAppletTag = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_jammAppletPreservingLink_openInWindow_jammAppletTag_href_disableWhenLinkMatchesAppletTag = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_intIterator_ints_id = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleChecker_includeIfValueIsPresent = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleField_fieldCode_displayAsLabel_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleFieldUnits_fieldCode_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleTextIterator_restrictToTextTypesOtherThan_restrictToAttributes = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_authorizationChecker_suppressRenderingOnly_requireAuthentication_canSeeSampleText = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleFieldLabel_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleField_displayAsLabel_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleFieldUnits_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleTextIterator_restrictToAnnotations = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleTextChecker_includeOnlyForAnnotationsWithReferences = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleContextTranslator_translateFromAnnotationReferenceSample = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_showsampleLink_jumpSitePageUrl_detailedPageUrl_basicPageUrl = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleParam_name_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_labField_fieldCode_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleField_fieldCode_displayValueOnly_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_labField_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_providerField_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_ltaIterator_skipBlankValues_id_considerShowSampleVisibility = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleField_displayValueOnly_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleField_fieldCode_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_link_href = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_authorizationChecker_suppressRenderingOnly_canEditSample = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_fileIterator_requestUnavailableFilesParamName_id = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_parityChecker_includeOnlyOnFirst = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_parityChecker_includeOnlyOnMultiplesOf = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_fileField_id_fieldCode_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_fileChecker_includeOnlyIfFileHasDescription = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_preferenceChecker_invert_includeIfBooleanPrefIsTrue = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_fileField_fieldCode_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_parityChecker_includeOnlyOnLast = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_linkParam_value_name_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_styleBlock = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
  }

  public void _jspDestroy() {
    _jspx_tagPool_rn_showSamplePage_useLabAndNumberAsTitlePrefix_titleSuffix_setPreferenceTo_overrideReinvocationServletPath_loginPageUrl_labAndNumberSeparator_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName.release();
    _jspx_tagPool_rn_sampleChecker_includeIfRetracted.release();
    _jspx_tagPool_rn_jammModel_repositoryFile_id.release();
    _jspx_tagPool_rn_jammApplet_width_id_height_appletParam_nobody.release();
    _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.release();
    _jspx_tagPool_ctl_errorMessage_invertFilter_errorFilter.release();
    _jspx_tagPool_ctl_stringIterator_strings_id.release();
    _jspx_tagPool_rn_jammChecker_jammAppletTag_includeOnlyIfCurrentAppletIs.release();
    _jspx_tagPool_rn_jammChecker_jammAppletTag_invert_includeOnlyIfCurrentAppletIs.release();
    _jspx_tagPool_rn_jammAppletPreservingLink_switchApplet_jammAppletTag_disableWhenLinkMatchesAppletTag.release();
    _jspx_tagPool_rn_jammAppletPreservingLink_openInWindow_jammAppletTag_href_disableWhenLinkMatchesAppletTag.release();
    _jspx_tagPool_ctl_intIterator_ints_id.release();
    _jspx_tagPool_rn_sampleChecker_includeIfValueIsPresent.release();
    _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.release();
    _jspx_tagPool_rn_sampleField_fieldCode_displayAsLabel_nobody.release();
    _jspx_tagPool_rn_sampleFieldUnits_fieldCode_nobody.release();
    _jspx_tagPool_rn_sampleTextIterator_restrictToTextTypesOtherThan_restrictToAttributes.release();
    _jspx_tagPool_rn_authorizationChecker_suppressRenderingOnly_requireAuthentication_canSeeSampleText.release();
    _jspx_tagPool_rn_sampleFieldLabel_nobody.release();
    _jspx_tagPool_rn_sampleField_displayAsLabel_nobody.release();
    _jspx_tagPool_rn_sampleFieldUnits_nobody.release();
    _jspx_tagPool_rn_sampleTextIterator_restrictToAnnotations.release();
    _jspx_tagPool_rn_sampleTextChecker_includeOnlyForAnnotationsWithReferences.release();
    _jspx_tagPool_rn_sampleContextTranslator_translateFromAnnotationReferenceSample.release();
    _jspx_tagPool_rn_showsampleLink_jumpSitePageUrl_detailedPageUrl_basicPageUrl.release();
    _jspx_tagPool_rn_sampleParam_name_nobody.release();
    _jspx_tagPool_rn_labField_fieldCode_nobody.release();
    _jspx_tagPool_rn_sampleField_fieldCode_displayValueOnly_nobody.release();
    _jspx_tagPool_rn_labField_nobody.release();
    _jspx_tagPool_rn_providerField_nobody.release();
    _jspx_tagPool_rn_ltaIterator_skipBlankValues_id_considerShowSampleVisibility.release();
    _jspx_tagPool_rn_sampleField_displayValueOnly_nobody.release();
    _jspx_tagPool_rn_sampleField_fieldCode_nobody.release();
    _jspx_tagPool_rn_link_href.release();
    _jspx_tagPool_rn_authorizationChecker_suppressRenderingOnly_canEditSample.release();
    _jspx_tagPool_rn_fileIterator_requestUnavailableFilesParamName_id.release();
    _jspx_tagPool_ctl_parityChecker_includeOnlyOnFirst.release();
    _jspx_tagPool_ctl_parityChecker_includeOnlyOnMultiplesOf.release();
    _jspx_tagPool_rn_fileField_id_fieldCode_nobody.release();
    _jspx_tagPool_rn_fileChecker_includeOnlyIfFileHasDescription.release();
    _jspx_tagPool_rn_preferenceChecker_invert_includeIfBooleanPrefIsTrue.release();
    _jspx_tagPool_rn_fileField_fieldCode_nobody.release();
    _jspx_tagPool_ctl_parityChecker_includeOnlyOnLast.release();
    _jspx_tagPool_ctl_linkParam_value_name_nobody.release();
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

      out.write("\n\n\n\n\n\n\n\n\n\n\n\n");
      //  rn:showSamplePage
      org.recipnet.site.content.rncontrols.ShowSamplePage _jspx_th_rn_showSamplePage_0 = (org.recipnet.site.content.rncontrols.ShowSamplePage) _jspx_tagPool_rn_showSamplePage_useLabAndNumberAsTitlePrefix_titleSuffix_setPreferenceTo_overrideReinvocationServletPath_loginPageUrl_labAndNumberSeparator_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName.get(org.recipnet.site.content.rncontrols.ShowSamplePage.class);
      _jspx_th_rn_showSamplePage_0.setPageContext(_jspx_page_context);
      _jspx_th_rn_showSamplePage_0.setParent(null);
      _jspx_th_rn_showSamplePage_0.setUseLabAndNumberAsTitlePrefix(true);
      _jspx_th_rn_showSamplePage_0.setSetPreferenceTo(UserBL.ShowsampleViewPref.DETAILED);
      _jspx_th_rn_showSamplePage_0.setLabAndNumberSeparator(" sample ");
      _jspx_th_rn_showSamplePage_0.setTitleSuffix(" - Reciprocal Net");
      _jspx_th_rn_showSamplePage_0.setOverrideReinvocationServletPath("/showsample.jsp");
      _jspx_th_rn_showSamplePage_0.setLoginPageUrl("/login.jsp");
      _jspx_th_rn_showSamplePage_0.setCurrentPageReinvocationUrlParamName("origUrl");
      _jspx_th_rn_showSamplePage_0.setAuthorizationFailedReasonParamName("authorizationFailedReason");
      int _jspx_eval_rn_showSamplePage_0 = _jspx_th_rn_showSamplePage_0.doStartTag();
      if (_jspx_eval_rn_showSamplePage_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        org.recipnet.site.content.rncontrols.ShowSamplePage htmlPage = null;
        if (_jspx_eval_rn_showSamplePage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
          out = _jspx_page_context.pushBody();
          _jspx_th_rn_showSamplePage_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
          _jspx_th_rn_showSamplePage_0.doInitBody();
        }
        htmlPage = (org.recipnet.site.content.rncontrols.ShowSamplePage) _jspx_page_context.findAttribute("htmlPage");
        do {
          out.write("\n  <div class=\"pageBody\">\n  ");
          if (_jspx_meth_rn_sampleChecker_0(_jspx_th_rn_showSamplePage_0, _jspx_page_context))
            return;
          out.write("\n  <table style=\"width: 100%; margin-bottom: 0.5em;\">\n  <tr>\n  <td rowspan=\"2\" style=\"width: 400px; vertical-align: top;\">\n    ");
          //  rn:jammModel
          org.recipnet.site.content.rncontrols.JammModelElement jammModel = null;
          org.recipnet.site.content.rncontrols.JammModelElement _jspx_th_rn_jammModel_0 = (org.recipnet.site.content.rncontrols.JammModelElement) _jspx_tagPool_rn_jammModel_repositoryFile_id.get(org.recipnet.site.content.rncontrols.JammModelElement.class);
          _jspx_th_rn_jammModel_0.setPageContext(_jspx_page_context);
          _jspx_th_rn_jammModel_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_showSamplePage_0);
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
              out.write("\n      ");
              //  rn:jammApplet
              org.recipnet.site.content.rncontrols.JammAppletTag jamm = null;
              org.recipnet.site.content.rncontrols.JammAppletTag _jspx_th_rn_jammApplet_0 = (org.recipnet.site.content.rncontrols.JammAppletTag) _jspx_tagPool_rn_jammApplet_width_id_height_appletParam_nobody.get(org.recipnet.site.content.rncontrols.JammAppletTag.class);
              _jspx_th_rn_jammApplet_0.setPageContext(_jspx_page_context);
              _jspx_th_rn_jammApplet_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_jammModel_0);
              _jspx_th_rn_jammApplet_0.setId("jamm");
              _jspx_th_rn_jammApplet_0.setWidth("400");
              _jspx_th_rn_jammApplet_0.setHeight("400");
              _jspx_th_rn_jammApplet_0.setAppletParam("jamm");
              int _jspx_eval_rn_jammApplet_0 = _jspx_th_rn_jammApplet_0.doStartTag();
              if (_jspx_th_rn_jammApplet_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              jamm = (org.recipnet.site.content.rncontrols.JammAppletTag) _jspx_page_context.findAttribute("jamm");
              _jspx_tagPool_rn_jammApplet_width_id_height_appletParam_nobody.reuse(_jspx_th_rn_jammApplet_0);
              out.write("\n      ");
              //  ctl:errorMessage
              org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_0 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
              _jspx_th_ctl_errorMessage_0.setPageContext(_jspx_page_context);
              _jspx_th_ctl_errorMessage_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_jammModel_0);
              _jspx_th_ctl_errorMessage_0.setErrorSupplier((org.recipnet.common.controls.ErrorSupplier) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${jammModel}", org.recipnet.common.controls.ErrorSupplier.class, (PageContext)_jspx_page_context, null, false));
              _jspx_th_ctl_errorMessage_0.setErrorFilter(JammModelElement.NO_CRT_FILE_AVAILABLE
                  | JammModelElement.REPOSITORY_DIRECTORY_NOT_FOUND);
              int _jspx_eval_ctl_errorMessage_0 = _jspx_th_ctl_errorMessage_0.doStartTag();
              if (_jspx_eval_ctl_errorMessage_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_ctl_errorMessage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_ctl_errorMessage_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_ctl_errorMessage_0.doInitBody();
                }
                do {
                  out.write("\n        &nbsp;<img src=\"images/nocrtfile.gif\" alt=\"no CRT file available\"/>\n      ");
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
              out.write("\n      ");
              //  ctl:errorMessage
              org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_1 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_invertFilter_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
              _jspx_th_ctl_errorMessage_1.setPageContext(_jspx_page_context);
              _jspx_th_ctl_errorMessage_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_jammModel_0);
              _jspx_th_ctl_errorMessage_1.setInvertFilter(true);
              _jspx_th_ctl_errorMessage_1.setErrorFilter(JammModelElement.NO_CRT_FILE_AVAILABLE
                  | JammModelElement.REPOSITORY_DIRECTORY_NOT_FOUND);
              int _jspx_eval_ctl_errorMessage_1 = _jspx_th_ctl_errorMessage_1.doStartTag();
              if (_jspx_eval_ctl_errorMessage_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_ctl_errorMessage_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_ctl_errorMessage_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_ctl_errorMessage_1.doInitBody();
                }
                do {
                  out.write("\n        <p style=\"font-size: small;\">\n          Switch to another visualization applet:\n        </p>\n        <table cellspacing=\"0\">\n          ");
                  //  ctl:stringIterator
                  org.recipnet.common.controls.ExplicitStringIterator appletChoices = null;
                  org.recipnet.common.controls.ExplicitStringIterator _jspx_th_ctl_stringIterator_0 = (org.recipnet.common.controls.ExplicitStringIterator) _jspx_tagPool_ctl_stringIterator_strings_id.get(org.recipnet.common.controls.ExplicitStringIterator.class);
                  _jspx_th_ctl_stringIterator_0.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_stringIterator_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_errorMessage_1);
                  _jspx_th_ctl_stringIterator_0.setId("appletChoices");
                  _jspx_th_ctl_stringIterator_0.setStrings("miniJaMM,JaMM1,JaMM2");
                  int _jspx_eval_ctl_stringIterator_0 = _jspx_th_ctl_stringIterator_0.doStartTag();
                  if (_jspx_eval_ctl_stringIterator_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_stringIterator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_stringIterator_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_stringIterator_0.doInitBody();
                    }
                    appletChoices = (org.recipnet.common.controls.ExplicitStringIterator) _jspx_page_context.findAttribute("appletChoices");
                    do {
                      out.write("\n          <tr>\n            <td>\n              ");
                      if (_jspx_meth_rn_jammChecker_0(_jspx_th_ctl_stringIterator_0, _jspx_page_context))
                        return;
                      out.write("\n              ");
                      if (_jspx_meth_rn_jammChecker_1(_jspx_th_ctl_stringIterator_0, _jspx_page_context))
                        return;
                      out.write("\n            </td>\n            <td> \n              ");
                      if (_jspx_meth_rn_jammAppletPreservingLink_0(_jspx_th_ctl_stringIterator_0, _jspx_page_context))
                        return;
                      out.write("\n            </td>\n            <td style=\"padding-left: 2em\">\n              ");
                      if (_jspx_meth_rn_jammChecker_2(_jspx_th_ctl_stringIterator_0, _jspx_page_context))
                        return;
                      out.write("\n            </td>\n          </tr>\n          ");
                      int evalDoAfterBody = _jspx_th_ctl_stringIterator_0.doAfterBody();
                      appletChoices = (org.recipnet.common.controls.ExplicitStringIterator) _jspx_page_context.findAttribute("appletChoices");
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_ctl_stringIterator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_ctl_stringIterator_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  appletChoices = (org.recipnet.common.controls.ExplicitStringIterator) _jspx_page_context.findAttribute("appletChoices");
                  _jspx_tagPool_ctl_stringIterator_strings_id.reuse(_jspx_th_ctl_stringIterator_0);
                  out.write("\n        </table>\n      ");
                  int evalDoAfterBody = _jspx_th_ctl_errorMessage_1.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_ctl_errorMessage_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_ctl_errorMessage_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_ctl_errorMessage_invertFilter_errorFilter.reuse(_jspx_th_ctl_errorMessage_1);
              out.write("\n    ");
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
          out.write("\n  </td>\n  <td>\n    <table class=\"dataTable\" cellSpacing=\"0\">\n      ");
          //  ctl:intIterator
          org.recipnet.common.controls.ExplicitIntIterator fieldCodes = null;
          org.recipnet.common.controls.ExplicitIntIterator _jspx_th_ctl_intIterator_0 = (org.recipnet.common.controls.ExplicitIntIterator) _jspx_tagPool_ctl_intIterator_ints_id.get(org.recipnet.common.controls.ExplicitIntIterator.class);
          _jspx_th_ctl_intIterator_0.setPageContext(_jspx_page_context);
          _jspx_th_ctl_intIterator_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_showSamplePage_0);
          _jspx_th_ctl_intIterator_0.setId("fieldCodes");
          _jspx_th_ctl_intIterator_0.setInts(new int[] {SampleTextBL.EMPIRICAL_FORMULA,
                                SampleDataInfo.A_FIELD,
                                SampleDataInfo.B_FIELD,
                                SampleDataInfo.C_FIELD,
                                SampleDataInfo.ALPHA_FIELD,
                                SampleDataInfo.BETA_FIELD,
                                SampleDataInfo.GAMMA_FIELD,
                                SampleDataInfo.V_FIELD,
                                SampleDataInfo.SPGP_FIELD,
                                SampleDataInfo.DCALC_FIELD,
                                SampleDataInfo.COLOR_FIELD,
                                SampleDataInfo.Z_FIELD,
                                SampleDataInfo.T_FIELD,
                                SampleDataInfo.FORMULAWEIGHT_FIELD,
                                SampleDataInfo.RF_FIELD,
                                SampleDataInfo.RWF_FIELD,
                                SampleDataInfo.RF2_FIELD,
                                SampleDataInfo.RWF2_FIELD,
                                SampleDataInfo.SUMMARY_FIELD});
          int _jspx_eval_ctl_intIterator_0 = _jspx_th_ctl_intIterator_0.doStartTag();
          if (_jspx_eval_ctl_intIterator_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_ctl_intIterator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_ctl_intIterator_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_ctl_intIterator_0.doInitBody();
            }
            fieldCodes = (org.recipnet.common.controls.ExplicitIntIterator) _jspx_page_context.findAttribute("fieldCodes");
            do {
              out.write("\n      ");
              if (_jspx_meth_rn_sampleChecker_1(_jspx_th_ctl_intIterator_0, _jspx_page_context))
                return;
              out.write("\n      ");
              int evalDoAfterBody = _jspx_th_ctl_intIterator_0.doAfterBody();
              fieldCodes = (org.recipnet.common.controls.ExplicitIntIterator) _jspx_page_context.findAttribute("fieldCodes");
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
            if (_jspx_eval_ctl_intIterator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
              out = _jspx_page_context.popBody();
          }
          if (_jspx_th_ctl_intIterator_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
            return;
          fieldCodes = (org.recipnet.common.controls.ExplicitIntIterator) _jspx_page_context.findAttribute("fieldCodes");
          _jspx_tagPool_ctl_intIterator_ints_id.reuse(_jspx_th_ctl_intIterator_0);
          out.write("\n\n      ");
          out.write("\n      ");
          //  rn:sampleTextIterator
          org.recipnet.site.content.rncontrols.SampleTextIterator _jspx_th_rn_sampleTextIterator_0 = (org.recipnet.site.content.rncontrols.SampleTextIterator) _jspx_tagPool_rn_sampleTextIterator_restrictToTextTypesOtherThan_restrictToAttributes.get(org.recipnet.site.content.rncontrols.SampleTextIterator.class);
          _jspx_th_rn_sampleTextIterator_0.setPageContext(_jspx_page_context);
          _jspx_th_rn_sampleTextIterator_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_showSamplePage_0);
          _jspx_th_rn_sampleTextIterator_0.setRestrictToAttributes(true);
          _jspx_th_rn_sampleTextIterator_0.setRestrictToTextTypesOtherThan(
              SampleTextBL.EMPIRICAL_FORMULA);
          int _jspx_eval_rn_sampleTextIterator_0 = _jspx_th_rn_sampleTextIterator_0.doStartTag();
          if (_jspx_eval_rn_sampleTextIterator_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_rn_sampleTextIterator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_rn_sampleTextIterator_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_rn_sampleTextIterator_0.doInitBody();
            }
            do {
              out.write("\n        ");
              if (_jspx_meth_rn_authorizationChecker_0(_jspx_th_rn_sampleTextIterator_0, _jspx_page_context))
                return;
              out.write("\n      ");
              int evalDoAfterBody = _jspx_th_rn_sampleTextIterator_0.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
            if (_jspx_eval_rn_sampleTextIterator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
              out = _jspx_page_context.popBody();
          }
          if (_jspx_th_rn_sampleTextIterator_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
            return;
          _jspx_tagPool_rn_sampleTextIterator_restrictToTextTypesOtherThan_restrictToAttributes.reuse(_jspx_th_rn_sampleTextIterator_0);
          out.write("\n\n      ");
          //  rn:sampleTextIterator
          org.recipnet.site.content.rncontrols.SampleTextIterator _jspx_th_rn_sampleTextIterator_1 = (org.recipnet.site.content.rncontrols.SampleTextIterator) _jspx_tagPool_rn_sampleTextIterator_restrictToAnnotations.get(org.recipnet.site.content.rncontrols.SampleTextIterator.class);
          _jspx_th_rn_sampleTextIterator_1.setPageContext(_jspx_page_context);
          _jspx_th_rn_sampleTextIterator_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_showSamplePage_0);
          _jspx_th_rn_sampleTextIterator_1.setRestrictToAnnotations(true);
          int _jspx_eval_rn_sampleTextIterator_1 = _jspx_th_rn_sampleTextIterator_1.doStartTag();
          if (_jspx_eval_rn_sampleTextIterator_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_rn_sampleTextIterator_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_rn_sampleTextIterator_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_rn_sampleTextIterator_1.doInitBody();
            }
            do {
              out.write("\n        ");
              //  rn:authorizationChecker
              org.recipnet.site.content.rncontrols.AuthorizationChecker _jspx_th_rn_authorizationChecker_1 = (org.recipnet.site.content.rncontrols.AuthorizationChecker) _jspx_tagPool_rn_authorizationChecker_suppressRenderingOnly_requireAuthentication_canSeeSampleText.get(org.recipnet.site.content.rncontrols.AuthorizationChecker.class);
              _jspx_th_rn_authorizationChecker_1.setPageContext(_jspx_page_context);
              _jspx_th_rn_authorizationChecker_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleTextIterator_1);
              _jspx_th_rn_authorizationChecker_1.setCanSeeSampleText(true);
              _jspx_th_rn_authorizationChecker_1.setRequireAuthentication(false);
              _jspx_th_rn_authorizationChecker_1.setSuppressRenderingOnly(true);
              int _jspx_eval_rn_authorizationChecker_1 = _jspx_th_rn_authorizationChecker_1.doStartTag();
              if (_jspx_eval_rn_authorizationChecker_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_rn_authorizationChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_rn_authorizationChecker_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_rn_authorizationChecker_1.doInitBody();
                }
                do {
                  out.write("\n          <tr>\n            <th>");
                  if (_jspx_meth_rn_sampleFieldLabel_2(_jspx_th_rn_authorizationChecker_1, _jspx_page_context))
                    return;
                  out.write(":</th>\n            <td>\n              ");
                  if (_jspx_meth_rn_sampleField_2(_jspx_th_rn_authorizationChecker_1, _jspx_page_context))
                    return;
                  out.write("\n              <span class=\"dataTableUnits\">");
                  if (_jspx_meth_rn_sampleFieldUnits_2(_jspx_th_rn_authorizationChecker_1, _jspx_page_context))
                    return;
                  out.write("</span>\n              ");
                  //  rn:sampleTextChecker
                  org.recipnet.site.content.rncontrols.SampleTextChecker _jspx_th_rn_sampleTextChecker_0 = (org.recipnet.site.content.rncontrols.SampleTextChecker) _jspx_tagPool_rn_sampleTextChecker_includeOnlyForAnnotationsWithReferences.get(org.recipnet.site.content.rncontrols.SampleTextChecker.class);
                  _jspx_th_rn_sampleTextChecker_0.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleTextChecker_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_1);
                  _jspx_th_rn_sampleTextChecker_0.setIncludeOnlyForAnnotationsWithReferences(true);
                  int _jspx_eval_rn_sampleTextChecker_0 = _jspx_th_rn_sampleTextChecker_0.doStartTag();
                  if (_jspx_eval_rn_sampleTextChecker_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_rn_sampleTextChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_rn_sampleTextChecker_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_rn_sampleTextChecker_0.doInitBody();
                    }
                    do {
                      out.write("\n                ");
                      //  rn:sampleContextTranslator
                      org.recipnet.site.content.rncontrols.SampleContextTranslator _jspx_th_rn_sampleContextTranslator_0 = (org.recipnet.site.content.rncontrols.SampleContextTranslator) _jspx_tagPool_rn_sampleContextTranslator_translateFromAnnotationReferenceSample.get(org.recipnet.site.content.rncontrols.SampleContextTranslator.class);
                      _jspx_th_rn_sampleContextTranslator_0.setPageContext(_jspx_page_context);
                      _jspx_th_rn_sampleContextTranslator_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleTextChecker_0);
                      _jspx_th_rn_sampleContextTranslator_0.setTranslateFromAnnotationReferenceSample(true);
                      int _jspx_eval_rn_sampleContextTranslator_0 = _jspx_th_rn_sampleContextTranslator_0.doStartTag();
                      if (_jspx_eval_rn_sampleContextTranslator_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_rn_sampleContextTranslator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_rn_sampleContextTranslator_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_rn_sampleContextTranslator_0.doInitBody();
                        }
                        do {
                          out.write("\n                  (jump to ");
                          //  rn:showsampleLink
                          org.recipnet.site.content.rncontrols.ShowsampleLink _jspx_th_rn_showsampleLink_0 = (org.recipnet.site.content.rncontrols.ShowsampleLink) _jspx_tagPool_rn_showsampleLink_jumpSitePageUrl_detailedPageUrl_basicPageUrl.get(org.recipnet.site.content.rncontrols.ShowsampleLink.class);
                          _jspx_th_rn_showsampleLink_0.setPageContext(_jspx_page_context);
                          _jspx_th_rn_showsampleLink_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleContextTranslator_0);
                          _jspx_th_rn_showsampleLink_0.setDetailedPageUrl("/showsampledetailed.jsp");
                          _jspx_th_rn_showsampleLink_0.setBasicPageUrl("/showsamplebasic.jsp");
                          _jspx_th_rn_showsampleLink_0.setJumpSitePageUrl("/showsamplejumpsite.jsp");
                          int _jspx_eval_rn_showsampleLink_0 = _jspx_th_rn_showsampleLink_0.doStartTag();
                          if (_jspx_eval_rn_showsampleLink_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            if (_jspx_eval_rn_showsampleLink_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_rn_showsampleLink_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_rn_showsampleLink_0.doInitBody();
                            }
                            do {
                              out.write("\n                    ");
                              if (_jspx_meth_rn_sampleParam_0(_jspx_th_rn_showsampleLink_0, _jspx_page_context))
                              return;
                              out.write("\n                    ");
                              //  rn:labField
                              org.recipnet.site.content.rncontrols.LabField _jspx_th_rn_labField_0 = (org.recipnet.site.content.rncontrols.LabField) _jspx_tagPool_rn_labField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.LabField.class);
                              _jspx_th_rn_labField_0.setPageContext(_jspx_page_context);
                              _jspx_th_rn_labField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_showsampleLink_0);
                              _jspx_th_rn_labField_0.setFieldCode(LabField.SHORT_NAME);
                              int _jspx_eval_rn_labField_0 = _jspx_th_rn_labField_0.doStartTag();
                              if (_jspx_th_rn_labField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                              return;
                              _jspx_tagPool_rn_labField_fieldCode_nobody.reuse(_jspx_th_rn_labField_0);
                              out.write("\n                    sample \n                    ");
                              //  rn:sampleField
                              org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_3 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_fieldCode_displayValueOnly_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
                              _jspx_th_rn_sampleField_3.setPageContext(_jspx_page_context);
                              _jspx_th_rn_sampleField_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_showsampleLink_0);
                              _jspx_th_rn_sampleField_3.setDisplayValueOnly(true);
                              _jspx_th_rn_sampleField_3.setFieldCode(SampleInfo.LOCAL_LAB_ID);
                              int _jspx_eval_rn_sampleField_3 = _jspx_th_rn_sampleField_3.doStartTag();
                              if (_jspx_th_rn_sampleField_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                              return;
                              _jspx_tagPool_rn_sampleField_fieldCode_displayValueOnly_nobody.reuse(_jspx_th_rn_sampleField_3);
                              int evalDoAfterBody = _jspx_th_rn_showsampleLink_0.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                            } while (true);
                            if (_jspx_eval_rn_showsampleLink_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                              out = _jspx_page_context.popBody();
                          }
                          if (_jspx_th_rn_showsampleLink_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          _jspx_tagPool_rn_showsampleLink_jumpSitePageUrl_detailedPageUrl_basicPageUrl.reuse(_jspx_th_rn_showsampleLink_0);
                          out.write(")\n                ");
                          int evalDoAfterBody = _jspx_th_rn_sampleContextTranslator_0.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_rn_sampleContextTranslator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                          out = _jspx_page_context.popBody();
                      }
                      if (_jspx_th_rn_sampleContextTranslator_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_rn_sampleContextTranslator_translateFromAnnotationReferenceSample.reuse(_jspx_th_rn_sampleContextTranslator_0);
                      out.write("\n              ");
                      int evalDoAfterBody = _jspx_th_rn_sampleTextChecker_0.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_rn_sampleTextChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_rn_sampleTextChecker_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_sampleTextChecker_includeOnlyForAnnotationsWithReferences.reuse(_jspx_th_rn_sampleTextChecker_0);
                  out.write("\n            </td>\n          </tr>\n        ");
                  int evalDoAfterBody = _jspx_th_rn_authorizationChecker_1.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_rn_authorizationChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_rn_authorizationChecker_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_authorizationChecker_suppressRenderingOnly_requireAuthentication_canSeeSampleText.reuse(_jspx_th_rn_authorizationChecker_1);
              out.write("\n      ");
              int evalDoAfterBody = _jspx_th_rn_sampleTextIterator_1.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
            if (_jspx_eval_rn_sampleTextIterator_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
              out = _jspx_page_context.popBody();
          }
          if (_jspx_th_rn_sampleTextIterator_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
            return;
          _jspx_tagPool_rn_sampleTextIterator_restrictToAnnotations.reuse(_jspx_th_rn_sampleTextIterator_1);
          out.write("\n\n      <tr>\n        <th>Lab name:</th>\n        <td>");
          if (_jspx_meth_rn_labField_1(_jspx_th_rn_showSamplePage_0, _jspx_page_context))
            return;
          out.write("</td>\n      </tr>\n      <tr>\n        <th>Sample provider:</th>\n        <td>");
          if (_jspx_meth_rn_providerField_0(_jspx_th_rn_showSamplePage_0, _jspx_page_context))
            return;
          out.write("</td>\n      </tr>\n      ");
          //  rn:ltaIterator
          org.recipnet.site.content.rncontrols.LtaIterator ltas = null;
          org.recipnet.site.content.rncontrols.LtaIterator _jspx_th_rn_ltaIterator_0 = (org.recipnet.site.content.rncontrols.LtaIterator) _jspx_tagPool_rn_ltaIterator_skipBlankValues_id_considerShowSampleVisibility.get(org.recipnet.site.content.rncontrols.LtaIterator.class);
          _jspx_th_rn_ltaIterator_0.setPageContext(_jspx_page_context);
          _jspx_th_rn_ltaIterator_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_showSamplePage_0);
          _jspx_th_rn_ltaIterator_0.setId("ltas");
          _jspx_th_rn_ltaIterator_0.setConsiderShowSampleVisibility(true);
          _jspx_th_rn_ltaIterator_0.setSkipBlankValues(true);
          int _jspx_eval_rn_ltaIterator_0 = _jspx_th_rn_ltaIterator_0.doStartTag();
          if (_jspx_eval_rn_ltaIterator_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_rn_ltaIterator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_rn_ltaIterator_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_rn_ltaIterator_0.doInitBody();
            }
            ltas = (org.recipnet.site.content.rncontrols.LtaIterator) _jspx_page_context.findAttribute("ltas");
            do {
              out.write("\n        <tr>\n          <th>\n            ");
              if (_jspx_meth_rn_sampleFieldLabel_3(_jspx_th_rn_ltaIterator_0, _jspx_page_context))
                return;
              out.write(":\n          </th>\n          <td>\n            ");
              if (_jspx_meth_rn_sampleField_4(_jspx_th_rn_ltaIterator_0, _jspx_page_context))
                return;
              if (_jspx_meth_rn_sampleFieldUnits_3(_jspx_th_rn_ltaIterator_0, _jspx_page_context))
                return;
              out.write("\n          </td>\n        </tr>\n      ");
              int evalDoAfterBody = _jspx_th_rn_ltaIterator_0.doAfterBody();
              ltas = (org.recipnet.site.content.rncontrols.LtaIterator) _jspx_page_context.findAttribute("ltas");
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
            if (_jspx_eval_rn_ltaIterator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
              out = _jspx_page_context.popBody();
          }
          if (_jspx_th_rn_ltaIterator_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
            return;
          ltas = (org.recipnet.site.content.rncontrols.LtaIterator) _jspx_page_context.findAttribute("ltas");
          _jspx_tagPool_rn_ltaIterator_skipBlankValues_id_considerShowSampleVisibility.reuse(_jspx_th_rn_ltaIterator_0);
          out.write("\n      <tr>\n        <th>\n          ");
          //  rn:sampleFieldLabel
          org.recipnet.site.content.rncontrols.SampleFieldLabel _jspx_th_rn_sampleFieldLabel_4 = (org.recipnet.site.content.rncontrols.SampleFieldLabel) _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldLabel.class);
          _jspx_th_rn_sampleFieldLabel_4.setPageContext(_jspx_page_context);
          _jspx_th_rn_sampleFieldLabel_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_showSamplePage_0);
          _jspx_th_rn_sampleFieldLabel_4.setFieldCode(SampleInfo.STATUS);
          int _jspx_eval_rn_sampleFieldLabel_4 = _jspx_th_rn_sampleFieldLabel_4.doStartTag();
          if (_jspx_th_rn_sampleFieldLabel_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
            return;
          _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.reuse(_jspx_th_rn_sampleFieldLabel_4);
          out.write(":\n        </th>\n        <td>\n          ");
          //  rn:sampleField
          org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_5 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
          _jspx_th_rn_sampleField_5.setPageContext(_jspx_page_context);
          _jspx_th_rn_sampleField_5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_showSamplePage_0);
          _jspx_th_rn_sampleField_5.setFieldCode(SampleInfo.STATUS);
          int _jspx_eval_rn_sampleField_5 = _jspx_th_rn_sampleField_5.doStartTag();
          if (_jspx_th_rn_sampleField_5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
            return;
          _jspx_tagPool_rn_sampleField_fieldCode_nobody.reuse(_jspx_th_rn_sampleField_5);
          out.write("\n        </td>\n      </tr>\n    </table>\n  </td>\n  </tr>\n\n  <tr>\n    <td class=\"navLinks\">\n      ");
          if (_jspx_meth_rn_link_0(_jspx_th_rn_showSamplePage_0, _jspx_page_context))
            return;
          out.write("\n      <br />\n      ");
          if (_jspx_meth_rn_link_1(_jspx_th_rn_showSamplePage_0, _jspx_page_context))
            return;
          out.write("\n      <br />\n      ");
          if (_jspx_meth_rn_link_2(_jspx_th_rn_showSamplePage_0, _jspx_page_context))
            return;
          out.write("\n      <br />\n      ");
          if (_jspx_meth_rn_authorizationChecker_2(_jspx_th_rn_showSamplePage_0, _jspx_page_context))
            return;
          out.write("\n    </td>\n  </tr>\n  </table>\n  <table class=\"fileTable\">\n    ");
          //  rn:fileIterator
          org.recipnet.site.content.rncontrols.FileIterator fileIt = null;
          org.recipnet.site.content.rncontrols.FileIterator _jspx_th_rn_fileIterator_0 = (org.recipnet.site.content.rncontrols.FileIterator) _jspx_tagPool_rn_fileIterator_requestUnavailableFilesParamName_id.get(org.recipnet.site.content.rncontrols.FileIterator.class);
          _jspx_th_rn_fileIterator_0.setPageContext(_jspx_page_context);
          _jspx_th_rn_fileIterator_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_showSamplePage_0);
          _jspx_th_rn_fileIterator_0.setId("fileIt");
          _jspx_th_rn_fileIterator_0.setRequestUnavailableFilesParamName("makeFilesAvailable");
          int _jspx_eval_rn_fileIterator_0 = _jspx_th_rn_fileIterator_0.doStartTag();
          if (_jspx_eval_rn_fileIterator_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_rn_fileIterator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_rn_fileIterator_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_rn_fileIterator_0.doInitBody();
            }
            fileIt = (org.recipnet.site.content.rncontrols.FileIterator) _jspx_page_context.findAttribute("fileIt");
            do {
              out.write("\n      ");
              if (_jspx_meth_ctl_parityChecker_0(_jspx_th_rn_fileIterator_0, _jspx_page_context))
                return;
              out.write("\n      ");
              if (_jspx_meth_ctl_parityChecker_1(_jspx_th_rn_fileIterator_0, _jspx_page_context))
                return;
              out.write("\n        <td>\n          ");
              //  rn:fileField
              org.recipnet.site.content.rncontrols.FileField file = null;
              org.recipnet.site.content.rncontrols.FileField _jspx_th_rn_fileField_0 = (org.recipnet.site.content.rncontrols.FileField) _jspx_tagPool_rn_fileField_id_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.FileField.class);
              _jspx_th_rn_fileField_0.setPageContext(_jspx_page_context);
              _jspx_th_rn_fileField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_fileIterator_0);
              _jspx_th_rn_fileField_0.setId("file");
              _jspx_th_rn_fileField_0.setFieldCode(FileField.LINKED_FILENAME);
              int _jspx_eval_rn_fileField_0 = _jspx_th_rn_fileField_0.doStartTag();
              if (_jspx_th_rn_fileField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              file = (org.recipnet.site.content.rncontrols.FileField) _jspx_page_context.findAttribute("file");
              _jspx_tagPool_rn_fileField_id_fieldCode_nobody.reuse(_jspx_th_rn_fileField_0);
              out.write("\n          ");
              //  ctl:errorMessage
              org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_2 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
              _jspx_th_ctl_errorMessage_2.setPageContext(_jspx_page_context);
              _jspx_th_ctl_errorMessage_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_fileIterator_0);
              _jspx_th_ctl_errorMessage_2.setErrorSupplier(file);
              _jspx_th_ctl_errorMessage_2.setErrorFilter(FileField.FILE_AVAILABLE_UPON_REQUEST);
              int _jspx_eval_ctl_errorMessage_2 = _jspx_th_ctl_errorMessage_2.doStartTag();
              if (_jspx_eval_ctl_errorMessage_2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_ctl_errorMessage_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_ctl_errorMessage_2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_ctl_errorMessage_2.doInitBody();
                }
                do {
                  out.write("\n            <img src=\"images/star.gif\" alt=\"*\">\n          ");
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
              out.write("\n          ");
              //  ctl:errorMessage
              org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_3 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
              _jspx_th_ctl_errorMessage_3.setPageContext(_jspx_page_context);
              _jspx_th_ctl_errorMessage_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_fileIterator_0);
              _jspx_th_ctl_errorMessage_3.setErrorSupplier(file);
              _jspx_th_ctl_errorMessage_3.setErrorFilter(FileField.FILE_UNAVAILABLE);
              int _jspx_eval_ctl_errorMessage_3 = _jspx_th_ctl_errorMessage_3.doStartTag();
              if (_jspx_eval_ctl_errorMessage_3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_ctl_errorMessage_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_ctl_errorMessage_3.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_ctl_errorMessage_3.doInitBody();
                }
                do {
                  out.write("\n            <img src=\"images/star.gif\" alt=\"*\">\n          ");
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
              out.write("\n          ");
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
                  out.write("\n            ");
                  //  rn:preferenceChecker
                  org.recipnet.site.content.rncontrols.PreferenceChecker _jspx_th_rn_preferenceChecker_0 = (org.recipnet.site.content.rncontrols.PreferenceChecker) _jspx_tagPool_rn_preferenceChecker_invert_includeIfBooleanPrefIsTrue.get(org.recipnet.site.content.rncontrols.PreferenceChecker.class);
                  _jspx_th_rn_preferenceChecker_0.setPageContext(_jspx_page_context);
                  _jspx_th_rn_preferenceChecker_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_fileChecker_0);
                  _jspx_th_rn_preferenceChecker_0.setIncludeIfBooleanPrefIsTrue(
                UserBL.Pref.SUPPRESS_DESCRIPTIONS);
                  _jspx_th_rn_preferenceChecker_0.setInvert(true);
                  int _jspx_eval_rn_preferenceChecker_0 = _jspx_th_rn_preferenceChecker_0.doStartTag();
                  if (_jspx_eval_rn_preferenceChecker_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_rn_preferenceChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_rn_preferenceChecker_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_rn_preferenceChecker_0.doInitBody();
                    }
                    do {
                      out.write("\n              <br />\n              <font class=\"description\">\n                ");
                      //  rn:fileField
                      org.recipnet.site.content.rncontrols.FileField _jspx_th_rn_fileField_1 = (org.recipnet.site.content.rncontrols.FileField) _jspx_tagPool_rn_fileField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.FileField.class);
                      _jspx_th_rn_fileField_1.setPageContext(_jspx_page_context);
                      _jspx_th_rn_fileField_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_preferenceChecker_0);
                      _jspx_th_rn_fileField_1.setFieldCode(FileField.DESCRIPTION);
                      int _jspx_eval_rn_fileField_1 = _jspx_th_rn_fileField_1.doStartTag();
                      if (_jspx_th_rn_fileField_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_rn_fileField_fieldCode_nobody.reuse(_jspx_th_rn_fileField_1);
                      out.write("\n              </font>\n            ");
                      int evalDoAfterBody = _jspx_th_rn_preferenceChecker_0.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_rn_preferenceChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_rn_preferenceChecker_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_preferenceChecker_invert_includeIfBooleanPrefIsTrue.reuse(_jspx_th_rn_preferenceChecker_0);
                  out.write("\n          ");
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
              out.write("\n        </td>\n        ");
              if (_jspx_meth_ctl_parityChecker_2(_jspx_th_rn_fileIterator_0, _jspx_page_context))
                return;
              out.write("\n    ");
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
          _jspx_tagPool_rn_fileIterator_requestUnavailableFilesParamName_id.reuse(_jspx_th_rn_fileIterator_0);
          out.write("\n    ");
          //  ctl:errorMessage
          org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_4 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
          _jspx_th_ctl_errorMessage_4.setPageContext(_jspx_page_context);
          _jspx_th_ctl_errorMessage_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_showSamplePage_0);
          _jspx_th_ctl_errorMessage_4.setErrorSupplier((org.recipnet.common.controls.ErrorSupplier) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${fileIt}", org.recipnet.common.controls.ErrorSupplier.class, (PageContext)_jspx_page_context, null, false));
          _jspx_th_ctl_errorMessage_4.setErrorFilter(FileIterator.SOME_FILES_AVAILABLE_UPON_REQUEST);
          int _jspx_eval_ctl_errorMessage_4 = _jspx_th_ctl_errorMessage_4.doStartTag();
          if (_jspx_eval_ctl_errorMessage_4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_ctl_errorMessage_4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_ctl_errorMessage_4.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_ctl_errorMessage_4.doInitBody();
            }
            do {
              out.write("\n      <tr>\n        <td colspan=\"5\" style=\"padding-top: 0.5em;\">\n            <img name=\"star\" src=\"images/star.gif\" alt=\"*\"/>\n            These large files are available upon request.\n            ");
              if (_jspx_meth_rn_link_4(_jspx_th_ctl_errorMessage_4, _jspx_page_context))
                return;
              out.write("\n        </td>\n      </tr>\n    ");
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
          out.write("\n    ");
          //  ctl:errorMessage
          org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_5 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
          _jspx_th_ctl_errorMessage_5.setPageContext(_jspx_page_context);
          _jspx_th_ctl_errorMessage_5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_showSamplePage_0);
          _jspx_th_ctl_errorMessage_5.setErrorSupplier((org.recipnet.common.controls.ErrorSupplier) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${fileIt}", org.recipnet.common.controls.ErrorSupplier.class, (PageContext)_jspx_page_context, null, false));
          _jspx_th_ctl_errorMessage_5.setErrorFilter(FileIterator.SOME_FILES_UNAVAILABLE);
          int _jspx_eval_ctl_errorMessage_5 = _jspx_th_ctl_errorMessage_5.doStartTag();
          if (_jspx_eval_ctl_errorMessage_5 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_ctl_errorMessage_5 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_ctl_errorMessage_5.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_ctl_errorMessage_5.doInitBody();
            }
            do {
              out.write("\n      <tr>\n        <td colspan=\"5\" style=\"padding-top: 0.5em;\">\n            <img name=\"star\" src=\"images/star.gif\" alt=\"*\"/>\n            You lack authorization to download these large files.\n        </td>\n      </tr>\n    ");
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
          out.write("\n    ");
          //  ctl:errorMessage
          org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_6 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
          _jspx_th_ctl_errorMessage_6.setPageContext(_jspx_page_context);
          _jspx_th_ctl_errorMessage_6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_showSamplePage_0);
          _jspx_th_ctl_errorMessage_6.setErrorSupplier((org.recipnet.common.controls.ErrorSupplier) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${fileIt}", org.recipnet.common.controls.ErrorSupplier.class, (PageContext)_jspx_page_context, null, false));
          _jspx_th_ctl_errorMessage_6.setErrorFilter((FileIterator.DIRECTORY_BUT_NO_HOLDINGS
            | FileIterator.HOLDINGS_BUT_NO_DIRECTORY
            | FileIterator.NO_DIRECTORY_NO_HOLDINGS));
          int _jspx_eval_ctl_errorMessage_6 = _jspx_th_ctl_errorMessage_6.doStartTag();
          if (_jspx_eval_ctl_errorMessage_6 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_ctl_errorMessage_6 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_ctl_errorMessage_6.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_ctl_errorMessage_6.doInitBody();
            }
            do {
              out.write("\n      <tr>\n        <td class=\"errorMessage\">\n            Repository is not available.\n        </td>\n      </tr>\n    ");
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
          out.write("\n    ");
          //  ctl:errorMessage
          org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_7 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
          _jspx_th_ctl_errorMessage_7.setPageContext(_jspx_page_context);
          _jspx_th_ctl_errorMessage_7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_showSamplePage_0);
          _jspx_th_ctl_errorMessage_7.setErrorSupplier((org.recipnet.common.controls.ErrorSupplier) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${fileIt}", org.recipnet.common.controls.ErrorSupplier.class, (PageContext)_jspx_page_context, null, false));
          _jspx_th_ctl_errorMessage_7.setErrorFilter(FileIterator.NO_FILES_IN_REPOSITORY);
          int _jspx_eval_ctl_errorMessage_7 = _jspx_th_ctl_errorMessage_7.doStartTag();
          if (_jspx_eval_ctl_errorMessage_7 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_ctl_errorMessage_7 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_ctl_errorMessage_7.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_ctl_errorMessage_7.doInitBody();
            }
            do {
              out.write("\n      <tr>\n        <td class=\"errorMessage\">\n          No files for ");
              //  rn:sampleField
              org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_6 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_fieldCode_displayAsLabel_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
              _jspx_th_rn_sampleField_6.setPageContext(_jspx_page_context);
              _jspx_th_rn_sampleField_6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_errorMessage_7);
              _jspx_th_rn_sampleField_6.setFieldCode(SampleInfo.LOCAL_LAB_ID);
              _jspx_th_rn_sampleField_6.setDisplayAsLabel(true);
              int _jspx_eval_rn_sampleField_6 = _jspx_th_rn_sampleField_6.doStartTag();
              if (_jspx_th_rn_sampleField_6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_sampleField_fieldCode_displayAsLabel_nobody.reuse(_jspx_th_rn_sampleField_6);
              out.write(" in repository!\n        </td>\n      </tr>\n    ");
              int evalDoAfterBody = _jspx_th_ctl_errorMessage_7.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
            if (_jspx_eval_ctl_errorMessage_7 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
              out = _jspx_page_context.popBody();
          }
          if (_jspx_th_ctl_errorMessage_7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
            return;
          _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.reuse(_jspx_th_ctl_errorMessage_7);
          out.write("\n  </table>\n  </div>\n  ");
          if (_jspx_meth_ctl_styleBlock_0(_jspx_th_rn_showSamplePage_0, _jspx_page_context))
            return;
          out.write('\n');
          int evalDoAfterBody = _jspx_th_rn_showSamplePage_0.doAfterBody();
          htmlPage = (org.recipnet.site.content.rncontrols.ShowSamplePage) _jspx_page_context.findAttribute("htmlPage");
          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
            break;
        } while (true);
        if (_jspx_eval_rn_showSamplePage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
          out = _jspx_page_context.popBody();
      }
      if (_jspx_th_rn_showSamplePage_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
        return;
      _jspx_tagPool_rn_showSamplePage_useLabAndNumberAsTitlePrefix_titleSuffix_setPreferenceTo_overrideReinvocationServletPath_loginPageUrl_labAndNumberSeparator_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName.reuse(_jspx_th_rn_showSamplePage_0);
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

  private boolean _jspx_meth_rn_sampleChecker_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_showSamplePage_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:sampleChecker
    org.recipnet.site.content.rncontrols.SampleChecker _jspx_th_rn_sampleChecker_0 = (org.recipnet.site.content.rncontrols.SampleChecker) _jspx_tagPool_rn_sampleChecker_includeIfRetracted.get(org.recipnet.site.content.rncontrols.SampleChecker.class);
    _jspx_th_rn_sampleChecker_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_sampleChecker_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_showSamplePage_0);
    _jspx_th_rn_sampleChecker_0.setIncludeIfRetracted(true);
    int _jspx_eval_rn_sampleChecker_0 = _jspx_th_rn_sampleChecker_0.doStartTag();
    if (_jspx_eval_rn_sampleChecker_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_sampleChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_sampleChecker_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_sampleChecker_0.doInitBody();
      }
      do {
        out.write("\n    <p style=\"text-align: center; text-color: #F00000;\">\n      This sample has been retracted by its lab, possibly because it is\n      partially or wholly incorrect.  Please examine this\n      sample's comments for more details.\n    </p>\n  ");
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

  private boolean _jspx_meth_rn_jammChecker_0(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_stringIterator_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:jammChecker
    org.recipnet.site.content.rncontrols.JammChecker _jspx_th_rn_jammChecker_0 = (org.recipnet.site.content.rncontrols.JammChecker) _jspx_tagPool_rn_jammChecker_jammAppletTag_includeOnlyIfCurrentAppletIs.get(org.recipnet.site.content.rncontrols.JammChecker.class);
    _jspx_th_rn_jammChecker_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_jammChecker_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_stringIterator_0);
    _jspx_th_rn_jammChecker_0.setJammAppletTag((org.recipnet.site.content.rncontrols.JammAppletTag) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${jamm}", org.recipnet.site.content.rncontrols.JammAppletTag.class, (PageContext)_jspx_page_context, null, false));
    _jspx_th_rn_jammChecker_0.setIncludeOnlyIfCurrentAppletIs((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${appletChoices.currentString}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_rn_jammChecker_0 = _jspx_th_rn_jammChecker_0.doStartTag();
    if (_jspx_eval_rn_jammChecker_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_jammChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_jammChecker_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_jammChecker_0.doInitBody();
      }
      do {
        out.write("\n                <img src=\"images/pointer.gif\" alt=\"&gt;\"/>\n              ");
        int evalDoAfterBody = _jspx_th_rn_jammChecker_0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_jammChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_jammChecker_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_jammChecker_jammAppletTag_includeOnlyIfCurrentAppletIs.reuse(_jspx_th_rn_jammChecker_0);
    return false;
  }

  private boolean _jspx_meth_rn_jammChecker_1(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_stringIterator_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:jammChecker
    org.recipnet.site.content.rncontrols.JammChecker _jspx_th_rn_jammChecker_1 = (org.recipnet.site.content.rncontrols.JammChecker) _jspx_tagPool_rn_jammChecker_jammAppletTag_invert_includeOnlyIfCurrentAppletIs.get(org.recipnet.site.content.rncontrols.JammChecker.class);
    _jspx_th_rn_jammChecker_1.setPageContext(_jspx_page_context);
    _jspx_th_rn_jammChecker_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_stringIterator_0);
    _jspx_th_rn_jammChecker_1.setJammAppletTag((org.recipnet.site.content.rncontrols.JammAppletTag) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${jamm}", org.recipnet.site.content.rncontrols.JammAppletTag.class, (PageContext)_jspx_page_context, null, false));
    _jspx_th_rn_jammChecker_1.setInvert(true);
    _jspx_th_rn_jammChecker_1.setIncludeOnlyIfCurrentAppletIs((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${appletChoices.currentString}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_rn_jammChecker_1 = _jspx_th_rn_jammChecker_1.doStartTag();
    if (_jspx_eval_rn_jammChecker_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_jammChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_jammChecker_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_jammChecker_1.doInitBody();
      }
      do {
        out.write("\n                <img src=\"images/pointer-blank.gif\" alt=\"-\"/>\n              ");
        int evalDoAfterBody = _jspx_th_rn_jammChecker_1.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_jammChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_jammChecker_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_jammChecker_jammAppletTag_invert_includeOnlyIfCurrentAppletIs.reuse(_jspx_th_rn_jammChecker_1);
    return false;
  }

  private boolean _jspx_meth_rn_jammAppletPreservingLink_0(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_stringIterator_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:jammAppletPreservingLink
    org.recipnet.site.content.rncontrols.JammAppletPreservingLink _jspx_th_rn_jammAppletPreservingLink_0 = (org.recipnet.site.content.rncontrols.JammAppletPreservingLink) _jspx_tagPool_rn_jammAppletPreservingLink_switchApplet_jammAppletTag_disableWhenLinkMatchesAppletTag.get(org.recipnet.site.content.rncontrols.JammAppletPreservingLink.class);
    _jspx_th_rn_jammAppletPreservingLink_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_jammAppletPreservingLink_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_stringIterator_0);
    _jspx_th_rn_jammAppletPreservingLink_0.setJammAppletTag((org.recipnet.site.content.rncontrols.JammAppletTag) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${jamm}", org.recipnet.site.content.rncontrols.JammAppletTag.class, (PageContext)_jspx_page_context, null, false));
    _jspx_th_rn_jammAppletPreservingLink_0.setSwitchApplet((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${appletChoices.currentString}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
    _jspx_th_rn_jammAppletPreservingLink_0.setDisableWhenLinkMatchesAppletTag(true);
    int _jspx_eval_rn_jammAppletPreservingLink_0 = _jspx_th_rn_jammAppletPreservingLink_0.doStartTag();
    if (_jspx_eval_rn_jammAppletPreservingLink_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_jammAppletPreservingLink_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_jammAppletPreservingLink_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_jammAppletPreservingLink_0.doInitBody();
      }
      do {
        out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${appletChoices.currentString}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
        int evalDoAfterBody = _jspx_th_rn_jammAppletPreservingLink_0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_jammAppletPreservingLink_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_jammAppletPreservingLink_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_jammAppletPreservingLink_switchApplet_jammAppletTag_disableWhenLinkMatchesAppletTag.reuse(_jspx_th_rn_jammAppletPreservingLink_0);
    return false;
  }

  private boolean _jspx_meth_rn_jammChecker_2(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_stringIterator_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:jammChecker
    org.recipnet.site.content.rncontrols.JammChecker _jspx_th_rn_jammChecker_2 = (org.recipnet.site.content.rncontrols.JammChecker) _jspx_tagPool_rn_jammChecker_jammAppletTag_includeOnlyIfCurrentAppletIs.get(org.recipnet.site.content.rncontrols.JammChecker.class);
    _jspx_th_rn_jammChecker_2.setPageContext(_jspx_page_context);
    _jspx_th_rn_jammChecker_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_stringIterator_0);
    _jspx_th_rn_jammChecker_2.setJammAppletTag((org.recipnet.site.content.rncontrols.JammAppletTag) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${jamm}", org.recipnet.site.content.rncontrols.JammAppletTag.class, (PageContext)_jspx_page_context, null, false));
    _jspx_th_rn_jammChecker_2.setIncludeOnlyIfCurrentAppletIs((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${appletChoices.currentString}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_rn_jammChecker_2 = _jspx_th_rn_jammChecker_2.doStartTag();
    if (_jspx_eval_rn_jammChecker_2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_jammChecker_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_jammChecker_2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_jammChecker_2.doInitBody();
      }
      do {
        out.write("\n                ");
        if (_jspx_meth_rn_jammAppletPreservingLink_1(_jspx_th_rn_jammChecker_2, _jspx_page_context))
          return true;
        out.write("\n              ");
        int evalDoAfterBody = _jspx_th_rn_jammChecker_2.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_jammChecker_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_jammChecker_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_jammChecker_jammAppletTag_includeOnlyIfCurrentAppletIs.reuse(_jspx_th_rn_jammChecker_2);
    return false;
  }

  private boolean _jspx_meth_rn_jammAppletPreservingLink_1(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_jammChecker_2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:jammAppletPreservingLink
    org.recipnet.site.content.rncontrols.JammAppletPreservingLink _jspx_th_rn_jammAppletPreservingLink_1 = (org.recipnet.site.content.rncontrols.JammAppletPreservingLink) _jspx_tagPool_rn_jammAppletPreservingLink_openInWindow_jammAppletTag_href_disableWhenLinkMatchesAppletTag.get(org.recipnet.site.content.rncontrols.JammAppletPreservingLink.class);
    _jspx_th_rn_jammAppletPreservingLink_1.setPageContext(_jspx_page_context);
    _jspx_th_rn_jammAppletPreservingLink_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_jammChecker_2);
    _jspx_th_rn_jammAppletPreservingLink_1.setHref("/jamm.jsp");
    _jspx_th_rn_jammAppletPreservingLink_1.setJammAppletTag((org.recipnet.site.content.rncontrols.JammAppletTag) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${jamm}", org.recipnet.site.content.rncontrols.JammAppletTag.class, (PageContext)_jspx_page_context, null, false));
    _jspx_th_rn_jammAppletPreservingLink_1.setOpenInWindow("true");
    _jspx_th_rn_jammAppletPreservingLink_1.setDisableWhenLinkMatchesAppletTag(false);
    int _jspx_eval_rn_jammAppletPreservingLink_1 = _jspx_th_rn_jammAppletPreservingLink_1.doStartTag();
    if (_jspx_eval_rn_jammAppletPreservingLink_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_jammAppletPreservingLink_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_jammAppletPreservingLink_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_jammAppletPreservingLink_1.doInitBody();
      }
      do {
        out.write("open in new window...");
        int evalDoAfterBody = _jspx_th_rn_jammAppletPreservingLink_1.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_jammAppletPreservingLink_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_jammAppletPreservingLink_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_jammAppletPreservingLink_openInWindow_jammAppletTag_href_disableWhenLinkMatchesAppletTag.reuse(_jspx_th_rn_jammAppletPreservingLink_1);
    return false;
  }

  private boolean _jspx_meth_rn_sampleChecker_1(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_intIterator_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:sampleChecker
    org.recipnet.site.content.rncontrols.SampleChecker _jspx_th_rn_sampleChecker_1 = (org.recipnet.site.content.rncontrols.SampleChecker) _jspx_tagPool_rn_sampleChecker_includeIfValueIsPresent.get(org.recipnet.site.content.rncontrols.SampleChecker.class);
    _jspx_th_rn_sampleChecker_1.setPageContext(_jspx_page_context);
    _jspx_th_rn_sampleChecker_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_intIterator_0);
    _jspx_th_rn_sampleChecker_1.setIncludeIfValueIsPresent(((java.lang.Integer) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${fieldCodes.currentInt}", java.lang.Integer.class, (PageContext)_jspx_page_context, null, false)).intValue());
    int _jspx_eval_rn_sampleChecker_1 = _jspx_th_rn_sampleChecker_1.doStartTag();
    if (_jspx_eval_rn_sampleChecker_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_sampleChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_sampleChecker_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_sampleChecker_1.doInitBody();
      }
      do {
        out.write("\n        <tr>\n          <th>");
        if (_jspx_meth_rn_sampleFieldLabel_0(_jspx_th_rn_sampleChecker_1, _jspx_page_context))
          return true;
        out.write(":</th>\n          <td>\n            ");
        if (_jspx_meth_rn_sampleField_0(_jspx_th_rn_sampleChecker_1, _jspx_page_context))
          return true;
        out.write("\n            <span class=\"dataTableUnits\">");
        if (_jspx_meth_rn_sampleFieldUnits_0(_jspx_th_rn_sampleChecker_1, _jspx_page_context))
          return true;
        out.write("</span>\n          </td>\n        </tr>\n      ");
        int evalDoAfterBody = _jspx_th_rn_sampleChecker_1.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_sampleChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_sampleChecker_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_sampleChecker_includeIfValueIsPresent.reuse(_jspx_th_rn_sampleChecker_1);
    return false;
  }

  private boolean _jspx_meth_rn_sampleFieldLabel_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleChecker_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:sampleFieldLabel
    org.recipnet.site.content.rncontrols.SampleFieldLabel _jspx_th_rn_sampleFieldLabel_0 = (org.recipnet.site.content.rncontrols.SampleFieldLabel) _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldLabel.class);
    _jspx_th_rn_sampleFieldLabel_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_sampleFieldLabel_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleChecker_1);
    _jspx_th_rn_sampleFieldLabel_0.setFieldCode(((java.lang.Integer) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${fieldCodes.currentInt}", java.lang.Integer.class, (PageContext)_jspx_page_context, null, false)).intValue());
    int _jspx_eval_rn_sampleFieldLabel_0 = _jspx_th_rn_sampleFieldLabel_0.doStartTag();
    if (_jspx_th_rn_sampleFieldLabel_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.reuse(_jspx_th_rn_sampleFieldLabel_0);
    return false;
  }

  private boolean _jspx_meth_rn_sampleField_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleChecker_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:sampleField
    org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_0 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_fieldCode_displayAsLabel_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
    _jspx_th_rn_sampleField_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_sampleField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleChecker_1);
    _jspx_th_rn_sampleField_0.setDisplayAsLabel(true);
    _jspx_th_rn_sampleField_0.setFieldCode(((java.lang.Integer) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${fieldCodes.currentInt}", java.lang.Integer.class, (PageContext)_jspx_page_context, null, false)).intValue());
    int _jspx_eval_rn_sampleField_0 = _jspx_th_rn_sampleField_0.doStartTag();
    if (_jspx_th_rn_sampleField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_sampleField_fieldCode_displayAsLabel_nobody.reuse(_jspx_th_rn_sampleField_0);
    return false;
  }

  private boolean _jspx_meth_rn_sampleFieldUnits_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleChecker_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:sampleFieldUnits
    org.recipnet.site.content.rncontrols.SampleFieldUnits _jspx_th_rn_sampleFieldUnits_0 = (org.recipnet.site.content.rncontrols.SampleFieldUnits) _jspx_tagPool_rn_sampleFieldUnits_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldUnits.class);
    _jspx_th_rn_sampleFieldUnits_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_sampleFieldUnits_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleChecker_1);
    _jspx_th_rn_sampleFieldUnits_0.setFieldCode(((java.lang.Integer) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${fieldCodes.currentInt}", java.lang.Integer.class, (PageContext)_jspx_page_context, null, false)).intValue());
    int _jspx_eval_rn_sampleFieldUnits_0 = _jspx_th_rn_sampleFieldUnits_0.doStartTag();
    if (_jspx_th_rn_sampleFieldUnits_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_sampleFieldUnits_fieldCode_nobody.reuse(_jspx_th_rn_sampleFieldUnits_0);
    return false;
  }

  private boolean _jspx_meth_rn_authorizationChecker_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleTextIterator_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:authorizationChecker
    org.recipnet.site.content.rncontrols.AuthorizationChecker _jspx_th_rn_authorizationChecker_0 = (org.recipnet.site.content.rncontrols.AuthorizationChecker) _jspx_tagPool_rn_authorizationChecker_suppressRenderingOnly_requireAuthentication_canSeeSampleText.get(org.recipnet.site.content.rncontrols.AuthorizationChecker.class);
    _jspx_th_rn_authorizationChecker_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_authorizationChecker_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleTextIterator_0);
    _jspx_th_rn_authorizationChecker_0.setCanSeeSampleText(true);
    _jspx_th_rn_authorizationChecker_0.setRequireAuthentication(false);
    _jspx_th_rn_authorizationChecker_0.setSuppressRenderingOnly(true);
    int _jspx_eval_rn_authorizationChecker_0 = _jspx_th_rn_authorizationChecker_0.doStartTag();
    if (_jspx_eval_rn_authorizationChecker_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_authorizationChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_authorizationChecker_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_authorizationChecker_0.doInitBody();
      }
      do {
        out.write("\n          <tr>\n            <th>");
        if (_jspx_meth_rn_sampleFieldLabel_1(_jspx_th_rn_authorizationChecker_0, _jspx_page_context))
          return true;
        out.write(":</th>\n            <td>\n              ");
        if (_jspx_meth_rn_sampleField_1(_jspx_th_rn_authorizationChecker_0, _jspx_page_context))
          return true;
        out.write("\n              <span class=\"dataTableUnits\">");
        if (_jspx_meth_rn_sampleFieldUnits_1(_jspx_th_rn_authorizationChecker_0, _jspx_page_context))
          return true;
        out.write("</span>\n            </td>\n          </tr>\n        ");
        int evalDoAfterBody = _jspx_th_rn_authorizationChecker_0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_authorizationChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_authorizationChecker_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_authorizationChecker_suppressRenderingOnly_requireAuthentication_canSeeSampleText.reuse(_jspx_th_rn_authorizationChecker_0);
    return false;
  }

  private boolean _jspx_meth_rn_sampleFieldLabel_1(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_authorizationChecker_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:sampleFieldLabel
    org.recipnet.site.content.rncontrols.SampleFieldLabel _jspx_th_rn_sampleFieldLabel_1 = (org.recipnet.site.content.rncontrols.SampleFieldLabel) _jspx_tagPool_rn_sampleFieldLabel_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldLabel.class);
    _jspx_th_rn_sampleFieldLabel_1.setPageContext(_jspx_page_context);
    _jspx_th_rn_sampleFieldLabel_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_0);
    int _jspx_eval_rn_sampleFieldLabel_1 = _jspx_th_rn_sampleFieldLabel_1.doStartTag();
    if (_jspx_th_rn_sampleFieldLabel_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_sampleFieldLabel_nobody.reuse(_jspx_th_rn_sampleFieldLabel_1);
    return false;
  }

  private boolean _jspx_meth_rn_sampleField_1(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_authorizationChecker_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:sampleField
    org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_1 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_displayAsLabel_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
    _jspx_th_rn_sampleField_1.setPageContext(_jspx_page_context);
    _jspx_th_rn_sampleField_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_0);
    _jspx_th_rn_sampleField_1.setDisplayAsLabel(true);
    int _jspx_eval_rn_sampleField_1 = _jspx_th_rn_sampleField_1.doStartTag();
    if (_jspx_th_rn_sampleField_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_sampleField_displayAsLabel_nobody.reuse(_jspx_th_rn_sampleField_1);
    return false;
  }

  private boolean _jspx_meth_rn_sampleFieldUnits_1(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_authorizationChecker_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:sampleFieldUnits
    org.recipnet.site.content.rncontrols.SampleFieldUnits _jspx_th_rn_sampleFieldUnits_1 = (org.recipnet.site.content.rncontrols.SampleFieldUnits) _jspx_tagPool_rn_sampleFieldUnits_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldUnits.class);
    _jspx_th_rn_sampleFieldUnits_1.setPageContext(_jspx_page_context);
    _jspx_th_rn_sampleFieldUnits_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_0);
    int _jspx_eval_rn_sampleFieldUnits_1 = _jspx_th_rn_sampleFieldUnits_1.doStartTag();
    if (_jspx_th_rn_sampleFieldUnits_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_sampleFieldUnits_nobody.reuse(_jspx_th_rn_sampleFieldUnits_1);
    return false;
  }

  private boolean _jspx_meth_rn_sampleFieldLabel_2(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_authorizationChecker_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:sampleFieldLabel
    org.recipnet.site.content.rncontrols.SampleFieldLabel _jspx_th_rn_sampleFieldLabel_2 = (org.recipnet.site.content.rncontrols.SampleFieldLabel) _jspx_tagPool_rn_sampleFieldLabel_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldLabel.class);
    _jspx_th_rn_sampleFieldLabel_2.setPageContext(_jspx_page_context);
    _jspx_th_rn_sampleFieldLabel_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_1);
    int _jspx_eval_rn_sampleFieldLabel_2 = _jspx_th_rn_sampleFieldLabel_2.doStartTag();
    if (_jspx_th_rn_sampleFieldLabel_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_sampleFieldLabel_nobody.reuse(_jspx_th_rn_sampleFieldLabel_2);
    return false;
  }

  private boolean _jspx_meth_rn_sampleField_2(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_authorizationChecker_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:sampleField
    org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_2 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_displayAsLabel_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
    _jspx_th_rn_sampleField_2.setPageContext(_jspx_page_context);
    _jspx_th_rn_sampleField_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_1);
    _jspx_th_rn_sampleField_2.setDisplayAsLabel(true);
    int _jspx_eval_rn_sampleField_2 = _jspx_th_rn_sampleField_2.doStartTag();
    if (_jspx_th_rn_sampleField_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_sampleField_displayAsLabel_nobody.reuse(_jspx_th_rn_sampleField_2);
    return false;
  }

  private boolean _jspx_meth_rn_sampleFieldUnits_2(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_authorizationChecker_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:sampleFieldUnits
    org.recipnet.site.content.rncontrols.SampleFieldUnits _jspx_th_rn_sampleFieldUnits_2 = (org.recipnet.site.content.rncontrols.SampleFieldUnits) _jspx_tagPool_rn_sampleFieldUnits_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldUnits.class);
    _jspx_th_rn_sampleFieldUnits_2.setPageContext(_jspx_page_context);
    _jspx_th_rn_sampleFieldUnits_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_1);
    int _jspx_eval_rn_sampleFieldUnits_2 = _jspx_th_rn_sampleFieldUnits_2.doStartTag();
    if (_jspx_th_rn_sampleFieldUnits_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_sampleFieldUnits_nobody.reuse(_jspx_th_rn_sampleFieldUnits_2);
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

  private boolean _jspx_meth_rn_labField_1(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_showSamplePage_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:labField
    org.recipnet.site.content.rncontrols.LabField _jspx_th_rn_labField_1 = (org.recipnet.site.content.rncontrols.LabField) _jspx_tagPool_rn_labField_nobody.get(org.recipnet.site.content.rncontrols.LabField.class);
    _jspx_th_rn_labField_1.setPageContext(_jspx_page_context);
    _jspx_th_rn_labField_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_showSamplePage_0);
    int _jspx_eval_rn_labField_1 = _jspx_th_rn_labField_1.doStartTag();
    if (_jspx_th_rn_labField_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_labField_nobody.reuse(_jspx_th_rn_labField_1);
    return false;
  }

  private boolean _jspx_meth_rn_providerField_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_showSamplePage_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:providerField
    org.recipnet.site.content.rncontrols.ProviderField _jspx_th_rn_providerField_0 = (org.recipnet.site.content.rncontrols.ProviderField) _jspx_tagPool_rn_providerField_nobody.get(org.recipnet.site.content.rncontrols.ProviderField.class);
    _jspx_th_rn_providerField_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_providerField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_showSamplePage_0);
    int _jspx_eval_rn_providerField_0 = _jspx_th_rn_providerField_0.doStartTag();
    if (_jspx_th_rn_providerField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_providerField_nobody.reuse(_jspx_th_rn_providerField_0);
    return false;
  }

  private boolean _jspx_meth_rn_sampleFieldLabel_3(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_ltaIterator_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:sampleFieldLabel
    org.recipnet.site.content.rncontrols.SampleFieldLabel _jspx_th_rn_sampleFieldLabel_3 = (org.recipnet.site.content.rncontrols.SampleFieldLabel) _jspx_tagPool_rn_sampleFieldLabel_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldLabel.class);
    _jspx_th_rn_sampleFieldLabel_3.setPageContext(_jspx_page_context);
    _jspx_th_rn_sampleFieldLabel_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_ltaIterator_0);
    int _jspx_eval_rn_sampleFieldLabel_3 = _jspx_th_rn_sampleFieldLabel_3.doStartTag();
    if (_jspx_th_rn_sampleFieldLabel_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_sampleFieldLabel_nobody.reuse(_jspx_th_rn_sampleFieldLabel_3);
    return false;
  }

  private boolean _jspx_meth_rn_sampleField_4(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_ltaIterator_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:sampleField
    org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_4 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_displayValueOnly_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
    _jspx_th_rn_sampleField_4.setPageContext(_jspx_page_context);
    _jspx_th_rn_sampleField_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_ltaIterator_0);
    _jspx_th_rn_sampleField_4.setDisplayValueOnly(true);
    int _jspx_eval_rn_sampleField_4 = _jspx_th_rn_sampleField_4.doStartTag();
    if (_jspx_th_rn_sampleField_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_sampleField_displayValueOnly_nobody.reuse(_jspx_th_rn_sampleField_4);
    return false;
  }

  private boolean _jspx_meth_rn_sampleFieldUnits_3(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_ltaIterator_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:sampleFieldUnits
    org.recipnet.site.content.rncontrols.SampleFieldUnits _jspx_th_rn_sampleFieldUnits_3 = (org.recipnet.site.content.rncontrols.SampleFieldUnits) _jspx_tagPool_rn_sampleFieldUnits_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldUnits.class);
    _jspx_th_rn_sampleFieldUnits_3.setPageContext(_jspx_page_context);
    _jspx_th_rn_sampleFieldUnits_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_ltaIterator_0);
    int _jspx_eval_rn_sampleFieldUnits_3 = _jspx_th_rn_sampleFieldUnits_3.doStartTag();
    if (_jspx_th_rn_sampleFieldUnits_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_sampleFieldUnits_nobody.reuse(_jspx_th_rn_sampleFieldUnits_3);
    return false;
  }

  private boolean _jspx_meth_rn_link_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_showSamplePage_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:link
    org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_0 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_href.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
    _jspx_th_rn_link_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_link_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_showSamplePage_0);
    _jspx_th_rn_link_0.setHref("/showsamplebasic.jsp");
    int _jspx_eval_rn_link_0 = _jspx_th_rn_link_0.doStartTag();
    if (_jspx_eval_rn_link_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_link_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_link_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_link_0.doInitBody();
      }
      do {
        out.write("Basic view...");
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

  private boolean _jspx_meth_rn_link_1(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_showSamplePage_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:link
    org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_1 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_href.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
    _jspx_th_rn_link_1.setPageContext(_jspx_page_context);
    _jspx_th_rn_link_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_showSamplePage_0);
    _jspx_th_rn_link_1.setHref("/jamm.jsp");
    int _jspx_eval_rn_link_1 = _jspx_th_rn_link_1.doStartTag();
    if (_jspx_eval_rn_link_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_link_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_link_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_link_1.doInitBody();
      }
      do {
        out.write("More visualization options...");
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

  private boolean _jspx_meth_rn_link_2(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_showSamplePage_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:link
    org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_2 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_href.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
    _jspx_th_rn_link_2.setPageContext(_jspx_page_context);
    _jspx_th_rn_link_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_showSamplePage_0);
    _jspx_th_rn_link_2.setHref("/sampleversions.jsp");
    int _jspx_eval_rn_link_2 = _jspx_th_rn_link_2.doStartTag();
    if (_jspx_eval_rn_link_2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_link_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_link_2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_link_2.doInitBody();
      }
      do {
        out.write("See other versions...");
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

  private boolean _jspx_meth_rn_authorizationChecker_2(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_showSamplePage_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:authorizationChecker
    org.recipnet.site.content.rncontrols.AuthorizationChecker _jspx_th_rn_authorizationChecker_2 = (org.recipnet.site.content.rncontrols.AuthorizationChecker) _jspx_tagPool_rn_authorizationChecker_suppressRenderingOnly_canEditSample.get(org.recipnet.site.content.rncontrols.AuthorizationChecker.class);
    _jspx_th_rn_authorizationChecker_2.setPageContext(_jspx_page_context);
    _jspx_th_rn_authorizationChecker_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_showSamplePage_0);
    _jspx_th_rn_authorizationChecker_2.setSuppressRenderingOnly(true);
    _jspx_th_rn_authorizationChecker_2.setCanEditSample(true);
    int _jspx_eval_rn_authorizationChecker_2 = _jspx_th_rn_authorizationChecker_2.doStartTag();
    if (_jspx_eval_rn_authorizationChecker_2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_authorizationChecker_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_authorizationChecker_2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_authorizationChecker_2.doInitBody();
      }
      do {
        out.write("\n        ");
        if (_jspx_meth_rn_link_3(_jspx_th_rn_authorizationChecker_2, _jspx_page_context))
          return true;
        out.write("\n        <br />\n      ");
        int evalDoAfterBody = _jspx_th_rn_authorizationChecker_2.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_authorizationChecker_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_authorizationChecker_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_authorizationChecker_suppressRenderingOnly_canEditSample.reuse(_jspx_th_rn_authorizationChecker_2);
    return false;
  }

  private boolean _jspx_meth_rn_link_3(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_authorizationChecker_2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:link
    org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_3 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_href.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
    _jspx_th_rn_link_3.setPageContext(_jspx_page_context);
    _jspx_th_rn_link_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_2);
    _jspx_th_rn_link_3.setHref("/lab/sample.jsp");
    int _jspx_eval_rn_link_3 = _jspx_th_rn_link_3.doStartTag();
    if (_jspx_eval_rn_link_3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_link_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_link_3.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_link_3.doInitBody();
      }
      do {
        out.write("Edit this sample...");
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
        out.write("\n        <tr>\n          <th colspan=\"5\">Repository Files:</th>\n      ");
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

  private boolean _jspx_meth_ctl_parityChecker_1(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_fileIterator_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:parityChecker
    org.recipnet.common.controls.ParityChecker _jspx_th_ctl_parityChecker_1 = (org.recipnet.common.controls.ParityChecker) _jspx_tagPool_ctl_parityChecker_includeOnlyOnMultiplesOf.get(org.recipnet.common.controls.ParityChecker.class);
    _jspx_th_ctl_parityChecker_1.setPageContext(_jspx_page_context);
    _jspx_th_ctl_parityChecker_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_fileIterator_0);
    _jspx_th_ctl_parityChecker_1.setIncludeOnlyOnMultiplesOf(5);
    int _jspx_eval_ctl_parityChecker_1 = _jspx_th_ctl_parityChecker_1.doStartTag();
    if (_jspx_eval_ctl_parityChecker_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_parityChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_parityChecker_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_parityChecker_1.doInitBody();
      }
      do {
        out.write("\n        </tr>\n        <tr>\n      ");
        int evalDoAfterBody = _jspx_th_ctl_parityChecker_1.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_parityChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_parityChecker_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_parityChecker_includeOnlyOnMultiplesOf.reuse(_jspx_th_ctl_parityChecker_1);
    return false;
  }

  private boolean _jspx_meth_ctl_parityChecker_2(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_fileIterator_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:parityChecker
    org.recipnet.common.controls.ParityChecker _jspx_th_ctl_parityChecker_2 = (org.recipnet.common.controls.ParityChecker) _jspx_tagPool_ctl_parityChecker_includeOnlyOnLast.get(org.recipnet.common.controls.ParityChecker.class);
    _jspx_th_ctl_parityChecker_2.setPageContext(_jspx_page_context);
    _jspx_th_ctl_parityChecker_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_fileIterator_0);
    _jspx_th_ctl_parityChecker_2.setIncludeOnlyOnLast(true);
    int _jspx_eval_ctl_parityChecker_2 = _jspx_th_ctl_parityChecker_2.doStartTag();
    if (_jspx_eval_ctl_parityChecker_2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_parityChecker_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_parityChecker_2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_parityChecker_2.doInitBody();
      }
      do {
        out.write("\n          </tr>\n        ");
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

  private boolean _jspx_meth_rn_link_4(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_errorMessage_4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:link
    org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_4 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_href.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
    _jspx_th_rn_link_4.setPageContext(_jspx_page_context);
    _jspx_th_rn_link_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_errorMessage_4);
    _jspx_th_rn_link_4.setHref("/showsampledetailed.jsp");
    int _jspx_eval_rn_link_4 = _jspx_th_rn_link_4.doStartTag();
    if (_jspx_eval_rn_link_4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_link_4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_link_4.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_link_4.doInitBody();
      }
      do {
        out.write("\n              Make them available...\n              ");
        if (_jspx_meth_ctl_linkParam_0(_jspx_th_rn_link_4, _jspx_page_context))
          return true;
        out.write("\n            ");
        int evalDoAfterBody = _jspx_th_rn_link_4.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_link_4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_link_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_link_href.reuse(_jspx_th_rn_link_4);
    return false;
  }

  private boolean _jspx_meth_ctl_linkParam_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_link_4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:linkParam
    org.recipnet.common.controls.LinkParam _jspx_th_ctl_linkParam_0 = (org.recipnet.common.controls.LinkParam) _jspx_tagPool_ctl_linkParam_value_name_nobody.get(org.recipnet.common.controls.LinkParam.class);
    _jspx_th_ctl_linkParam_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_linkParam_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_link_4);
    _jspx_th_ctl_linkParam_0.setName("makeFilesAvailable");
    _jspx_th_ctl_linkParam_0.setValue("true");
    int _jspx_eval_ctl_linkParam_0 = _jspx_th_ctl_linkParam_0.doStartTag();
    if (_jspx_th_ctl_linkParam_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_linkParam_value_name_nobody.reuse(_jspx_th_ctl_linkParam_0);
    return false;
  }

  private boolean _jspx_meth_ctl_styleBlock_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_showSamplePage_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:styleBlock
    org.recipnet.common.controls.HtmlPageStyleBlock _jspx_th_ctl_styleBlock_0 = (org.recipnet.common.controls.HtmlPageStyleBlock) _jspx_tagPool_ctl_styleBlock.get(org.recipnet.common.controls.HtmlPageStyleBlock.class);
    _jspx_th_ctl_styleBlock_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_styleBlock_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_showSamplePage_0);
    int _jspx_eval_ctl_styleBlock_0 = _jspx_th_ctl_styleBlock_0.doStartTag();
    if (_jspx_eval_ctl_styleBlock_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_styleBlock_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_styleBlock_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_styleBlock_0.doInitBody();
      }
      do {
        out.write("\n    div.pageBody { margin-top: 0.75em; text-align: inherit; }\n    table.dataTable { margin-left: 1.5em }\n    table.dataTable th { vertical-align: baseline; text-align: right;\n        font-style: narrow; font-weight: normal; color: gray;\n        white-space: nowrap; padding: 0;}\n    table.dataTable td { vertical-align: baseline; text-align: left;\n        color: #000040; padding: 0 0 0 0.25em;}\n    .dataTableUnits { color: gray; }\n    table.fileTable { clear: both; margin-top: 0.5em; }\n    table.fileTable th,\n    table.fileTable td { vertical-align: top; text-align: left; padding: 0.1em;}\n    table.fileTable th { background-color: #f4f4ff; }\n    table.fileTable td { width: 20%; background-color: #f4f4f4; }\n    .description { font: italic small Times, serif; color: gray; }\n    td.navLinks { padding-top: 1em; text-align: right; }\n  ");
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
