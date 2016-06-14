/*
 * Reciprocal Net project
 * 
 * DataDirectoryField.java
 *
 * 09-Jun-2005: midurbin wrote first draft
 * 17-Jan-2006: jobollin updated docs to reflect ErrorMessageElement's name
 *              change
 */

package org.recipnet.site.content.rncontrols;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import org.recipnet.common.Validator;
import org.recipnet.common.controls.HtmlControl;
import org.recipnet.common.controls.HtmlPageElement;
import org.recipnet.common.controls.TextboxHtmlControl;
import org.recipnet.site.core.RepositoryDirectoryNotFoundException;
import org.recipnet.site.shared.validation.ComplexFilenameValidator;

/**
 * <p>
 * A custom tag that exposes information about the potential data directory for
 * a sample that does not have a data directory.  This field interacts with a
 * {@code FileIterator} (supplied as the 'fileIterator' property) to get
 * the {@code RepositoryDirectoryNotFoundException} if one was thrown. 
 * This tag will be invisible in the event that a
 * {@code RepositoryDirectoryNotFoundException} was not thrown or does not
 * contain suggested directory names.  In order to ensure that those conditions
 * are true, this tag may be placed in an {@code ErrorChecker} set
 * to evaluate its body on the {@link
 * FileIterator#NO_DIRECTORY_NO_HOLDINGS NO_DIRECTORY_NO_HOLDINGS} error flag.
 * </p><p>
 * This tag is written to be nested within a {@code EditSamplePage}.  When
 * this tag represents the 'extension path' it notifies the
 * {@code EditSamplePage} of its parsed field value so that it may be used
 * if {@code EditSamplePage} is triggered to create a data directory.
 * </p><p>
 * Unlike other 'Field' tags, there is no context for this tag to recognize and
 * no container object to directly modify.  Instead, when the value of this tag
 * is required for a call to core, a peer tag with a reference to this tag
 * should be used (for example <code>{@link CreateDataDirectoryButton
 * CreateDataDirectoryButton}</code>).
 * </p>
 */
public class DataDirectoryField extends TextboxHtmlControl {

    /** An enumeration of the parts of the suggested directory name. */
    public static enum FieldCode {
        FIRST_PART,
        EXTENSION_PATH,
        LAST_PART
    }

    /**
     * The validator used for values entered for this textbox when it
     * represents the {@code EXTENSION_PATH}.
     */
    private static Validator extensionPathValidator
            = new ComplexFilenameValidator();

    /**
     * The {@code EditSamplePage} in which this tag is nested; determined
     * by {@code onRegistrationPhaseBeforeBody()}.  If this
     * {@code DataDirectoryField} represents the extension path
     * (the 'directoryPart' property is {@link
     * DataDirectoryField.FieldCode#EXTENSION_PATH EXTENSION_PATH} the
     * parsed and validated value of this {@code DataDirectoryField} is
     * reported by invoking {@link EditSamplePage#setExtensionPath(String)
     * setExtensionPath()} on this {@code EditSamplePage}.
     */
    private EditSamplePage editSamplePage;

    /**
     * A required property that indicates the {@code FileIterator} that
     * may report that no repository directory exists.  It is only in such a
     * case that this tag is useful.
     */
    private FileIterator fileIterator;

    /**
     * A required property that indicates which part of the suggested data
     * directory name this tag will expose.  Unless this is set to
     * {@code EXTENSION_PATH}, this textbox will be displayed as a label
     * and uneditable as the suggested first and last parts of the path are
     * just informative and not modifiable.
     */
    private FieldCode directoryPart;

    /** { @inheritDoc } */
    @Override
    protected void reset() {
        super.reset();
        this.editSamplePage = null;
        this.fileIterator = null;
        this.directoryPart = null;
    }

    /**
     * @param fileIt a reference to a {@code FileIterator} that may supply
     *     the information exposed by this tag.
     */
    public void setFileIterator(FileIterator fileIt) {
        this.fileIterator = fileIt;
    }

    /**
     * @return a reference to the {@code FilIterator} that may supply the
     *     information exposed by this tag
     */
    public FileIterator getFileIterator() {
        return this.fileIterator;
    }

