/*
 * Reciprocal Net project
 * 
 * SampleLocks.java
 *
 * 11-May-2004: ekoperda wrote first draft
 * 22-Mar-2005: ekoperda added managerRebuildSearchUnitCells()
 * 12-Jul-2005: ekoperda added managerRebuildSearchSpaceGroups()
 * 27-Sep-2005: midurbin added managerGetNextUnusedLocalLabId()
 * 12-May-2006: jobollin reformatted the source and removed unused imports
 * 30-May-2006: jobollin added some type arguments
 */

package org.recipnet.site.core.util;

import java.util.Collection;

import org.recipnet.site.core.lock.AbstractLock;
import org.recipnet.site.core.lock.GenericExclusiveLock;
import org.recipnet.site.core.lock.MultiLock;
import org.recipnet.site.core.lock.SimpleLock;
import org.recipnet.site.shared.db.SampleInfo;

/**
 * A class with nothing but static functions that encapsulates
 * {@code SampleManager}'s locking logic and consolidates it into a single
 * place for easier maintenance. Also encapsulates locking logic for core agents
 * related to {@code SampleManager}. Note that the "operation constants"
 * defined on this class must be distinct from those defined by
 * {@code RepositoryLocks} in order to avoid locking errors.
 */
public abstract class SampleLocks {
    /**
     * flag for assigning an unused sample id to a new sample; used by
     * SampleIdAgent.getNewSampleId().
     */
    private static final long SAMPLEIDS_ASSIGN = SimpleLock.GEN_OP_BASE << 16;

    /**
     * flag for any operation that requires exclusive
     * SELECT/INSERT/UPDATE/DELETE access to the 'sampleIdBlocks' table.
     */
    private static final long IDBLOCKS_EXCLUSIVE = SimpleLock.GEN_OP_BASE << 17;

    /**
     * flag for an update (INSERT/UPDATE/DELETE) to any row in the
     * 'searchLocalHoldings' table.
     */
    private static final long LOCALHOLDINGS_MODIFY_ANY
            = SimpleLock.GEN_OP_BASE << 18;

    /**
     * flag for an update (add, modify, remove, clear) to the list named
     * {@code localLabs} that's held in memory.
     */
    private static final long LOCALLABS_MODIFY_ANY
            = SimpleLock.GEN_OP_BASE << 19;

    /**
     * flag that allows use of SampleManager's {@code random} object for
     * generating random numbers.
     */
    private static final long RANDOM_GENERATE = SimpleLock.GEN_OP_BASE << 20;

    /**
     * flag for an INSERT of any sample into the 'samples' table (and usually
     * also corresponding updates to related tables).
     */
    private static final long SAMPLES_INSERT_ANY = SimpleLock.GEN_OP_BASE << 21;

    /**
     * flag for a read (SELECT) of most/all samples in one or more sample-
     * related tables; samples need not be individually enumerated, but may
     * optionally be put into the cache.
     */
    private static final long SAMPLES_FETCH_ALL = SimpleLock.GEN_OP_BASE << 22;

    /**
     * flag for a read (SELECT) of most/all samples in the 'samples' table but
     * no others. The nature of the read is that values in the fields
     * {@code current_sampleHistory_id}, {@code lastActionDate},
     * {@code releaseDate}, and {@code status} are ignored. This restriction
     * allows a SAMPLE_WRITE_SPECIFIC to execute concurrently without disrupting
     * one of these SAMPLE_READTINY_ANY operations.
     */
    private static final long SAMPLES_READTINY_ANY
            = SimpleLock.GEN_OP_BASE << 23;

    /**
     * flag for a write (INSERT/UPDATE/DELETE) to any portion of any sample-
     * related tables.
     */
    private static final long SAMPLES_MODIFY_ANY = SimpleLock.GEN_OP_BASE << 24;

    /**
     * flag for creating a new search (and receiving a unique search id); used
     * by SampleManager.storeSearchParams().
     */
    private static final long SEARCH_STORE = SimpleLock.GEN_OP_BASE << 25;

    /**
     * flag for a read (SELECT) of one or several enumerated samples from
     * any/all sample-related tables including samples, sampleHistory,
     * sampleData, sampleAttributes, sampleAnnotations, sampleAcls, searchAtoms,
     * and searchLocalHoldings. Also may indicate a read-from-cache, where the
     * specified sample is in the cache.
     */
    private static final long SAMPLES_READ_SPECIFIC
            = SimpleLock.SPEC_OP_BASE << 16;

    /**
     * flag for a write (INSERT/UPDATE/DELETE) of one or several enumerated
     * samples from any/all sample-related tables including samples,
     * sampleHistory, sampleData, sampleAttributes, sampleAnnotations,
     * sampleAcls, searchAtoms, and searchLocalHoldings. Always implies a cache
     * update, or at least a cache invalidation, for the sample(s).
     */
    private static final long SAMPLES_WRITE_SPECIFIC
            = SimpleLock.SPEC_OP_BASE << 17;

