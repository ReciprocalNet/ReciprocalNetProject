package org.recipnet.site.content.lab;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.recipnet.site.content.rncontrols.WapPage;
import org.recipnet.site.shared.bl.SampleWorkflowBL;

public final class customfields_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static java.util.Vector _jspx_dependants;

  static {
    _jspx_dependants = new java.util.Vector(2);
    _jspx_dependants.add("/WEB-INF/controls.tld");
    _jspx_dependants.add("/WEB-INF/rncontrols.tld");
  }

  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_wapPage_workflowActionCodeCorrected_workflowActionCode_title_loginPageUrl_editSamplePageHref_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_selfForm;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleChecker_includeIfNoVisibleLtas;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_wapCancelButton_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleChecker_invert_includeIfNoVisibleLtas;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_errorMessage_errorFilter;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_ltaIterator_id;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleFieldLabel_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleField_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleFieldUnits_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_wapComments_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_wapSaveButton_nobody;

  public java.util.List getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _jspx_tagPool_rn_wapPage_workflowActionCodeCorrected_workflowActionCode_title_loginPageUrl_editSamplePageHref_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_selfForm = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleChecker_includeIfNoVisibleLtas = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_wapCancelButton_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleChecker_invert_includeIfNoVisibleLtas = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_errorMessage_errorFilter = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_ltaIterator_id = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleFieldLabel_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleField_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleFieldUnits_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_wapComments_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_wapSaveButton_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
  }

  public void _jspDestroy() {
    _jspx_tagPool_rn_wapPage_workflowActionCodeCorrected_workflowActionCode_title_loginPageUrl_editSamplePageHref_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName.release();
    _jspx_tagPool_ctl_selfForm.release();
    _jspx_tagPool_rn_sampleChecker_includeIfNoVisibleLtas.release();
    _jspx_tagPool_rn_wapCancelButton_nobody.release();
    _jspx_tagPool_rn_sampleChecker_invert_includeIfNoVisibleLtas.release();
    _jspx_tagPool_ctl_errorMessage_errorFilter.release();
    _jspx_tagPool_rn_ltaIterator_id.release();
    _jspx_tagPool_rn_sampleFieldLabel_nobody.release();
    _jspx_tagPool_rn_sampleField_nobody.release();
    _jspx_tagPool_rn_sampleFieldUnits_nobody.release();
    _jspx_tagPool_rn_wapComments_nobody.release();
    _jspx_tagPool_rn_wapSaveButton_nobody.release();
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
      //  rn:wapPage
      org.recipnet.site.content.rncontrols.WapPage _jspx_th_rn_wapPage_0 = (org.recipnet.site.content.rncontrols.WapPage) _jspx_tagPool_rn_wapPage_workflowActionCodeCorrected_workflowActionCode_title_loginPageUrl_editSamplePageHref_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName.get(org.recipnet.site.content.rncontrols.WapPage.class);
      _jspx_th_rn_wapPage_0.setPageContext(_jspx_page_context);
      _jspx_th_rn_wapPage_0.setParent(null);
      _jspx_th_rn_wapPage_0.setTitle("Modify Custom Fields");
      _jspx_th_rn_wapPage_0.setWorkflowActionCode( SampleWorkflowBL.MODIFIED_LTAS);
      _jspx_th_rn_wapPage_0.setWorkflowActionCodeCorrected(SampleWorkflowBL.MODIFIED_LTAS);
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
          out.write("\n  <div class=\"pageBody\">\n  ");
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
              out.write("\n    ");
              if (_jspx_meth_rn_sampleChecker_0(_jspx_th_ctl_selfForm_0, _jspx_page_context))
                return;
              out.write("\n    ");
              //  rn:sampleChecker
              org.recipnet.site.content.rncontrols.SampleChecker _jspx_th_rn_sampleChecker_1 = (org.recipnet.site.content.rncontrols.SampleChecker) _jspx_tagPool_rn_sampleChecker_invert_includeIfNoVisibleLtas.get(org.recipnet.site.content.rncontrols.SampleChecker.class);
              _jspx_th_rn_sampleChecker_1.setPageContext(_jspx_page_context);
              _jspx_th_rn_sampleChecker_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_sampleChecker_1.setIncludeIfNoVisibleLtas(true);
              _jspx_th_rn_sampleChecker_1.setInvert(true);
              int _jspx_eval_rn_sampleChecker_1 = _jspx_th_rn_sampleChecker_1.doStartTag();
              if (_jspx_eval_rn_sampleChecker_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_rn_sampleChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_rn_sampleChecker_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_rn_sampleChecker_1.doInitBody();
                }
                do {
                  out.write("\n      <p class=\"pageInstructions\">\n        Modify the custom fields below and click the &quot;Submit&quot; button to\n        record the changes.\n        ");
                  //  ctl:errorMessage
                  org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_0 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
                  _jspx_th_ctl_errorMessage_0.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_errorMessage_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleChecker_1);
                  _jspx_th_ctl_errorMessage_0.setErrorFilter( WapPage.NESTED_TAG_REPORTED_VALIDATION_ERROR );
                  int _jspx_eval_ctl_errorMessage_0 = _jspx_th_ctl_errorMessage_0.doStartTag();
                  if (_jspx_eval_ctl_errorMessage_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_errorMessage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_errorMessage_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_errorMessage_0.doInitBody();
                    }
                    do {
                      out.write("\n          <span class=\"errorMessage\"\n                style=\"font-weight: normal; font-style: italic;\">\n            You must address the flagged validation errors before the data\n            will be accepted.\n          </span>\n        ");
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
                  out.write("\n      </p>\n      <table class=\"bodyTable\">\n        ");
                  //  rn:ltaIterator
                  org.recipnet.site.content.rncontrols.LtaIterator ltas = null;
                  org.recipnet.site.content.rncontrols.LtaIterator _jspx_th_rn_ltaIterator_0 = (org.recipnet.site.content.rncontrols.LtaIterator) _jspx_tagPool_rn_ltaIterator_id.get(org.recipnet.site.content.rncontrols.LtaIterator.class);
                  _jspx_th_rn_ltaIterator_0.setPageContext(_jspx_page_context);
                  _jspx_th_rn_ltaIterator_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleChecker_1);
                  _jspx_th_rn_ltaIterator_0.setId("ltas");
                  int _jspx_eval_rn_ltaIterator_0 = _jspx_th_rn_ltaIterator_0.doStartTag();
                  if (_jspx_eval_rn_ltaIterator_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_rn_ltaIterator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_rn_ltaIterator_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_rn_ltaIterator_0.doInitBody();
                    }
                    ltas = (org.recipnet.site.content.rncontrols.LtaIterator) _jspx_page_context.findAttribute("ltas");
                    do {
                      out.write("\n          ");
                      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${ltas.currentIterationFirst\n            ? '<tr><th class=\"leadSectionHead\" colspan=\"2\">Custom Fields</th></tr>'\n            : ''}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
                      out.write("\n          <tr>\n            <th>\n              ");
                      if (_jspx_meth_rn_sampleFieldLabel_0(_jspx_th_rn_ltaIterator_0, _jspx_page_context))
                        return;
                      out.write(":\n            </th>\n            <td>\n              ");
                      if (_jspx_meth_rn_sampleField_0(_jspx_th_rn_ltaIterator_0, _jspx_page_context))
                        return;
                      if (_jspx_meth_rn_sampleFieldUnits_0(_jspx_th_rn_ltaIterator_0, _jspx_page_context))
                        return;
                      out.write("\n            </td>\n          </tr>\n        ");
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
                  _jspx_tagPool_rn_ltaIterator_id.reuse(_jspx_th_rn_ltaIterator_0);
                  out.write("\n        <tr>\n          <th class=\"sectionHead\" colspan=\"2\">Comments</th>\n        </tr>\n        <tr>\n          <td colspan=\"2\" style=\"text-align: center;\">\n            ");
                  if (_jspx_meth_rn_wapComments_0(_jspx_th_rn_sampleChecker_1, _jspx_page_context))
                    return;
                  out.write("\n          </td>\n        </tr>\n        <tr>\n          <td colspan=\"2\" class=\"formButtons\">\n            ");
                  if (_jspx_meth_rn_wapSaveButton_0(_jspx_th_rn_sampleChecker_1, _jspx_page_context))
                    return;
                  out.write("\n            ");
                  if (_jspx_meth_rn_wapCancelButton_1(_jspx_th_rn_sampleChecker_1, _jspx_page_context))
                    return;
                  out.write("\n          </td>\n        </tr>\n      </table>\n    ");
                  int evalDoAfterBody = _jspx_th_rn_sampleChecker_1.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_rn_sampleChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_rn_sampleChecker_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_sampleChecker_invert_includeIfNoVisibleLtas.reuse(_jspx_th_rn_sampleChecker_1);
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
          out.write("\n  </div>\n");
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

  private boolean _jspx_meth_rn_sampleChecker_0(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_selfForm_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:sampleChecker
    org.recipnet.site.content.rncontrols.SampleChecker _jspx_th_rn_sampleChecker_0 = (org.recipnet.site.content.rncontrols.SampleChecker) _jspx_tagPool_rn_sampleChecker_includeIfNoVisibleLtas.get(org.recipnet.site.content.rncontrols.SampleChecker.class);
    _jspx_th_rn_sampleChecker_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_sampleChecker_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
    _jspx_th_rn_sampleChecker_0.setIncludeIfNoVisibleLtas(true);
    int _jspx_eval_rn_sampleChecker_0 = _jspx_th_rn_sampleChecker_0.doStartTag();
    if (_jspx_eval_rn_sampleChecker_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_sampleChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_sampleChecker_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_sampleChecker_0.doInitBody();
      }
      do {
        out.write("\n      <p class=\"errorMessage\">\n        No custom fields have been configured for this sample's\n        originating lab.  Local system administrators define custom\n        fields on a per-lab by editing the Reciprocal Net Site Software's\n        configuration file.  More information about this feature is\n        available in the User Guide.\n      </p>\n      ");
        if (_jspx_meth_rn_wapCancelButton_0(_jspx_th_rn_sampleChecker_0, _jspx_page_context))
          return true;
        out.write("\n    ");
        int evalDoAfterBody = _jspx_th_rn_sampleChecker_0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_sampleChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_sampleChecker_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_sampleChecker_includeIfNoVisibleLtas.reuse(_jspx_th_rn_sampleChecker_0);
    return false;
  }

  private boolean _jspx_meth_rn_wapCancelButton_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleChecker_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:wapCancelButton
    org.recipnet.site.content.rncontrols.WapCancelButton _jspx_th_rn_wapCancelButton_0 = (org.recipnet.site.content.rncontrols.WapCancelButton) _jspx_tagPool_rn_wapCancelButton_nobody.get(org.recipnet.site.content.rncontrols.WapCancelButton.class);
    _jspx_th_rn_wapCancelButton_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_wapCancelButton_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleChecker_0);
    int _jspx_eval_rn_wapCancelButton_0 = _jspx_th_rn_wapCancelButton_0.doStartTag();
    if (_jspx_th_rn_wapCancelButton_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_wapCancelButton_nobody.reuse(_jspx_th_rn_wapCancelButton_0);
    return false;
  }

  private boolean _jspx_meth_rn_sampleFieldLabel_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_ltaIterator_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:sampleFieldLabel
    org.recipnet.site.content.rncontrols.SampleFieldLabel _jspx_th_rn_sampleFieldLabel_0 = (org.recipnet.site.content.rncontrols.SampleFieldLabel) _jspx_tagPool_rn_sampleFieldLabel_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldLabel.class);
    _jspx_th_rn_sampleFieldLabel_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_sampleFieldLabel_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_ltaIterator_0);
    int _jspx_eval_rn_sampleFieldLabel_0 = _jspx_th_rn_sampleFieldLabel_0.doStartTag();
    if (_jspx_th_rn_sampleFieldLabel_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_sampleFieldLabel_nobody.reuse(_jspx_th_rn_sampleFieldLabel_0);
    return false;
  }

  private boolean _jspx_meth_rn_sampleField_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_ltaIterator_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:sampleField
    org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_0 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
    _jspx_th_rn_sampleField_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_sampleField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_ltaIterator_0);
    int _jspx_eval_rn_sampleField_0 = _jspx_th_rn_sampleField_0.doStartTag();
    if (_jspx_th_rn_sampleField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_sampleField_nobody.reuse(_jspx_th_rn_sampleField_0);
    return false;
  }

  private boolean _jspx_meth_rn_sampleFieldUnits_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_ltaIterator_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:sampleFieldUnits
    org.recipnet.site.content.rncontrols.SampleFieldUnits _jspx_th_rn_sampleFieldUnits_0 = (org.recipnet.site.content.rncontrols.SampleFieldUnits) _jspx_tagPool_rn_sampleFieldUnits_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldUnits.class);
    _jspx_th_rn_sampleFieldUnits_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_sampleFieldUnits_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_ltaIterator_0);
    int _jspx_eval_rn_sampleFieldUnits_0 = _jspx_th_rn_sampleFieldUnits_0.doStartTag();
    if (_jspx_th_rn_sampleFieldUnits_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_sampleFieldUnits_nobody.reuse(_jspx_th_rn_sampleFieldUnits_0);
    return false;
  }

  private boolean _jspx_meth_rn_wapComments_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleChecker_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:wapComments
    org.recipnet.site.content.rncontrols.WapComments _jspx_th_rn_wapComments_0 = (org.recipnet.site.content.rncontrols.WapComments) _jspx_tagPool_rn_wapComments_nobody.get(org.recipnet.site.content.rncontrols.WapComments.class);
    _jspx_th_rn_wapComments_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_wapComments_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleChecker_1);
    int _jspx_eval_rn_wapComments_0 = _jspx_th_rn_wapComments_0.doStartTag();
    if (_jspx_th_rn_wapComments_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_wapComments_nobody.reuse(_jspx_th_rn_wapComments_0);
    return false;
  }

  private boolean _jspx_meth_rn_wapSaveButton_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleChecker_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:wapSaveButton
    org.recipnet.site.content.rncontrols.WapSaveButton _jspx_th_rn_wapSaveButton_0 = (org.recipnet.site.content.rncontrols.WapSaveButton) _jspx_tagPool_rn_wapSaveButton_nobody.get(org.recipnet.site.content.rncontrols.WapSaveButton.class);
    _jspx_th_rn_wapSaveButton_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_wapSaveButton_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleChecker_1);
    int _jspx_eval_rn_wapSaveButton_0 = _jspx_th_rn_wapSaveButton_0.doStartTag();
    if (_jspx_th_rn_wapSaveButton_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_wapSaveButton_nobody.reuse(_jspx_th_rn_wapSaveButton_0);
    return false;
  }

  private boolean _jspx_meth_rn_wapCancelButton_1(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleChecker_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:wapCancelButton
    org.recipnet.site.content.rncontrols.WapCancelButton _jspx_th_rn_wapCancelButton_1 = (org.recipnet.site.content.rncontrols.WapCancelButton) _jspx_tagPool_rn_wapCancelButton_nobody.get(org.recipnet.site.content.rncontrols.WapCancelButton.class);
    _jspx_th_rn_wapCancelButton_1.setPageContext(_jspx_page_context);
    _jspx_th_rn_wapCancelButton_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleChecker_1);
    int _jspx_eval_rn_wapCancelButton_1 = _jspx_th_rn_wapCancelButton_1.doStartTag();
    if (_jspx_th_rn_wapCancelButton_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_wapCancelButton_nobody.reuse(_jspx_th_rn_wapCancelButton_1);
    return false;
  }
}