    /**
     * @param part the enumeration for the part of the suggested directory name
     *     to be exposed.
     */
    public void setDirectoryPart(FieldCode part) {
        switch (part) {
            case EXTENSION_PATH:
            case FIRST_PART:
            case LAST_PART:
                this.directoryPart = part;
                break;
            default:
                assert false;
        }
    }

    /**
     * @return the enumeration for the part of the suggested directory name to
     *     be exposed by this tag.
     */
    public FieldCode getDirectoryPart() {
        return this.directoryPart;
    }

    /**
     * {@inheritDoc}.  This version gets a reference to the
     * {@code EditSamplePage} and initializes some properties based on the
     * selected 'directoryPart'.
     * 
     * @throws IllegalStateException if this tag is not nested within an
     *     {@code EditSamplePage}.
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext); 
        this.editSamplePage
                = this.findRealAncestorWithClass(this, EditSamplePage.class);
        if (this.editSamplePage == null) {
            throw new IllegalStateException();
        }
        switch (this.directoryPart) {
            case EXTENSION_PATH:
                this.setValidator(extensionPathValidator);
                this.setDisplayAsLabel(false);
                this.setEditable(true);
                break;
            case FIRST_PART:
            case LAST_PART:
                this.setDisplayAsLabel(true);
                this.setEditable(false);
                break;
            default:
                assert false;
        }
        return rc;
    }

    /**
     * {@inheritDoc}. This version determines whether there is a repository
     * directory for the 'fileIterator' and if not, exposes the part of the
     * suggested directory indicatd by the 'directoryPart'. If a repository
     * directory is available or no suggested directory information is provided,
     * this tag's 'visibility' property is set to false.
     */
    @Override
    public int onFetchingPhaseBeforeBody() throws JspException {
        int rc = super.onFetchingPhaseBeforeBody();
        RepositoryDirectoryNotFoundException ex
                = this.fileIterator.getDirNotFoundException();
        if ((ex != null) && ex.isSuggestionAvailable()) {
            switch (this.directoryPart) {
                case FIRST_PART:
                    this.setValue(ex.getSuggestedDirectoryFirstPart(),
                            HtmlControl.EXISTING_VALUE_PRIORITY);
                    break;
                case EXTENSION_PATH:
                    this.setValue(ex.getSuggestedDirectoryExtensionPath(),
                            HtmlControl.EXISTING_VALUE_PRIORITY);
                    break;
                case LAST_PART:
                    this.setValue(ex.getSuggestedDirectoryLastPart(),
                            HtmlControl.EXISTING_VALUE_PRIORITY);
                    break;
            }
        } else {
            this.setVisible(false);
        }
        return rc;
    }

    /**
     * {@inheritDoc}. This version simply reports the value of this tag to the
     * {@code EditSamplePage} (via a call to
     * {@link EditSamplePage#setExtensionPath(String)
     * EditSamplePage.setExtensionPath()} if this tag represents the
     * {@code EXTENSION_PATH}.
     */
    @Override
    public int onProcessingPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onProcessingPhaseBeforeBody(pageContext);
        if ((this.directoryPart == FieldCode.EXTENSION_PATH)
                && !this.getFailedValidation()) {
            this.editSamplePage.setExtensionPath((String) this.getValue());
        }
        return rc;
    }

    /**
     * {@inheritDoc}. This version delegates the superclass but preserves the
     * value of the 'editable', 'displayAsLabel' and 'visible' properties which
     * are not meant to be exposed to the JSP author, but instead used
     * internally in conjunction with the 'directoryPart' (which is then
     * copied).
     */
    @Override
    protected void copyTransientPropertiesFrom(HtmlPageElement source) {
        DataDirectoryField src = (DataDirectoryField) source;
        boolean editable = this.getEditable();
        boolean displayAsLabel = this.getDisplayAsLabel();
        boolean visible = this.getVisible();
        
        super.copyTransientPropertiesFrom(source);
        this.setEditable(editable);
        this.setDisplayAsLabel(displayAsLabel);
        this.setVisible(visible);
        this.setFileIterator(src.fileIterator);
    }
}
