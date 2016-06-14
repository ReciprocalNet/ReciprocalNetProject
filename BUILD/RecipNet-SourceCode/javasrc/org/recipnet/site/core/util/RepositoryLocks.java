/*
 * Reciprocal Net project
 * 
 * RepositoryLocks.java
 *
 * 29-May-2003: ekoperda wrote first draft
 * 03-Jun-2003: ekoperda fixed bug #926 in beforeFileUpload()
 * 08-Aug-2003: midurbin added revertSampleToVersionIncludingFiles()
 * 14-Jul-2003: midurbin/ekoperda fixed bug #1020 in 
 *              registerExistingDirectories()
 * 07-Jan-2004: ekoperda changed package references to match source tree
 *              reorganization
 * 26-May-2004: ekoperda changed references to obsolete SampleLock class 
 *              to reference new SampleLocks class throughout; renamed 
 *              requestCreateDataDirectory() to createDataDirectory(); 
 *              clarified class comments; made operation constants private
 * 12-Jul-2004: ekoperda altered spec of afterFileUpload() to accommodate
 *              uploading several files at once
 * 04-Aug-2005: midurbin replaced removeFile(), eradicateFile() with
 *              removeFiles(), eradicateFiles() respectively
 * 17-Oct-2005: midurbin added getAllRepositoryFileInfosForSample()
 * 21-Oct-2005: midurbin added modifyFileDescription(), modified uploadFile()
 * 12-May-2006: jobollin reformatted the source
 * 07-Jan-2008: ekoperda removed primaryDirectoryScanContinue()
 * 30-Nov-2008: ekoperda added modifyAllHoldings() and HOLDINGS_MODIFY_ALL flag
 * 02-Jan-2009: ekoperda added elevateLocalHoldingLevel() and 
 *              decreaseLocalHoldingLevel()
 */

package org.recipnet.site.core.util;
import java.io.File;
import org.recipnet.site.core.RepositoryManager;
import org.recipnet.site.core.lock.AbstractLock;
import org.recipnet.site.core.lock.GenericExclusiveLock;
import org.recipnet.site.core.lock.MultiLock;
import org.recipnet.site.core.lock.OutOfBandReadTicket;
import org.recipnet.site.core.lock.PrimaryDirectoryCreationLock;
import org.recipnet.site.core.lock.PrimaryDirectoryReadLock;
import org.recipnet.site.core.lock.PrimaryFileReadTicket;
import org.recipnet.site.core.lock.PrimaryFileWriteTicket;
import org.recipnet.site.core.lock.RepositoryTicket;
import org.recipnet.site.core.lock.SecondaryDirectoryExtractionLock;
import org.recipnet.site.core.lock.SecondaryFileReadTicket;
import org.recipnet.site.core.lock.SimpleLock;
import org.recipnet.site.core.lock.SimplePrimaryFileLock;
import org.recipnet.site.shared.RepositoryFiles;
import org.recipnet.site.shared.db.SampleInfo;
import org.recipnet.site.shared.db.UserInfo;

/**
 * A class with nothing but static functions that encapsulates
 * {@code RepositoryManager}'s locking logic and consolidates it into a single
 * place for easier maintenance. Note that the "operation constants" defined on
 * this class must be distinct from those defined by {@code SampleLocks} in
 * order to avoid locking errors.
 */
public abstract class RepositoryLocks {

    /**
     * SimpleLock operation flag that entitles the caller to fetch one or more
     * holding records from the database table {@code repositoryHoldings} and
     * optionally put them into the {@code holdingsCache}.
     */
    private static final long HOLDINGS_FETCH_ALL = SimpleLock.GEN_OP_BASE << 0;

    /**
     * SimpleLock operation flag that signals that the caller also has set at
     * least one of the following:
     *    a) a HOLDINGS_MODIFY_SPECIFIC flag for a specific sample id, or
     *    b) a HOLDINGS_MODIFY_ALL flag.
     * This flag is useful strictly for detecting conflicts between
     * HOLDINGS_FETCH_ALL and the others and does not, by itself, entitle the
     * caller to perform any operation.
     */
    private static final long HOLDINGS_MODIFY_ANY 
            = SimpleLock.GEN_OP_BASE << 1;

    /**
     * SimpleLock operation flag that entitles the caller to modify one or 
     * more holding records from the database table {@code repositoryHoldings}
     * and optionally invalidate/update the {@code holdingsCache}.
     */
    private static final long HOLDINGS_MODIFY_ALL 
            = SimpleLock.GEN_OP_BASE << 2;

