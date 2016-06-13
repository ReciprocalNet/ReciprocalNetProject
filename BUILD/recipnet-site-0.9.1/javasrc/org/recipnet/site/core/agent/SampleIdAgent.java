/*
 * Reciprocal Net project
 * 
 * SampleIdAgent.java
 *
 * 25-Oct-2002: ekoperda wrote first draft
 * 04-Nov-2002: ekoperda added bootstrapMode argument to init() and new method
 *              claimLocalBlocks()
 * 07-Nov-2002: ekoperda change processSampleIdBlockISM() 
 *              and proposeSampleIdBlocks() to utilize the new expiresAfter
 *              field on SampleIdBlockISM
 * 22-Nov-2002: ekoperda fixed bug 623 in checkActiveProposals()
 * 09-Dec-2002: ekoperda fixed bug 625 in claimLocalBlocks()
 * 10-Dec-2002: ekoperda fixed bug 643 in processSampleIdBlockIsm()
 * 07-Feb-2003: ekoperda adjusted for changed method names in SampleLock
 * 21-Feb-2003: ekoperda added exception support throughout
 * 15-Apr-2003: midurbin moved logging code to LogRecordGenerator
 * 23-Apr-2003: ekoperda added support throughout for LockAgent
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.core.util package
 *              to org.recipnet.site.core.agent; also changed package
 *              references to match source tree reorganization
 * 29-Apr-2004: midurbin clarified javadocs on getNewSampleId() and
 *              buildIdList() as part of the fix for bug #1197
 * 21-May-2004: ekoperda modified all references to SampleLock's to employ new
 *              locking subsystem
 * 12-Jan-2005: ekoperda modified switchBlock() to account for spec change on
 *              RepositoryManager.passCoreMessage()
 * 02-Feb-2006: ekoperda improved logging for claims in checkActiveProposals()
 * 10-Apr-2006: jobollin reformatted the source, organized imports, made the
 *              db* methods non-static, removed inaccessible catch blocks
 * 24-Apr-2006: jobollin inserted some type parameterization in
 *              proposeSampleIdBlocks() and borrowSampleIdBlocks(), formatted
 *              the source, updated and expanded the docs
 * 04-Jul-2008: ekoperda added the event...() functions for ISM processing and
 *              enhanced support for operation without local sample id blocks
 * 28-Nov-2008: ekoperda updated checkActiveProposals to handle inactive sites
 * 18-Mar-2009: ekoperda fixed bug #1915 in processSampleIdBlockHintCm()
 */

package org.recipnet.site.core.agent;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import org.recipnet.site.OperationFailedException;
import org.recipnet.site.UnexpectedExceptionException;
import org.recipnet.site.core.DeadlockDetectedException;
import org.recipnet.site.core.ResourcesExhaustedException;
import org.recipnet.site.core.SampleManager;
import org.recipnet.site.core.SiteManager;
import org.recipnet.site.core.lock.AbstractLock;
import org.recipnet.site.core.lock.LockAgent;
import org.recipnet.site.core.lock.MultiLock;
import org.recipnet.site.core.msg.CoreMessage;
import org.recipnet.site.core.msg.InterSiteMessage;
import org.recipnet.site.core.msg.ProcessedIsmCM;
import org.recipnet.site.core.msg.SampleIdBlockHintCM;
import org.recipnet.site.core.msg.SampleIdBlockISM;
import org.recipnet.site.core.msg.SendIsmCM;
import org.recipnet.site.core.util.LogRecordGenerator;
import org.recipnet.site.core.util.SampleLocks;
import org.recipnet.site.shared.db.SampleIdBlock;
import org.recipnet.site.shared.db.SiteInfo;

/**
 * Helper class used by Sample Manager to offload all its sample id-related
 * code into one place. Because this class is tightly coupled with Sample
 * Manager, all locking and synchronization is done using Sample Manager's 
 * locking agent.  This class "owns" the sampleIdBlocks table and stores its
 * persistent state there.
 */
public class SampleIdAgent {
    private boolean initialized;
    private boolean initializationInProgress;
    private AbstractLock initializationLock;

    private List<Integer> newSampleIds;

    private SampleIdBlock currentSampleIdBlock;

    private Random random;

    // These values are supplied by Sample Manager at creation time
    private SiteManager siteManager;

    private SampleManager sampleManager;

    private LockAgent lockAgent;

    private Properties properties;

    /**
     * Initializes a new {@code SampleIdAgent} with the specified parameters
     * 
     * @param  siteManager the {@code SiteManager} from which to obtain
     *         information about the local site, labs, providers, and users
     * @param  sampleManager the {@code SampleManager} from which to obtain
     *         information about the samples known to this site
     * @param  lockAgent the {@code LockAgent} with which to work to coordinate
     *         locking 
     * @param  properties the configuration {@code Properties} of this site
     *         software instance
     */
    public SampleIdAgent(SiteManager siteManager, SampleManager sampleManager,
            LockAgent lockAgent, Properties properties) {
        this.siteManager = siteManager;
        this.sampleManager = sampleManager;
        this.lockAgent = lockAgent;
        this.properties = properties;
        this.initialized = false;
	this.initializationInProgress = false;
	this.initializationLock = null;
        this.newSampleIds = new ArrayList<Integer>(SampleIdBlock.BLOCK_SIZE);
        this.currentSampleIdBlock = null;
        this.random = new SecureRandom();
    }

    /**
     * Unless operating in bootstrap mode, scans the DB table 'sampleIdBlocks'
     * and loads persistent state. This includes detecting the current sample
     * ID block and activating a new one if necessary; this method obtains its
     * own database lock for the purpose.  Must be the first method invoked on
     * a new instance.
     * 
     * @param  bootstrapMode {@code true} if the core is operating in bootstrap
     *         mode, {@code false} if it isn't
     * 
     * @throws DeadlockDetectedException
     * @throws OperationFailedException
     */
    public void init(boolean bootstrapMode) throws DeadlockDetectedException,
            OperationFailedException {
	AbstractLock lock = SampleLocks.idAgentInit();    
	lockAgent.registerLock(lock);
	lock.acquire();

	try {
	    this.initializationInProgress = true;
	    this.initializationLock = lock;
	    buildIdList(lock);
	} catch (ResourcesExhaustedException ex) {
	    if (bootstrapMode) {
		// Nothing to do... during bootstrap mode we silently tolerate
		// having no sample id blocks owned by the local site.
	    } else {
		throw ex;
	    }
	} finally {
	    lock.release();
	    this.initializationInProgress = false;
	    this.initializationLock = null;
	}
	this.initialized = true;
    }

