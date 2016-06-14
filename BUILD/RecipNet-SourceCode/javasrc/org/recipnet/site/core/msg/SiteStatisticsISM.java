/*
 * Reciprocal Net project
 * @(#)SiteStatisticsIsm.java
 *
 * 10-Jul-2003: midurbin wrote first draft
 * 07-Jan-2004: ekoperda changed package references to match source tree
 *              reorganization
 * 13-May-2004: midurbin fixed bug #1207 in extractFromDom()
 * 26-May-2006: jobollin reformatted the source; implemented generics
 */

package org.recipnet.site.core.msg;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import org.recipnet.site.core.msg.InterSiteMessage;
import org.recipnet.site.shared.DomUtil;
import org.recipnet.site.shared.db.LabInfo;
import org.recipnet.site.shared.db.LabStatisticsInfo;
import org.recipnet.site.shared.db.SiteStatisticsInfo;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Inter-site message containing the site statistics since the last time it was
 * reset.
 */
public class SiteStatisticsISM extends InterSiteMessage {

    public static final int NOT_REPORTED = -1;

    /** The time when all of the counters were reset */
    public Date lastReset;

    /** The number of times recipnetd started up since last reset */
    public int countRecipnetdStartups;

    /** The number of times recipnetd shut down since last reset */
    public int countRecipnetdShutdowns;

    /** The number of seconds recipnetd has been running since last reset */
    public int uptime;

    /** The number of authenticated webapp sessions since last reset */
    public int authWebappSessions;

    /** The number of unauthenticated webapp sessions since last reset */
    public int unauthWebappSessions;

    /**
     * The number of OAI-PMH queries answered by local site since last reset
     */
    public int countOaiPmhQueries;

    /**
     * The number of samples returned for OAI-PMH queries to local site since
     * last reset
     */
    public int countOaiPmhQuerySamples;

    /** The version of Reciprocal Net software that is running. */
    public String version;

    /**
     * Maps Integers representing site IDs to Longs representing the
     * sourceSeqNum taken from the last accepted public ISM that originated from
     * that site. There is one entry in this map for each site about which a
     * SiteActivationISM has been received.
     */
    public Map<Integer, Long> ismPublicSeqNumMap;

    /**
     * A Map similar to {@code ismPublicSeqNumMap} except that it contains
     * sequence number from private messages to this site from all others.
     */
    public Map<Integer, Long> ismPrivateSeqNumMap;

    /**
     * A {@code Collection} of {@code PerLabStats}, one for each lab at the
     * local site. These records are created at construction time.
     */
    public Collection<PerLabStats> labSpecificStatistics;

    /**
     * This constructor is needed to instantiate a {@code SiteStatisticsISM }
     * from XML.
     */
    public SiteStatisticsISM() {
        super();
        this.lastReset = null;
        this.countRecipnetdStartups = SiteStatisticsISM.NOT_REPORTED;
        this.countRecipnetdShutdowns = SiteStatisticsISM.NOT_REPORTED;
        this.uptime = SiteStatisticsISM.NOT_REPORTED;
        this.authWebappSessions = SiteStatisticsISM.NOT_REPORTED;
        this.unauthWebappSessions = SiteStatisticsISM.NOT_REPORTED;
        this.countOaiPmhQueries = SiteStatisticsISM.NOT_REPORTED;
        this.countOaiPmhQuerySamples = SiteStatisticsISM.NOT_REPORTED;
        this.version = null;
        this.ismPublicSeqNumMap = null;
        this.ismPrivateSeqNumMap = null;
        this.labSpecificStatistics = null;
    }

