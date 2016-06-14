package org.recipnet.site.content;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.recipnet.site.content.rncontrols.ProviderField;
import org.recipnet.site.content.rncontrols.SearchPage;
import org.recipnet.site.content.rncontrols.SearchParamsField;
import org.recipnet.site.content.rncontrols.UnitCellSearchField;
import org.recipnet.site.content.rncontrols.UnitCellSearchFieldGroup;
import org.recipnet.site.shared.bl.SampleTextBL;

public final class search_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static java.util.Vector _jspx_dependants;

  static {
    _jspx_dependants = new java.util.Vector(2);
    _jspx_dependants.add("/WEB-INF/controls.tld");
    _jspx_dependants.add("/WEB-INF/rncontrols.tld");
  }

  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_searchPage_title_searchResultsPage;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_errorMessage;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_selfForm;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_searchParamsField_tabIndex_id_fieldCode_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_authorizationChecker_canSeeProviderListForLab;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_authorizationChecker_canFilterResultsByOwnProvider;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_providerField_fieldCode_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_unitCellSearchFieldGroup_id;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_unitCellSearchField_tabIndex_fieldCode_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_errorMessage_errorFilter;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_radioButtonGroup_initialValue;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_unitCellSearchField_tabIndex_id_fieldCode_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_iterator_iterations_id;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_searchParamsField_tabIndex_groupKey_fieldCode_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_searchParamsField_tabIndex_fieldCode_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_radioButtonGroup_initialValue_id;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_listbox_initialValue_id;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_option;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_searchButton_label_id_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_extraHtmlAttribute_value_name_attributeAccepter_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_styleBlock;

  public java.util.List getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _jspx_tagPool_rn_searchPage_title_searchResultsPage = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_errorMessage = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_selfForm = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_searchParamsField_tabIndex_id_fieldCode_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_authorizationChecker_canSeeProviderListForLab = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_authorizationChecker_canFilterResultsByOwnProvider = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_providerField_fieldCode_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_unitCellSearchFieldGroup_id = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_unitCellSearchField_tabIndex_fieldCode_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_errorMessage_errorFilter = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_radioButtonGroup_initialValue = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_unitCellSearchField_tabIndex_id_fieldCode_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_iterator_iterations_id = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_searchParamsField_tabIndex_groupKey_fieldCode_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_searchParamsField_tabIndex_fieldCode_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_radioButtonGroup_initialValue_id = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_listbox_initialValue_id = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_option = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_searchButton_label_id_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_extraHtmlAttribute_value_name_attributeAccepter_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_styleBlock = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
  }

  public void _jspDestroy() {
    _jspx_tagPool_rn_searchPage_title_searchResultsPage.release();
    _jspx_tagPool_ctl_errorMessage.release();
    _jspx_tagPool_ctl_selfForm.release();
    _jspx_tagPool_rn_searchParamsField_tabIndex_id_fieldCode_nobody.release();
    _jspx_tagPool_rn_authorizationChecker_canSeeProviderListForLab.release();
    _jspx_tagPool_rn_authorizationChecker_canFilterResultsByOwnProvider.release();
    _jspx_tagPool_rn_providerField_fieldCode_nobody.release();
    _jspx_tagPool_rn_unitCellSearchFieldGroup_id.release();
    _jspx_tagPool_rn_unitCellSearchField_tabIndex_fieldCode_nobody.release();
    _jspx_tagPool_ctl_errorMessage_errorFilter.release();
    _jspx_tagPool_ctl_radioButtonGroup_initialValue.release();
    _jspx_tagPool_rn_unitCellSearchField_tabIndex_id_fieldCode_nobody.release();
    _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.release();
    _jspx_tagPool_ctl_iterator_iterations_id.release();
    _jspx_tagPool_rn_searchParamsField_tabIndex_groupKey_fieldCode_nobody.release();
    _jspx_tagPool_rn_searchParamsField_tabIndex_fieldCode_nobody.release();
    _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.release();
    _jspx_tagPool_ctl_radioButtonGroup_initialValue_id.release();
    _jspx_tagPool_ctl_listbox_initialValue_id.release();
    _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.release();
    _jspx_tagPool_ctl_option.release();
    _jspx_tagPool_rn_searchButton_label_id_nobody.release();
    _jspx_tagPool_ctl_extraHtmlAttribute_value_name_attributeAccepter_nobody.release();
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
      //  rn:searchPage
      org.recipnet.site.content.rncontrols.SearchPage _jspx_th_rn_searchPage_0 = (org.recipnet.site.content.rncontrols.SearchPage) _jspx_tagPool_rn_searchPage_title_searchResultsPage.get(org.recipnet.site.content.rncontrols.SearchPage.class);
      _jspx_th_rn_searchPage_0.setPageContext(_jspx_page_context);
      _jspx_th_rn_searchPage_0.setParent(null);
      _jspx_th_rn_searchPage_0.setTitle("Site Search");
      _jspx_th_rn_searchPage_0.setSearchResultsPage("/searchresults.jsp");
      int _jspx_eval_rn_searchPage_0 = _jspx_th_rn_searchPage_0.doStartTag();
      if (_jspx_eval_rn_searchPage_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        org.recipnet.site.content.rncontrols.SearchPage htmlPage = null;
        if (_jspx_eval_rn_searchPage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
          out = _jspx_page_context.pushBody();
          _jspx_th_rn_searchPage_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
          _jspx_th_rn_searchPage_0.doInitBody();
        }
        htmlPage = (org.recipnet.site.content.rncontrols.SearchPage) _jspx_page_context.findAttribute("htmlPage");
        do {
          out.write("\n  <div style=\"margin-top: 1em;\">\n  ");
          if (_jspx_meth_ctl_errorMessage_0(_jspx_th_rn_searchPage_0, _jspx_page_context))
            return;
          out.write('\n');
          out.write(' ');
          out.write(' ');
          //  ctl:selfForm
          org.recipnet.common.controls.FormHtmlElement _jspx_th_ctl_selfForm_0 = (org.recipnet.common.controls.FormHtmlElement) _jspx_tagPool_ctl_selfForm.get(org.recipnet.common.controls.FormHtmlElement.class);
          _jspx_th_ctl_selfForm_0.setPageContext(_jspx_page_context);
          _jspx_th_ctl_selfForm_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_searchPage_0);
          int _jspx_eval_ctl_selfForm_0 = _jspx_th_ctl_selfForm_0.doStartTag();
          if (_jspx_eval_ctl_selfForm_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_ctl_selfForm_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_ctl_selfForm_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_ctl_selfForm_0.doInitBody();
            }
            do {
              out.write("\n    <table class=\"searchTable\" align=\"center\">\n      <tr>\n        <th colspan=\"4\" class=\"sectionHeader\">Sample Identification</th>\n      </tr>\n      <tr>\n        <th>\n          Sample number:\n        </th>\n        <td>\n          ");
              //  rn:searchParamsField
              org.recipnet.site.content.rncontrols.SearchParamsField localSampleId = null;
              org.recipnet.site.content.rncontrols.SearchParamsField _jspx_th_rn_searchParamsField_0 = (org.recipnet.site.content.rncontrols.SearchParamsField) _jspx_tagPool_rn_searchParamsField_tabIndex_id_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SearchParamsField.class);
              _jspx_th_rn_searchParamsField_0.setPageContext(_jspx_page_context);
              _jspx_th_rn_searchParamsField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_searchParamsField_0.setTabIndex("1");
              _jspx_th_rn_searchParamsField_0.setId("localSampleId");
              _jspx_th_rn_searchParamsField_0.setFieldCode( SearchParamsField.LOCAL_SAMPLE_ID );
              int _jspx_eval_rn_searchParamsField_0 = _jspx_th_rn_searchParamsField_0.doStartTag();
              if (_jspx_th_rn_searchParamsField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              localSampleId = (org.recipnet.site.content.rncontrols.SearchParamsField) _jspx_page_context.findAttribute("localSampleId");
              _jspx_tagPool_rn_searchParamsField_tabIndex_id_fieldCode_nobody.reuse(_jspx_th_rn_searchParamsField_0);
              out.write("\n        </td>\n        <th>\n          Laboratory:\n        </th>\n        <td>\n          ");
              //  rn:searchParamsField
              org.recipnet.site.content.rncontrols.SearchParamsField lab = null;
              org.recipnet.site.content.rncontrols.SearchParamsField _jspx_th_rn_searchParamsField_1 = (org.recipnet.site.content.rncontrols.SearchParamsField) _jspx_tagPool_rn_searchParamsField_tabIndex_id_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SearchParamsField.class);
              _jspx_th_rn_searchParamsField_1.setPageContext(_jspx_page_context);
              _jspx_th_rn_searchParamsField_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_searchParamsField_1.setTabIndex("2");
              _jspx_th_rn_searchParamsField_1.setId("lab");
              _jspx_th_rn_searchParamsField_1.setFieldCode( SearchParamsField.LAB_SEARCH );
              int _jspx_eval_rn_searchParamsField_1 = _jspx_th_rn_searchParamsField_1.doStartTag();
              if (_jspx_th_rn_searchParamsField_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              lab = (org.recipnet.site.content.rncontrols.SearchParamsField) _jspx_page_context.findAttribute("lab");
              _jspx_tagPool_rn_searchParamsField_tabIndex_id_fieldCode_nobody.reuse(_jspx_th_rn_searchParamsField_1);
              out.write("\n        </td>\n      </tr> \n      ");
              //  rn:authorizationChecker
              org.recipnet.site.content.rncontrols.AuthorizationChecker _jspx_th_rn_authorizationChecker_0 = (org.recipnet.site.content.rncontrols.AuthorizationChecker) _jspx_tagPool_rn_authorizationChecker_canSeeProviderListForLab.get(org.recipnet.site.content.rncontrols.AuthorizationChecker.class);
              _jspx_th_rn_authorizationChecker_0.setPageContext(_jspx_page_context);
              _jspx_th_rn_authorizationChecker_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_authorizationChecker_0.setCanSeeProviderListForLab(true);
              int _jspx_eval_rn_authorizationChecker_0 = _jspx_th_rn_authorizationChecker_0.doStartTag();
              if (_jspx_eval_rn_authorizationChecker_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_rn_authorizationChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_rn_authorizationChecker_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_rn_authorizationChecker_0.doInitBody();
                }
                do {
                  out.write("\n        <tr>\n          <th>Submitted by group:</th>\n          <td>\n            ");
                  //  rn:searchParamsField
                  org.recipnet.site.content.rncontrols.SearchParamsField provider = null;
                  org.recipnet.site.content.rncontrols.SearchParamsField _jspx_th_rn_searchParamsField_2 = (org.recipnet.site.content.rncontrols.SearchParamsField) _jspx_tagPool_rn_searchParamsField_tabIndex_id_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SearchParamsField.class);
                  _jspx_th_rn_searchParamsField_2.setPageContext(_jspx_page_context);
                  _jspx_th_rn_searchParamsField_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_0);
                  _jspx_th_rn_searchParamsField_2.setTabIndex("3");
                  _jspx_th_rn_searchParamsField_2.setId("provider");
                  _jspx_th_rn_searchParamsField_2.setFieldCode( SearchParamsField.PROVIDER );
                  int _jspx_eval_rn_searchParamsField_2 = _jspx_th_rn_searchParamsField_2.doStartTag();
                  if (_jspx_th_rn_searchParamsField_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  provider = (org.recipnet.site.content.rncontrols.SearchParamsField) _jspx_page_context.findAttribute("provider");
                  _jspx_tagPool_rn_searchParamsField_tabIndex_id_fieldCode_nobody.reuse(_jspx_th_rn_searchParamsField_2);
                  out.write("\n          </td>\n          <th>\n            Submitted by individual:\n          </th>\n          <td>\n            ");
                  //  rn:searchParamsField
                  org.recipnet.site.content.rncontrols.SearchParamsField sampleProviderName = null;
                  org.recipnet.site.content.rncontrols.SearchParamsField _jspx_th_rn_searchParamsField_3 = (org.recipnet.site.content.rncontrols.SearchParamsField) _jspx_tagPool_rn_searchParamsField_tabIndex_id_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SearchParamsField.class);
                  _jspx_th_rn_searchParamsField_3.setPageContext(_jspx_page_context);
                  _jspx_th_rn_searchParamsField_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_0);
                  _jspx_th_rn_searchParamsField_3.setTabIndex("3");
                  _jspx_th_rn_searchParamsField_3.setId("sampleProviderName");
                  _jspx_th_rn_searchParamsField_3.setFieldCode( SearchParamsField.SAMPLE_PROVIDER_NAME );
                  int _jspx_eval_rn_searchParamsField_3 = _jspx_th_rn_searchParamsField_3.doStartTag();
                  if (_jspx_th_rn_searchParamsField_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  sampleProviderName = (org.recipnet.site.content.rncontrols.SearchParamsField) _jspx_page_context.findAttribute("sampleProviderName");
                  _jspx_tagPool_rn_searchParamsField_tabIndex_id_fieldCode_nobody.reuse(_jspx_th_rn_searchParamsField_3);
                  out.write("\n          </td>\n        </tr> \n      ");
                  int evalDoAfterBody = _jspx_th_rn_authorizationChecker_0.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_rn_authorizationChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_rn_authorizationChecker_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_authorizationChecker_canSeeProviderListForLab.reuse(_jspx_th_rn_authorizationChecker_0);
              out.write("\n      ");
              //  rn:authorizationChecker
              org.recipnet.site.content.rncontrols.AuthorizationChecker _jspx_th_rn_authorizationChecker_1 = (org.recipnet.site.content.rncontrols.AuthorizationChecker) _jspx_tagPool_rn_authorizationChecker_canFilterResultsByOwnProvider.get(org.recipnet.site.content.rncontrols.AuthorizationChecker.class);
              _jspx_th_rn_authorizationChecker_1.setPageContext(_jspx_page_context);
              _jspx_th_rn_authorizationChecker_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_authorizationChecker_1.setCanFilterResultsByOwnProvider(true);
              int _jspx_eval_rn_authorizationChecker_1 = _jspx_th_rn_authorizationChecker_1.doStartTag();
              if (_jspx_eval_rn_authorizationChecker_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_rn_authorizationChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_rn_authorizationChecker_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_rn_authorizationChecker_1.doInitBody();
                }
                do {
                  out.write("\n        <tr>\n          <th>\n            Submitted by group:\n          </th>\n          <td>\n            ");
                  //  rn:searchParamsField
                  org.recipnet.site.content.rncontrols.SearchParamsField useUserProvider = null;
                  org.recipnet.site.content.rncontrols.SearchParamsField _jspx_th_rn_searchParamsField_4 = (org.recipnet.site.content.rncontrols.SearchParamsField) _jspx_tagPool_rn_searchParamsField_tabIndex_id_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SearchParamsField.class);
                  _jspx_th_rn_searchParamsField_4.setPageContext(_jspx_page_context);
                  _jspx_th_rn_searchParamsField_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_1);
                  _jspx_th_rn_searchParamsField_4.setTabIndex("4");
                  _jspx_th_rn_searchParamsField_4.setId("useUserProvider");
                  _jspx_th_rn_searchParamsField_4.setFieldCode( SearchParamsField.USE_USER_PROVIDER );
                  int _jspx_eval_rn_searchParamsField_4 = _jspx_th_rn_searchParamsField_4.doStartTag();
                  if (_jspx_th_rn_searchParamsField_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  useUserProvider = (org.recipnet.site.content.rncontrols.SearchParamsField) _jspx_page_context.findAttribute("useUserProvider");
                  _jspx_tagPool_rn_searchParamsField_tabIndex_id_fieldCode_nobody.reuse(_jspx_th_rn_searchParamsField_4);
                  out.write("\n            my group (");
                  //  rn:providerField
                  org.recipnet.site.content.rncontrols.ProviderField _jspx_th_rn_providerField_0 = (org.recipnet.site.content.rncontrols.ProviderField) _jspx_tagPool_rn_providerField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.ProviderField.class);
                  _jspx_th_rn_providerField_0.setPageContext(_jspx_page_context);
                  _jspx_th_rn_providerField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_1);
                  _jspx_th_rn_providerField_0.setFieldCode(ProviderField.NAME);
                  int _jspx_eval_rn_providerField_0 = _jspx_th_rn_providerField_0.doStartTag();
                  if (_jspx_th_rn_providerField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_providerField_fieldCode_nobody.reuse(_jspx_th_rn_providerField_0);
                  out.write(")\n          </td>\n          <th>\n            Submitted by individual:\n          </th>\n          <td>\n            ");
                  //  rn:searchParamsField
                  org.recipnet.site.content.rncontrols.SearchParamsField sampleProviderName = null;
                  org.recipnet.site.content.rncontrols.SearchParamsField _jspx_th_rn_searchParamsField_5 = (org.recipnet.site.content.rncontrols.SearchParamsField) _jspx_tagPool_rn_searchParamsField_tabIndex_id_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SearchParamsField.class);
                  _jspx_th_rn_searchParamsField_5.setPageContext(_jspx_page_context);
                  _jspx_th_rn_searchParamsField_5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_1);
                  _jspx_th_rn_searchParamsField_5.setTabIndex("4");
                  _jspx_th_rn_searchParamsField_5.setId("sampleProviderName");
                  _jspx_th_rn_searchParamsField_5.setFieldCode( SearchParamsField.SAMPLE_PROVIDER_NAME );
                  int _jspx_eval_rn_searchParamsField_5 = _jspx_th_rn_searchParamsField_5.doStartTag();
                  if (_jspx_th_rn_searchParamsField_5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  sampleProviderName = (org.recipnet.site.content.rncontrols.SearchParamsField) _jspx_page_context.findAttribute("sampleProviderName");
                  _jspx_tagPool_rn_searchParamsField_tabIndex_id_fieldCode_nobody.reuse(_jspx_th_rn_searchParamsField_5);
                  out.write("\n          </td>\n        </tr>\n      ");
                  int evalDoAfterBody = _jspx_th_rn_authorizationChecker_1.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_rn_authorizationChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_rn_authorizationChecker_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_authorizationChecker_canFilterResultsByOwnProvider.reuse(_jspx_th_rn_authorizationChecker_1);
              out.write("\n      <tr>\n        <th>Crystallographer:</th>\n        <td colspan=\"3\">\n          ");
              //  rn:searchParamsField
              org.recipnet.site.content.rncontrols.SearchParamsField crystallographer = null;
              org.recipnet.site.content.rncontrols.SearchParamsField _jspx_th_rn_searchParamsField_6 = (org.recipnet.site.content.rncontrols.SearchParamsField) _jspx_tagPool_rn_searchParamsField_tabIndex_id_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SearchParamsField.class);
              _jspx_th_rn_searchParamsField_6.setPageContext(_jspx_page_context);
              _jspx_th_rn_searchParamsField_6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_searchParamsField_6.setTabIndex("5");
              _jspx_th_rn_searchParamsField_6.setId("crystallographer");
              _jspx_th_rn_searchParamsField_6.setFieldCode( SearchParamsField.CRYSTALLOGRAPHER );
              int _jspx_eval_rn_searchParamsField_6 = _jspx_th_rn_searchParamsField_6.doStartTag();
              if (_jspx_th_rn_searchParamsField_6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              crystallographer = (org.recipnet.site.content.rncontrols.SearchParamsField) _jspx_page_context.findAttribute("crystallographer");
              _jspx_tagPool_rn_searchParamsField_tabIndex_id_fieldCode_nobody.reuse(_jspx_th_rn_searchParamsField_6);
              out.write("\n        </td>\n      </tr>\n      <tr>\n        <th valign=\"top\">\n          Constrain search to:\n        </th>\n        <td colspan=\"3\">\n          ");
              //  rn:searchParamsField
              org.recipnet.site.content.rncontrols.SearchParamsField requireLocalHolding = null;
              org.recipnet.site.content.rncontrols.SearchParamsField _jspx_th_rn_searchParamsField_7 = (org.recipnet.site.content.rncontrols.SearchParamsField) _jspx_tagPool_rn_searchParamsField_tabIndex_id_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SearchParamsField.class);
              _jspx_th_rn_searchParamsField_7.setPageContext(_jspx_page_context);
              _jspx_th_rn_searchParamsField_7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_searchParamsField_7.setTabIndex("6");
              _jspx_th_rn_searchParamsField_7.setId("requireLocalHolding");
              _jspx_th_rn_searchParamsField_7.setFieldCode( SearchParamsField.REQUIRE_LOCAL_HOLDING );
              int _jspx_eval_rn_searchParamsField_7 = _jspx_th_rn_searchParamsField_7.doStartTag();
              if (_jspx_th_rn_searchParamsField_7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              requireLocalHolding = (org.recipnet.site.content.rncontrols.SearchParamsField) _jspx_page_context.findAttribute("requireLocalHolding");
              _jspx_tagPool_rn_searchParamsField_tabIndex_id_fieldCode_nobody.reuse(_jspx_th_rn_searchParamsField_7);
              out.write("\n          samples with data at the local site\n          <br />\n          ");
              //  rn:searchParamsField
              org.recipnet.site.content.rncontrols.SearchParamsField requireTerminal = null;
              org.recipnet.site.content.rncontrols.SearchParamsField _jspx_th_rn_searchParamsField_8 = (org.recipnet.site.content.rncontrols.SearchParamsField) _jspx_tagPool_rn_searchParamsField_tabIndex_id_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SearchParamsField.class);
              _jspx_th_rn_searchParamsField_8.setPageContext(_jspx_page_context);
              _jspx_th_rn_searchParamsField_8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_searchParamsField_8.setTabIndex("6");
              _jspx_th_rn_searchParamsField_8.setId("requireTerminal");
              _jspx_th_rn_searchParamsField_8.setFieldCode( SearchParamsField.REQUIRE_TERMINAL_STATUS );
              int _jspx_eval_rn_searchParamsField_8 = _jspx_th_rn_searchParamsField_8.doStartTag();
              if (_jspx_th_rn_searchParamsField_8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              requireTerminal = (org.recipnet.site.content.rncontrols.SearchParamsField) _jspx_page_context.findAttribute("requireTerminal");
              _jspx_tagPool_rn_searchParamsField_tabIndex_id_fieldCode_nobody.reuse(_jspx_th_rn_searchParamsField_8);
              out.write("\n          samples for which processing has finished\n          <br />\n          ");
              //  rn:searchParamsField
              org.recipnet.site.content.rncontrols.SearchParamsField requireNonRetracted = null;
              org.recipnet.site.content.rncontrols.SearchParamsField _jspx_th_rn_searchParamsField_9 = (org.recipnet.site.content.rncontrols.SearchParamsField) _jspx_tagPool_rn_searchParamsField_tabIndex_id_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SearchParamsField.class);
              _jspx_th_rn_searchParamsField_9.setPageContext(_jspx_page_context);
              _jspx_th_rn_searchParamsField_9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_searchParamsField_9.setTabIndex("6");
              _jspx_th_rn_searchParamsField_9.setId("requireNonRetracted");
              _jspx_th_rn_searchParamsField_9.setFieldCode( SearchParamsField.REQUIRE_NON_RETRACTED );
              int _jspx_eval_rn_searchParamsField_9 = _jspx_th_rn_searchParamsField_9.doStartTag();
              if (_jspx_th_rn_searchParamsField_9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              requireNonRetracted = (org.recipnet.site.content.rncontrols.SearchParamsField) _jspx_page_context.findAttribute("requireNonRetracted");
              _jspx_tagPool_rn_searchParamsField_tabIndex_id_fieldCode_nobody.reuse(_jspx_th_rn_searchParamsField_9);
              out.write("\n          samples which have not been retracted previously\n        </td>\n      </tr>  \n      <tr>\n        <th colspan=\"4\" class=\"sectionHeader\">Names and Formulae</th>\n      </tr>\n      <tr>\n        <th>Compound Name: </th>\n        <td>\n          ");
              //  rn:searchParamsField
              org.recipnet.site.content.rncontrols.SearchParamsField chemName = null;
              org.recipnet.site.content.rncontrols.SearchParamsField _jspx_th_rn_searchParamsField_10 = (org.recipnet.site.content.rncontrols.SearchParamsField) _jspx_tagPool_rn_searchParamsField_tabIndex_id_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SearchParamsField.class);
              _jspx_th_rn_searchParamsField_10.setPageContext(_jspx_page_context);
              _jspx_th_rn_searchParamsField_10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_searchParamsField_10.setTabIndex("9");
              _jspx_th_rn_searchParamsField_10.setId("chemName");
              _jspx_th_rn_searchParamsField_10.setFieldCode( SearchParamsField.CHEM_NAME );
              int _jspx_eval_rn_searchParamsField_10 = _jspx_th_rn_searchParamsField_10.doStartTag();
              if (_jspx_th_rn_searchParamsField_10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              chemName = (org.recipnet.site.content.rncontrols.SearchParamsField) _jspx_page_context.findAttribute("chemName");
              _jspx_tagPool_rn_searchParamsField_tabIndex_id_fieldCode_nobody.reuse(_jspx_th_rn_searchParamsField_10);
              out.write("\n  \n        </td>\n        <th>Empirical Formula: </th>\n        <td>\n          ");
              //  rn:searchParamsField
              org.recipnet.site.content.rncontrols.SearchParamsField empirFormula = null;
              org.recipnet.site.content.rncontrols.SearchParamsField _jspx_th_rn_searchParamsField_11 = (org.recipnet.site.content.rncontrols.SearchParamsField) _jspx_tagPool_rn_searchParamsField_tabIndex_id_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SearchParamsField.class);
              _jspx_th_rn_searchParamsField_11.setPageContext(_jspx_page_context);
              _jspx_th_rn_searchParamsField_11.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_searchParamsField_11.setTabIndex("10");
              _jspx_th_rn_searchParamsField_11.setId("empirFormula");
              _jspx_th_rn_searchParamsField_11.setFieldCode( SearchParamsField.EMPIR_FORMULA );
              int _jspx_eval_rn_searchParamsField_11 = _jspx_th_rn_searchParamsField_11.doStartTag();
              if (_jspx_th_rn_searchParamsField_11.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              empirFormula = (org.recipnet.site.content.rncontrols.SearchParamsField) _jspx_page_context.findAttribute("empirFormula");
              _jspx_tagPool_rn_searchParamsField_tabIndex_id_fieldCode_nobody.reuse(_jspx_th_rn_searchParamsField_11);
              out.write("\n        </td>\n      </tr>\n      <tr>\n        <th>Structural Formula: </th>\n        <td>\n          ");
              //  rn:searchParamsField
              org.recipnet.site.content.rncontrols.SearchParamsField structFormula = null;
              org.recipnet.site.content.rncontrols.SearchParamsField _jspx_th_rn_searchParamsField_12 = (org.recipnet.site.content.rncontrols.SearchParamsField) _jspx_tagPool_rn_searchParamsField_tabIndex_id_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SearchParamsField.class);
              _jspx_th_rn_searchParamsField_12.setPageContext(_jspx_page_context);
              _jspx_th_rn_searchParamsField_12.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_searchParamsField_12.setTabIndex("11");
              _jspx_th_rn_searchParamsField_12.setId("structFormula");
              _jspx_th_rn_searchParamsField_12.setFieldCode( SearchParamsField.STRUCT_FORMULA );
              int _jspx_eval_rn_searchParamsField_12 = _jspx_th_rn_searchParamsField_12.doStartTag();
              if (_jspx_th_rn_searchParamsField_12.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              structFormula = (org.recipnet.site.content.rncontrols.SearchParamsField) _jspx_page_context.findAttribute("structFormula");
              _jspx_tagPool_rn_searchParamsField_tabIndex_id_fieldCode_nobody.reuse(_jspx_th_rn_searchParamsField_12);
              out.write("\n        </td>\n        <th>Moiety Formula: </th>\n        <td>\n          ");
              //  rn:searchParamsField
              org.recipnet.site.content.rncontrols.SearchParamsField moietyFormula = null;
              org.recipnet.site.content.rncontrols.SearchParamsField _jspx_th_rn_searchParamsField_13 = (org.recipnet.site.content.rncontrols.SearchParamsField) _jspx_tagPool_rn_searchParamsField_tabIndex_id_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SearchParamsField.class);
              _jspx_th_rn_searchParamsField_13.setPageContext(_jspx_page_context);
              _jspx_th_rn_searchParamsField_13.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_searchParamsField_13.setTabIndex("12");
              _jspx_th_rn_searchParamsField_13.setId("moietyFormula");
              _jspx_th_rn_searchParamsField_13.setFieldCode( SearchParamsField.MOIETY_FORMULA );
              int _jspx_eval_rn_searchParamsField_13 = _jspx_th_rn_searchParamsField_13.doStartTag();
              if (_jspx_th_rn_searchParamsField_13.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              moietyFormula = (org.recipnet.site.content.rncontrols.SearchParamsField) _jspx_page_context.findAttribute("moietyFormula");
              _jspx_tagPool_rn_searchParamsField_tabIndex_id_fieldCode_nobody.reuse(_jspx_th_rn_searchParamsField_13);
              out.write("\n        </td>\n      </tr>\n      <tr>\n        <th colspan=\"4\" class=\"sectionHeader\">\n          Unit Cell\n        </th>\n      </tr>\n      ");
              //  rn:unitCellSearchFieldGroup
              org.recipnet.site.content.rncontrols.UnitCellSearchFieldGroup ucSearch = null;
              org.recipnet.site.content.rncontrols.UnitCellSearchFieldGroup _jspx_th_rn_unitCellSearchFieldGroup_0 = (org.recipnet.site.content.rncontrols.UnitCellSearchFieldGroup) _jspx_tagPool_rn_unitCellSearchFieldGroup_id.get(org.recipnet.site.content.rncontrols.UnitCellSearchFieldGroup.class);
              _jspx_th_rn_unitCellSearchFieldGroup_0.setPageContext(_jspx_page_context);
              _jspx_th_rn_unitCellSearchFieldGroup_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_unitCellSearchFieldGroup_0.setId("ucSearch");
              int _jspx_eval_rn_unitCellSearchFieldGroup_0 = _jspx_th_rn_unitCellSearchFieldGroup_0.doStartTag();
              if (_jspx_eval_rn_unitCellSearchFieldGroup_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_rn_unitCellSearchFieldGroup_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_rn_unitCellSearchFieldGroup_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_rn_unitCellSearchFieldGroup_0.doInitBody();
                }
                ucSearch = (org.recipnet.site.content.rncontrols.UnitCellSearchFieldGroup) _jspx_page_context.findAttribute("ucSearch");
                do {
                  out.write("\n        <tr>\n          <th>a:</th>\n          <td>\n            ");
                  //  rn:unitCellSearchField
                  org.recipnet.site.content.rncontrols.UnitCellSearchField _jspx_th_rn_unitCellSearchField_0 = (org.recipnet.site.content.rncontrols.UnitCellSearchField) _jspx_tagPool_rn_unitCellSearchField_tabIndex_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.UnitCellSearchField.class);
                  _jspx_th_rn_unitCellSearchField_0.setPageContext(_jspx_page_context);
                  _jspx_th_rn_unitCellSearchField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_unitCellSearchFieldGroup_0);
                  _jspx_th_rn_unitCellSearchField_0.setTabIndex("14");
                  _jspx_th_rn_unitCellSearchField_0.setFieldCode( UnitCellSearchField.FieldCode.A );
                  int _jspx_eval_rn_unitCellSearchField_0 = _jspx_th_rn_unitCellSearchField_0.doStartTag();
                  if (_jspx_th_rn_unitCellSearchField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_unitCellSearchField_tabIndex_fieldCode_nobody.reuse(_jspx_th_rn_unitCellSearchField_0);
                  out.write("\n\t    ");
                  //  ctl:errorMessage
                  org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_1 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
                  _jspx_th_ctl_errorMessage_1.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_errorMessage_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_unitCellSearchFieldGroup_0);
                  _jspx_th_ctl_errorMessage_1.setErrorFilter(UnitCellSearchFieldGroup.A_MISSING);
                  int _jspx_eval_ctl_errorMessage_1 = _jspx_th_ctl_errorMessage_1.doStartTag();
                  if (_jspx_eval_ctl_errorMessage_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_errorMessage_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_errorMessage_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_errorMessage_1.doInitBody();
                    }
                    do {
                      out.write("\n              <span class=\"errorMessageSmall\">\n                * required for reduced cell search\n              </span>\n            ");
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
                  out.write("\n          </td>\n          <td colspan=\"2\" rowspan=\"7\" valign=\"top\">\n            ");
                  //  ctl:radioButtonGroup
                  org.recipnet.common.controls.RadioButtonGroupHtmlControl _jspx_th_ctl_radioButtonGroup_0 = (org.recipnet.common.controls.RadioButtonGroupHtmlControl) _jspx_tagPool_ctl_radioButtonGroup_initialValue.get(org.recipnet.common.controls.RadioButtonGroupHtmlControl.class);
                  _jspx_th_ctl_radioButtonGroup_0.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_radioButtonGroup_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_unitCellSearchFieldGroup_0);
                  _jspx_th_ctl_radioButtonGroup_0.setInitialValue(
                UnitCellSearchField.FieldCode.MATCH_UNIT_CELLS_AS_ENTERED.toString()
                );
                  int _jspx_eval_ctl_radioButtonGroup_0 = _jspx_th_ctl_radioButtonGroup_0.doStartTag();
                  if (_jspx_eval_ctl_radioButtonGroup_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_radioButtonGroup_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_radioButtonGroup_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_radioButtonGroup_0.doInitBody();
                    }
                    do {
                      out.write("\n            <div style=\"margin-bottom: 0.5em;\">\n            ");
                      //  rn:unitCellSearchField
                      org.recipnet.site.content.rncontrols.UnitCellSearchField enteredCell = null;
                      org.recipnet.site.content.rncontrols.UnitCellSearchField _jspx_th_rn_unitCellSearchField_1 = (org.recipnet.site.content.rncontrols.UnitCellSearchField) _jspx_tagPool_rn_unitCellSearchField_tabIndex_id_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.UnitCellSearchField.class);
                      _jspx_th_rn_unitCellSearchField_1.setPageContext(_jspx_page_context);
                      _jspx_th_rn_unitCellSearchField_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_radioButtonGroup_0);
                      _jspx_th_rn_unitCellSearchField_1.setTabIndex("21");
                      _jspx_th_rn_unitCellSearchField_1.setId("enteredCell");
                      _jspx_th_rn_unitCellSearchField_1.setFieldCode( 
               UnitCellSearchField.FieldCode.MATCH_UNIT_CELLS_AS_ENTERED );
                      int _jspx_eval_rn_unitCellSearchField_1 = _jspx_th_rn_unitCellSearchField_1.doStartTag();
                      if (_jspx_th_rn_unitCellSearchField_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      enteredCell = (org.recipnet.site.content.rncontrols.UnitCellSearchField) _jspx_page_context.findAttribute("enteredCell");
                      _jspx_tagPool_rn_unitCellSearchField_tabIndex_id_fieldCode_nobody.reuse(_jspx_th_rn_unitCellSearchField_1);
                      out.write("\n              match samples' unit cell parameters as they were entered\n            </div>\n            <div style=\"margin-top: 0.5em;\">\n              ");
                      //  rn:unitCellSearchField
                      org.recipnet.site.content.rncontrols.UnitCellSearchField reducedCell = null;
                      org.recipnet.site.content.rncontrols.UnitCellSearchField _jspx_th_rn_unitCellSearchField_2 = (org.recipnet.site.content.rncontrols.UnitCellSearchField) _jspx_tagPool_rn_unitCellSearchField_tabIndex_id_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.UnitCellSearchField.class);
                      _jspx_th_rn_unitCellSearchField_2.setPageContext(_jspx_page_context);
                      _jspx_th_rn_unitCellSearchField_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_radioButtonGroup_0);
                      _jspx_th_rn_unitCellSearchField_2.setTabIndex("21");
                      _jspx_th_rn_unitCellSearchField_2.setId("reducedCell");
                      _jspx_th_rn_unitCellSearchField_2.setFieldCode( 
                  UnitCellSearchField.FieldCode.MATCH_REDUCED_CELLS );
                      int _jspx_eval_rn_unitCellSearchField_2 = _jspx_th_rn_unitCellSearchField_2.doStartTag();
                      if (_jspx_th_rn_unitCellSearchField_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      reducedCell = (org.recipnet.site.content.rncontrols.UnitCellSearchField) _jspx_page_context.findAttribute("reducedCell");
                      _jspx_tagPool_rn_unitCellSearchField_tabIndex_id_fieldCode_nobody.reuse(_jspx_th_rn_unitCellSearchField_2);
                      out.write("\n              match samples' reduced cells; the specified cell (left) is\n              ");
                      int evalDoAfterBody = _jspx_th_ctl_radioButtonGroup_0.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_ctl_radioButtonGroup_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_ctl_radioButtonGroup_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_ctl_radioButtonGroup_initialValue.reuse(_jspx_th_ctl_radioButtonGroup_0);
                  out.write("\n              <div style=\"margin-top: 0.5em; margin-left: 3em;\">\n                ");
                  //  ctl:radioButtonGroup
                  org.recipnet.common.controls.RadioButtonGroupHtmlControl _jspx_th_ctl_radioButtonGroup_1 = (org.recipnet.common.controls.RadioButtonGroupHtmlControl) _jspx_tagPool_ctl_radioButtonGroup_initialValue.get(org.recipnet.common.controls.RadioButtonGroupHtmlControl.class);
                  _jspx_th_ctl_radioButtonGroup_1.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_radioButtonGroup_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_unitCellSearchFieldGroup_0);
                  _jspx_th_ctl_radioButtonGroup_1.setInitialValue(
                    UnitCellSearchField.FieldCode.P_CENTERING.toString() );
                  int _jspx_eval_ctl_radioButtonGroup_1 = _jspx_th_ctl_radioButtonGroup_1.doStartTag();
                  if (_jspx_eval_ctl_radioButtonGroup_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_radioButtonGroup_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_radioButtonGroup_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_radioButtonGroup_1.doInitBody();
                    }
                    do {
                      out.write("\n\t        ");
                      //  rn:unitCellSearchField
                      org.recipnet.site.content.rncontrols.UnitCellSearchField _jspx_th_rn_unitCellSearchField_3 = (org.recipnet.site.content.rncontrols.UnitCellSearchField) _jspx_tagPool_rn_unitCellSearchField_tabIndex_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.UnitCellSearchField.class);
                      _jspx_th_rn_unitCellSearchField_3.setPageContext(_jspx_page_context);
                      _jspx_th_rn_unitCellSearchField_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_radioButtonGroup_1);
                      _jspx_th_rn_unitCellSearchField_3.setTabIndex("22");
                      _jspx_th_rn_unitCellSearchField_3.setFieldCode(
                    UnitCellSearchField.FieldCode.P_CENTERING );
                      int _jspx_eval_rn_unitCellSearchField_3 = _jspx_th_rn_unitCellSearchField_3.doStartTag();
                      if (_jspx_th_rn_unitCellSearchField_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_rn_unitCellSearchField_tabIndex_fieldCode_nobody.reuse(_jspx_th_rn_unitCellSearchField_3);
                      out.write("\n                primitive<br />\n\t        ");
                      //  rn:unitCellSearchField
                      org.recipnet.site.content.rncontrols.UnitCellSearchField _jspx_th_rn_unitCellSearchField_4 = (org.recipnet.site.content.rncontrols.UnitCellSearchField) _jspx_tagPool_rn_unitCellSearchField_tabIndex_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.UnitCellSearchField.class);
                      _jspx_th_rn_unitCellSearchField_4.setPageContext(_jspx_page_context);
                      _jspx_th_rn_unitCellSearchField_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_radioButtonGroup_1);
                      _jspx_th_rn_unitCellSearchField_4.setTabIndex("22");
                      _jspx_th_rn_unitCellSearchField_4.setFieldCode(
                    UnitCellSearchField.FieldCode.A_CENTERING );
                      int _jspx_eval_rn_unitCellSearchField_4 = _jspx_th_rn_unitCellSearchField_4.doStartTag();
                      if (_jspx_th_rn_unitCellSearchField_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_rn_unitCellSearchField_tabIndex_fieldCode_nobody.reuse(_jspx_th_rn_unitCellSearchField_4);
                      out.write("\n                A centered<br/>\n\t        ");
                      //  rn:unitCellSearchField
                      org.recipnet.site.content.rncontrols.UnitCellSearchField _jspx_th_rn_unitCellSearchField_5 = (org.recipnet.site.content.rncontrols.UnitCellSearchField) _jspx_tagPool_rn_unitCellSearchField_tabIndex_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.UnitCellSearchField.class);
                      _jspx_th_rn_unitCellSearchField_5.setPageContext(_jspx_page_context);
                      _jspx_th_rn_unitCellSearchField_5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_radioButtonGroup_1);
                      _jspx_th_rn_unitCellSearchField_5.setTabIndex("22");
                      _jspx_th_rn_unitCellSearchField_5.setFieldCode(
                    UnitCellSearchField.FieldCode.B_CENTERING );
                      int _jspx_eval_rn_unitCellSearchField_5 = _jspx_th_rn_unitCellSearchField_5.doStartTag();
                      if (_jspx_th_rn_unitCellSearchField_5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_rn_unitCellSearchField_tabIndex_fieldCode_nobody.reuse(_jspx_th_rn_unitCellSearchField_5);
                      out.write("\n                B centered<br/>\n\t        ");
                      //  rn:unitCellSearchField
                      org.recipnet.site.content.rncontrols.UnitCellSearchField _jspx_th_rn_unitCellSearchField_6 = (org.recipnet.site.content.rncontrols.UnitCellSearchField) _jspx_tagPool_rn_unitCellSearchField_tabIndex_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.UnitCellSearchField.class);
                      _jspx_th_rn_unitCellSearchField_6.setPageContext(_jspx_page_context);
                      _jspx_th_rn_unitCellSearchField_6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_radioButtonGroup_1);
                      _jspx_th_rn_unitCellSearchField_6.setTabIndex("22");
                      _jspx_th_rn_unitCellSearchField_6.setFieldCode(
                    UnitCellSearchField.FieldCode.C_CENTERING );
                      int _jspx_eval_rn_unitCellSearchField_6 = _jspx_th_rn_unitCellSearchField_6.doStartTag();
                      if (_jspx_th_rn_unitCellSearchField_6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_rn_unitCellSearchField_tabIndex_fieldCode_nobody.reuse(_jspx_th_rn_unitCellSearchField_6);
                      out.write("\n                C centered<br/>\n\t        ");
                      //  rn:unitCellSearchField
                      org.recipnet.site.content.rncontrols.UnitCellSearchField _jspx_th_rn_unitCellSearchField_7 = (org.recipnet.site.content.rncontrols.UnitCellSearchField) _jspx_tagPool_rn_unitCellSearchField_tabIndex_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.UnitCellSearchField.class);
                      _jspx_th_rn_unitCellSearchField_7.setPageContext(_jspx_page_context);
                      _jspx_th_rn_unitCellSearchField_7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_radioButtonGroup_1);
                      _jspx_th_rn_unitCellSearchField_7.setTabIndex("22");
                      _jspx_th_rn_unitCellSearchField_7.setFieldCode(
                    UnitCellSearchField.FieldCode.F_CENTERING );
                      int _jspx_eval_rn_unitCellSearchField_7 = _jspx_th_rn_unitCellSearchField_7.doStartTag();
                      if (_jspx_th_rn_unitCellSearchField_7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_rn_unitCellSearchField_tabIndex_fieldCode_nobody.reuse(_jspx_th_rn_unitCellSearchField_7);
                      out.write("\n                F centered<br/>\n\t        ");
                      //  rn:unitCellSearchField
                      org.recipnet.site.content.rncontrols.UnitCellSearchField _jspx_th_rn_unitCellSearchField_8 = (org.recipnet.site.content.rncontrols.UnitCellSearchField) _jspx_tagPool_rn_unitCellSearchField_tabIndex_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.UnitCellSearchField.class);
                      _jspx_th_rn_unitCellSearchField_8.setPageContext(_jspx_page_context);
                      _jspx_th_rn_unitCellSearchField_8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_radioButtonGroup_1);
                      _jspx_th_rn_unitCellSearchField_8.setTabIndex("22");
                      _jspx_th_rn_unitCellSearchField_8.setFieldCode(
                    UnitCellSearchField.FieldCode.I_CENTERING );
                      int _jspx_eval_rn_unitCellSearchField_8 = _jspx_th_rn_unitCellSearchField_8.doStartTag();
                      if (_jspx_th_rn_unitCellSearchField_8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_rn_unitCellSearchField_tabIndex_fieldCode_nobody.reuse(_jspx_th_rn_unitCellSearchField_8);
                      out.write("\n                I centered<br/>\n\t        ");
                      //  rn:unitCellSearchField
                      org.recipnet.site.content.rncontrols.UnitCellSearchField _jspx_th_rn_unitCellSearchField_9 = (org.recipnet.site.content.rncontrols.UnitCellSearchField) _jspx_tagPool_rn_unitCellSearchField_tabIndex_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.UnitCellSearchField.class);
                      _jspx_th_rn_unitCellSearchField_9.setPageContext(_jspx_page_context);
                      _jspx_th_rn_unitCellSearchField_9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_radioButtonGroup_1);
                      _jspx_th_rn_unitCellSearchField_9.setTabIndex("22");
                      _jspx_th_rn_unitCellSearchField_9.setFieldCode(
                    UnitCellSearchField.FieldCode.R_CENTERING );
                      int _jspx_eval_rn_unitCellSearchField_9 = _jspx_th_rn_unitCellSearchField_9.doStartTag();
                      if (_jspx_th_rn_unitCellSearchField_9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_rn_unitCellSearchField_tabIndex_fieldCode_nobody.reuse(_jspx_th_rn_unitCellSearchField_9);
                      out.write("\n                R centered (obverse or reverse)\n                ");
                      int evalDoAfterBody = _jspx_th_ctl_radioButtonGroup_1.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_ctl_radioButtonGroup_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_ctl_radioButtonGroup_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_ctl_radioButtonGroup_initialValue.reuse(_jspx_th_ctl_radioButtonGroup_1);
                  out.write("\n              </div>\n            </div>\n          </td>\n        </tr>\n        <tr>\n          <th>b:</th>\n          <td>\n            ");
                  //  rn:unitCellSearchField
                  org.recipnet.site.content.rncontrols.UnitCellSearchField _jspx_th_rn_unitCellSearchField_10 = (org.recipnet.site.content.rncontrols.UnitCellSearchField) _jspx_tagPool_rn_unitCellSearchField_tabIndex_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.UnitCellSearchField.class);
                  _jspx_th_rn_unitCellSearchField_10.setPageContext(_jspx_page_context);
                  _jspx_th_rn_unitCellSearchField_10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_unitCellSearchFieldGroup_0);
                  _jspx_th_rn_unitCellSearchField_10.setTabIndex("15");
                  _jspx_th_rn_unitCellSearchField_10.setFieldCode( UnitCellSearchField.FieldCode.B );
                  int _jspx_eval_rn_unitCellSearchField_10 = _jspx_th_rn_unitCellSearchField_10.doStartTag();
                  if (_jspx_th_rn_unitCellSearchField_10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_unitCellSearchField_tabIndex_fieldCode_nobody.reuse(_jspx_th_rn_unitCellSearchField_10);
                  out.write("\n\t    ");
                  //  ctl:errorMessage
                  org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_2 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
                  _jspx_th_ctl_errorMessage_2.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_errorMessage_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_unitCellSearchFieldGroup_0);
                  _jspx_th_ctl_errorMessage_2.setErrorFilter(UnitCellSearchFieldGroup.B_MISSING);
                  int _jspx_eval_ctl_errorMessage_2 = _jspx_th_ctl_errorMessage_2.doStartTag();
                  if (_jspx_eval_ctl_errorMessage_2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_errorMessage_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_errorMessage_2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_errorMessage_2.doInitBody();
                    }
                    do {
                      out.write("\n              <span class=\"errorMessageSmall\">\n                * required for reduced cell search\n              </span>\n            ");
                      int evalDoAfterBody = _jspx_th_ctl_errorMessage_2.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_ctl_errorMessage_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_ctl_errorMessage_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_ctl_errorMessage_errorFilter.reuse(_jspx_th_ctl_errorMessage_2);
                  out.write("\n          </td>\n        </tr>\n        <tr>\n          <th>c:</th>\n          <td>\n            ");
                  //  rn:unitCellSearchField
                  org.recipnet.site.content.rncontrols.UnitCellSearchField _jspx_th_rn_unitCellSearchField_11 = (org.recipnet.site.content.rncontrols.UnitCellSearchField) _jspx_tagPool_rn_unitCellSearchField_tabIndex_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.UnitCellSearchField.class);
                  _jspx_th_rn_unitCellSearchField_11.setPageContext(_jspx_page_context);
                  _jspx_th_rn_unitCellSearchField_11.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_unitCellSearchFieldGroup_0);
                  _jspx_th_rn_unitCellSearchField_11.setTabIndex("16");
                  _jspx_th_rn_unitCellSearchField_11.setFieldCode( UnitCellSearchField.FieldCode.C );
                  int _jspx_eval_rn_unitCellSearchField_11 = _jspx_th_rn_unitCellSearchField_11.doStartTag();
                  if (_jspx_th_rn_unitCellSearchField_11.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_unitCellSearchField_tabIndex_fieldCode_nobody.reuse(_jspx_th_rn_unitCellSearchField_11);
                  out.write("\n\t    ");
                  //  ctl:errorMessage
                  org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_3 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
                  _jspx_th_ctl_errorMessage_3.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_errorMessage_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_unitCellSearchFieldGroup_0);
                  _jspx_th_ctl_errorMessage_3.setErrorFilter(UnitCellSearchFieldGroup.C_MISSING);
                  int _jspx_eval_ctl_errorMessage_3 = _jspx_th_ctl_errorMessage_3.doStartTag();
                  if (_jspx_eval_ctl_errorMessage_3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_errorMessage_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_errorMessage_3.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_errorMessage_3.doInitBody();
                    }
                    do {
                      out.write("\n              <span class=\"errorMessageSmall\">\n                * required for reduced cell search\n              </span>\n            ");
                      int evalDoAfterBody = _jspx_th_ctl_errorMessage_3.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_ctl_errorMessage_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_ctl_errorMessage_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_ctl_errorMessage_errorFilter.reuse(_jspx_th_ctl_errorMessage_3);
                  out.write("\n          </td>\n        </tr>\n        <tr>\n          <th>&alpha; (alpha):</th>\n          <td>\n            ");
                  //  rn:unitCellSearchField
                  org.recipnet.site.content.rncontrols.UnitCellSearchField _jspx_th_rn_unitCellSearchField_12 = (org.recipnet.site.content.rncontrols.UnitCellSearchField) _jspx_tagPool_rn_unitCellSearchField_tabIndex_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.UnitCellSearchField.class);
                  _jspx_th_rn_unitCellSearchField_12.setPageContext(_jspx_page_context);
                  _jspx_th_rn_unitCellSearchField_12.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_unitCellSearchFieldGroup_0);
                  _jspx_th_rn_unitCellSearchField_12.setTabIndex("17");
                  _jspx_th_rn_unitCellSearchField_12.setFieldCode( UnitCellSearchField.FieldCode.ALPHA );
                  int _jspx_eval_rn_unitCellSearchField_12 = _jspx_th_rn_unitCellSearchField_12.doStartTag();
                  if (_jspx_th_rn_unitCellSearchField_12.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_unitCellSearchField_tabIndex_fieldCode_nobody.reuse(_jspx_th_rn_unitCellSearchField_12);
                  out.write("\n\t    ");
                  //  ctl:errorMessage
                  org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_4 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
                  _jspx_th_ctl_errorMessage_4.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_errorMessage_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_unitCellSearchFieldGroup_0);
                  _jspx_th_ctl_errorMessage_4.setErrorFilter(UnitCellSearchFieldGroup.ALPHA_MISSING);
                  int _jspx_eval_ctl_errorMessage_4 = _jspx_th_ctl_errorMessage_4.doStartTag();
                  if (_jspx_eval_ctl_errorMessage_4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_errorMessage_4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_errorMessage_4.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_errorMessage_4.doInitBody();
                    }
                    do {
                      out.write("\n              <span class=\"errorMessageSmall\">\n                * required for reduced cell search\n              </span>\n            ");
                      int evalDoAfterBody = _jspx_th_ctl_errorMessage_4.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_ctl_errorMessage_4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_ctl_errorMessage_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_ctl_errorMessage_errorFilter.reuse(_jspx_th_ctl_errorMessage_4);
                  out.write("\n          </td>\n        </tr>\n        <tr>\n          <th>&beta; (beta):</th>\n          <td>\n            ");
                  //  rn:unitCellSearchField
                  org.recipnet.site.content.rncontrols.UnitCellSearchField _jspx_th_rn_unitCellSearchField_13 = (org.recipnet.site.content.rncontrols.UnitCellSearchField) _jspx_tagPool_rn_unitCellSearchField_tabIndex_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.UnitCellSearchField.class);
                  _jspx_th_rn_unitCellSearchField_13.setPageContext(_jspx_page_context);
                  _jspx_th_rn_unitCellSearchField_13.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_unitCellSearchFieldGroup_0);
                  _jspx_th_rn_unitCellSearchField_13.setTabIndex("18");
                  _jspx_th_rn_unitCellSearchField_13.setFieldCode( UnitCellSearchField.FieldCode.BETA );
                  int _jspx_eval_rn_unitCellSearchField_13 = _jspx_th_rn_unitCellSearchField_13.doStartTag();
                  if (_jspx_th_rn_unitCellSearchField_13.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_unitCellSearchField_tabIndex_fieldCode_nobody.reuse(_jspx_th_rn_unitCellSearchField_13);
                  out.write("\n\t    ");
                  //  ctl:errorMessage
                  org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_5 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
                  _jspx_th_ctl_errorMessage_5.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_errorMessage_5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_unitCellSearchFieldGroup_0);
                  _jspx_th_ctl_errorMessage_5.setErrorFilter(UnitCellSearchFieldGroup.BETA_MISSING);
                  int _jspx_eval_ctl_errorMessage_5 = _jspx_th_ctl_errorMessage_5.doStartTag();
                  if (_jspx_eval_ctl_errorMessage_5 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_errorMessage_5 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_errorMessage_5.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_errorMessage_5.doInitBody();
                    }
                    do {
                      out.write("\n              <span class=\"errorMessageSmall\">\n                * required for reduced cell search\n              </span>\n            ");
                      int evalDoAfterBody = _jspx_th_ctl_errorMessage_5.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_ctl_errorMessage_5 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_ctl_errorMessage_5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_ctl_errorMessage_errorFilter.reuse(_jspx_th_ctl_errorMessage_5);
                  out.write("\n          </td>\n        </tr>\n        <tr>\n          <th>&gamma; (gamma):</th>\n          <td>\n            ");
                  //  rn:unitCellSearchField
                  org.recipnet.site.content.rncontrols.UnitCellSearchField _jspx_th_rn_unitCellSearchField_14 = (org.recipnet.site.content.rncontrols.UnitCellSearchField) _jspx_tagPool_rn_unitCellSearchField_tabIndex_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.UnitCellSearchField.class);
                  _jspx_th_rn_unitCellSearchField_14.setPageContext(_jspx_page_context);
                  _jspx_th_rn_unitCellSearchField_14.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_unitCellSearchFieldGroup_0);
                  _jspx_th_rn_unitCellSearchField_14.setTabIndex("19");
                  _jspx_th_rn_unitCellSearchField_14.setFieldCode( UnitCellSearchField.FieldCode.GAMMA );
                  int _jspx_eval_rn_unitCellSearchField_14 = _jspx_th_rn_unitCellSearchField_14.doStartTag();
                  if (_jspx_th_rn_unitCellSearchField_14.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_unitCellSearchField_tabIndex_fieldCode_nobody.reuse(_jspx_th_rn_unitCellSearchField_14);
                  out.write("\n\t    ");
                  //  ctl:errorMessage
                  org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_6 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
                  _jspx_th_ctl_errorMessage_6.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_errorMessage_6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_unitCellSearchFieldGroup_0);
                  _jspx_th_ctl_errorMessage_6.setErrorFilter(UnitCellSearchFieldGroup.GAMMA_MISSING);
                  int _jspx_eval_ctl_errorMessage_6 = _jspx_th_ctl_errorMessage_6.doStartTag();
                  if (_jspx_eval_ctl_errorMessage_6 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_errorMessage_6 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_errorMessage_6.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_errorMessage_6.doInitBody();
                    }
                    do {
                      out.write("\n              <span class=\"errorMessageSmall\">\n                * required for reduced cell search\n              </span>\n            ");
                      int evalDoAfterBody = _jspx_th_ctl_errorMessage_6.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_ctl_errorMessage_6 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_ctl_errorMessage_6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_ctl_errorMessage_errorFilter.reuse(_jspx_th_ctl_errorMessage_6);
                  out.write("\n          </td>\n        </tr>\n        <tr>\n          <th>error tolerance:</th>\n          <td>\n\t    &nbsp;&#177;&nbsp;\n            ");
                  //  rn:unitCellSearchField
                  org.recipnet.site.content.rncontrols.UnitCellSearchField _jspx_th_rn_unitCellSearchField_15 = (org.recipnet.site.content.rncontrols.UnitCellSearchField) _jspx_tagPool_rn_unitCellSearchField_tabIndex_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.UnitCellSearchField.class);
                  _jspx_th_rn_unitCellSearchField_15.setPageContext(_jspx_page_context);
                  _jspx_th_rn_unitCellSearchField_15.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_unitCellSearchFieldGroup_0);
                  _jspx_th_rn_unitCellSearchField_15.setTabIndex("20");
                  _jspx_th_rn_unitCellSearchField_15.setFieldCode(
                UnitCellSearchField.FieldCode.PERCENT_ERROR_TOLERANCE );
                  int _jspx_eval_rn_unitCellSearchField_15 = _jspx_th_rn_unitCellSearchField_15.doStartTag();
                  if (_jspx_th_rn_unitCellSearchField_15.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_unitCellSearchField_tabIndex_fieldCode_nobody.reuse(_jspx_th_rn_unitCellSearchField_15);
                  out.write("%\n\t    ");
                  //  ctl:errorMessage
                  org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_7 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
                  _jspx_th_ctl_errorMessage_7.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_errorMessage_7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_unitCellSearchFieldGroup_0);
                  _jspx_th_ctl_errorMessage_7.setErrorFilter(UnitCellSearchFieldGroup.
                PERCENT_ERROR_TOLERANCE_MISSING);
                  int _jspx_eval_ctl_errorMessage_7 = _jspx_th_ctl_errorMessage_7.doStartTag();
                  if (_jspx_eval_ctl_errorMessage_7 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_errorMessage_7 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_errorMessage_7.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_errorMessage_7.doInitBody();
                    }
                    do {
                      out.write("\n              <span class=\"errorMessageSmall\">\n                * required\n              </span>\n            ");
                      int evalDoAfterBody = _jspx_th_ctl_errorMessage_7.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_ctl_errorMessage_7 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_ctl_errorMessage_7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_ctl_errorMessage_errorFilter.reuse(_jspx_th_ctl_errorMessage_7);
                  out.write("\n          </td>\n        </tr>\n      ");
                  int evalDoAfterBody = _jspx_th_rn_unitCellSearchFieldGroup_0.doAfterBody();
                  ucSearch = (org.recipnet.site.content.rncontrols.UnitCellSearchFieldGroup) _jspx_page_context.findAttribute("ucSearch");
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_rn_unitCellSearchFieldGroup_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_rn_unitCellSearchFieldGroup_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              ucSearch = (org.recipnet.site.content.rncontrols.UnitCellSearchFieldGroup) _jspx_page_context.findAttribute("ucSearch");
              _jspx_tagPool_rn_unitCellSearchFieldGroup_id.reuse(_jspx_th_rn_unitCellSearchFieldGroup_0);
              out.write("\n      <tr>\n        <th colspan=\"4\" class=\"sectionHeader\">\n          Other Crystallographic Data\n        </th>\n      </tr>\n      <tr>\n        <th>Space Group:</th>\n        <td colspan=\"3\">\n          ");
              //  rn:searchParamsField
              org.recipnet.site.content.rncontrols.SearchParamsField spaceGroup = null;
              org.recipnet.site.content.rncontrols.SearchParamsField _jspx_th_rn_searchParamsField_14 = (org.recipnet.site.content.rncontrols.SearchParamsField) _jspx_tagPool_rn_searchParamsField_tabIndex_id_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SearchParamsField.class);
              _jspx_th_rn_searchParamsField_14.setPageContext(_jspx_page_context);
              _jspx_th_rn_searchParamsField_14.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_searchParamsField_14.setTabIndex("23");
              _jspx_th_rn_searchParamsField_14.setId("spaceGroup");
              _jspx_th_rn_searchParamsField_14.setFieldCode( SearchParamsField.SPACE_GROUP );
              int _jspx_eval_rn_searchParamsField_14 = _jspx_th_rn_searchParamsField_14.doStartTag();
              if (_jspx_th_rn_searchParamsField_14.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              spaceGroup = (org.recipnet.site.content.rncontrols.SearchParamsField) _jspx_page_context.findAttribute("spaceGroup");
              _jspx_tagPool_rn_searchParamsField_tabIndex_id_fieldCode_nobody.reuse(_jspx_th_rn_searchParamsField_14);
              out.write("\n          <span class=\"supplementalInstructions\">\n            (All settings of the specified group will be matched, with\n            enantiomorphic distinctions ignored.)\n          </span>\n          ");
              //  ctl:errorMessage
              org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_8 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
              _jspx_th_ctl_errorMessage_8.setPageContext(_jspx_page_context);
              _jspx_th_ctl_errorMessage_8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_ctl_errorMessage_8.setErrorSupplier(spaceGroup);
              _jspx_th_ctl_errorMessage_8.setErrorFilter(SearchParamsField.VALIDATOR_REJECTED_VALUE);
              int _jspx_eval_ctl_errorMessage_8 = _jspx_th_ctl_errorMessage_8.doStartTag();
              if (_jspx_eval_ctl_errorMessage_8 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_ctl_errorMessage_8 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_ctl_errorMessage_8.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_ctl_errorMessage_8.doInitBody();
                }
                do {
                  out.write("\n            <span class=\"errorMessage\">\n              <br />\n              The specified text is not a valid space group symbol.\n          ");
                  int evalDoAfterBody = _jspx_th_ctl_errorMessage_8.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_ctl_errorMessage_8 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_ctl_errorMessage_8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.reuse(_jspx_th_ctl_errorMessage_8);
              out.write("\n        </td>\n      </tr>\n      <tr>\n        <th>Minimum Z:</th>\n        <td>\n          ");
              //  rn:searchParamsField
              org.recipnet.site.content.rncontrols.SearchParamsField minZ = null;
              org.recipnet.site.content.rncontrols.SearchParamsField _jspx_th_rn_searchParamsField_15 = (org.recipnet.site.content.rncontrols.SearchParamsField) _jspx_tagPool_rn_searchParamsField_tabIndex_id_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SearchParamsField.class);
              _jspx_th_rn_searchParamsField_15.setPageContext(_jspx_page_context);
              _jspx_th_rn_searchParamsField_15.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_searchParamsField_15.setTabIndex("24");
              _jspx_th_rn_searchParamsField_15.setId("minZ");
              _jspx_th_rn_searchParamsField_15.setFieldCode( SearchParamsField.MIN_Z );
              int _jspx_eval_rn_searchParamsField_15 = _jspx_th_rn_searchParamsField_15.doStartTag();
              if (_jspx_th_rn_searchParamsField_15.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              minZ = (org.recipnet.site.content.rncontrols.SearchParamsField) _jspx_page_context.findAttribute("minZ");
              _jspx_tagPool_rn_searchParamsField_tabIndex_id_fieldCode_nobody.reuse(_jspx_th_rn_searchParamsField_15);
              out.write("\n        </td>\n        <th>Maximum Z:</th>\n        <td>\n          ");
              //  rn:searchParamsField
              org.recipnet.site.content.rncontrols.SearchParamsField maxZ = null;
              org.recipnet.site.content.rncontrols.SearchParamsField _jspx_th_rn_searchParamsField_16 = (org.recipnet.site.content.rncontrols.SearchParamsField) _jspx_tagPool_rn_searchParamsField_tabIndex_id_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SearchParamsField.class);
              _jspx_th_rn_searchParamsField_16.setPageContext(_jspx_page_context);
              _jspx_th_rn_searchParamsField_16.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_searchParamsField_16.setTabIndex("25");
              _jspx_th_rn_searchParamsField_16.setId("maxZ");
              _jspx_th_rn_searchParamsField_16.setFieldCode( SearchParamsField.MAX_Z );
              int _jspx_eval_rn_searchParamsField_16 = _jspx_th_rn_searchParamsField_16.doStartTag();
              if (_jspx_th_rn_searchParamsField_16.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              maxZ = (org.recipnet.site.content.rncontrols.SearchParamsField) _jspx_page_context.findAttribute("maxZ");
              _jspx_tagPool_rn_searchParamsField_tabIndex_id_fieldCode_nobody.reuse(_jspx_th_rn_searchParamsField_16);
              out.write("\n          ");
              //  ctl:errorMessage
              org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_9 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
              _jspx_th_ctl_errorMessage_9.setPageContext(_jspx_page_context);
              _jspx_th_ctl_errorMessage_9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_ctl_errorMessage_9.setErrorFilter(SearchPage.MINZ_GREATER_THAN_MAXZ);
              int _jspx_eval_ctl_errorMessage_9 = _jspx_th_ctl_errorMessage_9.doStartTag();
              if (_jspx_eval_ctl_errorMessage_9 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_ctl_errorMessage_9 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_ctl_errorMessage_9.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_ctl_errorMessage_9.doInitBody();
                }
                do {
                  out.write("\n            <span class=\"errorMessageSmall\">\n              * cannot be less than Minimum Z.\n            </span>\n          ");
                  int evalDoAfterBody = _jspx_th_ctl_errorMessage_9.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_ctl_errorMessage_9 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_ctl_errorMessage_9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_ctl_errorMessage_errorFilter.reuse(_jspx_th_ctl_errorMessage_9);
              out.write("\n        </td>\n      </tr>\n      <tr>\n        <th>Volume:</th>\n        <td>\n          ");
              //  rn:searchParamsField
              org.recipnet.site.content.rncontrols.SearchParamsField volume = null;
              org.recipnet.site.content.rncontrols.SearchParamsField _jspx_th_rn_searchParamsField_17 = (org.recipnet.site.content.rncontrols.SearchParamsField) _jspx_tagPool_rn_searchParamsField_tabIndex_id_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SearchParamsField.class);
              _jspx_th_rn_searchParamsField_17.setPageContext(_jspx_page_context);
              _jspx_th_rn_searchParamsField_17.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_searchParamsField_17.setTabIndex("26");
              _jspx_th_rn_searchParamsField_17.setId("volume");
              _jspx_th_rn_searchParamsField_17.setFieldCode( SearchParamsField.VOLUME );
              int _jspx_eval_rn_searchParamsField_17 = _jspx_th_rn_searchParamsField_17.doStartTag();
              if (_jspx_th_rn_searchParamsField_17.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              volume = (org.recipnet.site.content.rncontrols.SearchParamsField) _jspx_page_context.findAttribute("volume");
              _jspx_tagPool_rn_searchParamsField_tabIndex_id_fieldCode_nobody.reuse(_jspx_th_rn_searchParamsField_17);
              out.write("&nbsp;&#177;&nbsp;\n          ");
              //  rn:searchParamsField
              org.recipnet.site.content.rncontrols.SearchParamsField rangeVolume = null;
              org.recipnet.site.content.rncontrols.SearchParamsField _jspx_th_rn_searchParamsField_18 = (org.recipnet.site.content.rncontrols.SearchParamsField) _jspx_tagPool_rn_searchParamsField_tabIndex_id_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SearchParamsField.class);
              _jspx_th_rn_searchParamsField_18.setPageContext(_jspx_page_context);
              _jspx_th_rn_searchParamsField_18.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_searchParamsField_18.setTabIndex("27");
              _jspx_th_rn_searchParamsField_18.setId("rangeVolume");
              _jspx_th_rn_searchParamsField_18.setFieldCode( SearchParamsField.RANGE_VOLUME );
              int _jspx_eval_rn_searchParamsField_18 = _jspx_th_rn_searchParamsField_18.doStartTag();
              if (_jspx_th_rn_searchParamsField_18.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              rangeVolume = (org.recipnet.site.content.rncontrols.SearchParamsField) _jspx_page_context.findAttribute("rangeVolume");
              _jspx_tagPool_rn_searchParamsField_tabIndex_id_fieldCode_nobody.reuse(_jspx_th_rn_searchParamsField_18);
              out.write("%\n        </td>\n        <th>Density:</th>\n        <td>\n          ");
              //  rn:searchParamsField
              org.recipnet.site.content.rncontrols.SearchParamsField density = null;
              org.recipnet.site.content.rncontrols.SearchParamsField _jspx_th_rn_searchParamsField_19 = (org.recipnet.site.content.rncontrols.SearchParamsField) _jspx_tagPool_rn_searchParamsField_tabIndex_id_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SearchParamsField.class);
              _jspx_th_rn_searchParamsField_19.setPageContext(_jspx_page_context);
              _jspx_th_rn_searchParamsField_19.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_searchParamsField_19.setTabIndex("28");
              _jspx_th_rn_searchParamsField_19.setId("density");
              _jspx_th_rn_searchParamsField_19.setFieldCode( SearchParamsField.DENSITY );
              int _jspx_eval_rn_searchParamsField_19 = _jspx_th_rn_searchParamsField_19.doStartTag();
              if (_jspx_th_rn_searchParamsField_19.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              density = (org.recipnet.site.content.rncontrols.SearchParamsField) _jspx_page_context.findAttribute("density");
              _jspx_tagPool_rn_searchParamsField_tabIndex_id_fieldCode_nobody.reuse(_jspx_th_rn_searchParamsField_19);
              out.write("&nbsp;&#177;&nbsp;\n          ");
              //  rn:searchParamsField
              org.recipnet.site.content.rncontrols.SearchParamsField rangeDensity = null;
              org.recipnet.site.content.rncontrols.SearchParamsField _jspx_th_rn_searchParamsField_20 = (org.recipnet.site.content.rncontrols.SearchParamsField) _jspx_tagPool_rn_searchParamsField_tabIndex_id_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SearchParamsField.class);
              _jspx_th_rn_searchParamsField_20.setPageContext(_jspx_page_context);
              _jspx_th_rn_searchParamsField_20.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_searchParamsField_20.setTabIndex("29");
              _jspx_th_rn_searchParamsField_20.setId("rangeDensity");
              _jspx_th_rn_searchParamsField_20.setFieldCode( SearchParamsField.RANGE_DENSITY );
              int _jspx_eval_rn_searchParamsField_20 = _jspx_th_rn_searchParamsField_20.doStartTag();
              if (_jspx_th_rn_searchParamsField_20.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              rangeDensity = (org.recipnet.site.content.rncontrols.SearchParamsField) _jspx_page_context.findAttribute("rangeDensity");
              _jspx_tagPool_rn_searchParamsField_tabIndex_id_fieldCode_nobody.reuse(_jspx_th_rn_searchParamsField_20);
              out.write("%\n        </td>\n      </tr>\n      <tr>\n        <th>Temperature:</th>\n        <td>\n          ");
              //  rn:searchParamsField
              org.recipnet.site.content.rncontrols.SearchParamsField temp = null;
              org.recipnet.site.content.rncontrols.SearchParamsField _jspx_th_rn_searchParamsField_21 = (org.recipnet.site.content.rncontrols.SearchParamsField) _jspx_tagPool_rn_searchParamsField_tabIndex_id_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SearchParamsField.class);
              _jspx_th_rn_searchParamsField_21.setPageContext(_jspx_page_context);
              _jspx_th_rn_searchParamsField_21.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_searchParamsField_21.setTabIndex("30");
              _jspx_th_rn_searchParamsField_21.setId("temp");
              _jspx_th_rn_searchParamsField_21.setFieldCode( SearchParamsField.TEMP );
              int _jspx_eval_rn_searchParamsField_21 = _jspx_th_rn_searchParamsField_21.doStartTag();
              if (_jspx_th_rn_searchParamsField_21.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              temp = (org.recipnet.site.content.rncontrols.SearchParamsField) _jspx_page_context.findAttribute("temp");
              _jspx_tagPool_rn_searchParamsField_tabIndex_id_fieldCode_nobody.reuse(_jspx_th_rn_searchParamsField_21);
              out.write("&nbsp;&#177;&nbsp;\n          ");
              //  rn:searchParamsField
              org.recipnet.site.content.rncontrols.SearchParamsField rangeTemp = null;
              org.recipnet.site.content.rncontrols.SearchParamsField _jspx_th_rn_searchParamsField_22 = (org.recipnet.site.content.rncontrols.SearchParamsField) _jspx_tagPool_rn_searchParamsField_tabIndex_id_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SearchParamsField.class);
              _jspx_th_rn_searchParamsField_22.setPageContext(_jspx_page_context);
              _jspx_th_rn_searchParamsField_22.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_searchParamsField_22.setTabIndex("31");
              _jspx_th_rn_searchParamsField_22.setId("rangeTemp");
              _jspx_th_rn_searchParamsField_22.setFieldCode( SearchParamsField.RANGE_TEMP );
              int _jspx_eval_rn_searchParamsField_22 = _jspx_th_rn_searchParamsField_22.doStartTag();
              if (_jspx_th_rn_searchParamsField_22.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              rangeTemp = (org.recipnet.site.content.rncontrols.SearchParamsField) _jspx_page_context.findAttribute("rangeTemp");
              _jspx_tagPool_rn_searchParamsField_tabIndex_id_fieldCode_nobody.reuse(_jspx_th_rn_searchParamsField_22);
              out.write("%\n        </td>\n        <td colspan=\"2\"></td>\n      </tr>\n      <tr>\n        <th colspan=\"4\" class=\"sectionHeader\">Atom Counts:</th>\n      </tr>\n      <tr>\n        <td colspan=\"2\">\n          ");
              //  ctl:errorMessage
              org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_10 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
              _jspx_th_ctl_errorMessage_10.setPageContext(_jspx_page_context);
              _jspx_th_ctl_errorMessage_10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_ctl_errorMessage_10.setErrorFilter(SearchPage.ATOM_TYPE_MISSING);
              int _jspx_eval_ctl_errorMessage_10 = _jspx_th_ctl_errorMessage_10.doStartTag();
              if (_jspx_eval_ctl_errorMessage_10 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_ctl_errorMessage_10 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_ctl_errorMessage_10.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_ctl_errorMessage_10.doInitBody();
                }
                do {
                  out.write("\n            <p class=\"errorMessage\">\n              You must select an atom type whenever an atom count is supplied.\n            </p>\n          ");
                  int evalDoAfterBody = _jspx_th_ctl_errorMessage_10.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_ctl_errorMessage_10 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_ctl_errorMessage_10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_ctl_errorMessage_errorFilter.reuse(_jspx_th_ctl_errorMessage_10);
              out.write("\n          ");
              //  ctl:errorMessage
              org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_11 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
              _jspx_th_ctl_errorMessage_11.setPageContext(_jspx_page_context);
              _jspx_th_ctl_errorMessage_11.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_ctl_errorMessage_11.setErrorFilter(SearchPage.ATOM_COUNT_MISSING);
              int _jspx_eval_ctl_errorMessage_11 = _jspx_th_ctl_errorMessage_11.doStartTag();
              if (_jspx_eval_ctl_errorMessage_11 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_ctl_errorMessage_11 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_ctl_errorMessage_11.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_ctl_errorMessage_11.doInitBody();
                }
                do {
                  out.write("\n            <p class=\"errorMessage\">\n              You must select a positive atom count whenever an atom type is\n              supplied.\n            </p>\n          ");
                  int evalDoAfterBody = _jspx_th_ctl_errorMessage_11.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_ctl_errorMessage_11 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_ctl_errorMessage_11.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_ctl_errorMessage_errorFilter.reuse(_jspx_th_ctl_errorMessage_11);
              out.write("\n          ");
              //  ctl:errorMessage
              org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_12 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
              _jspx_th_ctl_errorMessage_12.setPageContext(_jspx_page_context);
              _jspx_th_ctl_errorMessage_12.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_ctl_errorMessage_12.setErrorFilter(SearchPage.ATOM_OPERATOR_MISSING);
              int _jspx_eval_ctl_errorMessage_12 = _jspx_th_ctl_errorMessage_12.doStartTag();
              if (_jspx_eval_ctl_errorMessage_12 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_ctl_errorMessage_12 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_ctl_errorMessage_12.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_ctl_errorMessage_12.doInitBody();
                }
                do {
                  out.write("\n            <p class=\"errorMessage\">\n              You must select an operator whenever you select an atom type or\n              count.\n            </p>\n          ");
                  int evalDoAfterBody = _jspx_th_ctl_errorMessage_12.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_ctl_errorMessage_12 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_ctl_errorMessage_12.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_ctl_errorMessage_errorFilter.reuse(_jspx_th_ctl_errorMessage_12);
              out.write("\n          ");
              //  ctl:iterator
              org.recipnet.common.controls.SimpleIterator countGroup = null;
              org.recipnet.common.controls.SimpleIterator _jspx_th_ctl_iterator_0 = (org.recipnet.common.controls.SimpleIterator) _jspx_tagPool_ctl_iterator_iterations_id.get(org.recipnet.common.controls.SimpleIterator.class);
              _jspx_th_ctl_iterator_0.setPageContext(_jspx_page_context);
              _jspx_th_ctl_iterator_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_ctl_iterator_0.setId("countGroup");
              _jspx_th_ctl_iterator_0.setIterations(3);
              int _jspx_eval_ctl_iterator_0 = _jspx_th_ctl_iterator_0.doStartTag();
              if (_jspx_eval_ctl_iterator_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_ctl_iterator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_ctl_iterator_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_ctl_iterator_0.doInitBody();
                }
                countGroup = (org.recipnet.common.controls.SimpleIterator) _jspx_page_context.findAttribute("countGroup");
                do {
                  out.write("\n          <div style=\"margin: 0.25em 0 0.25em 1.5em; white-space: nowrap;\">\n          ");
                  //  rn:searchParamsField
                  org.recipnet.site.content.rncontrols.SearchParamsField _jspx_th_rn_searchParamsField_23 = (org.recipnet.site.content.rncontrols.SearchParamsField) _jspx_tagPool_rn_searchParamsField_tabIndex_groupKey_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SearchParamsField.class);
                  _jspx_th_rn_searchParamsField_23.setPageContext(_jspx_page_context);
                  _jspx_th_rn_searchParamsField_23.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_iterator_0);
                  _jspx_th_rn_searchParamsField_23.setTabIndex("32");
                  _jspx_th_rn_searchParamsField_23.setGroupKey((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${countGroup.iterationCountSinceThisPhaseBegan + 1}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
                  _jspx_th_rn_searchParamsField_23.setFieldCode(SearchParamsField.SEARCH_ATOM_TYPE);
                  int _jspx_eval_rn_searchParamsField_23 = _jspx_th_rn_searchParamsField_23.doStartTag();
                  if (_jspx_th_rn_searchParamsField_23.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_searchParamsField_tabIndex_groupKey_fieldCode_nobody.reuse(_jspx_th_rn_searchParamsField_23);
                  out.write("\n          is present and occurs\n          ");
                  //  rn:searchParamsField
                  org.recipnet.site.content.rncontrols.SearchParamsField _jspx_th_rn_searchParamsField_24 = (org.recipnet.site.content.rncontrols.SearchParamsField) _jspx_tagPool_rn_searchParamsField_tabIndex_groupKey_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SearchParamsField.class);
                  _jspx_th_rn_searchParamsField_24.setPageContext(_jspx_page_context);
                  _jspx_th_rn_searchParamsField_24.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_iterator_0);
                  _jspx_th_rn_searchParamsField_24.setTabIndex("32");
                  _jspx_th_rn_searchParamsField_24.setGroupKey((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${countGroup.iterationCountSinceThisPhaseBegan + 1}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
                  _jspx_th_rn_searchParamsField_24.setFieldCode(SearchParamsField.SEARCH_ATOM_OPERATOR);
                  int _jspx_eval_rn_searchParamsField_24 = _jspx_th_rn_searchParamsField_24.doStartTag();
                  if (_jspx_th_rn_searchParamsField_24.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_searchParamsField_tabIndex_groupKey_fieldCode_nobody.reuse(_jspx_th_rn_searchParamsField_24);
                  out.write("\n          ");
                  //  rn:searchParamsField
                  org.recipnet.site.content.rncontrols.SearchParamsField _jspx_th_rn_searchParamsField_25 = (org.recipnet.site.content.rncontrols.SearchParamsField) _jspx_tagPool_rn_searchParamsField_tabIndex_groupKey_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SearchParamsField.class);
                  _jspx_th_rn_searchParamsField_25.setPageContext(_jspx_page_context);
                  _jspx_th_rn_searchParamsField_25.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_iterator_0);
                  _jspx_th_rn_searchParamsField_25.setTabIndex("32");
                  _jspx_th_rn_searchParamsField_25.setGroupKey((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${countGroup.iterationCountSinceThisPhaseBegan + 1}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
                  _jspx_th_rn_searchParamsField_25.setFieldCode(SearchParamsField.SEARCH_ATOM_COUNT);
                  int _jspx_eval_rn_searchParamsField_25 = _jspx_th_rn_searchParamsField_25.doStartTag();
                  if (_jspx_th_rn_searchParamsField_25.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_searchParamsField_tabIndex_groupKey_fieldCode_nobody.reuse(_jspx_th_rn_searchParamsField_25);
                  out.write("\n          times.\n          </div>\n          ");
                  int evalDoAfterBody = _jspx_th_ctl_iterator_0.doAfterBody();
                  countGroup = (org.recipnet.common.controls.SimpleIterator) _jspx_page_context.findAttribute("countGroup");
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_ctl_iterator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_ctl_iterator_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              countGroup = (org.recipnet.common.controls.SimpleIterator) _jspx_page_context.findAttribute("countGroup");
              _jspx_tagPool_ctl_iterator_iterations_id.reuse(_jspx_th_ctl_iterator_0);
              out.write("\n        </td>\n        <td colspan=\"2\" style=\"vertical-align: top; padding-left: 1em;\">\n          ");
              //  rn:searchParamsField
              org.recipnet.site.content.rncontrols.SearchParamsField _jspx_th_rn_searchParamsField_26 = (org.recipnet.site.content.rncontrols.SearchParamsField) _jspx_tagPool_rn_searchParamsField_tabIndex_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SearchParamsField.class);
              _jspx_th_rn_searchParamsField_26.setPageContext(_jspx_page_context);
              _jspx_th_rn_searchParamsField_26.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_searchParamsField_26.setTabIndex("41");
              _jspx_th_rn_searchParamsField_26.setFieldCode( SearchParamsField.SEARCH_ATOMS_FROM_EF);
              int _jspx_eval_rn_searchParamsField_26 = _jspx_th_rn_searchParamsField_26.doStartTag();
              if (_jspx_th_rn_searchParamsField_26.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_searchParamsField_tabIndex_fieldCode_nobody.reuse(_jspx_th_rn_searchParamsField_26);
              out.write("\n          Search ");
              //  rn:sampleFieldLabel
              org.recipnet.site.content.rncontrols.SampleFieldLabel _jspx_th_rn_sampleFieldLabel_0 = (org.recipnet.site.content.rncontrols.SampleFieldLabel) _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldLabel.class);
              _jspx_th_rn_sampleFieldLabel_0.setPageContext(_jspx_page_context);
              _jspx_th_rn_sampleFieldLabel_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_sampleFieldLabel_0.setFieldCode(SampleTextBL.EMPIRICAL_FORMULA);
              int _jspx_eval_rn_sampleFieldLabel_0 = _jspx_th_rn_sampleFieldLabel_0.doStartTag();
              if (_jspx_th_rn_sampleFieldLabel_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.reuse(_jspx_th_rn_sampleFieldLabel_0);
              out.write("\n          <br />\n          ");
              //  rn:searchParamsField
              org.recipnet.site.content.rncontrols.SearchParamsField _jspx_th_rn_searchParamsField_27 = (org.recipnet.site.content.rncontrols.SearchParamsField) _jspx_tagPool_rn_searchParamsField_tabIndex_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SearchParamsField.class);
              _jspx_th_rn_searchParamsField_27.setPageContext(_jspx_page_context);
              _jspx_th_rn_searchParamsField_27.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_searchParamsField_27.setTabIndex("42");
              _jspx_th_rn_searchParamsField_27.setFieldCode( SearchParamsField.SEARCH_ATOMS_FROM_EFD);
              int _jspx_eval_rn_searchParamsField_27 = _jspx_th_rn_searchParamsField_27.doStartTag();
              if (_jspx_th_rn_searchParamsField_27.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_searchParamsField_tabIndex_fieldCode_nobody.reuse(_jspx_th_rn_searchParamsField_27);
              out.write("\n          Search ");
              //  rn:sampleFieldLabel
              org.recipnet.site.content.rncontrols.SampleFieldLabel _jspx_th_rn_sampleFieldLabel_1 = (org.recipnet.site.content.rncontrols.SampleFieldLabel) _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldLabel.class);
              _jspx_th_rn_sampleFieldLabel_1.setPageContext(_jspx_page_context);
              _jspx_th_rn_sampleFieldLabel_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_sampleFieldLabel_1.setFieldCode(SampleTextBL.EMPIRICAL_FORMULA_DERIVED);
              int _jspx_eval_rn_sampleFieldLabel_1 = _jspx_th_rn_sampleFieldLabel_1.doStartTag();
              if (_jspx_th_rn_sampleFieldLabel_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.reuse(_jspx_th_rn_sampleFieldLabel_1);
              out.write("\n          <br />\n          ");
              //  rn:searchParamsField
              org.recipnet.site.content.rncontrols.SearchParamsField _jspx_th_rn_searchParamsField_28 = (org.recipnet.site.content.rncontrols.SearchParamsField) _jspx_tagPool_rn_searchParamsField_tabIndex_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SearchParamsField.class);
              _jspx_th_rn_searchParamsField_28.setPageContext(_jspx_page_context);
              _jspx_th_rn_searchParamsField_28.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_searchParamsField_28.setTabIndex("43");
              _jspx_th_rn_searchParamsField_28.setFieldCode( SearchParamsField.SEARCH_ATOMS_FROM_EFLS);
              int _jspx_eval_rn_searchParamsField_28 = _jspx_th_rn_searchParamsField_28.doStartTag();
              if (_jspx_th_rn_searchParamsField_28.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_searchParamsField_tabIndex_fieldCode_nobody.reuse(_jspx_th_rn_searchParamsField_28);
              out.write("\n          Search ");
              //  rn:sampleFieldLabel
              org.recipnet.site.content.rncontrols.SampleFieldLabel _jspx_th_rn_sampleFieldLabel_2 = (org.recipnet.site.content.rncontrols.SampleFieldLabel) _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldLabel.class);
              _jspx_th_rn_sampleFieldLabel_2.setPageContext(_jspx_page_context);
              _jspx_th_rn_sampleFieldLabel_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_sampleFieldLabel_2.setFieldCode(
                  SampleTextBL.EMPIRICAL_FORMULA_LESS_SOLVENT);
              int _jspx_eval_rn_sampleFieldLabel_2 = _jspx_th_rn_sampleFieldLabel_2.doStartTag();
              if (_jspx_th_rn_sampleFieldLabel_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.reuse(_jspx_th_rn_sampleFieldLabel_2);
              out.write("\n          <br />\n          ");
              //  rn:searchParamsField
              org.recipnet.site.content.rncontrols.SearchParamsField _jspx_th_rn_searchParamsField_29 = (org.recipnet.site.content.rncontrols.SearchParamsField) _jspx_tagPool_rn_searchParamsField_tabIndex_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SearchParamsField.class);
              _jspx_th_rn_searchParamsField_29.setPageContext(_jspx_page_context);
              _jspx_th_rn_searchParamsField_29.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_searchParamsField_29.setTabIndex("44");
              _jspx_th_rn_searchParamsField_29.setFieldCode( SearchParamsField.SEARCH_ATOMS_FROM_EFSI);
              int _jspx_eval_rn_searchParamsField_29 = _jspx_th_rn_searchParamsField_29.doStartTag();
              if (_jspx_th_rn_searchParamsField_29.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_searchParamsField_tabIndex_fieldCode_nobody.reuse(_jspx_th_rn_searchParamsField_29);
              out.write("\n          Search ");
              //  rn:sampleFieldLabel
              org.recipnet.site.content.rncontrols.SampleFieldLabel _jspx_th_rn_sampleFieldLabel_3 = (org.recipnet.site.content.rncontrols.SampleFieldLabel) _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldLabel.class);
              _jspx_th_rn_sampleFieldLabel_3.setPageContext(_jspx_page_context);
              _jspx_th_rn_sampleFieldLabel_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_sampleFieldLabel_3.setFieldCode(
                  SampleTextBL.EMPIRICAL_FORMULA_SINGLE_ION);
              int _jspx_eval_rn_sampleFieldLabel_3 = _jspx_th_rn_sampleFieldLabel_3.doStartTag();
              if (_jspx_th_rn_sampleFieldLabel_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.reuse(_jspx_th_rn_sampleFieldLabel_3);
              out.write("\n        </td>\n      </tr>\n      <tr>\n        <th colspan=\"4\" class=\"sectionHeader\">Keywords</th>\n      </tr>\n      <tr>\n        <th>Match: </th>\n        <td colspan=\"3\">\n          <span style=\"padding-right: 1em;\">\n            ");
              //  ctl:radioButtonGroup
              org.recipnet.common.controls.RadioButtonGroupHtmlControl match = null;
              org.recipnet.common.controls.RadioButtonGroupHtmlControl _jspx_th_ctl_radioButtonGroup_2 = (org.recipnet.common.controls.RadioButtonGroupHtmlControl) _jspx_tagPool_ctl_radioButtonGroup_initialValue_id.get(org.recipnet.common.controls.RadioButtonGroupHtmlControl.class);
              _jspx_th_ctl_radioButtonGroup_2.setPageContext(_jspx_page_context);
              _jspx_th_ctl_radioButtonGroup_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_ctl_radioButtonGroup_2.setId("match");
              _jspx_th_ctl_radioButtonGroup_2.setInitialValue(new String("any"));
              int _jspx_eval_ctl_radioButtonGroup_2 = _jspx_th_ctl_radioButtonGroup_2.doStartTag();
              if (_jspx_eval_ctl_radioButtonGroup_2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_ctl_radioButtonGroup_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_ctl_radioButtonGroup_2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_ctl_radioButtonGroup_2.doInitBody();
                }
                match = (org.recipnet.common.controls.RadioButtonGroupHtmlControl) _jspx_page_context.findAttribute("match");
                do {
                  out.write("\n            ");
                  //  rn:searchParamsField
                  org.recipnet.site.content.rncontrols.SearchParamsField matchAny = null;
                  org.recipnet.site.content.rncontrols.SearchParamsField _jspx_th_rn_searchParamsField_30 = (org.recipnet.site.content.rncontrols.SearchParamsField) _jspx_tagPool_rn_searchParamsField_tabIndex_id_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SearchParamsField.class);
                  _jspx_th_rn_searchParamsField_30.setPageContext(_jspx_page_context);
                  _jspx_th_rn_searchParamsField_30.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_radioButtonGroup_2);
                  _jspx_th_rn_searchParamsField_30.setTabIndex("45");
                  _jspx_th_rn_searchParamsField_30.setId("matchAny");
                  _jspx_th_rn_searchParamsField_30.setFieldCode( SearchParamsField.MATCH_ANY_KEYWORD );
                  int _jspx_eval_rn_searchParamsField_30 = _jspx_th_rn_searchParamsField_30.doStartTag();
                  if (_jspx_th_rn_searchParamsField_30.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  matchAny = (org.recipnet.site.content.rncontrols.SearchParamsField) _jspx_page_context.findAttribute("matchAny");
                  _jspx_tagPool_rn_searchParamsField_tabIndex_id_fieldCode_nobody.reuse(_jspx_th_rn_searchParamsField_30);
                  out.write("\n            <strong>Any</strong> of these terms\n          </span>\n          <span style=\"padding-right: 1em;\">\n            ");
                  //  rn:searchParamsField
                  org.recipnet.site.content.rncontrols.SearchParamsField matchAll = null;
                  org.recipnet.site.content.rncontrols.SearchParamsField _jspx_th_rn_searchParamsField_31 = (org.recipnet.site.content.rncontrols.SearchParamsField) _jspx_tagPool_rn_searchParamsField_tabIndex_id_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SearchParamsField.class);
                  _jspx_th_rn_searchParamsField_31.setPageContext(_jspx_page_context);
                  _jspx_th_rn_searchParamsField_31.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_radioButtonGroup_2);
                  _jspx_th_rn_searchParamsField_31.setTabIndex("46");
                  _jspx_th_rn_searchParamsField_31.setId("matchAll");
                  _jspx_th_rn_searchParamsField_31.setFieldCode( SearchParamsField.MATCH_ALL_KEYWORD );
                  int _jspx_eval_rn_searchParamsField_31 = _jspx_th_rn_searchParamsField_31.doStartTag();
                  if (_jspx_th_rn_searchParamsField_31.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  matchAll = (org.recipnet.site.content.rncontrols.SearchParamsField) _jspx_page_context.findAttribute("matchAll");
                  _jspx_tagPool_rn_searchParamsField_tabIndex_id_fieldCode_nobody.reuse(_jspx_th_rn_searchParamsField_31);
                  out.write("\n            <strong>All</strong> of these terms\n          </span>\n          ");
                  int evalDoAfterBody = _jspx_th_ctl_radioButtonGroup_2.doAfterBody();
                  match = (org.recipnet.common.controls.RadioButtonGroupHtmlControl) _jspx_page_context.findAttribute("match");
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_ctl_radioButtonGroup_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_ctl_radioButtonGroup_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              match = (org.recipnet.common.controls.RadioButtonGroupHtmlControl) _jspx_page_context.findAttribute("match");
              _jspx_tagPool_ctl_radioButtonGroup_initialValue_id.reuse(_jspx_th_ctl_radioButtonGroup_2);
              out.write("\n        </td>\n      </tr>\n      <tr>\n        <th>Terms:</th>\n        <td colspan=\"3\">\n          <span style=\"padding-right: 1em;\">\n            ");
              //  rn:searchParamsField
              org.recipnet.site.content.rncontrols.SearchParamsField keyword1 = null;
              org.recipnet.site.content.rncontrols.SearchParamsField _jspx_th_rn_searchParamsField_32 = (org.recipnet.site.content.rncontrols.SearchParamsField) _jspx_tagPool_rn_searchParamsField_tabIndex_id_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SearchParamsField.class);
              _jspx_th_rn_searchParamsField_32.setPageContext(_jspx_page_context);
              _jspx_th_rn_searchParamsField_32.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_searchParamsField_32.setTabIndex("47");
              _jspx_th_rn_searchParamsField_32.setId("keyword1");
              _jspx_th_rn_searchParamsField_32.setFieldCode( SearchParamsField.KEYWORD );
              int _jspx_eval_rn_searchParamsField_32 = _jspx_th_rn_searchParamsField_32.doStartTag();
              if (_jspx_th_rn_searchParamsField_32.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              keyword1 = (org.recipnet.site.content.rncontrols.SearchParamsField) _jspx_page_context.findAttribute("keyword1");
              _jspx_tagPool_rn_searchParamsField_tabIndex_id_fieldCode_nobody.reuse(_jspx_th_rn_searchParamsField_32);
              out.write("\n          </span>\n          <span style=\"padding-right: 1em;\">\n            ");
              //  rn:searchParamsField
              org.recipnet.site.content.rncontrols.SearchParamsField keyword2 = null;
              org.recipnet.site.content.rncontrols.SearchParamsField _jspx_th_rn_searchParamsField_33 = (org.recipnet.site.content.rncontrols.SearchParamsField) _jspx_tagPool_rn_searchParamsField_tabIndex_id_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SearchParamsField.class);
              _jspx_th_rn_searchParamsField_33.setPageContext(_jspx_page_context);
              _jspx_th_rn_searchParamsField_33.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_searchParamsField_33.setTabIndex("48");
              _jspx_th_rn_searchParamsField_33.setId("keyword2");
              _jspx_th_rn_searchParamsField_33.setFieldCode( SearchParamsField.KEYWORD );
              int _jspx_eval_rn_searchParamsField_33 = _jspx_th_rn_searchParamsField_33.doStartTag();
              if (_jspx_th_rn_searchParamsField_33.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              keyword2 = (org.recipnet.site.content.rncontrols.SearchParamsField) _jspx_page_context.findAttribute("keyword2");
              _jspx_tagPool_rn_searchParamsField_tabIndex_id_fieldCode_nobody.reuse(_jspx_th_rn_searchParamsField_33);
              out.write("\n          </span>\n          <span style=\"padding-right: 1em;\">\n            ");
              //  rn:searchParamsField
              org.recipnet.site.content.rncontrols.SearchParamsField keyword3 = null;
              org.recipnet.site.content.rncontrols.SearchParamsField _jspx_th_rn_searchParamsField_34 = (org.recipnet.site.content.rncontrols.SearchParamsField) _jspx_tagPool_rn_searchParamsField_tabIndex_id_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SearchParamsField.class);
              _jspx_th_rn_searchParamsField_34.setPageContext(_jspx_page_context);
              _jspx_th_rn_searchParamsField_34.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_searchParamsField_34.setTabIndex("49");
              _jspx_th_rn_searchParamsField_34.setId("keyword3");
              _jspx_th_rn_searchParamsField_34.setFieldCode( SearchParamsField.KEYWORD );
              int _jspx_eval_rn_searchParamsField_34 = _jspx_th_rn_searchParamsField_34.doStartTag();
              if (_jspx_th_rn_searchParamsField_34.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              keyword3 = (org.recipnet.site.content.rncontrols.SearchParamsField) _jspx_page_context.findAttribute("keyword3");
              _jspx_tagPool_rn_searchParamsField_tabIndex_id_fieldCode_nobody.reuse(_jspx_th_rn_searchParamsField_34);
              out.write("\n          </span>\n        </td>\n      </tr>\n      <tr>\n        <th colspan=\"4\" class=\"sectionHeader\">\n          Ordering of Search Results\n        </th>\n      </tr>\n      <tr>\n        <th>Sort results by: </th>\n        <td>\n          ");
              //  rn:searchParamsField
              org.recipnet.site.content.rncontrols.SearchParamsField sortOrder = null;
              org.recipnet.site.content.rncontrols.SearchParamsField _jspx_th_rn_searchParamsField_35 = (org.recipnet.site.content.rncontrols.SearchParamsField) _jspx_tagPool_rn_searchParamsField_tabIndex_id_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SearchParamsField.class);
              _jspx_th_rn_searchParamsField_35.setPageContext(_jspx_page_context);
              _jspx_th_rn_searchParamsField_35.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_searchParamsField_35.setTabIndex("50");
              _jspx_th_rn_searchParamsField_35.setId("sortOrder");
              _jspx_th_rn_searchParamsField_35.setFieldCode( SearchParamsField.SORT_ORDER );
              int _jspx_eval_rn_searchParamsField_35 = _jspx_th_rn_searchParamsField_35.doStartTag();
              if (_jspx_th_rn_searchParamsField_35.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              sortOrder = (org.recipnet.site.content.rncontrols.SearchParamsField) _jspx_page_context.findAttribute("sortOrder");
              _jspx_tagPool_rn_searchParamsField_tabIndex_id_fieldCode_nobody.reuse(_jspx_th_rn_searchParamsField_35);
              out.write("\n        </td>\n        <th>Results per page: </th>\n        <td colspan=\"3\">\n          ");
              //  ctl:listbox
              org.recipnet.common.controls.ListboxHtmlControl pageSize = null;
              org.recipnet.common.controls.ListboxHtmlControl _jspx_th_ctl_listbox_0 = (org.recipnet.common.controls.ListboxHtmlControl) _jspx_tagPool_ctl_listbox_initialValue_id.get(org.recipnet.common.controls.ListboxHtmlControl.class);
              _jspx_th_ctl_listbox_0.setPageContext(_jspx_page_context);
              _jspx_th_ctl_listbox_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_ctl_listbox_0.setId("pageSize");
              _jspx_th_ctl_listbox_0.setInitialValue(new String("10"));
              int _jspx_eval_ctl_listbox_0 = _jspx_th_ctl_listbox_0.doStartTag();
              if (_jspx_eval_ctl_listbox_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_ctl_listbox_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_ctl_listbox_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_ctl_listbox_0.doInitBody();
                }
                pageSize = (org.recipnet.common.controls.ListboxHtmlControl) _jspx_page_context.findAttribute("pageSize");
                do {
                  out.write("\n            ");
                  if (_jspx_meth_ctl_extraHtmlAttribute_0(_jspx_th_ctl_listbox_0, _jspx_page_context))
                    return;
                  out.write("\n            ");
                  if (_jspx_meth_ctl_option_0(_jspx_th_ctl_listbox_0, _jspx_page_context))
                    return;
                  out.write("\n            ");
                  if (_jspx_meth_ctl_option_1(_jspx_th_ctl_listbox_0, _jspx_page_context))
                    return;
                  out.write("\n            ");
                  if (_jspx_meth_ctl_option_2(_jspx_th_ctl_listbox_0, _jspx_page_context))
                    return;
                  out.write("\n            ");
                  if (_jspx_meth_ctl_option_3(_jspx_th_ctl_listbox_0, _jspx_page_context))
                    return;
                  out.write("\n            ");
                  if (_jspx_meth_ctl_option_4(_jspx_th_ctl_listbox_0, _jspx_page_context))
                    return;
                  out.write("\n            ");
                  if (_jspx_meth_ctl_option_5(_jspx_th_ctl_listbox_0, _jspx_page_context))
                    return;
                  out.write("\n          ");
                  int evalDoAfterBody = _jspx_th_ctl_listbox_0.doAfterBody();
                  pageSize = (org.recipnet.common.controls.ListboxHtmlControl) _jspx_page_context.findAttribute("pageSize");
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_ctl_listbox_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_ctl_listbox_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              pageSize = (org.recipnet.common.controls.ListboxHtmlControl) _jspx_page_context.findAttribute("pageSize");
              _jspx_tagPool_ctl_listbox_initialValue_id.reuse(_jspx_th_ctl_listbox_0);
              out.write("\n        </td>\n      </tr>\n      <tr>\n        <td colspan=\"4\"\n            style=\"text-align: center; background-color: inherit; padding-top: 1em\">\n          ");
              //  rn:searchButton
              org.recipnet.site.content.rncontrols.SearchButton searchButton = null;
              org.recipnet.site.content.rncontrols.SearchButton _jspx_th_rn_searchButton_0 = (org.recipnet.site.content.rncontrols.SearchButton) _jspx_tagPool_rn_searchButton_label_id_nobody.get(org.recipnet.site.content.rncontrols.SearchButton.class);
              _jspx_th_rn_searchButton_0.setPageContext(_jspx_page_context);
              _jspx_th_rn_searchButton_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_searchButton_0.setId("searchButton");
              _jspx_th_rn_searchButton_0.setLabel("Search");
              int _jspx_eval_rn_searchButton_0 = _jspx_th_rn_searchButton_0.doStartTag();
              if (_jspx_th_rn_searchButton_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              searchButton = (org.recipnet.site.content.rncontrols.SearchButton) _jspx_page_context.findAttribute("searchButton");
              _jspx_tagPool_rn_searchButton_label_id_nobody.reuse(_jspx_th_rn_searchButton_0);
              out.write("\n          ");
              if (_jspx_meth_ctl_extraHtmlAttribute_1(_jspx_th_ctl_selfForm_0, _jspx_page_context))
                return;
              out.write("\n        </td>\n      </tr>\n    </table>\n  ");
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
          if (_jspx_meth_ctl_styleBlock_0(_jspx_th_rn_searchPage_0, _jspx_page_context))
            return;
          out.write('\n');
          int evalDoAfterBody = _jspx_th_rn_searchPage_0.doAfterBody();
          htmlPage = (org.recipnet.site.content.rncontrols.SearchPage) _jspx_page_context.findAttribute("htmlPage");
          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
            break;
        } while (true);
        if (_jspx_eval_rn_searchPage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
          out = _jspx_page_context.popBody();
      }
      if (_jspx_th_rn_searchPage_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
        return;
      _jspx_tagPool_rn_searchPage_title_searchResultsPage.reuse(_jspx_th_rn_searchPage_0);
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

  private boolean _jspx_meth_ctl_errorMessage_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_searchPage_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:errorMessage
    org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_0 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage.get(org.recipnet.common.controls.ErrorChecker.class);
    _jspx_th_ctl_errorMessage_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_errorMessage_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_searchPage_0);
    int _jspx_eval_ctl_errorMessage_0 = _jspx_th_ctl_errorMessage_0.doStartTag();
    if (_jspx_eval_ctl_errorMessage_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_errorMessage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_errorMessage_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_errorMessage_0.doInitBody();
      }
      do {
        out.write("\n    <span class=\"errorMessage\">\n      <center>\n        Validation error: please check your input and try again.\n      </center>\n    </span>\n  ");
        int evalDoAfterBody = _jspx_th_ctl_errorMessage_0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_errorMessage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_errorMessage_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_errorMessage.reuse(_jspx_th_ctl_errorMessage_0);
    return false;
  }

  private boolean _jspx_meth_ctl_extraHtmlAttribute_0(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_listbox_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:extraHtmlAttribute
    org.recipnet.common.controls.ExtraHtmlAttribute _jspx_th_ctl_extraHtmlAttribute_0 = (org.recipnet.common.controls.ExtraHtmlAttribute) _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.get(org.recipnet.common.controls.ExtraHtmlAttribute.class);
    _jspx_th_ctl_extraHtmlAttribute_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_extraHtmlAttribute_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_listbox_0);
    _jspx_th_ctl_extraHtmlAttribute_0.setName("tabindex");
    _jspx_th_ctl_extraHtmlAttribute_0.setValue("60");
    int _jspx_eval_ctl_extraHtmlAttribute_0 = _jspx_th_ctl_extraHtmlAttribute_0.doStartTag();
    if (_jspx_th_ctl_extraHtmlAttribute_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.reuse(_jspx_th_ctl_extraHtmlAttribute_0);
    return false;
  }

  private boolean _jspx_meth_ctl_option_0(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_listbox_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:option
    org.recipnet.common.controls.ListboxOption _jspx_th_ctl_option_0 = (org.recipnet.common.controls.ListboxOption) _jspx_tagPool_ctl_option.get(org.recipnet.common.controls.ListboxOption.class);
    _jspx_th_ctl_option_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_option_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_listbox_0);
    int _jspx_eval_ctl_option_0 = _jspx_th_ctl_option_0.doStartTag();
    if (_jspx_eval_ctl_option_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_option_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_option_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_option_0.doInitBody();
      }
      do {
        out.write('5');
        int evalDoAfterBody = _jspx_th_ctl_option_0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_option_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_option_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_option.reuse(_jspx_th_ctl_option_0);
    return false;
  }

  private boolean _jspx_meth_ctl_option_1(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_listbox_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:option
    org.recipnet.common.controls.ListboxOption _jspx_th_ctl_option_1 = (org.recipnet.common.controls.ListboxOption) _jspx_tagPool_ctl_option.get(org.recipnet.common.controls.ListboxOption.class);
    _jspx_th_ctl_option_1.setPageContext(_jspx_page_context);
    _jspx_th_ctl_option_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_listbox_0);
    int _jspx_eval_ctl_option_1 = _jspx_th_ctl_option_1.doStartTag();
    if (_jspx_eval_ctl_option_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_option_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_option_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_option_1.doInitBody();
      }
      do {
        out.write('1');
        out.write('0');
        int evalDoAfterBody = _jspx_th_ctl_option_1.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_option_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_option_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_option.reuse(_jspx_th_ctl_option_1);
    return false;
  }

  private boolean _jspx_meth_ctl_option_2(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_listbox_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:option
    org.recipnet.common.controls.ListboxOption _jspx_th_ctl_option_2 = (org.recipnet.common.controls.ListboxOption) _jspx_tagPool_ctl_option.get(org.recipnet.common.controls.ListboxOption.class);
    _jspx_th_ctl_option_2.setPageContext(_jspx_page_context);
    _jspx_th_ctl_option_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_listbox_0);
    int _jspx_eval_ctl_option_2 = _jspx_th_ctl_option_2.doStartTag();
    if (_jspx_eval_ctl_option_2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_option_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_option_2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_option_2.doInitBody();
      }
      do {
        out.write('2');
        out.write('5');
        int evalDoAfterBody = _jspx_th_ctl_option_2.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_option_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_option_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_option.reuse(_jspx_th_ctl_option_2);
    return false;
  }

  private boolean _jspx_meth_ctl_option_3(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_listbox_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:option
    org.recipnet.common.controls.ListboxOption _jspx_th_ctl_option_3 = (org.recipnet.common.controls.ListboxOption) _jspx_tagPool_ctl_option.get(org.recipnet.common.controls.ListboxOption.class);
    _jspx_th_ctl_option_3.setPageContext(_jspx_page_context);
    _jspx_th_ctl_option_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_listbox_0);
    int _jspx_eval_ctl_option_3 = _jspx_th_ctl_option_3.doStartTag();
    if (_jspx_eval_ctl_option_3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_option_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_option_3.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_option_3.doInitBody();
      }
      do {
        out.write('5');
        out.write('0');
        int evalDoAfterBody = _jspx_th_ctl_option_3.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_option_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_option_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_option.reuse(_jspx_th_ctl_option_3);
    return false;
  }

  private boolean _jspx_meth_ctl_option_4(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_listbox_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:option
    org.recipnet.common.controls.ListboxOption _jspx_th_ctl_option_4 = (org.recipnet.common.controls.ListboxOption) _jspx_tagPool_ctl_option.get(org.recipnet.common.controls.ListboxOption.class);
    _jspx_th_ctl_option_4.setPageContext(_jspx_page_context);
    _jspx_th_ctl_option_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_listbox_0);
    int _jspx_eval_ctl_option_4 = _jspx_th_ctl_option_4.doStartTag();
    if (_jspx_eval_ctl_option_4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_option_4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_option_4.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_option_4.doInitBody();
      }
      do {
        out.write('1');
        out.write('0');
        out.write('0');
        int evalDoAfterBody = _jspx_th_ctl_option_4.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_option_4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_option_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_option.reuse(_jspx_th_ctl_option_4);
    return false;
  }

  private boolean _jspx_meth_ctl_option_5(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_listbox_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:option
    org.recipnet.common.controls.ListboxOption _jspx_th_ctl_option_5 = (org.recipnet.common.controls.ListboxOption) _jspx_tagPool_ctl_option.get(org.recipnet.common.controls.ListboxOption.class);
    _jspx_th_ctl_option_5.setPageContext(_jspx_page_context);
    _jspx_th_ctl_option_5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_listbox_0);
    int _jspx_eval_ctl_option_5 = _jspx_th_ctl_option_5.doStartTag();
    if (_jspx_eval_ctl_option_5 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_option_5 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_option_5.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_option_5.doInitBody();
      }
      do {
        out.write('5');
        out.write('0');
        out.write('0');
        int evalDoAfterBody = _jspx_th_ctl_option_5.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_option_5 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_option_5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_option.reuse(_jspx_th_ctl_option_5);
    return false;
  }

  private boolean _jspx_meth_ctl_extraHtmlAttribute_1(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_selfForm_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:extraHtmlAttribute
    org.recipnet.common.controls.ExtraHtmlAttribute _jspx_th_ctl_extraHtmlAttribute_1 = (org.recipnet.common.controls.ExtraHtmlAttribute) _jspx_tagPool_ctl_extraHtmlAttribute_value_name_attributeAccepter_nobody.get(org.recipnet.common.controls.ExtraHtmlAttribute.class);
    _jspx_th_ctl_extraHtmlAttribute_1.setPageContext(_jspx_page_context);
    _jspx_th_ctl_extraHtmlAttribute_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
    _jspx_th_ctl_extraHtmlAttribute_1.setName("tabindex");
    _jspx_th_ctl_extraHtmlAttribute_1.setValue("1000");
    _jspx_th_ctl_extraHtmlAttribute_1.setAttributeAccepter((org.recipnet.common.controls.ExtraHtmlAttributeAccepter) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${searchButton}", org.recipnet.common.controls.ExtraHtmlAttributeAccepter.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_ctl_extraHtmlAttribute_1 = _jspx_th_ctl_extraHtmlAttribute_1.doStartTag();
    if (_jspx_th_ctl_extraHtmlAttribute_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_extraHtmlAttribute_value_name_attributeAccepter_nobody.reuse(_jspx_th_ctl_extraHtmlAttribute_1);
    return false;
  }

  private boolean _jspx_meth_ctl_styleBlock_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_searchPage_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:styleBlock
    org.recipnet.common.controls.HtmlPageStyleBlock _jspx_th_ctl_styleBlock_0 = (org.recipnet.common.controls.HtmlPageStyleBlock) _jspx_tagPool_ctl_styleBlock.get(org.recipnet.common.controls.HtmlPageStyleBlock.class);
    _jspx_th_ctl_styleBlock_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_styleBlock_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_searchPage_0);
    int _jspx_eval_ctl_styleBlock_0 = _jspx_th_ctl_styleBlock_0.doStartTag();
    if (_jspx_eval_ctl_styleBlock_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_styleBlock_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_styleBlock_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_styleBlock_0.doInitBody();
      }
      do {
        out.write("\n    .errorMessageSmall  { color: #F00000; font-size: x-small; }\n    .supplementalInstructions { color: gray; font-style: italic; }\n    table.searchTable  { border: thin solid #CCCCCC; }\n    table.searchTable th,\n    table.searchTable td { background-color: #EBEBEB }\n    table.searchTable th { text-align: right; white-space: nowrap;}\n    table.searchTable td { text-align: left; }\n    table.searchTable th.sectionHeader { text-align: left;\n        padding: 0.5em 0 0.1em 0.25em; background-color: #CCCCCC; }\n  ");
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
