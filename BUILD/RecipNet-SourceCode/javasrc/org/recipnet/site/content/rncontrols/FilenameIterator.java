/*
 * Reciprocal Net project
 * 
 * FilenameIterator.java
 * 
 * 04-Aug-2005: midurbin wrote the first draft
 * 10-Nov-2005: midurbin set the default value of the 'sortFilesByName'
 *              property to false
 * 11-Jan-2006: jobollin removed unusued imports
 * 05-May-2006: jobollin formatted the source
 */

package org.recipnet.site.content.rncontrols;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.recipnet.site.shared.SampleDataFile;

/**
 * A {@code FileIterator} that iterates through only the files named by the
 * innermost surrounding
 * {@link MultiFilenameContext#getFilenames() MultiFilenameContext}. This tag
 * fetches a {@code RepositoryFiles} object so that it can provide 'settled'
 * files to nested tags. This tag must be nested within a
 * {@code MultiFilenameContext} that provides filenames for existing files on
 * the current version of the {@code SampleContext} recognized by this tag.
 * Unlike its superclass, this tag's 'sortFilesByName' property defaults to
 * {@code false}.
 */
public class FilenameIterator extends FileIterator {

    /**
     * A reference to the nearest {@code MultiFilenameContext} tag that encloses
     * this one. Set by {@code onRegistrationPhaseBeforeBody()}.
     */
    private MultiFilenameContext multiFilenameContext;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.setSortFilesByName(false);
        this.multiFilenameContext = null;
    }

    /**
     * {@code inheritDoc}; this version determines the
     * {@code multiFilenameContext} and then delegates back to its superclass.
     * 
     * @throws IllegalStateException if this tag is not nested within a
     *         {@code multiFilenameContext}.
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);
        this.multiFilenameContext = findRealAncestorWithClass(this,
                MultiFilenameContext.class);
        if (this.multiFilenameContext == null) {
            throw new IllegalStateException();
        }

        return rc;
    }

    /**
     * {@inheritDoc}; this version consults the {@code MultiFilenameContext} to
     * determine the names of the files to include, and then fetches the
     * corresponding files.
     * 
     * @throws IllegalArgumentException if any of the filenames provided by the
     *         {@code MultiFilenameContext} could not be located in the current
     *         repository files.
     */
    @Override
    protected Collection<? extends SampleDataFile> fetchFiles()
            throws JspException {
        if (this.multiFilenameContext.getFilenames() == null) {
            return new ArrayList<SampleDataFile>();
        }

        Collection<String> filenames = this.multiFilenameContext.getFilenames();
        Collection<SampleDataFile> result = new ArrayList<SampleDataFile>();
        for (SampleDataFile dataFile : super.fetchFiles()) {
            if (filenames.contains(dataFile.getName())) {
                filenames.remove(dataFile.getName());
                result.add(dataFile);
            }
        }
        if (!filenames.isEmpty()) {
            // All of the files could not be found on the current sample
            // version either because it's changed since the files were
            // committed or because we haven't fetched the most recent version
            // (possibly due to invalid cache entries).
            throw new IllegalArgumentException();
        }
        return result;
    }
}