    /**
     * Returns the number of unused sample IDs presently available for
     * immediate assignment at the local site. This is the number of unused
     * sample IDs in the current block plus the product of the number of
     * CLAIMED_LOCAL blocks and the block size.
     * 
     * @return the number of IDs available
     * @throws DeadlockDetectedException
     * @throws OperationFailedException
     */
    public int countUnusedSampleIds() throws DeadlockDetectedException,
            OperationFailedException {
        AbstractLock lock = SampleLocks.idAgentCountUnusedSampleIds();
        
        lockAgent.registerLock(lock);
        lock.acquire();

        try {
            int count = 0;
            
            count += newSampleIds.size();
            count += dbCountBlocks(
                    lock.getConnection(), SampleIdBlock.CLAIMED_LOCAL)
                    * SampleIdBlock.BLOCK_SIZE;
            
            return count;
        } catch (SQLException ex) {
            throw new OperationFailedException(ex);
        } finally {
            lock.release();
        }
    }

    /**
     * Returns an unused sample ID that can be assigned to a new sample. The
     * caller must already posess a database lock that has been cleared for
     * invoking this function because it is assumed that acquiring a new/unused
     * sample ID will always be performed jointly with creating a new sample.
     * No {@code SampleIdAgent} instance will ever return the same ID from
     * multiple invocations of this method, but different instances (which
     * should not be running concurrently on the same site) might do
     * under certain circumstances.
     * 
     * @param  existingLock an {@code AbstractLock} supporting all the
     *         required operations
     *         
     * @return an ID suitable for assignment to a new sample
     * 
     * @throws DeadlockDetectedException
     * @throws IllegalArgumentException
     * @throws IllegalStateException
     * @throws OperationFailedException
     * 
     * @see SampleLocks#idAgentGetNewSampleId_verifyExistingLock(AbstractLock)
     */
    public int getNewSampleId(AbstractLock existingLock)
            throws DeadlockDetectedException, OperationFailedException {
        if (!initialized) {
            throw new IllegalStateException();
        }

        /*
         * Verify that the existing lock has been cleared for the necessary
         * operations.
         */
        if (!SampleLocks.idAgentGetNewSampleId_verifyExistingLock(
                existingLock)) {
            // Lock doesn't support the necessary operations.
            throw new IllegalArgumentException();
        }

        // Find the next unused sample id number
        if (newSampleIds.isEmpty()) {

            /*
             * Oops, the current sample id block has been exhausted. Better
             * switch to another block.
             */
            buildIdList(existingLock);
        }

        // Actually issue the new sample id
        return newSampleIds.remove(0);
    }

    /**
     * Processes {@code SampleIdBlockISM}s, generally on behalf of
     * {@code SampleManager}.  When done, this method passes a
     * {@code CoreMessage} to the configured {@code SiteManager} to report its
     * result, which may or may not be successful. 
     * 
     * @param  msg the {@code SampleIdBlockISM} to process; should not be
     *         {@code null} 
     * 
     * @throws DeadlockDetectedException
     * @throws IllegalStateException
     * @throws OperationFailedException
     */
    public void processSampleIdBlockIsm(SampleIdBlockISM msg)
            throws DeadlockDetectedException, OperationFailedException {
        if (!initialized) {
            throw new IllegalStateException();
        }

        /*
         * At the end of this method, just after the lock is released, if this
         * value was set during processing then the referenced CoreMessage will
         * be transmitted to Site Manager's message queue. Such a mechanism is
         * necessary to avoid deadlock that might occur if we were to send a
         * ProcessedIsmCM before we'd released our own lock.
         */
        CoreMessage cmForSiteManager = null;

        // Obtain a read/write lock on the sampleIdBlocks table
        AbstractLock lock = SampleLocks.idAgentProcessSampleIdBlockIsm();      
        lockAgent.registerLock(lock);
        lock.acquire();

        try {
            // Dispatch according to the message's function code
            switch (msg.func) {
                case SampleIdBlockISM.PROPOSAL:
		    cmForSiteManager = eventProposal(msg, lock);
		    break;
                case SampleIdBlockISM.PROPOSAL_APPROVED:
		    cmForSiteManager = eventProposalApproved(msg, lock);
		    break;
                case SampleIdBlockISM.PROPOSAL_DISAPPROVED:
		    cmForSiteManager = eventProposalDisapproved(msg, lock);
		    break;
                case SampleIdBlockISM.CLAIM:
                    cmForSiteManager = eventClaim(msg, lock);
		    break;
                case SampleIdBlockISM.TRANSFER_INITIATE:
		    cmForSiteManager = eventTransferInitiate(msg, lock);
		    break;
                case SampleIdBlockISM.TRANSFER_REJECT:
		    cmForSiteManager = eventTransferReject(msg, lock);
		    break;
                case SampleIdBlockISM.TRANSFER_COMPLETE:
		    cmForSiteManager = eventTransferComplete(msg, lock);
		    break;
                case SampleIdBlockISM.TRANSFER_REQUEST:
                    cmForSiteManager = eventTransferRequest(msg, lock);
		    break;
                case SampleIdBlockISM.TRANSFER_REQUEST_DENIED:
                    cmForSiteManager = eventTransferRequestDenied(msg, lock);
		    break;
                default:
                    cmForSiteManager = ProcessedIsmCM.failure(msg,
                            "Unknown function code " + msg.func
                                    + " received in a sample id block message;"
                                    + " postponing");
                    break;
            }
        } catch (SQLException ex) {
            throw new OperationFailedException(ex);
        } finally {
            lock.release();
        }
	if (cmForSiteManager != null) {
	    /*
	     * Earlier in this method somebody queued a CM to be sent to
	     * Site Manager once the lock was released. Send that CM now.
	     */
	    siteManager.passCoreMessage(cmForSiteManager);
	}
    }

    /**
     * <p>
     * Processes a {@code SampleIdBlockHintCM}, typically on behalf of the
     * Sample Manager.  This sort of message normally originates from a prior
     * invokation of {@link #getNewSampleId(AbstractLock) getNewSampleId()},
     * and indicates to this object that it should check its sample ID blocks
     * and possibly take action to acquire more.  The message is passed
     * asynchronously in order to avoid blocking a webapp thread longer than
     * necessary.
     * </p>
     * 
     * @param  msg nominally, the {@code SampleIdBlockHintCM} to process; these
     *         messages contain no data, however, so the parameter is ignored
     *         and may even be {@code null}
     * 
     * @throws IllegalStateException if this agent has not yet been initialized
     * @throws DeadlockDetectedException
     * @throws OperationFailedException
     */
    public void processSampleIdBlockHintCm(
            @SuppressWarnings("unused") SampleIdBlockHintCM msg)
            throws DeadlockDetectedException, OperationFailedException {
        if (!initialized && !initializationInProgress) {
	    // Normally a caller must invoke init() and let it complete before
	    // doing anything that might invoke this function.  Such is the
	    // intent of testing the 'initialized' variable.  However, during
	    // bootstrap mode, a caller's invocation of init() can lead to this
	    // method being invoked before init() returns to the caller.  Thus
	    // it is necessary for us to check 'initializationInProgress' also.
            throw new IllegalStateException();
        }

        // Obtain a lock and do the work.
	if (this.initializationInProgress && this.initializationLock != null) {
	    actOnBlockStatus(this.initializationLock);
	} else {
	    AbstractLock lock 
                    = SampleLocks.idAgentProcessSampleIdBlockHintCm();
	    lockAgent.registerLock(lock);
	    lock.acquire();
	    try {
		actOnBlockStatus(lock);
	    } finally {
		lock.release();
	    }
	}
    }