    /**
     * flag for executing (or retrieving the cached execution results of) a
     * specific previously stored search.
     */
    private static final long SEARCH_EXECUTE_SPECIFIC
            = SimpleLock.SPEC_OP_BASE << 18;

    public static AbstractLock idAgentCountUnusedSampleIds() {
        return new SimpleLock(true, IDBLOCKS_EXCLUSIVE, IDBLOCKS_EXCLUSIVE);
    }

    public static AbstractLock idAgentProcessSampleIdBlockHintCm() {
        return new SimpleLock(true, IDBLOCKS_EXCLUSIVE, IDBLOCKS_EXCLUSIVE);
    }

    public static AbstractLock idAgentPeriodicCheck() {
        return new SimpleLock(true, IDBLOCKS_EXCLUSIVE, IDBLOCKS_EXCLUSIVE);
    }

    public static AbstractLock idAgentInit() {
        return new SimpleLock(true, IDBLOCKS_EXCLUSIVE, IDBLOCKS_EXCLUSIVE);
    }

    public static SimpleLock idAgentGetNewSampleId() {
        return new SimpleLock(false, SAMPLEIDS_ASSIGN, SAMPLEIDS_ASSIGN);
    }

    public static boolean idAgentGetNewSampleId_verifyExistingLock(
            AbstractLock existingLock) {
        return idAgentGetNewSampleId().isEncompassedBy(existingLock);
    }

    public static AbstractLock idAgentProcessSampleIdBlockIsm() {
        return new SimpleLock(true, IDBLOCKS_EXCLUSIVE, IDBLOCKS_EXCLUSIVE);
    }

    public static AbstractLock idAgentClaimLocalBlocks() {
        return new GenericExclusiveLock(true);
    }

    public static AbstractLock idAgentBuildIdList(AbstractLock existingLock,
            Collection<Integer> proposedSampleIds) {

        return new MultiLock(existingLock.copy(), new SimpleLock(
                proposedSampleIds, true, SAMPLES_READTINY_ANY,
                SAMPLES_WRITE_SPECIFIC));
    }

    public static SimpleLock managerGetSampleInfo(int sampleId,
            boolean needsDbConnection) {
        return new SimpleLock(sampleId, needsDbConnection,
                SAMPLES_READ_SPECIFIC, SAMPLES_WRITE_SPECIFIC);
    }

    public static boolean managerGetSampleInfo_verifyExistingLock(
            AbstractLock existingLock, int sampleId,
            boolean needsDbConnection) {
        return managerGetSampleInfo(sampleId, needsDbConnection).isEncompassedBy(
                existingLock);
    }

    public static AbstractLock managerGetFullSampleInfo(int sampleId) {
        return new SimpleLock(sampleId, true, SAMPLES_READ_SPECIFIC,
                SAMPLES_WRITE_SPECIFIC);
    }

    public static AbstractLock managerLookupSampleId() {
        return new SimpleLock(true, SAMPLES_READTINY_ANY, SAMPLES_INSERT_ANY);
    }

    public static AbstractLock managerVerifySampleNumber(int sampleId,
            boolean needsDbConnection) {
        return new SimpleLock(sampleId, needsDbConnection,
                SAMPLES_READTINY_ANY, SAMPLES_WRITE_SPECIFIC);
    }

    public static SimpleLock managerVerifySampleNumbers(
            Collection<Integer> sampleIds, boolean needsDbConnection) {
        return new SimpleLock(sampleIds, needsDbConnection,
                SAMPLES_READTINY_ANY, SAMPLES_WRITE_SPECIFIC);
    }

    public static boolean managerVerifySampleNumbers_verifyExistingLock(
            AbstractLock existingLock, Collection<Integer> sampleIds,
            boolean needsDbConnection) {
        return managerVerifySampleNumbers(
                sampleIds, needsDbConnection).isEncompassedBy(existingLock);
    }

    public static SimpleLock managerPutSampleInfo(boolean creatingNewSample,
            int sampleId) {
        if (creatingNewSample) {
            return new SimpleLock(SampleInfo.INVALID_SAMPLE_ID, true,
                    SAMPLES_WRITE_SPECIFIC | SAMPLEIDS_ASSIGN
                            | SAMPLES_INSERT_ANY | RANDOM_GENERATE,
                    SAMPLES_WRITE_SPECIFIC | SAMPLEIDS_ASSIGN
                            | LOCALLABS_MODIFY_ANY | SAMPLES_INSERT_ANY
                            | RANDOM_GENERATE);
        } else {
            return new SimpleLock(sampleId, true, SAMPLES_WRITE_SPECIFIC
                    | SAMPLES_MODIFY_ANY | SAMPLES_READ_SPECIFIC,
                    SAMPLES_WRITE_SPECIFIC | LOCALLABS_MODIFY_ANY);
        }
    }

    public static boolean managerPutSampleInfo_verifyExistingLock(
            AbstractLock existingLock, boolean creatingNewSample, int sampleId) {
        return managerPutSampleInfo(creatingNewSample, sampleId).isEncompassedBy(
                existingLock);
    }

