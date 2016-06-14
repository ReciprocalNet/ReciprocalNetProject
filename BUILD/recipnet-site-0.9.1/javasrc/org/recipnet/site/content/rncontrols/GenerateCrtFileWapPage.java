/*
 * Reciprocal Net Project
 *
 * GenerateCrtFileWapPage.java
 *
 * 26-Jan-2006: jobollin wrote first draft
 */

package org.recipnet.site.content.rncontrols;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.rmi.RemoteException;

import javax.servlet.jsp.JspException;

import org.recipnet.common.controls.EvaluationAbortedException;
import org.recipnet.common.controls.HtmlPage;
import org.recipnet.site.InconsistentDbException;
import org.recipnet.site.InvalidDataException;
import org.recipnet.site.OperationFailedException;
import org.recipnet.site.OperationNotPermittedException;
import org.recipnet.site.shared.bl.SampleWorkflowBL;
import org.recipnet.site.shared.db.SampleInfo;
import org.recipnet.site.wrapper.CoreConnector;
import org.recipnet.site.wrapper.FileTracker;
import org.recipnet.site.wrapper.RepositoryFileOutputStream;
import org.recipnet.site.wrapper.TrackedFile;

/**
 * A custom {@code WapPage} that is specially tooled to support CRT file
 * generation actions.  It does not perform the actual CRT generation work
 * itself, but instead copies the result of another processor's generated CRT
 * from a webapp-side temporary file into the repository.
 * 
 * @author jobollin
 * @version 1.0
 */
public class GenerateCrtFileWapPage extends FileWapPage {
    
    /**
     * An {@code ErrorSupplier} error code indicating that no workflow action
     * could be performed by this page because no CRT file was provided to it.
     * This would be the case if the {@code crtFileKey} attribute were not set,
     * or if it wasn't the key to a {@code FileTracker}-tracked file (including
     * if the file had been dropped from FileTracker's cache before the action
     * was requested). 
     */
    public final static int NO_CRT_FILE
            = FileWapPage.getHighestErrorFlag() << 1;
    
    /**
     * A required tag attribute designating the name of the PDB file to create
     * or replace; not validated at this level by this page.  For best user
     * experience it is recommended that the value be exposed and validated by
     * a control within the page. 
     */
    private String crtFileName;
    
    /**
     * A required tag attribute specifying a FileTracker key referencing a CRT
     * file to copy into the repository when this page's workflow action is
     * performed
     */
    private Long crtFileKey;
    
    /**
     * The user-supplied description to be associated with the PDB file
     * generated by this page.  This property is not exposed as a tag attribute;
     * it is expected that the page body will set it as necessary
     */
    private String fileDescription;
    
    /**
     * {@inheritDoc}
     * 
     * @see FileWapPage#reset()
     */
    @Override
    protected void reset() {
        super.reset();
        crtFileName = null;
        fileDescription = null;
    }

    /**
     * {@inheritDoc}.  This version then exposes this tag as a scripting
     * variable 'crtPage', to which the TLD assigns an appropriate type.
     *
     * @see RecipnetPage#doStartTag()
     */
    @Override
    public int doStartTag() throws JspException {
        int rc = super.doStartTag();
        
        pageContext.setAttribute("crtPage", this);
        
        return rc;
    }

    /**
     * {@inheritDoc}.  This version removes the 'crtPage' scripting variable
     * before delegating to the superclass.
     * 
     * @see HtmlPage#doEndTag()
     */
    @Override
    public int doEndTag() throws JspException {
        pageContext.removeAttribute("crtPage");
        
        return super.doEndTag();
    }

    /**
     * Retrieves the CRT file name configured on this page
     * 
     * @return the CRT file name as a {@code String}
     */
    public String getCrtFileName() {
        return crtFileName;
    }

    /**
     * Sets the name of the CRT file to be generated by this page
     * 
     * @param  crtFileName the file name as a {@code String}
     */
    public void setCrtFileName(String crtFileName) {
        this.crtFileName = crtFileName;
    }

    /**
     * Gets the {@code FileTracker} key for the CRT file to record
     * 
     * @return the key as a {@code long}
     */
    public Long getCrtFileKey() {
        return crtFileKey;
    }

    /**
     * Sets the {@code FileTracker} key for the CRT file to record
     * 
     * @param  crtFileKey the key as a {@code long}
     */
    public void setCrtFileKey(Long crtFileKey) {
        this.crtFileKey = crtFileKey;
    }

    /**
     * Retrieves the file description currently configured on this page
     * 
     * @return the file description as a {@code String}
     */
    public String getFileDescription() {
        return fileDescription;
    }

    /**
     * Sets the file description for the PDB file to be created by this page
     * 
     * @param  fileDescription a {@code String} containing the file description
     */
    public void setFileDescription(String fileDescription) {
        this.fileDescription = fileDescription;
    }

