/*
 * Reciprocal Net project
 * 
 * UploaderPage.java
 *
 * 15-Jun-2005: ekoperda wrote first draft
 * 27-Jul-2005: midurbin updated doBeforeBody() to reflect name and spec
 *              changes; updated performWorkflowAction() to throw
 *              EvaluationAbortedException
 * 04-Aug-2005: midurbin updated performWorkflowAction() to sometimes redirect
 *              based on the UploaderOperation's 'confirmationUrl'
 * 14-Jun-2006: jobollin reformatted the source
 * 03-Nov-2006: jobollin ensured that the persisted operation is always closed;
 *              removed the odd dependence of performWorkflowAction() on
 *              cancelWorkflowAction(), making it instead use
 *              redirectToEditSamplePage() and manually abort()
 * 27-Dec-2007: ekoperda fixed bug #1834 in performWorkflowAction()
 */

package org.recipnet.site.content.rncontrols;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import org.recipnet.common.controls.EvaluationAbortedException;
import org.recipnet.common.controls.HtmlPage;
import org.recipnet.site.OperationFailedException;
import org.recipnet.site.shared.logevent.SampleActionLogEvent;
import org.recipnet.site.wrapper.CoreConnector;
import org.recipnet.site.wrapper.FileTracker;
import org.recipnet.site.wrapper.LanguageHelper;
import org.recipnet.site.wrapper.UploaderOperation;
import org.recipnet.site.wrapper.WorkflowActionPersistedOperation;

/**
 * <p>
 * A JSP custom tag extended from {@code ExtendedOperationWapPage} that is
 * designed for pages that allow multi-file uploads. The central feature of
 * this tag is that it creates an {@code UploaderOperation} persisted operation
 * object and manages a reference to it, even across HTTP round-trips. (This
 * allows a single logical upload procedure to occur over several page views.)
 * </p><p>
 * A typical use would involve {@code WapSaveButton} and 
 * {@code WapCancelButton} tags nested within this one to perform workflow
 * actions, just as on any other WAP. Depending upon the outcome of any
 * user-initiated workflow actions, this tag can redirect the browser to a few
 * configurable URL's. See this tag's properties for more information.
 * </p>
 */
public class UploaderPage extends ExtendedOperationWapPage {
    
    /**
     * A reference to the web application's {@code CoreConnector} object, set
     * during {@code REGISTRATION_PHASE}.
     */
    private CoreConnector cc;

    /**
     * A reference to the web application's {@code FileTracker} object, set
     * during {@code REGISTRATION_PHASE}.
     */
    private FileTracker fileTracker;

    /**
     * A required property that identifies the page that is used to manage
     * "held" files that were uploaded but that could not be written
     * immediately to {@code RepositoryManager}. This URL should be relative to
     * the web application's context root.
     */
    private String heldFilesPageHref;

    /**
     * An optional property that identifies the page that is used to display
     * confirmation that files were uploaded. This URL should be relative to
     * the web application's context root. This property must be set in order
     * for this page to handle {@code UploaderOperation}s that have been
     * configured to end up at an upload confirmation page.
     */
    private String uploadConfirmationPageHref;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void reset() {
        super.reset();
        this.cc = null;
        this.fileTracker = null;
        this.heldFilesPageHref = null;
        this.uploadConfirmationPageHref = null;
    }

    /** simple property getter */
    public String getHeldFilesPageHref() {
        return this.heldFilesPageHref;
    }

    /** simple property setter */
    public void setHeldFilesPageHref(String heldFilesPageHref) {
        this.heldFilesPageHref = heldFilesPageHref;
    }

    /** simple property getter */
    public String getUploadConfirmationPageHref() {
        return this.uploadConfirmationPageHref;
    }

    /** simple property setter */
    public void setUploadConfirmationPageHref(String pageHref) {
        this.uploadConfirmationPageHref = pageHref;
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
                
                // Bind to some wrapper-level objects.
                this.cc = CoreConnector.extract(
                        this.pageContext.getServletContext());
                this.fileTracker = FileTracker.getFileTracker(
                        this.pageContext.getServletContext());
                break;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean performWorkflowAction() throws JspException,
            EvaluationAbortedException {
	boolean okToClosePersistedOp = true;
        try {
            UploaderOperation op = getUploaderOperation();            
            op.startManagingHeldFiles();
            if (op.isAnyFileHeld()) {
                /*
                 * One or more uploaded files got held. The user needs to
		 * manage these. Redirect the browser to the held-file
		 * management page.  Important: the persisted operation must
		 * remain open for this to work.  We cannot close it now.
                 */
		okToClosePersistedOp = false;
		super.sendRedirect(this.heldFilesPageHref, new Object[] {
			"sampleId", getSampleInfo().id,
	       	        "sampleHistoryId", getSampleInfo().historyId,
			"persistedOpId", op.getId() });
		abort();
		return false;
            } else {
                // No files are held (anymore). Wrap up the whole shebang.
                op.commitFiles();

                // Record a log event.
                this.cc.getSiteManager().recordLogEvent(
                        new SampleActionLogEvent(
                                getSampleInfo().id,
                                getSampleInfo().localLabId,
                                super.pageContext.getSession().getId(),
                                super.pageContext.getRequest().getServerName(),
                                getUserInfo().fullName,
                                LanguageHelper.extract(
                                        pageContext.getServletContext()
                                                ).getActionString(
                                        getWorkflowActionCode(),
                                        pageContext.getRequest().getLocales(),
                                        true)));

                // Redirect appropriately.
                if (op.getDisplayConfirmationPage()) {
		    // The persisted operation must remain open so that the
		    // confirmation page can be displayed.
		    okToClosePersistedOp = false;
		    super.sendRedirect(this.uploadConfirmationPageHref,
		            new Object[] { "persistedOpId", op.getId() });
                } else {
                    super.redirectToEditSamplePage();
                }
		abort();
		return true;
            }
        } catch (OperationFailedException ex) {
            throw new JspException(ex);
        } catch (IOException ex) {
            throw new JspException(ex);
        } finally {
            if (okToClosePersistedOp) {
		closePersistedOp();
	    }
        }
    }

    /**
     * Nested tags may invoke this function to obtain a reference to the
     * persisted operation.
     */
    public UploaderOperation getUploaderOperation() {
        return (UploaderOperation) getPersistedOperation();
    }

    /** Overrides {@code ExtendedOperationWapPage}. */
    @Override
    protected WorkflowActionPersistedOperation createPersistedOperation() {
        UploaderOperation op = new UploaderOperation(this.cc, this.fileTracker,
                1800000, getSampleInfo(), getUserInfo(),
                getWorkflowActionCode(), getComments(), true, false, false);

        op.startReceivingUploads();
        
        return op;
    }
}