    /**
     * Performs periodic/infrequent maintenance. Currently this encompasses
     * three tasks: 1. purging very old proposals initiated by other sites that
     * have not been completed. 2. taking action on old proposals initiated by
     * the local site, either CLAIM'ing those blocks or dropping them. 3 check
     * the number of CLAIMED_LOCAL blocks available and take action to acquire
     * more if necessary. This task is performed periodically as a failsafe for
     * the processSampleIdBlockHintCM mechanism and hopefully won't ever need
     * to be utilized. This method never throws an exception and instead logs
     * them all via the logging mechanism.
     * 
     * @throws DeadlockDetectedException
     * @throws IllegalStateException
     * @throws OperationFailedException
     */
    public void periodicCheck() throws DeadlockDetectedException,
            OperationFailedException {
        if (!initialized) {
            throw new IllegalStateException();
        }

        // Obtain a lock.
        AbstractLock lock = SampleLocks.idAgentPeriodicCheck();
        
        lockAgent.registerLock(lock);
        lock.acquire();

        try {
            // Perform each of the periodic tasks
            clearOldProposals(lock);
            checkActiveProposals(lock);
            actOnBlockStatus(lock);
        } finally {
            lock.release();
        }
    }

    /** Helper method for ISM processing */
    private CoreMessage eventProposal(SampleIdBlockISM msg, 
            AbstractLock lock) throws OperationFailedException, SQLException {
	// See if any block with the specified id already exists
	SampleIdBlock block 
                = dbFetchBlock(lock.getConnection(), msg.blockId);
	if (block == null) {
	    /*
	     * No other site has any interest in this block, so reply with an
	     * approval. Also record the state change in the database.
	     */
	    dbAddUpdateBlock(lock.getConnection(),
	            new SampleIdBlock(SampleIdBlock.PROPOSED,
                    msg.blockId, msg.sourceSiteId));
	    if (!msg.hasExpired()) {
		/*
		 * Sending this kind of CM to Site Manager is not a
		 * deadlock risk.
		 */
		siteManager.passCoreMessage(new SendIsmCM(
                        SampleIdBlockISM.newProposalApproved(
                        siteManager.localSiteId,
                        msg.blockId, msg.sourceSiteId)));
		return ProcessedIsmCM.success(msg,
                        "Sent approval for proposed sample id block"
                        + msg.blockId + " from site id " + msg.sourceSiteId);
	    } else {
		return ProcessedIsmCM.success(msg,
                       "Recorded proposal for sample id block "
                       + msg.blockId + " from site id " + msg.sourceSiteId
		       + ", but did not transmit an approval because the"
                       + " proposal expired " + msg.expiresAfter);
                        }
	} else {
	    if (!msg.hasExpired()) {
		/*
		 * Some other site already has an interest in this
		 * block, so reply with a disapproval. Sending this
		 * kind of CM to SiteManager is not a deadlock risk.
		 */
		siteManager.passCoreMessage(new SendIsmCM(
                        SampleIdBlockISM.newProposalDisapproved(
                        siteManager.localSiteId,
                        msg.blockId, msg.sourceSiteId)));
		return ProcessedIsmCM.success(msg,
                        "Sent disapproval for proposed sample id block " 
                        + msg.blockId + " from site id "
                        + msg.sourceSiteId + " because site id " + block.siteId
                        + " already owns the block");
	    } else {
		return ProcessedIsmCM.success(msg,
                        "Ignored proposal for sample id block " + msg.blockId
                        + " from site id " + msg.sourceSiteId
                        + " because site id " + block.siteId
                        + " already owns the block; did not transmit a"
                        + " disapproval because the proposal expired "
                        + msg.expiresAfter);
	    }
	}
    }

    /** Helper method for ISM processing */
    private CoreMessage eventProposalApproved(SampleIdBlockISM msg,
	    AbstractLock lock) throws OperationFailedException, SQLException {
	/*
	 * Retrieve the referenced block and discard the message if
	 * we have no outstanding proposal for it.
	 */
	SampleIdBlock block = dbFetchBlock(lock.getConnection(), msg.blockId);
	if (block == null || block.status != SampleIdBlock.PROPOSED_LOCAL) {
	    return ProcessedIsmCM.ignoreFailure(msg,
                    "Discarding sample id block approval message for block id "
                    + msg.blockId + " from site id " + msg.sourceSiteId
                    + " because there is no active local proposal for that"
  		    + " block");
	}
                    
	/*
	 * The message makes sense; increment our approval count on the block.
	 * 
	 * TODO: perhaps some kind of logic should be implemented here to
	 * ensure that we count multiple approvals from the same site exactly 
	 * once.
	 */
	block.approvalCount++;
	dbAddUpdateBlock(lock.getConnection(), block);
                    
	/*
	 * This new approval just might be sufficient to end the proposal phase
	 * early. Check for that condition.
	 */
	checkActiveProposals(lock);
	return ProcessedIsmCM.success(msg,
                "Received approval for sample id block " + block.id
                + " from site id " + msg.sourceSiteId);
    }

    /** Helper method for ISM processing */
    private CoreMessage eventProposalDisapproved(SampleIdBlockISM msg,
	    AbstractLock lock) throws OperationFailedException, SQLException {
	/*
	 * Retrieve the referenced block and discard the message if
	 * we have no outstanding proposal for it.
	 */
	SampleIdBlock block = dbFetchBlock(lock.getConnection(), msg.blockId);
	if (block == null || (block.status != SampleIdBlock.PROPOSED_LOCAL)) {
	    return ProcessedIsmCM.ignoreFailure(msg,
                    "Discarded sample id block disapproval message for block"
		    + " id " + msg.blockId + " from site id "
                    + msg.sourceSiteId + " because there is no active local"
                    + " proposal for that block");
	}
                    
	/*
	 * Discontinue our proposal for this sample id block by
	 * deleting its row.
	 */
	dbDeleteBlock(lock.getConnection(), block.id);
                    
	/*
	 * Send a hint to ourselves that perhaps we need to evaluate
	 * our block status again (and perhaps issue a new proposal
	 * to replace the one that we just aborted)
	 */
	sampleManager.passCoreMessage(new SampleIdBlockHintCM());
	return ProcessedIsmCM.ignoreFailure(msg,
                "Received disapproval from site id " + msg.sourceSiteId
                + " for sample id block " + block.id + "; aborting proposal");
    }