    /**
     * Returns the workflow action code for the action to be performed by this
     * WAP; one of {@code SampleWorkflowBL.FILE_ADDED} and
     * {@code SampleWorkflowBL.SUBSTITUTE_FILE_ADDED_OR_FILE_REPLACED}.  The
     * specific code depends on whether this page is configured to replace
     * existing files, which may vary from phase to phase.
     * 
     * @return the workflow action code for this page
     * 
     * @see WapPage#getWorkflowActionCode()
     */
    @Override
    public int getWorkflowActionCode() {
        return SampleWorkflowBL.SUBSTITUTE_FILE_ADDED_OR_FILE_REPLACED;
    }

    /**
     * {@inheritDoc}.  This version always throws an
     * {@code IllegalStateException} because the workflow action code cannot be
     * configured directly.
     *  
     * @see WapPage#setWorkflowActionCode(int)
     */
    @Override
    public void setWorkflowActionCode(@SuppressWarnings("unused") int code) {
        throw new IllegalStateException(
                "Cannot set the workflow action code directly");
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.recipnet.site.content.rncontrols.WapPage#onReevaluation()
     */
    @Override
    protected void onReevaluation() {
        fileDescription = null;
        
        super.onReevaluation();
    }

    /**
     * {@inheritDoc}.  This version records a generated CRT file into the file
     * repository. 
     * 
     * @see FileWapPage#performWorkflowAction()
     */
    @Override
    protected boolean performWorkflowAction() throws JspException,
            EvaluationAbortedException {
        if (areAllFieldsValid()) {
            Long key = getCrtFileKey();
            
            if (key == null) {
                setErrorFlag(NO_CRT_FILE);
                return false;
            }
            
            CoreConnector cc
                    = CoreConnector.extract(pageContext.getServletContext());
            FileTracker fileTracker
                  = FileTracker.getFileTracker(pageContext.getServletContext());

            try {
                TrackedFile trackedFile = fileTracker.getTrackedFile(key); 
                
                if ((trackedFile == null) || !trackedFile.isValid()) {
                    setErrorFlag(NO_CRT_FILE);
                    return false;
                }

                /*
                 * Copy the specified file from FileTracker's cache to the
                 * repository
                 */
                int ticket = cc.getRepositoryManager().beginWritingDataFile(
                        getSampleInfo(), getCrtFileName(), true, true,
                        getWorkflowActionCode(), getUserInfo().id,
                        getComments(), getProcessedFileDescription());
                OutputStream out = new RepositoryFileOutputStream(cc, ticket);

                try {
                    byte[] buffer = new byte[4096];
                    InputStream in = new FileInputStream(
                         fileTracker.getTrackedFile(getCrtFileKey()).getFile());

                    try {
                        for (int bytesRead = in.read(buffer);
                                bytesRead >= 0;
                                bytesRead = in.read(buffer)) {
                            out.write(buffer, 0, bytesRead);
                        }
                    } finally {
                        try {
                            in.close();
                        } catch (IOException ioe) {
                            // ignore it
                        }
                    }
                    out.flush();
                } finally {
                    try {
                        out.close();
                    } catch (IOException ioe) {
                        // ignore it
                    }
                }
                
                updateSampleInfo(cc.getSampleManager().getSampleInfo(
                        this.getSampleInfo().id));
                
                redirectToEditSamplePage();
                abort();
                
                // Never reached: abort() always throws an exception 
                return true;
            } catch (RemoteException re) {
                cc.reportRemoteException(re);
                throw new JspException(re);
            } catch (IOException ioe) {
                throw new JspException(ioe);
            } catch (InvalidDataException ide) {
                throw new JspException(ide);
            } catch (OperationFailedException ofe) {
                throw new JspException(ofe);
            } catch (OperationNotPermittedException onpe) {
                throw new JspException(onpe);
            } catch (InconsistentDbException ide) {
                throw new JspException(ide);
            }
        }
        
        return false;
    }

    /**
     * Returns a value of the file description that has been massaged for
     * storage in the DB.  Specifically, if the configured file description is
     * {@code null}, blank, or empty then this method returns {@code null};
     * otherwise, it returns the trimmed value of the configured description.
     * 
     * @return the file description {@code String}, processed as described
     */
    private String getProcessedFileDescription() {
        String description = getFileDescription();
        
        if (description != null) {
            description = description.trim();
        }
        
        return "".equals(description) ? null : description;
    }

    /**
     * {@inheritDoc}.  This version updates the 'sampleHistoryId' form field
     * to reflect the updated sample
     * 
     * @see SamplePage#updateSampleInfo(SampleInfo)
     */
    @Override
    protected void updateSampleInfo(SampleInfo updatedSampleInfo) {
        super.updateSampleInfo(updatedSampleInfo);
        this.addFormField("sampleHistoryId",
                String.valueOf(updatedSampleInfo.historyId));
    }
}