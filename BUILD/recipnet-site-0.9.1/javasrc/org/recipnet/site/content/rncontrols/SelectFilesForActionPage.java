/*
 * Reciprocal Net project
 * 
 * SelectFilesForActionPage.java
 *
 * 04-Aug-2005: midurbin wrote first draft
 * 11-Aug-2005: midurbin factored some ErrorSupplier implementing code out to
 *              HtmlPage
 * 11-Jan-2006: jobollin removed unused imports
 */

package org.recipnet.site.content.rncontrols;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import org.recipnet.common.controls.EvaluationAbortedException;
import org.recipnet.common.controls.HtmlPage;
import org.recipnet.site.wrapper.MultiFileOperation;
import org.recipnet.site.wrapper.OperationPersister;
import org.recipnet.site.wrapper.PersistedOperation;

/**
 * A <code>SamplePage</code> subclass that maintains a list of the
 * <code>SampleDataFiles</code> supplied to it by nested tags invoking the
 * <code>addFile()</code> method. <p>
 *
 * Furthermore, if <code>triggerRedirectToTarget()</code> is called, this page
 * will redirect to the indicated page supplying the "sampleId",
 * "sampleHistoryId" and the id (as "persistedOpId" for a new {@link
 * org.recipnet.site.wrapper.MultiFileOperation MultiFileOperation} that
 * contains references to all of the added files.
 */
public class SelectFilesForActionPage extends SamplePage {

    /**
     * An <code>ErrorSupplier</code> error flag indicating that no files were
     * selected.
     */
    public static final int NO_FILES_SELECTED
            = HtmlPage.getHighestErrorFlag() << 1;

    /** Allows subclases to extend <code>ErrorSupplier</code>. */
    protected static int getHighestErrorFlag() {
        return NO_FILES_SELECTED;
    }
    
    /**
     * Maintains a collection of filenamess that have been provided through
     * calls to {@link SelectFilesForActionPage#addFilename(String)
     * addFilename()}.
     */
    private Collection<String> filenames;

    /**
     * A URL (relative to the servlet context root) that indicates a page to
     * which this tag should redirect at the end of the
     * <code>PROCESSING_PHASE</code>.  This variable is initialized to null,
     * and as long as it is unset no redirection will occur.  {@link
     * SelectFilesForActionPage#triggerRedirectToTarget(String)
     * triggerRedirectToTarget()} may be invoked to set this variable.
     */
    private String targetExtendedOpWapPageUrl;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.filenames = new ArrayList<String>();
        this.targetExtendedOpWapPageUrl = null;
    }

    /**
     * Overrides <code>HtmlPage</code>; the current implementation
     * reinitializes 'filenames' and unsets 'targetExtendedOpWapPageUrl' then
     * delegates back to the superclass.
     */
    @Override
    protected void onReevaluation() {
        this.filenames = new ArrayList<String>();
        this.targetExtendedOpWapPageUrl = null;
        super.onReevaluation();
    }

    /**
     * Adds a filename to those 'selected' for a potential action.  The
     * supplied filename will be included in a <code>MultiFileOperation</code>
     * if one is created.
     * @param filename a filename to be selected for inclusion
     */
    public void addFilename(String filename) {
        this.filenames.add(filename);
    }

    /**
     * Supplies the URL of a page to which the browser will be redirected at
     * the end of the <code>PROCESSING_PHASE</code>.  The page must be written
     * to accept the request parameter as described in this class' main
     * description.
     */
    public void triggerRedirectToTarget(String extendedOpWapPageUrl) {
        this.targetExtendedOpWapPageUrl = extendedOpWapPageUrl;
    }

    /**
     * Overrides <code>HtmlPage</code>; the current implementation redirects
     * the browser during the <code>PROCESSING_PHASE</code> if
     * 'targetExtendedOpWapPageUrl' has been set.
     */
    @Override
    protected void doAfterPageBody() throws JspException,
            EvaluationAbortedException {
        if ((this.getPhase() == PROCESSING_PHASE)
                && (this.targetExtendedOpWapPageUrl != null)) {
            if (this.filenames.isEmpty()) {
                // end early if no files were selected
                this.setErrorFlag(NO_FILES_SELECTED);
                super.doAfterPageBody();
                return;
            }
            OperationPersister persister = OperationPersister.extract(
                    this.pageContext.getServletContext());
            long timeoutInSeconds
                    = this.pageContext.getSession().getMaxInactiveInterval();
            MultiFileOperation op
                    = new MultiFileOperation(timeoutInSeconds < 0
                        ? PersistedOperation.NO_TIME : timeoutInSeconds * 1000,
                        this.getSampleInfo(), null, this.getUserInfo().id,
                        this.filenames);
            try {
                ((HttpServletResponse)
                        this.pageContext.getResponse()).sendRedirect(
                                this.getContextPath()
                                + this.targetExtendedOpWapPageUrl
                                + "?sampleId=" + this.getSampleInfo().id
                                + "&sampleHistoryId="
                                + this.getSampleInfo().historyId
                                + "&persistedOpId="
                                + persister.registerOperation(op));
                this.abort();
            } catch (IOException ex) {
                throw new JspException(ex);
            }
        }
        super.doAfterPageBody();
    }
}
