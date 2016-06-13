/*
 * Reciprocal Net project
 * 
 * SiteNetworkSynchronizer.java
 *
 * 06-Feb-2006: ekoperda wrote first draft
 * 11-Apr-2006: jobollin removed inaccessible catch blocks
 * 30-May-2006: jobollin reformatted the source
 * 10-Nov-2006: jobollin refactored to remove the networkState instance
 *              variable and to make the nested NetworkState class a bit more
 *              coherent.
 * 29-Dec-2007: ekoperda fixed bug #1843 in NetworkState.isDifferentFrom(),
 *              also improved convergence checking
 * 04-Jul-2008: ekoperda added logic to faciliate monitoring of sample id
 *              blocks and acquisition of them as neeeded
 * 28-Nov-2008: ekoperda added logic for deactivated sites
 * 31-Dec-2008: ekoperda improved handling of deactivated sites
 */

package org.recipnet.site.core.util;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.SignatureException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.recipnet.site.OperationFailedException;
import org.recipnet.site.UnexpectedExceptionException;
import org.recipnet.site.core.DeadlockDetectedException;
import org.recipnet.site.core.SampleManager;
import org.recipnet.site.core.SiteManager;
import org.recipnet.site.core.agent.MessageFileAgent;
import org.recipnet.site.core.agent.ReceivedMessageAgent;
import org.recipnet.site.core.msg.InterSiteMessage;
import org.recipnet.site.shared.SoapUtil;
import org.recipnet.site.shared.db.SiteInfo;

/**
 * <p>
 * This class is designed to drive an inter-site synchronization process for a
 * new site or a site that is not presently operating for some reason. It is
 * designed to be run interactively, from a command line or similar, because it
 * displays textual progress messages. This class should be invoked only when
 * Site Manager is in bootstrap mode because it interfaces intimately with
 * {@code SiteManager}, {@code ReceivedMessageAgent}, and
 * {@code MessageFileAgent}. This class is not thread-safe, but thread safety
 * is not a concern in bootstrap mode.
 * </p><p>
 * During routine site operations, {@code SiteManager}'s other mechanisms keep
 * the sites in the Site Network synchronized. This class is designed to be
 * used on those occasions where an explicit synchronization is required,
 * diagnostic messages are desirable, and the rest of the site software is not
 * online.
 * </p>
 */
public class SiteNetworkSynchronizer {

    /**
     * The default number of ISM's requested to be replayed by a remote site to
     * the local site during a single ISM exchange.
     */
    private static final int REQUESTED_EXCHANGE_SIZE = 128;

    /**
     * The maximum number of ISMs a new site is presumed to generate during its
     * initial bootstrap. Sites that have generated more ISMs than this number
     * are presumed to have commenced operations proper, and not to have been
     * just recently bootstrapped.
     */
    private static final int NEW_SITE_SENT_MESSAGE_THRESHOLD = 64;

    /**
     * The number of milliseconds to pause between rounds of synchronization.
     * Pausing is useful because it allows other sites an opportunity to
     * (asynchronously) process and react to ISMs that the local site pushed to
     * it during the preceding round. Adequate pausing generally decreases the
     * number of rounds the network state requires to converge.
     */
    private static final int DELAY_BETWEEN_ROUNDS = 10000;

    /**
     * A reference to the {@code SiteManager}, as supplied at construction
     * time.
     */
    private final SiteManager siteManager;

    /**
     * A reference to the {@code SampleManager}, as supplied at construction
     * time.
     */
    private final SampleManager sampleManager;

    /**
     * A reference to the {@code ReceivedMessageAgent}, as supplied at
     * construction time.
     */
    private final ReceivedMessageAgent receivedMessageAgent;

    /**
     * A reference to the {@code MessageFileAgent}, as supplied at construction
     * time.
     */
    private final MessageFileAgent messageFileAgent;

    /**
     * A reference to the {@code IsmExchanger}, as supplied at construction
     * time.
     */
    private final IsmExchanger ismExchanger;
    
    /**
     * A reference to some output where progress messages are to be written, as
     * supplied at construction time.
     */
    private final PrintStream display;

