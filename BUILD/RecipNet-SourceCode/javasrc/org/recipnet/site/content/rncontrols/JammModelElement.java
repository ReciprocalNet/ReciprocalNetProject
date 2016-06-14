/*
 * Reciprocal Net Project
 *
 * JammModelElement.java
 *
 * 03-Feb-2006: jobollin wrote first draft
 */

package org.recipnet.site.content.rncontrols;

import java.rmi.RemoteException;

import javax.servlet.jsp.JspException;

import org.recipnet.common.controls.ErrorSupplier;
import org.recipnet.common.controls.HtmlPageElement;
import org.recipnet.site.InconsistentDbException;
import org.recipnet.site.OperationFailedException;
import org.recipnet.site.core.RepositoryDirectoryNotFoundException;
import org.recipnet.site.core.RepositoryManagerRemote;
import org.recipnet.site.core.WrongSiteException;
import org.recipnet.site.shared.RepositoryFiles;
import org.recipnet.site.shared.SampleDataFile;
import org.recipnet.site.shared.RepositoryFiles.Record;
import org.recipnet.site.shared.db.SampleInfo;
import org.recipnet.site.shared.db.UserInfo;
import org.recipnet.site.wrapper.CoreConnector;
import org.recipnet.site.wrapper.RequestCache;

/**
 * A phase-recognizing tag that specifies the model that should be displayed by
 * a JammAppletTag(s) contained within.  It performs non-trivial work only
 * during the {@code FETCHING_PHASE}.  Normally the URL itself or the name of a
 * repository file would be specified via the 'modelUrl' or the 'repositoryFile'
 * attribute, respectively, but if neither is specified then an attempt is made
 * to choose an appropriate file for the surrounding sample context.  Only one
 * of those attributes should be given.  This tag must be nested in a sample
 * context and a user context <strong>unless</strong> the 'modelUrl' attribute
 * is specified. 
 * 
 * @author jobollin
 * @version 0.9.0
 */
public class JammModelElement extends HtmlPageElement implements ErrorSupplier {

    /**
     * An error flag that may be reported when the repository directory is not
     * found when trying to retrieve files for this sample. This is used in the
     * implementation of {@code ErrorSupplier}.
     */
    public static final int REPOSITORY_DIRECTORY_NOT_FOUND = 1 << 0;

    /**
     * An error flag that may be reported when there is no CRT file associated
     * with this sample. This is used in the implementation of
     * {@code ErrorSupplier}.
     */
    public static final int NO_CRT_FILE_AVAILABLE = 1 << 1;

    /**
     * Keeps track of the specific errors detected while parsing the input for
     * this control. Error code will be the logical OR of one or more of the
     * errors codes defined by this class or its subclass or may be modified by
     * {@code parseValue()}. This field is used in the implementation of
     * {@code ErrorSupplier}.
     */
    private int errorCode;

    /**
     * Optional attribute, defaults to null; specifies the name of a repository
     * file to display.  This is a transient property.
     */
    private String repositoryFile;
    
    /**
     * Optional attribute, defaults to null; specifies an arbitrary URL for the
     * CRT file to display.  This is a transient property.  The user should be
     * aware that if the model URL does not correspond to a CRT file from the
     * repository directory corresponding to the surrounding sample context,
     * then JaMM1 and JaMM2's image generation facilities will not work.  If the
     * CRT to be rendered is in the repository, then it should be specified via
     * the 'repositoryFile' property. 
     */
    private String modelUrl;
    
    /**
     * The model URL that nested {@code JammAppletTag}s should use, as
     * determined during the {@code RENDERING_PHASE} before body evaluation.
     */
    private String effectiveModelUrl;

    /**
     * {@inheritDoc}
     * 
     * @see HtmlPageElement#reset()
     */
    @Override
    protected void reset() {
        super.reset();
        errorCode = ErrorSupplier.NO_ERROR_REPORTED;
        repositoryFile = null;
        modelUrl = null;
        effectiveModelUrl = null;
    }

    /**
     * Returns the 'repositoryFile' property of this tag
     * 
     * @return the 'repositoryFile' property of this tag, as a {@code String},
     *         or {@code null} if none has been assigned
     */
    public String getRepositoryFile() {
        return repositoryFile;
    }

    /**
     * Sets the 'repositoryFile' property of this tag to the specified
     * {@code String}
     * 
     * @param  repositoryFile the {@code String} to set as the 'repositoryFile'
     *         property
     */
    public void setRepositoryFile(String repositoryFile) {
        this.repositoryFile = repositoryFile;
    }

    /**
     * Returns the 'modelUrl' property of this tag
     * 
     * @return the 'modelUrl' property of this tag, as a {@code String},
     *         or {@code null} if none has been assigned
     */
    public String getModelUrl() {
        return modelUrl;
    }

    /**
     * Sets the 'modelUrl' property of this tag to the specified {@code String}
     * 
     * @param  modelUrl the {@code String} to set as the 'modelUrl'
     *         property
     */
    public void setModelUrl(String modelUrl) {
        this.modelUrl = modelUrl;
    }

    /**
     * Returns the model URL that nested applet tags should use
     * 
     * @return the model URL {@code String} that nested applet tags should use,
     *         whether explicitly set on this tag or computed by it; will be
     *         {@code null} before the {@code RENDERING_PHASE}
     */
    public String getEffectiveModelUrl() {
        return effectiveModelUrl;
    }

