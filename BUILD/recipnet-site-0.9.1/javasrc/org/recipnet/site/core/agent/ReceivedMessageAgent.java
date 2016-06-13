/*
 * Reciprocal Net project
 * 
 * ReceivedMessageAgent.java
 *
 * 31-Oct-2002: ekoperda wrote first draft
 * 04-Nov-2002: ekoperda added acceptBootstrapIsm() and made supporting changes
 *              to queueMessage()
 * 22-Nov-2002: ekoperda fixed bug 620 in ReceivedSiteState.computeFlags()
 * 09-Dec-2002: ekoperda fixed bug 626 in acceptIncomingIsms()
 * 21-Feb-2003: ekoperda added exception support throughout
 * 19-Mar-2003: midurbin added second version of generatePullRequest() and
 *              modified processProcessedIsmCM() to handle forced saving
 * 24-Mar-2003: ekoperda fixed bug 813 in acceptIncomingIsms()
 * 15-Apr-2003: midurbin moved logging code to LogRecordGenerator
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.core.util package
 *              to org.recipnet.site.core.agent; also changed package
 *              references due to source tree reorganization
 * 04-Jun-2004: cwestnea modified processProcessedIsmCM() to deal with new
 *              InvalidDataException thrown by 
 *              SiteManager.writeUpdatedSiteInfo()
 * 19-Jan-2006: ekoperda rewrote class to support ISM exchanges, link-local
 *              ISM's, and replay responses
 * 20-Feb-2006: ekoperda added dispatchNextMessageInBootstrapMode(), 
 *              identifyStalledQueues(), countMessagesInQueues(), concept of
 *              bootstrap mode to support new class SiteNetworkSynchronizer
 * 11-Apr-2006: jobollin removed inaccessible catch blocks and organized
 *              imports
 * 05-Jun-2006: jobollin removed unused Javadocs of WrongSiteException
 * 07-Jan-2008: ekoperda fixed bug #1873 in 
 *              RemoteSiteState.getSeqnumForLastQueuedPublicIsm()
 * 28-Nov-2008: ekoperda added logic for delayed site deactivations and
 *              improved handling of ISM's from deactivated sites
 * 02-Jan-2009: ekoperda enhanced RemoteSiteState.notifyOtherSitesIsmProcessed
 *              with special logic for JoinISM's for increased efficiency
 * 02-Jan-2009: ekoperda relaxed arguments to generatePullRequests()
 */

package org.recipnet.site.core.agent;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.logging.LogRecord;
import org.recipnet.site.InvalidDataException;
import org.recipnet.site.OperationFailedException;
import org.recipnet.site.UnexpectedExceptionException;
import org.recipnet.site.core.IsmProcessingException;
import org.recipnet.site.core.MessageDecodingException;
import org.recipnet.site.core.RepositoryManager;
import org.recipnet.site.core.ResourceException;
import org.recipnet.site.core.ResourceNotFoundException;
import org.recipnet.site.core.SampleManager;
import org.recipnet.site.core.SiteManager;
import org.recipnet.site.core.msg.InterSiteMessage;
import org.recipnet.site.core.msg.JoinISM;
import org.recipnet.site.core.msg.ProcessedIsmCM;
import org.recipnet.site.core.msg.ReplayRequestISM;
import org.recipnet.site.core.msg.ReplayResponseISM;
import org.recipnet.site.core.util.LogRecordGenerator;
import org.recipnet.site.shared.db.SiteInfo;
import org.xml.sax.SAXException;

/**
 * Helper class used by Site Manager to keep track of received inter-site
 * messages as they are decoded, held, processed, and finally cleared. It
 * synchronizes ISM processing across the three core modules so that only one
 * ISM is processed at a time. It handles all ProcessedIsmCM messages that Site
 * Manager's worker thread receives and uses these notices to mark completion
 * of an ISM that was formerly being processed. It maintains a queue of pending
 * messages in memory and manages the corresponding message files in the
 * 'msgs-held' directory of the filesystem. It uses methods on Site Manager to
 * keep the 'publicSeqNum' and 'privateSeqNum' columns of the 'sites' table
 * consistent with current ISM state.
 */
public class ReceivedMessageAgent {
    /**
     * A reference to the {@code SiteManager}; set by the constructor.
     */
    private final SiteManager siteManager;

    /**
     * A reference to the {@code SampleManager}; set by the constructor.
     */
    private final SampleManager sampleManager;

    /**
     * A reference to the {@code RepositoryManager}; set by the constructor.
     */
    private final RepositoryManager repositoryManager;

    /**
     * A refernece to the {@code MessageFileAgent}; set by the constructor.
     * Callers must synchronize on this object before invoking any of its
     * methods.
     */
    private final MessageFileAgent messageFileAgent;

    /**
     * A refernece to the {@code TopologyAgent}; set by the constructor.
     */
    private final TopologyAgent topologyAgent;

    /**
     * Configuration directive set by the constructor that controls whether
     * processed messages are always saved to the received messages directory,
     * or are saved only when otherwise required.
     */
    private final boolean alwaysDumpMessages;

    /**
     * Configuration directive set by the constructor that limits the number of
     * milliseconds a message file may sit in the held messages directory
     * before it becomes eligible for deletion.
     */
    private final long messageHoldTime;

    /**
     * Configuration directive set by the constructor that limits the number of
     * milliseconds a core module may spend processing a received ISM before
     * this class assumes that processing has stalled.
     */
    private final long messageProcessingTimeout;

    /**
     * Configuration directive set by the constructor that limits the number of
     * ISM's the local site will replay at the request of a remote site, per
     * exchange.
     */
    private final int maxReplayedIsmsPerExchange;

    /**
     * Maps Integer's representing site id's to RemoteSiteState objects. One
     * component of the state table.
     */
    private Map<Integer, RemoteSiteState> remoteSites;

    /**
     * Pointer to the ISM currently "in processing", or null if no ISM is
     * currently being processed. One component of the state table.
     */
    private InterSiteMessage currentMessage;

    /**
     * The timestamp at which the current ISM started to be processed. Value is
     * undefined if there is no current ISM. One component of the state table.
     */
    private long currentMessageDispatchTimestamp;

    /**
     * The siteId taken from the *previous* message that was processed, or
     * INVALID_SITE_ID if there is no previous message.
     */
    private int originatingSiteForLastMessage;

    private boolean bootstrapMode;

