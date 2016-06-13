/*
 * Reciprocal Net project
 * 
 * PrimaryDirectory.java
 *
 * 29-May-2003: ekoperda wrote first draft
 * 07-Jan-2004: ekoperda changed package references to match source tree
 *              reorganization
 * 12-May-2006: jobollin reformatted the source and modified some docs
 */

package org.recipnet.site.core.util;

import java.io.File;
import org.recipnet.site.shared.db.LabInfo;
import org.recipnet.site.shared.db.RepositoryHoldingInfo;
import org.recipnet.site.shared.db.SampleInfo;

/**
 * A core-internal container class that describes the primary repository
 * directory for a particular sample (for which the local site is
 * authoritative). The directory's contents (i.e. data files associated with the
 * sample) are not described. Helper functions exist on this class to allow the
 * primary directory to be identified and located in a variety of contexts.
 */
public class PrimaryDirectory {

    /** Identifies the sample associated with this primary directory. */
    public int sampleId;

    /**
     * The name of the "lab directory" within the local site's repository,
     * underneath which the primary directory is located. This value should be
     * the same as the {@code directoryName} field from the {@code LabInfo}
     * object for the associated sample's originating lab. The string may not
     * contain any slashes (filesystem separator characters).
     */
    public String labDirectoryName;

    /**
     * The "extension path" below the "lab directory" used when computing the
     * filesystem location of the primary directory. May be null if no extension
     * path is used. This value should be the same as the {@code extensionPath}
     * field from the local {@code RepositoryHoldingInfo} object for this
     * sample. The string may contain slashes (filesystem separator characters),
     * but must not begin or end with one.
     */
    public String holdingExtensionPath;

    /**
     * The localLabId for the sample associated with this primary directory.
     * This is also the unqualified name of the primary directory. This value
     * should be the same as the {@code localLabId} field from the associated
     * sample's {@code SampleInfo} object. The string may not contain any
     * slashes (filesystem separator characters).
     */
    public String sampleLocalLabId;

    /**
     * Initializes a new PrimaryDirectory with the specified sample and lab
     * information.  It is assumed that no holding record is available.
     * 
     * @param sample the {@code SampleInfo} object for the sample associated
     *        with this primary directory.
     * @param lab the {@code LabInfo} object for {@code sample}'s originating
     *        lab.
     * @throws IllegalArgumentException if the lab record provided does not
     *         describe the sample's originating lab.
     */
    public PrimaryDirectory(SampleInfo sample, LabInfo lab) {
        // Sanity check.
        if (sample.labId != lab.id) {
            throw new IllegalArgumentException();
        }

        this.sampleId = sample.id;
        this.labDirectoryName = lab.directoryName;
        this.holdingExtensionPath = null;
        this.sampleLocalLabId = sample.localLabId;
    }

    /**
     * Initializes a new PrimaryDirectory with the specified sample, lab, and
     * holdings information
     * 
     * @param sample the {@code SampleInfo} object for the sample associated
     *        with this primary directory.
     * @param lab the {@code LabInfo} object for {@code sample}'s originating
     *        lab.
     * @param holding the local {@code RepositoryHoldingInfo} associated with
     *        {@code sample}.
     * @throws IllegalArgumentException if the lab record provided does not
     *         describe the sample's originating lab, or if the holding record
     *         provided is not associated with the sample record provided.
     */
    public PrimaryDirectory(SampleInfo sample, LabInfo lab,
            RepositoryHoldingInfo holding) {
        // Sanity check.
        if ((sample.id != holding.sampleId) || (sample.labId != lab.id)) {
            throw new IllegalArgumentException();
        }

        this.sampleId = sample.id;
        this.labDirectoryName = lab.directoryName;
        this.holdingExtensionPath = holding.urlPath;
        this.sampleLocalLabId = sample.localLabId;
    }

    /**
     * Returns a {@code File} representing the location of this directory on the
     * file system
     * 
     * @return a {@code File} object that indicates the filesystem location of
     *         the primary repository directory. Presumably, sample data files
     *         are contained beneath this directory.
     * @param baseDirectory the base filesystem directory of the repository, as
     *        configured by the system administrator.
     */
    public File getFile(File baseDirectory) {
        File tempDirectory = new File(baseDirectory, labDirectoryName);
        
        if (holdingExtensionPath != null) {
            tempDirectory = new File(tempDirectory, holdingExtensionPath);
        }
        tempDirectory = new File(tempDirectory, sampleLocalLabId);
        
        return tempDirectory;
    }

    /**
     * Returns a {@code File} representing the location of this directory's 
     * ancestor lab directory on the file system
     * 
     * @return a {@code File} object that indicates the filesystem location of
     *         the "lab directory", somewhere above the primary repository
     *         directory. This function exists more as a convenience to callers
     *         than as an intrinsic property of a particular primary directory.
     * @param baseDirectory the base filesystem directory of the repository, as
     *        configured by the system administrator.
     */
    public File getFileForLabDirectory(File baseDirectory) {
        return new File(baseDirectory, labDirectoryName);
    }

    /**
     * Returns the URL by which this directory is accessible
     * 
     * @return a {@code String} containing this directory's URL, ending with a
     *         forward slash '/'. Presumably, files within the primary directory
     *         are also web accessible and their URLs may be computed by
     *         appending a file name to this string
     * @param baseUrl the base URL of the local site's repository, as configured
     *        by the system administrator.
     */
    public String getUrl(String baseUrl) {
        StringBuilder buf = new StringBuilder();
        
        buf.append(baseUrl);
        buf.append(this.labDirectoryName);
        buf.append("/");
        if (this.holdingExtensionPath != null) {
            buf.append(this.holdingExtensionPath);
            buf.append("/");
        }
        buf.append(this.sampleLocalLabId);
        buf.append("/");
        
        return buf.toString();
    }

    /**
     * Provides the CVS module name for this directory
     * 
     * @return the "module name" within the CVS tree by which this primary
     *         directory is known within the CVS repository. This value matches
     *         that computed by {@link SecondaryDirectory#getCvsTreeName()}, as
     *         required.
     */
    public String getCvsTreeName() {
        return String.valueOf(this.sampleId);
    }
}
