/*
 * Reciprocal Net Project
 *
 * CifFileContext.java
 *
 * 26-Jan-2006: jobollin wrote first draft
 * 13-Mar-2006: jobollin fixed bug #1762 in getFileName()
 * 28-Apr-2006: jobollin added error code CIF_HAS_ERRORS and java code to
 *              raise it when necessary
 */

package org.recipnet.site.content.rncontrols;

import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.Map;

import javax.servlet.jsp.JspException;

import org.recipnet.common.controls.ErrorSupplier;
import org.recipnet.common.controls.HtmlPageElement;
import org.recipnet.common.files.CifFile;
import org.recipnet.common.files.cif.CifError;
import org.recipnet.common.files.cif.CifErrorHandler;
import org.recipnet.common.files.cif.CifParseException;
import org.recipnet.common.files.cif.CifParser;
import org.recipnet.common.files.cif.CifWarning;
import org.recipnet.site.InconsistentDbException;
import org.recipnet.site.OperationFailedException;
import org.recipnet.site.core.ResourceException;
import org.recipnet.site.shared.SampleDataFile;
import org.recipnet.site.shared.db.SampleInfo;
import org.recipnet.site.shared.db.UserInfo;
import org.recipnet.site.wrapper.CoreConnector;
import org.recipnet.site.wrapper.RepositoryFileInputStream;

/**
 * A tag that provides a parsed {@link CifFile} representation of a specified
 * CIF.  This tag must be nested within a
 * {@code SampleContext}.  If nested within a {@code UserContext} then it will
 * use the permissions assigned to the user in reading data from the repository.
 * The file to provide can be specified explicitly by assigning a
 * {@code FileContext} property to this tag through its attributes; otherwise it
 * must be nested in a {@code FileContext} from which it will determine what
 * file to parse.
 *  
 * @author jobollin
 * @version 1.0
 */
public class CifFileContext extends HtmlPageElement implements ErrorSupplier {
    
    /**
     * An error flag indicating that the file represented by this context could
     * not be parsed as a CIF
     */
    public final static int UNPARSEABLE_CIF
            = SimpleFileContext.getHighestErrorFlag() << 1;
    
    /**
     * An error flag indicating that the file represented by this context was
     * parsed as a CIF, but contained errors
     */
    public final static int CIF_HAS_ERRORS
            = SimpleFileContext.getHighestErrorFlag() << 2;

    /**
     * An error flag indicating that the file represented by this context was
     * parsed as a CIF, but contained no data blocks
     */
    public final static int EMPTY_CIF
            = SimpleFileContext.getHighestErrorFlag() << 3;
    
    /**
     * An error flag indicating that there is no file available to parse
     */
    public final static int NO_FILE_SPECIFIED
            = SimpleFileContext.getHighestErrorFlag() << 4;
    
    /**
     * The explicitly-specified {@code FileContext} describing the CIF file of
     * which this tag provides a parsed representation
     */
    private FileContext fileContext;
    
    /**
     * The {@code CifFile} resulting from parsing the file represented by this
     * context
     */
    private CifFile cifFile;

    /**
     * The current error code set on this {@code ErrorSupplier}
     */
    private int errorCode;

    /**
     * {@inheritDoc}.  This version retrieves the context's file and attempts to
     * parse it as a CIF, provided that the superclass does not set any error
     * flags.
     * 
     * @see SimpleFileContext#onFetchingPhaseBeforeBody()
     */
    @Override
    public int onFetchingPhaseBeforeBody() throws JspException {
        int rc = super.onFetchingPhaseBeforeBody();
        String fileName = getFileName();

        if (fileName == null) {
            setErrorFlag(NO_FILE_SPECIFIED);
        } else {
            CoreConnector connector
                    = CoreConnector.extract(pageContext.getServletContext());
            SampleContext sc
                    = findRealAncestorWithClass(this, SampleContext.class);

            if (sc == null) {
                throw new IllegalStateException("No sample context");
            }

            SampleInfo sample = sc.getSampleInfo();
            UserContext uc = findRealAncestorWithClass(this, UserContext.class);
            UserInfo user = ((uc == null) ? null : uc.getUserInfo());
            int userId = ((user == null) ? UserInfo.INVALID_USER_ID : user.id);

            try {
                int ticket
                        = connector.getRepositoryManager().beginReadingDataFile(
                                    sample.id, sample.historyId, fileName,
                                    userId);
                InputStream cifInput
                        = new RepositoryFileInputStream(connector, ticket);

                try {
                    CifParser parser = new CifParser();
                    
                    parser.setErrorHandler(new ErrorHandler());
                    cifFile = parser.parseCif(cifInput);
                    if (!cifFile.blockIterator().hasNext()) {
                        setErrorFlag(EMPTY_CIF);
                    }
                } catch (CifParseException cpe) {
                    setErrorFlag(UNPARSEABLE_CIF);
                } finally {
                    try {
                        cifInput.close();
                    } catch (IOException ioe) {
                        // do nothing
                    }
                }
            } catch (ResourceException re) {
                throw new JspException(re);
            } catch (InconsistentDbException idbe) {
                throw new JspException(idbe);
            } catch (OperationFailedException ofe) {
                throw new JspException(ofe);
            } catch (RemoteException re) {
                connector.reportRemoteException(re);
                throw new JspException(re);
            } catch (IOException ioe) {
                throw new JspException(ioe);
            }
        }
        
        return rc;
    }

