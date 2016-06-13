/*
 * Reciprocal Net Project
 *
 * TrackedFileChecker.java
 *
 * 10-Feb-2006: jobollin wrote first draft
 */

package org.recipnet.site.content.rncontrols;

import java.io.IOException;

import javax.servlet.jsp.JspException;

import org.recipnet.common.controls.AbstractChecker;
import org.recipnet.common.controls.HtmlPageElement;
import org.recipnet.site.wrapper.FileTracker;
import org.recipnet.site.wrapper.TrackedFile;

/**
 * A Checker tag that suppresses its body or not depending on the tracked file
 * (if any) associated with a specified key.  The supression decision is made
 * in the {@code FETCHING_PHASE}, based on the tag's attributes and context in
 * that phase.
 * 
 * @author jobollin
 * @version 0.9.0
 */
public class TrackedFileChecker extends AbstractChecker {
    
    /**
     * A required, transient attribute; the {@code FileTracker} key of the
     * tracked file to test, as a {@code String}
     */
    private String fileKey;
    
    /**
     * An optional, transient attribute of possible use to specify that the
     * tracked file must not only exist, but also be valid for this tag to not
     * suppress its body
     */
    private boolean requireValid;

    /**
     * {@inheritDoc}
     * 
     * @see AbstractChecker#reset()
     */
    @Override
    protected void reset() {
        super.reset();
        fileKey = null;
        requireValid = false;
    }

    /**
     * Gets the file tracking key with respect to which this checker will make
     * its decision
     * 
     * @return the {@code String} file key
     */
    public String getFileKey() {
        return fileKey;
    }

    /**
     * Sets the file tracking key with respect to which this checker will make
     * its decision
     * 
     * @param  fileKey the {@code String} to set as the key
     */
    public void setFileKey(String fileKey) {
        this.fileKey = fileKey;
    }

    /**
     * Determines whether this tag is configured to require that the referenced
     * tracked file is valid
     *  
     * @return the {@code true} to require validity, {@code false} to ignore
     *         validity
     */
    public boolean isRequireValid() {
        return requireValid;
    }

    /**
     * Sets whether this tag should require that the referenced
     * tracked file is valid
     * 
     * @param  requireValid {@code true} to require validity, {@code false} to
     *         ignore validity
     */
    public void setRequireValid(boolean requireValid) {
        this.requireValid = requireValid;
    }

    /**
     * {@inheritDoc}.  This version determines whether this tag's inclusion
     * conditions have been satisfied. 
     * 
     * @see HtmlPageElement#onFetchingPhaseAfterBody()
     */
    @Override
    public int onFetchingPhaseAfterBody() throws JspException {
        int rc = super.onFetchingPhaseAfterBody();
        String keyString = getFileKey();
        
        if ((keyString != null) && (keyString.length() > 0)) {
            try {
                FileTracker fileTracker = FileTracker.getFileTracker(
                        pageContext.getServletContext());
                TrackedFile file
                        = fileTracker.getTrackedFile(Long.parseLong(keyString));
                
                super.inclusionConditionMet =
                    ((file != null) && (!requireValid || file.isValid()));
            } catch (IOException ioe) {
                throw new JspException(ioe);
            } catch (NumberFormatException nfe) {
                super.inclusionConditionMet = false;
            }
        } else {
            super.inclusionConditionMet = false;
        }
            
        return rc;
    }

    /**
     * {@inheritDoc}.
     * 
     * @see HtmlPageElement#copyTransientPropertiesFrom(HtmlPageElement)
     */
    @Override
    protected void copyTransientPropertiesFrom(HtmlPageElement source) {
        TrackedFileChecker tfc = (TrackedFileChecker) source;
        
        super.copyTransientPropertiesFrom(source);
        setFileKey(tfc.getFileKey());
        setRequireValid(tfc.isRequireValid());
    }
}
