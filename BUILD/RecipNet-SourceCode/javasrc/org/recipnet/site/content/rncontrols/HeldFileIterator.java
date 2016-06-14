/*
 * Reciprocal Net project
 * 
 * HeldFileIterator.java
 * 
 * 15-Jun-2005: ekoperda wrote first draft
 * 10-May-2006: jobollin reformatted the source and updated some docs
 */

package org.recipnet.site.content.rncontrols;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.recipnet.common.controls.HtmlPageElement;
import org.recipnet.site.shared.SampleDataFile;
import org.recipnet.site.wrapper.UploaderOperation;

/**
 * <p>
 * A special-purpose subclass of the {@code FileIterator} tag that is designed
 * to be nested within an {@code UploaderPage} tag. During a file-upload
 * operation, if any files are "held" at the wrapper layer rather than having
 * been written to core, this tag will discover them and iterate through them.
 * </p><p>
 * Unlike the parent class, this tag does not need to be nested within a
 * {@code SampleContext} or a {@code UserContext} and does not recognize these.
 * This tag must be nested within an {@code UploaderPage}.
 * </p>
 */
public class HeldFileIterator extends FileIterator {

    /**
     * A reference to the nearest {@code UploaderPage} tag that encloses this
     * one. Set by {@code onRegistrationPhaseBeforeBody()}.
     */
    private UploaderPage uploaderPage;

    /**
     * A required property that is the URL at which the {@code FileRetriever}
     * servlet is accessible to the web client. This tag uses this value in
     * creating the {@code SampleDataFile} objects that are exposed via this
     * tag's {@code FileContext} interface. See the documentation on
     * {@code UploaderOperation.getHeldFiles()} for an explanation of the string
     * concatenation process.
     */
    private String fileRetrieveServletHref;

    /**
     * A required property that is the name of the query-line parameter the
     * {@code FileRetriever} servlet expects to be invoked with. This tag uses
     * this value in creating the {@code SampleDataFile} objects that are
     * exposed via this tag's {@code FileContext} interface. See the
     * documentation on {@code UploaderOpation.getHeldFiles()} for an
     * explanation of the string concatentation process.
     */
    private String keyParamName;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.uploaderPage = null;
        this.fileRetrieveServletHref = null;
        this.keyParamName = null;
    }

    /** simple property getter */
    public String getFileRetrieveServletHref() {
        return this.fileRetrieveServletHref;
    }

    /** simple property setter */
    public void setFileRetrieveServletHref(String fileRetrieveServletHref) {
        this.fileRetrieveServletHref = fileRetrieveServletHref;
    }

    /** simple property getter */
    public String getKeyParamName() {
        return this.keyParamName;
    }

    /** simple property setter */
    public void setKeyParamName(String keyParamName) {
        this.keyParamName = keyParamName;
    }

    /**
     * {@code inheritDoc}; this version determines the innermost surrounding
     * {@code UploaderPage} and then delegates back to its superclass.
     * 
     * @throws IllegalStateException if this tag is not nested within an
     *         {@code UploaderPage}.
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);

        this.uploaderPage = findRealAncestorWithClass(this, UploaderPage.class);
        if (this.uploaderPage == null) {
            throw new IllegalStateException();
        }

        return rc;
    }

    /** {@inheritDoc} */
    @Override
    public HtmlPageElement generateCopy(String newId, Map map) {
        HeldFileIterator dc = (HeldFileIterator) super.generateCopy(newId, map);
        dc.uploaderPage = (UploaderPage) map.get(this.uploaderPage);
        return dc;
    }

    /**
     * {@inheritDoc}; this version consults the surrounding
     * {@code UploaderPage} and makes all of the held files accessible for
     * iteration.
     */
    @Override
    protected Collection<? extends SampleDataFile> fetchFiles()
            throws JspException {
        UploaderOperation op = this.uploaderPage.getUploaderOperation();

        // Exit early if the page is empty for some reason.
        if (op == null) {
            return new ArrayList<SampleDataFile>();
        }

        return op.getHeldFiles(this.fileRetrieveServletHref, this.keyParamName);
    }
}
