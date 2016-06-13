/*
 * Reciprocal Net project
 * 
 * RepositoryFiles.java
 *
 * 29-May-2003: ekoperda wrote first draft
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.container package
 *              to org.recipnet.site.shared
 * 25-Jun-2004: ekoperda fixed bug #1260 by marking filesIterator and
 *              currentFile as transient
 * 01-Mar-2005: ekoperda added per-file historical information support, method
 *              getFileWithName(), and rewrote to use generics
 * 02-May-2005: ekoperda made the internal Record class publicly accessible and
 *              removed obsolete iteration code
 * 16-Jun-2005: ekoperda fixed bug #1617 in constructor for internal Record
 *              class
 * 21-Oct-2005: midurbin added support for file descriptions
 * 25-May-2006: jobollin reformatted the source and updated some docs
 */

package org.recipnet.site.shared;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.recipnet.site.UnexpectedExceptionException;
import org.recipnet.site.shared.db.RepositoryFileInfo;
import org.recipnet.site.shared.db.SampleHistoryInfo;
import org.recipnet.site.shared.db.SampleInfo;

/**
 * A container class that describes the set of data files in the repository
 * available for a particular sample/version. Additionally, URL's by which each
 * file may be accessed by web clients are provided. The files may be
 * distributed between the sample's primary repository directory, the
 * sample/version's secondary directory. Some files (typically large ones for
 * prior sample version) may not be immediately accessible, but can be made
 * accessible upon further request. The primary means for obtaining one of these
 * objects is to invoke
 * {@link org.recipnet.site.core.RepositoryManager#getRepositoryFiles(int, int,
 * boolean) RepositoryManager.getRepositoryFiles()}.
 */
public class RepositoryFiles implements Serializable {
    
    /** Identifies the sample whose data files this object describes. */
    private int sampleId;

    /**
     * Identifies the version of the sample whose data files this object
     * describes.
     */
    private int sampleHistoryId;

    /**
     * A {@code Collection} of {@code Record} objects that describes the set of
     * files available. Set at construction time and altered via
     * {@code addFile()} and {@code initForIteration()}. The reference can't
     * have type {@code Collection} because we need to sort the list in
     * {@code initForIteration()}.
     */
    private Collection<Record> files;

    /**
     * Constructor with no arguments; necessary for serialization to work. The
     * object created has invalid property values. Callers other than the
     * serialization subsystem should not invoke this constructor.
     */
    public RepositoryFiles() {
        this(SampleInfo.INVALID_SAMPLE_ID,
                SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID);
    }

    /**
     * Constructor that creates an object representing an empty file set. File
     * records may be added later via {@code addFile()}. Architecturally
     * speaking, this object is intended to be constructed only by
     * {@code RepositoryManager} and its agents.
     * 
     * @param sampleId identifies the sample whose data files this object
     *        describes.
     * @param sampleHistoryId identifies the version of the sample whose data
     *        files this object describes.
     */
    public RepositoryFiles(int sampleId, int sampleHistoryId) {
        this.sampleId = sampleId;
        this.sampleHistoryId = sampleHistoryId;
        this.files = new ArrayList<Record>();
    }

    /**
     * Constructor that creates an object representing a populated file set.
     * File records are populated based on the array of {@code File} objects
     * specified. It is assumed that each {@code File} object resides within the
     * same filesystem directory, and that directory (and the enumerated files
     * within it) is web-accessible. The availability {@code PRIMARY_DIRECTORY}
     * is assumed for each file. Historical data is not supplied when this
     * constructor is used; a caller who desired this object to contain
     * historical information would need to invoke
     * {@code supplyHistoricalInformation()} subsequently.
     * <p>
     * Architecturally speaking, this object is itnended to be constructed only
     * by {@code RepositoryManager} and its agents.
     * 
     * @param sampleId identifies the sample whose data files this object
     *        describes.
     * @param sampleHistoryId identifies the version of the sample whose data
     *        files this object describes.
     * @param baseUrl the URL by which the primary repository directory is
     *        web-accessible. Individual file names are appended to this value
     *        to compute the URL for each file.
     * @param files an array of {@code File} objects, presumably all residing
     *        within the specified sample's primary repository directory.
     *        {@code File} objects within this array that are not real files or
     *        are hidden (as reported by {@code File.isHidden()} are ignored.
     */
    public RepositoryFiles(int sampleId, int sampleHistoryId, String baseUrl,
            File[] files) {
        this(sampleId, sampleHistoryId);
        for (File file : files) {
            if (file.isFile() && !file.isHidden()) {
                addFile(new Record(Availability.PRIMARY_DIRECTORY, file,
                        baseUrl, null, this));
            }
        }
    }

    /**
     * Appends a new record describing a particular repository data file to the
     * collection encapsulated by this object. Architecturally speaking, this
     * method is intended to be invoked only by {@code RepositoryManager} and
     * its agents.
     */
    public void addFile(Record record) {
        this.files.add(record);
    }