    /**
     * {@inheritDoc}
     * 
     * @see SimpleFileContext#reset()
     */
    @Override
    protected void reset() {
        super.reset();
        
        fileContext = null;
        cifFile = null;
    }

    /**
     * Retrieves the {@code FileContext} explicitly set on this tag, if any
     * 
     * @return the {@code FileContext}
     */
    public FileContext getFileContext() {
        return fileContext;
    }

    /**
     * Explicitly sets the {@code FileContext} for this tag handler to use
     * during this evaluation; if not set then this handler uses the innermost
     * surrounding file context
     * 
     * @param  fileContext the {@code FileContext} to assign
     */
    public void setFileContext(FileContext fileContext) {
        this.fileContext = fileContext;
    }

    /**
     * Retrieves the {@code CifFile} property of this CIF file context.
     * 
     * @return the {@code CifFile}; {@code null} before the
     *         {@code FETCHING_PHASE} or if the specified file is unavailable
     *         or not a valid CIF
     */
    public CifFile getCifFile() {
        return cifFile;
    }
    
    /**
     * {@inheritDoc}.
     * 
     * @see SimpleFileContext#generateCopy(String, Map)
     */
    @Override
    protected HtmlPageElement generateCopy(String newId, Map origToCopyMap) {
        CifFileContext cfe
                = (CifFileContext) super.generateCopy(newId, origToCopyMap);
        
        this.fileContext = (FileContext) origToCopyMap.get(cfe.fileContext);
        
        return cfe;
    }

    /**
     * On or after the {@code FETCHING_PHASE}, retrieves the name of the CIF
     * file that this CIF file context is configured to parse; at this version,
     * used only by {@link #onFetchingPhaseBeforeBody()} 
     * 
     * @return the CIF file name, as a {@code String}, determined by querying
     *         the relevant file context
     */
    private String getFileName() {
        FileContext effectiveContext = ((fileContext != null)
                ? fileContext
                : findRealAncestorWithClass(this, FileContext.class));
        
        if (effectiveContext == null) {
            return null;
        } else {
            SampleDataFile file = effectiveContext.getSampleDataFile();
            
            return ((file != null) ? file.getName() : null);
        }
    }
    
    /**
     * {@inheritDoc}
     * 
     * @see ErrorSupplier#getErrorCode()
     */
    public int getErrorCode() {
        return errorCode;
    }

    /**
     * {@inheritDoc}
     * 
     * @see ErrorSupplier#setErrorFlag(int)
     */
    public void setErrorFlag(int errorFlag) {
        errorCode |= errorFlag;
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
        return EMPTY_CIF;
    }
    
    /**
     * A {@code CifErrorHandler} that raises the containing
     * {@code CifFileContext}'s {@code CIF_HAS_ERRORS} error flag when a CIF
     * error is reported to it, unless that error is a {@code CifWarning}.  It
     * never aborts a parse, however.
     * 
     * @author jobollin
     * @version 0.9.0
     */
    private class ErrorHandler implements CifErrorHandler {

        /**
         * {@inheritDoc}
         * 
         * @see CifErrorHandler#handleError(CifError)
         */
        public void handleError(CifError error) {
            assert error != null;
            
            if (error instanceof CifWarning) {
                // the error can safely be ignored
            } else {
                CifFileContext.this.setErrorFlag(CIF_HAS_ERRORS);
            }
        }
        
    }
}