    /**
     * A formatter used when formatting messages for display. Set by the
     * construtor.
     */
    private final NumberFormat percentFormatter;

    /**
     * A formatter used when formatting messages for display. Set by the
     * constructor.
     */
    private final NumberFormat byteFormatter;

    /**
     * The id of the local site, as obtained from Site Manager at construction
     * time.
     */
    private final int localSiteId;

    /** Statistics counter: number of ISMs received. */
    private long countReceived;

    /** Statistics counter: size required to express ISMs received. */
    private long countReceivedBytes;

    /** Statistics counter: number of ISMs processed. */
    private long countProcessed;

    /** Statistics counter: number of ISMs generated by the local site. */
    private long countSent;

    /**
     * Statistics counter: size required to express the ISMs transmitted, where
     * a single ISM sent to multiple destinations is counted multiple times.
     */
    private long countSentBytes;

    /**
     * Initializes a new {@code SiteNetworkSynchronizer} with the specified
     * parameters
     *
     * @param siteManager the {@code SiteManager} for the site on behalf of
     *        which this synchronizer will operate
     * @param sampleManager the {@code SampleManager} for the site on behalf
     *        of which this synchronizer will operate.
     * @param receivedMessageAgent the {@code ReceivedMessageAgent} for the
     *        site
     * @param ismExchanger an {@code IsmExchanger} that will operate on behalf
     *        of the local site
     * @param messageFileAgent a {@code MessageFileAgent} that will operate on
     *        behalf of the local site
     * @param display a {@code PrintStream} to which progress messages should 
     *        be directed
     *        
     * @throws OperationFailedException if the provided {@code SiteManager} is
     *        unable to provide site information for the local site
     */
    public SiteNetworkSynchronizer(SiteManager siteManager,
	    SampleManager sampleManager, 
            ReceivedMessageAgent receivedMessageAgent,
            IsmExchanger ismExchanger, MessageFileAgent messageFileAgent, 
	    PrintStream display) 
            throws OperationFailedException {
        // Handle arguments
        this.siteManager = siteManager;
	this.sampleManager = sampleManager;
        this.receivedMessageAgent = receivedMessageAgent;
        this.messageFileAgent = messageFileAgent;
        this.ismExchanger = ismExchanger;
        this.display = display;

        // Prepare formatters
        percentFormatter = NumberFormat.getInstance();
        percentFormatter.setMaximumFractionDigits(1);
        percentFormatter.setMinimumFractionDigits(1);
        percentFormatter.setMinimumIntegerDigits(1);
        byteFormatter = NumberFormat.getInstance();
        byteFormatter.setGroupingUsed(true);
        byteFormatter.setMaximumFractionDigits(0);

        // Determine the local site ID
        localSiteId = this.siteManager.getLocalSiteInfo().id;
    }