    /**
     * Constructor.
     * 
     * @param siteManager reference to Site Manager, which can be considered
     *        the "parent" object of this one. Using this reference the
     *        {@code localSiteId} value is obtained, and the getSiteInfo(),
     *        getAllSiteInfo(), putSiteInfo(), and passCoreMessage() functions
     *        are invoked.
     * @param sampleManager reference to Sample Manager. The only function
     *        invoked via this reference is {@code passCoreMessage()}.
     * @param repositoryManager reference to Repository Manager. The only
     *        function invoked via this reference is {@code passCoreMessage()}.
     * @param messageFileAgent reference to Site Manager's
     *        {@code MessageFileAgent} object. Using this reference the
     *        {@code readAllHeldMessages()}, {@code clearHeldMessages()} and
     *        {@code writeReceivedMessage()} functions are invoked. Because
     *        this reference is assumed to not be reserved for the exclusive
     *        use of this {@code ReceivedMessageAgent}, this class will
     *        synchronize on the {@code messageFileAgent} before invoking any
     *        of its methods.
     * @param topologyAgent reference to Site Manager's {@code TopologyAgent}
     *        object. Using this reference the {@code shouldRetainIsm()}
     *        function is invoked.
     * @param alwaysDumpMessages decoded value of the configuration directive
     *        'SitMsgsAlwaysDump'; controls whether processed messages are
     *        always saved to the 'msgs-recv' directory, or saved only when
     *        otherwise required.
     * @param messageHoldTime decoded value of the configuration directive
     *        'SitMsgsHoldTime'; this is the maximum number of milliseconds a
     *        message file may sit in the 'msgs-held' directory before it
     *        becomes eligible for deletion.
     * @param messageProcessingTimeout decoded value of the configuration
     *        directive 'SitIsmProcessingTimeout'; this is the maximum number
     *        of milliseconds a core module may spend processing a received ISM
     *        before this class assumes that processing has stalled.
     * @param maxReplayedIsmsPerExchange decoded value of the configuration
     *        directive 'SitReceivedIsmReplayLimit'; limits the number of ISM's
     *        the local site will replay at the request of a remote site, per
     *        exchange.
     * @param bootstrapMode
     */
    public ReceivedMessageAgent(SiteManager siteManager,
            SampleManager sampleManager, RepositoryManager repositoryManager,
            MessageFileAgent messageFileAgent, TopologyAgent topologyAgent,
            boolean alwaysDumpMessages, long messageHoldTime,
            long messageProcessingTimeout, int maxReplayedIsmsPerExchange,
            boolean bootstrapMode) {
        this.siteManager = siteManager;
        this.sampleManager = sampleManager;
        this.repositoryManager = repositoryManager;
        this.messageFileAgent = messageFileAgent;
        this.topologyAgent = topologyAgent;
        this.alwaysDumpMessages = alwaysDumpMessages;
        this.messageHoldTime = messageHoldTime;
        this.messageProcessingTimeout = messageProcessingTimeout;
        this.maxReplayedIsmsPerExchange = maxReplayedIsmsPerExchange;
        this.remoteSites = new HashMap<Integer, RemoteSiteState>();
        this.currentMessage = null;
        this.currentMessageDispatchTimestamp = 0;
        this.originatingSiteForLastMessage = SiteInfo.INVALID_SITE_ID;
        this.bootstrapMode = bootstrapMode;
    }

    /**
     * Should be called during Site Manager's startup phase, after the 'sites'
     * table has been preloaded. Synchronizes this object's sense of which
     * sites are valid with Site Manager.
     * 
     * @throws OperationFailedException
     * @throws UnexpectedExceptionException
     */
    public synchronized void initializeSites()
            throws OperationFailedException {
        remoteSites.clear();
        for (SiteInfo site : this.siteManager.getAllSiteInfo()) {
            if (site.id != this.siteManager.localSiteId
		    && site.isActive) {
                this.remoteSites.put(site.id, new RemoteSiteState(site));
            }
        }
    }

    /**
     * Should be called during Site Manager's startup phase, after a prior call
     * to initializeSites(). Read all message files from the 'msgs-held'
     * directory into memory (thus initializing the state table) and attempts
     * to dispatch one of them to a core module.
     * 
     * @throws OperationFailedException
     */
    public synchronized void readHeldMessages()
            throws OperationFailedException {
        synchronized (this.messageFileAgent) {
            String messageStrings[] = this.messageFileAgent.pollHeldMessages(
                    this.messageHoldTime);
            exchangeInterSiteMessages(messageStrings,
                    "automatic redelivery agent", false, false);
        }
        hintDispatchNext();
    }

    /**
     * Generates a set of link-local ISM's that will induce a specified remote
     * site to replay previously-generated ISM's. The ISM's the remote site
     * will replay might have been generated by a third site. The caller
     * specifies which remote sites' ISM's are to be replayed.
     * 
     * @return a collection of zero or more {@code InterSiteMessage} objects,
     *         each of which has its {@code linkLocal} field set to true, that
     *         is suitable for transmission to {@code destSiteId}.
     * @param destSiteId identifies the remote site to which the caller intends
     *        to transmit the link-local ISM's returned by this function.
     * @param targetSiteIds a collection of zero or more Integer's, each of
     *        which identifies a remote site whose ISM's the local site wishes
     *        to receive.  Any unrecognized site id's in this collection are
     *        ignored silently.
     * @param maxIsmsToReplay the maximum number of replayed ISM's the caller
     *        wishes to receive from the remote site. In the current
     *        implementation, this value is passed transparently to the
     *        constructor of {@code ReplayRequestISM}.
     */
    public synchronized Collection<InterSiteMessage> generatePullRequests(
            int destSiteId, Collection<Integer> targetSiteIds,
            long maxIsmsToReplay) {
        Collection<InterSiteMessage> isms = new ArrayList<InterSiteMessage>();
        for (Integer targetSiteId : targetSiteIds) {
            RemoteSiteState remoteSite = this.remoteSites.get(targetSiteId);
	    if (remoteSite != null) {
   	        long excludePublicSeqNumsUpTo 
                        = remoteSite.getSeqnumForLastQueuedPublicIsm();
                long excludePrivateSeqNumsUpTo = (destSiteId == targetSiteId)
                        ? remoteSite.getSeqnumForLastQueuedPrivateIsm()
                        : InterSiteMessage.INVALID_SEQ_NUM;
                isms.add(new ReplayRequestISM(this.siteManager.localSiteId,
                        destSiteId, targetSiteId, excludePublicSeqNumsUpTo,
                        excludePrivateSeqNumsUpTo, maxIsmsToReplay));
	    }
        }
        return isms;
    }

    /**
     * Should be called by Site Manager's worker thread whenever it receives a
     * {@code ProcessedIsmCM}. This handler completely consumes the message in
     * the sense that Site Manager need not perform any further processing on
     * it. The sequence numbers in the 'sites' table are updated if necessary
     * via calls to {@code SiteManager.getSiteInfo()} and
     * {@code SiteManager.writeUpdatedSiteInfo()}.
     * 
     * @param msg The processed Ism.
     * @throws IsmProcessingException
     * @throws OperationFailedException
     * @throws UnexpectedExceptionException
     */
    public synchronized void processProcessedIsmCM(ProcessedIsmCM msg)
            throws IsmProcessingException, OperationFailedException {
        if (msg.shouldCheckIfCurrent && (msg.ism != this.currentMessage)) {
            throw new IsmProcessingException("Processing state error: ISM just"
                    + " processed is not the current one", msg.ism);
        }
        if (msg.shouldUpdateDb) {
            // Update Site Manager's state in the database
            try {
                SiteInfo site = siteManager.getSiteInfo(msg.ism.sourceSiteId);

		// Update the seqnum.
                if (msg.ism.isPublic()) {
                    site.publicSeqNum = msg.ism.sourceSeqNum;
                } else {
                    site.privateSeqNum = msg.ism.sourceSeqNum;
                }
                siteManager.writeUpdatedSiteInfo(site);
            } catch (InvalidDataException ex) {
                // This shouldn't happen because all that is changed is the
                // seqNum
                throw new UnexpectedExceptionException(ex);
            }
        }
        if (msg.shouldUpdateState) {
            // Update all RemoteSiteState objects.
            this.currentMessage = null;
            for (RemoteSiteState remoteSite : this.remoteSites.values()) {
                if (msg.ism.isFrom(remoteSite.getSiteId())) {
                    remoteSite.notifyIsmProcessed(msg.ism,
                            !msg.shouldRevertState);
                } else {
                    remoteSite.notifyOtherSitesIsmProcessed(msg.ism);
                }
            }

	    // Maybe announce the site as fully deactivated.
	    SiteInfo site = siteManager.getSiteInfo(msg.ism.sourceSiteId);
  	    if (site.isActive
		    && site.finalSeqNum != InterSiteMessage.INVALID_SEQ_NUM
  		    && site.finalSeqNum == msg.ism.sourceSeqNum) {
		siteManager.performFullSiteDeactivation(site.id, 
                        site.finalSeqNum);
		siteManager.recordLogRecord(
	                LogRecordGenerator.ismSiteDeactivatedDelayed(site));
	    }
        }
        if (msg.shouldRevertState) {
            this.currentMessage = null;
            // Put this ISM back into the pending list.
            RemoteSiteState remoteSite
                    = this.remoteSites.get(msg.ism.sourceSiteId);
            remoteSite.notifyIsmProcessed(msg.ism, false);
        }

        // Log a completion message.
        if (msg.shouldLogMessage) {
            long processingTime = System.currentTimeMillis()
                    - currentMessageDispatchTimestamp;
            siteManager.recordLogRecord(LogRecordGenerator.ismProcessed(msg,
                    processingTime));

        }

        // Clear the message from the held-messages directory, possibly save a
        // a copy of it in the received-messages directory.
        if (msg.shouldClearFile) {
            synchronized (this.messageFileAgent) {
                this.messageFileAgent.clearHeldMessage(msg.ism,
                        shouldRetainMessageFile(msg.ism));
            }
        }

        // Possibly dispatch a new ISM that's pending.
        if (!this.bootstrapMode) {
            hintDispatchNext();
        }
    }

