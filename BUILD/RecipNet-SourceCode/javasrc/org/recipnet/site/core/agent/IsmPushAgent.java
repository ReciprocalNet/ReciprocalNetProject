/*
 * Reciprocal Net project
 * 
 * IsmPushAgent.java
 *
 * 10-Jun-2003: ekoperda wrote first draft
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.core.util package
 *              to org.recipnet.site.core.agent; change package references due
 *              to source tree reorganization
 * 05-Jan-2006: ekoperda made accommodations for new class IsmExchanger and
 *              also converted collections to be type-safe
 * 28-Apr-2006: updated this class to use an AtomicBoolean in place of an
 *              EventSignal for its termination flag, formatted the code,
 *              updated docs
 */

package org.recipnet.site.core.agent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.recipnet.site.OperationFailedException;
import org.recipnet.site.core.SiteManager;
import org.recipnet.site.core.msg.InterSiteMessage;
import org.recipnet.site.core.util.CoreUncaughtExceptionHandler;
import org.recipnet.site.core.util.EventSignal;
import org.recipnet.site.core.util.IsmExchanger;
import org.recipnet.site.core.util.LogRecordGenerator;
import org.recipnet.site.shared.SoapUtil;
import org.recipnet.site.shared.db.SiteInfo;

/**
 * A core-level agent owned by Site Manager that pushes locally-generated ISM's
 * to other sites in the site network. The agent maintains its own worker thread
 * for improved performance; thus pushing ISMs asynchronously with respect to
 * threads that generate them for dispatch. Multiple ISMs intended for the same
 * destination site are transparently batched together in the same push
 * operation. Decisions about which ISMs should be pushed to which destination
 * site are delegated to {@code TopologyAgent}.
 */
public class IsmPushAgent {

    /**
     * Signals the worker thread when this object is notified of a new ISM to be
     * pushed. The signal is sent by
     * {@link #notifyNewIsm(InterSiteMessage, String) notifyNewIsm()} and
     * received/reset by {@link #run()}.
     */
    private final EventSignal messagesPending;

    /**
     * Signals the worker thread that it should exit. The signal is sent by
     * {@code stop()} and received/reset by {@code run()}.
     */
    private final AtomicBoolean shouldTerminate;

    /**
     * A {@code Map} from some sort of key (as defined by
     * {@code DestinationSiteRecord}) to {@code DestinationSiteRecord}s. There
     * is one entry in this map for each destination site that has at least one
     * queued ISM. Entries are added to the map by
     * {@link #notifyNewIsm(InterSiteMessage, String) notifyNewIsm()} and
     * removed from the map by {@link #run()} as part of each push cycle.
     */
    /*
     * All access to this Map should be synchronized (on the Map itself)
     */
    private final Map<Object, DestinationSiteRecord> destinationSites;

    /**
     * A reference to the worker thread; set by {@code start()} and cleared by
     * {@code stop()}.
     */
    private Thread workerThread;

    /** Configuration parameter set at construction time. */
    private final long predelay;

    /** Reference to {@code SiteManager} set at construction time. */
    private final SiteManager siteManager;

    /** Reference to {@code TopologyAgent} set at construction time. */
    private final TopologyAgent topologyAgent;

    /** Reference to an {@code IsmExchanger} set at construction time. */
    private final IsmExchanger ismExchanger;

    /**
     * Initializes a new {@code IsmPushAgent} with the specified parameters.
     * 
     * @param predelay the approximate number of milliseconds
     *        {@code IsmPushAgent} should wait before initiating a push cycle,
     *        after being informed of a newly-generated ISM. Use of a predelay
     *        might improve efficiency for callers that often generate multiple
     *        ISM's to the same destination in quick succession.
     * @param siteManager reference to the {@code SiteManager} object. Only
     *        {@code recordLogEvent()} is invoked via this reference.
     * @param topologyAgent reference to an existing {@code TopologyAgent} with
     *        which this {@code IsmPushAgent} should interface. The caller may
     *        manipulate this {@code TopologyAgent} object as network topology
     *        changes.
     * @param ismExchanger reference to an existing {@code IsmExchanger} object
     *        that is dedicated for the exclusive use of this
     *        {@code IsmPushAgent}.
     */
    public IsmPushAgent(long predelay, SiteManager siteManager,
            TopologyAgent topologyAgent, IsmExchanger ismExchanger) {
        this.messagesPending = new EventSignal();
        this.shouldTerminate = new AtomicBoolean(false);
        this.destinationSites = new HashMap<Object, DestinationSiteRecord>();
        this.workerThread = null;
        this.predelay = predelay;
        this.siteManager = siteManager;
        this.topologyAgent = topologyAgent;
        this.ismExchanger = ismExchanger;
    }

