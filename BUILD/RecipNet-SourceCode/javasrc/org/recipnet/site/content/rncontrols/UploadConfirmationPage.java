/*
 * Reciprocal Net project
 * 
 * UploadConfirmationPage.java
 * 
 * 04-Aug-2005: midurbin wrote first draft
 * 14-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.site.content.rncontrols;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Collection;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import org.recipnet.common.controls.EvaluationAbortedException;
import org.recipnet.common.controls.HtmlPage;
import org.recipnet.site.OperationFailedException;
import org.recipnet.site.InconsistentDbException;
import org.recipnet.site.core.ResourceNotFoundException;
import org.recipnet.site.shared.db.SampleHistoryInfo;
import org.recipnet.site.shared.db.SampleInfo;
import org.recipnet.site.wrapper.CoreConnector;
import org.recipnet.site.wrapper.OperationPersister;
import org.recipnet.site.wrapper.RequestCache;
import org.recipnet.site.wrapper.UploaderOperation;

/**
 * A special-purpose custom tag that extends {@code RecipnetPage} to expose a
 * {@code SampleContext} and {@code MultiFilenameContext} based on the contents
 * of an {@code UploaderOperation} supplied to this page, either via an
 * attribute of the {@code ServletRequest} or via the "persistedOpId" request
 * parameter.
 */
public class UploadConfirmationPage extends RecipnetPage implements
        SampleContext, MultiFilenameContext {

    /**
     * A reference to the {@code UploaderOperation} passed to this page as the
     * 'persistedOp' attribute on the {@code ServletRequest} or whose id was
     * passed as the 'persistedOpId' parameter. This reference is determined by
     * {@code doBeforePageBody()} during the {@code REGISTRATION_PHASE}.
     */
    private UploaderOperation completedUploaderOp;

    /**
     * A required property that indicates the number of seconds until the
     * browser is redirected to the 'editSamplePage'.
     */
    private int secondsUntilRedirect;

    /**
     * A required property that indicates the URL (relative to the servlet
     * context root) of a page to which this tag should redirect the browser.
     * This page must accept the single parameter "sampleId" indicating the
     * sample id for the sample contained in the UploaderOperation.
     */
    private String redirectTargetPageUrl;

    /**
     * A reference to the most recent version of the sample included in the
     * {@code UploaderOperation} that was passed to this page. This
     * sample-version is fetched during the {@code FETCHING_PHASE}.
     */
    private SampleInfo sampleInfo;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.completedUploaderOp = null;
        this.sampleInfo = null;
    }

    /** Sets the 'secondsUntilRedirect' property. */
    public void setSecondsUntilRedirect(int seconds) {
        this.secondsUntilRedirect = seconds;
    }

    /** Gets the 'secondsUntilRedirect' property. */
    public int getSecondsUntilRedirect() {
        return this.secondsUntilRedirect;
    }

    /** Sets the 'redirectTargetPageUrl' property. */
    public void setRedirectTargetPageUrl(String url) {
        this.redirectTargetPageUrl = url;
    }

    /** Gets the 'redirectTargetPageUrl' property. */
    public String getRedirectTargetPageUrl() {
        return this.redirectTargetPageUrl;
    }

    /**
     * {@inheritDoc}; this version gets the {@code UploaderOperation} during
     * the {@code REGISTRATION_PHASE} and fetches the {@code SampleInfo} during
     * the {@code FETCHING_PHASE}.
     */
    @Override
    protected void doBeforePageBody() throws JspException,
            EvaluationAbortedException {
        super.doBeforePageBody();

        switch (getPhase()) {
            case HtmlPage.REGISTRATION_PHASE:
                this.completedUploaderOp = (UploaderOperation)
                        this.pageContext.getRequest().getAttribute(
                                "persistedOp");
                if (this.completedUploaderOp == null) {
                    int opId;

                    try {
                        opId = Integer.parseInt(
                                pageContext.getRequest().getParameter(
                                        "persistedOpId"));
                    } catch (NumberFormatException nfe) {
                        try {
                            ((HttpServletResponse) this.pageContext.getResponse()
                                    ).sendError(HttpServletResponse.SC_BAD_REQUEST);
                            abort();
                            return;
                        } catch (IOException ex) {
                            throw new JspException(ex);
                        }
                    }

                    try {
                        OperationPersister opPersister
                                = OperationPersister.extract(
                                        pageContext.getServletContext());
                        
                        this.completedUploaderOp = (UploaderOperation)
                                opPersister.getOperation(opId);
                        opPersister.closeOperation(
                                this.completedUploaderOp.getId());
                    } catch (IOException ex) {
                        throw new JspException(ex);
                    } catch (ResourceNotFoundException ex) {
                        throw new JspException(ex);
                    }
                }
                break;
            case HtmlPage.FETCHING_PHASE:
                // get the current version of the sample
                CoreConnector cc = CoreConnector.extract(
                        this.pageContext.getServletContext());

                try {
                    this.sampleInfo = RequestCache.getSampleInfo(
                            super.pageContext.getRequest(),
                            this.completedUploaderOp.getSampleInfo().id,
                            SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID);
                    if (this.sampleInfo == null) {
                        // cache miss
                        this.sampleInfo = cc.getSampleManager().getSampleInfo(
                                this.completedUploaderOp.getSampleInfo().id);
                        RequestCache.putSampleInfo(
                                super.pageContext.getRequest(), this.sampleInfo);
                    }
                } catch (InconsistentDbException ex) {
                    throw new JspException(ex);
                } catch (OperationFailedException ex) {
                    throw new JspException(ex);
                } catch (RemoteException ex) {
                    cc.reportRemoteException(ex);
                    throw new JspException(ex);
                }
                addToHeadContent("<meta http-equiv=\"Refresh\" content=\""
                        + this.secondsUntilRedirect + ";url="
                        + getContextPath() + getRedirectTargetPageUrl()
                        + "?sampleId=" + getSampleInfo().id + "\">");
                break;
        }
    }

    /** Implements {@code SampleContext}. */
    public SampleInfo getSampleInfo() {
        return this.sampleInfo;
    }

    /** Implements {@code MultiFilenameContext}. */
    public Collection<String> getFilenames() {
        return this.completedUploaderOp.getCompleteFilenames();
    }
}