    public static SimpleLock managerRevertSampleToVersion(int sampleId) {
        return new SimpleLock(sampleId, true, SAMPLES_READ_SPECIFIC
                | SAMPLES_WRITE_SPECIFIC | SAMPLES_MODIFY_ANY,
                SAMPLES_WRITE_SPECIFIC | LOCALLABS_MODIFY_ANY);
    }

    public static boolean managerRevertSampleToVersion_verifyExistingLock(
            AbstractLock existingLock, int sampleId) {
        return managerRevertSampleToVersion(sampleId).isEncompassedBy(
                existingLock);
    }

    public static AbstractLock managerStoreSearchParams() {
        return new SimpleLock(false, SEARCH_STORE | RANDOM_GENERATE,
                SEARCH_STORE | RANDOM_GENERATE);
    }

    public static AbstractLock managerGetSearchParams(int searchId) {
        return new SimpleLock(searchId, false, SEARCH_EXECUTE_SPECIFIC,
                SEARCH_EXECUTE_SPECIFIC);
    }

    public static AbstractLock managerGetSearchResults(int searchId) {
        return new SimpleLock(searchId, false, SEARCH_EXECUTE_SPECIFIC
                | SAMPLES_FETCH_ALL, SEARCH_EXECUTE_SPECIFIC
                | SAMPLES_MODIFY_ANY | LOCALLABS_MODIFY_ANY
                | LOCALHOLDINGS_MODIFY_ANY);
    }

    public static AbstractLock managerGetEarliestAuthoritativeHistoryDate() {
        return new SimpleLock(true, SAMPLES_READTINY_ANY, SAMPLES_INSERT_ANY
                | LOCALLABS_MODIFY_ANY);
    }

    public static AbstractLock managerGetNextUnusedLocalLabId() {
        return new SimpleLock(true, SAMPLES_READTINY_ANY, SAMPLES_INSERT_ANY);
    }

    public static AbstractLock managerRebuildSearchAtoms() {
        return new GenericExclusiveLock(true);
    }

    public static AbstractLock managerRebuildSearchLocalHoldings() {
        return new GenericExclusiveLock(true);
    }

    public static AbstractLock managerRebuildSearchUnitCells() {
        return new GenericExclusiveLock(true);
    }

    public static AbstractLock managerRebuildSearchSpaceGroups() {
        return new GenericExclusiveLock(true);
    }

    public static AbstractLock managerEventLocalLabsChanged() {
        return new SimpleLock(false, LOCALLABS_MODIFY_ANY, LOCALLABS_MODIFY_ANY);
    }

    public static AbstractLock managerEventSampleHolding() {
        return new SimpleLock(true, LOCALHOLDINGS_MODIFY_ANY,
                LOCALHOLDINGS_MODIFY_ANY);
    }

    public static AbstractLock managerEventSampleActivation(int sampleId) {
        return new SimpleLock(sampleId, true, SAMPLES_WRITE_SPECIFIC
                | SAMPLES_MODIFY_ANY | SAMPLES_READ_SPECIFIC
                | SAMPLES_INSERT_ANY, SAMPLES_WRITE_SPECIFIC
                | SAMPLES_INSERT_ANY);
    }

    public static AbstractLock managerEventSampleUpdate(int sampleId) {
        return new SimpleLock(sampleId, true, SAMPLES_WRITE_SPECIFIC
                | SAMPLES_MODIFY_ANY | SAMPLES_READ_SPECIFIC,
                SAMPLES_WRITE_SPECIFIC);
    }

    public static AbstractLock managerEventSampleDeactivation(int sampleId) {
        return new SimpleLock(sampleId, true, SAMPLES_WRITE_SPECIFIC
                | SAMPLES_MODIFY_ANY | SAMPLES_READ_SPECIFIC,
                SAMPLES_WRITE_SPECIFIC);
    }

    public static AbstractLock managerExecuteSearch(int searchId,
            AbstractLock existingLock) {
        AbstractLock oneLock = new SimpleLock(searchId, true,
                SEARCH_EXECUTE_SPECIFIC | SAMPLES_FETCH_ALL,
                SEARCH_EXECUTE_SPECIFIC | SAMPLES_MODIFY_ANY
                        | LOCALLABS_MODIFY_ANY | LOCALHOLDINGS_MODIFY_ANY);
        return (existingLock == null) ? oneLock : new MultiLock(
                existingLock.copy(), oneLock);
    }

    public static AbstractLock managerGetMultipleSampleInfo(int sampleIds[],
            boolean needsDbConnection, AbstractLock existingLock) {
        AbstractLock oneLock = new SimpleLock(sampleIds, needsDbConnection,
                SAMPLES_READ_SPECIFIC, SAMPLES_WRITE_SPECIFIC);

        return (existingLock == null) ? oneLock : new MultiLock(
                existingLock.copy(), oneLock);
    }
}