    /**
     * Supplies historical information regarding a particular sample's data
     * files to this object. This enables subsequent callers to receive
     * substantive return values from
     * {@code Record.getOriginalSampleHistoryId()}. Architecturally speaking,
     * this method is intended to be invoked only by {@code RepositoryManager}
     * and its agents.
     * 
     * @param repositoryFileInfos a {@code Collection} of zero or more
     *        {@code RepositoryFileInfo} objects that have been fetched from the
     *        database and that correspond to the sample id and sample history
     *        id values represented by this object. Select contents of this
     *        collection are copied and the collection itself is not modified.
     */
    public void supplyHistoricalInformation(
            Collection<RepositoryFileInfo> repositoryFileInfos) {
        /*
         * TODO: this algorithm is just a linear search. Consider replacing it
         * with something faster.
         */
        for (RepositoryFileInfo rfi : repositoryFileInfos) {
            Record rec = getRecordWithName(rfi.fileName);
            
            if (rec != null) {
                // Replace the old record with a new, more complete one.
                this.files.remove(rec);
                this.files.add(new Record(rec, rfi));
            }
        }
    }

    /**
     * @return the {@code sampleId} property value set at construction time.
     */
    public int getSampleId() {
        return this.sampleId;
    }

    /**
     * @return the {@code sampleHistoryId} property value set at construction
     *         time.
     */
    public int getSampleHistoryId() {
        return this.sampleHistoryId;
    }

    /**
     * Returns a collection of records that describes all the repository data
     * files that are available for the sample-version associated with this
     * object.
     * 
     * @return a {@code Collection} containing zero or more {@code Record}
     *         objects, each of which describes one repository data file. The
     *         collection is unmodifiable for safety.
     */
    public Collection<Record> getRecords() {
        return Collections.unmodifiableCollection(this.files);
    }

    /**
     * Scans the collection of repository data files encapsulated by this object
     * and returns the first {@code Record} that has a file name equal to
     * {@code filename}.
     * 
     * @return the first matching {@code Record}, or null if no matching
     *         records were found.
     * @param filename the file name to search for.
     */
    public Record getRecordWithName(String filename) {
        for (Record rec : this.files) {
            if (rec.name.equals(filename)) {
                return rec;
            }
        }
        return null;
    }

