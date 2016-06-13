/*
 * Reciprocal Net project
 * 
 * RemoveFilesWapPage.java
 *
 * 04-Aug-2005: midurbin wrote first draft
 * 13-Jun-2006: jobollin reformatted the source
 * 03-Nov-2006: jobollin ensured that the persisted operation is closed every
 *              time
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
import org.recipnet.site.shared.bl.SampleWorkflowBL;
import org.recipnet.site.wrapper.CoreConnector;
import org.recipnet.site.wrapper.WrongSampleStateException;

/**
 * An extension of MultiFileWapPage that overrides
 * {@code performWorkflowAction()} to remove all the indicated files.
 */
public class RemoveFilesWapPage extends MultiFileWapPage {

    /** {@inheritDoc}; this version removes the selected files. */
    @Override
    protected boolean performWorkflowAction() throws JspException,
            EvaluationAbortedException {
        int actionCode = SampleWorkflowBL.FILES_REMOVED;

        if (!SampleWorkflowBL.isActionValid(getSampleInfo().status, actionCode)) {
            throw new JspException(new WrongSampleStateException(
                    getSampleInfo().status, actionCode));
        }
        String[] filenames = getFilenames().toArray(new String[0]);
        CoreConnector coreConnector = CoreConnector.extract(
                super.pageContext.getServletContext());

        // update the sample to reflect action
        SampleWorkflowBL.alterSampleForWorkflowAction(getSampleInfo(),
                actionCode);
        try {
            coreConnector.getRepositoryManager().removeDataFiles(
                    getSampleInfo(), filenames, getUserInfo().id, actionCode,
                    getComments());
            redirectToEditSamplePage();
            abort();
            return true;
        } catch (InconsistentDbException ex) {
            throw new JspException(ex);
        } catch (InvalidDataException ex) {
            throw new JspException(ex);
        } catch (OperationNotPermittedException ex) {
            throw new JspException(ex);
        } catch (RemoteException ex) {
            coreConnector.reportRemoteException(ex);
            throw new JspException(ex);
        } catch (IOException ex) {
            throw new JspException(ex);
        } catch (OperationFailedException ex) {
            throw new JspException(ex);
        } finally {
            closePersistedOp();
        }
    }
}