    /**
     * This constructor requires a {@code SiteStatisticsInfo} object and a
     * {@code Collection} of {@code LabStatisticsInfo} objects to initialize a
     * {@code SiteStatisticsISM}. After construction, the fields
     * {@code version} and {@code seqNumMap} must be assigned manually. Also,
     * some lab specific values ought to be set using the provided interface.
     * 
     * @param sourceSiteId the id of the local site
     * @param destSiteId the id of the site to which the ISM is addressed
     * @param siteStats contains all of the site statistics that are stored in
     *        the db table 'statistics_site'.
     * @param labStats is a {@code Collection} of {@code  LabStatisticsInfo}
     *        objects reflecting the most recent lab specific statistics
     *        described in the db table 'statistics_lab'.
     */
    public SiteStatisticsISM(int sourceSiteId, int destSiteId,
            SiteStatisticsInfo siteStats,
            Collection<? extends LabStatisticsInfo> labStats) {
        super();
        super.sourceSiteId = sourceSiteId;
        super.destSiteId = destSiteId;
        this.lastReset = siteStats.lastReset;
        this.countRecipnetdStartups = siteStats.countRecipnetdStartups;
        this.countRecipnetdShutdowns = siteStats.countRecipnetdShutdowns;
        this.uptime = siteStats.uptime;
        this.authWebappSessions = siteStats.authWebappSessions;
        this.unauthWebappSessions = siteStats.unauthWebappSessions;
        this.countOaiPmhQueries = siteStats.countOaiPmhQueries;
        this.countOaiPmhQuerySamples = siteStats.countOaiPmhQuerySamples;
        if (labStats == null) {
            labSpecificStatistics = null;
        } else {
            labSpecificStatistics = new HashSet<PerLabStats>();
            for (LabStatisticsInfo ls : labStats) {
                labSpecificStatistics.add(new PerLabStats(ls));
            }
        }
    }

    /**
     * From interface DomTreeParticipant, overrides function on
     * InterSiteMessage.
     */
    @Override
    public Node insertIntoDom(Document doc, Node base) {
        Node parent = super.insertIntoDom(doc, base);
        SimpleDateFormat sdf = new SimpleDateFormat(
                InterSiteMessage.DATE_FORMAT);

        // Create a <siteStats> element
        Element siteStatsEl = DomUtil.createEl(doc, parent, "siteStats");

        // Add all site statistics
        if (this.lastReset != null) {
            DomUtil.createTextEl(siteStatsEl, "lastReset",
                    sdf.format(this.lastReset));
        }
        if (this.version != null) {
            DomUtil.createTextEl(siteStatsEl, "version", this.version);
        }
        if (this.countRecipnetdStartups != SiteStatisticsISM.NOT_REPORTED) {
            DomUtil.createTextEl(siteStatsEl, "countRecipnetdStartups",
                    Integer.toString(this.countRecipnetdStartups));
        }
        if (this.countRecipnetdShutdowns != SiteStatisticsISM.NOT_REPORTED) {
            DomUtil.createTextEl(siteStatsEl, "countRecipnetdShutdowns",
                    Integer.toString(this.countRecipnetdShutdowns));
        }
        if (this.uptime != SiteStatisticsISM.NOT_REPORTED) {
            DomUtil.createTextEl(siteStatsEl, "uptime",
                    Integer.toString(this.uptime));
        }
        if (this.authWebappSessions != SiteStatisticsISM.NOT_REPORTED) {
            DomUtil.createTextEl(siteStatsEl, "authWebappSessions",
                    Integer.toString(this.authWebappSessions));
        }
        if (this.unauthWebappSessions != SiteStatisticsISM.NOT_REPORTED) {
            DomUtil.createTextEl(siteStatsEl, "unauthWebappSessions",
                    Integer.toString(this.unauthWebappSessions));
        }
        if (this.countOaiPmhQueries != SiteStatisticsISM.NOT_REPORTED) {
            DomUtil.createTextEl(siteStatsEl, "countOaiPmhQueries",
                    Integer.toString(this.countOaiPmhQueries));
        }
        if (this.countOaiPmhQuerySamples != SiteStatisticsISM.NOT_REPORTED) {
            DomUtil.createTextEl(siteStatsEl, "countOaiPmhQuerySamples",
                    Integer.toString(this.countOaiPmhQuerySamples));
        }
        if ((this.ismPublicSeqNumMap != null)
                && (this.ismPrivateSeqNumMap != null)) {
            /*
             * Create a <ismSeqNums> element for each site with an attribute
             * identifying the site and two children representing the last
             * public and the last private sequence numbers
             */
            for (Entry<Integer, Long> entry : ismPublicSeqNumMap.entrySet()) {
                Element ismSeqNumsEl = DomUtil.createEl(doc, siteStatsEl,
                        "ismSeqNums");

                DomUtil.addAttrToEl(ismSeqNumsEl, "siteId",
                        (entry.getKey()).toString());
                DomUtil.createTextEl(ismSeqNumsEl, "lastPublic",
                        (entry.getValue()).toString());
                DomUtil.createTextEl(ismSeqNumsEl, "lastPrivate",
                        this.ismPrivateSeqNumMap.get(entry.getKey()).toString());
            }
        }

        if (this.labSpecificStatistics != null) {
            // Create a <perLabStats> element for each lab and create its
            // children
            Iterator<PerLabStats> it = this.labSpecificStatistics.iterator();
            while (it.hasNext()) {
                Element perLabStatsEl = DomUtil.createEl(doc, siteStatsEl,
                        "perLabStats");
                PerLabStats currentLab = it.next();

                DomUtil.addAttrToEl(perLabStatsEl, "labId", (new Integer(
                        currentLab.labId)).toString());
                if (currentLab.showSamplePageViews
                        != SiteStatisticsISM.NOT_REPORTED) {
                    DomUtil.createTextEl(perLabStatsEl, "showSamplePageViews",
                            Integer.toString(currentLab.showSamplePageViews));
                }
                if (currentLab.editSamplePageViews
                        != SiteStatisticsISM.NOT_REPORTED) {
                    DomUtil.createTextEl(perLabStatsEl, "editSamplePageViews",
                            Integer.toString(currentLab.editSamplePageViews));
                }
                if (currentLab.countPublicSamples
                        != SiteStatisticsISM.NOT_REPORTED) {
                    DomUtil.createTextEl(perLabStatsEl, "countPublicSamples",
                            Integer.toString(currentLab.countPublicSamples));
                }
                if (currentLab.repositorySize
                        != SiteStatisticsISM.NOT_REPORTED) {
                    DomUtil.createTextEl(perLabStatsEl, "repositorySize",
                            Long.toString(currentLab.repositorySize));
                }
            }
        }
        return parent;
    }

