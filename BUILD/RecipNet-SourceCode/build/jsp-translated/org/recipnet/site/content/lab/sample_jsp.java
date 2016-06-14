package org.recipnet.site.content.lab;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.recipnet.common.controls.HtmlPageIterator;
import org.recipnet.site.content.rncontrols.DataDirectoryField;
import org.recipnet.site.content.rncontrols.EditSamplePage;
import org.recipnet.site.content.rncontrols.FileField;
import org.recipnet.site.content.rncontrols.FileIterator;
import org.recipnet.site.content.rncontrols.LabField;
import org.recipnet.site.content.rncontrols.PreferenceField;
import org.recipnet.site.content.rncontrols.PreferenceField.PrefType;
import org.recipnet.site.content.rncontrols.SampleActionFieldIterator;
import org.recipnet.site.content.rncontrols.SampleActionFileIterator;
import org.recipnet.site.content.rncontrols.SampleActionIterator;
import org.recipnet.site.content.rncontrols.SampleField;
import org.recipnet.site.content.rncontrols.SampleHistoryField;
import org.recipnet.site.shared.bl.SampleTextBL;
import org.recipnet.site.shared.bl.SampleWorkflowBL;
import org.recipnet.site.shared.bl.UserBL;
import org.recipnet.site.shared.db.SampleInfo;

