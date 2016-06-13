/*
 * Reciprocal Net project
 * 
 * RevertWapPage.java
 *
 * 05-Jul-2005: midurbin wrote first draft
 * 07-Jul-2005: midurbin updated performWorkflowAction() to return a boolean
 * 27-Jul-2005: midurbin updated doBeforeBody() to reflect name and spec
 *              change; updated performWorkflowAction() to throw
 *              EvaluationAbortedException
 * 14-Jun-2006: jobolllin reformatted the source
 */

package org.recipnet.site.content.rncontrols;

import java.io.IOException;
import java.rmi.RemoteException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import org.recipnet.common.controls.EvaluationAbortedException;
import org.recipnet.common.controls.HtmlPage;
import org.recipnet.site.RecipnetException;
import org.recipnet.site.shared.bl.SampleWorkflowBL;
import org.recipnet.site.shared.logevent.SampleActionLogEvent;
import org.recipnet.site.wrapper.CoreConnector;
import org.recipnet.site.wrapper.LanguageHelper;
import org.recipnet.site.wrapper.WrongSampleStateException;

/**
 * <p>
 * An extension of {@code WapPage} that reverts a sample to a previous version.
 * </p>
 * <p>
 * In addition to the request parameters recognized by the superclass, this tag
 * recognizes the "targetSampleHistoryId" request parameter which indicates a
 * previous version of the sample to which the tag should be reverted.
 * </p>
 * <p>
 * In order to control whether data files are reverted along with metadata, the
 * 'includeDataFilesParamName' property may be set to the name of a request
 * parameter that if present indicates that data files should be included. Pages
 * may include a checkbox or other control whose 'id' value is equal to the
 * value of that property to have it control the inclusion.
 * </p>
 */
public class RevertWapPage extends WapPage {

    /**
     * An optional property that when set indicats a parameter name that when
     * present indicates that data files should be included in the reversion.
     */
    private String includeDataFilesParamName;

    /**
     * A private variable that keeps track of whether it has been determined
     * that data files should be included.
     */
    private boolean includeDataFilesInReversion;

    /**
     * A private member variable that keeps track of the history id representing
     * the version of the sample to which it should be reverted.
     */
    private int targetSampleHistoryId;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.includeDataFilesParamName = null;
        this.includeDataFilesInReversion = true;
    }

    /** Sets the 'includeDataFilesParamName' property. */
    public void setIncludeDataFilesParamName(String name) {
        this.includeDataFilesParamName = name;
    }

    /** Gets the 'includeDataFilesParamName' property. */
    public String getIncludeDataFilesParamName() {
        return this.includeDataFilesParamName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doBeforePageBody() throws JspException,
            EvaluationAbortedException {
        super.doBeforePageBody();

        switch (getPhase()) {
            case HtmlPage.REGISTRATION_PHASE:
                /*
                 * Attepmt to determine the sample version to which the sample
                 * is being reverted
                 */
                try {
                    String targetSampleHistoryIdStr
                            = super.pageContext.getRequest().getParameter(
                                    "targetSampleHistoryId");
                    
                    if (targetSampleHistoryIdStr == null) {
                        // targetSampleHistoryId is required, but missing
                        throw new NumberFormatException();
                    } else {
                        this.targetSampleHistoryId
                                = Integer.parseInt(targetSampleHistoryIdStr);
                        addFormField("targetSampleHistoryId",
                                targetSampleHistoryIdStr);
                    }
                } catch (NumberFormatException nfe) {
                    try {
                        ((HttpServletResponse) super.pageContext.getResponse()
                                ).sendError(HttpServletResponse.SC_BAD_REQUEST);
                        abort();
                        return;
                    } catch (IOException ex) {
                        throw new JspException(ex);
                    }
                }
                break;
            case HtmlPage.PARSING_PHASE:
                if (this.includeDataFilesParamName != null) {
                    this.includeDataFilesInReversion
                            = this.pageContext.getRequest().getParameter(
                                    this.includeDataFilesParamName) != null;
                }
                break;
            case HtmlPage.RENDERING_PHASE:
                // does nothing this phase
                break;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean performWorkflowAction() throws JspException,
            EvaluationAbortedException {
        if (!areAllFieldsValid()) {
            setErrorFlag(NESTED_TAG_REPORTED_VALIDATION_ERROR);
            return false;
        }
        int actionCode = (this.includeDataFilesInReversion
                ? SampleWorkflowBL.REVERTED_INCLUDING_FILES
                : SampleWorkflowBL.REVERTED_WITHOUT_FILES);
        if (!SampleWorkflowBL.isActionValid(getSampleInfo().status, actionCode)) {
            throw new JspException(new WrongSampleStateException(
                    getSampleInfo().status, actionCode));
        }

        // update the sample to reflect action
        SampleWorkflowBL.alterSampleForWorkflowAction(getSampleInfo(),
                actionCode);

        try {
            CoreConnector cc = CoreConnector.extract(
                    this.pageContext.getServletContext());
            
            if (this.includeDataFilesInReversion) {
                cc.getRepositoryManager().revertSampleToVersionIncludingFiles(
                        getSampleInfo().id, getSampleInfo().historyId,
                        this.targetSampleHistoryId, getUserInfo().id,
                        getComments());
            } else {
                cc.getSampleManager().revertSampleToVersion(getSampleInfo().id,
                        getSampleInfo().historyId, this.targetSampleHistoryId,
                        getUserInfo().id, getComments());
            }

            // record log event
            cc.getSiteManager().recordLogEvent(
                    new SampleActionLogEvent(
                            getSampleInfo().id,
                            getSampleInfo().localLabId,
                            this.pageContext.getSession().getId(),
                            this.pageContext.getRequest().getServerName(),
                            getUserInfo().fullName,
                            LanguageHelper.extract(
                                    pageContext.getServletContext()
                                    ).getActionString(
                                            actionCode,
                                            LanguageHelper.getDefaultLocales(),
                                            false)));

            // redirect upon completion
            redirectToEditSamplePage();

            // ensure that processing halts
            abort();
            return true;
        } catch (RemoteException ex) {
            CoreConnector.extract(this.pageContext.getServletContext()
                    ).reportRemoteException(ex);
            throw new JspException(ex);
        } catch (RecipnetException ex) {
            throw new JspException(ex);
        } catch (IOException ex) {
            throw new JspException(ex);
        }
    }
}
