/*
 * Reciprocal Net Project
 * @(#)RepositoryDirectoryNotFoundException.java
 *
 * 29-May-2003: ekoperda wrote first draft
 * 07-Jan-2004: ekoperda changed package references to match source tree
 *              reorganization
 */

package org.recipnet.site.core;
import org.recipnet.site.shared.RepositoryFiles;
import org.recipnet.site.shared.db.SampleInfo;

/**
 * A subclass of <code>ResourceNotFoundException</code> thrown when an
 * operation could not be completed because no primary repository directory
 * could be found for the specified sample.  Information about whether or not
 * a local repository holding record was available for the sample is included. 
 * Optionally, a filesystem location where the primary directory may be created
 * may be included as a suggestion to the caller.  
 */
public class RepositoryDirectoryNotFoundException 
        extends ResourceNotFoundException {
    /** Set at construction time. */
    private boolean doesHoldingRecordExist;

    /** Set at construction time. */
    private boolean isSuggestionAvailable;

    /** Set at construction time. */
    private boolean doesSuggestedDirectoryExist;

    /** Set at construction time. */
    private String suggestedDirectoryFirstPart;

    /** Set at construction time. */
    private String suggestedDirectoryExtensionPath;

    /** Set at construction time. */
    private String suggestedDirectoryLastPart;

    /** 
     * Constructor for use when no repository holding record is available for
     * the sample and no suggested location is available.
     * @param sample the sample for which no primary repository directory is
     *     available.  The <code>sampleId</code> field within this container
     *     may be informative to the catcher of this exception object.
     */
    public RepositoryDirectoryNotFoundException(SampleInfo sample) {
	super(sample);
	this.doesHoldingRecordExist = false;

	this.isSuggestionAvailable = false;
	this.doesSuggestedDirectoryExist = false;
	this.suggestedDirectoryFirstPart = null;
	this.suggestedDirectoryExtensionPath = null;
	this.suggestedDirectoryLastPart = null;
    }

    /**
     * Constructor for use when a suggested location is available, typically
     * when a <code>RepositoryFiles</code> object could not be fetched.  The
     * suggested directory location is specified in three pieces.
     * @param repositoryFiles an (invalid) <code>RepositoryFiles</code>
     *     container object that identifies the <code>RepositoryFiles</code>
     *     object that could not be fetched.  The <code>sampleId</code> and
     *     <code>sampleHistoryId</code> fields within this container may be
     *     information to the catcher of this exception object.
     * @param doesHoldingRecordExist should be true if a local repository
     *     holding record for the requested sample existed, false otherwise.
     *     This knowledge may be useful to the catcher of this exception
     *     object.
     * @param doesSuggestedDirectoryExist should be true if the suggested
     *     directory already exists on the filesystem; false otherwise.
     * @param suggestedDirectoryFirstPart the first part of the suggested
     *     directory location on the filesystem; should end with a trailing
     *     slash.
     * @param suggestedDirectoryExtensionPath the second part (extensionPath)
     *     of the suggested directory location on the filesystem.  May be null
     *     if a null extensionPath is being suggested.  If not null, should end
     *     with a trailing slash.
     * @param suggestedDirectoryLastPart the third and last part of the
     *     suggested directory location on the filesystem.  Should neither
     *     begin nor end with a slash.
     */
    public RepositoryDirectoryNotFoundException(
            RepositoryFiles repositoryFiles, boolean doesHoldingRecordExist, 
            boolean doesSuggestedDirectoryExist, 
            String suggestedDirectoryFirstPart, 
            String suggestedDirectoryExtensionPath, 
            String suggestedDirectoryLastPart) {
	super(repositoryFiles);
	this.doesHoldingRecordExist = doesHoldingRecordExist;

	this.isSuggestionAvailable = true;
	this.doesSuggestedDirectoryExist = doesSuggestedDirectoryExist;
	this.suggestedDirectoryFirstPart = suggestedDirectoryFirstPart;
	this.suggestedDirectoryExtensionPath = suggestedDirectoryExtensionPath;
	this.suggestedDirectoryLastPart = suggestedDirectoryLastPart;
    }

    /**
     * @return the <code>doesHoldingRecordExist</code> property value specified
     *     at construction time.
     */
    public boolean doesHoldingRecordExist() {
	return doesHoldingRecordExist;
    }

    /**
     * @return the <code>isSuggestionAvailable</code> property value specified 
     *     at construction time.
     */
    public boolean isSuggestionAvailable() {
	return isSuggestionAvailable;
    }

    /**
     * @return the <code>doesSuggestedDirectoryExist</code> property value
     *     specified at construction time.
     * @throws IllegalStateException if no suggestion is available (as
     *     indicated by <code>isSuggestionAvailable()</code>.
     */
    public boolean doesSuggestedDirectoryExist() {
	if (!isSuggestionAvailable) {
	    throw new IllegalStateException();
	}
	return doesSuggestedDirectoryExist;
    }

    /**
     * @return the <code>suggestedDirectoryFirstPart</code> property value
     *     specified at construction time.
     * @throws IllegalStateException if no suggestion is available (as
     *     indicated by <code>isSuggestionAvailable()</code>.
     */
    public String getSuggestedDirectoryFirstPart() {
	if (!isSuggestionAvailable) {
	    throw new IllegalStateException();
	}
	return suggestedDirectoryFirstPart;
    }

    /**
     * @return the <code>suggestedDirectoryExtensionPath</code> property 
     *     value specified at construction time.
     * @throws IllegalStateException if no suggestion is available (as
     *     indicated by <code>isSuggestionAvailable()</code>.
     */
    public String getSuggestedDirectoryExtensionPath() {
	if (!isSuggestionAvailable) {
	    throw new IllegalStateException();
	}
	return suggestedDirectoryExtensionPath;
    }

    /**
     * @return the <code>suggestedDirectoryLastPart</code> property value
     *     specified at construction time.
     * @throws IllegalStateException if no suggestion is available (as
     *     indicated by <code>isSuggestionAvailable()</code>.
     */
    public String getSuggestedDirectoryLastPart() {
	if (!isSuggestionAvailable) {
	    throw new IllegalStateException();
	}
	return suggestedDirectoryLastPart;
    }
}