    /**
     * From interface DomTreeParticipant, overrides function on
     * InterSiteMessage.
     */
    @Override
    public Node extractFromDom(Document doc, Node base) throws SAXException {
        Node parent = super.extractFromDom(doc, base);
        SimpleDateFormat sdf = new SimpleDateFormat(
                InterSiteMessage.DATE_FORMAT);

        // Get the <siteStats> element
        Element siteStatsEl = DomUtil.findSingleElement((Element) parent,
                "siteStats", true);

        // Get all children
        if (DomUtil.isElPresent(siteStatsEl, "lastReset")) {
            try {
                this.lastReset = sdf.parse(DomUtil.getTextForEl(siteStatsEl,
                        "lastReset", true));
            } catch (ParseException ex) {
                throw new SAXParseException("Invalid date format", null, ex);
            }
        }
        this.version = DomUtil.getTextForEl(siteStatsEl, "version", false);
        this.countRecipnetdStartups = DomUtil.getTextForElAsInt(siteStatsEl,
                "countRecipnetdStartups", SiteStatisticsISM.NOT_REPORTED);
        this.countRecipnetdShutdowns = DomUtil.getTextForElAsInt(siteStatsEl,
                "countRecipnetdShutdowns", SiteStatisticsISM.NOT_REPORTED);
        this.uptime = DomUtil.getTextForElAsInt(siteStatsEl, "uptime");
        this.authWebappSessions = DomUtil.getTextForElAsInt(siteStatsEl,
                "authWebappSessions", SiteStatisticsISM.NOT_REPORTED);
        this.unauthWebappSessions = DomUtil.getTextForElAsInt(siteStatsEl,
                "unauthWebappSessions", SiteStatisticsISM.NOT_REPORTED);
        this.countOaiPmhQueries = DomUtil.getTextForElAsInt(siteStatsEl,
                "countOaiPmhQueries", SiteStatisticsISM.NOT_REPORTED);
        this.countOaiPmhQuerySamples = DomUtil.getTextForElAsInt(siteStatsEl,
                "countOaiPmhQuerySamples", SiteStatisticsISM.NOT_REPORTED);

        NodeList ismSeqNumsEls = siteStatsEl.getElementsByTagName("ismSeqNums");
        if (ismSeqNumsEls.getLength() != 0) {
            this.ismPublicSeqNumMap = new HashMap<Integer, Long>();
            this.ismPrivateSeqNumMap = new HashMap<Integer, Long>();
            // Read values from each <ismSeqNums> element
            for (int i = 0; i < ismSeqNumsEls.getLength(); i++) {
                Element ismSeqNumsEl = (Element) ismSeqNumsEls.item(i);
                Integer siteId = new Integer(DomUtil.getAttrForElAsInt(
                        ismSeqNumsEl, "siteId"));
                this.ismPublicSeqNumMap.put(siteId, new Long(
                        DomUtil.getTextForElAsLong(ismSeqNumsEl, "lastPublic")));
                this.ismPrivateSeqNumMap.put(siteId,
                        new Long(DomUtil.getTextForElAsLong(ismSeqNumsEl,
                                "lastPrivate")));
            }
        }

        NodeList perLabStatsEls
                = siteStatsEl.getElementsByTagName("perLabStats");
        if (perLabStatsEls.getLength() != 0) {
            this.labSpecificStatistics = new HashSet<PerLabStats>();

            // Read values from each <perLabStats> element
            for (int i = 0; i < perLabStatsEls.getLength(); i++) {
                Element perLabStatsEl = (Element) perLabStatsEls.item(i);
                PerLabStats currentLab = new PerLabStats();
                currentLab.labId = DomUtil.getAttrForElAsInt(perLabStatsEl,
                        "labId");
                currentLab.showSamplePageViews = DomUtil.getTextForElAsInt(
                        perLabStatsEl, "showSamplePageViews",
                        SiteStatisticsISM.NOT_REPORTED);
                currentLab.editSamplePageViews = DomUtil.getTextForElAsInt(
                        perLabStatsEl, "editSamplePageViews",
                        SiteStatisticsISM.NOT_REPORTED);
                currentLab.countPublicSamples = DomUtil.getTextForElAsInt(
                        perLabStatsEl, "countPublicSamples",
                        SiteStatisticsISM.NOT_REPORTED);
                currentLab.repositorySize = DomUtil.getTextForElAsLong(
                        perLabStatsEl, "repositorySize",
                        SiteStatisticsISM.NOT_REPORTED);
                this.labSpecificStatistics.add(currentLab);
            }
        }
        return parent;
    }