    /** Helper method for ISM processing */
    private CoreMessage eventClaim(SampleIdBlockISM msg, AbstractLock lock)
            throws OperationFailedException, SQLException {
	/*
	 * Retrieve the referenced block and discard the message if the 
	 * originating site didn't issue a prior proposal for it, unless the
	 * claim came from the Coordinator.
	 */
	SampleIdBlock block = null;
	if (msg.isFromCoordinator()) {
	    block = new SampleIdBlock();
	    block.id = msg.blockId;
	    block.siteId = msg.sourceSiteId;
	    block.proposalDate = new Date();
	} else {
	    block = dbFetchBlock(lock.getConnection(), msg.blockId);
	    if (block == null
		    || block.status != SampleIdBlock.PROPOSED
                    || block.siteId != msg.sourceSiteId) {
		return ProcessedIsmCM.ignoreFailure(msg,
                        "Ignored claim for sample id block " + msg.blockId
                        + " from site id " + msg.sourceSiteId
                        + " because the remote site did not issue a proposal"
		        + " for the block previously");
	    }
	}
	assert block != null;
            
	// Record the claim in the database and log the activity
	block.claimDate = new Date();
	block.status = SampleIdBlock.CLAIMED;
	dbAddUpdateBlock(lock.getConnection(), block);
	return ProcessedIsmCM.success(msg,
                "Accepted claim for sample id block " + block.id
                + " from site id " + block.siteId);
    }

    /** Helper method for ISM processing */
    private CoreMessage eventTransferInitiate(SampleIdBlockISM msg, 
            AbstractLock lock) throws OperationFailedException, SQLException {
	/*
	 * Retrieve the referenced block and discard the message if
	 * the originating site doesn't already own it, unless the
	 * transfer came from the coodinator.
	 */
	SampleIdBlock block = dbFetchBlock(lock.getConnection(), msg.blockId);
	if (block == null
                || ((block.siteId != msg.sourceSiteId
                     || block.status != SampleIdBlock.CLAIMED)
		    && !msg.isFromCoordinator())) {
	    return ProcessedIsmCM.ignoreFailure(msg,
                    "Ignored transfer for sample id block " + msg.blockId 
                    + " from site id " + msg.sourceSiteId
                    + " because the remote site"
                    + " does not own the block");
	}
	if (msg.otherSiteId != siteManager.localSiteId) {
	    // Update the block's status in the database and log the activity
	    block.status = SampleIdBlock.TRANSFER_PENDING;
	    block.transferTargetSiteId = msg.otherSiteId;
	    block.transferDate = new Date();
	    dbAddUpdateBlock(lock.getConnection(), block);
	    return ProcessedIsmCM.success(msg,
                    "Recorded transfer begin for sample id block " + block.id
                    + " from site id " + msg.sourceSiteId + " to site id " 
                    + msg.otherSiteId);
	}
	
	/*
	 * If control reaches here then the remote site is transferring a block
	 * to the local site.  We need some special handling.  First decide if
	 * there's a need for the block.
	 */
	int unusedCount = dbCountBlocks(lock.getConnection(),
                SampleIdBlock.CLAIMED_LOCAL);
	int target = Integer.parseInt(properties.getProperty(
                "SamUnusedIdBlockTarget"));                
	if (unusedCount >= target && !msg.isFromCoordinator()) {
	    /*
	     * No need for this block. Reject the transfer and allow the
	     * transferring site to keep its block after all. Sending this kind
	     * of CM is not a deadlock risk
	     */
	    siteManager.passCoreMessage(new SendIsmCM(
                    SampleIdBlockISM.newTransferReject(
                    siteManager.localSiteId, block.id, block.siteId)));
	    return ProcessedIsmCM.success(msg,
                    "Rejected transfer of sample id block " + block.id
                    + " to the local site from site id " + msg.sourceSiteId
                    + " because the local site already owns " + unusedCount
                    + " unused blocks (target is " + target + ")");
	}

	/*
	 * Accept this block: update the database, send an announcement, and
	 * log the activity. Sending this kind of CM is not a deadlock risk.
	 */
	block.siteId = siteManager.localSiteId;
	block.status = SampleIdBlock.CLAIMED_LOCAL;
	block.claimDate = new Date();
	dbAddUpdateBlock(lock.getConnection(), block);
	siteManager.passCoreMessage(new SendIsmCM(
	        SampleIdBlockISM.newTransferComplete(siteManager.localSiteId, 
                block.id, msg.sourceSiteId)));
	return ProcessedIsmCM.success(msg, "Accepted transfer of sample"
                + " id block " + block.id + " to the local site from site id "
                + msg.sourceSiteId);
    }

    /** Helper method for ISM processing */
    private CoreMessage eventTransferReject(SampleIdBlockISM msg, 
            AbstractLock lock) throws OperationFailedException, SQLException {
	/*
	 * Retrieve the referenced block and postpone the ISM if the
	 * referenced block's state doesn't jive with the message
	 */
	SampleIdBlock block = dbFetchBlock(lock.getConnection(), msg.blockId);
	if (block == null
	        || block.status != SampleIdBlock.TRANSFER_PENDING
	        || block.siteId != msg.otherSiteId
	        || block.transferTargetSiteId != msg.sourceSiteId) {
	    return ProcessedIsmCM.failure(msg,
                    "Postponed processing of rejection of sample id block"
                    + " transfer for block " + msg.blockId + " from site id "
                    + msg.otherSiteId + " to site id " + msg.sourceSiteId
                    + " because no prior transfer initiation message has been"
                    + " received");
	}
        
	if (block.siteId != siteManager.localSiteId) {
	    /*
	     * Set the state values for the block back the way they were before
	     * the transfer was initiated.
	     */
	    block.status = SampleIdBlock.CLAIMED;
	    block.transferDate = null;
	    block.transferTargetSiteId = SiteInfo.INVALID_SITE_ID;
	    dbAddUpdateBlock(lock.getConnection(), block);
            return ProcessedIsmCM.success(msg,
                    "Recorded rejection of sample id block " + block.id 
                    + " during transfer from site id " + block.siteId
                    + " to site id " + block.transferTargetSiteId);
	}

	/*
	 * If control reaches here then the block previously belonged to the
	 * local site and now we're reclaiming it.
	 */
	block.status = SampleIdBlock.CLAIMED_LOCAL;
	block.transferDate = null;
	block.transferTargetSiteId = SiteInfo.INVALID_SITE_ID;
	dbAddUpdateBlock(lock.getConnection(), block);
	return ProcessedIsmCM.success(msg,
                "Cancelled transfer of sample id block " + block.id 
                + " to site id " + msg.sourceSiteId
                + " because the transfer was rejected by the remote site");
    }

