/*
 * Reciprocal Net project
 * 
 * SampleActionFileIterator.java
 * 
 * 17-Oct-2005: midurbin wrote the first draft
 * 27-Oct-2005: midurbin added support for modified files
 * 10-May-2006: jobollin formatted the source
 */

package org.recipnet.site.content.rncontrols;

import java.util.Collection;
import java.util.Map;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import org.recipnet.common.controls.HtmlPageElement;
import org.recipnet.site.shared.SampleDataFile;

/**
 * An extension of {@code FileIterator} that iterates over the files associated
 * with the particular sample action indicated by the surrounding
 * {@code SampleActionIterator}. Based on the 'mode' attribute, this tag may
 * either iterate over the files added during the action or the files removed
 * during the action.
 */
public class SampleActionFileIterator extends FileIterator {

    /** An enumeration of the modes for this iterator. */
    public static enum Mode {
        ADDED_FILES, REMOVED_FILES, MODIFIED_FILES;
    }

    /** The surrounding {@code SampleActionIterator} tag. */
    private SampleActionIterator actionIterator;

    /**
     * A required property that indicates which files should be iterated over by
     * this tag.
     */
    private Mode mode;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.actionIterator = null;
        this.mode = Mode.ADDED_FILES;
    }

    /** Setter for the 'mode' attribute. */
    public void setMode(Mode mode) {
        this.mode = mode;
    }

    /** Getter for the 'mode' attribute. */
    public Mode getMode() {
        return this.mode;
    }

    /**
     * {@inheritDoc}; this version finds and stores a reference to the
     * surrounding {@code SampleActionIterator}.
     * 
     * @throws IllegalStateException if this tag is not nested in a
     *         {@code SampleActionIterator}
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);

        this.actionIterator = findRealAncestorWithClass(this,
                SampleActionIterator.class);
        if (this.actionIterator == null) {
            throw new IllegalStateException();
        }

        return rc;
    }

    /**
     * {@inheritDoc}; this versionion returns a collection of all the files
     * added or removed during the action for the current iteration of the
     * surrounding {@code SampleActionIterator}, based on whether the 'mode' is
     * set to {@code ADDED_FILES} or {@code REMOVED_FILES}.
     * 
     * @throws IllegalStateException if 'mode' hasn't been set
     */
    @Override
    protected Collection<? extends SampleDataFile> fetchFiles()
            throws JspException {
        switch (this.mode) {
            case ADDED_FILES:
                return this.actionIterator.getFilesAddedDuringAction();
            case REMOVED_FILES:
                return this.actionIterator.getFilesRemovedDuringAction();
            case MODIFIED_FILES:
                return this.actionIterator.getFilesModifiedDuringAction();
        }
        throw new IllegalStateException();
    }

    /** {@inheritDoc} */
    @Override
    public HtmlPageElement generateCopy(String newId, Map map) {
        SampleActionFileIterator dc
                = (SampleActionFileIterator) super.generateCopy(newId, map);
        
        dc.actionIterator = (SampleActionIterator) map.get(this.actionIterator);
        
        return dc;
    }
}