    /**
     * An inner class record containing statistics information on a per-lab
     * basis. The {@code SiteStatisticsISM} has one of these records for each
     * lab.
     */
    public static class PerLabStats {

        /** identifies the lab whose statistics are contained in this record */
        public int labId;

        /**
         * the number of times a page displaying metadata about a sample from
         * this lab has been viewed
         */
        public int showSamplePageViews;

        /**
         * the number of times a page that allows metadata about a sample from
         * this lab to be altered, has been viewed
         */
        public int editSamplePageViews;

        /**
         * the number of smaples that have been released to public for this lab
         */
        public int countPublicSamples;

        /**
         * the total number of kilobytes stored in the primary directories for
         * all samples from this lab
         */
        public long repositorySize;

        /** creates an empty {@code PerLabStats} object */
        public PerLabStats() {
            this.labId = LabInfo.INVALID_LAB_ID;
            this.showSamplePageViews = SiteStatisticsISM.NOT_REPORTED;
            this.countPublicSamples = SiteStatisticsISM.NOT_REPORTED;
            this.repositorySize = SiteStatisticsISM.NOT_REPORTED;
        }

        /**
         * Initializes a PerLabStats object using the given LabStatisticsInfo
         * object.
         */
        public PerLabStats(LabStatisticsInfo ls) {
            this.labId = ls.labId;
            this.showSamplePageViews = ls.showSamplePageViews;
            this.editSamplePageViews = ls.editSamplePageViews;
            this.countPublicSamples = SiteStatisticsISM.NOT_REPORTED;
            this.repositorySize = SiteStatisticsISM.NOT_REPORTED;
        }
    }
}
