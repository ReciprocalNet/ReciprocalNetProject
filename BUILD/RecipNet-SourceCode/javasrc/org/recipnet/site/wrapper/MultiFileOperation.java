/*
 * Reciprocal Net Project
 * MultiFileOperation.java
 *
 * 04-Jul-2005: midurbin wrote first draft
 */
package org.recipnet.site.wrapper;

import java.util.ArrayList;
import java.util.Collection;
import org.recipnet.site.shared.db.SampleInfo;

/**
 * A <code>WorkflowActionPersistedOperation</code> that involves one or more
 * files.
 */
public class MultiFileOperation extends WorkflowActionPersistedOperation {

    /**
     * The names of the files included in the workflow action.  These are
     * expected to be names for files that exist on the sample-version included
     * in this <code>PersistedOperation</code>.
     */
    private Collection<String> filenames;

    /** A constructor. */
    public MultiFileOperation(long expirationInterval, SampleInfo si,
            String comments, int userId, Collection<String> filenames) {
        super(expirationInterval, si, comments, userId);
        this.filenames = filenames;
    }

    /**
     * Gets a new <code>Collection</code> containing the filename for each file
     * that is to be included in this operation.
     */
    public Collection<String> getFilenames() {
        return new ArrayList<String>(this.filenames);
    }

}