    /**
     * SimpleLock operation flag that entitles the caller to use the
     * {@code holdingsComparator} object to accomplish a sort operation.
     */
    private static final long HOLDINGSCOMPARATOR_SORT
            = SimpleLock.GEN_OP_BASE << 3;

    /**
     * SimpleLock operation flag that entitles the caller to update state in the
     * {@code holdingsComparator} object (presumably to be used in future sort
     * operations).
     */
    private static final long HOLDINGSCOMPARATOR_MODIFY
            = SimpleLock.GEN_OP_BASE << 4;

    /**
     * SimpleLock operation flag that entitles the caller to invoke
     * {@code contains()} on the {@code localLabs} collection object.
     */
    private static final long LOCALLABS_CONTAINS = SimpleLock.GEN_OP_BASE << 5;

    /**
     * SimpleLock operation flag that entitles the caller to modify the contents
     * of the {@code localLabs} collection object.
     */
    private static final long LOCALLABS_MODIFY = SimpleLock.GEN_OP_BASE << 6;

    /**
     * SimpleLock operation flag that entitles the caller to fetch holding
     * records from the database table {@code repositoryHoldings} for a specific
     * sample id and optionally put them into the {@code holdingsCache}.
     */
    private static final long HOLDINGS_FETCH_SPECIFIC
            = SimpleLock.SPEC_OP_BASE << 0;

    /**
     * SimpleLock operation flag that entitles the caller to INSERT, UPDATE, or
     * DELETE rows in the {@code repositoryHoldings} table for a specific sample
     * id, updating the {@code holdingsCache} along the way. Note that any
     * caller who takes out this flag probably also needs to take out
     * HOLDINGS_MODIFY_ANY.
     */
    private static final long HOLDINGS_MODIFY_SPECIFIC
            = SimpleLock.SPEC_OP_BASE << 1;

    /**
     * SimpleLock operation flag that entitles the caller to fetch rows from the
     * {@code repositoryFiles} database table for a specific sample id, and
     * optionally put them in the {@code secondaryDirsCache}.
     */
    private static final long FILES_FETCH_SPECIFIC
            = SimpleLock.SPEC_OP_BASE << 2;

    /**
     * SimpleLock operation flag that entitles the caller INSERT/UPDATE/DELETE
     * rows in the {@code repositoryFiles} database table for a specific sample
     * id, updating the {@code secondaryDirsCache} along the way. The caller
     * must not touch rows with other sample id's.
     */
    private static final long FILES_MODIFY_SPECIFIC
            = SimpleLock.SPEC_OP_BASE << 3;

    /**
     * Creates and returns a lock representing the appropriate permissions and
     * restrictions for safely determining the specified sample's local holding
     * level
     * 
     * @param sampleId the ID of the sample for which the local holding level
     *        is to be requested
     *        
     * @return a new AbstractLock which, when active, will allow the thread
     *        holding it to safely determine the specified sample's local
     *        holding level 
     */
    public static AbstractLock getLocalHoldingLevel(int sampleId) {
        return new SimpleLock(sampleId, true, 
                HOLDINGS_FETCH_SPECIFIC,
                HOLDINGS_MODIFY_SPECIFIC 
	        | HOLDINGS_MODIFY_ALL);
    }

    public static AbstractLock getHoldings(int sampleId) {
        return new SimpleLock(sampleId, true,
                HOLDINGS_FETCH_SPECIFIC 
		| HOLDINGSCOMPARATOR_SORT,
                HOLDINGS_MODIFY_SPECIFIC 
                | HOLDINGS_MODIFY_ALL
		| HOLDINGSCOMPARATOR_MODIFY);
    }

    public static MultiLock getAllRepositoryFileInfosForSample(int sampleId) {
        return new MultiLock(
                new SimpleLock(sampleId, true,
                        HOLDINGS_FETCH_SPECIFIC 
                        | FILES_FETCH_SPECIFIC
                        | LOCALLABS_CONTAINS,
                        HOLDINGS_MODIFY_SPECIFIC 
                        | HOLDINGS_MODIFY_ALL
                        | FILES_MODIFY_SPECIFIC
                        | LOCALLABS_MODIFY),
                SampleLocks.managerGetSampleInfo(sampleId, true));
    }