public final class sample_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static java.util.Vector _jspx_dependants;

  static {
    _jspx_dependants = new java.util.Vector(2);
    _jspx_dependants.add("/WEB-INF/controls.tld");
    _jspx_dependants.add("/WEB-INF/rncontrols.tld");
  }

  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_editSamplePage_unsetSuppressionPreferenceParamName_title_loginPageUrl_ignoreSampleHistoryId_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_selfForm;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_errorMessage_errorFilter;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_img_styleClass_src_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleField_fieldCode_displayValueOnly_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_labField_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_providerField_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_link_href;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_showsampleLink_sampleIsKnownToBeLocal_detailedPageUrl_basicPageUrl;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleParam_name_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_statusDisplay_unselectedStatusPatternHtml_selectedStatusPatternHtml_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_fileIterator_id;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_parityChecker_includeOnlyOnFirst;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_fileField_fieldCode_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_fileChecker_includeOnlyIfFileHasDescription;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_preferenceChecker_invert_includeIfBooleanPrefIsTrue;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_parityChecker_includeOnlyOnLast;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_dataDirectoryField_fileIterator_directoryPart_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_dataDirectoryField_id_fileIterator_directoryPart_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_createDataDirectoryButton_label_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_errorMessage_errorSupplier;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_errorMessage_invertFilter_errorSupplier_errorFilter;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_uploadFilesButton_label_formBasedPageUrl_dragAndDropPageUrl_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_buttonLink_target_label_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_browserSpecific_notNetscape4x;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_intIterator_ints_id;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_preferenceField_preferenceType_id_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_storePreferencesButton_label_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_phaseEvent_onPhases;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleActionIterator_id;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleHistoryField_fieldCode_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_errorChecker_errorFilter;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_errorChecker_invert_errorFilter;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_actionIcon_styleClass_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_correctionCount_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleActionFieldIterator_id;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_phaseEvent_skipIfSuppressed_onPhases;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleFieldLabel_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleChecker_includeIfSampleFieldContextProvidesValue;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleField_displayValueOnly_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleFieldUnits_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleTextChecker_includeOnlyForAnnotationsWithReferences;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleContextTranslator_translateFromAnnotationReferenceSample;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_showsampleLink_jumpSitePageUrl_detailedPageUrl_basicPageUrl;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_labField_fieldCode_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleChecker_invert_includeIfSampleFieldContextProvidesValue;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_errorChecker_invert_errorSupplier_errorFilter;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_errorChecker_errorSupplier_errorFilter;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_suppressedFieldCount_fieldIterator_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_if_invert_conditionMet;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_correctActionButton_withdrawnByProviderCorrectionPageUrl_suspendedCorrectionPageUrl_submittedCorrectionPageUrl_structureRefinedCorrectionPageUrl_resumedCorrectionPageUrl_releasedToPublicCorrectionPageUrl_preliminaryDataCollectedCorrectionPageUrl_label_failedToCollectCorrectionPageUrl_declaredNonScsCorrectionPageUrl_declaredIncompleteCorrectionPageUrl_citationCorrectionPageUrl_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleActionFileIterator_mode_id;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_parityChecker_invert_includeOnlyOnFirst;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_fileField_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_workflowCommentChecker;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_link_style_href;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_linkParam_value_name_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_suppressedActionCount_includeFileActionsInCount_actionIterator_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_suppressedActionCount_includeCorrectionActionsInCount_actionIterator_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_suppressedActionCount_includeOtherActionsInCount_actionIterator_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_suppressedActionCount_includeSkippedActionsInCount_actionIterator_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleChecker_includeIfActionIsValid;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_link_styleClass_href;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleChecker_invert_includeIfActionIsValid;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_img_src_alt_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_styleBlock;

  public java.util.List getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _jspx_tagPool_rn_editSamplePage_unsetSuppressionPreferenceParamName_title_loginPageUrl_ignoreSampleHistoryId_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_selfForm = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_errorMessage_errorFilter = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_img_styleClass_src_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleField_fieldCode_displayValueOnly_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_labField_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_providerField_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_link_href = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_showsampleLink_sampleIsKnownToBeLocal_detailedPageUrl_basicPageUrl = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleParam_name_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_statusDisplay_unselectedStatusPatternHtml_selectedStatusPatternHtml_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_fileIterator_id = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_parityChecker_includeOnlyOnFirst = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_fileField_fieldCode_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_fileChecker_includeOnlyIfFileHasDescription = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_preferenceChecker_invert_includeIfBooleanPrefIsTrue = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_parityChecker_includeOnlyOnLast = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_dataDirectoryField_fileIterator_directoryPart_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_dataDirectoryField_id_fileIterator_directoryPart_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_createDataDirectoryButton_label_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_errorMessage_errorSupplier = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_errorMessage_invertFilter_errorSupplier_errorFilter = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_uploadFilesButton_label_formBasedPageUrl_dragAndDropPageUrl_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_buttonLink_target_label_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_browserSpecific_notNetscape4x = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_intIterator_ints_id = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_preferenceField_preferenceType_id_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_storePreferencesButton_label_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_phaseEvent_onPhases = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleActionIterator_id = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleHistoryField_fieldCode_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_errorChecker_errorFilter = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_errorChecker_invert_errorFilter = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_actionIcon_styleClass_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_correctionCount_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleActionFieldIterator_id = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_phaseEvent_skipIfSuppressed_onPhases = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleFieldLabel_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleChecker_includeIfSampleFieldContextProvidesValue = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleField_displayValueOnly_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleFieldUnits_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleTextChecker_includeOnlyForAnnotationsWithReferences = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleContextTranslator_translateFromAnnotationReferenceSample = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_showsampleLink_jumpSitePageUrl_detailedPageUrl_basicPageUrl = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_labField_fieldCode_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleChecker_invert_includeIfSampleFieldContextProvidesValue = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_errorChecker_invert_errorSupplier_errorFilter = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_errorChecker_errorSupplier_errorFilter = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_suppressedFieldCount_fieldIterator_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_if_invert_conditionMet = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_correctActionButton_withdrawnByProviderCorrectionPageUrl_suspendedCorrectionPageUrl_submittedCorrectionPageUrl_structureRefinedCorrectionPageUrl_resumedCorrectionPageUrl_releasedToPublicCorrectionPageUrl_preliminaryDataCollectedCorrectionPageUrl_label_failedToCollectCorrectionPageUrl_declaredNonScsCorrectionPageUrl_declaredIncompleteCorrectionPageUrl_citationCorrectionPageUrl_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleActionFileIterator_mode_id = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_parityChecker_invert_includeOnlyOnFirst = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_fileField_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_workflowCommentChecker = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_link_style_href = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_linkParam_value_name_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_suppressedActionCount_includeFileActionsInCount_actionIterator_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_suppressedActionCount_includeCorrectionActionsInCount_actionIterator_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_suppressedActionCount_includeOtherActionsInCount_actionIterator_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_suppressedActionCount_includeSkippedActionsInCount_actionIterator_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleChecker_includeIfActionIsValid = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_link_styleClass_href = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleChecker_invert_includeIfActionIsValid = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_img_src_alt_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_styleBlock = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
  }

  public void _jspDestroy() {
    _jspx_tagPool_rn_editSamplePage_unsetSuppressionPreferenceParamName_title_loginPageUrl_ignoreSampleHistoryId_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName.release();
    _jspx_tagPool_ctl_selfForm.release();
    _jspx_tagPool_ctl_errorMessage_errorFilter.release();
    _jspx_tagPool_ctl_img_styleClass_src_nobody.release();
    _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.release();
    _jspx_tagPool_rn_sampleField_fieldCode_displayValueOnly_nobody.release();
    _jspx_tagPool_rn_labField_nobody.release();
    _jspx_tagPool_rn_providerField_nobody.release();
    _jspx_tagPool_rn_link_href.release();
    _jspx_tagPool_rn_showsampleLink_sampleIsKnownToBeLocal_detailedPageUrl_basicPageUrl.release();
    _jspx_tagPool_rn_sampleParam_name_nobody.release();
    _jspx_tagPool_rn_statusDisplay_unselectedStatusPatternHtml_selectedStatusPatternHtml_nobody.release();
    _jspx_tagPool_rn_fileIterator_id.release();
    _jspx_tagPool_ctl_parityChecker_includeOnlyOnFirst.release();
    _jspx_tagPool_rn_fileField_fieldCode_nobody.release();
    _jspx_tagPool_rn_fileChecker_includeOnlyIfFileHasDescription.release();
    _jspx_tagPool_rn_preferenceChecker_invert_includeIfBooleanPrefIsTrue.release();
    _jspx_tagPool_ctl_parityChecker_includeOnlyOnLast.release();
    _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.release();
    _jspx_tagPool_rn_dataDirectoryField_fileIterator_directoryPart_nobody.release();
    _jspx_tagPool_rn_dataDirectoryField_id_fileIterator_directoryPart_nobody.release();
    _jspx_tagPool_rn_createDataDirectoryButton_label_nobody.release();
    _jspx_tagPool_ctl_errorMessage_errorSupplier.release();
    _jspx_tagPool_ctl_errorMessage_invertFilter_errorSupplier_errorFilter.release();
    _jspx_tagPool_rn_uploadFilesButton_label_formBasedPageUrl_dragAndDropPageUrl_nobody.release();
    _jspx_tagPool_rn_buttonLink_target_label_nobody.release();
    _jspx_tagPool_ctl_browserSpecific_notNetscape4x.release();
    _jspx_tagPool_ctl_intIterator_ints_id.release();
    _jspx_tagPool_rn_preferenceField_preferenceType_id_nobody.release();
    _jspx_tagPool_rn_storePreferencesButton_label_nobody.release();
    _jspx_tagPool_ctl_phaseEvent_onPhases.release();
    _jspx_tagPool_rn_sampleActionIterator_id.release();
    _jspx_tagPool_rn_sampleHistoryField_fieldCode_nobody.release();
    _jspx_tagPool_ctl_errorChecker_errorFilter.release();
    _jspx_tagPool_ctl_errorChecker_invert_errorFilter.release();
    _jspx_tagPool_rn_actionIcon_styleClass_nobody.release();
    _jspx_tagPool_rn_correctionCount_nobody.release();
    _jspx_tagPool_rn_sampleActionFieldIterator_id.release();
    _jspx_tagPool_ctl_phaseEvent_skipIfSuppressed_onPhases.release();
    _jspx_tagPool_rn_sampleFieldLabel_nobody.release();
    _jspx_tagPool_rn_sampleChecker_includeIfSampleFieldContextProvidesValue.release();
    _jspx_tagPool_rn_sampleField_displayValueOnly_nobody.release();
    _jspx_tagPool_rn_sampleFieldUnits_nobody.release();
    _jspx_tagPool_rn_sampleTextChecker_includeOnlyForAnnotationsWithReferences.release();
    _jspx_tagPool_rn_sampleContextTranslator_translateFromAnnotationReferenceSample.release();
    _jspx_tagPool_rn_showsampleLink_jumpSitePageUrl_detailedPageUrl_basicPageUrl.release();
    _jspx_tagPool_rn_labField_fieldCode_nobody.release();
    _jspx_tagPool_rn_sampleChecker_invert_includeIfSampleFieldContextProvidesValue.release();
    _jspx_tagPool_ctl_errorChecker_invert_errorSupplier_errorFilter.release();
    _jspx_tagPool_ctl_errorChecker_errorSupplier_errorFilter.release();
    _jspx_tagPool_rn_suppressedFieldCount_fieldIterator_nobody.release();
    _jspx_tagPool_ctl_if_invert_conditionMet.release();
    _jspx_tagPool_rn_correctActionButton_withdrawnByProviderCorrectionPageUrl_suspendedCorrectionPageUrl_submittedCorrectionPageUrl_structureRefinedCorrectionPageUrl_resumedCorrectionPageUrl_releasedToPublicCorrectionPageUrl_preliminaryDataCollectedCorrectionPageUrl_label_failedToCollectCorrectionPageUrl_declaredNonScsCorrectionPageUrl_declaredIncompleteCorrectionPageUrl_citationCorrectionPageUrl_nobody.release();
    _jspx_tagPool_rn_sampleActionFileIterator_mode_id.release();
    _jspx_tagPool_ctl_parityChecker_invert_includeOnlyOnFirst.release();
    _jspx_tagPool_rn_fileField_nobody.release();
    _jspx_tagPool_rn_workflowCommentChecker.release();
    _jspx_tagPool_rn_link_style_href.release();
    _jspx_tagPool_ctl_linkParam_value_name_nobody.release();
    _jspx_tagPool_rn_suppressedActionCount_includeFileActionsInCount_actionIterator_nobody.release();
    _jspx_tagPool_rn_suppressedActionCount_includeCorrectionActionsInCount_actionIterator_nobody.release();
    _jspx_tagPool_rn_suppressedActionCount_includeOtherActionsInCount_actionIterator_nobody.release();
    _jspx_tagPool_rn_suppressedActionCount_includeSkippedActionsInCount_actionIterator_nobody.release();
    _jspx_tagPool_rn_sampleChecker_includeIfActionIsValid.release();
    _jspx_tagPool_rn_link_styleClass_href.release();
    _jspx_tagPool_rn_sampleChecker_invert_includeIfActionIsValid.release();
    _jspx_tagPool_ctl_img_src_alt_nobody.release();
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

      out.write("\n\n\n\n");
      //  rn:editSamplePage
      org.recipnet.site.content.rncontrols.EditSamplePage _jspx_th_rn_editSamplePage_0 = (org.recipnet.site.content.rncontrols.EditSamplePage) _jspx_tagPool_rn_editSamplePage_unsetSuppressionPreferenceParamName_title_loginPageUrl_ignoreSampleHistoryId_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName.get(org.recipnet.site.content.rncontrols.EditSamplePage.class);
      _jspx_th_rn_editSamplePage_0.setPageContext(_jspx_page_context);
      _jspx_th_rn_editSamplePage_0.setParent(null);
      _jspx_th_rn_editSamplePage_0.setTitle("Edit Sample");
      _jspx_th_rn_editSamplePage_0.setIgnoreSampleHistoryId(true);
      _jspx_th_rn_editSamplePage_0.setUnsetSuppressionPreferenceParamName("unsetSuppression");
      _jspx_th_rn_editSamplePage_0.setLoginPageUrl("/login.jsp");
      _jspx_th_rn_editSamplePage_0.setCurrentPageReinvocationUrlParamName("origUrl");
      _jspx_th_rn_editSamplePage_0.setAuthorizationFailedReasonParamName("authorizationFailedReason");
      int _jspx_eval_rn_editSamplePage_0 = _jspx_th_rn_editSamplePage_0.doStartTag();
      if (_jspx_eval_rn_editSamplePage_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        org.recipnet.site.content.rncontrols.EditSamplePage htmlPage = null;
        if (_jspx_eval_rn_editSamplePage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
          out = _jspx_page_context.pushBody();
          _jspx_th_rn_editSamplePage_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
          _jspx_th_rn_editSamplePage_0.doInitBody();
        }
        htmlPage = (org.recipnet.site.content.rncontrols.EditSamplePage) _jspx_page_context.findAttribute("htmlPage");
        do {
          out.write("\n  <div class=\"pageBody\">\n  ");
          //  ctl:selfForm
          org.recipnet.common.controls.FormHtmlElement _jspx_th_ctl_selfForm_0 = (org.recipnet.common.controls.FormHtmlElement) _jspx_tagPool_ctl_selfForm.get(org.recipnet.common.controls.FormHtmlElement.class);
          _jspx_th_ctl_selfForm_0.setPageContext(_jspx_page_context);
          _jspx_th_ctl_selfForm_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_editSamplePage_0);
          int _jspx_eval_ctl_selfForm_0 = _jspx_th_ctl_selfForm_0.doStartTag();
          if (_jspx_eval_ctl_selfForm_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_ctl_selfForm_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_ctl_selfForm_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_ctl_selfForm_0.doInitBody();
            }
            do {
              out.write("\n    <table cellspacing=\"0\" border=\"0\" cellpadding=\"0\">\n      <tr>\n        <td style=\"text-align: left; vertical-align: top; width: 300px;\">\n        ");
              //  ctl:errorMessage
              org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_0 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
              _jspx_th_ctl_errorMessage_0.setPageContext(_jspx_page_context);
              _jspx_th_ctl_errorMessage_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_ctl_errorMessage_0.setErrorFilter(
              EditSamplePage.NESTED_TAG_REPORTED_VALIDATION_ERROR);
              int _jspx_eval_ctl_errorMessage_0 = _jspx_th_ctl_errorMessage_0.doStartTag();
              if (_jspx_eval_ctl_errorMessage_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_ctl_errorMessage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_ctl_errorMessage_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_ctl_errorMessage_0.doInitBody();
                }
                do {
                  out.write("\n          <div class=\"errorBox\">\n            <div class=\"errorBoxTitle\">\n              ");
                  if (_jspx_meth_ctl_img_0(_jspx_th_ctl_errorMessage_0, _jspx_page_context))
                    return;
                  out.write("\n              Error\n            </div>\n            <div class=\"errorBoxBody\">\n              <div>\n                One or more errors were encountered while processing this\n                page. Please correct the indicated input boxes and\n                resubmit the form.\n              </div>\n              <div style=\"text-align: right\">\n                (<a href=\"#error\">jump to error on this page</a>)\n              </div>\n            </div>\n          </div>\n        ");
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
              out.write("\n        <div class=\"infoBox\">\n          <div class=\"infoBoxTitle\">\n            ");
              if (_jspx_meth_ctl_img_1(_jspx_th_ctl_selfForm_0, _jspx_page_context))
                return;
              out.write("\n            Basic Information\n          </div>\n          <div class=\"infoBoxBody\">\n            <table cellspacing=\"0\">\n              <tr>\n                <th>");
              //  rn:sampleFieldLabel
              org.recipnet.site.content.rncontrols.SampleFieldLabel _jspx_th_rn_sampleFieldLabel_0 = (org.recipnet.site.content.rncontrols.SampleFieldLabel) _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldLabel.class);
              _jspx_th_rn_sampleFieldLabel_0.setPageContext(_jspx_page_context);
              _jspx_th_rn_sampleFieldLabel_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_sampleFieldLabel_0.setFieldCode( SampleInfo.LOCAL_LAB_ID );
              int _jspx_eval_rn_sampleFieldLabel_0 = _jspx_th_rn_sampleFieldLabel_0.doStartTag();
              if (_jspx_th_rn_sampleFieldLabel_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.reuse(_jspx_th_rn_sampleFieldLabel_0);
              out.write(":</th>\n                <td>");
              //  rn:sampleField
              org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_0 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_fieldCode_displayValueOnly_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
              _jspx_th_rn_sampleField_0.setPageContext(_jspx_page_context);
              _jspx_th_rn_sampleField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_sampleField_0.setDisplayValueOnly(true);
              _jspx_th_rn_sampleField_0.setFieldCode( SampleInfo.LOCAL_LAB_ID );
              int _jspx_eval_rn_sampleField_0 = _jspx_th_rn_sampleField_0.doStartTag();
              if (_jspx_th_rn_sampleField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_sampleField_fieldCode_displayValueOnly_nobody.reuse(_jspx_th_rn_sampleField_0);
              out.write("</td>\n              </tr>\n              <tr>\n                <th>Lab name:</th>\n                <td>");
              if (_jspx_meth_rn_labField_0(_jspx_th_ctl_selfForm_0, _jspx_page_context))
                return;
              out.write("</td>\n              </tr>\n              <tr>\n                <th>Provided by group:</th>\n                <td>");
              if (_jspx_meth_rn_providerField_0(_jspx_th_ctl_selfForm_0, _jspx_page_context))
                return;
              out.write("</td>\n              </tr>\n            </table>\n          </div>\n        </div>\n        <div class=\"infoBox\">\n          <div class=\"infoBoxTitle\">\n            ");
              if (_jspx_meth_ctl_img_2(_jspx_th_ctl_selfForm_0, _jspx_page_context))
                return;
              out.write("\n            Other views\n          </div>\n          <div class=\"infoBoxBody\">\n            <ul>\n              <li>\n                ");
              if (_jspx_meth_rn_link_0(_jspx_th_ctl_selfForm_0, _jspx_page_context))
                return;
              out.write("\n              </li>\n              <li>\n                ");
              if (_jspx_meth_rn_showsampleLink_0(_jspx_th_ctl_selfForm_0, _jspx_page_context))
                return;
              out.write("\n              </li>\n              <li>\n                ");
              if (_jspx_meth_rn_link_1(_jspx_th_ctl_selfForm_0, _jspx_page_context))
                return;
              out.write("\n              </li>\n            </ul>\n          </div>\n        </div>\n        <div class=\"infoBox\">\n          <div class=\"infoBoxTitle\">\n            ");
              if (_jspx_meth_ctl_img_3(_jspx_th_ctl_selfForm_0, _jspx_page_context))
                return;
              out.write("\n            Status\n          </div>\n          <div class=\"infoBoxBody\">\n            <ul>\n              ");
              if (_jspx_meth_rn_statusDisplay_0(_jspx_th_ctl_selfForm_0, _jspx_page_context))
                return;
              out.write("\n            </ul>\n          </div>\n        </div>\n        <div class=\"infoBox\">\n          <div class=\"infoBoxTitle\">\n            ");
              if (_jspx_meth_ctl_img_4(_jspx_th_ctl_selfForm_0, _jspx_page_context))
                return;
              out.write("\n            Data files\n          </div>\n          <div class=\"infoBoxBody\">\n            ");
              //  rn:fileIterator
              org.recipnet.site.content.rncontrols.FileIterator files = null;
              org.recipnet.site.content.rncontrols.FileIterator _jspx_th_rn_fileIterator_0 = (org.recipnet.site.content.rncontrols.FileIterator) _jspx_tagPool_rn_fileIterator_id.get(org.recipnet.site.content.rncontrols.FileIterator.class);
              _jspx_th_rn_fileIterator_0.setPageContext(_jspx_page_context);
              _jspx_th_rn_fileIterator_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_fileIterator_0.setId("files");
              int _jspx_eval_rn_fileIterator_0 = _jspx_th_rn_fileIterator_0.doStartTag();
              if (_jspx_eval_rn_fileIterator_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_rn_fileIterator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_rn_fileIterator_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_rn_fileIterator_0.doInitBody();
                }
                files = (org.recipnet.site.content.rncontrols.FileIterator) _jspx_page_context.findAttribute("files");
                do {
                  out.write("\n              ");
                  if (_jspx_meth_ctl_parityChecker_0(_jspx_th_rn_fileIterator_0, _jspx_page_context))
                    return;
                  out.write("\n                <li>");
                  //  rn:fileField
                  org.recipnet.site.content.rncontrols.FileField _jspx_th_rn_fileField_0 = (org.recipnet.site.content.rncontrols.FileField) _jspx_tagPool_rn_fileField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.FileField.class);
                  _jspx_th_rn_fileField_0.setPageContext(_jspx_page_context);
                  _jspx_th_rn_fileField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_fileIterator_0);
                  _jspx_th_rn_fileField_0.setFieldCode( FileField.LINKED_FILENAME);
                  int _jspx_eval_rn_fileField_0 = _jspx_th_rn_fileField_0.doStartTag();
                  if (_jspx_th_rn_fileField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_fileField_fieldCode_nobody.reuse(_jspx_th_rn_fileField_0);
                  out.write("\n                    (");
                  //  rn:fileField
                  org.recipnet.site.content.rncontrols.FileField _jspx_th_rn_fileField_1 = (org.recipnet.site.content.rncontrols.FileField) _jspx_tagPool_rn_fileField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.FileField.class);
                  _jspx_th_rn_fileField_1.setPageContext(_jspx_page_context);
                  _jspx_th_rn_fileField_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_fileIterator_0);
                  _jspx_th_rn_fileField_1.setFieldCode( FileField.FILE_SIZE);
                  int _jspx_eval_rn_fileField_1 = _jspx_th_rn_fileField_1.doStartTag();
                  if (_jspx_th_rn_fileField_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_fileField_fieldCode_nobody.reuse(_jspx_th_rn_fileField_1);
                  out.write(")\n                  ");
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
                      out.write("\n                    ");
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
                          out.write("\n                      <div class=\"fileDescription\">\n                        ");
                          //  rn:fileField
                          org.recipnet.site.content.rncontrols.FileField _jspx_th_rn_fileField_2 = (org.recipnet.site.content.rncontrols.FileField) _jspx_tagPool_rn_fileField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.FileField.class);
                          _jspx_th_rn_fileField_2.setPageContext(_jspx_page_context);
                          _jspx_th_rn_fileField_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_preferenceChecker_0);
                          _jspx_th_rn_fileField_2.setFieldCode( FileField.DESCRIPTION);
                          int _jspx_eval_rn_fileField_2 = _jspx_th_rn_fileField_2.doStartTag();
                          if (_jspx_th_rn_fileField_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          _jspx_tagPool_rn_fileField_fieldCode_nobody.reuse(_jspx_th_rn_fileField_2);
                          out.write("\n                      </div>\n                    ");
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
                      out.write("\n                  ");
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
                  out.write("\n                </li>\n                ");
                  if (_jspx_meth_ctl_parityChecker_1(_jspx_th_rn_fileIterator_0, _jspx_page_context))
                    return;
                  out.write("\n              ");
                  int evalDoAfterBody = _jspx_th_rn_fileIterator_0.doAfterBody();
                  files = (org.recipnet.site.content.rncontrols.FileIterator) _jspx_page_context.findAttribute("files");
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_rn_fileIterator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_rn_fileIterator_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              files = (org.recipnet.site.content.rncontrols.FileIterator) _jspx_page_context.findAttribute("files");
              _jspx_tagPool_rn_fileIterator_id.reuse(_jspx_th_rn_fileIterator_0);
              out.write("\n              ");
              //  ctl:errorMessage
              org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_1 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
              _jspx_th_ctl_errorMessage_1.setPageContext(_jspx_page_context);
              _jspx_th_ctl_errorMessage_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_ctl_errorMessage_1.setErrorSupplier(files);
              _jspx_th_ctl_errorMessage_1.setErrorFilter( FileIterator.NO_FILES_IN_REPOSITORY);
              int _jspx_eval_ctl_errorMessage_1 = _jspx_th_ctl_errorMessage_1.doStartTag();
              if (_jspx_eval_ctl_errorMessage_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_ctl_errorMessage_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_ctl_errorMessage_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_ctl_errorMessage_1.doInitBody();
                }
                do {
                  out.write("\n                There are no files in the repository.\n              ");
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
              _jspx_th_ctl_errorMessage_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_ctl_errorMessage_2.setErrorSupplier(files);
              _jspx_th_ctl_errorMessage_2.setErrorFilter(
                    FileIterator.DIRECTORY_BUT_NO_HOLDINGS
                    | FileIterator.NO_DIRECTORY_NO_HOLDINGS
                    | FileIterator.HOLDINGS_BUT_NO_DIRECTORY);
              int _jspx_eval_ctl_errorMessage_2 = _jspx_th_ctl_errorMessage_2.doStartTag();
              if (_jspx_eval_ctl_errorMessage_2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_ctl_errorMessage_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_ctl_errorMessage_2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_ctl_errorMessage_2.doInitBody();
                }
                do {
                  out.write("\n              <div class=\"errorMessage\"> The file repository is not configured\n                (correctly) for sample\n                ");
                  //  rn:sampleField
                  org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_1 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_fieldCode_displayValueOnly_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
                  _jspx_th_rn_sampleField_1.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleField_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_errorMessage_2);
                  _jspx_th_rn_sampleField_1.setFieldCode( SampleInfo.LOCAL_LAB_ID);
                  _jspx_th_rn_sampleField_1.setDisplayValueOnly(true);
                  int _jspx_eval_rn_sampleField_1 = _jspx_th_rn_sampleField_1.doStartTag();
                  if (_jspx_th_rn_sampleField_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_sampleField_fieldCode_displayValueOnly_nobody.reuse(_jspx_th_rn_sampleField_1);
                  out.write(":\n              </div>\n              ");
                  //  ctl:errorMessage
                  org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_3 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
                  _jspx_th_ctl_errorMessage_3.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_errorMessage_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_errorMessage_2);
                  _jspx_th_ctl_errorMessage_3.setErrorSupplier(files);
                  _jspx_th_ctl_errorMessage_3.setErrorFilter(
                  FileIterator.DIRECTORY_BUT_NO_HOLDINGS);
                  int _jspx_eval_ctl_errorMessage_3 = _jspx_th_ctl_errorMessage_3.doStartTag();
                  if (_jspx_eval_ctl_errorMessage_3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_errorMessage_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_errorMessage_3.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_errorMessage_3.doInitBody();
                    }
                    do {
                      out.write("\n                <div class=\"errorMessage\" style=\"margin-left: 1em;\">\n                  Data directory found but no holding record.<br />\n                </div>\n              ");
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
                  out.write("\n              ");
                  //  ctl:errorMessage
                  org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_4 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
                  _jspx_th_ctl_errorMessage_4.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_errorMessage_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_errorMessage_2);
                  _jspx_th_ctl_errorMessage_4.setErrorSupplier(files);
                  _jspx_th_ctl_errorMessage_4.setErrorFilter( FileIterator.HOLDINGS_BUT_NO_DIRECTORY);
                  int _jspx_eval_ctl_errorMessage_4 = _jspx_th_ctl_errorMessage_4.doStartTag();
                  if (_jspx_eval_ctl_errorMessage_4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_errorMessage_4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_errorMessage_4.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_errorMessage_4.doInitBody();
                    }
                    do {
                      out.write("\n                <div class=\"errorMessage\">\n                  Data directory missing.\n                </div>\n              ");
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
                  out.write("\n              ");
                  //  ctl:errorMessage
                  org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_5 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
                  _jspx_th_ctl_errorMessage_5.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_errorMessage_5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_errorMessage_2);
                  _jspx_th_ctl_errorMessage_5.setErrorSupplier(files);
                  _jspx_th_ctl_errorMessage_5.setErrorFilter(
                  FileIterator.NO_DIRECTORY_NO_HOLDINGS);
                  int _jspx_eval_ctl_errorMessage_5 = _jspx_th_ctl_errorMessage_5.doStartTag();
                  if (_jspx_eval_ctl_errorMessage_5 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_errorMessage_5 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_errorMessage_5.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_errorMessage_5.doInitBody();
                    }
                    do {
                      out.write("\n                Data directory missing and no holding record.\n                <div> You should create a\n                  directory in the repository to store this sample's\n                  data files. Please specify the directory to be created\n                  below. You may leave the text box blank if you wish to\n                  have the data directory created in the default\n                  location.\n                </div>\n                <div style=\"margin-top: 1em;\">\n                  ");
                      //  rn:dataDirectoryField
                      org.recipnet.site.content.rncontrols.DataDirectoryField _jspx_th_rn_dataDirectoryField_0 = (org.recipnet.site.content.rncontrols.DataDirectoryField) _jspx_tagPool_rn_dataDirectoryField_fileIterator_directoryPart_nobody.get(org.recipnet.site.content.rncontrols.DataDirectoryField.class);
                      _jspx_th_rn_dataDirectoryField_0.setPageContext(_jspx_page_context);
                      _jspx_th_rn_dataDirectoryField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_errorMessage_5);
                      _jspx_th_rn_dataDirectoryField_0.setDirectoryPart(
                      DataDirectoryField.FieldCode.FIRST_PART);
                      _jspx_th_rn_dataDirectoryField_0.setFileIterator(files);
                      int _jspx_eval_rn_dataDirectoryField_0 = _jspx_th_rn_dataDirectoryField_0.doStartTag();
                      if (_jspx_th_rn_dataDirectoryField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_rn_dataDirectoryField_fileIterator_directoryPart_nobody.reuse(_jspx_th_rn_dataDirectoryField_0);
                      out.write("/ <br />\n                    &nbsp;&nbsp;\n                    ");
                      //  rn:dataDirectoryField
                      org.recipnet.site.content.rncontrols.DataDirectoryField extpath = null;
                      org.recipnet.site.content.rncontrols.DataDirectoryField _jspx_th_rn_dataDirectoryField_1 = (org.recipnet.site.content.rncontrols.DataDirectoryField) _jspx_tagPool_rn_dataDirectoryField_id_fileIterator_directoryPart_nobody.get(org.recipnet.site.content.rncontrols.DataDirectoryField.class);
                      _jspx_th_rn_dataDirectoryField_1.setPageContext(_jspx_page_context);
                      _jspx_th_rn_dataDirectoryField_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_errorMessage_5);
                      _jspx_th_rn_dataDirectoryField_1.setId("extpath");
                      _jspx_th_rn_dataDirectoryField_1.setDirectoryPart(
                        DataDirectoryField.FieldCode.EXTENSION_PATH);
                      _jspx_th_rn_dataDirectoryField_1.setFileIterator(files);
                      int _jspx_eval_rn_dataDirectoryField_1 = _jspx_th_rn_dataDirectoryField_1.doStartTag();
                      if (_jspx_th_rn_dataDirectoryField_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      extpath = (org.recipnet.site.content.rncontrols.DataDirectoryField) _jspx_page_context.findAttribute("extpath");
                      _jspx_tagPool_rn_dataDirectoryField_id_fileIterator_directoryPart_nobody.reuse(_jspx_th_rn_dataDirectoryField_1);
                      out.write("/\n                      ");
                      //  rn:dataDirectoryField
                      org.recipnet.site.content.rncontrols.DataDirectoryField _jspx_th_rn_dataDirectoryField_2 = (org.recipnet.site.content.rncontrols.DataDirectoryField) _jspx_tagPool_rn_dataDirectoryField_fileIterator_directoryPart_nobody.get(org.recipnet.site.content.rncontrols.DataDirectoryField.class);
                      _jspx_th_rn_dataDirectoryField_2.setPageContext(_jspx_page_context);
                      _jspx_th_rn_dataDirectoryField_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_errorMessage_5);
                      _jspx_th_rn_dataDirectoryField_2.setDirectoryPart(
                          DataDirectoryField.FieldCode.LAST_PART);
                      _jspx_th_rn_dataDirectoryField_2.setFileIterator(files);
                      int _jspx_eval_rn_dataDirectoryField_2 = _jspx_th_rn_dataDirectoryField_2.doStartTag();
                      if (_jspx_th_rn_dataDirectoryField_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_rn_dataDirectoryField_fileIterator_directoryPart_nobody.reuse(_jspx_th_rn_dataDirectoryField_2);
                      out.write(" <br />\n                        <center>");
                      if (_jspx_meth_rn_createDataDirectoryButton_0(_jspx_th_ctl_errorMessage_5, _jspx_page_context))
                        return;
                      out.write("\n                        </center>\n                </div>\n                ");
                      //  ctl:errorMessage
                      org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_6 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorSupplier.get(org.recipnet.common.controls.ErrorChecker.class);
                      _jspx_th_ctl_errorMessage_6.setPageContext(_jspx_page_context);
                      _jspx_th_ctl_errorMessage_6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_errorMessage_5);
                      _jspx_th_ctl_errorMessage_6.setErrorSupplier(extpath);
                      int _jspx_eval_ctl_errorMessage_6 = _jspx_th_ctl_errorMessage_6.doStartTag();
                      if (_jspx_eval_ctl_errorMessage_6 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_ctl_errorMessage_6 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_ctl_errorMessage_6.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_ctl_errorMessage_6.doInitBody();
                        }
                        do {
                          out.write("\n                  <div class=\"errorMessage\">\n                    <a name=\"error\" />The extention path is invalid either\n                    because it contains an invalid character or because it is\n                    set to a reserved word. Please select a different extension\n                    path and press \"Create\" again.\n                  </div>\n                ");
                          int evalDoAfterBody = _jspx_th_ctl_errorMessage_6.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_ctl_errorMessage_6 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                          out = _jspx_page_context.popBody();
                      }
                      if (_jspx_th_ctl_errorMessage_6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_ctl_errorMessage_errorSupplier.reuse(_jspx_th_ctl_errorMessage_6);
                      out.write("\n              ");
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
              org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_7 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_invertFilter_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
              _jspx_th_ctl_errorMessage_7.setPageContext(_jspx_page_context);
              _jspx_th_ctl_errorMessage_7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_ctl_errorMessage_7.setErrorSupplier(files);
              _jspx_th_ctl_errorMessage_7.setInvertFilter(true);
              _jspx_th_ctl_errorMessage_7.setErrorFilter(
                                  FileIterator.DIRECTORY_BUT_NO_HOLDINGS
                                  | FileIterator.NO_DIRECTORY_NO_HOLDINGS
                             | FileIterator.HOLDINGS_BUT_NO_DIRECTORY);
              int _jspx_eval_ctl_errorMessage_7 = _jspx_th_ctl_errorMessage_7.doStartTag();
              if (_jspx_eval_ctl_errorMessage_7 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_ctl_errorMessage_7 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_ctl_errorMessage_7.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_ctl_errorMessage_7.doInitBody();
                }
                do {
                  out.write("\n              <div style=\"text-align: right; margin: 0.25em 0 0.25em 0;\">\n                ");
                  if (_jspx_meth_rn_uploadFilesButton_0(_jspx_th_ctl_errorMessage_7, _jspx_page_context))
                    return;
                  out.write("\n              </div>\n            ");
                  int evalDoAfterBody = _jspx_th_ctl_errorMessage_7.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_ctl_errorMessage_7 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_ctl_errorMessage_7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_ctl_errorMessage_invertFilter_errorSupplier_errorFilter.reuse(_jspx_th_ctl_errorMessage_7);
              out.write("\n            ");
              //  ctl:errorMessage
              org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_8 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_invertFilter_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
              _jspx_th_ctl_errorMessage_8.setPageContext(_jspx_page_context);
              _jspx_th_ctl_errorMessage_8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_ctl_errorMessage_8.setErrorSupplier(files);
              _jspx_th_ctl_errorMessage_8.setErrorFilter(
                                  FileIterator.DIRECTORY_BUT_NO_HOLDINGS
                                  | FileIterator.NO_DIRECTORY_NO_HOLDINGS
                              | FileIterator.HOLDINGS_BUT_NO_DIRECTORY
                              | FileIterator.NO_FILES_IN_REPOSITORY);
              _jspx_th_ctl_errorMessage_8.setInvertFilter(true);
              int _jspx_eval_ctl_errorMessage_8 = _jspx_th_ctl_errorMessage_8.doStartTag();
              if (_jspx_eval_ctl_errorMessage_8 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_ctl_errorMessage_8 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_ctl_errorMessage_8.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_ctl_errorMessage_8.doInitBody();
                }
                do {
                  out.write("\n              <div style=\"text-align: right; margin: 0.25em 0 0.25em 0;\">\n                ");
                  if (_jspx_meth_rn_buttonLink_0(_jspx_th_ctl_errorMessage_8, _jspx_page_context))
                    return;
                  out.write("\n              </div>\n              <div style=\"text-align: right; margin: 0.25em 0 0.25em 0;\">\n                ");
                  if (_jspx_meth_rn_buttonLink_1(_jspx_th_ctl_errorMessage_8, _jspx_page_context))
                    return;
                  out.write("\n              </div>\n            ");
                  int evalDoAfterBody = _jspx_th_ctl_errorMessage_8.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_ctl_errorMessage_8 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_ctl_errorMessage_8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_ctl_errorMessage_invertFilter_errorSupplier_errorFilter.reuse(_jspx_th_ctl_errorMessage_8);
              out.write("\n          </div>\n        </div>\n        </td>\n        <td class=\"actionColumn\">\n          ");
              //  rn:preferenceChecker
              org.recipnet.site.content.rncontrols.PreferenceChecker _jspx_th_rn_preferenceChecker_1 = (org.recipnet.site.content.rncontrols.PreferenceChecker) _jspx_tagPool_rn_preferenceChecker_invert_includeIfBooleanPrefIsTrue.get(org.recipnet.site.content.rncontrols.PreferenceChecker.class);
              _jspx_th_rn_preferenceChecker_1.setPageContext(_jspx_page_context);
              _jspx_th_rn_preferenceChecker_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_preferenceChecker_1.setInvert(true);
              _jspx_th_rn_preferenceChecker_1.setIncludeIfBooleanPrefIsTrue(
                  UserBL.Pref.SUPPRESS_SUPPRESSION);
              int _jspx_eval_rn_preferenceChecker_1 = _jspx_th_rn_preferenceChecker_1.doStartTag();
              if (_jspx_eval_rn_preferenceChecker_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_rn_preferenceChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_rn_preferenceChecker_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_rn_preferenceChecker_1.doInitBody();
                }
                do {
                  out.write("\n          <table class=\"preferenceTable\" cellspacing=\"0\" cellpadding=\"0\"\n              style=\"text-align: left;\">\n            ");
                  if (_jspx_meth_ctl_browserSpecific_0(_jspx_th_rn_preferenceChecker_1, _jspx_page_context))
                    return;
                  out.write("\n            <tr>\n              <th style=\"font-style:normal; color: black; vertical-align: top; padding-left: 0.5em\">Suppress:</th>\n              <td>");
                  //  ctl:intIterator
                  org.recipnet.common.controls.ExplicitIntIterator prefs = null;
                  org.recipnet.common.controls.ExplicitIntIterator _jspx_th_ctl_intIterator_0 = (org.recipnet.common.controls.ExplicitIntIterator) _jspx_tagPool_ctl_intIterator_ints_id.get(org.recipnet.common.controls.ExplicitIntIterator.class);
                  _jspx_th_ctl_intIterator_0.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_intIterator_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_preferenceChecker_1);
                  _jspx_th_ctl_intIterator_0.setId("prefs");
                  _jspx_th_ctl_intIterator_0.setInts( new int[] {
                    PrefType.SUPPRESS_BLANK_FIELD.ordinal(),
                    PrefType.SUPPRESS_COMMENTS.ordinal(),
                    PrefType.SUPPRESS_EMPTY_FILE_ACTIONS.ordinal(),
                    PrefType.SUPPRESS_EMPTY_CORRECTION_ACTIONS.ordinal(),
                    PrefType.SUPPRESS_EMPTY_OTHER_ACTIONS.ordinal(),
                    PrefType.SUPPRESS_SKIPPED_ACTIONS.ordinal(),
                    PrefType.SUPPRESS_BLANK_FILE_LISTINGS.ordinal(),
                    PrefType.SUPPRESS_SUPPRESSION.ordinal()
                  } );
                  int _jspx_eval_ctl_intIterator_0 = _jspx_th_ctl_intIterator_0.doStartTag();
                  if (_jspx_eval_ctl_intIterator_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_intIterator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_intIterator_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_intIterator_0.doInitBody();
                    }
                    prefs = (org.recipnet.common.controls.ExplicitIntIterator) _jspx_page_context.findAttribute("prefs");
                    do {
                      out.write("\n                  <span class=\"preferenceItem\">");
                      //  rn:preferenceField
                      org.recipnet.site.content.rncontrols.PreferenceField prefField = null;
                      org.recipnet.site.content.rncontrols.PreferenceField _jspx_th_rn_preferenceField_0 = (org.recipnet.site.content.rncontrols.PreferenceField) _jspx_tagPool_rn_preferenceField_preferenceType_id_nobody.get(org.recipnet.site.content.rncontrols.PreferenceField.class);
                      _jspx_th_rn_preferenceField_0.setPageContext(_jspx_page_context);
                      _jspx_th_rn_preferenceField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_intIterator_0);
                      _jspx_th_rn_preferenceField_0.setId("prefField");
                      _jspx_th_rn_preferenceField_0.setPreferenceType(
                      PrefType.values()[prefs.getCurrentInt()] );
                      int _jspx_eval_rn_preferenceField_0 = _jspx_th_rn_preferenceField_0.doStartTag();
                      if (_jspx_th_rn_preferenceField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      prefField = (org.recipnet.site.content.rncontrols.PreferenceField) _jspx_page_context.findAttribute("prefField");
                      _jspx_tagPool_rn_preferenceField_preferenceType_id_nobody.reuse(_jspx_th_rn_preferenceField_0);
                      out.write("\n                    ");
                      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${ prefField.preferenceType.defaultLabel }", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
                      out.write("</span>\n                ");
                      int evalDoAfterBody = _jspx_th_ctl_intIterator_0.doAfterBody();
                      prefs = (org.recipnet.common.controls.ExplicitIntIterator) _jspx_page_context.findAttribute("prefs");
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_ctl_intIterator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_ctl_intIterator_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  prefs = (org.recipnet.common.controls.ExplicitIntIterator) _jspx_page_context.findAttribute("prefs");
                  _jspx_tagPool_ctl_intIterator_ints_id.reuse(_jspx_th_ctl_intIterator_0);
                  out.write("\n              </td>\n              <td>");
                  if (_jspx_meth_rn_storePreferencesButton_0(_jspx_th_rn_preferenceChecker_1, _jspx_page_context))
                    return;
                  out.write("</td>\n            </tr>\n            ");
                  if (_jspx_meth_ctl_browserSpecific_1(_jspx_th_rn_preferenceChecker_1, _jspx_page_context))
                    return;
                  out.write("\n          </table>\n        ");
                  int evalDoAfterBody = _jspx_th_rn_preferenceChecker_1.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_rn_preferenceChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_rn_preferenceChecker_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_preferenceChecker_invert_includeIfBooleanPrefIsTrue.reuse(_jspx_th_rn_preferenceChecker_1);
              out.write("\n        ");
              if (_jspx_meth_ctl_phaseEvent_0(_jspx_th_ctl_selfForm_0, _jspx_page_context))
                return;
              out.write("\n        ");
              //  rn:sampleActionIterator
              org.recipnet.site.content.rncontrols.SampleActionIterator actionIt = null;
              org.recipnet.site.content.rncontrols.SampleActionIterator _jspx_th_rn_sampleActionIterator_0 = (org.recipnet.site.content.rncontrols.SampleActionIterator) _jspx_tagPool_rn_sampleActionIterator_id.get(org.recipnet.site.content.rncontrols.SampleActionIterator.class);
              _jspx_th_rn_sampleActionIterator_0.setPageContext(_jspx_page_context);
              _jspx_th_rn_sampleActionIterator_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_sampleActionIterator_0.setId("actionIt");
              int _jspx_eval_rn_sampleActionIterator_0 = _jspx_th_rn_sampleActionIterator_0.doStartTag();
              if (_jspx_eval_rn_sampleActionIterator_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_rn_sampleActionIterator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_rn_sampleActionIterator_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_rn_sampleActionIterator_0.doInitBody();
                }
                actionIt = (org.recipnet.site.content.rncontrols.SampleActionIterator) _jspx_page_context.findAttribute("actionIt");
                do {
                  out.write("\n          ");
                  org.apache.jasper.runtime.JspRuntimeLibrary.introspecthelper(_jspx_page_context.findAttribute("closeBody"), "string", "", null, null, false);
                  out.write("\n          <div class=\"actionBox");
                  //  rn:sampleHistoryField
                  org.recipnet.site.content.rncontrols.SampleHistoryField _jspx_th_rn_sampleHistoryField_0 = (org.recipnet.site.content.rncontrols.SampleHistoryField) _jspx_tagPool_rn_sampleHistoryField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleHistoryField.class);
                  _jspx_th_rn_sampleHistoryField_0.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleHistoryField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleActionIterator_0);
                  _jspx_th_rn_sampleHistoryField_0.setFieldCode(
                      SampleHistoryField.FieldCode.ACTION_CODE);
                  int _jspx_eval_rn_sampleHistoryField_0 = _jspx_th_rn_sampleHistoryField_0.doStartTag();
                  if (_jspx_th_rn_sampleHistoryField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_sampleHistoryField_fieldCode_nobody.reuse(_jspx_th_rn_sampleHistoryField_0);
                  out.write("\">\n            <div class=\"actionBox\">\n              ");
                  //  ctl:errorChecker
                  org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorChecker_0 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorChecker_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
                  _jspx_th_ctl_errorChecker_0.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_errorChecker_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleActionIterator_0);
                  _jspx_th_ctl_errorChecker_0.setErrorFilter(
                    SampleActionIterator.ACTION_WAS_CORRECTED_ONCE
                    | SampleActionIterator.ACTION_WAS_CORRECTED_MORE_THAN_ONCE
              );
                  int _jspx_eval_ctl_errorChecker_0 = _jspx_th_ctl_errorChecker_0.doStartTag();
                  if (_jspx_eval_ctl_errorChecker_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_errorChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_errorChecker_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_errorChecker_0.doInitBody();
                    }
                    do {
                      out.write("\n              <div class=\"twoLineActionBoxTitle\">\n              ");
                      int evalDoAfterBody = _jspx_th_ctl_errorChecker_0.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_ctl_errorChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_ctl_errorChecker_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_ctl_errorChecker_errorFilter.reuse(_jspx_th_ctl_errorChecker_0);
                  out.write("\n              ");
                  //  ctl:errorChecker
                  org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorChecker_1 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorChecker_invert_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
                  _jspx_th_ctl_errorChecker_1.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_errorChecker_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleActionIterator_0);
                  _jspx_th_ctl_errorChecker_1.setInvert(true);
                  _jspx_th_ctl_errorChecker_1.setErrorFilter(
                    SampleActionIterator.ACTION_WAS_CORRECTED_ONCE
                    | SampleActionIterator.ACTION_WAS_CORRECTED_MORE_THAN_ONCE
              );
                  int _jspx_eval_ctl_errorChecker_1 = _jspx_th_ctl_errorChecker_1.doStartTag();
                  if (_jspx_eval_ctl_errorChecker_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_errorChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_errorChecker_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_errorChecker_1.doInitBody();
                    }
                    do {
                      out.write("\n              <div class=\"actionBoxTitle\">\n              ");
                      int evalDoAfterBody = _jspx_th_ctl_errorChecker_1.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_ctl_errorChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_ctl_errorChecker_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_ctl_errorChecker_invert_errorFilter.reuse(_jspx_th_ctl_errorChecker_1);
                  out.write("\n                ");
                  if (_jspx_meth_rn_actionIcon_0(_jspx_th_rn_sampleActionIterator_0, _jspx_page_context))
                    return;
                  out.write("\n                <span class=\"bright\">");
                  //  rn:sampleHistoryField
                  org.recipnet.site.content.rncontrols.SampleHistoryField _jspx_th_rn_sampleHistoryField_1 = (org.recipnet.site.content.rncontrols.SampleHistoryField) _jspx_tagPool_rn_sampleHistoryField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleHistoryField.class);
                  _jspx_th_rn_sampleHistoryField_1.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleHistoryField_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleActionIterator_0);
                  _jspx_th_rn_sampleHistoryField_1.setFieldCode(
                    SampleHistoryField.FieldCode.ACTION_PERFORMED);
                  int _jspx_eval_rn_sampleHistoryField_1 = _jspx_th_rn_sampleHistoryField_1.doStartTag();
                  if (_jspx_th_rn_sampleHistoryField_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_sampleHistoryField_fieldCode_nobody.reuse(_jspx_th_rn_sampleHistoryField_1);
                  out.write("</span>\n                ");
                  //  rn:sampleHistoryField
                  org.recipnet.site.content.rncontrols.SampleHistoryField _jspx_th_rn_sampleHistoryField_2 = (org.recipnet.site.content.rncontrols.SampleHistoryField) _jspx_tagPool_rn_sampleHistoryField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleHistoryField.class);
                  _jspx_th_rn_sampleHistoryField_2.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleHistoryField_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleActionIterator_0);
                  _jspx_th_rn_sampleHistoryField_2.setFieldCode(
                    SampleHistoryField.FieldCode.ACTION_DATE);
                  int _jspx_eval_rn_sampleHistoryField_2 = _jspx_th_rn_sampleHistoryField_2.doStartTag();
                  if (_jspx_th_rn_sampleHistoryField_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_sampleHistoryField_fieldCode_nobody.reuse(_jspx_th_rn_sampleHistoryField_2);
                  out.write(" by\n                <span class=\"bright\">");
                  //  rn:sampleHistoryField
                  org.recipnet.site.content.rncontrols.SampleHistoryField _jspx_th_rn_sampleHistoryField_3 = (org.recipnet.site.content.rncontrols.SampleHistoryField) _jspx_tagPool_rn_sampleHistoryField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleHistoryField.class);
                  _jspx_th_rn_sampleHistoryField_3.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleHistoryField_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleActionIterator_0);
                  _jspx_th_rn_sampleHistoryField_3.setFieldCode( SampleHistoryField.FieldCode.USER_FULLNAME_THAT_PERFORMED_ACTION);
                  int _jspx_eval_rn_sampleHistoryField_3 = _jspx_th_rn_sampleHistoryField_3.doStartTag();
                  if (_jspx_th_rn_sampleHistoryField_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_sampleHistoryField_fieldCode_nobody.reuse(_jspx_th_rn_sampleHistoryField_3);
                  out.write("\n                </span>\n                ");
                  //  ctl:errorMessage
                  org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_9 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
                  _jspx_th_ctl_errorMessage_9.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_errorMessage_9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleActionIterator_0);
                  _jspx_th_ctl_errorMessage_9.setErrorFilter(
                    SampleActionIterator.ACTION_WAS_CORRECTED_ONCE);
                  int _jspx_eval_ctl_errorMessage_9 = _jspx_th_ctl_errorMessage_9.doStartTag();
                  if (_jspx_eval_ctl_errorMessage_9 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_errorMessage_9 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_errorMessage_9.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_errorMessage_9.doInitBody();
                    }
                    do {
                      out.write("<br/>\n                  1 revision made subsequently\n                ");
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
                  out.write("\n                ");
                  //  ctl:errorMessage
                  org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_10 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
                  _jspx_th_ctl_errorMessage_10.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_errorMessage_10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleActionIterator_0);
                  _jspx_th_ctl_errorMessage_10.setErrorFilter(
                    SampleActionIterator.ACTION_WAS_CORRECTED_MORE_THAN_ONCE);
                  int _jspx_eval_ctl_errorMessage_10 = _jspx_th_ctl_errorMessage_10.doStartTag();
                  if (_jspx_eval_ctl_errorMessage_10 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_errorMessage_10 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_errorMessage_10.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_errorMessage_10.doInitBody();
                    }
                    do {
                      out.write("<br/>\n                  ");
                      if (_jspx_meth_rn_correctionCount_0(_jspx_th_ctl_errorMessage_10, _jspx_page_context))
                        return;
                      out.write(" revisions made subsequently\n                ");
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
                  out.write("\n              </div>\n              ");
                  //  rn:sampleActionFieldIterator
                  org.recipnet.site.content.rncontrols.SampleActionFieldIterator fieldIt = null;
                  org.recipnet.site.content.rncontrols.SampleActionFieldIterator _jspx_th_rn_sampleActionFieldIterator_0 = (org.recipnet.site.content.rncontrols.SampleActionFieldIterator) _jspx_tagPool_rn_sampleActionFieldIterator_id.get(org.recipnet.site.content.rncontrols.SampleActionFieldIterator.class);
                  _jspx_th_rn_sampleActionFieldIterator_0.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleActionFieldIterator_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleActionIterator_0);
                  _jspx_th_rn_sampleActionFieldIterator_0.setId("fieldIt");
                  int _jspx_eval_rn_sampleActionFieldIterator_0 = _jspx_th_rn_sampleActionFieldIterator_0.doStartTag();
                  if (_jspx_eval_rn_sampleActionFieldIterator_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_rn_sampleActionFieldIterator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_rn_sampleActionFieldIterator_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_rn_sampleActionFieldIterator_0.doInitBody();
                    }
                    fieldIt = (org.recipnet.site.content.rncontrols.SampleActionFieldIterator) _jspx_page_context.findAttribute("fieldIt");
                    do {
                      out.write("\n                ");
                      if (_jspx_meth_ctl_parityChecker_2(_jspx_th_rn_sampleActionFieldIterator_0, _jspx_page_context))
                        return;
                      out.write("\n                  <tr>\n                    <th style=\"width: 35%\">");
                      if (_jspx_meth_rn_sampleFieldLabel_1(_jspx_th_rn_sampleActionFieldIterator_0, _jspx_page_context))
                        return;
                      out.write(":</th>\n                    <td style=\"width: 65%\">\n                      ");
                      //  rn:sampleChecker
                      org.recipnet.site.content.rncontrols.SampleChecker _jspx_th_rn_sampleChecker_0 = (org.recipnet.site.content.rncontrols.SampleChecker) _jspx_tagPool_rn_sampleChecker_includeIfSampleFieldContextProvidesValue.get(org.recipnet.site.content.rncontrols.SampleChecker.class);
                      _jspx_th_rn_sampleChecker_0.setPageContext(_jspx_page_context);
                      _jspx_th_rn_sampleChecker_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleActionFieldIterator_0);
                      _jspx_th_rn_sampleChecker_0.setIncludeIfSampleFieldContextProvidesValue(true);
                      int _jspx_eval_rn_sampleChecker_0 = _jspx_th_rn_sampleChecker_0.doStartTag();
                      if (_jspx_eval_rn_sampleChecker_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_rn_sampleChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_rn_sampleChecker_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_rn_sampleChecker_0.doInitBody();
                        }
                        do {
                          out.write("\n                        ");
                          if (_jspx_meth_rn_sampleField_2(_jspx_th_rn_sampleChecker_0, _jspx_page_context))
                            return;
                          out.write("\n                        ");
                          if (_jspx_meth_rn_sampleFieldUnits_0(_jspx_th_rn_sampleChecker_0, _jspx_page_context))
                            return;
                          out.write("\n                        <span class=\"light\">");
                          //  rn:sampleTextChecker
                          org.recipnet.site.content.rncontrols.SampleTextChecker _jspx_th_rn_sampleTextChecker_0 = (org.recipnet.site.content.rncontrols.SampleTextChecker) _jspx_tagPool_rn_sampleTextChecker_includeOnlyForAnnotationsWithReferences.get(org.recipnet.site.content.rncontrols.SampleTextChecker.class);
                          _jspx_th_rn_sampleTextChecker_0.setPageContext(_jspx_page_context);
                          _jspx_th_rn_sampleTextChecker_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleChecker_0);
                          _jspx_th_rn_sampleTextChecker_0.setIncludeOnlyForAnnotationsWithReferences(true);
                          int _jspx_eval_rn_sampleTextChecker_0 = _jspx_th_rn_sampleTextChecker_0.doStartTag();
                          if (_jspx_eval_rn_sampleTextChecker_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            if (_jspx_eval_rn_sampleTextChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_rn_sampleTextChecker_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_rn_sampleTextChecker_0.doInitBody();
                            }
                            do {
                              out.write("\n                          ");
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
                              out.write("\n                            (jump to\n                            ");
                              //  rn:showsampleLink
                              org.recipnet.site.content.rncontrols.ShowsampleLink _jspx_th_rn_showsampleLink_1 = (org.recipnet.site.content.rncontrols.ShowsampleLink) _jspx_tagPool_rn_showsampleLink_jumpSitePageUrl_detailedPageUrl_basicPageUrl.get(org.recipnet.site.content.rncontrols.ShowsampleLink.class);
                              _jspx_th_rn_showsampleLink_1.setPageContext(_jspx_page_context);
                              _jspx_th_rn_showsampleLink_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleContextTranslator_0);
                              _jspx_th_rn_showsampleLink_1.setDetailedPageUrl("/showsampledetailed.jsp");
                              _jspx_th_rn_showsampleLink_1.setBasicPageUrl("/showsamplebasic.jsp");
                              _jspx_th_rn_showsampleLink_1.setJumpSitePageUrl("/showsamplejumpsite.jsp");
                              int _jspx_eval_rn_showsampleLink_1 = _jspx_th_rn_showsampleLink_1.doStartTag();
                              if (_jspx_eval_rn_showsampleLink_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              if (_jspx_eval_rn_showsampleLink_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_rn_showsampleLink_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_rn_showsampleLink_1.doInitBody();
                              }
                              do {
                              out.write("\n                              ");
                              if (_jspx_meth_rn_sampleParam_1(_jspx_th_rn_showsampleLink_1, _jspx_page_context))
                              return;
                              out.write("\n                              ");
                              //  rn:labField
                              org.recipnet.site.content.rncontrols.LabField _jspx_th_rn_labField_1 = (org.recipnet.site.content.rncontrols.LabField) _jspx_tagPool_rn_labField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.LabField.class);
                              _jspx_th_rn_labField_1.setPageContext(_jspx_page_context);
                              _jspx_th_rn_labField_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_showsampleLink_1);
                              _jspx_th_rn_labField_1.setFieldCode(
                                  LabField.SHORT_NAME);
                              int _jspx_eval_rn_labField_1 = _jspx_th_rn_labField_1.doStartTag();
                              if (_jspx_th_rn_labField_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                              return;
                              _jspx_tagPool_rn_labField_fieldCode_nobody.reuse(_jspx_th_rn_labField_1);
                              out.write(" sample\n                              ");
                              //  rn:sampleField
                              org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_3 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_fieldCode_displayValueOnly_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
                              _jspx_th_rn_sampleField_3.setPageContext(_jspx_page_context);
                              _jspx_th_rn_sampleField_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_showsampleLink_1);
                              _jspx_th_rn_sampleField_3.setDisplayValueOnly(true);
                              _jspx_th_rn_sampleField_3.setFieldCode(SampleInfo.LOCAL_LAB_ID);
                              int _jspx_eval_rn_sampleField_3 = _jspx_th_rn_sampleField_3.doStartTag();
                              if (_jspx_th_rn_sampleField_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                              return;
                              _jspx_tagPool_rn_sampleField_fieldCode_displayValueOnly_nobody.reuse(_jspx_th_rn_sampleField_3);
                              out.write("\n                            ");
                              int evalDoAfterBody = _jspx_th_rn_showsampleLink_1.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              if (_jspx_eval_rn_showsampleLink_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                              out = _jspx_page_context.popBody();
                              }
                              if (_jspx_th_rn_showsampleLink_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                              return;
                              _jspx_tagPool_rn_showsampleLink_jumpSitePageUrl_detailedPageUrl_basicPageUrl.reuse(_jspx_th_rn_showsampleLink_1);
                              out.write(")\n                          ");
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
                              out.write("\n                        ");
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
                          out.write("</span>\n                      ");
                          int evalDoAfterBody = _jspx_th_rn_sampleChecker_0.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_rn_sampleChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                          out = _jspx_page_context.popBody();
                      }
                      if (_jspx_th_rn_sampleChecker_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_rn_sampleChecker_includeIfSampleFieldContextProvidesValue.reuse(_jspx_th_rn_sampleChecker_0);
                      out.write("\n                      ");
                      if (_jspx_meth_rn_sampleChecker_1(_jspx_th_rn_sampleActionFieldIterator_0, _jspx_page_context))
                        return;
                      out.write("\n                    </td>\n                  </tr>\n                  ");
                      //  ctl:parityChecker
                      org.recipnet.common.controls.ParityChecker _jspx_th_ctl_parityChecker_3 = (org.recipnet.common.controls.ParityChecker) _jspx_tagPool_ctl_parityChecker_includeOnlyOnLast.get(org.recipnet.common.controls.ParityChecker.class);
                      _jspx_th_ctl_parityChecker_3.setPageContext(_jspx_page_context);
                      _jspx_th_ctl_parityChecker_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleActionFieldIterator_0);
                      _jspx_th_ctl_parityChecker_3.setIncludeOnlyOnLast(true);
                      int _jspx_eval_ctl_parityChecker_3 = _jspx_th_ctl_parityChecker_3.doStartTag();
                      if (_jspx_eval_ctl_parityChecker_3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_ctl_parityChecker_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_ctl_parityChecker_3.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_ctl_parityChecker_3.doInitBody();
                        }
                        do {
                          out.write("\n                    ");
                          //  ctl:errorChecker
                          org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorChecker_2 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorChecker_invert_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
                          _jspx_th_ctl_errorChecker_2.setPageContext(_jspx_page_context);
                          _jspx_th_ctl_errorChecker_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_parityChecker_3);
                          _jspx_th_ctl_errorChecker_2.setErrorSupplier((org.recipnet.common.controls.ErrorSupplier) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${fieldIt}", org.recipnet.common.controls.ErrorSupplier.class, (PageContext)_jspx_page_context, null, false));
                          _jspx_th_ctl_errorChecker_2.setInvert(true);
                          _jspx_th_ctl_errorChecker_2.setErrorFilter(
                      SampleActionFieldIterator.SOME_FIELDS_WERE_SUPPRESSED);
                          int _jspx_eval_ctl_errorChecker_2 = _jspx_th_ctl_errorChecker_2.doStartTag();
                          if (_jspx_eval_ctl_errorChecker_2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            if (_jspx_eval_ctl_errorChecker_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_ctl_errorChecker_2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_ctl_errorChecker_2.doInitBody();
                            }
                            do {
                              out.write("\n                      </table>\n                      </td>\n                    ");
                              int evalDoAfterBody = _jspx_th_ctl_errorChecker_2.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                            } while (true);
                            if (_jspx_eval_ctl_errorChecker_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                              out = _jspx_page_context.popBody();
                          }
                          if (_jspx_th_ctl_errorChecker_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          _jspx_tagPool_ctl_errorChecker_invert_errorSupplier_errorFilter.reuse(_jspx_th_ctl_errorChecker_2);
                          out.write("\n                  ");
                          int evalDoAfterBody = _jspx_th_ctl_parityChecker_3.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_ctl_parityChecker_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                          out = _jspx_page_context.popBody();
                      }
                      if (_jspx_th_ctl_parityChecker_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_ctl_parityChecker_includeOnlyOnLast.reuse(_jspx_th_ctl_parityChecker_3);
                      out.write("\n                ");
                      int evalDoAfterBody = _jspx_th_rn_sampleActionFieldIterator_0.doAfterBody();
                      fieldIt = (org.recipnet.site.content.rncontrols.SampleActionFieldIterator) _jspx_page_context.findAttribute("fieldIt");
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_rn_sampleActionFieldIterator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_rn_sampleActionFieldIterator_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  fieldIt = (org.recipnet.site.content.rncontrols.SampleActionFieldIterator) _jspx_page_context.findAttribute("fieldIt");
                  _jspx_tagPool_rn_sampleActionFieldIterator_id.reuse(_jspx_th_rn_sampleActionFieldIterator_0);
                  out.write("\n                ");
                  //  ctl:errorMessage
                  org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_11 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
                  _jspx_th_ctl_errorMessage_11.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_errorMessage_11.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleActionIterator_0);
                  _jspx_th_ctl_errorMessage_11.setErrorSupplier((org.recipnet.common.controls.ErrorSupplier) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${fieldIt}", org.recipnet.common.controls.ErrorSupplier.class, (PageContext)_jspx_page_context, null, false));
                  _jspx_th_ctl_errorMessage_11.setErrorFilter(
                    SampleActionFieldIterator.SOME_FIELDS_WERE_SUPPRESSED);
                  int _jspx_eval_ctl_errorMessage_11 = _jspx_th_ctl_errorMessage_11.doStartTag();
                  if (_jspx_eval_ctl_errorMessage_11 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_errorMessage_11 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_errorMessage_11.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_errorMessage_11.doInitBody();
                    }
                    do {
                      out.write("\n                  ");
                      //  ctl:errorChecker
                      org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorChecker_3 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorChecker_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
                      _jspx_th_ctl_errorChecker_3.setPageContext(_jspx_page_context);
                      _jspx_th_ctl_errorChecker_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_errorMessage_11);
                      _jspx_th_ctl_errorChecker_3.setErrorSupplier((org.recipnet.common.controls.ErrorSupplier) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${fieldIt}", org.recipnet.common.controls.ErrorSupplier.class, (PageContext)_jspx_page_context, null, false));
                      _jspx_th_ctl_errorChecker_3.setErrorFilter(
                    HtmlPageIterator.NO_ITERATIONS);
                      int _jspx_eval_ctl_errorChecker_3 = _jspx_th_ctl_errorChecker_3.doStartTag();
                      if (_jspx_eval_ctl_errorChecker_3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_ctl_errorChecker_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_ctl_errorChecker_3.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_ctl_errorChecker_3.doInitBody();
                        }
                        do {
                          out.write("\n                    <div class=\"actionBoxBody\">\n                      <table>\n                      <tr>\n                      <td style=\"width: 100%;\">\n                      <table>\n                    ");
                          if (_jspx_meth_ctl_phaseEvent_2(_jspx_th_ctl_errorChecker_3, _jspx_page_context))
                            return;
                          out.write("\n                  ");
                          int evalDoAfterBody = _jspx_th_ctl_errorChecker_3.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_ctl_errorChecker_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                          out = _jspx_page_context.popBody();
                      }
                      if (_jspx_th_ctl_errorChecker_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_ctl_errorChecker_errorSupplier_errorFilter.reuse(_jspx_th_ctl_errorChecker_3);
                      out.write("\n                  <tr>\n                    <td colspan=\"2\" class=\"suppressionCount\">\n                      (");
                      if (_jspx_meth_rn_suppressedFieldCount_0(_jspx_th_ctl_errorMessage_11, _jspx_page_context))
                        return;
                      out.write("\n                      blank fields suppressed)\n                    </td>\n                  </tr>\n                </table>\n                </td>\n                ");
                      int evalDoAfterBody = _jspx_th_ctl_errorMessage_11.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_ctl_errorMessage_11 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_ctl_errorMessage_11.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.reuse(_jspx_th_ctl_errorMessage_11);
                  out.write("\n                ");
                  if (_jspx_meth_ctl_if_0(_jspx_th_rn_sampleActionIterator_0, _jspx_page_context))
                    return;
                  out.write("\n              ");
                  //  rn:sampleActionFileIterator
                  org.recipnet.site.content.rncontrols.SampleActionFileIterator addedFiles = null;
                  org.recipnet.site.content.rncontrols.SampleActionFileIterator _jspx_th_rn_sampleActionFileIterator_0 = (org.recipnet.site.content.rncontrols.SampleActionFileIterator) _jspx_tagPool_rn_sampleActionFileIterator_mode_id.get(org.recipnet.site.content.rncontrols.SampleActionFileIterator.class);
                  _jspx_th_rn_sampleActionFileIterator_0.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleActionFileIterator_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleActionIterator_0);
                  _jspx_th_rn_sampleActionFileIterator_0.setId("addedFiles");
                  _jspx_th_rn_sampleActionFileIterator_0.setMode( SampleActionFileIterator.Mode.ADDED_FILES);
                  int _jspx_eval_rn_sampleActionFileIterator_0 = _jspx_th_rn_sampleActionFileIterator_0.doStartTag();
                  if (_jspx_eval_rn_sampleActionFileIterator_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_rn_sampleActionFileIterator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_rn_sampleActionFileIterator_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_rn_sampleActionFileIterator_0.doInitBody();
                    }
                    addedFiles = (org.recipnet.site.content.rncontrols.SampleActionFileIterator) _jspx_page_context.findAttribute("addedFiles");
                    do {
                      if (_jspx_meth_ctl_parityChecker_4(_jspx_th_rn_sampleActionFileIterator_0, _jspx_page_context))
                        return;
                      if (_jspx_meth_ctl_parityChecker_5(_jspx_th_rn_sampleActionFileIterator_0, _jspx_page_context))
                        return;
                      out.write("\n                  <span class=\"fileName\">");
                      if (_jspx_meth_rn_fileField_3(_jspx_th_rn_sampleActionFileIterator_0, _jspx_page_context))
                        return;
                      out.write("</span>");
                      if (_jspx_meth_ctl_parityChecker_7(_jspx_th_rn_sampleActionFileIterator_0, _jspx_page_context))
                        return;
                      int evalDoAfterBody = _jspx_th_rn_sampleActionFileIterator_0.doAfterBody();
                      addedFiles = (org.recipnet.site.content.rncontrols.SampleActionFileIterator) _jspx_page_context.findAttribute("addedFiles");
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_rn_sampleActionFileIterator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_rn_sampleActionFileIterator_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  addedFiles = (org.recipnet.site.content.rncontrols.SampleActionFileIterator) _jspx_page_context.findAttribute("addedFiles");
                  _jspx_tagPool_rn_sampleActionFileIterator_mode_id.reuse(_jspx_th_rn_sampleActionFileIterator_0);
                  out.write("\n              ");
                  //  ctl:errorMessage
                  org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_12 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
                  _jspx_th_ctl_errorMessage_12.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_errorMessage_12.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleActionIterator_0);
                  _jspx_th_ctl_errorMessage_12.setErrorSupplier((org.recipnet.common.controls.ErrorSupplier) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${addedFiles}", org.recipnet.common.controls.ErrorSupplier.class, (PageContext)_jspx_page_context, null, false));
                  _jspx_th_ctl_errorMessage_12.setErrorFilter(HtmlPageIterator.NO_ITERATIONS);
                  int _jspx_eval_ctl_errorMessage_12 = _jspx_th_ctl_errorMessage_12.doStartTag();
                  if (_jspx_eval_ctl_errorMessage_12 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_errorMessage_12 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_errorMessage_12.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_errorMessage_12.doInitBody();
                    }
                    do {
                      out.write("\n                ");
                      //  rn:preferenceChecker
                      org.recipnet.site.content.rncontrols.PreferenceChecker _jspx_th_rn_preferenceChecker_2 = (org.recipnet.site.content.rncontrols.PreferenceChecker) _jspx_tagPool_rn_preferenceChecker_invert_includeIfBooleanPrefIsTrue.get(org.recipnet.site.content.rncontrols.PreferenceChecker.class);
                      _jspx_th_rn_preferenceChecker_2.setPageContext(_jspx_page_context);
                      _jspx_th_rn_preferenceChecker_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_errorMessage_12);
                      _jspx_th_rn_preferenceChecker_2.setIncludeIfBooleanPrefIsTrue(
                        UserBL.Pref.SUPPRESS_BLANK_FILE_LISTINGS);
                      _jspx_th_rn_preferenceChecker_2.setInvert(true);
                      int _jspx_eval_rn_preferenceChecker_2 = _jspx_th_rn_preferenceChecker_2.doStartTag();
                      if (_jspx_eval_rn_preferenceChecker_2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_rn_preferenceChecker_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_rn_preferenceChecker_2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_rn_preferenceChecker_2.doInitBody();
                        }
                        do {
                          out.write("\n                  <div>\n                    Files added:\n                    <span class=\"fileName\">(none)</span>\n                  </div>\n                ");
                          int evalDoAfterBody = _jspx_th_rn_preferenceChecker_2.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_rn_preferenceChecker_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                          out = _jspx_page_context.popBody();
                      }
                      if (_jspx_th_rn_preferenceChecker_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_rn_preferenceChecker_invert_includeIfBooleanPrefIsTrue.reuse(_jspx_th_rn_preferenceChecker_2);
                      out.write("\n              ");
                      int evalDoAfterBody = _jspx_th_ctl_errorMessage_12.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_ctl_errorMessage_12 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_ctl_errorMessage_12.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.reuse(_jspx_th_ctl_errorMessage_12);
                  out.write("\n              ");
                  //  rn:sampleActionFileIterator
                  org.recipnet.site.content.rncontrols.SampleActionFileIterator removedFiles = null;
                  org.recipnet.site.content.rncontrols.SampleActionFileIterator _jspx_th_rn_sampleActionFileIterator_1 = (org.recipnet.site.content.rncontrols.SampleActionFileIterator) _jspx_tagPool_rn_sampleActionFileIterator_mode_id.get(org.recipnet.site.content.rncontrols.SampleActionFileIterator.class);
                  _jspx_th_rn_sampleActionFileIterator_1.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleActionFileIterator_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleActionIterator_0);
                  _jspx_th_rn_sampleActionFileIterator_1.setId("removedFiles");
                  _jspx_th_rn_sampleActionFileIterator_1.setMode( SampleActionFileIterator.Mode.REMOVED_FILES);
                  int _jspx_eval_rn_sampleActionFileIterator_1 = _jspx_th_rn_sampleActionFileIterator_1.doStartTag();
                  if (_jspx_eval_rn_sampleActionFileIterator_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_rn_sampleActionFileIterator_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_rn_sampleActionFileIterator_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_rn_sampleActionFileIterator_1.doInitBody();
                    }
                    removedFiles = (org.recipnet.site.content.rncontrols.SampleActionFileIterator) _jspx_page_context.findAttribute("removedFiles");
                    do {
                      if (_jspx_meth_ctl_parityChecker_8(_jspx_th_rn_sampleActionFileIterator_1, _jspx_page_context))
                        return;
                      if (_jspx_meth_ctl_parityChecker_9(_jspx_th_rn_sampleActionFileIterator_1, _jspx_page_context))
                        return;
                      out.write("\n                  <span class=\"fileName\">");
                      if (_jspx_meth_rn_fileField_4(_jspx_th_rn_sampleActionFileIterator_1, _jspx_page_context))
                        return;
                      out.write("</span>");
                      if (_jspx_meth_ctl_parityChecker_11(_jspx_th_rn_sampleActionFileIterator_1, _jspx_page_context))
                        return;
                      int evalDoAfterBody = _jspx_th_rn_sampleActionFileIterator_1.doAfterBody();
                      removedFiles = (org.recipnet.site.content.rncontrols.SampleActionFileIterator) _jspx_page_context.findAttribute("removedFiles");
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_rn_sampleActionFileIterator_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_rn_sampleActionFileIterator_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  removedFiles = (org.recipnet.site.content.rncontrols.SampleActionFileIterator) _jspx_page_context.findAttribute("removedFiles");
                  _jspx_tagPool_rn_sampleActionFileIterator_mode_id.reuse(_jspx_th_rn_sampleActionFileIterator_1);
                  out.write("\n              ");
                  //  ctl:errorMessage
                  org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_13 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
                  _jspx_th_ctl_errorMessage_13.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_errorMessage_13.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleActionIterator_0);
                  _jspx_th_ctl_errorMessage_13.setErrorSupplier((org.recipnet.common.controls.ErrorSupplier) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${removedFiles}", org.recipnet.common.controls.ErrorSupplier.class, (PageContext)_jspx_page_context, null, false));
                  _jspx_th_ctl_errorMessage_13.setErrorFilter(HtmlPageIterator.NO_ITERATIONS);
                  int _jspx_eval_ctl_errorMessage_13 = _jspx_th_ctl_errorMessage_13.doStartTag();
                  if (_jspx_eval_ctl_errorMessage_13 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_errorMessage_13 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_errorMessage_13.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_errorMessage_13.doInitBody();
                    }
                    do {
                      out.write("\n                ");
                      //  rn:preferenceChecker
                      org.recipnet.site.content.rncontrols.PreferenceChecker _jspx_th_rn_preferenceChecker_3 = (org.recipnet.site.content.rncontrols.PreferenceChecker) _jspx_tagPool_rn_preferenceChecker_invert_includeIfBooleanPrefIsTrue.get(org.recipnet.site.content.rncontrols.PreferenceChecker.class);
                      _jspx_th_rn_preferenceChecker_3.setPageContext(_jspx_page_context);
                      _jspx_th_rn_preferenceChecker_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_errorMessage_13);
                      _jspx_th_rn_preferenceChecker_3.setIncludeIfBooleanPrefIsTrue(
                        UserBL.Pref.SUPPRESS_BLANK_FILE_LISTINGS);
                      _jspx_th_rn_preferenceChecker_3.setInvert(true);
                      int _jspx_eval_rn_preferenceChecker_3 = _jspx_th_rn_preferenceChecker_3.doStartTag();
                      if (_jspx_eval_rn_preferenceChecker_3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_rn_preferenceChecker_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_rn_preferenceChecker_3.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_rn_preferenceChecker_3.doInitBody();
                        }
                        do {
                          out.write("\n                  <div>\n                    Files removed:\n                    <span class=\"fileName\">(none)</span>\n                  </div>\n                ");
                          int evalDoAfterBody = _jspx_th_rn_preferenceChecker_3.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_rn_preferenceChecker_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                          out = _jspx_page_context.popBody();
                      }
                      if (_jspx_th_rn_preferenceChecker_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_rn_preferenceChecker_invert_includeIfBooleanPrefIsTrue.reuse(_jspx_th_rn_preferenceChecker_3);
                      out.write("\n              ");
                      int evalDoAfterBody = _jspx_th_ctl_errorMessage_13.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_ctl_errorMessage_13 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_ctl_errorMessage_13.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.reuse(_jspx_th_ctl_errorMessage_13);
                  out.write("\n              ");
                  //  rn:sampleActionFileIterator
                  org.recipnet.site.content.rncontrols.SampleActionFileIterator modifiedFiles = null;
                  org.recipnet.site.content.rncontrols.SampleActionFileIterator _jspx_th_rn_sampleActionFileIterator_2 = (org.recipnet.site.content.rncontrols.SampleActionFileIterator) _jspx_tagPool_rn_sampleActionFileIterator_mode_id.get(org.recipnet.site.content.rncontrols.SampleActionFileIterator.class);
                  _jspx_th_rn_sampleActionFileIterator_2.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleActionFileIterator_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleActionIterator_0);
                  _jspx_th_rn_sampleActionFileIterator_2.setId("modifiedFiles");
                  _jspx_th_rn_sampleActionFileIterator_2.setMode( SampleActionFileIterator.Mode.MODIFIED_FILES);
                  int _jspx_eval_rn_sampleActionFileIterator_2 = _jspx_th_rn_sampleActionFileIterator_2.doStartTag();
                  if (_jspx_eval_rn_sampleActionFileIterator_2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_rn_sampleActionFileIterator_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_rn_sampleActionFileIterator_2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_rn_sampleActionFileIterator_2.doInitBody();
                    }
                    modifiedFiles = (org.recipnet.site.content.rncontrols.SampleActionFileIterator) _jspx_page_context.findAttribute("modifiedFiles");
                    do {
                      if (_jspx_meth_ctl_parityChecker_12(_jspx_th_rn_sampleActionFileIterator_2, _jspx_page_context))
                        return;
                      if (_jspx_meth_ctl_parityChecker_13(_jspx_th_rn_sampleActionFileIterator_2, _jspx_page_context))
                        return;
                      out.write("\n                  <span class=\"fileName\">");
                      if (_jspx_meth_rn_fileField_5(_jspx_th_rn_sampleActionFileIterator_2, _jspx_page_context))
                        return;
                      out.write("</span>");
                      if (_jspx_meth_ctl_parityChecker_15(_jspx_th_rn_sampleActionFileIterator_2, _jspx_page_context))
                        return;
                      int evalDoAfterBody = _jspx_th_rn_sampleActionFileIterator_2.doAfterBody();
                      modifiedFiles = (org.recipnet.site.content.rncontrols.SampleActionFileIterator) _jspx_page_context.findAttribute("modifiedFiles");
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_rn_sampleActionFileIterator_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_rn_sampleActionFileIterator_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  modifiedFiles = (org.recipnet.site.content.rncontrols.SampleActionFileIterator) _jspx_page_context.findAttribute("modifiedFiles");
                  _jspx_tagPool_rn_sampleActionFileIterator_mode_id.reuse(_jspx_th_rn_sampleActionFileIterator_2);
                  out.write("\n              ");
                  //  ctl:errorMessage
                  org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_14 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
                  _jspx_th_ctl_errorMessage_14.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_errorMessage_14.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleActionIterator_0);
                  _jspx_th_ctl_errorMessage_14.setErrorSupplier((org.recipnet.common.controls.ErrorSupplier) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${modifiedFiles}", org.recipnet.common.controls.ErrorSupplier.class, (PageContext)_jspx_page_context, null, false));
                  _jspx_th_ctl_errorMessage_14.setErrorFilter(HtmlPageIterator.NO_ITERATIONS);
                  int _jspx_eval_ctl_errorMessage_14 = _jspx_th_ctl_errorMessage_14.doStartTag();
                  if (_jspx_eval_ctl_errorMessage_14 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_errorMessage_14 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_errorMessage_14.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_errorMessage_14.doInitBody();
                    }
                    do {
                      out.write("\n                ");
                      //  rn:preferenceChecker
                      org.recipnet.site.content.rncontrols.PreferenceChecker _jspx_th_rn_preferenceChecker_4 = (org.recipnet.site.content.rncontrols.PreferenceChecker) _jspx_tagPool_rn_preferenceChecker_invert_includeIfBooleanPrefIsTrue.get(org.recipnet.site.content.rncontrols.PreferenceChecker.class);
                      _jspx_th_rn_preferenceChecker_4.setPageContext(_jspx_page_context);
                      _jspx_th_rn_preferenceChecker_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_errorMessage_14);
                      _jspx_th_rn_preferenceChecker_4.setIncludeIfBooleanPrefIsTrue(
                        UserBL.Pref.SUPPRESS_BLANK_FILE_LISTINGS);
                      _jspx_th_rn_preferenceChecker_4.setInvert(true);
                      int _jspx_eval_rn_preferenceChecker_4 = _jspx_th_rn_preferenceChecker_4.doStartTag();
                      if (_jspx_eval_rn_preferenceChecker_4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_rn_preferenceChecker_4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_rn_preferenceChecker_4.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_rn_preferenceChecker_4.doInitBody();
                        }
                        do {
                          out.write("\n                  <div>\n                    Files modified:\n                    <span class=\"fileName\">(none)</span>\n                  </div>\n                ");
                          int evalDoAfterBody = _jspx_th_rn_preferenceChecker_4.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_rn_preferenceChecker_4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                          out = _jspx_page_context.popBody();
                      }
                      if (_jspx_th_rn_preferenceChecker_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_rn_preferenceChecker_invert_includeIfBooleanPrefIsTrue.reuse(_jspx_th_rn_preferenceChecker_4);
                      out.write("\n              ");
                      int evalDoAfterBody = _jspx_th_ctl_errorMessage_14.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_ctl_errorMessage_14 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_ctl_errorMessage_14.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.reuse(_jspx_th_ctl_errorMessage_14);
                  out.write("\n              ");
                  //  rn:preferenceChecker
                  org.recipnet.site.content.rncontrols.PreferenceChecker _jspx_th_rn_preferenceChecker_5 = (org.recipnet.site.content.rncontrols.PreferenceChecker) _jspx_tagPool_rn_preferenceChecker_invert_includeIfBooleanPrefIsTrue.get(org.recipnet.site.content.rncontrols.PreferenceChecker.class);
                  _jspx_th_rn_preferenceChecker_5.setPageContext(_jspx_page_context);
                  _jspx_th_rn_preferenceChecker_5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleActionIterator_0);
                  _jspx_th_rn_preferenceChecker_5.setInvert(true);
                  _jspx_th_rn_preferenceChecker_5.setIncludeIfBooleanPrefIsTrue(UserBL.Pref.SUPPRESS_COMMENT);
                  int _jspx_eval_rn_preferenceChecker_5 = _jspx_th_rn_preferenceChecker_5.doStartTag();
                  if (_jspx_eval_rn_preferenceChecker_5 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_rn_preferenceChecker_5 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_rn_preferenceChecker_5.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_rn_preferenceChecker_5.doInitBody();
                    }
                    do {
                      out.write("\n                ");
                      //  rn:workflowCommentChecker
                      org.recipnet.site.content.rncontrols.WorkflowCommentChecker _jspx_th_rn_workflowCommentChecker_0 = (org.recipnet.site.content.rncontrols.WorkflowCommentChecker) _jspx_tagPool_rn_workflowCommentChecker.get(org.recipnet.site.content.rncontrols.WorkflowCommentChecker.class);
                      _jspx_th_rn_workflowCommentChecker_0.setPageContext(_jspx_page_context);
                      _jspx_th_rn_workflowCommentChecker_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_preferenceChecker_5);
                      int _jspx_eval_rn_workflowCommentChecker_0 = _jspx_th_rn_workflowCommentChecker_0.doStartTag();
                      if (_jspx_eval_rn_workflowCommentChecker_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_rn_workflowCommentChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_rn_workflowCommentChecker_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_rn_workflowCommentChecker_0.doInitBody();
                        }
                        do {
                          out.write("\n                  <div>\n                    Comments:<span class=\"comments\">\n                    ");
                          //  rn:sampleHistoryField
                          org.recipnet.site.content.rncontrols.SampleHistoryField _jspx_th_rn_sampleHistoryField_4 = (org.recipnet.site.content.rncontrols.SampleHistoryField) _jspx_tagPool_rn_sampleHistoryField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleHistoryField.class);
                          _jspx_th_rn_sampleHistoryField_4.setPageContext(_jspx_page_context);
                          _jspx_th_rn_sampleHistoryField_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_workflowCommentChecker_0);
                          _jspx_th_rn_sampleHistoryField_4.setFieldCode(
                      SampleHistoryField.FieldCode.WORKFLOW_ACTION_COMMENTS);
                          int _jspx_eval_rn_sampleHistoryField_4 = _jspx_th_rn_sampleHistoryField_4.doStartTag();
                          if (_jspx_th_rn_sampleHistoryField_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          _jspx_tagPool_rn_sampleHistoryField_fieldCode_nobody.reuse(_jspx_th_rn_sampleHistoryField_4);
                          out.write("\n                    </span>\n                  </div>\n                ");
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
                      out.write("\n              ");
                      int evalDoAfterBody = _jspx_th_rn_preferenceChecker_5.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_rn_preferenceChecker_5 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_rn_preferenceChecker_5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_preferenceChecker_invert_includeIfBooleanPrefIsTrue.reuse(_jspx_th_rn_preferenceChecker_5);
                  out.write("\n              ");
                  //  ctl:errorMessage
                  org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_15 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
                  _jspx_th_ctl_errorMessage_15.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_errorMessage_15.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleActionIterator_0);
                  _jspx_th_ctl_errorMessage_15.setErrorFilter(
                SampleActionIterator.CURRENT_ACTION_WAS_SKIPPED_BY_REVERSION);
                  int _jspx_eval_ctl_errorMessage_15 = _jspx_th_ctl_errorMessage_15.doStartTag();
                  if (_jspx_eval_ctl_errorMessage_15 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_errorMessage_15 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_errorMessage_15.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_errorMessage_15.doInitBody();
                    }
                    do {
                      out.write("\n                <div>\n                  <span class=\"skippedByReversionMessage\"> The effect of this\n                    action was undone by a subsequent \"Reverted\" action.</span>\n                </div>\n              ");
                      int evalDoAfterBody = _jspx_th_ctl_errorMessage_15.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_ctl_errorMessage_15 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_ctl_errorMessage_15.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_ctl_errorMessage_errorFilter.reuse(_jspx_th_ctl_errorMessage_15);
                  out.write("\n            </div>\n          </div>\n        ");
                  int evalDoAfterBody = _jspx_th_rn_sampleActionIterator_0.doAfterBody();
                  actionIt = (org.recipnet.site.content.rncontrols.SampleActionIterator) _jspx_page_context.findAttribute("actionIt");
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_rn_sampleActionIterator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_rn_sampleActionIterator_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              actionIt = (org.recipnet.site.content.rncontrols.SampleActionIterator) _jspx_page_context.findAttribute("actionIt");
              _jspx_tagPool_rn_sampleActionIterator_id.reuse(_jspx_th_rn_sampleActionIterator_0);
              out.write("\n        ");
              //  ctl:errorMessage
              org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_16 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
              _jspx_th_ctl_errorMessage_16.setPageContext(_jspx_page_context);
              _jspx_th_ctl_errorMessage_16.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_ctl_errorMessage_16.setErrorSupplier((org.recipnet.common.controls.ErrorSupplier) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${actionIt}", org.recipnet.common.controls.ErrorSupplier.class, (PageContext)_jspx_page_context, null, false));
              _jspx_th_ctl_errorMessage_16.setErrorFilter(
            SampleActionIterator.ONE_FILE_ACTION_WAS_SUPPRESSED
                | SampleActionIterator.SOME_FILE_ACTIONS_WERE_SUPPRESSED
                | SampleActionIterator.ONE_CORRECTION_ACTION_WAS_SUPPRESSED
                | SampleActionIterator.SOME_CORRECTION_ACTIONS_WERE_SUPPRESSED
                | SampleActionIterator.ONE_OTHER_ACTION_WAS_SUPPRESSED
                | SampleActionIterator.SOME_OTHER_ACTIONS_WERE_SUPPRESSED
                | SampleActionIterator.ONE_SKIPPED_ACTION_WAS_SUPPRESSED 
                | SampleActionIterator.SOME_SKIPPED_ACTIONS_WERE_SUPPRESSED);
              int _jspx_eval_ctl_errorMessage_16 = _jspx_th_ctl_errorMessage_16.doStartTag();
              if (_jspx_eval_ctl_errorMessage_16 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_ctl_errorMessage_16 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_ctl_errorMessage_16.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_ctl_errorMessage_16.doInitBody();
                }
                do {
                  out.write("\n        ");
                  int evalDoAfterBody = _jspx_th_ctl_errorMessage_16.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_ctl_errorMessage_16 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_ctl_errorMessage_16.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.reuse(_jspx_th_ctl_errorMessage_16);
              out.write("\n        ");
              //  ctl:errorMessage
              org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_17 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
              _jspx_th_ctl_errorMessage_17.setPageContext(_jspx_page_context);
              _jspx_th_ctl_errorMessage_17.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_ctl_errorMessage_17.setErrorSupplier((org.recipnet.common.controls.ErrorSupplier) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${actionIt}", org.recipnet.common.controls.ErrorSupplier.class, (PageContext)_jspx_page_context, null, false));
              _jspx_th_ctl_errorMessage_17.setErrorFilter(
            SampleActionIterator.ONE_FILE_ACTION_WAS_SUPPRESSED);
              int _jspx_eval_ctl_errorMessage_17 = _jspx_th_ctl_errorMessage_17.doStartTag();
              if (_jspx_eval_ctl_errorMessage_17 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_ctl_errorMessage_17 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_ctl_errorMessage_17.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_ctl_errorMessage_17.doInitBody();
                }
                do {
                  out.write("\n          <div>\n            <span class=\"dark\">1</span>\n            empty file action box was suppressed.\n            ");
                  //  rn:link
                  org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_2 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_style_href.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
                  _jspx_th_rn_link_2.setPageContext(_jspx_page_context);
                  _jspx_th_rn_link_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_errorMessage_17);
                  _jspx_th_rn_link_2.setHref("/lab/sample.jsp");
                  _jspx_th_rn_link_2.setStyle("padding-left: 1em;");
                  int _jspx_eval_rn_link_2 = _jspx_th_rn_link_2.doStartTag();
                  if (_jspx_eval_rn_link_2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_rn_link_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_rn_link_2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_rn_link_2.doInitBody();
                    }
                    do {
                      out.write("\n              ");
                      //  ctl:linkParam
                      org.recipnet.common.controls.LinkParam _jspx_th_ctl_linkParam_0 = (org.recipnet.common.controls.LinkParam) _jspx_tagPool_ctl_linkParam_value_name_nobody.get(org.recipnet.common.controls.LinkParam.class);
                      _jspx_th_ctl_linkParam_0.setPageContext(_jspx_page_context);
                      _jspx_th_ctl_linkParam_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_link_2);
                      _jspx_th_ctl_linkParam_0.setName("unsetSuppression");
                      _jspx_th_ctl_linkParam_0.setValue( UserBL.Pref.SUPPRESS_EMPTY_FILE.toString());
                      int _jspx_eval_ctl_linkParam_0 = _jspx_th_ctl_linkParam_0.doStartTag();
                      if (_jspx_th_ctl_linkParam_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_ctl_linkParam_value_name_nobody.reuse(_jspx_th_ctl_linkParam_0);
                      out.write("\n              see this action box...\n            ");
                      int evalDoAfterBody = _jspx_th_rn_link_2.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_rn_link_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_rn_link_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_link_style_href.reuse(_jspx_th_rn_link_2);
                  out.write("\n          </div>\n        ");
                  int evalDoAfterBody = _jspx_th_ctl_errorMessage_17.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_ctl_errorMessage_17 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_ctl_errorMessage_17.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.reuse(_jspx_th_ctl_errorMessage_17);
              out.write("\n        ");
              //  ctl:errorMessage
              org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_18 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
              _jspx_th_ctl_errorMessage_18.setPageContext(_jspx_page_context);
              _jspx_th_ctl_errorMessage_18.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_ctl_errorMessage_18.setErrorSupplier((org.recipnet.common.controls.ErrorSupplier) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${actionIt}", org.recipnet.common.controls.ErrorSupplier.class, (PageContext)_jspx_page_context, null, false));
              _jspx_th_ctl_errorMessage_18.setErrorFilter(
            SampleActionIterator.SOME_FILE_ACTIONS_WERE_SUPPRESSED);
              int _jspx_eval_ctl_errorMessage_18 = _jspx_th_ctl_errorMessage_18.doStartTag();
              if (_jspx_eval_ctl_errorMessage_18 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_ctl_errorMessage_18 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_ctl_errorMessage_18.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_ctl_errorMessage_18.doInitBody();
                }
                do {
                  out.write("\n          <div>\n            <span class=\"dark\">");
                  if (_jspx_meth_rn_suppressedActionCount_0(_jspx_th_ctl_errorMessage_18, _jspx_page_context))
                    return;
                  out.write("</span>\n            empty file action boxes were suppressed.\n            ");
                  //  rn:link
                  org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_3 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_style_href.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
                  _jspx_th_rn_link_3.setPageContext(_jspx_page_context);
                  _jspx_th_rn_link_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_errorMessage_18);
                  _jspx_th_rn_link_3.setHref("/lab/sample.jsp");
                  _jspx_th_rn_link_3.setStyle("padding-left: 1em;");
                  int _jspx_eval_rn_link_3 = _jspx_th_rn_link_3.doStartTag();
                  if (_jspx_eval_rn_link_3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_rn_link_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_rn_link_3.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_rn_link_3.doInitBody();
                    }
                    do {
                      out.write("\n              ");
                      //  ctl:linkParam
                      org.recipnet.common.controls.LinkParam _jspx_th_ctl_linkParam_1 = (org.recipnet.common.controls.LinkParam) _jspx_tagPool_ctl_linkParam_value_name_nobody.get(org.recipnet.common.controls.LinkParam.class);
                      _jspx_th_ctl_linkParam_1.setPageContext(_jspx_page_context);
                      _jspx_th_ctl_linkParam_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_link_3);
                      _jspx_th_ctl_linkParam_1.setName("unsetSuppression");
                      _jspx_th_ctl_linkParam_1.setValue( UserBL.Pref.SUPPRESS_EMPTY_FILE.toString());
                      int _jspx_eval_ctl_linkParam_1 = _jspx_th_ctl_linkParam_1.doStartTag();
                      if (_jspx_th_ctl_linkParam_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_ctl_linkParam_value_name_nobody.reuse(_jspx_th_ctl_linkParam_1);
                      out.write("\n              see these action boxes...\n            ");
                      int evalDoAfterBody = _jspx_th_rn_link_3.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_rn_link_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_rn_link_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_link_style_href.reuse(_jspx_th_rn_link_3);
                  out.write("\n          </div>\n        ");
                  int evalDoAfterBody = _jspx_th_ctl_errorMessage_18.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_ctl_errorMessage_18 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_ctl_errorMessage_18.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.reuse(_jspx_th_ctl_errorMessage_18);
              out.write("\n        ");
              //  ctl:errorMessage
              org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_19 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
              _jspx_th_ctl_errorMessage_19.setPageContext(_jspx_page_context);
              _jspx_th_ctl_errorMessage_19.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_ctl_errorMessage_19.setErrorSupplier((org.recipnet.common.controls.ErrorSupplier) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${actionIt}", org.recipnet.common.controls.ErrorSupplier.class, (PageContext)_jspx_page_context, null, false));
              _jspx_th_ctl_errorMessage_19.setErrorFilter(
            SampleActionIterator.ONE_CORRECTION_ACTION_WAS_SUPPRESSED);
              int _jspx_eval_ctl_errorMessage_19 = _jspx_th_ctl_errorMessage_19.doStartTag();
              if (_jspx_eval_ctl_errorMessage_19 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_ctl_errorMessage_19 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_ctl_errorMessage_19.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_ctl_errorMessage_19.doInitBody();
                }
                do {
                  out.write("\n          <div>\n            <span class=\"dark\">1</span>\n            empty correction action box was suppressed.\n            ");
                  //  rn:link
                  org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_4 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_style_href.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
                  _jspx_th_rn_link_4.setPageContext(_jspx_page_context);
                  _jspx_th_rn_link_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_errorMessage_19);
                  _jspx_th_rn_link_4.setHref("/lab/sample.jsp");
                  _jspx_th_rn_link_4.setStyle("padding-left: 1em;");
                  int _jspx_eval_rn_link_4 = _jspx_th_rn_link_4.doStartTag();
                  if (_jspx_eval_rn_link_4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_rn_link_4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_rn_link_4.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_rn_link_4.doInitBody();
                    }
                    do {
                      out.write("\n              ");
                      //  ctl:linkParam
                      org.recipnet.common.controls.LinkParam _jspx_th_ctl_linkParam_2 = (org.recipnet.common.controls.LinkParam) _jspx_tagPool_ctl_linkParam_value_name_nobody.get(org.recipnet.common.controls.LinkParam.class);
                      _jspx_th_ctl_linkParam_2.setPageContext(_jspx_page_context);
                      _jspx_th_ctl_linkParam_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_link_4);
                      _jspx_th_ctl_linkParam_2.setName("unsetSuppression");
                      _jspx_th_ctl_linkParam_2.setValue( UserBL.Pref.SUPPRESS_EMPTY_CORRECTION.toString());
                      int _jspx_eval_ctl_linkParam_2 = _jspx_th_ctl_linkParam_2.doStartTag();
                      if (_jspx_th_ctl_linkParam_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_ctl_linkParam_value_name_nobody.reuse(_jspx_th_ctl_linkParam_2);
                      out.write("\n              see this action box...\n            ");
                      int evalDoAfterBody = _jspx_th_rn_link_4.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_rn_link_4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_rn_link_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_link_style_href.reuse(_jspx_th_rn_link_4);
                  out.write("\n          </div>\n        ");
                  int evalDoAfterBody = _jspx_th_ctl_errorMessage_19.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_ctl_errorMessage_19 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_ctl_errorMessage_19.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.reuse(_jspx_th_ctl_errorMessage_19);
              out.write("\n        ");
              //  ctl:errorMessage
              org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_20 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
              _jspx_th_ctl_errorMessage_20.setPageContext(_jspx_page_context);
              _jspx_th_ctl_errorMessage_20.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_ctl_errorMessage_20.setErrorSupplier((org.recipnet.common.controls.ErrorSupplier) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${actionIt}", org.recipnet.common.controls.ErrorSupplier.class, (PageContext)_jspx_page_context, null, false));
              _jspx_th_ctl_errorMessage_20.setErrorFilter(
            SampleActionIterator.SOME_CORRECTION_ACTIONS_WERE_SUPPRESSED);
              int _jspx_eval_ctl_errorMessage_20 = _jspx_th_ctl_errorMessage_20.doStartTag();
              if (_jspx_eval_ctl_errorMessage_20 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_ctl_errorMessage_20 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_ctl_errorMessage_20.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_ctl_errorMessage_20.doInitBody();
                }
                do {
                  out.write("\n          <div>\n            <span class=\"dark\">");
                  if (_jspx_meth_rn_suppressedActionCount_1(_jspx_th_ctl_errorMessage_20, _jspx_page_context))
                    return;
                  out.write("</span>\n            empty correction action boxes were suppressed.\n            ");
                  //  rn:link
                  org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_5 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_style_href.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
                  _jspx_th_rn_link_5.setPageContext(_jspx_page_context);
                  _jspx_th_rn_link_5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_errorMessage_20);
                  _jspx_th_rn_link_5.setHref("/lab/sample.jsp");
                  _jspx_th_rn_link_5.setStyle("padding-left: 1em;");
                  int _jspx_eval_rn_link_5 = _jspx_th_rn_link_5.doStartTag();
                  if (_jspx_eval_rn_link_5 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_rn_link_5 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_rn_link_5.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_rn_link_5.doInitBody();
                    }
                    do {
                      out.write("\n              ");
                      //  ctl:linkParam
                      org.recipnet.common.controls.LinkParam _jspx_th_ctl_linkParam_3 = (org.recipnet.common.controls.LinkParam) _jspx_tagPool_ctl_linkParam_value_name_nobody.get(org.recipnet.common.controls.LinkParam.class);
                      _jspx_th_ctl_linkParam_3.setPageContext(_jspx_page_context);
                      _jspx_th_ctl_linkParam_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_link_5);
                      _jspx_th_ctl_linkParam_3.setName("unsetSuppression");
                      _jspx_th_ctl_linkParam_3.setValue( UserBL.Pref.SUPPRESS_EMPTY_CORRECTION.toString());
                      int _jspx_eval_ctl_linkParam_3 = _jspx_th_ctl_linkParam_3.doStartTag();
                      if (_jspx_th_ctl_linkParam_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_ctl_linkParam_value_name_nobody.reuse(_jspx_th_ctl_linkParam_3);
                      out.write("\n              see these action boxes...\n            ");
                      int evalDoAfterBody = _jspx_th_rn_link_5.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_rn_link_5 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_rn_link_5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_link_style_href.reuse(_jspx_th_rn_link_5);
                  out.write("\n          </div>\n        ");
                  int evalDoAfterBody = _jspx_th_ctl_errorMessage_20.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_ctl_errorMessage_20 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_ctl_errorMessage_20.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.reuse(_jspx_th_ctl_errorMessage_20);
              out.write("\n        ");
              //  ctl:errorMessage
              org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_21 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
              _jspx_th_ctl_errorMessage_21.setPageContext(_jspx_page_context);
              _jspx_th_ctl_errorMessage_21.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_ctl_errorMessage_21.setErrorSupplier((org.recipnet.common.controls.ErrorSupplier) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${actionIt}", org.recipnet.common.controls.ErrorSupplier.class, (PageContext)_jspx_page_context, null, false));
              _jspx_th_ctl_errorMessage_21.setErrorFilter(
            SampleActionIterator.ONE_OTHER_ACTION_WAS_SUPPRESSED);
              int _jspx_eval_ctl_errorMessage_21 = _jspx_th_ctl_errorMessage_21.doStartTag();
              if (_jspx_eval_ctl_errorMessage_21 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_ctl_errorMessage_21 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_ctl_errorMessage_21.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_ctl_errorMessage_21.doInitBody();
                }
                do {
                  out.write("\n          <div>\n            <span class=\"dark\">1</span>\n            other empty action box was suppressed.\n            ");
                  //  rn:link
                  org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_6 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_style_href.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
                  _jspx_th_rn_link_6.setPageContext(_jspx_page_context);
                  _jspx_th_rn_link_6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_errorMessage_21);
                  _jspx_th_rn_link_6.setHref("/lab/sample.jsp");
                  _jspx_th_rn_link_6.setStyle("padding-left: 1em;");
                  int _jspx_eval_rn_link_6 = _jspx_th_rn_link_6.doStartTag();
                  if (_jspx_eval_rn_link_6 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_rn_link_6 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_rn_link_6.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_rn_link_6.doInitBody();
                    }
                    do {
                      out.write("\n              ");
                      //  ctl:linkParam
                      org.recipnet.common.controls.LinkParam _jspx_th_ctl_linkParam_4 = (org.recipnet.common.controls.LinkParam) _jspx_tagPool_ctl_linkParam_value_name_nobody.get(org.recipnet.common.controls.LinkParam.class);
                      _jspx_th_ctl_linkParam_4.setPageContext(_jspx_page_context);
                      _jspx_th_ctl_linkParam_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_link_6);
                      _jspx_th_ctl_linkParam_4.setName("unsetSuppression");
                      _jspx_th_ctl_linkParam_4.setValue( UserBL.Pref.SUPPRESS_EMPTY_OTHER.toString());
                      int _jspx_eval_ctl_linkParam_4 = _jspx_th_ctl_linkParam_4.doStartTag();
                      if (_jspx_th_ctl_linkParam_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_ctl_linkParam_value_name_nobody.reuse(_jspx_th_ctl_linkParam_4);
                      out.write("\n              see this action box...\n            ");
                      int evalDoAfterBody = _jspx_th_rn_link_6.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_rn_link_6 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_rn_link_6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_link_style_href.reuse(_jspx_th_rn_link_6);
                  out.write("\n          </div>\n        ");
                  int evalDoAfterBody = _jspx_th_ctl_errorMessage_21.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_ctl_errorMessage_21 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_ctl_errorMessage_21.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.reuse(_jspx_th_ctl_errorMessage_21);
              out.write("\n        ");
              //  ctl:errorMessage
              org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_22 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
              _jspx_th_ctl_errorMessage_22.setPageContext(_jspx_page_context);
              _jspx_th_ctl_errorMessage_22.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_ctl_errorMessage_22.setErrorSupplier((org.recipnet.common.controls.ErrorSupplier) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${actionIt}", org.recipnet.common.controls.ErrorSupplier.class, (PageContext)_jspx_page_context, null, false));
              _jspx_th_ctl_errorMessage_22.setErrorFilter(
            SampleActionIterator.SOME_OTHER_ACTIONS_WERE_SUPPRESSED);
              int _jspx_eval_ctl_errorMessage_22 = _jspx_th_ctl_errorMessage_22.doStartTag();
              if (_jspx_eval_ctl_errorMessage_22 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_ctl_errorMessage_22 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_ctl_errorMessage_22.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_ctl_errorMessage_22.doInitBody();
                }
                do {
                  out.write("\n          <div>\n            <span class=\"dark\">");
                  if (_jspx_meth_rn_suppressedActionCount_2(_jspx_th_ctl_errorMessage_22, _jspx_page_context))
                    return;
                  out.write("</span>\n            other empty action boxes were suppressed.\n            ");
                  //  rn:link
                  org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_7 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_style_href.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
                  _jspx_th_rn_link_7.setPageContext(_jspx_page_context);
                  _jspx_th_rn_link_7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_errorMessage_22);
                  _jspx_th_rn_link_7.setHref("/lab/sample.jsp");
                  _jspx_th_rn_link_7.setStyle("padding-left: 1em;");
                  int _jspx_eval_rn_link_7 = _jspx_th_rn_link_7.doStartTag();
                  if (_jspx_eval_rn_link_7 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_rn_link_7 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_rn_link_7.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_rn_link_7.doInitBody();
                    }
                    do {
                      out.write("\n              ");
                      //  ctl:linkParam
                      org.recipnet.common.controls.LinkParam _jspx_th_ctl_linkParam_5 = (org.recipnet.common.controls.LinkParam) _jspx_tagPool_ctl_linkParam_value_name_nobody.get(org.recipnet.common.controls.LinkParam.class);
                      _jspx_th_ctl_linkParam_5.setPageContext(_jspx_page_context);
                      _jspx_th_ctl_linkParam_5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_link_7);
                      _jspx_th_ctl_linkParam_5.setName("unsetSuppression");
                      _jspx_th_ctl_linkParam_5.setValue( UserBL.Pref.SUPPRESS_EMPTY_OTHER.toString());
                      int _jspx_eval_ctl_linkParam_5 = _jspx_th_ctl_linkParam_5.doStartTag();
                      if (_jspx_th_ctl_linkParam_5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_ctl_linkParam_value_name_nobody.reuse(_jspx_th_ctl_linkParam_5);
                      out.write("\n              see these action boxes...\n            ");
                      int evalDoAfterBody = _jspx_th_rn_link_7.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_rn_link_7 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_rn_link_7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_link_style_href.reuse(_jspx_th_rn_link_7);
                  out.write("\n          </div>\n        ");
                  int evalDoAfterBody = _jspx_th_ctl_errorMessage_22.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_ctl_errorMessage_22 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_ctl_errorMessage_22.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.reuse(_jspx_th_ctl_errorMessage_22);
              out.write("\n        ");
              //  ctl:errorMessage
              org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_23 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
              _jspx_th_ctl_errorMessage_23.setPageContext(_jspx_page_context);
              _jspx_th_ctl_errorMessage_23.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_ctl_errorMessage_23.setErrorSupplier((org.recipnet.common.controls.ErrorSupplier) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${actionIt}", org.recipnet.common.controls.ErrorSupplier.class, (PageContext)_jspx_page_context, null, false));
              _jspx_th_ctl_errorMessage_23.setErrorFilter(
            SampleActionIterator.ONE_SKIPPED_ACTION_WAS_SUPPRESSED);
              int _jspx_eval_ctl_errorMessage_23 = _jspx_th_ctl_errorMessage_23.doStartTag();
              if (_jspx_eval_ctl_errorMessage_23 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_ctl_errorMessage_23 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_ctl_errorMessage_23.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_ctl_errorMessage_23.doInitBody();
                }
                do {
                  out.write("\n          <div>\n            <span class=\"dark\">1</span>\n            action that was skipped by a reversion was suppressed.\n            ");
                  //  rn:link
                  org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_8 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_style_href.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
                  _jspx_th_rn_link_8.setPageContext(_jspx_page_context);
                  _jspx_th_rn_link_8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_errorMessage_23);
                  _jspx_th_rn_link_8.setHref("/lab/sample.jsp");
                  _jspx_th_rn_link_8.setStyle("padding-left: 1em;");
                  int _jspx_eval_rn_link_8 = _jspx_th_rn_link_8.doStartTag();
                  if (_jspx_eval_rn_link_8 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_rn_link_8 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_rn_link_8.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_rn_link_8.doInitBody();
                    }
                    do {
                      out.write("\n              ");
                      //  ctl:linkParam
                      org.recipnet.common.controls.LinkParam _jspx_th_ctl_linkParam_6 = (org.recipnet.common.controls.LinkParam) _jspx_tagPool_ctl_linkParam_value_name_nobody.get(org.recipnet.common.controls.LinkParam.class);
                      _jspx_th_ctl_linkParam_6.setPageContext(_jspx_page_context);
                      _jspx_th_ctl_linkParam_6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_link_8);
                      _jspx_th_ctl_linkParam_6.setName("unsetSuppression");
                      _jspx_th_ctl_linkParam_6.setValue( UserBL.Pref.SUPPRESS_SKIPPED.toString());
                      int _jspx_eval_ctl_linkParam_6 = _jspx_th_ctl_linkParam_6.doStartTag();
                      if (_jspx_th_ctl_linkParam_6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_ctl_linkParam_value_name_nobody.reuse(_jspx_th_ctl_linkParam_6);
                      out.write("\n              see this action box...\n            ");
                      int evalDoAfterBody = _jspx_th_rn_link_8.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_rn_link_8 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_rn_link_8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_link_style_href.reuse(_jspx_th_rn_link_8);
                  out.write("\n          </div>\n        ");
                  int evalDoAfterBody = _jspx_th_ctl_errorMessage_23.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_ctl_errorMessage_23 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_ctl_errorMessage_23.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.reuse(_jspx_th_ctl_errorMessage_23);
              out.write("\n        ");
              //  ctl:errorMessage
              org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_24 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
              _jspx_th_ctl_errorMessage_24.setPageContext(_jspx_page_context);
              _jspx_th_ctl_errorMessage_24.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_ctl_errorMessage_24.setErrorSupplier((org.recipnet.common.controls.ErrorSupplier) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${actionIt}", org.recipnet.common.controls.ErrorSupplier.class, (PageContext)_jspx_page_context, null, false));
              _jspx_th_ctl_errorMessage_24.setErrorFilter(
            SampleActionIterator.SOME_SKIPPED_ACTIONS_WERE_SUPPRESSED);
              int _jspx_eval_ctl_errorMessage_24 = _jspx_th_ctl_errorMessage_24.doStartTag();
              if (_jspx_eval_ctl_errorMessage_24 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_ctl_errorMessage_24 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_ctl_errorMessage_24.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_ctl_errorMessage_24.doInitBody();
                }
                do {
                  out.write("\n          <div>\n            <span class=\"dark\">");
                  if (_jspx_meth_rn_suppressedActionCount_3(_jspx_th_ctl_errorMessage_24, _jspx_page_context))
                    return;
                  out.write("</span>\n            actions that were skipped by a reversion were suppressed.\n            ");
                  //  rn:link
                  org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_9 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_style_href.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
                  _jspx_th_rn_link_9.setPageContext(_jspx_page_context);
                  _jspx_th_rn_link_9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_errorMessage_24);
                  _jspx_th_rn_link_9.setHref("/lab/sample.jsp");
                  _jspx_th_rn_link_9.setStyle("padding-left: 1em;");
                  int _jspx_eval_rn_link_9 = _jspx_th_rn_link_9.doStartTag();
                  if (_jspx_eval_rn_link_9 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_rn_link_9 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_rn_link_9.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_rn_link_9.doInitBody();
                    }
                    do {
                      out.write("\n              ");
                      //  ctl:linkParam
                      org.recipnet.common.controls.LinkParam _jspx_th_ctl_linkParam_7 = (org.recipnet.common.controls.LinkParam) _jspx_tagPool_ctl_linkParam_value_name_nobody.get(org.recipnet.common.controls.LinkParam.class);
                      _jspx_th_ctl_linkParam_7.setPageContext(_jspx_page_context);
                      _jspx_th_ctl_linkParam_7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_link_9);
                      _jspx_th_ctl_linkParam_7.setName("unsetSuppression");
                      _jspx_th_ctl_linkParam_7.setValue( UserBL.Pref.SUPPRESS_SKIPPED.toString());
                      int _jspx_eval_ctl_linkParam_7 = _jspx_th_ctl_linkParam_7.doStartTag();
                      if (_jspx_th_ctl_linkParam_7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_ctl_linkParam_value_name_nobody.reuse(_jspx_th_ctl_linkParam_7);
                      out.write("\n              see these action boxes...\n            ");
                      int evalDoAfterBody = _jspx_th_rn_link_9.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_rn_link_9 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_rn_link_9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_link_style_href.reuse(_jspx_th_rn_link_9);
                  out.write("\n          </div>\n        ");
                  int evalDoAfterBody = _jspx_th_ctl_errorMessage_24.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_ctl_errorMessage_24 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_ctl_errorMessage_24.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.reuse(_jspx_th_ctl_errorMessage_24);
              out.write("\n        </td>\n      </tr>\n    </table>\n    <table id=\"workflowTable\">\n      <tr>\n        <th colspan=\"5\">Standard Workflow:</th>\n        <th colspan=\"3\" class=\"leftPadded\">Other Options:</th>\n      </tr>\n      <tr>\n        <td>\n          ");
              //  rn:sampleChecker
              org.recipnet.site.content.rncontrols.SampleChecker _jspx_th_rn_sampleChecker_2 = (org.recipnet.site.content.rncontrols.SampleChecker) _jspx_tagPool_rn_sampleChecker_includeIfActionIsValid.get(org.recipnet.site.content.rncontrols.SampleChecker.class);
              _jspx_th_rn_sampleChecker_2.setPageContext(_jspx_page_context);
              _jspx_th_rn_sampleChecker_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_sampleChecker_2.setIncludeIfActionIsValid(
              SampleWorkflowBL.PRELIMINARY_DATA_COLLECTED);
              int _jspx_eval_rn_sampleChecker_2 = _jspx_th_rn_sampleChecker_2.doStartTag();
              if (_jspx_eval_rn_sampleChecker_2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_rn_sampleChecker_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_rn_sampleChecker_2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_rn_sampleChecker_2.doInitBody();
                }
                do {
                  out.write("\n            ");
                  if (_jspx_meth_rn_link_10(_jspx_th_rn_sampleChecker_2, _jspx_page_context))
                    return;
                  out.write("\n          ");
                  int evalDoAfterBody = _jspx_th_rn_sampleChecker_2.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_rn_sampleChecker_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_rn_sampleChecker_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_sampleChecker_includeIfActionIsValid.reuse(_jspx_th_rn_sampleChecker_2);
              out.write("\n          ");
              //  rn:sampleChecker
              org.recipnet.site.content.rncontrols.SampleChecker _jspx_th_rn_sampleChecker_3 = (org.recipnet.site.content.rncontrols.SampleChecker) _jspx_tagPool_rn_sampleChecker_invert_includeIfActionIsValid.get(org.recipnet.site.content.rncontrols.SampleChecker.class);
              _jspx_th_rn_sampleChecker_3.setPageContext(_jspx_page_context);
              _jspx_th_rn_sampleChecker_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_sampleChecker_3.setInvert(true);
              _jspx_th_rn_sampleChecker_3.setIncludeIfActionIsValid(
              SampleWorkflowBL.PRELIMINARY_DATA_COLLECTED);
              int _jspx_eval_rn_sampleChecker_3 = _jspx_th_rn_sampleChecker_3.doStartTag();
              if (_jspx_eval_rn_sampleChecker_3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_rn_sampleChecker_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_rn_sampleChecker_3.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_rn_sampleChecker_3.doInitBody();
                }
                do {
                  out.write("\n            Collect preliminary data\n          ");
                  int evalDoAfterBody = _jspx_th_rn_sampleChecker_3.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_rn_sampleChecker_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_rn_sampleChecker_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_sampleChecker_invert_includeIfActionIsValid.reuse(_jspx_th_rn_sampleChecker_3);
              out.write("</td>\n        <td rowspan=\"4\">\n          ");
              if (_jspx_meth_ctl_img_7(_jspx_th_ctl_selfForm_0, _jspx_page_context))
                return;
              out.write("\n        </td>\n        <td>\n          ");
              //  rn:sampleChecker
              org.recipnet.site.content.rncontrols.SampleChecker _jspx_th_rn_sampleChecker_4 = (org.recipnet.site.content.rncontrols.SampleChecker) _jspx_tagPool_rn_sampleChecker_includeIfActionIsValid.get(org.recipnet.site.content.rncontrols.SampleChecker.class);
              _jspx_th_rn_sampleChecker_4.setPageContext(_jspx_page_context);
              _jspx_th_rn_sampleChecker_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_sampleChecker_4.setIncludeIfActionIsValid(
              SampleWorkflowBL.STRUCTURE_REFINED);
              int _jspx_eval_rn_sampleChecker_4 = _jspx_th_rn_sampleChecker_4.doStartTag();
              if (_jspx_eval_rn_sampleChecker_4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_rn_sampleChecker_4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_rn_sampleChecker_4.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_rn_sampleChecker_4.doInitBody();
                }
                do {
                  out.write("\n            ");
                  if (_jspx_meth_rn_link_11(_jspx_th_rn_sampleChecker_4, _jspx_page_context))
                    return;
                  out.write("\n          ");
                  int evalDoAfterBody = _jspx_th_rn_sampleChecker_4.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_rn_sampleChecker_4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_rn_sampleChecker_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_sampleChecker_includeIfActionIsValid.reuse(_jspx_th_rn_sampleChecker_4);
              out.write("\n          ");
              //  rn:sampleChecker
              org.recipnet.site.content.rncontrols.SampleChecker _jspx_th_rn_sampleChecker_5 = (org.recipnet.site.content.rncontrols.SampleChecker) _jspx_tagPool_rn_sampleChecker_invert_includeIfActionIsValid.get(org.recipnet.site.content.rncontrols.SampleChecker.class);
              _jspx_th_rn_sampleChecker_5.setPageContext(_jspx_page_context);
              _jspx_th_rn_sampleChecker_5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_sampleChecker_5.setInvert(true);
              _jspx_th_rn_sampleChecker_5.setIncludeIfActionIsValid(
              SampleWorkflowBL.STRUCTURE_REFINED);
              int _jspx_eval_rn_sampleChecker_5 = _jspx_th_rn_sampleChecker_5.doStartTag();
              if (_jspx_eval_rn_sampleChecker_5 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_rn_sampleChecker_5 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_rn_sampleChecker_5.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_rn_sampleChecker_5.doInitBody();
                }
                do {
                  out.write("\n            Refine structure\n          ");
                  int evalDoAfterBody = _jspx_th_rn_sampleChecker_5.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_rn_sampleChecker_5 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_rn_sampleChecker_5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_sampleChecker_invert_includeIfActionIsValid.reuse(_jspx_th_rn_sampleChecker_5);
              out.write("\n        </td>\n        <td rowspan=\"4\">\n          ");
              if (_jspx_meth_ctl_img_8(_jspx_th_ctl_selfForm_0, _jspx_page_context))
                return;
              out.write("\n        </td>\n        <td>\n          ");
              //  rn:sampleChecker
              org.recipnet.site.content.rncontrols.SampleChecker _jspx_th_rn_sampleChecker_6 = (org.recipnet.site.content.rncontrols.SampleChecker) _jspx_tagPool_rn_sampleChecker_includeIfActionIsValid.get(org.recipnet.site.content.rncontrols.SampleChecker.class);
              _jspx_th_rn_sampleChecker_6.setPageContext(_jspx_page_context);
              _jspx_th_rn_sampleChecker_6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_sampleChecker_6.setIncludeIfActionIsValid(
              SampleWorkflowBL.RELEASED_TO_PUBLIC);
              int _jspx_eval_rn_sampleChecker_6 = _jspx_th_rn_sampleChecker_6.doStartTag();
              if (_jspx_eval_rn_sampleChecker_6 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_rn_sampleChecker_6 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_rn_sampleChecker_6.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_rn_sampleChecker_6.doInitBody();
                }
                do {
                  out.write("\n            ");
                  if (_jspx_meth_rn_link_12(_jspx_th_rn_sampleChecker_6, _jspx_page_context))
                    return;
                  out.write("\n          ");
                  int evalDoAfterBody = _jspx_th_rn_sampleChecker_6.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_rn_sampleChecker_6 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_rn_sampleChecker_6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_sampleChecker_includeIfActionIsValid.reuse(_jspx_th_rn_sampleChecker_6);
              out.write("\n          ");
              //  rn:sampleChecker
              org.recipnet.site.content.rncontrols.SampleChecker _jspx_th_rn_sampleChecker_7 = (org.recipnet.site.content.rncontrols.SampleChecker) _jspx_tagPool_rn_sampleChecker_invert_includeIfActionIsValid.get(org.recipnet.site.content.rncontrols.SampleChecker.class);
              _jspx_th_rn_sampleChecker_7.setPageContext(_jspx_page_context);
              _jspx_th_rn_sampleChecker_7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_sampleChecker_7.setInvert(true);
              _jspx_th_rn_sampleChecker_7.setIncludeIfActionIsValid(
              SampleWorkflowBL.RELEASED_TO_PUBLIC);
              int _jspx_eval_rn_sampleChecker_7 = _jspx_th_rn_sampleChecker_7.doStartTag();
              if (_jspx_eval_rn_sampleChecker_7 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_rn_sampleChecker_7 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_rn_sampleChecker_7.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_rn_sampleChecker_7.doInitBody();
                }
                do {
                  out.write("\n            Release to Public\n          ");
                  int evalDoAfterBody = _jspx_th_rn_sampleChecker_7.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_rn_sampleChecker_7 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_rn_sampleChecker_7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_sampleChecker_invert_includeIfActionIsValid.reuse(_jspx_th_rn_sampleChecker_7);
              out.write("\n        </td>\n        <td class=\"leftPadded\">\n          ");
              //  rn:sampleChecker
              org.recipnet.site.content.rncontrols.SampleChecker _jspx_th_rn_sampleChecker_8 = (org.recipnet.site.content.rncontrols.SampleChecker) _jspx_tagPool_rn_sampleChecker_includeIfActionIsValid.get(org.recipnet.site.content.rncontrols.SampleChecker.class);
              _jspx_th_rn_sampleChecker_8.setPageContext(_jspx_page_context);
              _jspx_th_rn_sampleChecker_8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_sampleChecker_8.setIncludeIfActionIsValid(
              SampleWorkflowBL.MODIFIED_TEXT_FIELDS);
              int _jspx_eval_rn_sampleChecker_8 = _jspx_th_rn_sampleChecker_8.doStartTag();
              if (_jspx_eval_rn_sampleChecker_8 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_rn_sampleChecker_8 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_rn_sampleChecker_8.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_rn_sampleChecker_8.doInitBody();
                }
                do {
                  out.write("\n            ");
                  if (_jspx_meth_rn_link_13(_jspx_th_rn_sampleChecker_8, _jspx_page_context))
                    return;
                  out.write("\n          ");
                  int evalDoAfterBody = _jspx_th_rn_sampleChecker_8.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_rn_sampleChecker_8 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_rn_sampleChecker_8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_sampleChecker_includeIfActionIsValid.reuse(_jspx_th_rn_sampleChecker_8);
              out.write("\n          ");
              //  rn:sampleChecker
              org.recipnet.site.content.rncontrols.SampleChecker _jspx_th_rn_sampleChecker_9 = (org.recipnet.site.content.rncontrols.SampleChecker) _jspx_tagPool_rn_sampleChecker_invert_includeIfActionIsValid.get(org.recipnet.site.content.rncontrols.SampleChecker.class);
              _jspx_th_rn_sampleChecker_9.setPageContext(_jspx_page_context);
              _jspx_th_rn_sampleChecker_9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_sampleChecker_9.setInvert(true);
              _jspx_th_rn_sampleChecker_9.setIncludeIfActionIsValid(
              SampleWorkflowBL.MODIFIED_TEXT_FIELDS);
              int _jspx_eval_rn_sampleChecker_9 = _jspx_th_rn_sampleChecker_9.doStartTag();
              if (_jspx_eval_rn_sampleChecker_9 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_rn_sampleChecker_9 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_rn_sampleChecker_9.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_rn_sampleChecker_9.doInitBody();
                }
                do {
                  out.write("\n            Modify text\n          ");
                  int evalDoAfterBody = _jspx_th_rn_sampleChecker_9.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_rn_sampleChecker_9 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_rn_sampleChecker_9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_sampleChecker_invert_includeIfActionIsValid.reuse(_jspx_th_rn_sampleChecker_9);
              out.write("\n        </td>\n        <td class=\"leftPadded\">\n          ");
              //  rn:sampleChecker
              org.recipnet.site.content.rncontrols.SampleChecker _jspx_th_rn_sampleChecker_10 = (org.recipnet.site.content.rncontrols.SampleChecker) _jspx_tagPool_rn_sampleChecker_includeIfActionIsValid.get(org.recipnet.site.content.rncontrols.SampleChecker.class);
              _jspx_th_rn_sampleChecker_10.setPageContext(_jspx_page_context);
              _jspx_th_rn_sampleChecker_10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_sampleChecker_10.setIncludeIfActionIsValid(
              SampleWorkflowBL.CITATION_ADDED);
              int _jspx_eval_rn_sampleChecker_10 = _jspx_th_rn_sampleChecker_10.doStartTag();
              if (_jspx_eval_rn_sampleChecker_10 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_rn_sampleChecker_10 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_rn_sampleChecker_10.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_rn_sampleChecker_10.doInitBody();
                }
                do {
                  out.write("\n            ");
                  if (_jspx_meth_rn_link_14(_jspx_th_rn_sampleChecker_10, _jspx_page_context))
                    return;
                  out.write("\n          ");
                  int evalDoAfterBody = _jspx_th_rn_sampleChecker_10.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_rn_sampleChecker_10 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_rn_sampleChecker_10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_sampleChecker_includeIfActionIsValid.reuse(_jspx_th_rn_sampleChecker_10);
              out.write("\n          ");
              //  rn:sampleChecker
              org.recipnet.site.content.rncontrols.SampleChecker _jspx_th_rn_sampleChecker_11 = (org.recipnet.site.content.rncontrols.SampleChecker) _jspx_tagPool_rn_sampleChecker_invert_includeIfActionIsValid.get(org.recipnet.site.content.rncontrols.SampleChecker.class);
              _jspx_th_rn_sampleChecker_11.setPageContext(_jspx_page_context);
              _jspx_th_rn_sampleChecker_11.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_sampleChecker_11.setInvert(true);
              _jspx_th_rn_sampleChecker_11.setIncludeIfActionIsValid(
              SampleWorkflowBL.CITATION_ADDED);
              int _jspx_eval_rn_sampleChecker_11 = _jspx_th_rn_sampleChecker_11.doStartTag();
              if (_jspx_eval_rn_sampleChecker_11 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_rn_sampleChecker_11 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_rn_sampleChecker_11.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_rn_sampleChecker_11.doInitBody();
                }
                do {
                  out.write("\n            Add Citation\n          ");
                  int evalDoAfterBody = _jspx_th_rn_sampleChecker_11.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_rn_sampleChecker_11 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_rn_sampleChecker_11.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_sampleChecker_invert_includeIfActionIsValid.reuse(_jspx_th_rn_sampleChecker_11);
              out.write("\n        </td>\n      </tr>\n      <tr>\n        <td>\n          ");
              //  rn:sampleChecker
              org.recipnet.site.content.rncontrols.SampleChecker _jspx_th_rn_sampleChecker_12 = (org.recipnet.site.content.rncontrols.SampleChecker) _jspx_tagPool_rn_sampleChecker_includeIfActionIsValid.get(org.recipnet.site.content.rncontrols.SampleChecker.class);
              _jspx_th_rn_sampleChecker_12.setPageContext(_jspx_page_context);
              _jspx_th_rn_sampleChecker_12.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_sampleChecker_12.setIncludeIfActionIsValid(
              SampleWorkflowBL.FAILED_TO_COLLECT);
              int _jspx_eval_rn_sampleChecker_12 = _jspx_th_rn_sampleChecker_12.doStartTag();
              if (_jspx_eval_rn_sampleChecker_12 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_rn_sampleChecker_12 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_rn_sampleChecker_12.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_rn_sampleChecker_12.doInitBody();
                }
                do {
                  out.write("\n            ");
                  if (_jspx_meth_rn_link_15(_jspx_th_rn_sampleChecker_12, _jspx_page_context))
                    return;
                  out.write("\n          ");
                  int evalDoAfterBody = _jspx_th_rn_sampleChecker_12.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_rn_sampleChecker_12 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_rn_sampleChecker_12.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_sampleChecker_includeIfActionIsValid.reuse(_jspx_th_rn_sampleChecker_12);
              out.write("\n          ");
              //  rn:sampleChecker
              org.recipnet.site.content.rncontrols.SampleChecker _jspx_th_rn_sampleChecker_13 = (org.recipnet.site.content.rncontrols.SampleChecker) _jspx_tagPool_rn_sampleChecker_invert_includeIfActionIsValid.get(org.recipnet.site.content.rncontrols.SampleChecker.class);
              _jspx_th_rn_sampleChecker_13.setPageContext(_jspx_page_context);
              _jspx_th_rn_sampleChecker_13.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_sampleChecker_13.setInvert(true);
              _jspx_th_rn_sampleChecker_13.setIncludeIfActionIsValid(
              SampleWorkflowBL.FAILED_TO_COLLECT);
              int _jspx_eval_rn_sampleChecker_13 = _jspx_th_rn_sampleChecker_13.doStartTag();
              if (_jspx_eval_rn_sampleChecker_13 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_rn_sampleChecker_13 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_rn_sampleChecker_13.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_rn_sampleChecker_13.doInitBody();
                }
                do {
                  out.write("\n            Failed to collect\n          ");
                  int evalDoAfterBody = _jspx_th_rn_sampleChecker_13.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_rn_sampleChecker_13 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_rn_sampleChecker_13.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_sampleChecker_invert_includeIfActionIsValid.reuse(_jspx_th_rn_sampleChecker_13);
              out.write("\n        </td>\n        <td>\n          ");
              //  rn:sampleChecker
              org.recipnet.site.content.rncontrols.SampleChecker _jspx_th_rn_sampleChecker_14 = (org.recipnet.site.content.rncontrols.SampleChecker) _jspx_tagPool_rn_sampleChecker_includeIfActionIsValid.get(org.recipnet.site.content.rncontrols.SampleChecker.class);
              _jspx_th_rn_sampleChecker_14.setPageContext(_jspx_page_context);
              _jspx_th_rn_sampleChecker_14.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_sampleChecker_14.setIncludeIfActionIsValid(
              SampleWorkflowBL.DECLARED_INCOMPLETE);
              int _jspx_eval_rn_sampleChecker_14 = _jspx_th_rn_sampleChecker_14.doStartTag();
              if (_jspx_eval_rn_sampleChecker_14 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_rn_sampleChecker_14 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_rn_sampleChecker_14.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_rn_sampleChecker_14.doInitBody();
                }
                do {
                  out.write("\n            ");
                  if (_jspx_meth_rn_link_16(_jspx_th_rn_sampleChecker_14, _jspx_page_context))
                    return;
                  out.write("\n          ");
                  int evalDoAfterBody = _jspx_th_rn_sampleChecker_14.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_rn_sampleChecker_14 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_rn_sampleChecker_14.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_sampleChecker_includeIfActionIsValid.reuse(_jspx_th_rn_sampleChecker_14);
              out.write("\n          ");
              //  rn:sampleChecker
              org.recipnet.site.content.rncontrols.SampleChecker _jspx_th_rn_sampleChecker_15 = (org.recipnet.site.content.rncontrols.SampleChecker) _jspx_tagPool_rn_sampleChecker_invert_includeIfActionIsValid.get(org.recipnet.site.content.rncontrols.SampleChecker.class);
              _jspx_th_rn_sampleChecker_15.setPageContext(_jspx_page_context);
              _jspx_th_rn_sampleChecker_15.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_sampleChecker_15.setInvert(true);
              _jspx_th_rn_sampleChecker_15.setIncludeIfActionIsValid(
              SampleWorkflowBL.DECLARED_INCOMPLETE);
              int _jspx_eval_rn_sampleChecker_15 = _jspx_th_rn_sampleChecker_15.doStartTag();
              if (_jspx_eval_rn_sampleChecker_15 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_rn_sampleChecker_15 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_rn_sampleChecker_15.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_rn_sampleChecker_15.doInitBody();
                }
                do {
                  out.write("\n            Declare Incomplete\n          ");
                  int evalDoAfterBody = _jspx_th_rn_sampleChecker_15.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_rn_sampleChecker_15 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_rn_sampleChecker_15.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_sampleChecker_invert_includeIfActionIsValid.reuse(_jspx_th_rn_sampleChecker_15);
              out.write("\n        </td>\n        <td>&nbsp;</td>\n        <td class=\"leftPadded\">\n          ");
              //  rn:sampleChecker
              org.recipnet.site.content.rncontrols.SampleChecker _jspx_th_rn_sampleChecker_16 = (org.recipnet.site.content.rncontrols.SampleChecker) _jspx_tagPool_rn_sampleChecker_includeIfActionIsValid.get(org.recipnet.site.content.rncontrols.SampleChecker.class);
              _jspx_th_rn_sampleChecker_16.setPageContext(_jspx_page_context);
              _jspx_th_rn_sampleChecker_16.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_sampleChecker_16.setIncludeIfActionIsValid(
              SampleWorkflowBL.MODIFIED_LTAS);
              int _jspx_eval_rn_sampleChecker_16 = _jspx_th_rn_sampleChecker_16.doStartTag();
              if (_jspx_eval_rn_sampleChecker_16 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_rn_sampleChecker_16 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_rn_sampleChecker_16.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_rn_sampleChecker_16.doInitBody();
                }
                do {
                  out.write("\n            ");
                  if (_jspx_meth_rn_link_17(_jspx_th_rn_sampleChecker_16, _jspx_page_context))
                    return;
                  out.write("\n          ");
                  int evalDoAfterBody = _jspx_th_rn_sampleChecker_16.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_rn_sampleChecker_16 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_rn_sampleChecker_16.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_sampleChecker_includeIfActionIsValid.reuse(_jspx_th_rn_sampleChecker_16);
              out.write("\n          ");
              //  rn:sampleChecker
              org.recipnet.site.content.rncontrols.SampleChecker _jspx_th_rn_sampleChecker_17 = (org.recipnet.site.content.rncontrols.SampleChecker) _jspx_tagPool_rn_sampleChecker_invert_includeIfActionIsValid.get(org.recipnet.site.content.rncontrols.SampleChecker.class);
              _jspx_th_rn_sampleChecker_17.setPageContext(_jspx_page_context);
              _jspx_th_rn_sampleChecker_17.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_sampleChecker_17.setInvert(true);
              _jspx_th_rn_sampleChecker_17.setIncludeIfActionIsValid(
              SampleWorkflowBL.MODIFIED_LTAS);
              int _jspx_eval_rn_sampleChecker_17 = _jspx_th_rn_sampleChecker_17.doStartTag();
              if (_jspx_eval_rn_sampleChecker_17 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_rn_sampleChecker_17 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_rn_sampleChecker_17.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_rn_sampleChecker_17.doInitBody();
                }
                do {
                  out.write("\n            Edit Custom fields\n          ");
                  int evalDoAfterBody = _jspx_th_rn_sampleChecker_17.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_rn_sampleChecker_17 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_rn_sampleChecker_17.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_sampleChecker_invert_includeIfActionIsValid.reuse(_jspx_th_rn_sampleChecker_17);
              out.write("\n        </td>\n        <td class=\"leftPadded\">\n          ");
              //  rn:sampleChecker
              org.recipnet.site.content.rncontrols.SampleChecker _jspx_th_rn_sampleChecker_18 = (org.recipnet.site.content.rncontrols.SampleChecker) _jspx_tagPool_rn_sampleChecker_includeIfActionIsValid.get(org.recipnet.site.content.rncontrols.SampleChecker.class);
              _jspx_th_rn_sampleChecker_18.setPageContext(_jspx_page_context);
              _jspx_th_rn_sampleChecker_18.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_sampleChecker_18.setIncludeIfActionIsValid(
              SampleWorkflowBL.RETRACTED);
              int _jspx_eval_rn_sampleChecker_18 = _jspx_th_rn_sampleChecker_18.doStartTag();
              if (_jspx_eval_rn_sampleChecker_18 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_rn_sampleChecker_18 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_rn_sampleChecker_18.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_rn_sampleChecker_18.doInitBody();
                }
                do {
                  out.write("\n            ");
                  if (_jspx_meth_rn_link_18(_jspx_th_rn_sampleChecker_18, _jspx_page_context))
                    return;
                  out.write("\n          ");
                  int evalDoAfterBody = _jspx_th_rn_sampleChecker_18.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_rn_sampleChecker_18 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_rn_sampleChecker_18.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_sampleChecker_includeIfActionIsValid.reuse(_jspx_th_rn_sampleChecker_18);
              out.write("\n          ");
              //  rn:sampleChecker
              org.recipnet.site.content.rncontrols.SampleChecker _jspx_th_rn_sampleChecker_19 = (org.recipnet.site.content.rncontrols.SampleChecker) _jspx_tagPool_rn_sampleChecker_invert_includeIfActionIsValid.get(org.recipnet.site.content.rncontrols.SampleChecker.class);
              _jspx_th_rn_sampleChecker_19.setPageContext(_jspx_page_context);
              _jspx_th_rn_sampleChecker_19.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_sampleChecker_19.setInvert(true);
              _jspx_th_rn_sampleChecker_19.setIncludeIfActionIsValid(
              SampleWorkflowBL.RETRACTED);
              int _jspx_eval_rn_sampleChecker_19 = _jspx_th_rn_sampleChecker_19.doStartTag();
              if (_jspx_eval_rn_sampleChecker_19 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_rn_sampleChecker_19 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_rn_sampleChecker_19.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_rn_sampleChecker_19.doInitBody();
                }
                do {
                  out.write("\n            Retract\n          ");
                  int evalDoAfterBody = _jspx_th_rn_sampleChecker_19.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_rn_sampleChecker_19 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_rn_sampleChecker_19.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_sampleChecker_invert_includeIfActionIsValid.reuse(_jspx_th_rn_sampleChecker_19);
              out.write("\n        </td>\n      </tr>\n      <tr>\n        <td>\n          ");
              //  rn:sampleChecker
              org.recipnet.site.content.rncontrols.SampleChecker _jspx_th_rn_sampleChecker_20 = (org.recipnet.site.content.rncontrols.SampleChecker) _jspx_tagPool_rn_sampleChecker_includeIfActionIsValid.get(org.recipnet.site.content.rncontrols.SampleChecker.class);
              _jspx_th_rn_sampleChecker_20.setPageContext(_jspx_page_context);
              _jspx_th_rn_sampleChecker_20.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_sampleChecker_20.setIncludeIfActionIsValid(
              SampleWorkflowBL.DECLARED_NON_SCS);
              int _jspx_eval_rn_sampleChecker_20 = _jspx_th_rn_sampleChecker_20.doStartTag();
              if (_jspx_eval_rn_sampleChecker_20 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_rn_sampleChecker_20 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_rn_sampleChecker_20.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_rn_sampleChecker_20.doInitBody();
                }
                do {
                  out.write("\n            ");
                  if (_jspx_meth_rn_link_19(_jspx_th_rn_sampleChecker_20, _jspx_page_context))
                    return;
                  out.write("\n          ");
                  int evalDoAfterBody = _jspx_th_rn_sampleChecker_20.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_rn_sampleChecker_20 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_rn_sampleChecker_20.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_sampleChecker_includeIfActionIsValid.reuse(_jspx_th_rn_sampleChecker_20);
              out.write("\n          ");
              //  rn:sampleChecker
              org.recipnet.site.content.rncontrols.SampleChecker _jspx_th_rn_sampleChecker_21 = (org.recipnet.site.content.rncontrols.SampleChecker) _jspx_tagPool_rn_sampleChecker_invert_includeIfActionIsValid.get(org.recipnet.site.content.rncontrols.SampleChecker.class);
              _jspx_th_rn_sampleChecker_21.setPageContext(_jspx_page_context);
              _jspx_th_rn_sampleChecker_21.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_sampleChecker_21.setInvert(true);
              _jspx_th_rn_sampleChecker_21.setIncludeIfActionIsValid(
              SampleWorkflowBL.DECLARED_NON_SCS);
              int _jspx_eval_rn_sampleChecker_21 = _jspx_th_rn_sampleChecker_21.doStartTag();
              if (_jspx_eval_rn_sampleChecker_21 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_rn_sampleChecker_21 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_rn_sampleChecker_21.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_rn_sampleChecker_21.doInitBody();
                }
                do {
                  out.write("\n            Declare non-SCS\n          ");
                  int evalDoAfterBody = _jspx_th_rn_sampleChecker_21.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_rn_sampleChecker_21 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_rn_sampleChecker_21.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_sampleChecker_invert_includeIfActionIsValid.reuse(_jspx_th_rn_sampleChecker_21);
              out.write("\n        </td>\n        <td>\n          ");
              //  rn:sampleChecker
              org.recipnet.site.content.rncontrols.SampleChecker _jspx_th_rn_sampleChecker_22 = (org.recipnet.site.content.rncontrols.SampleChecker) _jspx_tagPool_rn_sampleChecker_includeIfActionIsValid.get(org.recipnet.site.content.rncontrols.SampleChecker.class);
              _jspx_th_rn_sampleChecker_22.setPageContext(_jspx_page_context);
              _jspx_th_rn_sampleChecker_22.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_sampleChecker_22.setIncludeIfActionIsValid(
              SampleWorkflowBL.SUSPENDED);
              int _jspx_eval_rn_sampleChecker_22 = _jspx_th_rn_sampleChecker_22.doStartTag();
              if (_jspx_eval_rn_sampleChecker_22 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_rn_sampleChecker_22 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_rn_sampleChecker_22.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_rn_sampleChecker_22.doInitBody();
                }
                do {
                  out.write("\n            ");
                  if (_jspx_meth_rn_link_20(_jspx_th_rn_sampleChecker_22, _jspx_page_context))
                    return;
                  out.write("\n          ");
                  int evalDoAfterBody = _jspx_th_rn_sampleChecker_22.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_rn_sampleChecker_22 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_rn_sampleChecker_22.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_sampleChecker_includeIfActionIsValid.reuse(_jspx_th_rn_sampleChecker_22);
              out.write("\n          ");
              //  rn:sampleChecker
              org.recipnet.site.content.rncontrols.SampleChecker _jspx_th_rn_sampleChecker_23 = (org.recipnet.site.content.rncontrols.SampleChecker) _jspx_tagPool_rn_sampleChecker_includeIfActionIsValid.get(org.recipnet.site.content.rncontrols.SampleChecker.class);
              _jspx_th_rn_sampleChecker_23.setPageContext(_jspx_page_context);
              _jspx_th_rn_sampleChecker_23.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_sampleChecker_23.setIncludeIfActionIsValid(
              SampleWorkflowBL.RESUMED);
              int _jspx_eval_rn_sampleChecker_23 = _jspx_th_rn_sampleChecker_23.doStartTag();
              if (_jspx_eval_rn_sampleChecker_23 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_rn_sampleChecker_23 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_rn_sampleChecker_23.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_rn_sampleChecker_23.doInitBody();
                }
                do {
                  out.write("\n            ");
                  if (_jspx_meth_rn_link_21(_jspx_th_rn_sampleChecker_23, _jspx_page_context))
                    return;
                  out.write("\n          ");
                  int evalDoAfterBody = _jspx_th_rn_sampleChecker_23.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_rn_sampleChecker_23 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_rn_sampleChecker_23.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_sampleChecker_includeIfActionIsValid.reuse(_jspx_th_rn_sampleChecker_23);
              out.write("\n          ");
              //  rn:sampleChecker
              org.recipnet.site.content.rncontrols.SampleChecker _jspx_th_rn_sampleChecker_24 = (org.recipnet.site.content.rncontrols.SampleChecker) _jspx_tagPool_rn_sampleChecker_invert_includeIfActionIsValid.get(org.recipnet.site.content.rncontrols.SampleChecker.class);
              _jspx_th_rn_sampleChecker_24.setPageContext(_jspx_page_context);
              _jspx_th_rn_sampleChecker_24.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_sampleChecker_24.setInvert(true);
              _jspx_th_rn_sampleChecker_24.setIncludeIfActionIsValid(
              SampleWorkflowBL.SUSPENDED);
              int _jspx_eval_rn_sampleChecker_24 = _jspx_th_rn_sampleChecker_24.doStartTag();
              if (_jspx_eval_rn_sampleChecker_24 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_rn_sampleChecker_24 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_rn_sampleChecker_24.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_rn_sampleChecker_24.doInitBody();
                }
                do {
                  out.write("\n            ");
                  //  rn:sampleChecker
                  org.recipnet.site.content.rncontrols.SampleChecker _jspx_th_rn_sampleChecker_25 = (org.recipnet.site.content.rncontrols.SampleChecker) _jspx_tagPool_rn_sampleChecker_invert_includeIfActionIsValid.get(org.recipnet.site.content.rncontrols.SampleChecker.class);
                  _jspx_th_rn_sampleChecker_25.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleChecker_25.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleChecker_24);
                  _jspx_th_rn_sampleChecker_25.setInvert(true);
                  _jspx_th_rn_sampleChecker_25.setIncludeIfActionIsValid(
                SampleWorkflowBL.RESUMED);
                  int _jspx_eval_rn_sampleChecker_25 = _jspx_th_rn_sampleChecker_25.doStartTag();
                  if (_jspx_eval_rn_sampleChecker_25 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_rn_sampleChecker_25 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_rn_sampleChecker_25.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_rn_sampleChecker_25.doInitBody();
                    }
                    do {
                      out.write("\n              Suspend\n            ");
                      int evalDoAfterBody = _jspx_th_rn_sampleChecker_25.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_rn_sampleChecker_25 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_rn_sampleChecker_25.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_sampleChecker_invert_includeIfActionIsValid.reuse(_jspx_th_rn_sampleChecker_25);
                  out.write("\n          ");
                  int evalDoAfterBody = _jspx_th_rn_sampleChecker_24.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_rn_sampleChecker_24 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_rn_sampleChecker_24.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_sampleChecker_invert_includeIfActionIsValid.reuse(_jspx_th_rn_sampleChecker_24);
              out.write("\n        </td>\n        <td>&nbsp;</td>\n        <td colspan=\"2\" class=\"leftPadded\">\n          ");
              //  rn:sampleChecker
              org.recipnet.site.content.rncontrols.SampleChecker _jspx_th_rn_sampleChecker_26 = (org.recipnet.site.content.rncontrols.SampleChecker) _jspx_tagPool_rn_sampleChecker_includeIfActionIsValid.get(org.recipnet.site.content.rncontrols.SampleChecker.class);
              _jspx_th_rn_sampleChecker_26.setPageContext(_jspx_page_context);
              _jspx_th_rn_sampleChecker_26.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_sampleChecker_26.setIncludeIfActionIsValid(
              SampleWorkflowBL.CHANGED_ACL);
              int _jspx_eval_rn_sampleChecker_26 = _jspx_th_rn_sampleChecker_26.doStartTag();
              if (_jspx_eval_rn_sampleChecker_26 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_rn_sampleChecker_26 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_rn_sampleChecker_26.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_rn_sampleChecker_26.doInitBody();
                }
                do {
                  out.write("\n            ");
                  if (_jspx_meth_rn_link_22(_jspx_th_rn_sampleChecker_26, _jspx_page_context))
                    return;
                  out.write("\n          ");
                  int evalDoAfterBody = _jspx_th_rn_sampleChecker_26.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_rn_sampleChecker_26 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_rn_sampleChecker_26.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_sampleChecker_includeIfActionIsValid.reuse(_jspx_th_rn_sampleChecker_26);
              out.write("\n        </td>\n      </tr>\n      <tr>\n        <td>\n          ");
              //  rn:sampleChecker
              org.recipnet.site.content.rncontrols.SampleChecker _jspx_th_rn_sampleChecker_27 = (org.recipnet.site.content.rncontrols.SampleChecker) _jspx_tagPool_rn_sampleChecker_includeIfActionIsValid.get(org.recipnet.site.content.rncontrols.SampleChecker.class);
              _jspx_th_rn_sampleChecker_27.setPageContext(_jspx_page_context);
              _jspx_th_rn_sampleChecker_27.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_sampleChecker_27.setIncludeIfActionIsValid(
              SampleWorkflowBL.WITHDRAWN_BY_PROVIDER);
              int _jspx_eval_rn_sampleChecker_27 = _jspx_th_rn_sampleChecker_27.doStartTag();
              if (_jspx_eval_rn_sampleChecker_27 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_rn_sampleChecker_27 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_rn_sampleChecker_27.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_rn_sampleChecker_27.doInitBody();
                }
                do {
                  out.write("\n            ");
                  if (_jspx_meth_rn_link_23(_jspx_th_rn_sampleChecker_27, _jspx_page_context))
                    return;
                  out.write("\n          ");
                  int evalDoAfterBody = _jspx_th_rn_sampleChecker_27.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_rn_sampleChecker_27 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_rn_sampleChecker_27.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_sampleChecker_includeIfActionIsValid.reuse(_jspx_th_rn_sampleChecker_27);
              out.write("\n          ");
              //  rn:sampleChecker
              org.recipnet.site.content.rncontrols.SampleChecker _jspx_th_rn_sampleChecker_28 = (org.recipnet.site.content.rncontrols.SampleChecker) _jspx_tagPool_rn_sampleChecker_invert_includeIfActionIsValid.get(org.recipnet.site.content.rncontrols.SampleChecker.class);
              _jspx_th_rn_sampleChecker_28.setPageContext(_jspx_page_context);
              _jspx_th_rn_sampleChecker_28.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_sampleChecker_28.setInvert(true);
              _jspx_th_rn_sampleChecker_28.setIncludeIfActionIsValid(
              SampleWorkflowBL.WITHDRAWN_BY_PROVIDER);
              int _jspx_eval_rn_sampleChecker_28 = _jspx_th_rn_sampleChecker_28.doStartTag();
              if (_jspx_eval_rn_sampleChecker_28 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_rn_sampleChecker_28 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_rn_sampleChecker_28.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_rn_sampleChecker_28.doInitBody();
                }
                do {
                  out.write("\n            Withdraw\n          ");
                  int evalDoAfterBody = _jspx_th_rn_sampleChecker_28.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_rn_sampleChecker_28 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_rn_sampleChecker_28.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_sampleChecker_invert_includeIfActionIsValid.reuse(_jspx_th_rn_sampleChecker_28);
              out.write("\n        </td>\n        <td>&nbsp;</td>\n        <td>&nbsp;</td>\n        <td>&nbsp;</td>\n        <td>&nbsp;</td>\n      </tr>\n    </table>\n  ");
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
          if (_jspx_meth_ctl_styleBlock_0(_jspx_th_rn_editSamplePage_0, _jspx_page_context))
            return;
          out.write('\n');
          int evalDoAfterBody = _jspx_th_rn_editSamplePage_0.doAfterBody();
          htmlPage = (org.recipnet.site.content.rncontrols.EditSamplePage) _jspx_page_context.findAttribute("htmlPage");
          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
            break;
        } while (true);
        if (_jspx_eval_rn_editSamplePage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
          out = _jspx_page_context.popBody();
      }
      if (_jspx_th_rn_editSamplePage_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
        return;
      _jspx_tagPool_rn_editSamplePage_unsetSuppressionPreferenceParamName_title_loginPageUrl_ignoreSampleHistoryId_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName.reuse(_jspx_th_rn_editSamplePage_0);
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

  private boolean _jspx_meth_ctl_img_0(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_errorMessage_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:img
    org.recipnet.common.controls.ImageHtmlElement _jspx_th_ctl_img_0 = (org.recipnet.common.controls.ImageHtmlElement) _jspx_tagPool_ctl_img_styleClass_src_nobody.get(org.recipnet.common.controls.ImageHtmlElement.class);
    _jspx_th_ctl_img_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_img_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_errorMessage_0);
    _jspx_th_ctl_img_0.setSrc("/images/error.gif");
    _jspx_th_ctl_img_0.setStyleClass("icon");
    int _jspx_eval_ctl_img_0 = _jspx_th_ctl_img_0.doStartTag();
    if (_jspx_th_ctl_img_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_img_styleClass_src_nobody.reuse(_jspx_th_ctl_img_0);
    return false;
  }

  private boolean _jspx_meth_ctl_img_1(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_selfForm_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:img
    org.recipnet.common.controls.ImageHtmlElement _jspx_th_ctl_img_1 = (org.recipnet.common.controls.ImageHtmlElement) _jspx_tagPool_ctl_img_styleClass_src_nobody.get(org.recipnet.common.controls.ImageHtmlElement.class);
    _jspx_th_ctl_img_1.setPageContext(_jspx_page_context);
    _jspx_th_ctl_img_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
    _jspx_th_ctl_img_1.setSrc("/images/basicinfo.gif");
    _jspx_th_ctl_img_1.setStyleClass("icon");
    int _jspx_eval_ctl_img_1 = _jspx_th_ctl_img_1.doStartTag();
    if (_jspx_th_ctl_img_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_img_styleClass_src_nobody.reuse(_jspx_th_ctl_img_1);
    return false;
  }

  private boolean _jspx_meth_rn_labField_0(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_selfForm_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:labField
    org.recipnet.site.content.rncontrols.LabField _jspx_th_rn_labField_0 = (org.recipnet.site.content.rncontrols.LabField) _jspx_tagPool_rn_labField_nobody.get(org.recipnet.site.content.rncontrols.LabField.class);
    _jspx_th_rn_labField_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_labField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
    int _jspx_eval_rn_labField_0 = _jspx_th_rn_labField_0.doStartTag();
    if (_jspx_th_rn_labField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_labField_nobody.reuse(_jspx_th_rn_labField_0);
    return false;
  }

  private boolean _jspx_meth_rn_providerField_0(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_selfForm_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:providerField
    org.recipnet.site.content.rncontrols.ProviderField _jspx_th_rn_providerField_0 = (org.recipnet.site.content.rncontrols.ProviderField) _jspx_tagPool_rn_providerField_nobody.get(org.recipnet.site.content.rncontrols.ProviderField.class);
    _jspx_th_rn_providerField_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_providerField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
    int _jspx_eval_rn_providerField_0 = _jspx_th_rn_providerField_0.doStartTag();
    if (_jspx_th_rn_providerField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_providerField_nobody.reuse(_jspx_th_rn_providerField_0);
    return false;
  }

  private boolean _jspx_meth_ctl_img_2(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_selfForm_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:img
    org.recipnet.common.controls.ImageHtmlElement _jspx_th_ctl_img_2 = (org.recipnet.common.controls.ImageHtmlElement) _jspx_tagPool_ctl_img_styleClass_src_nobody.get(org.recipnet.common.controls.ImageHtmlElement.class);
    _jspx_th_ctl_img_2.setPageContext(_jspx_page_context);
    _jspx_th_ctl_img_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
    _jspx_th_ctl_img_2.setSrc("/images/otherviews.gif");
    _jspx_th_ctl_img_2.setStyleClass("icon");
    int _jspx_eval_ctl_img_2 = _jspx_th_ctl_img_2.doStartTag();
    if (_jspx_th_ctl_img_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_img_styleClass_src_nobody.reuse(_jspx_th_ctl_img_2);
    return false;
  }

  private boolean _jspx_meth_rn_link_0(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_selfForm_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:link
    org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_0 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_href.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
    _jspx_th_rn_link_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_link_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
    _jspx_th_rn_link_0.setHref("/lab/samplehistory.jsp");
    int _jspx_eval_rn_link_0 = _jspx_th_rn_link_0.doStartTag();
    if (_jspx_eval_rn_link_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_link_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_link_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_link_0.doInitBody();
      }
      do {
        out.write("Sample History");
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

  private boolean _jspx_meth_rn_showsampleLink_0(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_selfForm_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:showsampleLink
    org.recipnet.site.content.rncontrols.ShowsampleLink _jspx_th_rn_showsampleLink_0 = (org.recipnet.site.content.rncontrols.ShowsampleLink) _jspx_tagPool_rn_showsampleLink_sampleIsKnownToBeLocal_detailedPageUrl_basicPageUrl.get(org.recipnet.site.content.rncontrols.ShowsampleLink.class);
    _jspx_th_rn_showsampleLink_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_showsampleLink_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
    _jspx_th_rn_showsampleLink_0.setBasicPageUrl("/showsamplebasic.jsp");
    _jspx_th_rn_showsampleLink_0.setDetailedPageUrl("/showsampledetailed.jsp");
    _jspx_th_rn_showsampleLink_0.setSampleIsKnownToBeLocal(true);
    int _jspx_eval_rn_showsampleLink_0 = _jspx_th_rn_showsampleLink_0.doStartTag();
    if (_jspx_eval_rn_showsampleLink_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_showsampleLink_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_showsampleLink_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_showsampleLink_0.doInitBody();
      }
      do {
        out.write("\n                  ");
        if (_jspx_meth_rn_sampleParam_0(_jspx_th_rn_showsampleLink_0, _jspx_page_context))
          return true;
        out.write("\n                  Sample detail\n                ");
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

  private boolean _jspx_meth_rn_link_1(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_selfForm_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:link
    org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_1 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_href.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
    _jspx_th_rn_link_1.setPageContext(_jspx_page_context);
    _jspx_th_rn_link_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
    _jspx_th_rn_link_1.setHref("/jamm.jsp");
    int _jspx_eval_rn_link_1 = _jspx_th_rn_link_1.doStartTag();
    if (_jspx_eval_rn_link_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_link_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_link_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_link_1.doInitBody();
      }
      do {
        out.write("JaMM Visualization");
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

  private boolean _jspx_meth_ctl_img_3(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_selfForm_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:img
    org.recipnet.common.controls.ImageHtmlElement _jspx_th_ctl_img_3 = (org.recipnet.common.controls.ImageHtmlElement) _jspx_tagPool_ctl_img_styleClass_src_nobody.get(org.recipnet.common.controls.ImageHtmlElement.class);
    _jspx_th_ctl_img_3.setPageContext(_jspx_page_context);
    _jspx_th_ctl_img_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
    _jspx_th_ctl_img_3.setSrc("/images/status.gif");
    _jspx_th_ctl_img_3.setStyleClass("icon");
    int _jspx_eval_ctl_img_3 = _jspx_th_ctl_img_3.doStartTag();
    if (_jspx_th_ctl_img_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_img_styleClass_src_nobody.reuse(_jspx_th_ctl_img_3);
    return false;
  }

  private boolean _jspx_meth_rn_statusDisplay_0(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_selfForm_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:statusDisplay
    org.recipnet.site.content.rncontrols.StatusDisplay _jspx_th_rn_statusDisplay_0 = (org.recipnet.site.content.rncontrols.StatusDisplay) _jspx_tagPool_rn_statusDisplay_unselectedStatusPatternHtml_selectedStatusPatternHtml_nobody.get(org.recipnet.site.content.rncontrols.StatusDisplay.class);
    _jspx_th_rn_statusDisplay_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_statusDisplay_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
    _jspx_th_rn_statusDisplay_0.setUnselectedStatusPatternHtml("<li class='otherStatus'>[statusName]</li>");
    _jspx_th_rn_statusDisplay_0.setSelectedStatusPatternHtml("<li class='selectedStatus'>[statusName]</li>");
    int _jspx_eval_rn_statusDisplay_0 = _jspx_th_rn_statusDisplay_0.doStartTag();
    if (_jspx_th_rn_statusDisplay_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_statusDisplay_unselectedStatusPatternHtml_selectedStatusPatternHtml_nobody.reuse(_jspx_th_rn_statusDisplay_0);
    return false;
  }

  private boolean _jspx_meth_ctl_img_4(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_selfForm_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:img
    org.recipnet.common.controls.ImageHtmlElement _jspx_th_ctl_img_4 = (org.recipnet.common.controls.ImageHtmlElement) _jspx_tagPool_ctl_img_styleClass_src_nobody.get(org.recipnet.common.controls.ImageHtmlElement.class);
    _jspx_th_ctl_img_4.setPageContext(_jspx_page_context);
    _jspx_th_ctl_img_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
    _jspx_th_ctl_img_4.setSrc("/images/managefiles.gif");
    _jspx_th_ctl_img_4.setStyleClass("icon");
    int _jspx_eval_ctl_img_4 = _jspx_th_ctl_img_4.doStartTag();
    if (_jspx_th_ctl_img_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_img_styleClass_src_nobody.reuse(_jspx_th_ctl_img_4);
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
        out.write("\n                <ul>\n              ");
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
    org.recipnet.common.controls.ParityChecker _jspx_th_ctl_parityChecker_1 = (org.recipnet.common.controls.ParityChecker) _jspx_tagPool_ctl_parityChecker_includeOnlyOnLast.get(org.recipnet.common.controls.ParityChecker.class);
    _jspx_th_ctl_parityChecker_1.setPageContext(_jspx_page_context);
    _jspx_th_ctl_parityChecker_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_fileIterator_0);
    _jspx_th_ctl_parityChecker_1.setIncludeOnlyOnLast(true);
    int _jspx_eval_ctl_parityChecker_1 = _jspx_th_ctl_parityChecker_1.doStartTag();
    if (_jspx_eval_ctl_parityChecker_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_parityChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_parityChecker_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_parityChecker_1.doInitBody();
      }
      do {
        out.write("\n                  </ul>\n                ");
        int evalDoAfterBody = _jspx_th_ctl_parityChecker_1.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_parityChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_parityChecker_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_parityChecker_includeOnlyOnLast.reuse(_jspx_th_ctl_parityChecker_1);
    return false;
  }

  private boolean _jspx_meth_rn_createDataDirectoryButton_0(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_errorMessage_5, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:createDataDirectoryButton
    org.recipnet.site.content.rncontrols.CreateDataDirectoryButton _jspx_th_rn_createDataDirectoryButton_0 = (org.recipnet.site.content.rncontrols.CreateDataDirectoryButton) _jspx_tagPool_rn_createDataDirectoryButton_label_nobody.get(org.recipnet.site.content.rncontrols.CreateDataDirectoryButton.class);
    _jspx_th_rn_createDataDirectoryButton_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_createDataDirectoryButton_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_errorMessage_5);
    _jspx_th_rn_createDataDirectoryButton_0.setLabel("Create");
    int _jspx_eval_rn_createDataDirectoryButton_0 = _jspx_th_rn_createDataDirectoryButton_0.doStartTag();
    if (_jspx_th_rn_createDataDirectoryButton_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_createDataDirectoryButton_label_nobody.reuse(_jspx_th_rn_createDataDirectoryButton_0);
    return false;
  }

  private boolean _jspx_meth_rn_uploadFilesButton_0(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_errorMessage_7, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:uploadFilesButton
    org.recipnet.site.content.rncontrols.UploadFilesButton _jspx_th_rn_uploadFilesButton_0 = (org.recipnet.site.content.rncontrols.UploadFilesButton) _jspx_tagPool_rn_uploadFilesButton_label_formBasedPageUrl_dragAndDropPageUrl_nobody.get(org.recipnet.site.content.rncontrols.UploadFilesButton.class);
    _jspx_th_rn_uploadFilesButton_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_uploadFilesButton_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_errorMessage_7);
    _jspx_th_rn_uploadFilesButton_0.setFormBasedPageUrl("/lab/uploadfilesform.jsp");
    _jspx_th_rn_uploadFilesButton_0.setDragAndDropPageUrl("/lab/uploadfilesapplet.jsp");
    _jspx_th_rn_uploadFilesButton_0.setLabel("Upload files...");
    int _jspx_eval_rn_uploadFilesButton_0 = _jspx_th_rn_uploadFilesButton_0.doStartTag();
    if (_jspx_th_rn_uploadFilesButton_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_uploadFilesButton_label_formBasedPageUrl_dragAndDropPageUrl_nobody.reuse(_jspx_th_rn_uploadFilesButton_0);
    return false;
  }

  private boolean _jspx_meth_rn_buttonLink_0(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_errorMessage_8, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:buttonLink
    org.recipnet.site.content.rncontrols.ContextPreservingButton _jspx_th_rn_buttonLink_0 = (org.recipnet.site.content.rncontrols.ContextPreservingButton) _jspx_tagPool_rn_buttonLink_target_label_nobody.get(org.recipnet.site.content.rncontrols.ContextPreservingButton.class);
    _jspx_th_rn_buttonLink_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_buttonLink_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_errorMessage_8);
    _jspx_th_rn_buttonLink_0.setTarget("/lab/generatefiles.jsp");
    _jspx_th_rn_buttonLink_0.setLabel("Generate files...");
    int _jspx_eval_rn_buttonLink_0 = _jspx_th_rn_buttonLink_0.doStartTag();
    if (_jspx_th_rn_buttonLink_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_buttonLink_target_label_nobody.reuse(_jspx_th_rn_buttonLink_0);
    return false;
  }

  private boolean _jspx_meth_rn_buttonLink_1(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_errorMessage_8, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:buttonLink
    org.recipnet.site.content.rncontrols.ContextPreservingButton _jspx_th_rn_buttonLink_1 = (org.recipnet.site.content.rncontrols.ContextPreservingButton) _jspx_tagPool_rn_buttonLink_target_label_nobody.get(org.recipnet.site.content.rncontrols.ContextPreservingButton.class);
    _jspx_th_rn_buttonLink_1.setPageContext(_jspx_page_context);
    _jspx_th_rn_buttonLink_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_errorMessage_8);
    _jspx_th_rn_buttonLink_1.setTarget("/lab/managefiles.jsp");
    _jspx_th_rn_buttonLink_1.setLabel("Manage files...");
    int _jspx_eval_rn_buttonLink_1 = _jspx_th_rn_buttonLink_1.doStartTag();
    if (_jspx_th_rn_buttonLink_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_buttonLink_target_label_nobody.reuse(_jspx_th_rn_buttonLink_1);
    return false;
  }

  private boolean _jspx_meth_ctl_browserSpecific_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_preferenceChecker_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:browserSpecific
    org.recipnet.common.controls.BrowserSpecificElement _jspx_th_ctl_browserSpecific_0 = (org.recipnet.common.controls.BrowserSpecificElement) _jspx_tagPool_ctl_browserSpecific_notNetscape4x.get(org.recipnet.common.controls.BrowserSpecificElement.class);
    _jspx_th_ctl_browserSpecific_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_browserSpecific_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_preferenceChecker_1);
    _jspx_th_ctl_browserSpecific_0.setNotNetscape4x(true);
    int _jspx_eval_ctl_browserSpecific_0 = _jspx_th_ctl_browserSpecific_0.doStartTag();
    if (_jspx_eval_ctl_browserSpecific_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_browserSpecific_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_browserSpecific_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_browserSpecific_0.doInitBody();
      }
      do {
        out.write("\n              <tr>\n                <td rowspan=\"3\" bgcolor=\"#FFFFFF\" valign=\"top\" align=\"right\">\n                ");
        if (_jspx_meth_ctl_img_5(_jspx_th_ctl_browserSpecific_0, _jspx_page_context))
          return true;
        out.write("                </td>\n                <td colspan=\"3\">&nbsp;</td>\n              </tr>\n            ");
        int evalDoAfterBody = _jspx_th_ctl_browserSpecific_0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_browserSpecific_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_browserSpecific_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_browserSpecific_notNetscape4x.reuse(_jspx_th_ctl_browserSpecific_0);
    return false;
  }

  private boolean _jspx_meth_ctl_img_5(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_browserSpecific_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:img
    org.recipnet.common.controls.ImageHtmlElement _jspx_th_ctl_img_5 = (org.recipnet.common.controls.ImageHtmlElement) _jspx_tagPool_ctl_img_styleClass_src_nobody.get(org.recipnet.common.controls.ImageHtmlElement.class);
    _jspx_th_ctl_img_5.setPageContext(_jspx_page_context);
    _jspx_th_ctl_img_5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_browserSpecific_0);
    _jspx_th_ctl_img_5.setSrc("/images/grayarch.gif");
    _jspx_th_ctl_img_5.setStyleClass("grayArcImage");
    int _jspx_eval_ctl_img_5 = _jspx_th_ctl_img_5.doStartTag();
    if (_jspx_th_ctl_img_5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_img_styleClass_src_nobody.reuse(_jspx_th_ctl_img_5);
    return false;
  }

  private boolean _jspx_meth_rn_storePreferencesButton_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_preferenceChecker_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:storePreferencesButton
    org.recipnet.site.content.rncontrols.StorePreferencesButton _jspx_th_rn_storePreferencesButton_0 = (org.recipnet.site.content.rncontrols.StorePreferencesButton) _jspx_tagPool_rn_storePreferencesButton_label_nobody.get(org.recipnet.site.content.rncontrols.StorePreferencesButton.class);
    _jspx_th_rn_storePreferencesButton_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_storePreferencesButton_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_preferenceChecker_1);
    _jspx_th_rn_storePreferencesButton_0.setLabel("Apply");
    int _jspx_eval_rn_storePreferencesButton_0 = _jspx_th_rn_storePreferencesButton_0.doStartTag();
    if (_jspx_th_rn_storePreferencesButton_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_storePreferencesButton_label_nobody.reuse(_jspx_th_rn_storePreferencesButton_0);
    return false;
  }

  private boolean _jspx_meth_ctl_browserSpecific_1(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_preferenceChecker_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:browserSpecific
    org.recipnet.common.controls.BrowserSpecificElement _jspx_th_ctl_browserSpecific_1 = (org.recipnet.common.controls.BrowserSpecificElement) _jspx_tagPool_ctl_browserSpecific_notNetscape4x.get(org.recipnet.common.controls.BrowserSpecificElement.class);
    _jspx_th_ctl_browserSpecific_1.setPageContext(_jspx_page_context);
    _jspx_th_ctl_browserSpecific_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_preferenceChecker_1);
    _jspx_th_ctl_browserSpecific_1.setNotNetscape4x(true);
    int _jspx_eval_ctl_browserSpecific_1 = _jspx_th_ctl_browserSpecific_1.doStartTag();
    if (_jspx_eval_ctl_browserSpecific_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_browserSpecific_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_browserSpecific_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_browserSpecific_1.doInitBody();
      }
      do {
        out.write("\n              <tr>\n                <td colspan=\"3\">");
        if (_jspx_meth_ctl_img_6(_jspx_th_ctl_browserSpecific_1, _jspx_page_context))
          return true;
        out.write("</td>\n              </tr>\n            ");
        int evalDoAfterBody = _jspx_th_ctl_browserSpecific_1.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_browserSpecific_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_browserSpecific_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_browserSpecific_notNetscape4x.reuse(_jspx_th_ctl_browserSpecific_1);
    return false;
  }

  private boolean _jspx_meth_ctl_img_6(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_browserSpecific_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:img
    org.recipnet.common.controls.ImageHtmlElement _jspx_th_ctl_img_6 = (org.recipnet.common.controls.ImageHtmlElement) _jspx_tagPool_ctl_img_styleClass_src_nobody.get(org.recipnet.common.controls.ImageHtmlElement.class);
    _jspx_th_ctl_img_6.setPageContext(_jspx_page_context);
    _jspx_th_ctl_img_6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_browserSpecific_1);
    _jspx_th_ctl_img_6.setSrc("/images/graycurve.gif");
    _jspx_th_ctl_img_6.setStyleClass("grayCurveImage");
    int _jspx_eval_ctl_img_6 = _jspx_th_ctl_img_6.doStartTag();
    if (_jspx_th_ctl_img_6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_img_styleClass_src_nobody.reuse(_jspx_th_ctl_img_6);
    return false;
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
        out.write("\n          ");
        org.recipnet.site.wrapper.StringBean closeBody = null;
        synchronized (_jspx_page_context) {
          closeBody = (org.recipnet.site.wrapper.StringBean) _jspx_page_context.getAttribute("closeBody", PageContext.PAGE_SCOPE);
          if (closeBody == null){
            closeBody = new org.recipnet.site.wrapper.StringBean();
            _jspx_page_context.setAttribute("closeBody", closeBody, PageContext.PAGE_SCOPE);
          }
        }
        out.write("\n        ");
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

  private boolean _jspx_meth_rn_actionIcon_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleActionIterator_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:actionIcon
    org.recipnet.site.content.rncontrols.ActionIcon _jspx_th_rn_actionIcon_0 = (org.recipnet.site.content.rncontrols.ActionIcon) _jspx_tagPool_rn_actionIcon_styleClass_nobody.get(org.recipnet.site.content.rncontrols.ActionIcon.class);
    _jspx_th_rn_actionIcon_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_actionIcon_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleActionIterator_0);
    _jspx_th_rn_actionIcon_0.setStyleClass("icon");
    int _jspx_eval_rn_actionIcon_0 = _jspx_th_rn_actionIcon_0.doStartTag();
    if (_jspx_th_rn_actionIcon_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_actionIcon_styleClass_nobody.reuse(_jspx_th_rn_actionIcon_0);
    return false;
  }

  private boolean _jspx_meth_rn_correctionCount_0(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_errorMessage_10, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:correctionCount
    org.recipnet.site.content.rncontrols.SampleActionCorrectionCount _jspx_th_rn_correctionCount_0 = (org.recipnet.site.content.rncontrols.SampleActionCorrectionCount) _jspx_tagPool_rn_correctionCount_nobody.get(org.recipnet.site.content.rncontrols.SampleActionCorrectionCount.class);
    _jspx_th_rn_correctionCount_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_correctionCount_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_errorMessage_10);
    int _jspx_eval_rn_correctionCount_0 = _jspx_th_rn_correctionCount_0.doStartTag();
    if (_jspx_th_rn_correctionCount_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_correctionCount_nobody.reuse(_jspx_th_rn_correctionCount_0);
    return false;
  }

  private boolean _jspx_meth_ctl_parityChecker_2(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleActionFieldIterator_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    HttpServletRequest request = (HttpServletRequest)_jspx_page_context.getRequest();
    //  ctl:parityChecker
    org.recipnet.common.controls.ParityChecker _jspx_th_ctl_parityChecker_2 = (org.recipnet.common.controls.ParityChecker) _jspx_tagPool_ctl_parityChecker_includeOnlyOnFirst.get(org.recipnet.common.controls.ParityChecker.class);
    _jspx_th_ctl_parityChecker_2.setPageContext(_jspx_page_context);
    _jspx_th_ctl_parityChecker_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleActionFieldIterator_0);
    _jspx_th_ctl_parityChecker_2.setIncludeOnlyOnFirst(true);
    int _jspx_eval_ctl_parityChecker_2 = _jspx_th_ctl_parityChecker_2.doStartTag();
    if (_jspx_eval_ctl_parityChecker_2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_parityChecker_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_parityChecker_2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_parityChecker_2.doInitBody();
      }
      do {
        out.write("\n                  <div class=\"actionBoxBody\">\n                    <table>\n                    <tr>\n                    <td style=\"width: 100%;\">\n                    <table cellspacing=\"0\">\n                  ");
        if (_jspx_meth_ctl_phaseEvent_1(_jspx_th_ctl_parityChecker_2, _jspx_page_context))
          return true;
        out.write("\n                ");
        int evalDoAfterBody = _jspx_th_ctl_parityChecker_2.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_parityChecker_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_parityChecker_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_parityChecker_includeOnlyOnFirst.reuse(_jspx_th_ctl_parityChecker_2);
    return false;
  }

  private boolean _jspx_meth_ctl_phaseEvent_1(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_parityChecker_2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    HttpServletRequest request = (HttpServletRequest)_jspx_page_context.getRequest();
    //  ctl:phaseEvent
    org.recipnet.common.controls.HtmlPagePhaseEvent _jspx_th_ctl_phaseEvent_1 = (org.recipnet.common.controls.HtmlPagePhaseEvent) _jspx_tagPool_ctl_phaseEvent_skipIfSuppressed_onPhases.get(org.recipnet.common.controls.HtmlPagePhaseEvent.class);
    _jspx_th_ctl_phaseEvent_1.setPageContext(_jspx_page_context);
    _jspx_th_ctl_phaseEvent_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_parityChecker_2);
    _jspx_th_ctl_phaseEvent_1.setOnPhases("fetching,rendering");
    _jspx_th_ctl_phaseEvent_1.setSkipIfSuppressed(true);
    int _jspx_eval_ctl_phaseEvent_1 = _jspx_th_ctl_phaseEvent_1.doStartTag();
    if (_jspx_eval_ctl_phaseEvent_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_phaseEvent_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_phaseEvent_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_phaseEvent_1.doInitBody();
      }
      do {
        out.write("\n                    ");
        org.apache.jasper.runtime.JspRuntimeLibrary.introspecthelper(_jspx_page_context.findAttribute("closeBody"), "string", "</div>", null, null, false);
        out.write("\n                  ");
        int evalDoAfterBody = _jspx_th_ctl_phaseEvent_1.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_phaseEvent_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_phaseEvent_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_phaseEvent_skipIfSuppressed_onPhases.reuse(_jspx_th_ctl_phaseEvent_1);
    return false;
  }

  private boolean _jspx_meth_rn_sampleFieldLabel_1(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleActionFieldIterator_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:sampleFieldLabel
    org.recipnet.site.content.rncontrols.SampleFieldLabel _jspx_th_rn_sampleFieldLabel_1 = (org.recipnet.site.content.rncontrols.SampleFieldLabel) _jspx_tagPool_rn_sampleFieldLabel_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldLabel.class);
    _jspx_th_rn_sampleFieldLabel_1.setPageContext(_jspx_page_context);
    _jspx_th_rn_sampleFieldLabel_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleActionFieldIterator_0);
    int _jspx_eval_rn_sampleFieldLabel_1 = _jspx_th_rn_sampleFieldLabel_1.doStartTag();
    if (_jspx_th_rn_sampleFieldLabel_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_sampleFieldLabel_nobody.reuse(_jspx_th_rn_sampleFieldLabel_1);
    return false;
  }

  private boolean _jspx_meth_rn_sampleField_2(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleChecker_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:sampleField
    org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_2 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_displayValueOnly_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
    _jspx_th_rn_sampleField_2.setPageContext(_jspx_page_context);
    _jspx_th_rn_sampleField_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleChecker_0);
    _jspx_th_rn_sampleField_2.setDisplayValueOnly(true);
    int _jspx_eval_rn_sampleField_2 = _jspx_th_rn_sampleField_2.doStartTag();
    if (_jspx_th_rn_sampleField_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_sampleField_displayValueOnly_nobody.reuse(_jspx_th_rn_sampleField_2);
    return false;
  }

  private boolean _jspx_meth_rn_sampleFieldUnits_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleChecker_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:sampleFieldUnits
    org.recipnet.site.content.rncontrols.SampleFieldUnits _jspx_th_rn_sampleFieldUnits_0 = (org.recipnet.site.content.rncontrols.SampleFieldUnits) _jspx_tagPool_rn_sampleFieldUnits_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldUnits.class);
    _jspx_th_rn_sampleFieldUnits_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_sampleFieldUnits_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleChecker_0);
    int _jspx_eval_rn_sampleFieldUnits_0 = _jspx_th_rn_sampleFieldUnits_0.doStartTag();
    if (_jspx_th_rn_sampleFieldUnits_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_sampleFieldUnits_nobody.reuse(_jspx_th_rn_sampleFieldUnits_0);
    return false;
  }

  private boolean _jspx_meth_rn_sampleParam_1(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_showsampleLink_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:sampleParam
    org.recipnet.site.content.rncontrols.SampleParam _jspx_th_rn_sampleParam_1 = (org.recipnet.site.content.rncontrols.SampleParam) _jspx_tagPool_rn_sampleParam_name_nobody.get(org.recipnet.site.content.rncontrols.SampleParam.class);
    _jspx_th_rn_sampleParam_1.setPageContext(_jspx_page_context);
    _jspx_th_rn_sampleParam_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_showsampleLink_1);
    _jspx_th_rn_sampleParam_1.setName("sampleId");
    int _jspx_eval_rn_sampleParam_1 = _jspx_th_rn_sampleParam_1.doStartTag();
    if (_jspx_th_rn_sampleParam_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_sampleParam_name_nobody.reuse(_jspx_th_rn_sampleParam_1);
    return false;
  }

  private boolean _jspx_meth_rn_sampleChecker_1(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleActionFieldIterator_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:sampleChecker
    org.recipnet.site.content.rncontrols.SampleChecker _jspx_th_rn_sampleChecker_1 = (org.recipnet.site.content.rncontrols.SampleChecker) _jspx_tagPool_rn_sampleChecker_invert_includeIfSampleFieldContextProvidesValue.get(org.recipnet.site.content.rncontrols.SampleChecker.class);
    _jspx_th_rn_sampleChecker_1.setPageContext(_jspx_page_context);
    _jspx_th_rn_sampleChecker_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleActionFieldIterator_0);
    _jspx_th_rn_sampleChecker_1.setInvert(true);
    _jspx_th_rn_sampleChecker_1.setIncludeIfSampleFieldContextProvidesValue(true);
    int _jspx_eval_rn_sampleChecker_1 = _jspx_th_rn_sampleChecker_1.doStartTag();
    if (_jspx_eval_rn_sampleChecker_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_sampleChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_sampleChecker_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_sampleChecker_1.doInitBody();
      }
      do {
        out.write("\n                        <span class=\"light\">(no value)</span>\n                      ");
        int evalDoAfterBody = _jspx_th_rn_sampleChecker_1.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_sampleChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_sampleChecker_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_sampleChecker_invert_includeIfSampleFieldContextProvidesValue.reuse(_jspx_th_rn_sampleChecker_1);
    return false;
  }

  private boolean _jspx_meth_ctl_phaseEvent_2(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_errorChecker_3, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    HttpServletRequest request = (HttpServletRequest)_jspx_page_context.getRequest();
    //  ctl:phaseEvent
    org.recipnet.common.controls.HtmlPagePhaseEvent _jspx_th_ctl_phaseEvent_2 = (org.recipnet.common.controls.HtmlPagePhaseEvent) _jspx_tagPool_ctl_phaseEvent_skipIfSuppressed_onPhases.get(org.recipnet.common.controls.HtmlPagePhaseEvent.class);
    _jspx_th_ctl_phaseEvent_2.setPageContext(_jspx_page_context);
    _jspx_th_ctl_phaseEvent_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_errorChecker_3);
    _jspx_th_ctl_phaseEvent_2.setOnPhases("fetching,rendering");
    _jspx_th_ctl_phaseEvent_2.setSkipIfSuppressed(true);
    int _jspx_eval_ctl_phaseEvent_2 = _jspx_th_ctl_phaseEvent_2.doStartTag();
    if (_jspx_eval_ctl_phaseEvent_2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_phaseEvent_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_phaseEvent_2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_phaseEvent_2.doInitBody();
      }
      do {
        out.write("\n                      ");
        org.apache.jasper.runtime.JspRuntimeLibrary.introspecthelper(_jspx_page_context.findAttribute("closeBody"), "string", "</div>", null, null, false);
        out.write("\n                    ");
        int evalDoAfterBody = _jspx_th_ctl_phaseEvent_2.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_phaseEvent_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_phaseEvent_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_phaseEvent_skipIfSuppressed_onPhases.reuse(_jspx_th_ctl_phaseEvent_2);
    return false;
  }

  private boolean _jspx_meth_rn_suppressedFieldCount_0(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_errorMessage_11, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:suppressedFieldCount
    org.recipnet.site.content.rncontrols.SuppressedFieldCount _jspx_th_rn_suppressedFieldCount_0 = (org.recipnet.site.content.rncontrols.SuppressedFieldCount) _jspx_tagPool_rn_suppressedFieldCount_fieldIterator_nobody.get(org.recipnet.site.content.rncontrols.SuppressedFieldCount.class);
    _jspx_th_rn_suppressedFieldCount_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_suppressedFieldCount_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_errorMessage_11);
    _jspx_th_rn_suppressedFieldCount_0.setFieldIterator((org.recipnet.site.content.rncontrols.SampleActionFieldIterator) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${fieldIt}", org.recipnet.site.content.rncontrols.SampleActionFieldIterator.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_rn_suppressedFieldCount_0 = _jspx_th_rn_suppressedFieldCount_0.doStartTag();
    if (_jspx_th_rn_suppressedFieldCount_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_suppressedFieldCount_fieldIterator_nobody.reuse(_jspx_th_rn_suppressedFieldCount_0);
    return false;
  }

  private boolean _jspx_meth_ctl_if_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleActionIterator_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:if
    org.recipnet.common.controls.SimpleChecker _jspx_th_ctl_if_0 = (org.recipnet.common.controls.SimpleChecker) _jspx_tagPool_ctl_if_invert_conditionMet.get(org.recipnet.common.controls.SimpleChecker.class);
    _jspx_th_ctl_if_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_if_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleActionIterator_0);
    _jspx_th_ctl_if_0.setConditionMet(((java.lang.Boolean) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${empty closeBody.string}", java.lang.Boolean.class, (PageContext)_jspx_page_context, null, false)).booleanValue());
    _jspx_th_ctl_if_0.setInvert(true);
    int _jspx_eval_ctl_if_0 = _jspx_th_ctl_if_0.doStartTag();
    if (_jspx_eval_ctl_if_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_if_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_if_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_if_0.doInitBody();
      }
      do {
        out.write("\n                <td style=\"vertical-align: bottom;\">\n                ");
        if (_jspx_meth_rn_correctActionButton_0(_jspx_th_ctl_if_0, _jspx_page_context))
          return true;
        out.write("\n                </td>\n                </tr>\n                </table>\n              ");
        out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${closeBody.string}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
        out.write("\n              ");
        int evalDoAfterBody = _jspx_th_ctl_if_0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_if_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_if_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_if_invert_conditionMet.reuse(_jspx_th_ctl_if_0);
    return false;
  }

  private boolean _jspx_meth_rn_correctActionButton_0(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_if_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:correctActionButton
    org.recipnet.site.content.rncontrols.CorrectActionButton _jspx_th_rn_correctActionButton_0 = (org.recipnet.site.content.rncontrols.CorrectActionButton) _jspx_tagPool_rn_correctActionButton_withdrawnByProviderCorrectionPageUrl_suspendedCorrectionPageUrl_submittedCorrectionPageUrl_structureRefinedCorrectionPageUrl_resumedCorrectionPageUrl_releasedToPublicCorrectionPageUrl_preliminaryDataCollectedCorrectionPageUrl_label_failedToCollectCorrectionPageUrl_declaredNonScsCorrectionPageUrl_declaredIncompleteCorrectionPageUrl_citationCorrectionPageUrl_nobody.get(org.recipnet.site.content.rncontrols.CorrectActionButton.class);
    _jspx_th_rn_correctActionButton_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_correctActionButton_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_if_0);
    _jspx_th_rn_correctActionButton_0.setLabel("Revise...");
    _jspx_th_rn_correctActionButton_0.setSubmittedCorrectionPageUrl("/lab/submit.jsp");
    _jspx_th_rn_correctActionButton_0.setPreliminaryDataCollectedCorrectionPageUrl("/lab/collectpreliminarydata.jsp");
    _jspx_th_rn_correctActionButton_0.setStructureRefinedCorrectionPageUrl("/lab/refinestructure.jsp");
    _jspx_th_rn_correctActionButton_0.setReleasedToPublicCorrectionPageUrl("/lab/releasetopublic.jsp");
    _jspx_th_rn_correctActionButton_0.setDeclaredIncompleteCorrectionPageUrl("/lab/declareincomplete.jsp");
    _jspx_th_rn_correctActionButton_0.setDeclaredNonScsCorrectionPageUrl("/lab/declarenonscs.jsp");
    _jspx_th_rn_correctActionButton_0.setFailedToCollectCorrectionPageUrl("/lab/failedtocollect.jsp");
    _jspx_th_rn_correctActionButton_0.setWithdrawnByProviderCorrectionPageUrl("/lab/withdraw.jsp");
    _jspx_th_rn_correctActionButton_0.setCitationCorrectionPageUrl("/lab/addcitation.jsp");
    _jspx_th_rn_correctActionButton_0.setSuspendedCorrectionPageUrl("/lab/suspend.jsp");
    _jspx_th_rn_correctActionButton_0.setResumedCorrectionPageUrl("/lab/resume.jsp");
    int _jspx_eval_rn_correctActionButton_0 = _jspx_th_rn_correctActionButton_0.doStartTag();
    if (_jspx_th_rn_correctActionButton_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_correctActionButton_withdrawnByProviderCorrectionPageUrl_suspendedCorrectionPageUrl_submittedCorrectionPageUrl_structureRefinedCorrectionPageUrl_resumedCorrectionPageUrl_releasedToPublicCorrectionPageUrl_preliminaryDataCollectedCorrectionPageUrl_label_failedToCollectCorrectionPageUrl_declaredNonScsCorrectionPageUrl_declaredIncompleteCorrectionPageUrl_citationCorrectionPageUrl_nobody.reuse(_jspx_th_rn_correctActionButton_0);
    return false;
  }

  private boolean _jspx_meth_ctl_parityChecker_4(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleActionFileIterator_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:parityChecker
    org.recipnet.common.controls.ParityChecker _jspx_th_ctl_parityChecker_4 = (org.recipnet.common.controls.ParityChecker) _jspx_tagPool_ctl_parityChecker_includeOnlyOnFirst.get(org.recipnet.common.controls.ParityChecker.class);
    _jspx_th_ctl_parityChecker_4.setPageContext(_jspx_page_context);
    _jspx_th_ctl_parityChecker_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleActionFileIterator_0);
    _jspx_th_ctl_parityChecker_4.setIncludeOnlyOnFirst(true);
    int _jspx_eval_ctl_parityChecker_4 = _jspx_th_ctl_parityChecker_4.doStartTag();
    if (_jspx_eval_ctl_parityChecker_4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_parityChecker_4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_parityChecker_4.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_parityChecker_4.doInitBody();
      }
      do {
        out.write("\n                  <div>\n                    Files added:\n                ");
        int evalDoAfterBody = _jspx_th_ctl_parityChecker_4.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_parityChecker_4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_parityChecker_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_parityChecker_includeOnlyOnFirst.reuse(_jspx_th_ctl_parityChecker_4);
    return false;
  }

  private boolean _jspx_meth_ctl_parityChecker_5(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleActionFileIterator_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:parityChecker
    org.recipnet.common.controls.ParityChecker _jspx_th_ctl_parityChecker_5 = (org.recipnet.common.controls.ParityChecker) _jspx_tagPool_ctl_parityChecker_invert_includeOnlyOnFirst.get(org.recipnet.common.controls.ParityChecker.class);
    _jspx_th_ctl_parityChecker_5.setPageContext(_jspx_page_context);
    _jspx_th_ctl_parityChecker_5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleActionFileIterator_0);
    _jspx_th_ctl_parityChecker_5.setIncludeOnlyOnFirst(true);
    _jspx_th_ctl_parityChecker_5.setInvert(true);
    int _jspx_eval_ctl_parityChecker_5 = _jspx_th_ctl_parityChecker_5.doStartTag();
    if (_jspx_eval_ctl_parityChecker_5 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_parityChecker_5 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_parityChecker_5.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_parityChecker_5.doInitBody();
      }
      do {
        out.write(",\n                    ");
        if (_jspx_meth_ctl_parityChecker_6(_jspx_th_ctl_parityChecker_5, _jspx_page_context))
          return true;
        out.write("\n                  ");
        int evalDoAfterBody = _jspx_th_ctl_parityChecker_5.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_parityChecker_5 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_parityChecker_5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_parityChecker_invert_includeOnlyOnFirst.reuse(_jspx_th_ctl_parityChecker_5);
    return false;
  }

  private boolean _jspx_meth_ctl_parityChecker_6(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_parityChecker_5, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:parityChecker
    org.recipnet.common.controls.ParityChecker _jspx_th_ctl_parityChecker_6 = (org.recipnet.common.controls.ParityChecker) _jspx_tagPool_ctl_parityChecker_includeOnlyOnLast.get(org.recipnet.common.controls.ParityChecker.class);
    _jspx_th_ctl_parityChecker_6.setPageContext(_jspx_page_context);
    _jspx_th_ctl_parityChecker_6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_parityChecker_5);
    _jspx_th_ctl_parityChecker_6.setIncludeOnlyOnLast(true);
    int _jspx_eval_ctl_parityChecker_6 = _jspx_th_ctl_parityChecker_6.doStartTag();
    if (_jspx_eval_ctl_parityChecker_6 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_parityChecker_6 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_parityChecker_6.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_parityChecker_6.doInitBody();
      }
      do {
        out.write("\n                      and\n                    ");
        int evalDoAfterBody = _jspx_th_ctl_parityChecker_6.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_parityChecker_6 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_parityChecker_6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_parityChecker_includeOnlyOnLast.reuse(_jspx_th_ctl_parityChecker_6);
    return false;
  }

  private boolean _jspx_meth_rn_fileField_3(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleActionFileIterator_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:fileField
    org.recipnet.site.content.rncontrols.FileField _jspx_th_rn_fileField_3 = (org.recipnet.site.content.rncontrols.FileField) _jspx_tagPool_rn_fileField_nobody.get(org.recipnet.site.content.rncontrols.FileField.class);
    _jspx_th_rn_fileField_3.setPageContext(_jspx_page_context);
    _jspx_th_rn_fileField_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleActionFileIterator_0);
    int _jspx_eval_rn_fileField_3 = _jspx_th_rn_fileField_3.doStartTag();
    if (_jspx_th_rn_fileField_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_fileField_nobody.reuse(_jspx_th_rn_fileField_3);
    return false;
  }

  private boolean _jspx_meth_ctl_parityChecker_7(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleActionFileIterator_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:parityChecker
    org.recipnet.common.controls.ParityChecker _jspx_th_ctl_parityChecker_7 = (org.recipnet.common.controls.ParityChecker) _jspx_tagPool_ctl_parityChecker_includeOnlyOnLast.get(org.recipnet.common.controls.ParityChecker.class);
    _jspx_th_ctl_parityChecker_7.setPageContext(_jspx_page_context);
    _jspx_th_ctl_parityChecker_7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleActionFileIterator_0);
    _jspx_th_ctl_parityChecker_7.setIncludeOnlyOnLast(true);
    int _jspx_eval_ctl_parityChecker_7 = _jspx_th_ctl_parityChecker_7.doStartTag();
    if (_jspx_eval_ctl_parityChecker_7 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_parityChecker_7 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_parityChecker_7.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_parityChecker_7.doInitBody();
      }
      do {
        out.write("\n                    </div>\n                  ");
        int evalDoAfterBody = _jspx_th_ctl_parityChecker_7.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_parityChecker_7 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_parityChecker_7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_parityChecker_includeOnlyOnLast.reuse(_jspx_th_ctl_parityChecker_7);
    return false;
  }

  private boolean _jspx_meth_ctl_parityChecker_8(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleActionFileIterator_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:parityChecker
    org.recipnet.common.controls.ParityChecker _jspx_th_ctl_parityChecker_8 = (org.recipnet.common.controls.ParityChecker) _jspx_tagPool_ctl_parityChecker_includeOnlyOnFirst.get(org.recipnet.common.controls.ParityChecker.class);
    _jspx_th_ctl_parityChecker_8.setPageContext(_jspx_page_context);
    _jspx_th_ctl_parityChecker_8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleActionFileIterator_1);
    _jspx_th_ctl_parityChecker_8.setIncludeOnlyOnFirst(true);
    int _jspx_eval_ctl_parityChecker_8 = _jspx_th_ctl_parityChecker_8.doStartTag();
    if (_jspx_eval_ctl_parityChecker_8 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_parityChecker_8 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_parityChecker_8.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_parityChecker_8.doInitBody();
      }
      do {
        out.write("\n                  <div>\n                    Files removed:\n                ");
        int evalDoAfterBody = _jspx_th_ctl_parityChecker_8.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_parityChecker_8 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_parityChecker_8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_parityChecker_includeOnlyOnFirst.reuse(_jspx_th_ctl_parityChecker_8);
    return false;
  }

  private boolean _jspx_meth_ctl_parityChecker_9(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleActionFileIterator_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:parityChecker
    org.recipnet.common.controls.ParityChecker _jspx_th_ctl_parityChecker_9 = (org.recipnet.common.controls.ParityChecker) _jspx_tagPool_ctl_parityChecker_invert_includeOnlyOnFirst.get(org.recipnet.common.controls.ParityChecker.class);
    _jspx_th_ctl_parityChecker_9.setPageContext(_jspx_page_context);
    _jspx_th_ctl_parityChecker_9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleActionFileIterator_1);
    _jspx_th_ctl_parityChecker_9.setIncludeOnlyOnFirst(true);
    _jspx_th_ctl_parityChecker_9.setInvert(true);
    int _jspx_eval_ctl_parityChecker_9 = _jspx_th_ctl_parityChecker_9.doStartTag();
    if (_jspx_eval_ctl_parityChecker_9 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_parityChecker_9 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_parityChecker_9.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_parityChecker_9.doInitBody();
      }
      do {
        out.write(",\n                    ");
        if (_jspx_meth_ctl_parityChecker_10(_jspx_th_ctl_parityChecker_9, _jspx_page_context))
          return true;
        out.write("\n                  ");
        int evalDoAfterBody = _jspx_th_ctl_parityChecker_9.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_parityChecker_9 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_parityChecker_9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_parityChecker_invert_includeOnlyOnFirst.reuse(_jspx_th_ctl_parityChecker_9);
    return false;
  }

  private boolean _jspx_meth_ctl_parityChecker_10(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_parityChecker_9, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:parityChecker
    org.recipnet.common.controls.ParityChecker _jspx_th_ctl_parityChecker_10 = (org.recipnet.common.controls.ParityChecker) _jspx_tagPool_ctl_parityChecker_includeOnlyOnLast.get(org.recipnet.common.controls.ParityChecker.class);
    _jspx_th_ctl_parityChecker_10.setPageContext(_jspx_page_context);
    _jspx_th_ctl_parityChecker_10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_parityChecker_9);
    _jspx_th_ctl_parityChecker_10.setIncludeOnlyOnLast(true);
    int _jspx_eval_ctl_parityChecker_10 = _jspx_th_ctl_parityChecker_10.doStartTag();
    if (_jspx_eval_ctl_parityChecker_10 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_parityChecker_10 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_parityChecker_10.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_parityChecker_10.doInitBody();
      }
      do {
        out.write("\n                      and\n                    ");
        int evalDoAfterBody = _jspx_th_ctl_parityChecker_10.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_parityChecker_10 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_parityChecker_10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_parityChecker_includeOnlyOnLast.reuse(_jspx_th_ctl_parityChecker_10);
    return false;
  }

  private boolean _jspx_meth_rn_fileField_4(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleActionFileIterator_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:fileField
    org.recipnet.site.content.rncontrols.FileField _jspx_th_rn_fileField_4 = (org.recipnet.site.content.rncontrols.FileField) _jspx_tagPool_rn_fileField_nobody.get(org.recipnet.site.content.rncontrols.FileField.class);
    _jspx_th_rn_fileField_4.setPageContext(_jspx_page_context);
    _jspx_th_rn_fileField_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleActionFileIterator_1);
    int _jspx_eval_rn_fileField_4 = _jspx_th_rn_fileField_4.doStartTag();
    if (_jspx_th_rn_fileField_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_fileField_nobody.reuse(_jspx_th_rn_fileField_4);
    return false;
  }

  private boolean _jspx_meth_ctl_parityChecker_11(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleActionFileIterator_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:parityChecker
    org.recipnet.common.controls.ParityChecker _jspx_th_ctl_parityChecker_11 = (org.recipnet.common.controls.ParityChecker) _jspx_tagPool_ctl_parityChecker_includeOnlyOnLast.get(org.recipnet.common.controls.ParityChecker.class);
    _jspx_th_ctl_parityChecker_11.setPageContext(_jspx_page_context);
    _jspx_th_ctl_parityChecker_11.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleActionFileIterator_1);
    _jspx_th_ctl_parityChecker_11.setIncludeOnlyOnLast(true);
    int _jspx_eval_ctl_parityChecker_11 = _jspx_th_ctl_parityChecker_11.doStartTag();
    if (_jspx_eval_ctl_parityChecker_11 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_parityChecker_11 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_parityChecker_11.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_parityChecker_11.doInitBody();
      }
      do {
        out.write("\n                    </div>\n                  ");
        int evalDoAfterBody = _jspx_th_ctl_parityChecker_11.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_parityChecker_11 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_parityChecker_11.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_parityChecker_includeOnlyOnLast.reuse(_jspx_th_ctl_parityChecker_11);
    return false;
  }

  private boolean _jspx_meth_ctl_parityChecker_12(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleActionFileIterator_2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:parityChecker
    org.recipnet.common.controls.ParityChecker _jspx_th_ctl_parityChecker_12 = (org.recipnet.common.controls.ParityChecker) _jspx_tagPool_ctl_parityChecker_includeOnlyOnFirst.get(org.recipnet.common.controls.ParityChecker.class);
    _jspx_th_ctl_parityChecker_12.setPageContext(_jspx_page_context);
    _jspx_th_ctl_parityChecker_12.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleActionFileIterator_2);
    _jspx_th_ctl_parityChecker_12.setIncludeOnlyOnFirst(true);
    int _jspx_eval_ctl_parityChecker_12 = _jspx_th_ctl_parityChecker_12.doStartTag();
    if (_jspx_eval_ctl_parityChecker_12 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_parityChecker_12 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_parityChecker_12.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_parityChecker_12.doInitBody();
      }
      do {
        out.write("\n                  <div>\n                    Files modified:\n                ");
        int evalDoAfterBody = _jspx_th_ctl_parityChecker_12.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_parityChecker_12 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_parityChecker_12.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_parityChecker_includeOnlyOnFirst.reuse(_jspx_th_ctl_parityChecker_12);
    return false;
  }

  private boolean _jspx_meth_ctl_parityChecker_13(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleActionFileIterator_2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:parityChecker
    org.recipnet.common.controls.ParityChecker _jspx_th_ctl_parityChecker_13 = (org.recipnet.common.controls.ParityChecker) _jspx_tagPool_ctl_parityChecker_invert_includeOnlyOnFirst.get(org.recipnet.common.controls.ParityChecker.class);
    _jspx_th_ctl_parityChecker_13.setPageContext(_jspx_page_context);
    _jspx_th_ctl_parityChecker_13.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleActionFileIterator_2);
    _jspx_th_ctl_parityChecker_13.setIncludeOnlyOnFirst(true);
    _jspx_th_ctl_parityChecker_13.setInvert(true);
    int _jspx_eval_ctl_parityChecker_13 = _jspx_th_ctl_parityChecker_13.doStartTag();
    if (_jspx_eval_ctl_parityChecker_13 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_parityChecker_13 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_parityChecker_13.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_parityChecker_13.doInitBody();
      }
      do {
        out.write(",\n                    ");
        if (_jspx_meth_ctl_parityChecker_14(_jspx_th_ctl_parityChecker_13, _jspx_page_context))
          return true;
        out.write("\n                  ");
        int evalDoAfterBody = _jspx_th_ctl_parityChecker_13.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_parityChecker_13 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_parityChecker_13.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_parityChecker_invert_includeOnlyOnFirst.reuse(_jspx_th_ctl_parityChecker_13);
    return false;
  }

  private boolean _jspx_meth_ctl_parityChecker_14(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_parityChecker_13, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:parityChecker
    org.recipnet.common.controls.ParityChecker _jspx_th_ctl_parityChecker_14 = (org.recipnet.common.controls.ParityChecker) _jspx_tagPool_ctl_parityChecker_includeOnlyOnLast.get(org.recipnet.common.controls.ParityChecker.class);
    _jspx_th_ctl_parityChecker_14.setPageContext(_jspx_page_context);
    _jspx_th_ctl_parityChecker_14.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_parityChecker_13);
    _jspx_th_ctl_parityChecker_14.setIncludeOnlyOnLast(true);
    int _jspx_eval_ctl_parityChecker_14 = _jspx_th_ctl_parityChecker_14.doStartTag();
    if (_jspx_eval_ctl_parityChecker_14 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_parityChecker_14 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_parityChecker_14.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_parityChecker_14.doInitBody();
      }
      do {
        out.write("\n                      and\n                    ");
        int evalDoAfterBody = _jspx_th_ctl_parityChecker_14.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_parityChecker_14 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_parityChecker_14.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_parityChecker_includeOnlyOnLast.reuse(_jspx_th_ctl_parityChecker_14);
    return false;
  }

  private boolean _jspx_meth_rn_fileField_5(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleActionFileIterator_2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:fileField
    org.recipnet.site.content.rncontrols.FileField _jspx_th_rn_fileField_5 = (org.recipnet.site.content.rncontrols.FileField) _jspx_tagPool_rn_fileField_nobody.get(org.recipnet.site.content.rncontrols.FileField.class);
    _jspx_th_rn_fileField_5.setPageContext(_jspx_page_context);
    _jspx_th_rn_fileField_5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleActionFileIterator_2);
    int _jspx_eval_rn_fileField_5 = _jspx_th_rn_fileField_5.doStartTag();
    if (_jspx_th_rn_fileField_5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_fileField_nobody.reuse(_jspx_th_rn_fileField_5);
    return false;
  }

  private boolean _jspx_meth_ctl_parityChecker_15(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleActionFileIterator_2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:parityChecker
    org.recipnet.common.controls.ParityChecker _jspx_th_ctl_parityChecker_15 = (org.recipnet.common.controls.ParityChecker) _jspx_tagPool_ctl_parityChecker_includeOnlyOnLast.get(org.recipnet.common.controls.ParityChecker.class);
    _jspx_th_ctl_parityChecker_15.setPageContext(_jspx_page_context);
    _jspx_th_ctl_parityChecker_15.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleActionFileIterator_2);
    _jspx_th_ctl_parityChecker_15.setIncludeOnlyOnLast(true);
    int _jspx_eval_ctl_parityChecker_15 = _jspx_th_ctl_parityChecker_15.doStartTag();
    if (_jspx_eval_ctl_parityChecker_15 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_parityChecker_15 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_parityChecker_15.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_parityChecker_15.doInitBody();
      }
      do {
        out.write("\n                    </div>\n                  ");
        int evalDoAfterBody = _jspx_th_ctl_parityChecker_15.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_parityChecker_15 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_parityChecker_15.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_parityChecker_includeOnlyOnLast.reuse(_jspx_th_ctl_parityChecker_15);
    return false;
  }

  private boolean _jspx_meth_rn_suppressedActionCount_0(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_errorMessage_18, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:suppressedActionCount
    org.recipnet.site.content.rncontrols.SuppressedActionCount _jspx_th_rn_suppressedActionCount_0 = (org.recipnet.site.content.rncontrols.SuppressedActionCount) _jspx_tagPool_rn_suppressedActionCount_includeFileActionsInCount_actionIterator_nobody.get(org.recipnet.site.content.rncontrols.SuppressedActionCount.class);
    _jspx_th_rn_suppressedActionCount_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_suppressedActionCount_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_errorMessage_18);
    _jspx_th_rn_suppressedActionCount_0.setActionIterator((org.recipnet.site.content.rncontrols.SampleActionIterator) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${actionIt}", org.recipnet.site.content.rncontrols.SampleActionIterator.class, (PageContext)_jspx_page_context, null, false));
    _jspx_th_rn_suppressedActionCount_0.setIncludeFileActionsInCount(true);
    int _jspx_eval_rn_suppressedActionCount_0 = _jspx_th_rn_suppressedActionCount_0.doStartTag();
    if (_jspx_th_rn_suppressedActionCount_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_suppressedActionCount_includeFileActionsInCount_actionIterator_nobody.reuse(_jspx_th_rn_suppressedActionCount_0);
    return false;
  }

  private boolean _jspx_meth_rn_suppressedActionCount_1(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_errorMessage_20, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:suppressedActionCount
    org.recipnet.site.content.rncontrols.SuppressedActionCount _jspx_th_rn_suppressedActionCount_1 = (org.recipnet.site.content.rncontrols.SuppressedActionCount) _jspx_tagPool_rn_suppressedActionCount_includeCorrectionActionsInCount_actionIterator_nobody.get(org.recipnet.site.content.rncontrols.SuppressedActionCount.class);
    _jspx_th_rn_suppressedActionCount_1.setPageContext(_jspx_page_context);
    _jspx_th_rn_suppressedActionCount_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_errorMessage_20);
    _jspx_th_rn_suppressedActionCount_1.setActionIterator((org.recipnet.site.content.rncontrols.SampleActionIterator) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${actionIt}", org.recipnet.site.content.rncontrols.SampleActionIterator.class, (PageContext)_jspx_page_context, null, false));
    _jspx_th_rn_suppressedActionCount_1.setIncludeCorrectionActionsInCount(true);
    int _jspx_eval_rn_suppressedActionCount_1 = _jspx_th_rn_suppressedActionCount_1.doStartTag();
    if (_jspx_th_rn_suppressedActionCount_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_suppressedActionCount_includeCorrectionActionsInCount_actionIterator_nobody.reuse(_jspx_th_rn_suppressedActionCount_1);
    return false;
  }

  private boolean _jspx_meth_rn_suppressedActionCount_2(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_errorMessage_22, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:suppressedActionCount
    org.recipnet.site.content.rncontrols.SuppressedActionCount _jspx_th_rn_suppressedActionCount_2 = (org.recipnet.site.content.rncontrols.SuppressedActionCount) _jspx_tagPool_rn_suppressedActionCount_includeOtherActionsInCount_actionIterator_nobody.get(org.recipnet.site.content.rncontrols.SuppressedActionCount.class);
    _jspx_th_rn_suppressedActionCount_2.setPageContext(_jspx_page_context);
    _jspx_th_rn_suppressedActionCount_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_errorMessage_22);
    _jspx_th_rn_suppressedActionCount_2.setActionIterator((org.recipnet.site.content.rncontrols.SampleActionIterator) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${actionIt}", org.recipnet.site.content.rncontrols.SampleActionIterator.class, (PageContext)_jspx_page_context, null, false));
    _jspx_th_rn_suppressedActionCount_2.setIncludeOtherActionsInCount(true);
    int _jspx_eval_rn_suppressedActionCount_2 = _jspx_th_rn_suppressedActionCount_2.doStartTag();
    if (_jspx_th_rn_suppressedActionCount_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_suppressedActionCount_includeOtherActionsInCount_actionIterator_nobody.reuse(_jspx_th_rn_suppressedActionCount_2);
    return false;
  }

  private boolean _jspx_meth_rn_suppressedActionCount_3(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_errorMessage_24, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:suppressedActionCount
    org.recipnet.site.content.rncontrols.SuppressedActionCount _jspx_th_rn_suppressedActionCount_3 = (org.recipnet.site.content.rncontrols.SuppressedActionCount) _jspx_tagPool_rn_suppressedActionCount_includeSkippedActionsInCount_actionIterator_nobody.get(org.recipnet.site.content.rncontrols.SuppressedActionCount.class);
    _jspx_th_rn_suppressedActionCount_3.setPageContext(_jspx_page_context);
    _jspx_th_rn_suppressedActionCount_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_errorMessage_24);
    _jspx_th_rn_suppressedActionCount_3.setActionIterator((org.recipnet.site.content.rncontrols.SampleActionIterator) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${actionIt}", org.recipnet.site.content.rncontrols.SampleActionIterator.class, (PageContext)_jspx_page_context, null, false));
    _jspx_th_rn_suppressedActionCount_3.setIncludeSkippedActionsInCount(true);
    int _jspx_eval_rn_suppressedActionCount_3 = _jspx_th_rn_suppressedActionCount_3.doStartTag();
    if (_jspx_th_rn_suppressedActionCount_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_suppressedActionCount_includeSkippedActionsInCount_actionIterator_nobody.reuse(_jspx_th_rn_suppressedActionCount_3);
    return false;
  }

  private boolean _jspx_meth_rn_link_10(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleChecker_2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:link
    org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_10 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_styleClass_href.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
    _jspx_th_rn_link_10.setPageContext(_jspx_page_context);
    _jspx_th_rn_link_10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleChecker_2);
    _jspx_th_rn_link_10.setHref("/lab/collectpreliminarydata.jsp");
    _jspx_th_rn_link_10.setStyleClass("menuItem");
    int _jspx_eval_rn_link_10 = _jspx_th_rn_link_10.doStartTag();
    if (_jspx_eval_rn_link_10 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_link_10 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_link_10.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_link_10.doInitBody();
      }
      do {
        out.write("\n              Collect preliminary data\n            ");
        int evalDoAfterBody = _jspx_th_rn_link_10.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_link_10 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_link_10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_link_styleClass_href.reuse(_jspx_th_rn_link_10);
    return false;
  }

  private boolean _jspx_meth_ctl_img_7(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_selfForm_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:img
    org.recipnet.common.controls.ImageHtmlElement _jspx_th_ctl_img_7 = (org.recipnet.common.controls.ImageHtmlElement) _jspx_tagPool_ctl_img_src_alt_nobody.get(org.recipnet.common.controls.ImageHtmlElement.class);
    _jspx_th_ctl_img_7.setPageContext(_jspx_page_context);
    _jspx_th_ctl_img_7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
    _jspx_th_ctl_img_7.setSrc("/images/flow-arrow.gif");
    _jspx_th_ctl_img_7.setAlt("-->");
    int _jspx_eval_ctl_img_7 = _jspx_th_ctl_img_7.doStartTag();
    if (_jspx_th_ctl_img_7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_img_src_alt_nobody.reuse(_jspx_th_ctl_img_7);
    return false;
  }

  private boolean _jspx_meth_rn_link_11(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleChecker_4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:link
    org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_11 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_styleClass_href.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
    _jspx_th_rn_link_11.setPageContext(_jspx_page_context);
    _jspx_th_rn_link_11.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleChecker_4);
    _jspx_th_rn_link_11.setHref("/lab/refinestructure.jsp");
    _jspx_th_rn_link_11.setStyleClass("menuItem");
    int _jspx_eval_rn_link_11 = _jspx_th_rn_link_11.doStartTag();
    if (_jspx_eval_rn_link_11 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_link_11 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_link_11.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_link_11.doInitBody();
      }
      do {
        out.write("\n              Refine structure\n            ");
        int evalDoAfterBody = _jspx_th_rn_link_11.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_link_11 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_link_11.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_link_styleClass_href.reuse(_jspx_th_rn_link_11);
    return false;
  }

  private boolean _jspx_meth_ctl_img_8(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_selfForm_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:img
    org.recipnet.common.controls.ImageHtmlElement _jspx_th_ctl_img_8 = (org.recipnet.common.controls.ImageHtmlElement) _jspx_tagPool_ctl_img_src_alt_nobody.get(org.recipnet.common.controls.ImageHtmlElement.class);
    _jspx_th_ctl_img_8.setPageContext(_jspx_page_context);
    _jspx_th_ctl_img_8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
    _jspx_th_ctl_img_8.setSrc("/images/flow-arrow.gif");
    _jspx_th_ctl_img_8.setAlt("-->");
    int _jspx_eval_ctl_img_8 = _jspx_th_ctl_img_8.doStartTag();
    if (_jspx_th_ctl_img_8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_img_src_alt_nobody.reuse(_jspx_th_ctl_img_8);
    return false;
  }

  private boolean _jspx_meth_rn_link_12(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleChecker_6, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:link
    org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_12 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_styleClass_href.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
    _jspx_th_rn_link_12.setPageContext(_jspx_page_context);
    _jspx_th_rn_link_12.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleChecker_6);
    _jspx_th_rn_link_12.setHref("/lab/releasetopublic.jsp");
    _jspx_th_rn_link_12.setStyleClass("menuItem");
    int _jspx_eval_rn_link_12 = _jspx_th_rn_link_12.doStartTag();
    if (_jspx_eval_rn_link_12 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_link_12 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_link_12.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_link_12.doInitBody();
      }
      do {
        out.write("\n              Release to Public\n            ");
        int evalDoAfterBody = _jspx_th_rn_link_12.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_link_12 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_link_12.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_link_styleClass_href.reuse(_jspx_th_rn_link_12);
    return false;
  }

  private boolean _jspx_meth_rn_link_13(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleChecker_8, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:link
    org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_13 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_styleClass_href.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
    _jspx_th_rn_link_13.setPageContext(_jspx_page_context);
    _jspx_th_rn_link_13.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleChecker_8);
    _jspx_th_rn_link_13.setHref("/lab/modifytext.jsp");
    _jspx_th_rn_link_13.setStyleClass("menuItem");
    int _jspx_eval_rn_link_13 = _jspx_th_rn_link_13.doStartTag();
    if (_jspx_eval_rn_link_13 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_link_13 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_link_13.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_link_13.doInitBody();
      }
      do {
        out.write("\n              Modify text\n            ");
        int evalDoAfterBody = _jspx_th_rn_link_13.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_link_13 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_link_13.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_link_styleClass_href.reuse(_jspx_th_rn_link_13);
    return false;
  }

  private boolean _jspx_meth_rn_link_14(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleChecker_10, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:link
    org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_14 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_styleClass_href.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
    _jspx_th_rn_link_14.setPageContext(_jspx_page_context);
    _jspx_th_rn_link_14.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleChecker_10);
    _jspx_th_rn_link_14.setHref("/lab/addcitation.jsp");
    _jspx_th_rn_link_14.setStyleClass("menuItem");
    int _jspx_eval_rn_link_14 = _jspx_th_rn_link_14.doStartTag();
    if (_jspx_eval_rn_link_14 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_link_14 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_link_14.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_link_14.doInitBody();
      }
      do {
        out.write("\n              Add Citation\n            ");
        int evalDoAfterBody = _jspx_th_rn_link_14.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_link_14 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_link_14.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_link_styleClass_href.reuse(_jspx_th_rn_link_14);
    return false;
  }

  private boolean _jspx_meth_rn_link_15(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleChecker_12, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:link
    org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_15 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_styleClass_href.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
    _jspx_th_rn_link_15.setPageContext(_jspx_page_context);
    _jspx_th_rn_link_15.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleChecker_12);
    _jspx_th_rn_link_15.setHref("/lab/failedtocollect.jsp");
    _jspx_th_rn_link_15.setStyleClass("menuItem");
    int _jspx_eval_rn_link_15 = _jspx_th_rn_link_15.doStartTag();
    if (_jspx_eval_rn_link_15 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_link_15 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_link_15.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_link_15.doInitBody();
      }
      do {
        out.write("\n              Failed to collect\n            ");
        int evalDoAfterBody = _jspx_th_rn_link_15.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_link_15 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_link_15.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_link_styleClass_href.reuse(_jspx_th_rn_link_15);
    return false;
  }

  private boolean _jspx_meth_rn_link_16(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleChecker_14, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:link
    org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_16 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_styleClass_href.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
    _jspx_th_rn_link_16.setPageContext(_jspx_page_context);
    _jspx_th_rn_link_16.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleChecker_14);
    _jspx_th_rn_link_16.setHref("/lab/declareincomplete.jsp");
    _jspx_th_rn_link_16.setStyleClass("menuItem");
    int _jspx_eval_rn_link_16 = _jspx_th_rn_link_16.doStartTag();
    if (_jspx_eval_rn_link_16 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_link_16 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_link_16.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_link_16.doInitBody();
      }
      do {
        out.write("\n              Declare Incomplete\n            ");
        int evalDoAfterBody = _jspx_th_rn_link_16.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_link_16 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_link_16.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_link_styleClass_href.reuse(_jspx_th_rn_link_16);
    return false;
  }

  private boolean _jspx_meth_rn_link_17(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleChecker_16, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:link
    org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_17 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_styleClass_href.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
    _jspx_th_rn_link_17.setPageContext(_jspx_page_context);
    _jspx_th_rn_link_17.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleChecker_16);
    _jspx_th_rn_link_17.setHref("/lab/customfields.jsp");
    _jspx_th_rn_link_17.setStyleClass("menuItem");
    int _jspx_eval_rn_link_17 = _jspx_th_rn_link_17.doStartTag();
    if (_jspx_eval_rn_link_17 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_link_17 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_link_17.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_link_17.doInitBody();
      }
      do {
        out.write("\n              Edit Custom fields\n            ");
        int evalDoAfterBody = _jspx_th_rn_link_17.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_link_17 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_link_17.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_link_styleClass_href.reuse(_jspx_th_rn_link_17);
    return false;
  }

  private boolean _jspx_meth_rn_link_18(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleChecker_18, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:link
    org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_18 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_styleClass_href.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
    _jspx_th_rn_link_18.setPageContext(_jspx_page_context);
    _jspx_th_rn_link_18.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleChecker_18);
    _jspx_th_rn_link_18.setHref("/lab/retract.jsp");
    _jspx_th_rn_link_18.setStyleClass("menuItem");
    int _jspx_eval_rn_link_18 = _jspx_th_rn_link_18.doStartTag();
    if (_jspx_eval_rn_link_18 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_link_18 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_link_18.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_link_18.doInitBody();
      }
      do {
        out.write("\n              Retract\n            ");
        int evalDoAfterBody = _jspx_th_rn_link_18.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_link_18 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_link_18.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_link_styleClass_href.reuse(_jspx_th_rn_link_18);
    return false;
  }

  private boolean _jspx_meth_rn_link_19(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleChecker_20, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:link
    org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_19 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_styleClass_href.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
    _jspx_th_rn_link_19.setPageContext(_jspx_page_context);
    _jspx_th_rn_link_19.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleChecker_20);
    _jspx_th_rn_link_19.setHref("/lab/declarenonscs.jsp");
    _jspx_th_rn_link_19.setStyleClass("menuItem");
    int _jspx_eval_rn_link_19 = _jspx_th_rn_link_19.doStartTag();
    if (_jspx_eval_rn_link_19 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_link_19 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_link_19.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_link_19.doInitBody();
      }
      do {
        out.write("\n              Declare non-SCS\n            ");
        int evalDoAfterBody = _jspx_th_rn_link_19.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_link_19 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_link_19.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_link_styleClass_href.reuse(_jspx_th_rn_link_19);
    return false;
  }

  private boolean _jspx_meth_rn_link_20(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleChecker_22, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:link
    org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_20 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_styleClass_href.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
    _jspx_th_rn_link_20.setPageContext(_jspx_page_context);
    _jspx_th_rn_link_20.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleChecker_22);
    _jspx_th_rn_link_20.setHref("/lab/suspend.jsp");
    _jspx_th_rn_link_20.setStyleClass("menuItem");
    int _jspx_eval_rn_link_20 = _jspx_th_rn_link_20.doStartTag();
    if (_jspx_eval_rn_link_20 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_link_20 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_link_20.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_link_20.doInitBody();
      }
      do {
        out.write("\n              Suspend\n            ");
        int evalDoAfterBody = _jspx_th_rn_link_20.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_link_20 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_link_20.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_link_styleClass_href.reuse(_jspx_th_rn_link_20);
    return false;
  }

  private boolean _jspx_meth_rn_link_21(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleChecker_23, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:link
    org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_21 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_styleClass_href.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
    _jspx_th_rn_link_21.setPageContext(_jspx_page_context);
    _jspx_th_rn_link_21.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleChecker_23);
    _jspx_th_rn_link_21.setHref("/lab/resume.jsp");
    _jspx_th_rn_link_21.setStyleClass("menuItem");
    int _jspx_eval_rn_link_21 = _jspx_th_rn_link_21.doStartTag();
    if (_jspx_eval_rn_link_21 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_link_21 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_link_21.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_link_21.doInitBody();
      }
      do {
        out.write("\n              Resume\n            ");
        int evalDoAfterBody = _jspx_th_rn_link_21.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_link_21 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_link_21.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_link_styleClass_href.reuse(_jspx_th_rn_link_21);
    return false;
  }

  private boolean _jspx_meth_rn_link_22(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleChecker_26, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:link
    org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_22 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_styleClass_href.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
    _jspx_th_rn_link_22.setPageContext(_jspx_page_context);
    _jspx_th_rn_link_22.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleChecker_26);
    _jspx_th_rn_link_22.setHref("/lab/changeacl.jsp");
    _jspx_th_rn_link_22.setStyleClass("menuItem");
    int _jspx_eval_rn_link_22 = _jspx_th_rn_link_22.doStartTag();
    if (_jspx_eval_rn_link_22 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_link_22 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_link_22.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_link_22.doInitBody();
      }
      do {
        out.write("\n              Grant / Revoke Access\n            ");
        int evalDoAfterBody = _jspx_th_rn_link_22.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_link_22 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_link_22.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_link_styleClass_href.reuse(_jspx_th_rn_link_22);
    return false;
  }

  private boolean _jspx_meth_rn_link_23(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleChecker_27, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:link
    org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_23 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_styleClass_href.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
    _jspx_th_rn_link_23.setPageContext(_jspx_page_context);
    _jspx_th_rn_link_23.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleChecker_27);
    _jspx_th_rn_link_23.setHref("/lab/withdraw.jsp");
    _jspx_th_rn_link_23.setStyleClass("menuItem");
    int _jspx_eval_rn_link_23 = _jspx_th_rn_link_23.doStartTag();
    if (_jspx_eval_rn_link_23 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_link_23 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_link_23.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_link_23.doInitBody();
      }
      do {
        out.write("\n              Withdraw\n            ");
        int evalDoAfterBody = _jspx_th_rn_link_23.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_link_23 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_link_23.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_link_styleClass_href.reuse(_jspx_th_rn_link_23);
    return false;
  }

  private boolean _jspx_meth_ctl_styleBlock_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_editSamplePage_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:styleBlock
    org.recipnet.common.controls.HtmlPageStyleBlock _jspx_th_ctl_styleBlock_0 = (org.recipnet.common.controls.HtmlPageStyleBlock) _jspx_tagPool_ctl_styleBlock.get(org.recipnet.common.controls.HtmlPageStyleBlock.class);
    _jspx_th_ctl_styleBlock_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_styleBlock_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_editSamplePage_0);
    int _jspx_eval_ctl_styleBlock_0 = _jspx_th_ctl_styleBlock_0.doStartTag();
    if (_jspx_eval_ctl_styleBlock_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_styleBlock_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_styleBlock_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_styleBlock_0.doInitBody();
      }
      do {
        out.write("\n      div.actionBox100,\n      div.actionBox110 { background-color: #68396F; }\n      div.actionBox200,\n      div.actionBox210 { background-color: #2D532D; }\n      div.actionBox300,\n      div.actionBox310 { background-color: #794647; }\n      div.actionBox400,\n      div.actionBox410 { background-color: #787547; }\n      div.actionBox500,\n      div.actionBox550 { background-color: #666666; }\n      div.actionBox600,\n      div.actionBox610 { background-color: #107060; }\n      div.actionBox800,\n      div.actionBox850,\n      div.actionBox900,\n      div.actionBox905,\n      div.actionBox910,\n      div.actionBox915,\n      div.actionBox1000,\n      div.actionBox1100,\n      div.actionBox1210,\n      div.actionBox1220,\n      div.actionBox1300,\n      div.actionBox1400 { background-color: #666666; }\n      div.actionBox1500,\n      div.actionBox1510 { background-color: #84603C; }\n      div.actionBox1600,\n      div.actionBox1610 { background-color: #666666; }\n      div.actionBox1700,\n      div.actionBox1710 { background-color: #84603C; }\n");
        out.write("      div.actionBox1800,\n      div.actionBox1810,\n      div.actionBox10000,\n      div.actionBox10100,\n      div.actionBox10200,\n      div.actionBox10300,\n      div.actionBox10400,\n      div.actionBox10500,\n      div.actionBox10600,\n      div.actionBox10700,\n      div.actionBox10800,\n      div.actionBox10900,\n      div.actionBox100000 { background-color: #666666; }\n      form { margin: 0; border: 0; padding: 0; }\n      div.pageBody table { width: 100%; }\n      td.actionColumn { vertical-align: top; text-align: right; }\n      td.actionColumn > div { margin-left: 1em; }\n      div.errorBox,\n      div.actionBox,\n      div.infoBox { padding: 0.25em 0.25em 0.25em 1.25em; margin: 1em 0 1em 0; }\n      div.errorBox { background-color: #CC0000; }\n      div.infoBox { background-color: #2E385C; }\n      div.actionBox { text-align: left; color: #CCCCCC; }\n      span.bright,\n      div.errorBoxTitle,\n      div.infoBoxTitle { font-weight: bold; color: white; }\n      div.errorBoxTitle,\n      div.actionBoxTitle,\n      div.twoLineActionBoxTitle,\n");
        out.write("      div.infoBoxTitle { font-size: small; margin-bottom: 0.25em;\n                         white-space: nowrap; }\n      div.actionBoxTitle,\n      div.errorBoxTitle,\n      div.infoBoxTitle { line-height: 32px; }\n      div.twoLineActionBoxTitle { line-height: 16px; }\n      div.infoBoxBody,\n      div.errorBoxBody,\n      div.actionBoxBody { background-color: #F3F3F3; padding: 0.25em;\n                          clear: both; }\n      div.infoBoxBody th,\n      div.infoBoxBody td,\n      div.actionBoxBody th,\n      div.actionBoxBody td { vertical-align: baseline; }\n      span.dark,\n      li.currentStatus,\n      div.actionBox td,\n      div.infoBox td { font-weight: bold;  color: #000050; }\n      div.actionBox td,\n      div.infoBox td { text-align: left; }\n      div.infoBox ul { margin: 0; }\n      div.actionBox td.suppressionCount { text-align: center;\n          font-weight: normal; }\n      div.actionBox td.suppressionCount,\n      div.actionBox th,\n      div.infoBox th { font-style: narrow; color: #606060; }\n      div.actionBox th,\n");
        out.write("      div.infoBox th { font-weight: normal; text-align: right;\n                       white-space: nowrap; }\n      table.preferenceTable { color: #505050; background-color: #C8C8C8;\n          margin-top: 0; font-size: small; font-style: italic; }\n      img.icon { height: 32px; float: left; margin-right: 0.2em; }\n      span.light,\n      li.otherStatus { font-style: narrow; color: #A0A0A0; }\n      span.comments { font-style: normal; color: #DDDDDD; }\n      span.fileName { font-weight: bold; color: white; }\n      span.skippedByReversionMessage { color: #F0F0F0; font-weight: bold;\n          font-style: italic; }\n      div.fileDescription { margin-left: 1em; font: italic small Times, serif;\n          color: #707070; }\n      img.grayArcImage { vertical-align: top; text-align: right; }\n      img.grayCurveImage { vertical-align: bottom; }\n      span.preferenceItem { white-space: nowrap; padding-right: 1em; }\n      table#workflowTable { white-space: nowrap; text-align: center; width: 1px;\n          margin: 1em auto 0 auto; border: 2px solid #DAE8F1; }\n");
        out.write("      #workflowTable th { background: #DAE8F1; text-align: left; }\n      th.leftPadded,\n      td.leftPadded { padding-left: 1em; }\n  ");
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
