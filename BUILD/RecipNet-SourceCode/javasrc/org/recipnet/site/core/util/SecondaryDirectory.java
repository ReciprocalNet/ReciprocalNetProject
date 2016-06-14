/*
 * Reciprocal Net project
 * 
 * SecondaryDirectory.java
 *
 * 29-May-2003: ekoperda wrote first draft
 * 07-Jan-2004: ekoperda changed package references to match source tree
 *              reorganization
 * 11-May-2006: jobollin reformatted the source; removed unused imports;
 *              implemented generics
 * 15-Jun-2006: jobollin updated the docs of getKey(int, int)
 * 07-Jan-2008: ekoperda fixed bug #1870 in containsFile()
 */

package org.recipnet.site.core.util;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import org.recipnet.site.shared.db.RepositoryFileInfo;

/**
 * A core-internal container class that describes the secondary repository
 * directory for a particular sample/version (for which the local site is
 * authoritative). The directory's contents (i.e. data files associated with
 * the same) are described as well. Note that the secondary repository
 * directory described by this object need not exist on the filesystem, or if
 * it does exist it need not contain every data file described by this object:
 * this object merely describes what the complete secondary directory for a
 * particular sample/version *would* look like if *did* exist.
 */
public class SecondaryDirectory {

    /** Identifies the sample associated with this secondary directory. */
    public int sampleId;

    /**
     * Identifies the particular version of the associated sample associated
     * with this secondary directory.
     */
    public int sampleHistoryId;

    /**
     * A {@code Collection} of zero or more {@code RepositoryFileInfo} objects
     * that comprises the set of all data files available for the
     * sample/version described by this object.
     */
    public Collection<RepositoryFileInfo> filesAvailable;

    /**
     * Initializes a new {@code SecondaryDirectory} with the specified
     * parameters
     * 
     * @param sampleId identifies the sample to be assocaited with this
     *        secondary directory.
     * @param sampleHistoryId identifies the particular version of the
     *        associated sample to be associated with this secondary directory.
     * @param filesAvailable a {@code Collection} of zero or more
     *        {@code RepositoryFileInfo} objects that comprises the set of all
     *        data files available for the sample/version described by this
     *        object. Presumably this information would be have been fetched by
     *        the database, perhaps via
     *        {@code RepositoryManager.dbFetchFilesForSample()}.
     */
    public SecondaryDirectory(int sampleId, int sampleHistoryId,
            Collection<? extends RepositoryFileInfo> filesAvailable) {
        this.sampleId = sampleId;
        this.sampleHistoryId = sampleHistoryId;
        this.filesAvailable 
                = new ArrayList<RepositoryFileInfo>(filesAvailable);
    }

    /**
     * Detects whether the specified file exists (or would exist) in the actual
     * secondary repository directory described by this object. A match is
     * considered to have occurred if filenames match.
     * 
     * @return true if a matching file was found; false otherwise.
     * @param file a file in the filesystem that is to be checked for inclusion
     *        within a secondary directory described by this object.
     */
    public boolean containsFile(File file) {
        String filename = file.getName().trim();
        for (RepositoryFileInfo fileInfo : filesAvailable) {
            if (filename.equals(fileInfo.fileName)) {
                return true;
            }
        }      
        return false;
    }

    /**
     * @return a reference to the first {@code RepositoryFileInfo} object
     *         within {@code filesAvailable} that has the file name
     *         {@code fileName}, or {@code null} if no such
     *         {@code RepositoryFileInfo} object could be found.
     */
    public RepositoryFileInfo findFileWithName(String fileName) {
        for (RepositoryFileInfo file : filesAvailable) {
            if (file.fileName.equals(fileName)) {
                return file;
            }
        }
        
        return null;
    }

    /**
     * @return a {@code File} object that indicates the proper filesystem
     *         location of the secondary repository directory, regardless of
     *         whether or not the directory actually exists. Presumably, sample
     *         data files would be contained beneath this directory.
     * @param baseDirectory the base filesystem directory of the repository, as
     *        configured by the system administrator.
     */
    public File getFile(File baseDirectory) {
        return new File(new File(getFileForWholeArea(baseDirectory),
                String.valueOf(this.sampleId)),
                String.valueOf(this.sampleHistoryId));
    }

    /**
     * @return a {@code File} object that indicates the filesystem location of
     *         the "temporary files area" within the repository, somwhere above
     *         the secondary repository directory. This function exists more as
     *         a convenience to callers than as an intrinsic property of a
     *         particular primary directory. The current always returns a
     *         directory called 'temp' underneath {@code baseDirectory}.
     */
    public static File getFileForWholeArea(File baseDirectory) {
        return new File(baseDirectory, "temp");
    }

    /**
     * @return a URL by which the secondary directory is web-accessible (or
     *         would be web-accessible, if it existed). Always ends with a
     *         forward slash '/'. Presumably, files within the secondary
     *         directory are also web accessible and their uRL's may be
     *         computed by appending a file name to the string returned by this
     *         function.
     */
    public String getUrl(String baseUrl) {
        return baseUrl + "temp/" + String.valueOf(this.sampleId) + "/"
                + String.valueOf(this.sampleHistoryId) + "/";
    }

    /**
     * @return the "module name" within the CVS tree by which this primary
     *         directory is known within the CVS repository. The logic in this
     *         function matches that in
     *         {@code PrimaryDirectory.getCvsTreeName()}, as required.
     */
    public String getCvsTreeName() {
        return String.valueOf(this.sampleId);
    }

    /**
     * @return an integer key that uniquely identifies this object, suitable
     *         for use as a key within an {@code ObjectCache}. The current
     *         implementation always returns a sampleHistoryId.
     */
    public int getKey() {
        return getKey(this.sampleId, this.sampleHistoryId);
    }

    public static int getKey(@SuppressWarnings("unused") int sampleId,
            int sampleHistoryId) {
        
        /*
         * A sample history ID is in fact sufficient alone, as it implies a
         * sample ID.  This method takes both as parameters, however, as a
         * precaution against an as-yet unrecognized future consideration
	 * making it convenient to use the sample ID explicitly in formin the
	 * key.
         */
        
        return sampleHistoryId;
    }
}
