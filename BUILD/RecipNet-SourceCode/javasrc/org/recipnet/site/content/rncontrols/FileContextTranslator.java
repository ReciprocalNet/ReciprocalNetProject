/*
 * Reciprocal Net project
 * 
 * FileContextTranslator.java
 * 
 * 15-Jun-2005: ekoperda wrote first draft
 * 13-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.site.content.rncontrols;

import java.rmi.RemoteException;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

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
 * A custom tag that is designed to be nested within a {@code FileContext} tag
 * and that also exposes a {@code FileContext} interface to other nested tags.
 * Its purpose is to "translate" one {@code SampleDataFile} object to another
 * {@code SampleDataFile} object based upon rules that the JSP author specifies.
 * Exactly one of the {@code translate...} attributes on this tag should be set
 * to true. See comments on the various translation mode attributes for an
 * explanation of their function. In all cases, when the
 * {@code getSampleDataFile()} function on the {@code FileContext} that encloses
 * this tag returns null, this tag's {@code getSampleDataFile()} method returns
 * null also.
 */
public class FileContextTranslator extends HtmlPageElement implements
        FileContext {
    /**
     * A reference to the nearest {@code FileContext} tag that encloses this
     * one, as set by {@code onRegistrationPhaseBeforeBody()}.
     */
    private FileContext fileContext;

    /**
     * The container object that this context exposes, as set by
     * {@code onFetchingPhaseBeforeBody()}.
     */
    private SampleDataFile exposedFile;

    /**
     * An optional attribute that, when set to true, enables one of this tag's
     * translation modes. For every file exposed by the parent context whose
     * {@code SampleDataFile.isSettled()} function returns true, this context
     * exposes the same file. For every file exposed by the parent context whose
     * {@code SampleDataFile.isProvisional()} function returns true, this
     * context consults {@code RepositoryManager} and exposes the file that has
     * the same sampleId, sampleHistoryId, and name, or null if no matching file
     * was found. For simplicity, any file size limits that
     * {@code RepositoryManager} may impose are not overriden. The overall
     * effect of enabling this translation mode is that any file exposed by this
     * context always will return true from {@code SampleDataFile.isSettled()}.
     */
    private boolean translateProvisionalToSettled;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.fileContext = null;
        this.exposedFile = null;
    }

    /** simple property getter */
    public boolean getTranslateProvisionalToSettled() {
        return this.translateProvisionalToSettled;
    }

    /** simple property setter */
    public void setTranslateProvisionalToSettled(boolean translate) {
        this.translateProvisionalToSettled = translate;
    }

    /** Implements {@code FileContext}. */
    public SampleDataFile getSampleDataFile() {
        return this.exposedFile;
    }

    /**
     * {@inheritDoc}; this version validates its properties, binds to a
     * {@code FileContext}, and delegates back to its superclass for a return
     * value.
     * 
     * @throws IllegalStateException if this tag is not nested within an
     *         {@code FileContext}, or if a translation mode is not selected.
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);

        // Find a FileContext.
        this.fileContext = findRealAncestorWithClass(this, FileContext.class);
        if (this.fileContext == null) {
            throw new IllegalStateException();
        }

        // Validate the translation mode.
        // TODO: add more tests here as modes are added.
        if (!translateProvisionalToSettled) {
            throw new IllegalStateException();
        }

        return rc;
    }

    /**
     * {@inheritDoc}; this version gets the
     * {@code SampleDataFile} object and performs any necessary translation.
     * 
     * @throws JspException any exception thrown while attempting to get data
     *         from the core will be set as the root cause of a new
     *         {@code JspException}
     */
    @Override
    public int onFetchingPhaseBeforeBody() throws JspException {
        int rc = super.onFetchingPhaseBeforeBody();

        // Fetch the parent context's file.
        SampleDataFile fetchedFile = this.fileContext.getSampleDataFile();
        if (fetchedFile == null) {
            // The parent context is not exposing a file so we won't either.
            return rc;
        }

        // Perform translation.
        if (this.translateProvisionalToSettled) {
            this.exposedFile = fetchedFile.isSettled()
                    ? fetchedFile
                    : fetchMatchingFileFromRepositoryManager(
                            fetchedFile.getSampleId(),
                            fetchedFile.getSampleHistoryId(),
                            fetchedFile.getName());
        } else {
            // Invalid translation mode.
            assert false;
        }

        return rc;
    }

    /** {@inheritDoc} */
    @Override
    public HtmlPageElement generateCopy(String newId, Map map) {
        FileContextTranslator dc = (FileContextTranslator) super.generateCopy(
                newId, map);
        dc.fileContext = (FileContext) map.get(this.fileContext);
        return dc;
    }

    /**
     * An internal helper method that attempts to fetch a {@code SampleDataFile}
     * container object from {@code RepositoryManager} that describes a
     * particular data file.
     * 
     * @return the matching container object, or null if no match was found.
     * @param sampleId identifies the sample with which the desired file is
     *        associated.
     * @param sampleHistoryId identifies the sample-version with which the
     *        desired file is associated.
     * @param name the name of the desired file.
     * @throws JspException that may wrap any {@code RemoteException},
     *         {@code InconsistentDbException},
     *         {@code OperationFailedException}, or {@code WrongSiteException}
     *         that may be received from core.
     */
    private SampleDataFile fetchMatchingFileFromRepositoryManager(int sampleId,
            int sampleHistoryId, String name) throws JspException {
        // Check the cache and exit early if the object we need is there.
        RepositoryFiles rf = RequestCache.getRepositoryFiles(
                super.pageContext.getRequest(), sampleId, sampleHistoryId);
        if (rf != null) {
            return rf.getRecordWithName(name);
        }

        // Cache miss; fetch a RepositoryFiles object from RepositoryManager.
        CoreConnector coreConnector
                = CoreConnector.extract(super.pageContext.getServletContext());
        try {
            RepositoryManagerRemote repositoryManager
                    = coreConnector.getRepositoryManager();
            
            rf = repositoryManager.getRepositoryFiles(sampleId,
                    sampleHistoryId, false);
            RequestCache.putRepositoryFiles(super.pageContext.getRequest(), rf);
            
            return rf.getRecordWithName(name);
        } catch (RemoteException ex) {
            coreConnector.reportRemoteException(ex);
            throw new JspException(ex);
        } catch (RepositoryDirectoryNotFoundException ex) {
            /*
             * This exception is more of an indicator than an exception and does
             * not need to be thrown but instead stored to be available upon
             * request
             */
            return null;
        } catch (InconsistentDbException ex) {
            throw new JspException(ex);
        } catch (OperationFailedException ex) {
            throw new JspException(ex);
        } catch (WrongSiteException ex) {
            throw new JspException(ex);
        }
    }
}