    /** Helper method for ISM processing */
    private CoreMessage eventTransferComplete(SampleIdBlockISM msg, 
            AbstractLock lock) throws OperationFailedException, SQLException {
	/*
	 * Retrieve the referenced block and postponse the ISM if
	 * the referenced block's state doesn't jive with the message
	 */
	SampleIdBlock block = dbFetchBlock(lock.getConnection(), msg.blockId);
	if (block == null
  	        || block.status != SampleIdBlock.TRANSFER_PENDING
	        || block.siteId != msg.otherSiteId
	        || block.transferTargetSiteId != msg.sourceSiteId) {
	    return ProcessedIsmCM.failure(msg,
                    "Postponed processing of completion of sample id block"
                    + " transfer for block " + msg.blockId + " from site id "
                    + msg.otherSiteId + " to site id " + msg.sourceSiteId
                    + " because no prior transfer"
                    + " initiation message has been"
                    + " received");
	}
                    
	/*
	 * Update state values for the block from the old site to the new one
	 */
	block.siteId = block.transferTargetSiteId;
	block.status = SampleIdBlock.CLAIMED;
	block.transferDate = null;
	block.transferTargetSiteId = SiteInfo.INVALID_SITE_ID;
	dbAddUpdateBlock(lock.getConnection(), block);
	// Log the activity
	if (msg.otherSiteId != siteManager.localSiteId) {
	    return ProcessedIsmCM.success(msg,
                    "Recorded transfer complete for sample id block " 
                    + block.id + " from site id " + msg.otherSiteId
                    + " to site id " + msg.sourceSiteId);
	} else {
	    return ProcessedIsmCM.success(msg,
                    "Finished transfer of sample id block " + block.id 
                    + " to site id " + msg.sourceSiteId);
	}
    }

    /** Helper method for ISM processing */
    private CoreMessage eventTransferRequest(SampleIdBlockISM msg, 
            AbstractLock lock) throws OperationFailedException, SQLException {
	if (msg.hasExpired()) {
	    /*
	     * The transfer request is sufficiently old that we needn't bother
	     * answering it.  Just accept the ISM silently and move on.
	     */
	    return ProcessedIsmCM.success(msg,
                    "Ignored sample id block transfer request from from site"
                    + " id " + msg.sourceSiteId + " because the request is"
                    + " expired");
	}

	/*
	 * Next decide whether we have any blocks we can spare.
	 * 
	 * FIXME: make this logic more intelligent so that the
	 * sysadmin can configure *which* sites are allowed to
	 * borrow our blocks and which are not
	 */
	int unusedCount = dbCountBlocks(lock.getConnection(),
	        SampleIdBlock.CLAIMED_LOCAL);
	int loanThreshold = Integer.parseInt(properties.getProperty(
                "SamUnusedIdBlockLoanThreshold"));
	if (unusedCount > loanThreshold) {
	    /*
	     * We can spare a block; initiate the transfer by updating our db
	     * state and sending an ISM. Sending this kind of CM is not a
	     * deadlock risk.
	     */
	    SampleIdBlock block = dbFetchUnusedBlock(lock.getConnection());
	    block.status = SampleIdBlock.TRANSFER_PENDING;
	    block.transferDate = new Date();
	    block.transferTargetSiteId = msg.sourceSiteId;
	    dbAddUpdateBlock(lock.getConnection(), block);
	    sampleManager.passCoreMessage(new SampleIdBlockHintCM());
	    siteManager.passCoreMessage(new SendIsmCM(
	            SampleIdBlockISM.newTransferInitiate(
                    siteManager.localSiteId, block.id,
                    msg.sourceSiteId)));
	    return ProcessedIsmCM.success(msg,
                    "Began transfer of sample id block " + block.id
                    + " to site id " + block.transferTargetSiteId
                    + " because an emergency loan request was received");
	} else {
	    /*
	     * We have no blocks to spare; send a denial message.
	     * Sending this kind of CM is not a deadlock risk.
	     */
	    siteManager.passCoreMessage(new SendIsmCM(
	            SampleIdBlockISM.newTransferRequestDenied(
                    siteManager.localSiteId,
                    msg.sourceSiteId)));
	    return ProcessedIsmCM.success(msg,
                   "Denied sample id block transfer request from from site id "
                   + msg.sourceSiteId + " because the local site has no"
                   + " unused blocks to spare");
	}
    }

    /** Helper method for ISM processing */
    private CoreMessage eventTransferRequestDenied(SampleIdBlockISM msg, 
            AbstractLock lock) throws OperationFailedException, SQLException {
	/*
	 * No action need be taken for this kind of message because we
	 * currently have no tracking mechanism for them. These sorts of
	 * messages simply are a courtesy to us.
	 */
	return ProcessedIsmCM.success(msg,
                "Site id " + msg.sourceSiteId + " denied a sample id block"
                + " transfer request from the local site");
    }

    /**
     * Generates proposals for {@code count} new sample id blocks and
     * broadcasts corresponding ISMs. The caller must already hold a read/write
     * lock on the sampleIdBlocks table. The new block IDs are selected
     * randomly and will not conflict with a known sample ID block.
     * 
     * @param  count the number of blocks to propose 
     * @param  existingLock an active {@code AbstractLock} providing sufficient
     *         privileges for this action
     * 
     * @throws OperationFailedException
     */
    private void proposeSampleIdBlocks(int count, AbstractLock existingLock)
            throws OperationFailedException {
        // Quick exit if no blocks need to be proposed
        if (count <= 0) {
            return;
        }

        List<SendIsmCM> msgsToSend = new ArrayList<SendIsmCM>(count);
        
        for (int i = 0; i < count; i++) {
            try {
                // Randomly generate a block id number that isn't already in
		// use
                int blockId;
                
                do {
                    blockId = random.nextInt(SampleIdBlock.MAX_BLOCK_ID
                            - SampleIdBlock.MIN_BLOCK_ID)
                            + SampleIdBlock.MIN_BLOCK_ID;
                } while (dbFetchBlock(existingLock.getConnection(), blockId)
                        != null);

                // Flag the block as being PROPOSED_LOCAL
                SampleIdBlock proposedBlock = new SampleIdBlock(
                        SampleIdBlock.PROPOSED_LOCAL, blockId,
                        siteManager.localSiteId);
                dbAddUpdateBlock(existingLock.getConnection(), proposedBlock);

                /*
                 * Generate an ISM broadcast to ALL_SITES about this proposal.
                 * The other sites will respond back to us with comments until
                 * the expiration date is reached.
                 */
                msgsToSend.add(new SendIsmCM(
	                SampleIdBlockISM.newProposal(
                                siteManager.localSiteId,
                                blockId,
                                Long.parseLong(properties.getProperty(
                                        "SamIdBlockProposalPeriod")))));

                // Log this activity
                siteManager.recordLogRecord(
                        LogRecordGenerator.sampleIdBlockInitProposal(blockId));
            } catch (SQLException ex) {
                throw new OperationFailedException(ex);
            }
        }
        siteManager.passCoreMessages(msgsToSend);
    }