    /**
     * Attempts to synchronize this site with the rest of the site network by
     * exchanging and processing intersite messages. When it returns, either
     * synchronization is complete or it has terminated prematurely due to a
     * complete stall of the ISM queues. Progress messages are periodically
     * written to the configured {@code display}. Users should note that this
     * method is both synchronized and potentially long-running. This will not
     * be an issue, however, unless an instance is shared among multiple
     * threads, which is not recommended.
     * 
     * @throws DeadlockDetectedException on core deadlock.
     * @throws OperationFailedException if {@code SiteManager},
     *         {@code ReceivedMessageAgent}, or {@code MessageFileAgent}
     *         reports a low-level error.
     */
    public synchronized void synchronize() throws DeadlockDetectedException,
                OperationFailedException {
        NetworkState networkState = new NetworkState();
        NetworkState networkStateBeforeRound;
        int roundNumber = 0;
        
        // Prepare the network state
        networkState.updateFromSiteManager();
        
        // Initialize counters
        countReceived = 0;
        countReceivedBytes = 0;
        countProcessed = 0;
        countSent = 0;
        countSentBytes = 0;

	/*
	 * Examine the local site's sample id block status, and possibly take
	 * action to obtain some more.
	 */
	display.println("  Examining sample ID block status... ");
	sampleManager.runPeriodicSampleIdBlockCheck();
        
        /*
         * Possibly push our old sent-messages to all the other sites in the
         * Site Network. This would be appropriate only if we are a brand-new
         * site that just finished bootstrapping.
         */
        if (shouldPushOldSentMessages()) {
            display.println("  Broadcasting this site's initial"
                    + " messages... ");
            pushSentMessages(InterSiteMessage.INVALID_SEQ_NUM, networkState);
        }

        /*
         * Run one or more rounds of synchronization, until the network state
         * stabilizes.
         */
        do {
            roundNumber++;

            display.println("  Synchronizing with the Reciprocal Net Site "
                    + "Network, round " + roundNumber + ":");

            // Initialize state for the round.
            networkStateBeforeRound = networkState.clone();
            long localSeqNumBeforeRound
                    = networkStateBeforeRound.getMaxSeqNumForSite(localSiteId);

            /*
             * Interact with each accessible site of the Site Network in turn.
             * We (implicitly) iterate in order of site id, on the assumption
             * that lower-numbered sites are more likely to have the ISM's we
             * need than higher-numbered ones.
             */
            for (SiteInfo site 
                    : networkState.getSites(true, true, true, true)) {
                interactWithRemoteSite(site, networkState);
            }

            // Push any newly-generated messages out to the Site Network.
            boolean messagesWereSent
                    = pushSentMessages(localSeqNumBeforeRound, networkState);

            // Update the network state to account for changes this round
            networkState.updateFromSiteManager();
            countSent += (networkState.getMaxSeqNumForSite(localSiteId)
                    - localSeqNumBeforeRound);
            
            // Report round statistics
            display.println("    Round " + roundNumber + " is complete.");
            display.println("      Processed "
                    + byteFormatter.format(countProcessed)
                    + " messages so far.");
            display.println("      Received "
                    + byteFormatter.format(countReceived)
                    + " messages, "
                    + calculateKilobytes(countReceivedBytes) + " so far.");
            display.println("      Transmitted "
                    + byteFormatter.format(countSent) + " messages, "
                    + calculateKilobytes(countSentBytes) + " so far.");
            
            /*
             * Possibly pause before the next round. This gives other sites an
             * opportunity to process the ISM's we just pushed to them.
             */
            if (messagesWereSent) {
                display.print("    Pausing before the next round... ");
                try {
                    Thread.sleep(DELAY_BETWEEN_ROUNDS);
                    display.println("ok");
                } catch (InterruptedException ex) {
                    // just drop through...
                    display.println("interrupted");
                }
            }
        } while (networkState.isDifferentFrom(networkStateBeforeRound));

	/*
	 * Check to see whether any ISM's remain in the queues and inform the
	 * user.
	 */
        long countMessagesInQueues
                = receivedMessageAgent.countMessagesInQueues();
        if (countMessagesInQueues > 0) {
            display.println("  Synchronization was ABORTED because "
                    + countMessagesInQueues + " messages cannot be processed"
                    + " at this time.");
            display.println("  This server is NOT synchronized with the"
                    + " Site Network.");
            display.println("  Please try running this utility again, or"
                    + " contact Reciprocal Net technical support if this"
                    + " condition persists.");
        } else {
            display.println("  This server is synchronized with the Site"
                    + " Network.");
        }

	/*
	 * Check to see whether the local site holds any sample id blocks and
	 * inform the user.
	 */
	if (this.sampleManager.countUnusedSampleIds() == 0) {
	    display.println("  *** WARNING: this site has no available sample"
	            + " id blocks.  The recipnetd daemon might be unable to"
                    + " start up.  Consider synchronizing again.");
	}
    }