    /**
     * A nested container class that describes a single available repository
     * file. Each instance describes a <i>settled</i> file, with the meaning
     * defined in {@code SampleDataFile}. These objects are immutable once
     * constructed.
     */
    public static class Record implements Cloneable, SampleDataFile,
            Serializable {
        /** Variables used to implement {@code SampleDataFile}. */
        private int originalSampleHistoryId;

        private String name;

        private long size;

        private String url;

        private String description;

        /**
         * One of the availability constants that identifies the place from
         * which the file's data may be fetched.
         */
        private Availability availability;

        /**
         * A reference to the RepositoryFiles object that contains this file
         * record.
         */
        private RepositoryFiles repositoryFiles;

        /**
         * Constructor that uses an existing {@code File} on the filesystem as
         * the basis for the new record and optionally accepts historical
         * information for the file.
         * 
         * @param availability the availability of this file.
         * @param realFile the {@code File} object that exists on the
         *        filesystem. The file typically would sit within a particular
         *        sample's primary repository directory or secondary repository
         *        directory.
         * @param baseUrl the URL by which the file's parent directory is
         *        web-accessible. {@code realFile}'s name is appended to this
         *        value to compute the whole URL for the file. This value may be
         *        null, in which case the {@code url} field is set to null and
         *        the file is presumed to be not web-accessible at this time.
         * @param rfi a {@code RepositoryFileInfo} object that describes the
         *        file in the database. This argument may be null, but in this
         *        case no historical information nor the description for the
         *        file will be available.
         * @param rf a reference to the {@code RepositoryFiles} object to which
         *        this record is being added.
         */
        public Record(Availability availability, File realFile, String baseUrl,
                RepositoryFileInfo rfi, RepositoryFiles rf) {
            this(SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID,
                    realFile.getName(), realFile.length(), null, null,
                    availability, rf);
            if (baseUrl != null) {
                this.url = baseUrl + realFile.getName();
            }
            if (rfi != null) {
                this.originalSampleHistoryId = rfi.originalSampleHistoryId;
                this.description = rfi.description;
            }
        }

        /**
         * Constructor that uses an existing {@code RepositoryFileInfo} object
         * from the database as the basis for the new record. No actual file
         * need exist on the filesystem. Historical information for the file is
         * supplied.
         * 
         * @param availability the availability of this file -- should be
         *        {@code FILE_AVAILABLE_UPON_REQUEST} unless the file actually
         *        exists on the filesystem.
         * @param fileInfo the {@code RepositoryFileInfo} object that describes
         *        the data file being added.
         * @param baseUrl the URL by which the file's parent directory is
         *        web-accessible. {@code fileInfo}'s name is appended to this
         *        value to compute the whole URL for the file. This value may be
         *        null, in which case the file record's {@code url} field is set
         *        to null and the file is presumed not web-accessible at this
         *        time.
         * @param rf a reference to the {@code RepositoryFiles} object to which
         *        this record is being added.
         */
        public Record(Availability availability, RepositoryFileInfo fileInfo,
                String baseUrl, RepositoryFiles rf) {
            this(fileInfo.originalSampleHistoryId, fileInfo.fileName,
                    fileInfo.fileBytes, null, fileInfo.description,
                    availability, rf);
            if (baseUrl != null) {
                this.url = baseUrl + fileInfo.fileName;
            }
        }

        /**
         * Constructor that creates a copy of another {@code Record} object with
         * two differences. This object's {@code originalSampleHistoryId} and
         * {@code descriptioN} are copied from {@code rfi}, while the
         * corresponding fields in {@code rec} are ignored. Historical
         * information for the file is supplied.
         * 
         * @param rec the {@code Record} to copy from.
         * @param rfi the {@code RepositoryFileInfo} object from which the
         *        {@code originalSampleHistoryId} value is copied.
         * @throws IllegalArgumentException if the repository data files
         *         described by {@code rec} and {@code rfi} have different
         *         names.
         */
        public Record(Record rec, RepositoryFileInfo rfi) {
            this(rfi.originalSampleHistoryId, rec.name, rec.size, rec.url,
                    rfi.description, rec.availability, rec.repositoryFiles);
            if (!this.name.equals(rfi.fileName)) {
                throw new IllegalArgumentException();
            }
        }

        /**
         * Constructor with which all of this class's member variables are set
         * explicitly.
         */
        public Record(int originalSampleHistoryId, String name, long size,
                String url, String description, Availability availability,
                RepositoryFiles rf) {
            this.originalSampleHistoryId = originalSampleHistoryId;
            this.name = name;
            this.size = size;
            this.url = url;
            this.description = description;
            this.availability = availability;
            this.repositoryFiles = rf;
        }

        /** @inheritDoc */
        public int getSampleId() {
            return this.repositoryFiles.getSampleId();
        }

        /** @inheritDoc */
        public int getSampleHistoryId() {
            return this.repositoryFiles.getSampleHistoryId();
        }

        /**
         * {@inheritDoc}.  This version sometimes returns a sensical value and
         * sometimes doesn't, depending upon which
         * version of the constructor was utilized. Constructors that do not
         * supply "historical information" to this class will cause this
         * function to return {@code INVALID_SAMPLE_HISTORY_ID}.
         */
        public int getOriginalSampleHistoryId() {
            return this.originalSampleHistoryId;
        }

        /** @inheritDoc */
        public String getName() {
            return this.name;
        }

        /** @inheritDoc */
        public long getSize() {
            return this.size;
        }

        /** @inheritDoc */
        public String getUrl() {
            return this.url;
        }

        /** @inheritDoc */
        public String getDescription() {
            return this.description;
        }

        /**
         * {@inheritDoc}.  This version always returns {@code true}.
         */
        public boolean isSettled() {
            return true;
        }

        /**
         * {@inheritDoc}.  This version always returns {@code false}.
         */
        public boolean isProvisional() {
            return false;
        }

        /**
         * Returns the {@code Availability} assigned to this
         * {@code RepositoryFiles}
         * 
         * @return the {@code Availability}
         */
        public Availability getAvailability() {
            return this.availability;
        }

        /**
         * Returns the {@code RepositoryFiles} object associated with this one
         * 
         * @return the {@code RepositoryFiles} object
         */
        public RepositoryFiles getRepositoryFiles() {
            return this.repositoryFiles;
        }

        /**
         * Determines whether the specified object is equal to this one, which
         * is the case if it is also a {@code Record} and its {@code name} field
         * is equal to this {@code Record}'s
         * 
         * @param  o the {@code Object} to compare to this one
         * @return {@code true} if the specified object is equal to this one, or
         *         {@code false} if not
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            } else if (o instanceof Record) {
                return this.name.equals(((Record) o).name);
            } else {
                return false;
            }
        }

        /**
         * Computes and returns a hash code for this object.  This
         * implementation is consistent with {@link #equals(Object)}.
         * 
         *  @return the hash code
         */
        @Override
        public int hashCode() {
            return this.name.hashCode();
        }

        /**
         * Creates and returns a new {@code Record} equal to this one but
         * distinct from it
         * 
         * @return the {@code Record} clone
         */
        @Override
        public Record clone() {
            try {
                return (Record) super.clone();
            } catch (CloneNotSupportedException ex) {
                // Can't happen because Record's are Cloneable
                throw new UnexpectedExceptionException(ex);
            }
        }
    }

    /**
     * Enumeration that describes the various levels of availability that a
     * single repository data file may have.
     */
    public static enum Availability {

        /**
         * An {@code Availability} representing that a file (version) is
         * available from its sample's primary directory
         */
        PRIMARY_DIRECTORY,

        /**
         * An {@code Availability} representing that a file (version) is
         * available from a secondary directory
         */
        SECONDARY_DIRECTORY,

        /**
         * An {@code Availability} representing that a file (version) can be
         * made available from a secondary directory by authorized request
         */
        ON_REQUEST
    }
}