    /**
     * Sends requests for an emergency sample ID block loan to each known site.
     * The precise number of requests sent may vary depending upon Site Network
     * topology and size, but will not be less than {@code count}.  <p>
     *
     * The current implementation divides the loan requests amongst all known
     * remote sites, possibly adding a few additional loan requests as needed
     * such that every remote site is sent the same number of loan requests.
     * If every site responded affirmatively, we'd be offered more blocks than
     * we really needed and would need to reject some of the loans.  Although
     * this is inefficient with respect to the overall site network, it's the 
     * fastest way for the local site to satisfy an urgent need for additional
     * blocks.
     * 
     * @param  count the number of blocks to request
     * 
     * @throws OperationFailedException
     * @throws UnexpectedExceptionException
     */
    private void borrowSampleIdBlocks(int count)
            throws OperationFailedException {
        // Quick exit if no blocks need to be borrowed
        if (count <= 0) {
            return;
        }

	// Decide which sites we should borrow from.
	Collection<SiteInfo> lenderSites = new ArrayList(
                Arrays.asList(siteManager.getAllActiveSiteInfo()));
	Iterator<SiteInfo> it = lenderSites.iterator();
	while (it.hasNext()) {
	    SiteInfo site = it.next();
	    if (site.id == siteManager.localSiteId
		    || site.baseUrl == null) {
		// The current site is not able to loan us a sample id block.
		// Remove it from consideration.
		it.remove();
	    }
	}

        /*
         * Send private ISM's to every known site (except ourselves and sites
         * that have no URL) requesting an immediate transfer.
         */
        List<SendIsmCM> msgsToSend
                = new ArrayList<SendIsmCM>();
	int blockQuantityPerLender 
	        = (int) Math.ceil((double)count / lenderSites.size());
	long validityPeriod = Long.parseLong(properties.getProperty(
                "SamIdBlockTransferRequestPeriod"));
        for (SiteInfo lenderSite : lenderSites) {
            for (int i = 0; i < blockQuantityPerLender; i ++) {
                msgsToSend.add(new SendIsmCM(
                        SampleIdBlockISM.newTransferRequest(
                        siteManager.localSiteId, lenderSite.id, 
                        validityPeriod)));
            }
            siteManager.recordLogRecord(
                    LogRecordGenerator.sampleIdBlockEmergencyTransferInit(
                            count, lenderSite.id));
        }
        siteManager.passCoreMessages(msgsToSend);
    }

    /**
     * <p>
     * Scans the list of all active proposals that were initiated by the local
     * site and takes action on as many proposals as appropriate.
     * </p><p>
     * Active proposals are identified by a row in the sampleIdBlocks table
     * where the status is PROPOSED_LOCAL. Of those rows, the blocks where the
     * {@code proposalDate} is more than 'SamIdBlockProposalPeriod' (a
     * configuration directive) milliseconds ago may be acted upon: the block
     * is either CLAIM'ed or deleted, depending on whether the block's
     * {@code approvalCount} has exceeded a threshold. Also, blocks that have
     * received a "perfect" approval count (equal to the number of known sites
     * minus two - the Coordinator and the local site) are eligible to be
     * CLAIM'ed, regardless of how long the proposal has been active.
     * </p><p>
     * The approval count threshold is computed as half of the number of
     * sites currently in the site network, plus one.  This is intended to
     * prevent two sites from proposing and then attempting to claim the same
     * block simultaneously, based on the impossibility of more than one of
     * them receiving greater than 50% approval.  There is no guarantee,
     * however, that the number of sites in the site network would not
     * fluctuate during the proposal period, so this algorithm is imperfect.
     * This is an acceptable risk because sample ID block numbers are chosen at
     * random, and the chances of a conflict actually occurring are minute.
     * </p><p>
     * The caller should provide a lock with read/write privileges on
     * sampleIdBlocks.
     * </p>
     * 
     * @param  existingLock an active {@code AbstractLock} providing sufficient
     *         privileges for this operation
     * @throws OperationFailedException
     * @throws UnexpectedExceptionException
     */
    private void checkActiveProposals(AbstractLock existingLock)
            throws OperationFailedException, UnexpectedExceptionException {
        /*
         * TODO: Devise a better algorithm that avoids *any* possibility of two
         * sites attempting to claim the same block
         */
        try {
            Statement cmd = existingLock.getConnection().createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            String sql = "SELECT * FROM sampleIdBlocks" + " WHERE status="
                    + SampleIdBlock.PROPOSED_LOCAL + ";";
            ResultSet rs = cmd.executeQuery(sql);
            Date now = new Date();
            long maxProposalPeriod = Long.parseLong(
                    properties.getProperty("SamIdBlockProposalPeriod"));
            int siteCount = siteManager.getAllActiveSiteInfo().length;
            int perfectApprovalCount = siteCount - 2;
            int approvalCountThreshold = siteCount / 2 + 1;
            
            if (approvalCountThreshold > perfectApprovalCount) {
                approvalCountThreshold = perfectApprovalCount;
            }

            /*
             * Iterate through every locally-proposed block and act on the
             * actionable ones.
             */
            while (rs.next()) {
                SampleIdBlock block = new SampleIdBlock(rs);
                long proposalPeriod
                        = now.getTime() - block.proposalDate.getTime();
                
                if ((proposalPeriod < maxProposalPeriod)
                        && (block.approvalCount < perfectApprovalCount)) {
                    // This proposal is not actionable at this time.
                    continue;
                }

                if (block.approvalCount >= approvalCountThreshold) {
                    
                    /*
                     * Our proposal succeeded; we should issue a CLAIM for the
                     * block. Update the row in the database.
                     */
                    int originalApprovalCount = block.approvalCount;
                    
                    block.status = SampleIdBlock.CLAIMED_LOCAL;
                    block.claimDate = now;
                    block.approvalCount = SampleIdBlock.INVALID_APPROVAL_COUNT;
                    block.dbStore(rs);
                    rs.updateRow();

                    // Send a CLAIM ISM to all the other sites
                    siteManager.passCoreMessage(new SendIsmCM(
                            SampleIdBlockISM.newClaim(
                                    siteManager.localSiteId, block.id)));

                    // Log this activity
                    siteManager.recordLogRecord(
                            LogRecordGenerator.sampleIdBlockClaim(
                                    block.id, proposalPeriod,
                                    originalApprovalCount,
                                    perfectApprovalCount));
                } else {
                    
                    /*
                     * Our proposal failed; we should delete the block. If
                     * necessary and appropriate processSampleIdBlockHintCm()
                     * may be executed again to create a new proposal.
                     */
                    rs.deleteRow();
                    siteManager.recordLogRecord(
                            LogRecordGenerator.sampleIdBlockClaimFailure(
                                    block.id, proposalPeriod,
                                    block.approvalCount, 
				    perfectApprovalCount));
                }
            }
            cmd.close();
        } catch (SQLException ex) {
            throw new OperationFailedException(ex);
        }
    }