    /**
     * During a round of synchronization, this helper method performs all the
     * synchronization activities specific to a single remote site. It pulls
     * messages from the remote site and causes them to be processed by the
     * local site. To the extent possible, all ISMs available from that site
     * are downloaded and processed. The algorithm is designed to avoid having
     * too many ISM's that were downloaded but not yet processed sitting in
     * {@code ReceivedMessageAgent}'s queues, however, in order to avoid
     * potential memory limitations. Thus, in the event all of
     * {@code ReceivedMessageAgent}'s queues have stalled, this method will
     * return prematurely. Callers should monitor network state and invoke this
     * function repeatedly for all known sites in a round-robin fashion until
     * network state stabilizes. If any offline sites are encountered, they are
     * detected and reported gracefully. Informative progress messages are
     * written to the {@code display} as this method executes.
     * 
     * @param site identifies the site to be interacted with.
     * @throws OperationFailedException if {@code SiteManager} or
     *         {@code ReceivedMessageAgent} reported low-level errors.
     */
    private void interactWithRemoteSite(SiteInfo site,
            NetworkState networkState) throws OperationFailedException {
        display.println("    Connecting to " + site.name + " (id "
                + site.id + "):");

        // Poll the remote site to detect how many messages it has for us.
        if (!downloadAndAcceptMessages(site, 0, false, networkState)) {
            return;
        }

        long countAvailableThisInteraction
                = receivedMessageAgent.getHintedAvailableMessageCount(site.id);
        
        display.println("      " + countAvailableThisInteraction
                + " messages are available for download.");

        // Download and process messages from the remote site in batches.
        long countProcessedThisInteraction = 0;
        boolean wereMessagesProcessedThisExchange = true;
        while ((countAvailableThisInteraction > 0)
                && wereMessagesProcessedThisExchange) {
            if (!downloadAndAcceptMessages(site, REQUESTED_EXCHANGE_SIZE, true,
                    networkState)) {
                return;
            }

            // Process as many pending messages as possible.
            wereMessagesProcessedThisExchange = false;
            String lastPercentDisplayed = null;
            while (receivedMessageAgent.dispatchNextMessageInBootstrapMode()) {
                countProcessed++;
                countProcessedThisInteraction++;
                wereMessagesProcessedThisExchange = true;

                // Display a fancy progrss message.
                String currentPercent = calculatePercent(
                        countProcessedThisInteraction,
                        countAvailableThisInteraction);
                if ((lastPercentDisplayed == null)
                        || !lastPercentDisplayed.equals(currentPercent)) {
                    display.println("      progress "
                            + currentPercent
                            + ", "
                            + byteFormatter.format(
                                    countProcessedThisInteraction)
                            + " of "
                            + byteFormatter.format(
                                    countAvailableThisInteraction)
                            + " messages for this connection.");
                    lastPercentDisplayed = currentPercent;
                }
            }
        }

        // Display a final status message for this interaction.
        long countMessagesInQueues
                = receivedMessageAgent.countMessagesInQueues();
        if (countMessagesInQueues > 0) {
            display.println("      " + countMessagesInQueues
                    + " messages cannot be processed at this time.");
        }
        display.println("      Closing connection.");
    }