    public static MultiLock getRepositoryFiles(int sampleId,
            int sampleHistoryId) {
        return new MultiLock(
                new SimpleLock(sampleId, true,
                        HOLDINGS_FETCH_SPECIFIC 
                        | FILES_FETCH_SPECIFIC
                        | LOCALLABS_CONTAINS,
                        HOLDINGS_MODIFY_SPECIFIC 
                        | HOLDINGS_MODIFY_ALL
                        | FILES_FETCH_SPECIFIC
                        | FILES_MODIFY_SPECIFIC 
			| LOCALLABS_MODIFY),
                new PrimaryDirectoryReadLock(sampleId, true, false),
                new SecondaryDirectoryExtractionLock(
                        sampleId, sampleHistoryId, UserInfo.INVALID_USER_ID),
                /*
                 * RepositoryManager will call SampleManager.getSampleInfo(), so
                 * it makes sense for it to acquire that kind of lock now.
                 */
                SampleLocks.managerGetSampleInfo(sampleId, true));
    }

    public static MultiLock createDataDirectory(int sampleId, int userId) {
        return new MultiLock(
		createDataDirectorySubpart(sampleId, userId),
                new PrimaryDirectoryCreationLock(sampleId, userId),
                /*
                 * RepositoryManager will call SampleManager.getSampleInfo() and
                 * SampleManager.putSampleInfo(), so it makes sense for it to
                 * acquire those kinds of locks now.
                 */
                SampleLocks.managerGetSampleInfo(sampleId, true),
                SampleLocks.managerPutSampleInfo(false, sampleId));
    }

    public static SimpleLock createDataDirectorySubpart(int sampleId, 
            int userId) {
	return new SimpleLock(sampleId, true,
                HOLDINGS_FETCH_SPECIFIC 
                | HOLDINGS_MODIFY_SPECIFIC
                | HOLDINGS_MODIFY_ANY 
                | LOCALLABS_CONTAINS,
                HOLDINGS_MODIFY_SPECIFIC 
                | HOLDINGS_MODIFY_ALL
		| LOCALLABS_MODIFY,
		userId);
    }

    public static MultiLock beforeFileDownload(int sampleId,
            int sampleHistoryId, String fileName, int userId) {
        return new MultiLock(
                new SimpleLock(sampleId, true,
                        HOLDINGS_FETCH_SPECIFIC 
                        | FILES_FETCH_SPECIFIC,
                        HOLDINGS_MODIFY_SPECIFIC 
                        | HOLDINGS_MODIFY_ALL 
			| FILES_MODIFY_SPECIFIC,
                        userId),
                new SimplePrimaryFileLock(sampleId, fileName, true, false,
                        false, userId),
                new SecondaryDirectoryExtractionLock(sampleId, sampleHistoryId,
                        userId),
                /*
                 * RepositoryManager will call SampleManager.getSampleInfo(), so
                 * it makes sense for it to acquire that kind of lock now.
                 */
                SampleLocks.managerGetSampleInfo(sampleId, true));
    }

    public static RepositoryTicket fileDownloadPrimary(int userId,
            long timeUntilExpiration, int sampleId, File file) {
        return new PrimaryFileReadTicket(userId, timeUntilExpiration, sampleId,
                file);
    }

    public static RepositoryTicket fileDownloadSecondary(int userId,
            long timeUntilExpiration,
            SecondaryDirectory secondaryDirectoryInfo, File file) {
        return new SecondaryFileReadTicket(userId, timeUntilExpiration,
                secondaryDirectoryInfo, file);
    }

    public static AbstractLock beforeFileUpload(int sampleId, String fileName,
            int userId) {
        return new MultiLock(
                new SimpleLock(sampleId, true,
                        HOLDINGS_FETCH_SPECIFIC 
                        | LOCALLABS_CONTAINS,
                        HOLDINGS_MODIFY_SPECIFIC 
                        | HOLDINGS_MODIFY_ALL 
			| LOCALLABS_MODIFY,
                        userId),
                new SimplePrimaryFileLock(sampleId, fileName, false, true,
                        false, userId));
    }

    public static RepositoryTicket uploadFile(int userId,
            long timeUntilExpiration, PrimaryDirectory primaryDirectoryInfo,
            String fileName, File tempFile, SampleInfo sample, String comments,
            String description, int actionCode,
            RepositoryManager repositoryManager) {
        /*
         * Continuous locking of uploaded files from beforeFileUpload() to here
         * to afterFileUpload() is provided because PrimaryFileWriteTicket
         * implements PrimaryFileLock and SimplePrimaryFileLock does too.
         */
        return new PrimaryFileWriteTicket(userId, timeUntilExpiration,
                primaryDirectoryInfo, fileName, tempFile, sample, comments,
                description, actionCode, repositoryManager);
    }