    /**
     * Should be called by Site Manager's worker thread whenever it receives a
     * {@code SiteActivationISM}. This object updates its state tables
     * accordingly so that ISM's from the new site can be processed. The method
     * has no effect if the specified {@code site} was already known.
     */
    public synchronized void notifySiteActivation(SiteInfo site) {
        if (!this.remoteSites.containsKey(site.id)) {
            this.remoteSites.put(site.id, new RemoteSiteState(site));
        }
    }

    /**
     * Either this method or notifyFullSiteDeactivation() should be called by
     * Site Manager's worker thread whenever it receives a SiteDeactivationISM.
     * Calling this method in particular indicates that a site deactivation has
     * been initiated but that additional ISM's generated by that site must be
     * processed before the deactivation can become final.
     * 
     * This object updates its state tables accordingly.  This method has no
     * effect if the specified siteId was not already known.
     */
    public synchronized void notifyPartialSiteDeactivation(int siteId, 
            long finalSeqNum) {
	RemoteSiteState remoteSite = this.remoteSites.get(siteId);
	if (remoteSite != null) {
	    remoteSite.purgePendingMessagesBySeqNum(finalSeqNum);
	}
    }

    /**
     * Either this method or notifyPartialSiteDeactivation() should be called
     * by Site Manager's worker thread whenever it receives a
     * SiteDeactivationISM.  Calling this method in particular indicates that
     * the site deactivation is complete and that no further ISM's from the
     * site should be processed.
     *
     * This object updates its state tables accordingly.  The method has no
     * effect if the specified {@code siteId} was not already known.
     */
    public synchronized void notifyFullSiteDeactivation(int siteId) {
        this.remoteSites.remove(siteId);
    }

    /**
     * Should be called by Site Manager's worker thread whenever it receives a
     * {@code SiteResetISM}. This object updates its state tables accordingly.
     * 
     * @throws OperationFailedException
     * @throws ResourceNotFoundException
     */
    public synchronized void notifySiteReset(SiteInfo site)
            throws OperationFailedException, ResourceNotFoundException {
        RemoteSiteState remoteSite = this.remoteSites.get(site.id);
        if (remoteSite == null) {
            throw new ResourceNotFoundException(site);
        }
        remoteSite.resetSequenceNumbers(site);
    }

    /**
     * Should be called every now and then as a periodic task by Site Manager's
     * task scheduler. Checks to see if the current message has taken an
     * excessively long time to process and logs a message if this is the case.
     * Also reads all message files from the 'msgs-held' directory, deleting
     * those that are extremely old, and redelivers all of them to the queue.
     * TODO: most of these tasks are probably unnecessary, and are here just as
     * a failsafe. Remove them once we're sure the ISM-passing implementation
     * is sufficiently stable.
     * 
     * @throws OperationFailedException
     */
    public synchronized void periodicCheck() throws OperationFailedException {
        // Make sure the current ISM hasn't been in processing for an obscenely
        // long time.
        long now = System.currentTimeMillis();
        
        if ((currentMessage != null)
                && ((now - currentMessageDispatchTimestamp)
                        > this.messageProcessingTimeout)) {
            this.siteManager.recordLogRecord(
                    LogRecordGenerator.ismProcessingStalled(
                            currentMessage.getSuggestedFileName(),
                            now - currentMessageDispatchTimestamp));
        }

        /*
         * Dredge up all the held-messages and redeliver them. This probably
         * won't have any discernable effect, but we do the operation just in
         * case. This is also where we delete very old held-message files.
         */
        synchronized (this.messageFileAgent) {
            String messageStrings[]
                    = this.messageFileAgent.pollHeldMessages(messageHoldTime);
            
            exchangeInterSiteMessages(messageStrings,
                    "automatic redelivery agent", false, false);
        }
        hintDispatchNext();
    }

