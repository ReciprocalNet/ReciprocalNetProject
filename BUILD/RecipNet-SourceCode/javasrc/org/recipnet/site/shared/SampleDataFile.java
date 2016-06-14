/*
 * Reciprocal Net project
 * 
 * SampleDataFile.java
 * 
 * 02-May-2005: ekoperda wrote first draft
 * 21-Oct-2005: midurbin added getDescription()
 * 01-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.site.shared;

/**
 * <p>
 * This interface is implemented by various container objects that describe
 * repository data files available for specific sample-versions. For an object
 * that implements this interface, each instance of that object describes a
 * single data file associated with one specific sample-version.
 * </p><p>
 * An instance that described a sample data file sitting in a primary repository
 * directory would be termed <i>settled</i> because the file is stored in the
 * CVS archive and is bound to a specific version of sample metadata that exists
 * in the database. Instances that described sample data files sitting in
 * secondary repository directories or that could be extracted to secondary
 * repository directories upon request also are dubbed <i>settled</i> using
 * similar logic. <i>Settled</i> sample data files may be associated with a
 * specific version of a particular sample (in fact, they often may be
 * associated with numerous such versions) and their precise moment of
 * inception, in terms of sample history IDs, can be identified.
 * </p><p>
 * By contrast, an instance may instead describe a <i>provisional</i> sample
 * data file. Such sample data files have not been written to the CVS archive,
 * do not exist in primary repository directories or secondary repository
 * directories (except perhaps as temporary, in-progress files), and are not
 * associated with a specific version of a particular sample. Files dubbed
 * <i>provisional</i> generally would be in the process of being created and
 * would be expected to metamorphasise to <i>settled</i> files at some point in
 * the future, once the genesis process had concluded.
 * </p>
 */
public interface SampleDataFile {
    public static final long INDETERMINATE_SIZE = -1;

    /**
     * Identifies the sample with which this data file is associated.
     * 
     * @return a sample id; guaranteed not to be invalid.
     */
    public int getSampleId();

    /**
     * Identifies the particular version of the sample identified by
     * {@code getSampleId()} with which this data file is associated.
     * (Frequently a single sample data file would be associated with many such
     * version numbers, but each instance of this object describes only one such
     * version.) This number is not available for <i>provisional</i> sample
     * data files.
     * 
     * @return a sample history id. This is guaranteed to be equal to
     *         {@code SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID} if and only
     *         if {@code isProvisional()} returns true.
     */
    public int getSampleHistoryId();

    /**
     * Identifies the particular version of the sample identified by
     * {@code getSampleId()} at which this data file was first created. This
     * will be less than or equal to the value returned by
     * {@code getSampleHistoryId()}. This value is never available when
     * {@code isProvisional()} returns true, and may not be available even when
     * {@code isSettled()} returns true. Callers should consult the
     * documentation of implementing classes to determine this function's
     * precise behavior.
     */
    public int getOriginalSampleHistoryId();

    /**
     * Discovers the file's simple name, as would be returned by
     * {@code File.getName()}. The file name is not adorned with any path
     * components.
     * 
     * @return the file's name. This is guaranteed not to be null.
     */
    public String getName();

    /**
     * Discovers the number of bytes in the file. Doing so is always possible
     * when {@code isSettled()} returns true but may not be possible when
     * {@code isProvisional()} returns true.
     * 
     * @return the number of bytes of data contained within the file, or
     *         {@code INDETERMINATE_SIZE} if this is not known.
     */
    public long getSize();

    /**
     * Discovers the URL from which the file's data may be downloaded. Not all
     * sample data files are web-accessible, so callers should consult the
     * documententation of implementing classes to determine this function's
     * precise behavior.
     * 
     * @return a string containing a URL, or null if the file is not
     *         web-accessible.
     */
    public String getUrl();

    /**
     * Discovers the description associated with the file. Not all data files
     * have been given descriptions so sometimes this method returns null.
     */
    public String getDescription();

    /**
     * @return true if this instance describes a <i>provisional</i> file, or
     *         false otherwise. The value will be false exactly when
     *         {@code isSettled()} returns true.
     */
    public boolean isProvisional();

    /**
     * @return true if this instance describes a <i>settled</i> file, or false
     *         otherwise. The value will be false exactly when
     *         {@code isProvisional()} returns true.
     */
    public boolean isSettled();
}