    public static MultiLock afterFileUpload(int sampleId, String fileNames[],
            int userId) {
        /*
         * We deliberately do not return any filesystem-related locks to the
         * caller because we assume he already has the filesystem locked in some
         * other way, probably via PrimaryFileWriteTickets he acquired
         * separately. The lock we return to him will not conflict with
         * PrimaryFileWriteTickets.
         */
        return new MultiLock(
                new SimpleLock(sampleId, true,
                        FILES_MODIFY_SPECIFIC,
                        /*
                         * FILES_FETCH_SPECIFIC is a conflicting operation
                         * because the caller will invalidate entries in the
                         * secondaryDirsCache:
                         */
                        FILES_FETCH_SPECIFIC 
			| FILES_MODIFY_SPECIFIC,
                        userId),
                /*
                 * RepositoryManager will call SampleManager.getSampleInfo() and
                 * SampleManager.putSampleInfo(), so it makes sense for it to
                 * acquire those kinds of locks now.
                 */
                SampleLocks.managerGetSampleInfo(sampleId, true),
                SampleLocks.managerPutSampleInfo(false, sampleId));
    }

    public static MultiLock eradicateFiles(int sampleId, String[] fileNames,
            int userId) {
        /*
         * create an array for all the SimplePrimaryFileLocks, the SimpleLock,
         * and the SampleLocks.
         */
        int i = 0;
        AbstractLock locks[] = new AbstractLock[fileNames.length + 3];
        locks[i++] = new SimpleLock(sampleId, true,
                HOLDINGS_FETCH_SPECIFIC 
                | FILES_MODIFY_SPECIFIC
                | LOCALLABS_CONTAINS,
                HOLDINGS_MODIFY_SPECIFIC
                | HOLDINGS_MODIFY_ALL
                        /*
                         * FILES_FETCH_SPECIFIC is a conflicting operation
                         * because the caller will invalidate entries in the
                         * secondaryDirsCache:
                         */
                | FILES_FETCH_SPECIFIC 
                | FILES_MODIFY_SPECIFIC
                | LOCALLABS_MODIFY,
                userId);

        // add a SimplePrimaryFileLock for each file
        for (String fileName : fileNames) {
            locks[i++] = new SimplePrimaryFileLock(sampleId, fileName, false,
                    false, true, userId);
        }

        /*
         * RepositoryManager will call SampleManager.getSampleInfo() and
         * SampleManager.putSampleInfo(), so it makes sense for it to acquire
         * those kinds of locks now.
         */
        locks[i++] = SampleLocks.managerGetSampleInfo(sampleId, true);
        locks[i++] = SampleLocks.managerPutSampleInfo(false, sampleId);

        return new MultiLock(locks);
    }

    public static AbstractLock primaryDirectoryScanBegin() {
        return new SimpleLock(true, HOLDINGS_FETCH_ALL, HOLDINGS_MODIFY_ANY);
    }

    public static MultiLock primaryDirectoryScanSingle(int sampleId) {
        return new MultiLock(
                new SimpleLock(sampleId, true,
                        FILES_FETCH_SPECIFIC | FILES_MODIFY_SPECIFIC,
                        /*
                         * FILES_FETCH_SPECIFIC is a conflicting operation
                         * because the caller will invalidate entries in the
                         * secondaryDirsCache:
                         */
                        FILES_FETCH_SPECIFIC | FILES_MODIFY_SPECIFIC),
                new PrimaryDirectoryReadLock(sampleId, false, false),
                /*
                 * RepositoryManager will call SampleManager.getSampleInfo() and
                 * SampleManager.putSampleInfo(), so it makes sense for it to
                 * acquire those kinds of locks now.
                 */
                SampleLocks.managerGetSampleInfo(sampleId, true),
                SampleLocks.managerPutSampleInfo(false, sampleId));
    }

    public static AbstractLock holdingsDump() {
        return new SimpleLock(true, HOLDINGS_FETCH_ALL, HOLDINGS_MODIFY_ANY);
    }

    public static AbstractLock modifyLocalLabs() {
        return new SimpleLock(false, LOCALLABS_MODIFY, LOCALLABS_MODIFY);
    }

