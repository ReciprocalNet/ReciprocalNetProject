/*
 * Reciprocal Net project
 * 
 * FileChecker.java
 *
 * 21-Oct-2005: midurbin wrote first draft
 * 11-Jan-2006: jobollin added support for checking the file name vs. a required
 *              extension; updated docs; added @Override annotations as
 *              appropriate
 */

package org.recipnet.site.content.rncontrols;

import java.util.Map;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import org.recipnet.common.controls.AbstractChecker;
import org.recipnet.common.controls.HtmlPageElement;
import org.recipnet.site.shared.SampleDataFile;

/**
 * A 'checker' tag that evaluates its body based on characteristics of a
 * {@code SampleDataFile} obtained from a surrounding {@code FileContext}.
 */
public class FileChecker extends AbstractChecker {

    /** A reference to the {@code FileContext} surrounding this tag. */
    private FileContext fileContext;

    /**
     * An 'inclusion' property, that when set to true instructs this tag to
     * evaluate its body only when the {@code SampleDataFile} returned by
     * the surrounding {@code FileContext} contains a non-null
     * description.
     */
    private boolean includeOnlyIfFileHasDescription;
    
    /**
     * A string that, if not {@code null}, must match the end of the name of
     * the file being checked for that file to satisfy this checker's condition.
     */
    private String requiredExtension;

    /**
     * Determines whether this checker is configured to test for the presence of
     * file descriptions
     * 
     * @return {@code true} if this handler is configured to include files only
     *         if they have descriptions associated with them; {@code false}
     *         otherwise
     */
    public boolean isIncludeOnlyIfFileHasDescription() {
        return this.includeOnlyIfFileHasDescription;
    }

    /**
     * Configures whether this tag should check whether files have descriptions
     * 
     * @param  include {@code true} if this tag should check files for
     *         descriptions, rejecting those without; {@code false} if it
     *         should ignore file descriptions altogether
     */
    public void setIncludeOnlyIfFileHasDescription(boolean include) {
        this.includeOnlyIfFileHasDescription = include;
    }

    /**
     * Gets the filename extension required to satisfy this checker's condition
     * 
     * @return the extension with which filenames are required to end in order
     *         to meet this checker's criterion, or {@code null} if filename
     *         extension checking is not being performed
     */
    public String getRequiredExtension() {
        return requiredExtension;
    }

    /**
     * Sets the filename extension required to satisfy this checkers condition
     * 
     * @param  requiredExtension the {@code String} to as the required filename
     *         extension checked by this checker, or {@code null} to disable
     *         filename extension checking
     */
    public void setRequiredExtension(String requiredExtension) {
        this.requiredExtension = requiredExtension;
    }

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.fileContext = null;
        this.requiredExtension = null;
    }

    /**
     * {@inheritDoc}.  This version gets the {@code SampleDataFile} from the
     * {@code FileContext} and uses it to determine whether the body of this tag
     * should be suppressed.
     */
    @Override
    public int onFetchingPhaseBeforeBody() throws JspException {
        int rc = super.onFetchingPhaseBeforeBody();
        SampleDataFile sdf = this.fileContext.getSampleDataFile();
        boolean fileDescriptionPredicate
                = (!this.includeOnlyIfFileHasDescription
                         || ((sdf != null) && (sdf.getDescription() != null)));
        boolean filenameExtensionPredicate = ((requiredExtension == null)
                || ((sdf != null) && sdf.getName().endsWith(requiredExtension)));
        
        super.inclusionConditionMet
                = fileDescriptionPredicate && filenameExtensionPredicate;
        
        return rc;
    }

    /**
     * {@inheritDoc}.  This implementation finds a reference to the innermost
     * surrounding {@code FileContext}.
     * 
     * @throws IllegalStateException if this tag is not nested within a
     *     {@code FileContext}.
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);
        
        this.fileContext = findRealAncestorWithClass(this, FileContext.class);
        if (this.fileContext == null) {
            throw new IllegalStateException();
        }
        
        return rc;
    }

    /**
     * {@inheritDoc}.  This implementation delegates to the superclass then
     * updates references ancestor tags using the 'map' parameter that was
     * populated by the superclass' implementation as well as the caller.
     */
    @Override
    public HtmlPageElement generateCopy(String newId, Map map) {
        FileChecker dc = (FileChecker) super.generateCopy(newId, map);
        dc.fileContext = (FileContext) map.get(this.fileContext);
        return dc;
    }
}