    /**
     * Helper function that pulls ISM's from a specified target site, feeds
     * them to the local site's {@code ReceivedMessageAgent}, and optionally
     * displays progress messages. This method decides specifically which ISM's
     * the remote site is asked for. Only a single ISM exchange is attempted.
     * 
     * @return true if the pull succeeded, or false if the pull failed because
     *         the remote site was offline.
     * @param targetSite describes the site from which the ISM's are to be
     *        pulled. The {@code baseUrl} field of this argument must not be
     *        null.
     * @param maxLimit the maximum number of ISM's to download from the remote
     *        site during the exchange.
     * @param displayMessage should be true if progress messages are to be
     *        written to the {@code display} or false otherwise.
     * @throws OperationFailedException if {@code SiteManager} or
     *         {@code ReceivedMessageAgent} reported low-level errors.
     */
    private boolean downloadAndAcceptMessages(SiteInfo targetSite,
            int maxLimit, boolean displayMessage, NetworkState networkState)
            throws OperationFailedException {
        // Decide which sites' ISM's we wanted to request.
	Collection<Integer> siteIdsToRequest = new ArrayList<Integer>();
	for (SiteInfo site : networkState.getSites(true, false, false, true)) {
	    siteIdsToRequest.add(site.id);
	}
	siteIdsToRequest.removeAll(
                receivedMessageAgent.identifyStalledQueues());

        // Possibly display a message.
        if (displayMessage) {
            display.print("      Waiting for messages... ");
        }

        // Pull messages from the target site.
        String messageStrings[];
        try {
            messageStrings = siteManager.pullIsmsFromRemoteSite(
                    targetSite.id, siteIdsToRequest, maxLimit, true);
        } catch (OperationFailedException ex) {
            // The site is offline. Display an error message and notify the
            // caller.
            displaySiteOfflineMessage(ex);
            networkState.flagSiteAsOffline(targetSite.id);
            return false;
        } catch (InvalidKeyException ex) {
            // SiteManager had trouble signing our pull request.
            throw new OperationFailedException(ex);
        } catch (SignatureException ex) {
            // SiteManager had trouble signing our pull request.
            throw new OperationFailedException(ex);
        }
        
        receivedMessageAgent.exchangeInterSiteMessages(messageStrings,
                "site id " + targetSite.id + " (pull)", false, true);

        // Update the statistics counters and display a message.
        countReceived += messageStrings.length;
        for (String messageString : messageStrings) {
            try {
                countReceivedBytes
                        += messageString.getBytes("UTF-8").length;
            } catch (UnsupportedEncodingException ex) {
                // Can't happen because UTF-8 is required to be supported
                throw new UnexpectedExceptionException(ex);
            }
        }
        if (displayMessage) {
            display.println("received " + messageStrings.length + ".");
        }
	if (messageStrings.length == 0) {
	    display.println(
                    "      *** WARNING: this site seems to be ignoring us!");
	}

        return true;
    }

    /**
     * Helper function that consults Site Manager and decides whether old,
     * locally-generated ISM's should be pushed to other sites. "Old" ISM's are
     * defined to be those generated prior to the start of the present
     * synchronization process. Generally, the sending of old ISM's is
     * appropriate only during the first synchronization following a new site's
     * bootstrap operation. (In present implementations, a number of ISM's may
     * be generated but not transmitted during bootstrap.) This function tests
     * as precisely as possible whether this present synchronization is the
     * first one executed after bootstrap.
     * 
     * @throws OperationFailedException if Site Manager reports a low-level
     *         error.
     */
    private boolean shouldPushOldSentMessages()
            throws OperationFailedException {
        SiteInfo localSite = siteManager.getSiteInfo(localSiteId);
        SiteInfo coordinatorSite = siteManager.getSiteInfo(
                InterSiteMessage.RECIPROCAL_NET_COORDINATOR);

        return (coordinatorSite.publicSeqNum < coordinatorSite.privateSeqNum)
                && (localSite.publicSeqNum < NEW_SITE_SENT_MESSAGE_THRESHOLD);
    }

    /**
     * Helper function that pushes all new locally-generated ISM's to all the
     * other sites in the Site Network that are eligible to receive them. Most
     * of the pushing work is delegated to {@code pushSentMessagesToSite}.
     * 
     * @return true if at least one ISM was pushed to at least one site, or
     *         false if no ISM's were pushed.
     * @param excludeSequenceNumbersUpTo the threshold sequence number at which
     *        locally-generated ISM's are considered to be "new". Any ISM's
     *        with sequence numbers less than or equal to this value are not
     *        pushed.  The special value
     *        {@code InterSiteMessage.INVALID_SEQ_NUM} specifies that all
     *        locally-generated ISM's should be pushed.
     * @throws OperationFailedException if {@code MessageFileAgent} reported a
     *         low-level error.
     */
    private boolean pushSentMessages(long excludeSequenceNumbersUpTo,
            NetworkState networkState) throws OperationFailedException {
        boolean wereMessagesSent = false;

        for (SiteInfo site : networkState.getSites(true, true, true, true)) {
            Collection<String> ismsAsXml = new ArrayList<String>();

            messageFileAgent.readMessagesSuitableForRemoteSite(site.id,
                    localSiteId, excludeSequenceNumbersUpTo,
                    excludeSequenceNumbersUpTo, MessageFileAgent.NO_LIMIT,
                    ismsAsXml);
            if (!ismsAsXml.isEmpty()) {
                wereMessagesSent = 
                        pushSentMessagesToSite(site, ismsAsXml, networkState)
                        || wereMessagesSent;
            }
        }

        return wereMessagesSent;
    }