    /**
     * <p>
     * Scans the sampleIdBlocks table for outstanding, expired proposals issued
     * by other sites. This would be an abnormal condition because
     * normally a PROPOSAL from any site would be followed later by a CLAIM,
     * thus this function mainly perfoms garbage collection.  Another site's
     * proposal is considered to have expired if the block's
     * {@code proposalDate} field is older than two times the
     * 'SamIdBlockProposalPeriod' configuration parameter.
     * </p><p>
     * The caller should supply a lock cleared for read/write access to the
     * sampleIdBlocks table.
     * <p>
     * 
     * @param  existingLock an active {@code AbstractLock} providing sufficient
     *         privileges for this operation
     *         
     * @throws OperationFailedException
     */
    private void clearOldProposals(AbstractLock existingLock)
            throws OperationFailedException {
        try {
            Statement cmd = existingLock.getConnection().createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            String sql = "SELECT * FROM sampleIdBlocks" + " WHERE status="
                    + SampleIdBlock.PROPOSED + ";";
            ResultSet rs = cmd.executeQuery(sql);
            long timeNow = System.currentTimeMillis();
            long maxProposalPeriod = Long.parseLong(
                    properties.getProperty("SamIdBlockProposalPeriod"));

            while (rs.next()) {
                SampleIdBlock block = new SampleIdBlock(rs);
                long proposalPeriod = timeNow - block.proposalDate.getTime();
                
                if (proposalPeriod > maxProposalPeriod * 2) {
                    rs.deleteRow();
                    siteManager.recordLogRecord(
                            LogRecordGenerator.sampleIdBlockPurged(block));
                }
            }
            cmd.close();
        } catch (SQLException ex) {
            throw new OperationFailedException(ex);
        }
    }

    /**
     * Fills the {@code newSampleIds}
     * list with a randomly-ordered list of IDs that haven't been used before.
     * This method will open a new sample ID block (via
     * {@link #switchBlock(Connection)}) if the current block has been
     * exhausted. This method may promote the provided lock, therefore callers
     * should update their reference via
     * {@code AbstractLock.getPromotedVersion()}.
     * 
     * @param  existingLock an active {@code AbstractLock} upon which this
     *         method should build a lock representing the privileges it
     *         requires; may be promoted by this method
     * 
     * @throws DeadlockDetectedException
     * @throws OperationFailedException if a database error occurs
     * @throws ResourcesExhaustedException with reason SAMPLE_IDS_EXHAUSTED if
     *         there are no more CLAIMED_LOCAL sample id blocks at this site.
     * @throws UnexpectedExceptionException
     */
    private void buildIdList(AbstractLock existingLock)
            throws DeadlockDetectedException, OperationFailedException {
        try {
            
            /*
             * Loop until a sample id block with unused sample id's is found,
	     * or until there are no more sample id blocks assigned to the
	     * local site. Begin with the current sample id block as read from
	     * the database.
             */
            currentSampleIdBlock
                    = dbReadCurrentBlock(existingLock.getConnection());
            if (currentSampleIdBlock == null) {
                switchBlock(existingLock.getConnection());
            }
	    assert currentSampleIdBlock != null;

            do {
                /*
                 * build a list of all the sample id's that incorporate the
                 * site's currentSampleIdPrefix and that are not in use. A
                 * sample number is composed of a 21-bit prefix (assigned to
                 * this site by Reciprocal Net) and a 10-bit local extension.
                 */
                Set<Integer> proposedSampleIds = new HashSet<Integer>(
                        (4 * SampleIdBlock.BLOCK_SIZE + 2) / 3);
                AbstractLock oldLock = existingLock;
                
                for (int i = 0; i < SampleIdBlock.BLOCK_SIZE; i++) {
                    proposedSampleIds.add((currentSampleIdBlock.id
                            * SampleIdBlock.BLOCK_SIZE) + i);
                }

                // promote the lock so that these sample ids can be verified.
                existingLock = SampleLocks.idAgentBuildIdList(
                        oldLock, proposedSampleIds);
                lockAgent.registerLock(existingLock);
                existingLock.promoteFrom(oldLock);

                /*
                 * build a list of all proposed sample id's that have been used
                 * already.
                 */
                Set<Integer> usedSampleIds = sampleManager.verifySampleNumbers(
                        proposedSampleIds, existingLock);

                /*
                 * compare the set of used id's to the list of proposed id's;
                 * the differences between the two are the unused id's.
                 */
                proposedSampleIds.removeAll(usedSampleIds);
                newSampleIds.clear();
                newSampleIds.addAll(proposedSampleIds);

                if (newSampleIds.isEmpty()) {
                    switchBlock(existingLock.getConnection());
                }
            } while (newSampleIds.isEmpty());
            
            Collections.shuffle(newSampleIds, random);

            // If we get here then the task completed successfully.
        } catch (SQLException ex) {
            throw new OperationFailedException(ex);
        }
    }

    /**
     * <p>
     * Internal function called from buildIdList() when all sample id's under
     * the current block have been exhausted. Marks the current block (if any)
     * as CLAIMED_LOCAL_USED, then chooses a CLAIMED_LOCAL block from the
     * database and sets it to CLAIMED_LOCAL_CURRENT. Set conn to a valid
     * database connection for which a a read/write lock on table
     * sampleIdBlocks has been acquired.
     * </p><p>
     * In every case, a SampleIdCBlockHintCM is generated and sent back to
     * Sample Manager's worker thread as a signal that it should double-check
     * the number of unused blocks and take action to acquire more if
     * necessary.  That processing is done asynchronously since this method
     * normally is invoked from a webapp thread and we need to avoid blocking
     * for too much time.
     * </p><p>
     * This function should not be called unless either a) the current block of
     * prefixes has been exhausted, or b) no block is flagged as active in the
     * database.
     * </p>
     * 
     * @param  conn a {@code Connection} with which to obtain sample ID block
     *         information from the database
     * 
     * @throws OperationFailedException 
     * @throws ResourcesExhaustedException with reason SAMPLE_IDS_EXHAUSTED if
     *         there are no more CLAIMED_LOCAL sample id blocks at this site.
     * @throws SQLException 
     */
    private void switchBlock(Connection conn) throws OperationFailedException,
            ResourcesExhaustedException, SQLException {
        Statement cmd = conn.createStatement();
        String sql;
        // mark the current sample id block as full
        if (currentSampleIdBlock != null) {
            sql = "UPDATE sampleIdBlocks" + " SET status="
                    + SampleIdBlock.CLAIMED_LOCAL_USED + " WHERE id="
                    + currentSampleIdBlock.id + ";";
            cmd.executeUpdate(sql);
        }

        // find another claimed/unused sample id block
        currentSampleIdBlock = dbFetchUnusedBlock(conn);
        if (currentSampleIdBlock == null) {
            throw new ResourcesExhaustedException(
                    ResourcesExhaustedException.SAMPLE_IDS_EXHAUSTED);
        }

        // mark the new current sample id block as active
        sql = "UPDATE sampleIdBlocks" + " SET status="
                + SampleIdBlock.CLAIMED_LOCAL_CURRENT + " WHERE id="
                + currentSampleIdBlock.id + ";";
        cmd.executeUpdate(sql);
        cmd.close();

        /*
         * send a core message to the worker thread that will cause it to
         * examine the current sample id status at the local site
         */
        sampleManager.passCoreMessage(new SampleIdBlockHintCM());
    }