    /**
     * {@inheritDoc}.  This version determines the actual model URL that nested
     * applet tags should use
     * 
     * @see HtmlPageElement#onFetchingPhaseBeforeBody()
     */
    @Override
    public int onFetchingPhaseBeforeBody() throws JspException {
        int rc = super.onFetchingPhaseBeforeBody();
        
        effectiveModelUrl = getModelUrl();
        if (effectiveModelUrl == null) {

            // Generate a URL for a repository file
            try {
                effectiveModelUrl = generateModelUrl();
                if (effectiveModelUrl == null) {
                    
                    // We were unable to find a suitable file.
                    setErrorFlag(NO_CRT_FILE_AVAILABLE);
                }
            } catch (RepositoryDirectoryNotFoundException ex) {
                setErrorFlag(REPOSITORY_DIRECTORY_NOT_FOUND);
            }
        }
        
        return rc;
    }

    /**
     * Generates a model file URL based on the requested model repository file
     * name, if any, and the available repository files pertaining to the sample
     * context applicable to this tag.
     * 
     * @return a {@code String} containing the URL of a repository file to use
     *         as the JaMM model, or {@code null} if no such URL can be
     *         generated (presumably because no suitable file is present in the
     *         repository)
     *
     * @throws IllegalStateException if this tag is not nested in a sample
     *         context and user context
     * @throws RepositoryDirectoryNotFoundException if there is no repository
     *         directory available for the sample reflected by the containing
     *         sample context  
     * @throws JspException if any other checked exception occurs
     */
    private String generateModelUrl() throws JspException,
            RepositoryDirectoryNotFoundException {
        
        // get sample context
        SampleContext sampleContext
                = findRealAncestorWithClass(this, SampleContext.class);
        if (sampleContext == null) {
            throw new IllegalStateException();
        }
        
        // get user context
        UserContext userContext
                = findRealAncestorWithClass(this, UserContext.class);
        if (userContext == null) {
            throw new IllegalStateException();
        }

        CoreConnector coreConnector
                = CoreConnector.extract(pageContext.getServletContext());
        RepositoryFiles repositoryFiles;
        Integer ticketNum;
        SampleDataFile selectedDataFile;

        // get the repository files object and ticket number from core or cache
        try {
            SampleInfo sampleInfo = sampleContext.getSampleInfo();
            RepositoryManagerRemote repositoryManager
                    = coreConnector.getRepositoryManager();

            // Get the RepositoryFiles
            repositoryFiles = RequestCache.getRepositoryFiles(
                    this.pageContext.getRequest(), sampleInfo.id,
                    sampleInfo.historyId);
            if (repositoryFiles == null) {
                repositoryFiles = repositoryManager.getRepositoryFiles(
                        sampleInfo.id, sampleInfo.historyId, false);
                RequestCache.putRepositoryFiles(this.pageContext.getRequest(),
                        repositoryFiles);
            }

            // get the ticket
            ticketNum = RequestCache.getTicket(this.pageContext.getRequest(),
                    repositoryFiles);
            if (ticketNum == null) {
                UserInfo user = userContext.getUserInfo();

                ticketNum = Integer.valueOf(repositoryManager.grantNewTicket(
                        repositoryFiles,
                        ((user == null) ? UserInfo.INVALID_USER_ID : user.id)));
                RequestCache.putTicket(this.pageContext.getRequest(),
                        repositoryFiles, ticketNum);
            }
        } catch (RemoteException ex) {
            coreConnector.reportRemoteException(ex);
            throw new JspException(ex);
        } catch (InconsistentDbException ex) {
            throw new JspException(ex);
        } catch (WrongSiteException ex) {
            throw new JspException(ex);
        } catch (RepositoryDirectoryNotFoundException rdnfe) {
            
            // We want to rethrow this particular OperationFailedException
            throw rdnfe;
        } catch (OperationFailedException ex) {
            throw new JspException(ex);
        }

        /*
         * Select one of the sample data files for display via a prioritization
         * algorithm
         */

        selectedDataFile = null;
        
        // Try the requested file name, if any
        if (getRepositoryFile() != null) {
            selectedDataFile
                    = repositoryFiles.getRecordWithName(getRepositoryFile());
        }

        // If necessary, try a file name derived from the sample's localLabId
        if (selectedDataFile == null) {
            selectedDataFile = repositoryFiles.getRecordWithName(
                    sampleContext.getSampleInfo().localLabId + ".crt");
        }

        // If necessary, look for the first file name ending with ".crt"
        if (selectedDataFile == null) {
            for (Record repositoryRecord : repositoryFiles.getRecords()) {
                if (repositoryRecord.getName().toLowerCase().endsWith(".crt")) {
                    selectedDataFile = repositoryRecord;
                    break;
                }
            }
        }

        return ((selectedDataFile == null) ? null
                : (selectedDataFile.getUrl() + "?ticket=" + ticketNum));
    }

    /**
     * {@inheritDoc}
     * 
     * @see HtmlPageElement#copyTransientPropertiesFrom(HtmlPageElement)
     */
    @Override
    protected void copyTransientPropertiesFrom(HtmlPageElement source) {
        JammModelElement copyFrom = (JammModelElement) source;
        
        super.copyTransientPropertiesFrom(source);
        this.setRepositoryFile(copyFrom.getRepositoryFile());
        this.setModelUrl(copyFrom.getModelUrl());
    }

    /**
     * {@inheritDoc}
     */
    public int getErrorCode() {
        return errorCode;
    }

    /**
     * {@inheritDoc}
     */
    public void setErrorFlag(int errorFlag) {
        this.errorCode |= errorFlag;
    }

    /**
     * Returns the numerically largest error code defined by this
     * {@code ErrorSupplier}, to facilitate subclasses defining their own error
     * codes
     * 
     * @return the numerically greatest error code implemented by this error
     *         supplier
     */
    protected static int getHighestErrorFlag() {
        return NO_CRT_FILE_AVAILABLE;
    }
}
