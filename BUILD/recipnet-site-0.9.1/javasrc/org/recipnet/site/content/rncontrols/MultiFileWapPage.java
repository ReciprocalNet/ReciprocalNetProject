/*
 * Reciprocal Net project
 * 
 * MultiFileWapPage.java
 *
 * 04-Aug-2005: midurbin wrote first draft
 * 13-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.site.content.rncontrols;

import java.util.ArrayList;
import java.util.Collection;

import org.recipnet.common.controls.HtmlPage;
import org.recipnet.site.wrapper.MultiFileOperation;
import org.recipnet.site.wrapper.PersistedOperation;
import org.recipnet.site.wrapper.WorkflowActionPersistedOperation;

/**
 * An abstract extension of {@code ExtendedOperationWapPage} that allows for
 * subclasses to expose workflow actions that are performed on multiple files at
 * once. Thus, the persisted operation indicated by the "persistedOpId"
 * parameter must be an instance of {@link
 * org.recipnet.site.wrapper.MultiFileOperation MultiFileOperation}.
 */
public abstract class MultiFileWapPage extends ExtendedOperationWapPage
        implements MultiFilenameContext {

    /**
     * An optional property, that must be set if this page is to be able to
     * handle a request where a previously created {@code MultiFileOperation}
     * has not been supplied. In such cases the value of the request parameter
     * with the name indicated by this property is considered to be the filename
     * of the single file that will be included in a new
     * {@code MultiFileOperation}.
     */
    public String filenameParamName;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.filenameParamName = null;
    }

    /** Sets the 'filenameParamName' property. */
    public void setFilenameParamName(String name) {
        this.filenameParamName = name;
    }

    /** Gets the 'filenameParamName' property. */
    public String getFilenameParamName() {
        return this.filenameParamName;
    }

    /**
     * Implements {@code MultiFilenameContext} to get the filenames for files
     * that are to be included in the action.
     * 
     * @return a collection of SampleDataFiles or null
     * @throws IllegalStateException if called before the {@code FETCHING_PHASE}
     */
    public Collection<String> getFilenames() {
        if ((getPhase() == HtmlPage.REGISTRATION_PHASE)
                || (getPhase() == HtmlPage.PARSING_PHASE)) {
            throw new IllegalStateException();
        }
        return getMultiFileOperation().getFilenames();
    }

    /**
     * {@inheritDoc}; this version creates a new {@code MultiFileOperation} for
     * the single file indicated by the {@code filenameParamName}.
     * 
     * @throws IllegalStateException if no filename is indicated either because
     *         'filenameParamName' was not set or because the indicated
     *         parameter did not contain a filename
     */
    @Override
    protected WorkflowActionPersistedOperation createPersistedOperation() {
        if (this.filenameParamName == null) {
            throw new IllegalStateException();
        }
        String filename = pageContext.getRequest().getParameter(
                filenameParamName);
        if (filename == null) {
            throw new IllegalStateException();
        }
        Collection<String> filenames = new ArrayList<String>();

        filenames.add(filename);
        long timeoutInSeconds
                = this.pageContext.getSession().getMaxInactiveInterval();
        
        return new MultiFileOperation(
                (timeoutInSeconds < 0) ? PersistedOperation.NO_TIME
                        : timeoutInSeconds * 1000, getSampleInfo(),
                getComments(), getUserInfo().id, filenames);
    }

    /**
     * Provides a reference to the persisted operation
     * 
     * @return the {@code MultiFileOperation} underway
     */
    public MultiFileOperation getMultiFileOperation() {
        return (MultiFileOperation) getPersistedOperation();
    }
}
