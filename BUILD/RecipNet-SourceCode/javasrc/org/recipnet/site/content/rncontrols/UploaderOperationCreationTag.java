/*
 * Reciprocal Net project
 * 
 * UploaderOperationCreationTag.java
 * 
 * 04-Aug-2005: midurbin wrote the first draft
 * 14-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.site.content.rncontrols;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import org.recipnet.common.controls.HtmlPageElement;
import org.recipnet.site.shared.db.UserInfo;
import org.recipnet.site.wrapper.CoreConnector;
import org.recipnet.site.wrapper.FileTracker;
import org.recipnet.site.wrapper.OperationPersister;
import org.recipnet.site.wrapper.PersistedOperation;
import org.recipnet.site.wrapper.UploaderOperation;

/**
 * <p>
 * A custom tag that creates the {@code FormOperation} needed by the
 * {@link org.recipnet.site.content.servlet.MultipartUploadAccepter
 * MultipartUploadAccepter} servlet and outputs a hidden form field with a value
 * equal to its ID and a name equal to the provided 'persistedOpIdParamName'
 * property.
 * </p><p>
 * This tag is intended to be placed within a form that posts
 * "multipart/form-data". And must be nested within a {@code SampleContext}
 * defining the sample for which the files will be uploaded.
 * </p>
 */
public class UploaderOperationCreationTag extends HtmlPageElement {

    /**
     * An optional property that defaults to "persistedOpId" that serves as the
     * name of the hidden form field output by this tag.
     */
    private String persistedOpIdParamName;

    /**
     * A required property that indicates the code for the workflow action that
     * is to be performed by the form in which this tag is included.
     */
    private int workflowAction;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.persistedOpIdParamName = "persistedOpId";
    }

    /** Sets the 'persistedOpIdParamName' property. */
    public void setPersistedOpIdParamName(String name) {
        this.persistedOpIdParamName = name;
    }

    /** Gets the 'persistedOpIdParamName' property. */
    public String getPersistedOpIdParamName() {
        return this.persistedOpIdParamName;
    }

    /** Sets the 'workflowAction' property. */
    public void setWorkflowAction(int action) {
        this.workflowAction = action;
    }

    /** Gets the 'workflowAction' property. */
    public int getWorkflowAction() {
        return this.workflowAction;
    }

    /**
     * {@inheritDoc}; this version instantiates and registers an
     * {@code UploaderOperation}, and outputs its ID as the value of a hidden
     * form field.
     * 
     * @throws IllegalStateException if this tag is not nested in a
     *         {@code SampleContext}, or if it is nested within a
     *         {@code SampleContext} that provides a {@code null}
     *         {@code SampleInfo}
     */
    @Override
    public int onRenderingPhaseAfterBody(JspWriter out) throws IOException,
            JspException {
        long timeoutInSeconds
                = this.pageContext.getSession().getMaxInactiveInterval();
        SampleContext sampleContext
                = findRealAncestorWithClass(this, SampleContext.class);

        if ((sampleContext == null) || (sampleContext.getSampleInfo() == null)) {
            throw new IllegalStateException();
        }
        
        UploaderOperation op = new UploaderOperation(
                CoreConnector.extract(this.pageContext.getServletContext()),
                FileTracker.getFileTracker(this.pageContext.getServletContext()),
                ((timeoutInSeconds < 0) ? PersistedOperation.NO_TIME
                        : (timeoutInSeconds * 1000)),
                sampleContext.getSampleInfo(),
                (UserInfo) this.pageContext.getSession().getAttribute(
                        "userInfo"),
                this.workflowAction, null, true, false, true);

        op.startReceivingUploads();
        out.print("<input type=\"hidden\" name=\""
                + this.persistedOpIdParamName
                + "\" value=\""
                + OperationPersister.extract(
                        this.pageContext.getServletContext()).registerOperation(
                        op)
                + "\" />");

        return super.onRenderingPhaseAfterBody(out);
    }
}