    /**
     * Should be called by Site Manager whenever ISM's sent by another site
     * need to be processed. The ISM's might have arrived just a moment ago in
     * the request portion of a remotely-inititated ISM exchange, or might have
     * arrived just a moment ago in the response portion of a locally-initiated
     * ISM exchange, or might have arrived a long time ago and are being
     * automatically redelivered. This function takes as an argument String
     * representations of the ISM's sent by the remote site. It decodes them,
     * validates their signatures, and acts upon them. Any reply ISM's that may
     * be generated in the course of processing the incoming ISM's are returned
     * to the caller. The caller would be expected to transmit these responses
     * back to the remote site if a suitable response channel is available.
     * <p>
     * Incoming link-local ISM's are processed synchronously by this method and
     * its helpers. Digital signatures are verified on these. Responses in the
     * form of link-local ISM's addressed to the remote site may be generated
     * to indicate success, or failure, or status. Additionally, replays of
     * previous ISM's suitable for transmission to the remote site may be
     * included among the responses.
     * <p>
     * Incoming regular ISM's (non-link-local) are enqueued for later
     * asynchronous processing by the appropriate core module. They are written
     * to the held-messages directory on the filesystem in the meantime for
     * safekeeping. Digital signatures are verified on these ISM's, and
     * incoming ISM's that are malformed, or poorly-addressed, or forged, or
     * that already have been processed by the local site are dropped. Dropping
     * them early, before they have been enqueued, is more efficient than
     * dropping them later. Because incoming regular ISM's are processed
     * asynchronously, responses to them are not available at the time this
     * function returns.
     * <p>
     * This method and its helpers keep detailed statistics about the incoming
     * messages and the local site's response to them, and these are written to
     * the log at the conclusion of each exchange. Error codes and statistics
     * are not available to the caller because they generally are not conveyed
     * back to the remote site that initiated the exchange.
     * 
     * @return an array of zero or more {@code String}'s, each of which is an
     *         XML document that contains a signed signed representation of an
     *         {@code InterSiteMessage} addressed to the remote site. These are
     *         responses to the incoming ISM's passed to this function as
     *         {@code messageStrings}. The caller is expected to transmit these
     *         ISM's back to the remote site as part of an exchange. The return
     *         value will be null exactly when {@code responseChannelAvailable}
     *         is false.
     * @param messageStrings an array of zero or more {@code String}'s, each of
     *        which is an XML representation of an {@code InterSiteMessage}
     *        that was transmitted to the local site from the remote site as
     *        part of an exchange.
     * @param remoteSite a String that identifies the source of these messages,
     *        i.e. '129.79.85.43' or 'site id 34566'. Used in formulating the
     *        log message.
     * @param responseChannelAvailable a boolean value that indicates to this
     *        function whether the caller has a convenient mechanism for 
     *        passing response ISM's back to the remote site. Normally this
     *        value would be true in the case of a remotely-initiated ISM
     *        exchange and false in the case of a locally-initiated ISM
     *        exchange.
     * @param allowLinkLocalProcessing a boolean value that indicates to this
     *        function whether it is permitted to process link-local ISM's that
     *        it may find within {@code messageStrings}. Normally this value
     *        would be true in the case of a live ISM exchange and false in the
     *        case of an automatic redelivery. If this is false, any link-local
     *        messages that might be found within {@code messageStrings} are
     *        ignored silently.
     * @throws OperationFailedException on low-level error. Note that to the
     *         greatest extent possible, exceptions specific to one particular
     *         message are not propagated to the caller but instead are caught
     *         and logged by this method
     */
    public synchronized String[] exchangeInterSiteMessages(
            String messageStrings[], String remoteSite,
            boolean responseChannelAvailable, boolean allowLinkLocalProcessing)
            throws OperationFailedException {
        IsmExchangeStats stats = new IsmExchangeStats(remoteSite,
                messageStrings.length);
        Collection<String> replies = new ArrayList<String>();

        for (String ismAsXml : messageStrings) {
            InterSiteMessage ism = decodeIsmFromString(ismAsXml, stats);
            
            if (ism == null) {
                /*
                 * Nothing to do; the ISM was not decoded. An appropriate
                 * statistics counter has already been incremented.
                 */
            } else if (ism.linkLocal) {
                // We have a special ISM that requires link-local processing.
                if (allowLinkLocalProcessing) {
                    processLinkLocalIsm(ism, replies, stats);
                }
                stats.linkLocal++;
            } else if (verifyIsmSuitability(ism, stats)) {
                /*
                 * We have a regular ISM that should be delivered to the
                 * incoming message queue.
                 */
                synchronized (this.messageFileAgent) {
                    this.messageFileAgent.writeHeldMessage(ismAsXml,
                            ism.sourceSiteId, ism.sourceSeqNum);
                }
                this.remoteSites.get(ism.sourceSiteId).queueMessage(ism);
                stats.acceptedAndQueued++;
            } else {
                /*
                 * The ISM is being dropped for some reason. At least one of 
		 * the statistics counters would have been incremented in this
		 * case.  We can clear any held-file that may exist.
                 */
                this.siteManager.passCoreMessage(
                        ProcessedIsmCM.clearOldFile(ism));
            }
        }

        // Clean up and return replies to the caller.
        siteManager.recordLogRecord(stats.getSummaryForLogging());
        if ((stats.acceptedAndQueued > 0) && !this.bootstrapMode) {
            hintDispatchNext();
        }
        return replies.toArray(new String[replies.size()]);
    }

    /**
     * To be used only during Site Manager's "bootstrap" operation, this method
     * accepts for processing an ISM that was read from the site grant file.
     * The caller is responsible for parsing and validating the ISM prior to
     * invoking this method.
     * 
     * @param ism the inter-site message to be processed.
     * @throws IllegalStateException if this object was not initialized in
     *         bootstrap mode.
     * @throws IsmProcessingException if the ISM could not be processed.
     * @throws OperationFailedException on low-level error.
     */
    public synchronized void acceptBootstrapIsm(InterSiteMessage ism)
            throws IsmProcessingException, OperationFailedException {
        if (!this.bootstrapMode) {
            throw new IllegalStateException();
        }
        this.remoteSites.get(ism.sourceSiteId).queueMessage(ism);
        hintDispatchNext();
    }

    /**
     * For a specified remote site that the local site has pulled ISM's from
     * previously, fetches the number of ISM's presumably still available from
     * that remote site. In general, a return value from this function that is
     * greater than 0 suggests that the local site should initiate a pull
     * request to the specified remote site. The actual number of ISM's such a
     * pull might elicit is approximated by this function's return value but is
     * not guaranteed to be exact.
     * <p>
     * Because it is possible that the same ISM might be included in the hinted
     * counts of two different remote sites, it is not useful to sum the hinted
     * counts from several sites to arrive at any "master total".
     * <p>
     * The count returned by this function is derived from the contents of
     * {@code ReplayResponseISM}'s that have been passed to
     * {@code exchangeInterSiteMessages()} previously. Presumably such
     * {@code ReplayResponseISM}'s would have been received from the remote
     * site as part of a locally-initiated pull operation. This function
     * returns 0 for any remote site for which no hint information has been
     * received. 
     * @return an approximation of the count of ISM's that would be eligible
     *         for replay to the local site if it were to initiate an ISM pull
     *         operation against the specified remote site. In general, a value
     *         greater than 0 suggests that the caller should initiate a pull
     *         operation to the remote site.
     * @param siteId identifies the remote site whose hinted count should be
     *        returned.
     * @throws IllegalArgumentException if siteId is not recognized as a valid
     *         site identifier.
     */
    public synchronized long getHintedAvailableMessageCount(int siteId) {
        RemoteSiteState remoteSite = this.remoteSites.get(siteId);
        
        if (remoteSite == null) {
            throw new IllegalArgumentException();
        }
        
        return remoteSite.getCountAvailableMessageHints();
    }

    /**
     * For use only when the core modules were initialized in bootstrap mode,
     * this method possibly causes an inter-site message to be pulled from the
     * queue of pending messages and processed by the appropriate core module.
     * ISM processing occurs synchronously in bootstrap mode as there are no
     * worker threads. Thus, this function will not return until the ISM has
     * been processed or has failed processing.
     * 
     * @return true if an inter-site message was dispatched for processing, or
     *         false otherwise.
     * @throws IllegalStateException if this object was not initialized in
     *         bootstrap mode.
     */
    public synchronized boolean dispatchNextMessageInBootstrapMode()
            throws OperationFailedException {
        if (!this.bootstrapMode) {
            throw new IllegalStateException();
        }
        
        return hintDispatchNext();
    }

    /**
     * Identifies those other sites whose received-messages are sitting in our
     * queue but cannot be processed for one reason or another.
     * 
     * @return a collection of zero or more integer site id's.
     */
    public synchronized Collection<Integer> identifyStalledQueues() {
        Collection<Integer> siteIdsWithStalledQueues 
                = new ArrayList<Integer>();
        
        for (RemoteSiteState remoteSite : this.remoteSites.values()) {
            if ((remoteSite.getQueueSize() > 0)
                    && !remoteSite.isMessageAvailableForProcessing()) {
                siteIdsWithStalledQueues.add(remoteSite.getSiteId());
            }
        }
        
        return siteIdsWithStalledQueues;
    }

    /**
     * Counts the number if inter-site messages presently sitting in our
     * queues, awaiting processing. Note that there is no guarantee that the
     * messages counted by this function actually can be processed, as some
     * messages may later fail processing or some queues may stall.
     */
    public synchronized long countMessagesInQueues() {
        long count = 0;
        
        for (RemoteSiteState remoteSite : this.remoteSites.values()) {
            count += remoteSite.getQueueSize();
        }
        
        return count;
    }