    public static MultiLock modifyHolding(int sampleId) {
        return new MultiLock(
                new SimpleLock(sampleId, true,
                        HOLDINGS_MODIFY_SPECIFIC
                        | HOLDINGS_MODIFY_ANY, 
                        HOLDINGS_MODIFY_SPECIFIC
                        | HOLDINGS_MODIFY_ALL),
                /*
                 * RepositoryManager will call SampleManager.getSampleInfo(),
                 * so it makes sense for it to acquire that kind of lock now:
                 */
                SampleLocks.managerGetSampleInfo(sampleId, true));
    }

    public static AbstractLock modifyAllHoldings() {
	return new SimpleLock(true, 
	        HOLDINGS_MODIFY_ALL
		| HOLDINGS_MODIFY_ANY,
		HOLDINGS_MODIFY_ALL
	        | HOLDINGS_MODIFY_ANY);
    }

    public static GenericExclusiveLock registerExistingDirectories() {
        return new GenericExclusiveLock(true);
    }

    public static RepositoryTicket grantNewTicket(int userId,
            long timeUntilExpiration, RepositoryFiles files) {
        return new OutOfBandReadTicket(userId, timeUntilExpiration, files);
    }

    public static MultiLock removeFiles(int sampleId, String[] fileNames,
            int userId) {
        /*
         * create an array for all the SimplePrimaryFileLocks, the SimpleLock,
         * and the SampleLocks.
         */
        int i = 0;
        AbstractLock locks[] = new AbstractLock[fileNames.length + 3];
        
        locks[i++] = new SimpleLock(sampleId, true,
                HOLDINGS_FETCH_SPECIFIC 
                | FILES_FETCH_SPECIFIC
                | FILES_MODIFY_SPECIFIC 
                | LOCALLABS_CONTAINS,
                /*
                 * FILES_FETCH_SPECIFIC is a conflicting operation because the
                 * caller will invalidate entries in the secondaryDirsCache.
                 */
                HOLDINGS_MODIFY_SPECIFIC 
                | HOLDINGS_MODIFY_ALL
                | FILES_FETCH_SPECIFIC
                | FILES_MODIFY_SPECIFIC 
                | LOCALLABS_MODIFY,
                userId);

        // add a SimplePrimaryFileLock for each file
        for (String fileName : fileNames) {
            locks[i++] = new SimplePrimaryFileLock(sampleId, fileName, false,
                    false, true, userId);
        }

        /*
         * RepositoryManager will call SampleManager.getSampleInfo() and
         * SampleManager.putSampleInfo(), so it makes sense for it to acquire
         * those kinds of locks now.
         */
        locks[i++] = SampleLocks.managerGetSampleInfo(sampleId, true);
        locks[i++] = SampleLocks.managerPutSampleInfo(false, sampleId);

        return new MultiLock(locks);
    }

    public static MultiLock renameFile(int sampleId, String oldFileName,
            String newFileName, int userId) {
        return new MultiLock(
                new SimpleLock(sampleId, true,
                        HOLDINGS_FETCH_SPECIFIC 
                        | FILES_FETCH_SPECIFIC
                        | FILES_MODIFY_SPECIFIC 
                        | LOCALLABS_CONTAINS,
                        /*
                         * FILES_FETCH_SPECIFIC is a conflicting operation
                         * because the caller will invalidate entries in the
                         * secondaryDirsCache:
                         */
                        HOLDINGS_MODIFY_SPECIFIC
                        | HOLDINGS_MODIFY_ALL
                        | FILES_FETCH_SPECIFIC
                        | FILES_MODIFY_SPECIFIC 
                        | LOCALLABS_MODIFY,
                        userId),
                new SimplePrimaryFileLock(sampleId, oldFileName, false, false,
                        true, userId),
                new SimplePrimaryFileLock(sampleId, newFileName, false, true,
                        false, userId),
                /*
                 * RepositoryManager will call SampleManager.getSampleInfo() and
                 * SampleManager.putSampleInfo(), so it makes sense for it to
                 * acquire those kinds of locks now.
                 */
                SampleLocks.managerGetSampleInfo(sampleId, true),
                SampleLocks.managerPutSampleInfo(false, sampleId));
    }

