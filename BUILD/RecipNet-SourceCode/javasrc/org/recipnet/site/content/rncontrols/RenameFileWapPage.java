/*
 * Reciprocal Net project
 * @(#)RenameFileWapPage.java
 *
 * 04-Aug-2005: midurbin wrote first draft
 */
package org.recipnet.site.content.rncontrols;

import java.io.IOException;
import java.rmi.RemoteException;
import javax.servlet.jsp.JspException;
import org.recipnet.common.controls.EvaluationAbortedException;
import org.recipnet.site.InconsistentDbException;
import org.recipnet.site.InvalidDataException;
import org.recipnet.site.OperationFailedException;
import org.recipnet.site.OperationNotPermittedException;
import org.recipnet.site.core.DuplicateDataException;
import org.recipnet.site.core.RepositoryDirectoryNotFoundException;
import org.recipnet.site.core.ResourceNotAccessibleException;
import org.recipnet.site.core.ResourceNotFoundException;
import org.recipnet.site.core.WrongSiteException;
import org.recipnet.site.shared.bl.SampleWorkflowBL;
import org.recipnet.site.wrapper.CoreConnector;
import org.recipnet.site.wrapper.WrongSampleStateException;

/**
 * An extension of <code>FileWapPage</code> that overrides
 * <code>performWorkflowAction()</code> to rename the indicated file. <p>
 *
 * The proposed new filename must be provided through the method
 * <code>setNewFileName()</code>.  This may be done by a special purpose nested
 * tag such as {@link RenameFileNewFileName RenameFileNewFileName}.
 */
public class RenameFileWapPage extends FileWapPage {

    /** The suggested new name for the file. */
    private String newFileName;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.newFileName = null;
    }

    /** 
     * A method to allow nested tags or subclasses to set the new filename
     * for the file upon which the <code>FILE_RENAMED</code> action will occur.
     */
    public void setNewFileName(String newName) {
        this.newFileName = newName;
    }

    /**
     * Renames the file provided by this page's <code>FileContext</code>
     * implementation then redirects the the page indicated by the
     * 'editSamplePage' property.
     */
    @Override
    protected boolean performWorkflowAction() throws JspException,
            EvaluationAbortedException {
        int actionCode = SampleWorkflowBL.FILE_RENAMED;

        if (!this.areAllFieldsValid()) {
            this.setErrorFlag(NESTED_TAG_REPORTED_VALIDATION_ERROR);
            return false;
        }
        if (this.newFileName.equals(this.getSampleDataFile().getName())) {
            // the rename operation would have no effect, so silently ignore it
            try {
                this.redirectToEditSamplePage();
                this.abort();
            } catch (IOException ex) {
                throw new JspException(ex);
            }
        }

        if (!SampleWorkflowBL.isActionValid(this.getSampleInfo().status,
                actionCode)) {
            throw new JspException(new WrongSampleStateException(
                    this.getSampleInfo().status, actionCode));
        }

        // update the sample to reflect action
        SampleWorkflowBL.alterSampleForWorkflowAction(this.getSampleInfo(),
                actionCode);
        CoreConnector coreConnector = CoreConnector.extract(
                super.pageContext.getServletContext());
        try {
            coreConnector.getRepositoryManager().renameDataFile(
                    this.getSampleInfo(), this.getSampleDataFile().getName(),
                    this.newFileName, this.getUserInfo().id,
                    this.getComments());
            this.redirectToEditSamplePage();
            this.abort();
            return true;
        } catch (DuplicateDataException ex) {
            throw new JspException(ex);
        } catch (InconsistentDbException ex) {
            throw new JspException(ex);
        } catch (InvalidDataException ex) {
            throw new JspException(ex);
        } catch (WrongSiteException ex) {
            throw new JspException(ex);
        } catch (OperationNotPermittedException ex) {
            throw new JspException(ex);
        } catch (RemoteException ex) {
            coreConnector.reportRemoteException(ex);
            throw new JspException(ex);
        } catch (IOException ex) {
            throw new JspException(ex);
        } catch (RepositoryDirectoryNotFoundException ex) {
            throw new JspException(ex);
        } catch (ResourceNotAccessibleException ex) {
            throw new JspException(ex);
        } catch (ResourceNotFoundException ex) {
            throw new JspException(ex);
        } catch (OperationFailedException ex) {
            throw new JspException(ex);
        }
    }
}