    /**
     * Examines the current ISM state and possibly dispatches a pending ISM to
     * one of the core modules' worker threads. No ISM is dispatched if one is
     * currently being processed. When there are ISM's pending from several
     * sites, the various sites' queues are serviced in the order defined by 
     * the {@code RemoteSiteComparator}.
     * 
     * @return true if a message was dispatched as a result of this method
     *         invocation, or false otherwise.
     */
    private synchronized boolean hintDispatchNext()
            throws OperationFailedException {
        if (this.currentMessage != null) {
            /*
             * An ISM is currently being processed; we can't dispatch another
             * ISM until the current one's processing has finished.
             */
            return false;
        }

        /*
         * Sort the set of all remote sites in order of those from which it
         * would be most desirable to process a message.
         */
        ArrayList<RemoteSiteState> sortedRemoteSites
                = new ArrayList<RemoteSiteState>(this.remoteSites.values());
        Collections.sort(sortedRemoteSites, new RemoteSiteComparator(
                this.originatingSiteForLastMessage));

        /*
         * Choose the first message in the queue from the first site that has a
         * message queued.
         */
        InterSiteMessage ism = null;
        for (RemoteSiteState remoteSite : sortedRemoteSites) {
            ism = remoteSite.getNextMessageForProcessing();
	    if (ism == null) {
		continue;
	    }
	    if (this.bootstrapMode) {
		// Process this ISM.
		break;
	    }
	    // Verify again that the ISM is suitable for processing.  We should
	    // have checked before enqueueing this ISM, but there's a slight
	    // chance the ISM's suitability might have changed since then.
	    if (this.verifyIsmSuitability(ism, null)) {
		// Process this ISM.
		break;
	    } else {
		// This ISM failed verification.  Return it to the queue.
		remoteSite.notifyIsmProcessed(ism, false);
		ism = null;
		continue;
	    }
        }
        if (ism != null) {
            // Dispatch a message to the appropriate core module's worker
            // thread.
            this.currentMessage = ism;
            this.originatingSiteForLastMessage = ism.sourceSiteId;
            this.currentMessageDispatchTimestamp = System.currentTimeMillis();
            if (ism.deliverToSiteManager) {
                this.siteManager.passCoreMessage(ism);
            } else if (ism.deliverToSampleManager) {
                this.sampleManager.passCoreMessage(ism);
            } else if (ism.deliverToRepositoryManager) {
                this.repositoryManager.passCoreMessage(ism);
            } else {
                this.siteManager.passCoreMessage(ProcessedIsmCM.success(ism));
            }
            return true;
        }
        return false;
    }

    /**
     * Internal function that decides whether a message recently received from
     * the Internet (and which has a message file currently sitting in the
     * 'msgs-held' directory) should have its message file retained for
     * posterity in the 'msgs-recv' directory. The ISM is assumed to be valid
     * and have already been processed successfully (accepted), but it need not
     * have *just* been processed -- it might have been processed long ago and
     * only recently re-received.
     * 
     * @return true if the message file should be retained, or false otherwise.
     * @param ism the inter-site message to consider.
     */
    private boolean shouldRetainMessageFile(InterSiteMessage ism) {
        return ism.deliverToFile || this.alwaysDumpMessages
                || topologyAgent.shouldRetainIsm(ism);
    }

    /**
     * A helper function utilized during an ISM exchange that decodes an XML
     * representation of an incoming {@code InterSiteMessage} and returns an 
     * ISM object that contains corresponding information. As part of the
     * decoding process, the remote site id is verified against local state, 
     * the digital signature on the ISM is verified to assure authenticity and
     * integrity, and the XML is checked for parse errors. If the message could
     * not be decoded for any reason, this function writes an appropriate log
     * message and increments appropriate statistics counters.
     * 
     * @return the decoded {@code InterSiteMessage} object, or null if the
     *         message could not be decoded for some reason.
     * @param ismAsXml an XML representation of the message to be decoded, as
     *        received from the remote site.
     * @param stats a reference to the {@code IsmExchangeStats} object that is
     *        tracking statistics for the present ISM exchange. This function
     *        increments counters within {@code stats} in the event that the
     *        ISM could not be decoded. The counters are not modified if the
     *        function returns successfully (i.e. returns something other than
     *        null).
     * @throws OperationFailedException on low-level error.
     */
    private InterSiteMessage decodeIsmFromString(String ismAsXml,
            IsmExchangeStats stats) throws OperationFailedException {
        int sourceSiteId = SiteInfo.INVALID_SITE_ID;
        
        try {
            sourceSiteId = InterSiteMessage.extractSourceSiteId(ismAsXml);
            SiteInfo sourceSite = this.siteManager.getSiteInfo(sourceSiteId);

            // Verify the signature and parse the message.
            // TODO: make the choice of algorithm configurable.
            Signature sig = Signature.getInstance("SHA1withDSA");
            sig.initVerify(sourceSite.publicKey);
            return InterSiteMessage.fromXmlCheckSignature(ismAsXml, sig);
        } catch (ResourceNotFoundException ex) {
            /*
             * The sourceSiteId on the message is not a recognized site.
	     * There's no way for us to verify the signature so we need to drop
	     * this message.
             */
            stats.unknownSender++;
            this.siteManager.recordLogRecord(
                    LogRecordGenerator.ismReceiveException(sourceSiteId, ex));
        } catch (NoSuchAlgorithmException ex) {
            // Can't happen because SHA1withDSA is guaranteed to be supported.
            stats.miscError++;
            this.siteManager.recordLogRecord(
                    LogRecordGenerator.ismReceiveException(sourceSiteId, ex));
        } catch (SignatureException ex) {
            // The message has been tampered with or is not authentic --
            // the digital signature does not match the message content.
            stats.badSignature++;
            this.siteManager.recordLogRecord(
                    LogRecordGenerator.ismReceiveException(sourceSiteId, ex));
        } catch (SAXException ex) {
            // The message could not be parsed for some reason, maybe it
            // didn't look like an ISM.
            stats.parseError++;
            this.siteManager.recordLogRecord(
                    LogRecordGenerator.ismReceiveException(sourceSiteId, ex));
        } catch (InvalidKeyException ex) {
            stats.miscError++;
            this.siteManager.recordLogRecord(
                    LogRecordGenerator.ismReceiveException(sourceSiteId, ex));
        }

        // If control reaches this point then we were unable to decode the ISM.
        return null;
    }

    /**
     * A helper function utilized during an ISM exchange that examines a
     * specified {@code InterSiteMessage} object that has been received from a
     * remote site, runs some integrity checks, and decides whether the local
     * site should invest resources in processing the ISM or not. Such a check
     * is pertinent only for ISM's whose {@code linkLocal} field is false.
     * Link-local ISM's are not subject to such intense scrutiny because they
     * do not enter core modules' message queues.
     * 
     * @return true if the {@code ism} is suitable for the local site to
     *         process, or false otherwise.
     * @param ism the {@code InterSiteMessage} to be examined.
     * @param stats a reference to the {@code IsmExchangeStats} object that is
     *        tracking statistics for the present ISM exchange. This function
     *        increments counters within {@code stats} in the event that this
     *        function returns false. The counters are not modified if the
     *        function returns true.  This argument may be null, in which case
     *        no statistics counters are updated.
     * @throws OperationFailedException if the operation could not be compelted
     *         because of a low-level error.
     * @throws ResourceNotFoundException if the {@code ism.sourceSiteId} field
     *         contains a number that is not recognized as a valid site id.
     */
    private boolean verifyIsmSuitability(InterSiteMessage ism,
            IsmExchangeStats stats) throws OperationFailedException {
        SiteInfo site = siteManager.getSiteInfo(ism.sourceSiteId);

        // Drop this message if it has already been processed by
        // the local site.
        if ((ism.isPublic() && ism.sourceSeqNum <= site.publicSeqNum)
	        || (ism.isPrivate() 
                    && ism.sourceSeqNum <= site.privateSeqNum)) {
	    if (stats != null) {
		stats.tooOld ++;
	    }
            return false;
        }

        // Drop this message if it originated from the local site
        // and got reflected back to us somehow.
        if (ism.isFrom(this.siteManager.localSiteId)) {
            stats.localOrigin ++;
            return false;
        }

        // Drop this message if it arrived here but was not
        // addressed to us.
        if (ism.isPrivate() && !ism.isTo(this.siteManager.localSiteId)) {
	    if (stats != null) {
		stats.notAddressedToLocal ++;
	    }
            return false;
        }

	// Drop this message if it purportedly was sent by an inactive site, or
	// by an almost-inactive site with a sequence number that was too high.
	if (!site.isActive
	        || (site.finalSeqNum != InterSiteMessage.INVALID_SEQ_NUM 
                    && ism.sourceSeqNum > site.finalSeqNum)) {
	    if (stats != null) {
		stats.fromDeactivatedSite ++;
	    }
	    return false;
	}

        // The ISM meets our integrity requirements.
        return true;
    }