    /**
     * Helper function that pushes a batch of ISM's to a specified remote site
     * and displays appropriate progress messages. Any replies that might be
     * received from the remote site during the exchange are dropped.
     * 
     * @return true if the push was successful, or false if some sort of error
     *         was encountered, most likely because the remote site was
     *         offline.
     * @param site describes the site to which the ISM's are to be pushed. The
     *        {@code baseUrl} field of this argument must not be null.
     * @param ismsAsXml a collection of zero or more XML documents, where each
     *        represents an ISM that is to be transmitted.
     */
    private boolean pushSentMessagesToSite(SiteInfo site,
            Collection<String> ismsAsXml, NetworkState networkState) {
        // Transform the collection into an array of the proper sort.
        String ismsAsXmlArray[] = new String[ismsAsXml.size()];
        int i = 0;

        for (String ismAsXml : ismsAsXml) {
            String xmlDocFragment = SoapUtil.dropXmlDocumentHeader(ismAsXml,
                    "message");

            ismsAsXmlArray[i++] = xmlDocFragment;
            try {
                countSentBytes += xmlDocFragment.getBytes("UTF-8").length;
            } catch (UnsupportedEncodingException ex) {
                // Can't happen because UTF-8 is required to be supported.
                throw new UnexpectedExceptionException(ex);
            }
        }

        // Push the messages.
        display.print("    Sending " + ismsAsXmlArray.length
                + " messages to " + site.name + " (id " + site.id + ")... ");
        try {
            ismExchanger.exchange(ismsAsXmlArray, site.baseUrl, true);
            /*
             * (discard the replies because we don't know what purpose they
             * would serve anyway)
             */

            display.println("ok");
            return true;
        } catch (OperationFailedException ex) {
            /*
             * The site is offline. Display an error message and notify the
             * caller.
             */
            displaySiteOfflineMessage(ex);
            networkState.flagSiteAsOffline(site.id);
            return false;
        }
    }

    /**
     * Helper function that formats and writes to {@code display} an
     * appropriate error message when an ISM exchange with a remote site fails.
     * It is assumed that the caller received an
     * {@code OperationFailedException} from the {@code IsmExchanger}.
     */
    private void displaySiteOfflineMessage(OperationFailedException ex) {
        display.print("\n      site offline: ");
        if (ex.getCause() != null) {
            display.println(ex.getCause());
        } else if (ex.getMessage() != null) {
            display.println(ex.getMessage());
        } else {
            display.println(ex);
        }
    }

    /**
     * Utility function that calculates and returns a string showing {@code a}
     * as a percentage of {@code b}.
     */
    private String calculatePercent(long a, long b) {
        double x = ((double) a) * 100 / b;
        return percentFormatter.format(x) + "%";
    }

    /**
     * Utility function that returns a string containing a formatted
     * respresenting of {@code x}, with units of kilobytes.
     * 
     * @param x some number of bytes.
     */
    private String calculateKilobytes(long x) {
        return byteFormatter.format(x / 1024) + " KB";
    }

    /**
     * Class used for state-tracking that records the synchronization condition
     * of the Site Network at some point in time. Additionally, the class
     * tracks site-offline status on a dynamic basis. The various
     * {@code get...()} functions do not consult outside resources but merely
     * return information that was supplied to this class at construction time
     * or in subsequent method invocations.
     */
    private class NetworkState implements Cloneable {
        /**
         * A map from site ID to corresponding {@code SiteInfo}, sorted by
         * ascending site ID.
         */
        private SortedMap<Integer, SiteInfo> siteInfos;

        /**
         * A collection of {@code Integer}'s, where each identifies a site that
         * has been reported to be offline. This collection is populated by
         * {@code flagSiteAsOffline}.
         */
        private Set<Integer> siteIdsOffline;

