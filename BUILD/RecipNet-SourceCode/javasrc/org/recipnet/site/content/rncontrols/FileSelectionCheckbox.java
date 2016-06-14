/*
 * Reciprocal Net project
 * 
 * FileSelectionCheckbox.java
 *
 * 04-Aug-2005: midurbin wrote first draft
 * 13-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.site.content.rncontrols;

import java.util.Map;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import org.recipnet.common.controls.CheckboxHtmlControl;
import org.recipnet.common.controls.HtmlPageElement;

/**
 * A checkbox that must be nested in a {@code FileContext} and provides the
 * file's name to the a {@code SelectFilesForActionPage} when checked.
 */
public class FileSelectionCheckbox extends CheckboxHtmlControl {

    /**
     * A reference to the most immediate {@code FileContext}, set during the
     * {@code REGISTRATION_PHASE} and used to get the current
     * {@code SampleDataFile} object during and after the
     * {@code FETCHING_PHASE}.
     */
    private FileContext fileContext;

    /**
     * A reference to the surrounding {@code SelectFilesForActionPage} to which
     * selected files will be provided.
     */
    private SelectFilesForActionPage selectFilesForActionPage;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.fileContext = null;
        this.selectFilesForActionPage = null;
    }

    /**
     * {@inheritDoc}; this version looks up a reference to the innermost
     * surrounding {@code FileContext}, for later use.
     * 
     * @throws IllegalStateException if there is no {@code FileContext}
     *         surrounding this tag or if the {@code HtmlPage} evaluating this
     *         tag is not a {@code SelectFilesForActionPage}.
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);

        // get FileContext
        this.fileContext = findRealAncestorWithClass(this, FileContext.class);
        if (this.fileContext == null) {
            throw new IllegalStateException();
        }
        try {
            this.selectFilesForActionPage 
                    = (SelectFilesForActionPage) getPage();
        } catch (ClassCastException cce) {
            throw new IllegalStateException(cce);
        }

        return rc;
    }

    /**
     * {@inheritDoc}; this version invokes
     * {@link SelectFilesForActionPage#addFilename(String) addFilename()} on the
     * surrounding {@code SelectFilesForActionPage} if this checkbox is checked.
     */
    @Override
    public int onProcessingPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onProcessingPhaseBeforeBody(pageContext);

        if (Boolean.TRUE.equals(getValue())
                && (this.fileContext.getSampleDataFile() != null)) {
            this.selectFilesForActionPage.addFilename(
                    this.fileContext.getSampleDataFile().getName());
        }

        return rc;
    }

    /** {@inheritDoc} */
    @Override
    public HtmlPageElement generateCopy(String newId, Map map) {
        FileSelectionCheckbox dc = (FileSelectionCheckbox) super.generateCopy(
                newId, map);

        dc.fileContext = (FileContext) map.get(this.fileContext);

        return dc;
    }
}