    /**
     * A helper method utilized during an ISM exchange that performs all
     * processing related to a specified link-local ISM. (Unlike regular ISM's,
     * link-local ISM's are processed synchronously during an ISM exchange.)
     * This method may generate response ISM's or replay ISM's and append these
     * to the caller-supplied {@code Collection}; the caller then would be
     * expected to convey them to the remote site as part of the exchange. If
     * the ISM could not be processed for any reason, this message writes an
     * appropriate log message. Statistics counters are incremented in every
     * case.
     * 
     * @param ism the {@code InterSiteMessage} to be processed.
     * @param replies a caller-supplied {@code Collection} that is able to
     *        store {@code String} objects. This method may append zero or more
     *        items to this collection as a way of conveying reply ISM's back
     *        to the caller. Each returned item is a complete XML document and
     *        may contain a document header. The caller is expected to transmit
     *        any reply ISM's back to the remote site. If the caller has no
     *        convenient facility for sending response ISM's to the remote
     *        site, this argument may be set to null. In this case this
     *        function would not generate any response ISM's and might decline
     *        to process certain kinds of ISM's.
     * @param stats a reference to the {@code IsmExchangeStats} object that is
     *        tracking statistics for the present ISM exchange. This function
     *        increments appropriate counters within {@code stats} both on
     *        successful processing and on failure.
     * @throws MessageDecodingException if processing of {@code ism} required
     *         the local site to read other previous ISM's from the filesystem,
     *         but one of those previous ISM's could not be parsed.
     * @throws OperationFailedException if processing of {@code ism} required
     *         the local site to read other previous ISM's from the filesystem,
     *         but a low-level error was encountered while doing so.
     * @throws ResourceException if processing of {@code ism} required the
     *         local site to read other previous ISM's from the filesystem, but
     *         a filesystem error was encountered. In this case the exception
     *         object's {@code identifier} field contains a value of type
     *         {@code File} that identifies the problematic file.
     */
    private void processLinkLocalIsm(InterSiteMessage ism,
            Collection<String> replies, IsmExchangeStats stats)
            throws MessageDecodingException, OperationFailedException,
            ResourceException {
        assert ism.linkLocal;
        if ((ism instanceof ReplayRequestISM) && (replies != null)) {
            ReplayRequestISM msg = (ReplayRequestISM) ism;
            int repliesOldSize = replies.size();
            long maxIsmsToReplay = Math.min(this.maxReplayedIsmsPerExchange,
                    msg.maxIsmsToReplay)
                    - stats.messagesReplayed;

            // Find matching ISM's to replay and add them to our reply.
            long countMatchingIsms
                    = messageFileAgent.readMessagesSuitableForRemoteSite(
                            msg.sourceSiteId, msg.requestedSiteId,
                            msg.excludePublicSeqNumsUpTo,
                            msg.excludePrivateSeqNumsUpTo, maxIsmsToReplay,
                            replies);
            long countReplayedIsms = replies.size() - repliesOldSize;

            // Formulate a response that summarizes all the ISM's that we are
            // replaying.
            ReplayResponseISM response = new ReplayResponseISM(
                    this.siteManager.localSiteId, msg.sourceSiteId,
                    msg.requestedSiteId, msg.excludePublicSeqNumsUpTo,
                    msg.excludePrivateSeqNumsUpTo, countMatchingIsms,
                    countReplayedIsms);
            
            replies.add(
                    this.siteManager.signLinkLocalInterSiteMessage(response));
            stats.messagesReplayed += countReplayedIsms;
            stats.linkLocalAccepted++;
            stats.linkLocalResponses++;
        } else if (ism instanceof ReplayResponseISM) {
            ReplayResponseISM msg = (ReplayResponseISM) ism;
            
            stats.sumMatchingIsms += msg.countMatchingIsms;
            stats.linkLocalAccepted++;
            this.remoteSites.get(msg.sourceSiteId).setAvailableMessagesHint(
                    msg.requestedSiteId,
                    msg.countMatchingIsms - msg.countReplayedIsms);
        } else {
            // Unrecognized message type.
            this.siteManager.recordLogRecord(LogRecordGenerator.cmUnknownType(
                    this, ism));
            stats.unrecognizedType++;
        }
    }

    /**
     * Internal class that stores state information about a single remote site.
     * This includes a queue of pending messages and status flags to indicate
     * whether the queue may be serviced at the present time.
     */
    private static class RemoteSiteState {
        /** Identifies the site whose messages this object tracks. */
        private int siteId;

        /**
         * The sequence number of the last public ISM from the site described
	 * by this object that the local site has processed. This value is
	 * copied from the corresponding {@code SiteInfo} at construction time
	 * and is maintained thereafter.
         */
        private long highestProcessedPublicSeqNum;

        /**
         * The sequence number of the last private ISM from the site described
         * by this object that the local site has processed. This value is
         * copied from the corresponding {@code SiteInfo} at construction time
         * and is maintained thereafter.
         */
        private long highestProcessedPrivateSeqNum;

        /**
         * The queue of ISM's originated by the remote site that are waiting to
         * be processed by the local site. The queue is prioritized according
	 * to the natural ordering of {@code InterSiteMessage} objects; thus,
	 * ISM's with low sequence numbers are at the head and ISM's with
	 * higher sequence numbers are at the tail.
         */
        private PriorityQueue<InterSiteMessage> pendingMessages;

        /**
         * A flag that is true when processing of the message at the head of
         * {@code pendingMessages} has previously been attempted and failed for
         * some reason. This is an efficiency feature; it prevents the local
         * site from attempting to re-process the message incessantly. We
	 * assume that an ISM that has failed processing once will continue to
	 * fail processing until some other system state has changed.
         */
        private boolean hasHeadMessageFailedProcessing;

        /**
         * A reference to the {@code InterSiteMessage} most recently returned
	 * by {@code getNextMessage()}, if the message presently is being
         * processed by the local site, or null if the message is no longer
         * being processed by the local site.
         */
        private InterSiteMessage messageBeingProcessed;

        /**
         * A map from site id's to available message counts that tracks the
         * message count hints received from remote sites during
         * locally-initiated replay requests. Each entry in the map describes
         * ISM's that were originated by the site identified by the map entry's
         * key and that are known to the remote site described by this
         * {@code RemoteSiteState} object. The value of each map entry is a
         * count of such ISM's, as hinted by the remote site. For sites where
	 * no corresponding map entry is present, a count of 0 is assumed.
         */
        private Map<Integer, Long> availableMessagesHints;

        /** Constructor. */
        public RemoteSiteState(SiteInfo site) {
            this.siteId = site.id;
            this.highestProcessedPublicSeqNum = site.publicSeqNum;
            this.highestProcessedPrivateSeqNum = site.privateSeqNum;
            this.pendingMessages = new PriorityQueue<InterSiteMessage>();
            this.hasHeadMessageFailedProcessing = false;
            this.messageBeingProcessed = null;
            this.availableMessagesHints = new HashMap<Integer, Long>();
        }

