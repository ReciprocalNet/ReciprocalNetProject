/*
 * Reciprocal Net Project
 *
 * SimpleFileContext.java
 *
 * 17-Jan-2006: jobollin wrote first draft
 */

package org.recipnet.site.content.rncontrols;

import java.rmi.RemoteException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.recipnet.common.controls.ErrorSupplier;
import org.recipnet.common.controls.HtmlPageElement;
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
 * A {@code FileContext} implementation that simply provides a named file. This
 * tag must be nested within a sample context that establishes the association
 * between the specified file name and the particular repository file referenced
 * by this context.
 * 
 * @author jobollin
 * @version 1.0
 */
public class SimpleFileContext extends HtmlPageElement implements FileContext,
        ErrorSupplier {

    /**
     * An {@code ErrorSupplier} error flag indicating that although the
     * repository directory for the sample exists and is correctly configured,
     * it does not contain a file by the name specified to this context
     */
    public final static int NO_SUCH_FILE = 1 << 0;

    /**
     * An {@code ErrorSupplier} error flag indicating that an exception was
     * thrown while fetching the repository files because no directory existed
     * on the filesystem and no holding record existed in the database.
     */
    public static final int NO_DIRECTORY_NO_HOLDINGS = 1 << 1;

    /**
     * An {@code ErrorSupplier} error flag indicating that an exception was
     * thrown while fetching the repository files because no directory existed
     * on the filesystem even though a holding record existed in the database.
     */
    public static final int HOLDINGS_BUT_NO_DIRECTORY = 1 << 2;

    /**
     * An {@code ErrorSupplier} error flag indicating that an exception was
     * thrown while fetching the repository files because a directory existed on
     * the filesystem even though no holding record existed in the database.
     */
    public static final int DIRECTORY_BUT_NO_HOLDINGS = 1 << 3;

    /**
     * An {@code ErrorSupplier} error flag indicating no file was specified to
     * this context via either explicit filename or request parameter.  This may
     * or may not be expected.
     */
    public static final int NO_FILE_SPECIFIED = 1 << 4;
    
    /**
     * The name of a request parameter from which the name of the file to
     * provide should be drawn; if this attribute is specified then the
     * {@code fileName} attribute should <em>not</em> be specified.  Unlike
     * the {@link #fileName}, this property is not transient
     */
    private String fileParamName;

    /**
     * The name of the file to provide; if this attribute is specified then the
     * {@code fileParamName} attribute should <em>not</em> be specified.  This
     * is a transient property, but it is useless to try to change it after the
     * {@code FETCHING_PHASE}.
     */
    private String fileName;

    /**
     * The innermost {@code SampleContext} surrounding this tag
     */
    private SampleContext sampleContext;

    /**
     * The {@code SampleDataFile} provided by this context; the file information
     * is retrieved during the {@code FETCHING_PHASE}, and until then it will
     * be {@code null}. It will also be {@code null} <em>after</em> the
     * fetching phase if neither the {@code fileName} nor the
     * {@code fileNameParam} attribute is specified, or if whichever one is
     * specified does not name an existing file relative to the surrounding
     * sample context.
     */
    private SampleDataFile dataFile;

    /**
     * The current error code set on this {@code ErrorSupplier}
     */
    private int errorCode;

    /**
     * Retrieves the {@code fileName} property
     * 
     * @return the name of the file represented by this context, as a
     *         {@code String}
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Sets the {@code fileName} property
     * 
     * @param  fileName the name of the file represented by this context, as a
     *         {@code String}
     */
    public void setFileName(String fileName) {
        if ((fileName != null) && (this.fileParamName != null)) {
            throw new IllegalStateException(
                    "'fileName' and 'fileParamName' may not both be specified");
        }
        this.fileName = fileName;
    }

    /**
     * Retrieves the {@code fileParamName} property
     * 
     * @return the name of the parameter from which the file name represented by
     *         this context should be drawn, as a {@code String}
     */
    public String getFileParamName() {
        return fileParamName;
    }

    /**
     * Sets the {@code fileParamName} property
     * 
     * @param  fileParamName the name of the parameter from which the file name
     *         represented by this context should be drawn, as a {@code String}
     */
    public void setFileParamName(String fileParamName) {
        if ((this.fileName != null) && (fileParamName != null)) {
            throw new IllegalStateException(
                    "'fileName' and 'fileParamName' may not both be specified");
        }
        this.fileParamName = fileParamName;
    }

    /**
     * Determines the name of the file that should be represented by this
     * context based on the attributes specified to this tag handler
     * 
     * @return the file name as a {@code String}
     */
    public String getEffectiveFileName() {
        if (fileParamName != null) {
            return ((HttpServletRequest) pageContext.getRequest()).getParameter(
                    fileParamName);
        } else {
            return fileName;
        }
    }

    /**
     * Nominally, sets the value of the {@code effectiveFileName} property.  In
     * practice, this method always throws an exception; it is present only so
     * that buggy expression evaluators recognize the existence of the property.
     * Use {@link #setFileName(String)} instead.
     * 
     * @param  name the effective file name to set
     * 
     * @throws IllegalStateException every time
     */
    public void setEffectiveFileName(@SuppressWarnings("unused") String name) {
        throw new IllegalStateException(
                "The effective file name cannot be set directly");
    }

    /**
     * {@inheritDoc}. This version provides information about the file
     * specified by this tag's attributes and its containing sample context
     * 
     * @see FileContext#getSampleDataFile()
     */
    public SampleDataFile getSampleDataFile() {
        return dataFile;
    }

    /**
     * Provides the {@code SampleContext} surrounding this tag handler to
     * subclasses
     *  
     * @return the {@code SampleContext}
     */
    protected SampleContext getSampleContext() {
        return sampleContext;
    }

    /**
     * {@inheritDoc}. This version obtains a reference to the innermost sample
     * context surrounding this tag.
     * 
     * @see HtmlPageElement#onRegistrationPhaseBeforeBody(PageContext)
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);
    
        sampleContext = findRealAncestorWithClass(this, SampleContext.class);
    
        if (sampleContext == null) {
            throw new IllegalStateException("No sample context");
        }
    
        return rc;
    }

    /**
     * {@inheritDoc}. This version retrieves the appropriate sample data file
     * 
     * @see HtmlPageElement#onFetchingPhaseBeforeBody()
     */
    @Override
    public int onFetchingPhaseBeforeBody() throws JspException {
        int rc = super.onFetchingPhaseBeforeBody();
        String effectiveFileName = getEffectiveFileName();

        if (effectiveFileName == null) {
            setErrorFlag(NO_FILE_SPECIFIED);
        } else {
            // Get the RepositoryFiles
            RepositoryFiles rf = RequestCache.getRepositoryFiles(
                    pageContext.getRequest(), sampleContext.getSampleInfo().id,
                    sampleContext.getSampleInfo().historyId);

            if (rf == null) {
                // Cache miss; fetch a RepositoryFiles object from
                // RepositoryManager
                CoreConnector cc = CoreConnector.extract(
                        pageContext.getServletContext());

                try {
                    RepositoryManagerRemote repositoryManager
                            = cc.getRepositoryManager();

                    rf = repositoryManager.getRepositoryFiles(
                            sampleContext.getSampleInfo().id,
                            sampleContext.getSampleInfo().historyId, false);
                    RequestCache.putRepositoryFiles(
                            super.pageContext.getRequest(), rf);
                } catch (RemoteException re) {
                    cc.reportRemoteException(re);
                    throw new JspException(re);
                } catch (RepositoryDirectoryNotFoundException rdnfe) {

                    /*
                     * this exception is more of an indicator than an exception
                     * and does not need to be thrown
                     */

                    if (rdnfe.doesSuggestedDirectoryExist()
                            && !rdnfe.doesHoldingRecordExist()) {
                        this.setErrorFlag(DIRECTORY_BUT_NO_HOLDINGS);
                    } else if (!rdnfe.doesSuggestedDirectoryExist()
                            && rdnfe.doesHoldingRecordExist()) {
                        this.setErrorFlag(HOLDINGS_BUT_NO_DIRECTORY);
                    } else {
                        this.setErrorFlag(NO_DIRECTORY_NO_HOLDINGS);
                    }

                    return rc;
                } catch (InconsistentDbException ide) {
                    throw new JspException(ide);
                } catch (OperationFailedException ofe) {
                    throw new JspException(ofe);
                } catch (WrongSiteException wse) {
                    throw new JspException(wse);
                }
            }

            dataFile = rf.getRecordWithName(effectiveFileName);
            if (dataFile == null) {
                setErrorFlag(NO_SUCH_FILE);
            }
        }

        return rc;
    }

    /**
     * {@inheritDoc}. This version copies this tag's {@code SampleContext}
     * reference, in addition to the work performed by the superclass.
     * 
     * @see HtmlPageElement#generateCopy(String, Map)
     */
    @Override
    protected HtmlPageElement generateCopy(String newId, Map origToCopyMap) {
        SimpleFileContext copy = (SimpleFileContext) super.generateCopy(newId,
                origToCopyMap);

        copy.sampleContext
                = (SampleContext) origToCopyMap.get(this.sampleContext);

        return copy;
    }

    /**
     * {@inheritDoc}.  This version updates the {@code fileName} property.
     * 
     * @see HtmlPageElement#copyTransientPropertiesFrom(HtmlPageElement)
     */
    @Override
    protected void copyTransientPropertiesFrom(HtmlPageElement source) {
        SimpleFileContext copyFrom = (SimpleFileContext) source;
        
        super.copyTransientPropertiesFrom(source);
        setFileName(copyFrom.getFileName());
    }

    /**
     * {@inheritDoc}
     * 
     * @see HtmlPageElement#reset()
     */
    @Override
    protected void reset() {
        super.reset();

        fileParamName = null;
        fileName = null;
        sampleContext = null;
        dataFile = null;
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
        return NO_FILE_SPECIFIED;
    }
}
