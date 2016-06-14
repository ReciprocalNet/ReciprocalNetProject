/*
 * Reciprocal Net project
 * @(#)FileDescriptionField.java
 *
 * 21-Sep-2005: midurbin wrote first draft
 */
package org.recipnet.site.content.rncontrols;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import org.recipnet.common.controls.HtmlControl;
import org.recipnet.common.controls.TextareaHtmlControl;

/**
 * A <code>TextareaHtmlControl</code> that displays the current description for
 * the <code>SampleDataFile</code> provided by the surrounding
 * <code>ModifyFileDescriptionWapPage</code> and allows it to be updated or
 * erased.
 */
public class FileDescriptionField extends TextareaHtmlControl {

    /**
     * A reference to the <code>RenameFileWapPage</code> in which this tag must
     * be nested.
     */
    protected ModifyFileDescriptionWapPage modifyFileDescriptionWapPage;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.modifyFileDescriptionWapPage = null;
        this.setMaxLength(200);
    }

    /**
     * Overrides <code>HtmlPageElement</code>; the current implementation
     * ensures that this tag is nested within a <code>RenameFileWapPage</code>
     * and attaches an appropriate validator.
     * @throws IllegalStateException if this tag's <code>HtmlPage</code> is not
     *     a <code>RenameFileWapPage</code>.
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);
        if (!(this.getPage() instanceof ModifyFileDescriptionWapPage)) {
            throw new IllegalStateException();
        } else {
            this.modifyFileDescriptionWapPage
                    = (ModifyFileDescriptionWapPage) this.getPage();
        }
        return rc;
    }

    /**
     * Overrides <code>HtmlPageElement</code>; the current implementation sets
     * the value of this text area to the description for the file indicated by
     * the <code>ModifyFileDescriptionWapPage</code>.
     */
    @Override
    public int onFetchingPhaseBeforeBody() throws JspException {
        int rc = super.onFetchingPhaseBeforeBody();
        this.setValue(this.modifyFileDescriptionWapPage.getSampleDataFile()
                .getDescription(), HtmlControl.EXISTING_VALUE_PRIORITY);
        return rc;
    }

    /** 
     * Overrides <code>HtmlPageElement</code>; the current implementation 
     * provides the surrounding <code>ReanmeFileWapPage</code> with the new
     * name if a valid one was entered into this textbox.
     */
    @Override
    public int onProcessingPhaseAfterBody(PageContext pageContext)
            throws JspException {
        if (!this.getFailedValidation()) {
            this.modifyFileDescriptionWapPage.setNewFileDescription(
                    (String) this.getValue());
        }
        return super.onProcessingPhaseAfterBody(pageContext);
    }
}
