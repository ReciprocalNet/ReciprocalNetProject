/*
 * Reciprocal Net project
 * 
 * HeldFileOption.java
 * 
 * 15-Jun-2005: ekoperda wrote first draft
 * 28-Oct-2005: midurbin added support for preference-based initial values
 * 01-Feb-2006: jobollin converted this class to use the new button group
 *              control; formatted the source
 * 26-Apr-2006: jobollin reformatted the source, removed commented-out code,
 *              organized imports
 */

package org.recipnet.site.content.rncontrols;

import java.io.IOException;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.recipnet.common.controls.HtmlControl;
import org.recipnet.common.controls.HtmlPageElement;
import org.recipnet.common.controls.RadioButtonGroupHtmlControl;
import org.recipnet.common.controls.RadioButtonHtmlControl;
import org.recipnet.site.InvalidDataException;
import org.recipnet.site.OperationFailedException;
import org.recipnet.site.OperationNotPermittedException;
import org.recipnet.site.shared.SampleDataFile;
import org.recipnet.site.shared.UserPreferences;
import org.recipnet.site.shared.bl.UserBL;
import org.recipnet.site.wrapper.UploaderOperation;

/**
 * <p>
 * A JSP custom tag that behaves like a {@code RadioButtonHtmlControl} and is
 * designed to be nested within both an {@code UploaderPage} tag and a
 * {@code FileContext} tag. This control allows the user to select which version
 * of a "held" file to keep and which to discard. Normally a JSP author would
 * use two of these controls within a single {@code FileContext} tag, one for
 * each choice. This tag's {@code fileToKeep} property determines which choice
 * the tag represents. For enhanced grouping behavior, this tag can be nested
 * within an {@code HtmlPageIterator} tag and its
 * {@code takeGroupNameFromIterator} property set to true.
 * </p><p>
 * The initial value of this radio button is set based on the preferences
 * associated with the current session.
 * </p><p>
 * On {@code PROCESSING_PHASE}, if the control has been selected by the user,
 * then this tag communicates with the {@code UploaderPage} and causes the
 * "held" file exposed by the nearest {@code FileContext} to be either dropped
 * or preserved, according to the {@code fileToKeep} property.
 * </p>
 */
public class HeldFileOption extends RadioButtonHtmlControl {
    public enum FileToKeep {
        EXISTING, UPLOADED
    }

    /**
     * A reference to the nearest {@code UploaderPage} that encloses this tag.
     * Set by {@code onRegistrationPhaseBeforeBody()}.
     */
    private UploaderPage uploaderPage;

    /**
     * A reference to the nearest {@code FileContext} that encloses this tag.
     * Set by {@code onRegistrationPhaseBeforeBody()}.
     */
    private FileContext fileContext;

    /**
     * A required property that should to set to one of the values of the
     * {@code FileToKeep} enumeration. This value affects the behavior of the
     * control. On {@code PROCESSING_PHASE}, if this control has been selected
     * by the user and this property's value is {@code EXISTING}, then this tag
     * eventually invokes {@code UploaderOperation.abandonHeldFile()} in order
     * to preserve the existing file. On the other hand, if this property's
     * value is {@code UPLOADED}, then this tag eventually invokes
     * {@code UploaderOperation.transferHeldFileToRepositoryManager()} in order
     * to preserve the uploaded file.
     */
    private FileToKeep fileToKeep;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.uploaderPage = null;
        this.fileContext = null;
        this.fileToKeep = null;
    }

    /** simple property setter */
    public void setFileToKeep(FileToKeep fileToKeep) {
        this.fileToKeep = fileToKeep;
    }

    /** simple property getter */
    public FileToKeep getFileToKeep() {
        return this.fileToKeep;
    }

    /**
     * {@inheritDoc}
     * 
     * @throws IllegalStateException if no {@code UploaderPage} or
     *         {@code FileContent} tags could be found
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);

        // Find the nearest UploaderPage.
        this.uploaderPage = findRealAncestorWithClass(this, UploaderPage.class);
        if (this.uploaderPage == null) {
            throw new IllegalStateException();
        }

        // Find the nearest FileContext.
        this.fileContext = findRealAncestorWithClass(this, FileContext.class);
        if (this.fileContext == null) {
            throw new IllegalStateException();
        }

        // Tell the superclass how the radio button should be set up.
        setOption(getId() + "_option");
        
        // Configure the whether this option is initially selected
        boolean overwriteExisting = UserBL.getPreferenceAsBoolean(
                UserBL.Pref.DEFAULT_FILE_OVERWRITE,
                (UserPreferences) pageContext.getSession().getAttribute(
                        "preferences"));
        boolean value;
        
        switch (this.fileToKeep) {
            case UPLOADED:
                value = overwriteExisting;
                break;
            case EXISTING:
                value = !overwriteExisting;
                break;
            default:
                throw new IllegalStateException(
                        "This button's file to keep is unknown");
        }
        
        if (value) {
            RadioButtonGroupHtmlControl group = findRealAncestorWithClass(
                    this, RadioButtonGroupHtmlControl.class);
            
            group.setValue(getOption(), HtmlControl.LOWEST_PRIORITY);
        }

        // done
        return rc;
    }

    /**
     * {@inheritDoc}
     * 
     * @throws JspException with a more specific nested exception if the sample
     *         data file represented by this control could not be abandoned or
     *         transfered as the user instructed.
     */
    @Override
    public int onProcessingPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onProcessingPhaseBeforeBody(pageContext);

        if (getValueAsBoolean()) {
            // This checkbox is checked. Figure out which file we're dealing
            // with and what the user wanted to do.
            UploaderOperation op = this.uploaderPage.getUploaderOperation();
            SampleDataFile file = this.fileContext.getSampleDataFile();
            
            try {
                if (this.fileToKeep == FileToKeep.EXISTING) {
                    op.abandonHeldFile(file);
                }
                if (this.fileToKeep == FileToKeep.UPLOADED) {
                    op.keepHeldFile(file, true, true);
                }
            } catch (InvalidDataException ex) {
                throw new JspException(ex);
            } catch (IOException ex) {
                throw new JspException(ex);
            } catch (OperationFailedException ex) {
                throw new JspException(ex);
            } catch (OperationNotPermittedException ex) {
                throw new JspException(ex);
            }
        }

        return rc;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HtmlPageElement generateCopy(String newId, Map map) {
        HeldFileOption x = (HeldFileOption) super.generateCopy(newId, map);
        
        x.fileContext = (FileContext) map.get(this.fileContext);
        x.uploaderPage = (UploaderPage) map.get(this.uploaderPage);
        
        return x;
    }

    /**
     * {@inheritDoc}; this version delegates back to the superclass but ensures
     * that the 'option' property is not modified, but instead stays as whatever
     * value was set by this class.
     * 
     * @param source a {@code HeldFileOption} whose transient fields are being
     *        copied to this object.
     */
    @Override
    protected void copyTransientPropertiesFrom(HtmlPageElement source) {
        String overrideOption = this.getOption();
        
        super.copyTransientPropertiesFrom(source);
        this.setOption(overrideOption);
    }
}