	/**
	 * A collection of {@code Integer}'s, where each identifies a site
	 * whose ISM stream is stalled.  This collection is populated by
	 * updateFromSiteManager().
	 */
	private Set<Integer> siteIdsWithStalledQueues;

        /**
         * Initializes a new {@code NetworkState} with no site information and
         * no offline site IDs.  It may be useful to subsequently populate the
         * resulting object with site information by invoking its
         * {@link #updateFromSiteManager()} method.
         */
        public NetworkState() {
            siteInfos = new TreeMap<Integer, SiteInfo>();
            siteIdsOffline = new HashSet<Integer>();
	    siteIdsWithStalledQueues = new HashSet<Integer>();
        }
        
        /**
         * Contacts the {@code SiteManager} held by the containing
         * {@code SiteNetworkSynchronizer} instance to obtain its current list
         * of all site information, and replaces any previous site information
         * maintained by this object with the result.  If the URL's of any
	 * sites have changed, those sites are removed from the siteIdsOffline
	 * set.
         * 
         * @throws OperationFailedException if Site Manager fails to provide
	 *         the requested site information
         */
        public void updateFromSiteManager() throws OperationFailedException {
            SiteInfo[] newInfo = siteManager.getAllSiteInfo();

	    // Check for sites whose baseUrl has changed.
	    for (SiteInfo site : newInfo) {
		if (siteIdsOffline.contains(site.id)) {
		    try {
			SiteInfo oldSite = findSite(site.id);
			if (site.baseUrl != null 
                                && !site.baseUrl.equals(oldSite.baseUrl)) {
			    // The offline site's URL has changed.
			    siteIdsOffline.remove(site.id);
			}
		    } catch (IllegalArgumentException ex) {
			// Apparently this is a new site record.  Just eat the
			// error and continue.
		    }
		}
	    }

	    // Copy the sites into our set.
            siteInfos.clear();
            for (SiteInfo site : newInfo) {
                siteInfos.put(site.id, site);
            }

	    // Copy the stalled queues info into our set.
	    siteIdsWithStalledQueues.clear();
	    siteIdsWithStalledQueues.addAll(
                    receivedMessageAgent.identifyStalledQueues());
        }

        /**
         * Reports that a specified site is offline and not available for
         * synchronization at the present time. This information may be
         * retrieved through future invocations of {@code isSiteOffline()}.
         * 
         * @throws IllegalArgumentException if {@code siteId} is not known.
         */
        public void flagSiteAsOffline(int siteId) {
            // Meaningless lookup to validate siteId.
            findSite(siteId);

            siteIdsOffline.add(siteId);
        }

        /**
         * For a specified {@code siteId}, returns true if the site has been
         * previously reported to be offline, or false otherwise.
         * 
         * @throws IllegalArgumentException if {@code siteId} is not known.
         */
        public boolean isSiteOffline(int siteId) {
            // Meaningless lookup to validate siteId.
            findSite(siteId);

            return siteIdsOffline.contains(siteId);
        }

        /**
         * For a specified {@code siteId}, returns either the sequence number
         * of the most recent public ISM originated by the specified site that
         * has been processed by the local site, or the sequence number of the
         * most recent private ISM originated by the specified site that has
         * been processed by the local site, whichever is higher.
         * 
         * @throws IllegalArgumentException if {@code siteId} is not known.
         */
        public long getMaxSeqNumForSite(int siteId) {
            SiteInfo site = findSite(siteId);

            return Math.max(site.publicSeqNum, site.privateSeqNum);
        }

        /**
         * Returns a list of all the {@code SiteInfo} objects that were
	 * provided to this object at construction time. Ordering of records
	 * within the list is not specified. The caller is free to manipulate
	 * the returned list; however, fields on the {@code SiteInfo}'s within
	 * the returned list should not be modified.
         */
        public List<SiteInfo> getAllSites() {
            return new ArrayList<SiteInfo>(siteInfos.values());
        }

