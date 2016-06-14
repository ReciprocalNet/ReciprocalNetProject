/*
 * Reciprocal Net project
 * 
 * FileWapPage.java
 *
 * 04-Aug-2005: midurbin wrote first draft
 * 20-Jan-2006: jobollin updated docs; reformatted the source; removed unused
 *              imports
 */

package org.recipnet.site.content.rncontrols;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import org.recipnet.common.controls.EvaluationAbortedException;
import org.recipnet.common.controls.HtmlPage;
import org.recipnet.site.InconsistentDbException;
import org.recipnet.site.OperationFailedException;
import org.recipnet.site.core.RepositoryDirectoryNotFoundException;
import org.recipnet.site.core.RepositoryManagerRemote;
import org.recipnet.site.core.WrongSiteException;
import org.recipnet.site.shared.RepositoryFiles;
import org.recipnet.site.shared.SampleDataFile;
import org.recipnet.site.wrapper.CoreConnector;
import org.recipnet.site.wrapper.RequestCache;

/**
 * An abstract extension of {@code WapPage} that allows for subclasses to
 * exponse workflow actions that are performed involving a specific file. This
 * tag recognizes a request parameter with the name given as the
 * 'fileNameParamName' property, whose value is used as the filename for the
 * file involved in this action. This file is provided through the
 * {@code FileContext} implementation.
 */
public abstract class FileWapPage extends WapPage implements FileContext {

    /**
     * A required property indicating the name of the request parameter that is
     * expected to contain the filename for a file that is to be included in the
     * action.
     */
    private String fileNameParamName;

    /**
     * The {@code SampleDataFile} involved in the workflow action performed by
     * this page
     */
    private SampleDataFile activeFile;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.fileNameParamName = null;
        this.activeFile = null;
    }

    /**
     * Retrieves the value of the 'fileNameParamName' property
     * 
     * @return the {@code String} representing the request parameter name from
     *         which the name of the file involved in this action should be
     *         obtained 
     */
    public String getFileNameParamName() {
        return this.fileNameParamName;
    }

    /**
     * Sets the value of the 'fileNameParamName' property
     * 
     * @param paramName a {@code String} representing the request parameter name
     *        from which the name of the file involved in this action should be
     *        obtained
     */
    public void setFileNameParamName(String paramName) {
        this.fileNameParamName = paramName;
    }

    /**
     * {@inheritDoc}
     * 
     * @throws IllegalStateException if called before the {@code FETCHING_PHASE}
     */
    public SampleDataFile getSampleDataFile() {
        if ((this.getPhase() == HtmlPage.REGISTRATION_PHASE)
                || (this.getPhase() == HtmlPage.PARSING_PHASE)) {
            throw new IllegalStateException();
        }
        return this.activeFile;
    }

    /**
     * Overrides {@code HtmlPage}; the current implementation fetches the file
     * indicated by the 'fileNameParamName' property.
     * 
     * @throws IllegalArgumentException if there is no file with the given
     *         filename
     */
    @Override
    protected void doBeforePageBody() throws JspException,
            EvaluationAbortedException {
        super.doBeforePageBody();
        switch (this.getPhase()) {
            case HtmlPage.FETCHING_PHASE:
                HttpServletRequest request
                        = (HttpServletRequest) this.pageContext.getRequest();
                String filename = request.getParameter(this.fileNameParamName);
                this.addFormField(this.fileNameParamName, filename);

                // Get the RepositoryFiles
                RepositoryFiles rf = RequestCache.getRepositoryFiles(
                        super.pageContext.getRequest(),
                        this.getSampleInfo().id,
                        this.getSampleInfo().historyId);

                // Cache miss; fetch a RepositoryFiles object from
                // RepositoryManager.
                CoreConnector cc = CoreConnector.extract(
                        super.pageContext.getServletContext());
                try {
                    RepositoryManagerRemote repositoryManager
                            = cc.getRepositoryManager();
                    rf = repositoryManager.getRepositoryFiles(
                            this.getSampleInfo().id,
                            this.getSampleInfo().historyId, false);
                    RequestCache.putRepositoryFiles(
                            super.pageContext.getRequest(), rf);
                    this.activeFile = rf.getRecordWithName(filename);
                    if (this.activeFile == null) {
                        throw new IllegalArgumentException();
                    }
                } catch (RemoteException ex) {
                    cc.reportRemoteException(ex);
                    throw new JspException(ex);
                } catch (RepositoryDirectoryNotFoundException ex) {
                    throw new JspException(ex);
                } catch (InconsistentDbException ex) {
                    throw new JspException(ex);
                } catch (OperationFailedException ex) {
                    throw new JspException(ex);
                } catch (WrongSiteException ex) {
                    throw new JspException(ex);
                }
                break;
        }
    }
}