    /**
     * Start the worker thread. This must be invoked by the caller in order to
     * enable ISM pushing.
     */
    public void start() {
        if (this.workerThread != null) {
            throw new IllegalStateException();
        }
        this.workerThread = new Thread(new Runnable() {
            public void run() {
                IsmPushAgent.this.run();
            }
        });
        this.workerThread.start();
    }

    /**
     * Stops the worker thread, aborting the current push cycle if there is one.
     * Blocks until the worker thread has terminated, or until the calling
     * thread is interrupted.
     */
    public void stop() {
        if (this.workerThread == null) {
            throw new IllegalStateException();
        }

        shouldTerminate.set(true);
        Thread dummy = this.workerThread;
        this.workerThread = null;
        dummy.interrupt();
        try {
            dummy.join();
        } catch (InterruptedException ex) {
            // Do nothing; just return.
        }
    }

    /**
     * May be called once for each new ISM that the local site generates. This
     * method signals the worker thread (asynchronously) to push the specified
     * ISM to its target sites. This method invokes
     * {@link TopologyAgent#suggestSitesToPushTo(InterSiteMessage)
     * suggestSitesToPushTo()} internally to discover the appropriate
     * destinations for the specified ISM.
     * 
     * @param ism the {@code InterSiteMessage} object to be pushed.
     * @param ismAsXml the XML representation of {@code ism}, including a valid
     *        digital signature. Such a string might have been obtained via a
     *        prior call to {@code MessageFileAgent.writeSentMessage()}.
     */
    public void notifyNewIsm(InterSiteMessage ism, String ismAsXml) {
        // Adjust the XML string by dropping any headers before the <message>
        // element. This is necessary so that the message will fit inside
        // a SOAP envelope.
        String goodIsmAsXml = SoapUtil.dropXmlDocumentHeader(ismAsXml,
                "message");

        // Discover which sites this ISM should be pushed to.
        Collection<SiteInfo> destinations
                = this.topologyAgent.suggestSitesToPushTo(ism);

        // Place the ISM in each destination site's queue, creating destination
        // site records as needed.
        synchronized (this.destinationSites) {
            for (SiteInfo site : destinations) {
                DestinationSiteRecord rec = this.destinationSites.get(
                        DestinationSiteRecord.getKey(site));
                if (rec == null) {
                    // No record exists yet for this site; create one.
                    rec = new DestinationSiteRecord(site);
                    this.destinationSites.put(rec.getKey(), rec);
                }
                rec.queueMessage(ism, goodIsmAsXml);
            }
        }

        // Notify the worker thread that a new ISM is pending.
        this.messagesPending.send();
    }

    /** Defines the worker thread's behavior */
    private void run() {
        Thread.currentThread().setUncaughtExceptionHandler(
                new CoreUncaughtExceptionHandler(this, this.siteManager));
        while (!shouldTerminate.get()) {
            this.messagesPending.receive();
            if (shouldTerminate.get()) {
                break;
            }

            // Wait a short while to see if any other messages show up.
            try {
                Thread.sleep(this.predelay);
            } catch (InterruptedException ex) {
                // don't do anything; just drop through.
            }

            // Take a snapshot of the destination sites.
            Collection<DestinationSiteRecord> destinationSitesSnapshot;
            synchronized (this.destinationSites) {
                destinationSitesSnapshot = new ArrayList<DestinationSiteRecord>(
                        this.destinationSites.values());
            }

            // Iterate through all the destination sites.
            for (DestinationSiteRecord rec : destinationSitesSnapshot) {
                if (shouldTerminate.get()) {
                    break;
                }

                // Obtain the queue of pending ISM's for the current
                // destination site.
                String queuedIsmsAsXml[];
                synchronized (this.destinationSites) {
                    queuedIsmsAsXml = rec.getQueuedMessages();
                    this.destinationSites.remove(rec.getKey());
                }
                assert queuedIsmsAsXml.length != 0;

                // Push all queued ISM's to the current destination site.
                // Recover gracefully from any network errors that might
                // occur.
                try {
                    String replies[] = this.ismExchanger.exchange(
                            queuedIsmsAsXml, rec.getBaseUrl(), true);
                    // FIXME: if the remote site sent any ISMs in reply then
                    // maybe we should do something with them. For now,
                    // just discard them because we have no idea what
                    // purpose they might serve.
                    this.siteManager.recordLogRecord(
                            LogRecordGenerator.ismPushed(
                                    rec.getIsms(), rec.getSiteInfo(),
                                    replies.length));
                    topologyAgent.notifyPush(rec.getSiteId(), true);
                } catch (OperationFailedException ex) {
                    this.siteManager.recordLogRecord(
                            LogRecordGenerator.ismPushFailed(
                                    rec.getIsms(), rec.getSiteInfo(),
                                    ismExchanger.getLastServerErrorMessage(),
                                    ex));
                    topologyAgent.notifyPush(rec.getSiteId(), false);
                }
            }
        }
    }