        /**
         * Returns a subcollection of this network state's {@code SiteInfo}s
         * that meet specified inclusion criteria.
         * 
         * @param excludeLocal {@code true} if the {@code SiteInfo} associated
         *        with the local site should be excluded from the returned
         *        collection, or {@code false} otherwise
         * @param excludeNoBaseUrl {@code true} if {@code SiteInfo}s whose
         *        {@code baseUrl} field is null should be excluded from the
         *        returned collection, or {@code false} otherwise
         * @param excludeOffline {@code true} if {@code SiteInfo}s associated
         *        with remote sites that have previously been discovered to be
         *        offline should be excluded from the returned collection, or
         *        {@code false} otherwise
         * @param excludeDeactivated {@code true} if {@code SiteInfo}
	 *        associated with fully-deactivated remote sites should be
	 *        excluded from the returned collection, or {@code false}
	 *        otherwise.
         * @return a {@code Collection<SiteInfo>} containing the site
         *         information that satisfies the inclusion criteria; the
         *         collection's iterator will provide {@code SiteInfo}s in
         *         ascending order by site ID
         */
        public Collection<SiteInfo> getSites(boolean excludeLocal,
		boolean excludeNoBaseUrl, boolean excludeOffline, 
                boolean excludeDeactivated) {
            List<SiteInfo> sites = new ArrayList<SiteInfo>();

            for (SiteInfo site : siteInfos.values()) {
                if ((!excludeLocal || (site.id != localSiteId))
                        && (!excludeNoBaseUrl || (site.baseUrl != null)) 
                        && (!excludeOffline || !isSiteOffline(site.id))
		        && (!excludeDeactivated || site.isActive)) {
                    sites.add(site);
                }
            }

            return sites;
        }

        /**
         * Returns the {@code SiteInfo} associated with a particular site id
         * 
         * @throws IllegalArgumentException if {@code siteId} is not known.
         */
        public SiteInfo findSite(int siteId) {
            SiteInfo site = siteInfos.get(siteId);
            
            if (site == null) {
                throw new IllegalArgumentException("Unknown site ID");
            } else {
                return site;
            }
        }
        
        public boolean isDifferentFrom(NetworkState otherState) {
            if (this.siteInfos.size() != otherState.siteInfos.size()) {
                return true;
            }
            
            /*
             * Check for equivalence of the two NetworkStates' siteInfos.
             * We can't use List.equals() because SiteInfo.equals() doesn't do
	     * all the tests we need.
             */
            Iterator<SiteInfo> it = otherState.siteInfos.values().iterator();
            for (SiteInfo site : this.siteInfos.values()) {
                
                // We already verified that the collections are the same size
                assert it.hasNext();

                SiteInfo otherSite = it.next();
                
                // Note: SiteInfos ordered by site ID, so the two collections
                // can be expected to match up
                
                if ((site.id != otherSite.id)
                        || (site.publicSeqNum != otherSite.publicSeqNum)
                        || (site.privateSeqNum != otherSite.privateSeqNum)
	 	        || (site.baseUrl != null && 
                              !site.baseUrl.equals(otherSite.baseUrl))) {
                    return true;
                }
            }

            // We already verified that the collections are the same size
            assert !it.hasNext();

	    /*
	     * Check for equivalence of the two NetworkStates' 
	     * siteIdsWithStalledQueues collections.
	     */
	    if (!this.siteIdsWithStalledQueues.equals(
	            otherState.siteIdsWithStalledQueues)) {
		return true;
	    }

            /*
             * The only other possible difference is in offline states, but we
             * don't care about that
             */
            return false;
        }
        
        /**
         * {@inheritDoc}
         */
        @Override
        public NetworkState clone() {
            try {
                NetworkState x = (NetworkState) super.clone();
                
                x.siteInfos = new TreeMap<Integer, SiteInfo>(siteInfos);
                x.siteIdsOffline = new HashSet<Integer>(siteIdsOffline);
		x.siteIdsWithStalledQueues 
                        = new HashSet<Integer>(siteIdsWithStalledQueues);
                
                return x;
            } catch (CloneNotSupportedException ex) {
                throw new UnexpectedExceptionException(ex);
            }
        }
    }
}