    public static MultiLock modifyFileDescription(int sampleId,
            String fileName, int userId) {
        return new MultiLock(
                new SimpleLock(sampleId, true,
                        HOLDINGS_FETCH_SPECIFIC 
                        | FILES_FETCH_SPECIFIC
                        | FILES_MODIFY_SPECIFIC 
                        | LOCALLABS_CONTAINS,
                        /*
                         * FILES_FETCH_SPECIFIC is a conflicting operation
                         * because the caller will invalidate entries in the
                         * secondaryDirsCache:
                         */
                        HOLDINGS_MODIFY_SPECIFIC
                        | HOLDINGS_MODIFY_ALL 
                        | FILES_FETCH_SPECIFIC
                        | FILES_MODIFY_SPECIFIC 
                        | LOCALLABS_MODIFY,
                        userId),
                new SimplePrimaryFileLock(sampleId, fileName, false, false,
                        true, userId),
                /*
                 * RepositoryManager will call SampleManager.getSampleInfo() and
                 * SampleManager.putSampleInfo(), so it makes sense for it to
                 * acquire those kinds of locks now.
                 */
                SampleLocks.managerGetSampleInfo(sampleId, true),
                SampleLocks.managerPutSampleInfo(false, sampleId));
    }

    public static MultiLock revertSampleToVersionIncludingFiles(int sampleId,
            int userId) {
        return new MultiLock(
                new SimpleLock(sampleId, true,
                        HOLDINGS_FETCH_SPECIFIC 
                        | FILES_FETCH_SPECIFIC
                        | FILES_MODIFY_SPECIFIC 
                        | LOCALLABS_CONTAINS,
                        /*
                         * FILES_FETCH_SPECIFIC is a conflicting operation
                         * because the caller will invalidate entries in the
                         * secondaryDirsCache:
                         */
                        HOLDINGS_MODIFY_SPECIFIC
                        | HOLDINGS_MODIFY_ALL
                        | FILES_FETCH_SPECIFIC
                        | FILES_MODIFY_SPECIFIC 
                        | LOCALLABS_MODIFY,
                        userId),
                new PrimaryDirectoryCreationLock(sampleId, userId),
                /*
                 * RepositoryManager will call SampleManager.getSampleInfo() and
                 * SampleManager.putSampleInfo(), so it makes sense for it to
                 * acquire those kinds of locks now.
                 */
                SampleLocks.managerGetSampleInfo(sampleId, true),
                SampleLocks.managerPutSampleInfo(false, sampleId));
    }

    public static MultiLock getDataFileAggregateSize(int sampleId,
            String fileName) {
        return new MultiLock(
                new SimpleLock(sampleId, true,
                        HOLDINGS_FETCH_SPECIFIC, 
                        HOLDINGS_MODIFY_SPECIFIC
                        | HOLDINGS_MODIFY_ALL),
                new SimplePrimaryFileLock(sampleId, fileName, true, false,
                        false, UserInfo.INVALID_USER_ID),
                /*
                 * RepositoryManager will call SampleManager.getSampleInfo() and
                 * SampleManager.putSampleInfo(), so it makes sense for it to
                 * acquire those kinds of locks now.
                 */
                SampleLocks.managerGetSampleInfo(sampleId, true),
                SampleLocks.managerPutSampleInfo(false, sampleId));
    }

    public static MultiLock elevateLocalHoldingLevel(int sampleId) {
	return new MultiLock(
                new SimpleLock(sampleId, true,
		        HOLDINGS_FETCH_SPECIFIC
                        | HOLDINGS_MODIFY_SPECIFIC
			| HOLDINGS_MODIFY_ANY, 
                        HOLDINGS_MODIFY_SPECIFIC
		        | HOLDINGS_MODIFY_ALL),
		/*
		 * The protected code might call 
		 * RepositoryManager.createDataDirectory() or 
                 * SampleManager.getSampleInfo() so acquire those locks now.
		 */
		createDataDirectory(sampleId, UserInfo.INVALID_USER_ID),
		SampleLocks.managerGetSampleInfo(sampleId, true));
    }

    public static MultiLock decreaseLocalHoldingLevel(int sampleId) {
	return new MultiLock(
                new SimpleLock(sampleId, true,
		        HOLDINGS_FETCH_SPECIFIC
                        | HOLDINGS_MODIFY_SPECIFIC
			| HOLDINGS_MODIFY_ANY, 
                        HOLDINGS_MODIFY_SPECIFIC
		        | HOLDINGS_MODIFY_ALL),
		/*
		 * The protected code might call 
                 * SampleManager.getSampleInfo() so acquire that lock now.
		 */
		SampleLocks.managerGetSampleInfo(sampleId, true));
    }
}