    /**
     * Creates and returns a String representation of this agent; intended for
     * debugging only.
     * 
     * @return a {@code String} representation of this agent
     */
    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        String term = System.getProperty("line.separator");

        buf.append("IsmPushAgent queue:").append(term);
        synchronized (this.destinationSites) {
            for (DestinationSiteRecord rec : this.destinationSites.values()) {
                buf.append(rec.toString()).append(term);
            }
        }
        return buf.toString();
    }

    /**
     * Internal container class
     */
    private static class DestinationSiteRecord {
        private final SiteInfo site;

        private final Collection<InterSiteMessage> isms;

        private final Collection<String> ismsAsXml;

        /**
         * Initializes a new {@code DestinationSiteRecord} for ISMs directed to
         * the specified site
         * 
         * @param site a {@code SiteInfo} representing the site with which this
         *        {@code DestinationSiteRecord} is associated
         */
        public DestinationSiteRecord(SiteInfo site) {
            this.site = site;
            this.isms = new ArrayList<InterSiteMessage>();
            this.ismsAsXml = new ArrayList<String>();
        }

        /**
         * Queues a message for delivery to the site associated with this
         * {@code DestinationSiteRecord}
         * 
         * @param ism the {@code InterSiteMessage} to deliver
         * @param ismAsXml a {@code String} representation of the ISM
         */
        public void queueMessage(InterSiteMessage ism, String ismAsXml) {
            this.isms.add(ism);
            this.ismsAsXml.add(ismAsXml);
        }

        /**
         * Retrieves the XML String forms of the currently queued messages
         * 
         * @return a {@code String[]} containing the as elements the XML
         *         {@code String}s corresponding to this
         *         {@code DestinationSiteRecord}'s queued messages
         */
        public String[] getQueuedMessages() {
            return this.ismsAsXml.toArray(new String[ismsAsXml.size()]);
        }

        /**
         * Retrieves the ID of the site with which this site record is
         * associated
         * 
         * @return the site ID of the associated site
         */
        public int getSiteId() {
            return this.site.id;
        }

        /**
         * Retrieves the {@code SiteInfo} of the site with which this site
         * record is associated
         * 
         * @return the {@code SiteInfo} for the associated site
         */
        public SiteInfo getSiteInfo() {
            return this.site;
        }

        /**
         * Retrieves the base URL of the site associated with this record
         * 
         * @return the associated site's base URL
         */
        public String getBaseUrl() {
            return this.site.baseUrl;
        }

        /**
         * Retrieves the ISMs currently queued for delivery to the site
         * associated with this record
         * 
         * @return the ISMs queued for delivery, in {@code Collection} form
         */
        public Collection<InterSiteMessage> getIsms() {
            return Collections.unmodifiableCollection(this.isms);
        }

        /**
         * Creates and returns a {@code String} representation of this
         * {@code DestinationSiteRecord}. Intended only for debugging.
         * 
         * @return a {@code String} representing this record and describing the
         *         messages it contains
         */
        @Override
        public String toString() {
            StringBuilder buf = new StringBuilder();

            buf.append("  target site id ");
            buf.append(this.site.id);
            buf.append(" (");
            buf.append(this.site.shortName);
            buf.append("):");

            for (InterSiteMessage ism : this.isms) {
                buf.append(" ");
                buf.append(ism.getSuggestedFileName());
            }

            return buf.toString();
        }

        /**
         * Provides the recommended key with which to reference this
         * {@code DestinationSiteRecord}
         * 
         * @return the key {@code Object} recommended for use with this record
         */
        public Object getKey() {
            return getKey(this.site);
        }

        /**
         * Provides the recommended key with which to associate any
         * {@code DestinationSiteRecord} referring to the specified site
         * 
         * @param site a {@code SiteInfo} representing the site for which
         *        {@code DestinationSiteRecord} objects' keys are requested
         * @return the key {@code Object}
         */
        public static Object getKey(SiteInfo site) {
            return site.id;
        }
    }
}
