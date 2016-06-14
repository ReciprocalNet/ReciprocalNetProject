/*
 * Reciprocal Net project
 * @(#)RenameFileWapPage.java
 *
 * 04-Aug-2005: midurbin wrote first draft
 */
package org.recipnet.site.content.rncontrols;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import org.recipnet.common.controls.TextboxHtmlControl;
import org.recipnet.site.shared.validation.FilenameValidator;

/**
 * An extension of <code>TextboxHtmlControl</code> that allows a user to input
 * a filename.  The filename is validated using a
 * <code>FilenameValidator</code> and supplied to the surrounding
 * <code>RenameFileWapPage</code> in which this tag must be nested.
 */
public class RenameFileNewFileName extends TextboxHtmlControl {

    /**
     * A reference to the <code>RenameFileWapPage</code> in which this tag must
     * be nested.
     */
    protected RenameFileWapPage renamePage;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.renamePage = null;
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
        if (!(this.getPage() instanceof RenameFileWapPage)) {
            throw new IllegalStateException();
        } else {
            this.renamePage = (RenameFileWapPage) this.getPage();
        }
        this.setValidator(new FilenameValidator());
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
            this.renamePage.setNewFileName((String) this.getValue());
        }
        return super.onProcessingPhaseAfterBody(pageContext);
    }
}