        /** Simple getter */
        public int getSiteId() {
            return this.siteId;
        }

        /**
         * Adds an ISM originated by {@code siteId} to the queue. If
	 * {@code ism} is a duplicate of a message already in the queue, or is
	 * a duplicate of the message presently being processed, it is dropped.
         * 
         * @return true if the message was added to the queue, or false
         *         otherwise.
         */
        public boolean queueMessage(InterSiteMessage ism) {
            assert ism.sourceSiteId == this.siteId;
            if (((this.messageBeingProcessed != null)
                    && this.messageBeingProcessed.equals(ism))
                    || this.pendingMessages.contains(ism)) {
                return false;
            }
            this.pendingMessages.add(ism);
            if (this.pendingMessages.peek() == ism) {
                // The message we just enqueued landed at the head of the
                // queue. Reset the failed-processing flag.
                this.hasHeadMessageFailedProcessing = false;
            }
            return true;
        }

        /**
         * Returns the next ISM in the queue to the caller for processing. The
         * caller is expected to invoke {@code notifyIsmProcessed()} on this
         * object once processing is complete, or if processing fails.
         * 
         * @return the next ISM in the queue, or null if there are no messages
         *         eligible for processing.
         */
        public InterSiteMessage getNextMessageForProcessing() {
            assert this.messageBeingProcessed == null;
            InterSiteMessage ism = peekAtNextMessageForProcessing();
            this.pendingMessages.remove(ism);
            this.messageBeingProcessed = ism;
            return ism;
        }

        /**
         * Returns true exactly when {@code getNextMessageForProcessing()}
	 * would return an ISM, and false exactly when
         * {@code getNextMessageForProcessing()} would return null. This
         * function is convenient in that is does not alter the contents of the
         * queue.
         */
        public boolean isMessageAvailableForProcessing() {
            return peekAtNextMessageForProcessing() != null;
        }

        /**
         * Returns the number of ISM's presently sitting in the queue. There is
         * no guarantee that any of the messages counted by this function are
         * eligible for processing at the moment.
         */
        public int getQueueSize() {
            return this.pendingMessages.size();
        }

        /**
         * Updates state to indicate that an ISM returned by
         * {@code getNextMessageForProcessing()} previously has finished
         * processing or failed processing.
         */
        public void notifyIsmProcessed(InterSiteMessage ism,
                boolean successful) {
            assert this.messageBeingProcessed == ism;
            assert this.siteId == ism.sourceSiteId;
            this.messageBeingProcessed = null;
            if (successful && ism.isPublic()) {
                // The failed-processing flag must have been clear or ism would
                // not have been dispatched in the first place. No need to
                // clear the flag again.
                assert this.hasHeadMessageFailedProcessing == false;

                this.highestProcessedPublicSeqNum = ism.sourceSeqNum;
            } else if (successful && ism.isPrivate()) {
                // The failed-processing flag must have been clear or ism would
                // not have been dispatched in the first place. No need to
                // clear the flag again.
                assert this.hasHeadMessageFailedProcessing == false;

                this.highestProcessedPrivateSeqNum = ism.sourceSeqNum;
            } else {
                queueMessage(ism);
                assert ism.equals(this.pendingMessages.peek());
                // The ISM should have gone straight to the head of the queue
                // since we plucked it from the head a short while ago.
                this.hasHeadMessageFailedProcessing = true;
            }
        }

        /**
         * Updates state to indicate that an ISM originated by a site other
	 * than the one decribed by this object has been processed.
	 *
	 * In the current implementation, this has the effect of clearing the
         * {@code hasHeadMessageFailedProcessing} flag because it is 
	 * conceivable that the newly-processed message caused some local
	 * sample state (for instance) to change in such a way that our queue's
	 * head ISM could be processed succesfully now.
	 *
	 * For efficiency's sake, the current implementation contains special
	 * handling for head ISM's of the class JoinISM.  JoinISM's cannot be
	 * processed by SiteManager until some other site's ISM stream reaches
	 * a particular point.  The special handling logic here mirrors that in
	 * SiteManager.eventJoin().  It is quicker to evaluate the join
	 * condition here than to repeatedly dispatch the JoinISM to
	 * SiteManager for evaluation.
         */
        public void notifyOtherSitesIsmProcessed(
                InterSiteMessage otherSitesIsm) {
            assert this.siteId != otherSitesIsm.sourceSiteId;

            InterSiteMessage ourNextIsm = this.pendingMessages.peek();
	    if (ourNextIsm != null && (ourNextIsm instanceof JoinISM)) {
		// Special handling for JoinISM's.
		JoinISM j = (JoinISM) ourNextIsm;
		if (otherSitesIsm.sourceSiteId == j.joinedSiteId
		        && otherSitesIsm.sourceSeqNum >= j.joinedSiteSeqNum
		        && otherSitesIsm.isPublic()
                        && this.hasHeadMessageFailedProcessing) {
		    // The join is likely to be satisfied now that 
		    // otherSitesIsm has been processed.  Mark our JoinISM
		    // eligible for dispatch.
		    this.hasHeadMessageFailedProcessing = false;
		} else {
		    // Do nothing; leave the failed-processing flag intact.
		    // The otherSitesIsm that was just processed was not
		    // sufficient to satisfy the join.  It would be wasteful to
		    // allow our JoinISM to be dispatched just yet.
		}
	    } else {
		// Default case for ISM's other than JoinISM's.
		this.hasHeadMessageFailedProcessing = false;
	    }
        }

        /**
         * Updates the state table with new, known-valid sequence numbers for a
         * specified site.
         * 
         * @param site this object's {@code id} field identifies the site whose
         *        record should be updated, its {@code publicSeqNum} field is
         *        taken as the site's new highest public sequence number
         *        processed, and its {@code privateSeqNum} field is taken as
	 *        the site's new highest private sequence number procesed.
         * @throws IllegalArgumentException if {@code site.id} is not 
	 *        recognized as a valid site identifier.
         */
        public void resetSequenceNumbers(SiteInfo site) {
            if (site.id != this.siteId) {
                throw new IllegalArgumentException();
            }
            this.highestProcessedPublicSeqNum = site.publicSeqNum;
            this.highestProcessedPrivateSeqNum = site.privateSeqNum;
        }

        /**
         * @return the highest seqnum of all public ISM's presently sitting in
         *         this object's ISM queue that are eligible for processing, or
         *         the highest sequence number of any public ISM already
         *         processed, whichever is highest.
         */
        public long getSeqnumForLastQueuedPublicIsm() {
            long lastGoodSeqnum = this.highestProcessedPublicSeqNum;

	    // Consider the message presently being processed, if any.
	    if (this.messageBeingProcessed != null
		    && this.messageBeingProcessed.isPublic()
		    && this.messageBeingProcessed.sourcePrevSeqNum 
                    == lastGoodSeqnum) {
		lastGoodSeqnum = this.messageBeingProcessed.sourceSeqNum;
	    }

	    // Consider all the messages sitting in the pending queue, in order
	    // by seqnum.
            ArrayList<InterSiteMessage> isms = new ArrayList<InterSiteMessage>(
                    this.pendingMessages);
            Collections.sort(isms);
            for (InterSiteMessage ism : isms) {
                if (ism.isPublic() && ism.sourcePrevSeqNum == lastGoodSeqnum) {
                    lastGoodSeqnum = ism.sourceSeqNum;
                }
            }
            return lastGoodSeqnum;
        }