    /**
     * Examines the sample ID block status at the local site and makes a
     * decision (based upon configuration parameters), one of:
     * <ul>
     * <li>do nothing, if the number of {@code CLAIMED_LOCAL} sample ID blocks
     * meets or exceeds the configuration parameter
     * 'SamUnusedIdBlockTarget';</li>
     * <li>invoke {@link #proposeSampleIdBlocks(int, AbstractLock)
     * proposeSampleIdBlocks()} if the number of {@code CLAIMED_LOCAL} sample
     * ID blocks is greater than or equal to the configuration parameter
     * 'SamUnusedIdBlockMinimum'; or</li>
     * <li>invoke {@link #borrowSampleIdBlocks(int)} otherwise.</li>
     * 
     * @param existingLock an active lock providing sufficient privileges for
     *        this method's behavior
     * @throws OperationFailedException
     */
    private void actOnBlockStatus(AbstractLock existingLock)
            throws OperationFailedException {
        // Detect the current site's status with regard to blocks
        int cAvailable;
        int cProposed;
        try {
            cAvailable = dbCountBlocks(
                    existingLock.getConnection(), SampleIdBlock.CLAIMED_LOCAL);
            cProposed = dbCountBlocks(
                    existingLock.getConnection(), 
		    SampleIdBlock.PROPOSED_LOCAL);
        } catch (SQLException ex) {
            throw new OperationFailedException(ex);
        }

        // Read the relevent configuration parameters
        int cTarget = Integer.parseInt(
                properties.getProperty("SamUnusedIdBlockTarget"));
        int cMinimum = Integer.parseInt(
                properties.getProperty("SamUnusedIdBlockMinimum"));

	// Possibly generate some emergency loan requests.
	if (cAvailable < cMinimum) {
	    int blocksToBorrow = cMinimum - cAvailable;
            this.borrowSampleIdBlocks(blocksToBorrow);
	    cProposed += blocksToBorrow;
	}

	// Possibly generate some new sample id block proposals.
	if (cAvailable < cTarget) {
	    int blocksToPropose = cTarget - cAvailable - cProposed;
	    this.proposeSampleIdBlocks(blocksToPropose, existingLock);
	}
    }

    /**
     * Queries thew sampleIdBlocks table and returns a count of the number of
     * rows whose status value is set as specified. The caller must already
     * hold an appropriate type of lock. Valid status values are defined in the
     * {@code SampleIdBlock} class.
     * 
     * @param conn a {@code Connection} with which to obtain sample ID block
     *        information from the database
     * @param status the status code for which a block count is requested
     * @return the number of blocks currently in the database that have the
     *         specified status
     * @throws SQLException on database error.
     */
    private int dbCountBlocks(Connection conn, int status)
            throws SQLException {
        Statement cmd = conn.createStatement();
        String sql = "SELECT COUNT(*) AS c FROM sampleIdBlocks"
                + " WHERE status=" + status + ";";
        ResultSet rs = cmd.executeQuery(sql);
        int rval;
        
        rs.next();
        rval = rs.getInt("c");
        cmd.close();
        
        return rval;
    }

    /**
     * Fetches the "current" {@code SampleIdBlock} object from the
     * sampleIdBlocks table, or {@code null} if there is no block flagged as
     * current in the table. The caller must already hold an appropriate lock.
     * 
     * @param conn a {@code Connection} with which to obtain sample ID block
     *        information from the database
     * @return the current {@code SampleIdBlock}, or {@code null} if there
     *         isn't any
     * @throws SQLException
     */
    private SampleIdBlock dbReadCurrentBlock(Connection conn)
            throws SQLException {
        SampleIdBlock block;
        Statement cmd = conn.createStatement();
        String sql = "SELECT * FROM sampleIdBlocks" + " WHERE status="
                + SampleIdBlock.CLAIMED_LOCAL_CURRENT + ";";
        ResultSet rs = cmd.executeQuery(sql);
        
        if (rs.next() == false) {
            block = null;
        } else {
            block = new SampleIdBlock(rs);
        }
        cmd.close();
        
        return block;
    }

    /**
     * Fetches a {@code SampleIdBlock} object from the sampleIdBlocks table
     * given the block's id. Returns null if no such block could be found.
     * 
     * @param conn a {@code Connection} with which to obtain sample ID block
     *        information from the database
     * @param blockId the ID of the requested block
     * @return the {@code SampleIdBlock} bearing the specified ID, or
     *         {@code null} if there isn't any
     * @throws SQLException on database error.
     */
    private SampleIdBlock dbFetchBlock(Connection conn, int blockId)
            throws SQLException {
        SampleIdBlock block = null;
        Statement cmd = conn.createStatement();
        String sql = "SELECT * FROM sampleIdBlocks" + " WHERE id=" + blockId
                + ";";
        ResultSet rs = cmd.executeQuery(sql);
        
        if (rs.next()) {
            block = new SampleIdBlock(rs);
        }
        cmd.close();
        
        return block;
    }

    /**
     * Fetches and returns the {@code SampleIdBlock} object from the first row
     * in the sampleIdBlocks table with status CLAIMED_LOCAL, or returns
     * {@code null} if no such block could be found.
     * 
     * @param conn a {@code Connection} with which to obtain sample ID block
     *        information from the database
     * @return a {@code SampleIdBlock} representing the next unused block, or
     *         {@code null} if there isn't any
     * @throws SQLException on database error.
     */
    private SampleIdBlock dbFetchUnusedBlock(Connection conn)
            throws SQLException {
        SampleIdBlock block = null;
        Statement cmd = conn.createStatement();
        String sql = "SELECT * FROM sampleIdBlocks" + " WHERE status="
                + SampleIdBlock.CLAIMED_LOCAL + ";";
        ResultSet rs = cmd.executeQuery(sql);
        
        if (rs.next()) {
            block = new SampleIdBlock(rs);
        }
        cmd.close();
        
        return block;
    }

    /**
     * Writes the {@code SampleIdBlock} to the sampleIdBlocks table,
     * overwriting an existing row of the same ID if it exists or inserting a
     * new row if necessary.
     * 
     * @param conn a {@code Connection} with which to update sample ID block
     *        information into the database
     * @param block a {@code SampleIdBlock} containing the information to
     *        record
     * 
     * @throws SQLException on database error.
     */
    private void dbAddUpdateBlock(Connection conn, SampleIdBlock block)
            throws SQLException {
        Statement cmd = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_UPDATABLE);
        String sql = "SELECT * FROM sampleIdBlocks WHERE id=" + block.id + ";";
        ResultSet rs = cmd.executeQuery(sql);
        
        if (rs.next()) {
            // update the existing row
            block.dbStore(rs);
            rs.updateRow();
        } else {
            // insert a new row
            rs.moveToInsertRow();
            block.dbStore(rs);
            rs.insertRow();
        }
        cmd.close();
    }

    /**
     * Deletes any row from the {@code SampleIdBlocks} table that has the
     * specified id.
     * 
     * @param conn a {@code Connection} with which to update sample ID block
     *        information into the database
     * @param blockId the ID of the block to delete
     * 
     * @throws SQLException on database error.
     */
    private void dbDeleteBlock(Connection conn, int blockId)
            throws SQLException {
        Statement cmd = conn.createStatement();
        String sql = "DELETE FROM sampleIdBlocks WHERE id=" + blockId + ";";
        
        cmd.executeUpdate(sql);
        cmd.close();
    }
}
