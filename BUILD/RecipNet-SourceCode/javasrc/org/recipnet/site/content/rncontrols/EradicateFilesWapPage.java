/*
 * Reciprocal Net project
 * 
 * EradicateFilesWapPage.java
 *
 * 04-Aug-2005: midurbin wrote first draft
 * 13-Jun-2006: jobollin reformatted the source
 * 03-Nov-2006: jobollin ensured that the persisted operation is closed in the
 *              case of an exception
 */

package org.recipnet.site.content.rncontrols;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Collections;

import javax.servlet.jsp.JspException;

import org.recipnet.common.controls.EvaluationAbortedException;
import org.recipnet.site.RecipnetException;
import org.recipnet.site.shared.bl.SampleWorkflowBL;
import org.recipnet.site.wrapper.CoreConnector;
import org.recipnet.site.wrapper.WrongSampleStateException;

/**
 * An extension of MultiFileWapPage that overrides
 * {@code performWorkflowAction()} to eradicate all the indicated files.
 */
public class EradicateFilesWapPage extends MultiFileWapPage {

    /**
     * Overrides {@code WapPage} to eradicate the selected files and redirect to
     * the edit sample page.
     */
    @Override
    protected boolean performWorkflowAction() throws JspException,
            EvaluationAbortedException {
        int actionCode = SampleWorkflowBL.FILES_ERADICATED;
        
        if (!SampleWorkflowBL.isActionValid(getSampleInfo().status, actionCode)) {
            throw new JspException(new WrongSampleStateException(
                    getSampleInfo().status, actionCode));
        }

        String[] filenames = Collections.list(
                Collections.enumeration(getFilenames())).toArray(new String[0]);

        // update the sample to reflect action
        SampleWorkflowBL.alterSampleForWorkflowAction(getSampleInfo(),
                actionCode);
        CoreConnector coreConnector
                = CoreConnector.extract(super.pageContext.getServletContext());
        
        try {
            coreConnector.getRepositoryManager().eradicateDataFiles(
                    getSampleInfo(), filenames, getUserInfo().id, actionCode,
                    getComments());
            redirectToEditSamplePage();
            abort();
            
            return true;
        } catch (RecipnetException re) {
            throw new JspException(re);
        } catch (RemoteException ex) {
            coreConnector.reportRemoteException(ex);
            throw new JspException(ex);
        } catch (IOException ex) {
            throw new JspException(ex);
        } finally {
            closePersistedOp();
        }
    }
}