        /**
         * @return the highest seqnum of all private ISM's presently sitting in
         *         this object's ISM queue that are eligible for processing, or
         *         the highest sequence number of any private ISM already
         *         processed, whichever is highest.
         */
        public long getSeqnumForLastQueuedPrivateIsm() {
            long lastGoodSeqnum = this.highestProcessedPrivateSeqNum;

	    // Consider the message presently being processed, if any.
	    if (this.messageBeingProcessed != null
		    && this.messageBeingProcessed.isPrivate()
		    && this.messageBeingProcessed.sourcePrevSeqNum
                    == lastGoodSeqnum) {
		lastGoodSeqnum = this.messageBeingProcessed.sourceSeqNum;
	    }

	    // Consider all the messages sitting in the pending queue, in order
	    // by seqnum.
            ArrayList<InterSiteMessage> isms = new ArrayList<InterSiteMessage>(
                    this.pendingMessages);
            Collections.sort(isms);
            for (InterSiteMessage ism : isms) {
                if (ism.isPrivate() 
                        && ism.sourcePrevSeqNum == lastGoodSeqnum) {
                    lastGoodSeqnum = ism.sourceSeqNum;
                }
            }
            
            return lastGoodSeqnum;
        }

        /**
         * Sets an available message count hint for the specified originating
         * site.
         * 
         * @param siteId identifies the site that originated the ISM's that are
         *        counted.
         * @param count an approximation of the number of ISM's available for
         *        replay.
         */
        public void setAvailableMessagesHint(int siteId, long count) {
            if (count == 0) {
                this.availableMessagesHints.remove(siteId);
            } else {
                this.availableMessagesHints.put(siteId, count);
            }
        }

        /**
         * @return a sum of message count hints across all originating sites.
         */
        public long getCountAvailableMessageHints() {
            long total = 0;
            for (Long hint : this.availableMessagesHints.values()) {
                total += hint;
            }
            return total;
        }

	/**
	 * Removes from the pending messages queue any ISM's whose sequence
	 * number is greater than threshold.
	 */
	public void purgePendingMessagesBySeqNum(long threshold) {
	    Iterator<InterSiteMessage> it = this.pendingMessages.iterator();
	    while (it.hasNext()) {
		InterSiteMessage ism = it.next();
		if (ism.sourceSeqNum > threshold) {
		    if (ism == this.pendingMessages.peek()) {
			// Special handling if we're at the head of the queue.
			this.hasHeadMessageFailedProcessing = false;
		    }

		    // Remove this ISM from the queue.
		    it.remove();
		}
	    }
	}

        /**
         * Helper function that returns the next ISM in the queue, if it is
         * eligible for processing at the present moment, or null otherwise.
         */
        private InterSiteMessage peekAtNextMessageForProcessing() {
            InterSiteMessage ism = this.pendingMessages.peek();
            if ((ism == null)
                    || this.hasHeadMessageFailedProcessing
                    || (ism.isPublic() && (ism.sourcePrevSeqNum
                            != this.highestProcessedPublicSeqNum))
                    || (ism.isPrivate() && (ism.sourcePrevSeqNum
                            != this.highestProcessedPrivateSeqNum))) {
                return null;
            }
            return ism;
        }
    }

    /**
     * Internal class that can sort RemoteSiteState objects into order from
     * most desirable to service a message from to least, based upon the site
     * id from which the most recent message was processed. This is a
     * round-robin implementation among all known sites.
     */
    private static class RemoteSiteComparator implements
            Comparator<RemoteSiteState> {
        private int lastSite;

        public RemoteSiteComparator(int originatingSiteForLastMessage) {
            this.lastSite = originatingSiteForLastMessage;
        }

        public int compare(RemoteSiteState x, RemoteSiteState y) {
            int xScore = x.getSiteId() > this.lastSite ? x.getSiteId()
                    - this.lastSite : x.getSiteId() + 65536;
            int yScore = y.getSiteId() > this.lastSite ? y.getSiteId()
                    - this.lastSite : y.getSiteId() + 65536;
            return xScore - yScore;
        }
    }

    /**
     * Internal class that tracks ISM statistics information associated with a
     * single ISM exchange with a remote site. It is used by
     * {@code exchangeInterSiteMessages()} and its associated helper functions.
     */
    private static class IsmExchangeStats {
        /** Identifies the remote site that initiated the exchange. */
        String remoteSite = null;

        /**
         * The number of messages received from the remote site, including all
         * kinds of ISM's and even ISM's that were unparsable, etc.
         */
        public int received = 0;

        /**
         * The number of {@code received} messages that purportedly were
         * originated by a site whose id number was not recognized.
         */
        public int unknownSender = 0;

        /**
         * The number of {@code received} messages whose digital signatures
         * could not be verified or might have been forged.
         */
        public int badSignature = 0;

        /**
         * The number of {@code received} messages whose XML could not be
         * parsed.
         */
        public int parseError = 0;

        /**
         * The number of {@code received} messages that were dropped because
         * they had been processed by the local site previously, but that were
         * otherwise valid.
         */
        public int tooOld = 0;

        /**
         * The number of {@code received} messages that were dropped because
         * they were originated by the local site, but that were otherwise
         * valid.
         */
        public int localOrigin = 0;

        /**
         * The number of {@code received} messages that were dropped because
         * they were not addressed to the local site, but that were otherwise
         * valid.
         */
        public int notAddressedToLocal = 0;

	/**
	 * The number of {@code received} messages that were dropped because
	 * they originated from a deactivated site or from an
	 * almost-deactivated site with a sequence number higher than expected.
	 */
	public int fromDeactivatedSite = 0;

        /**
         * The number of {@code received} messages that were dropped for some
         * obscure reason that does not have a more specific counter.
         */
        public int miscError = 0;

        /**
         * The number of {@code received} messages that survived initial checks
         * and that were accepted and enqueued for later (asynchronous)
         * processing by the appropriate core module.
         */
        public int acceptedAndQueued = 0;

        /**
         * The number of {@code received} messages that were diverted for
         * link-local processing. These messages are excluded from the
         * {@code acceptedAndQueued} counter.
         */
        public int linkLocal = 0;

        /**
         * The number of {@code linkLocal} messages that were not acted upon
         * because their intended purpose was not recognized.
         */
        public int unrecognizedType = 0;

        /**
         * The number of {@code linkLocal} messages that were accepted and
         * processed.
         */
        public int linkLocalAccepted = 0;

        /**
         * The number of link-local response messages transmitted to the remote
         * site as part of the exchange.
         */
        public int linkLocalResponses = 0;

        /**
         * The number of regular (non-link-local) ISM's that were replayed to
         * the remote site as part of the exchange.
         */
        public int messagesReplayed = 0;

        /**
         * The number of replayable ISM's that matched criteria specified by
	 * the local site as part of a replay request that it transmitted to a
         * remote site as part of an ISM exchange initiated by the local site.
         * This value would only be incremented during processing of the
         * responses to an ISM exchange initiated by the local site.
         */
        public long sumMatchingIsms = 0;

        /** Constructor. Sets two member variables. */
        public IsmExchangeStats(String remoteSite, int received) {
            this.remoteSite = remoteSite;
            this.received = received;
        }

        /**
         * A function that returns a {@code LogRecord} that summarizes the
         * statistics contained within this object. This is no more than a
         * convenience function because the current implementation simply
         * delegates to {@code LogRecordGenerator}.
         */
        public LogRecord getSummaryForLogging() {
            return LogRecordGenerator.ismExchangeSummary(this.remoteSite,
                    this.received, this.unknownSender, this.badSignature,
                    this.parseError, this.tooOld, this.localOrigin,
		    this.notAddressedToLocal, this.fromDeactivatedSite, 
		    this.miscError, this.acceptedAndQueued, this.linkLocal,
                    this.unrecognizedType, this.linkLocalAccepted,
                    this.linkLocalResponses, this.messagesReplayed);
        }
    }
}
