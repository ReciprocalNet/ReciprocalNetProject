/*
 * Reciprocal Net project
 * 
 * Coordinator.java
 *
 * 12-Jul-2002: ekoperda wrote first draft
 * 11-Sep-2002: ekoperda added updateExistingSite() method
 * 30-Sep-2002: ekoperda updated whole class to support XML-format ISM's and
 *              corresponding restructuring in the site software
 * 07-Jan-2004: ekoperda changed package references to match source tree
 *              reorganization
 * 03-May-2004: midurbin added requestStatistics() and support for parsing
 *              previously sent siteStatisticsISMs
 * 05-May-2005: ekoperda added deactivateExistingSite()
 * 05-Jun-2006: jobollin reformatted the source
 * 14-May-2008: ekoperda adjusted to match changes to SampleIdBlockISM
 * 28-Nov-2008: ekoperda added forceUpgrade() and support for ForceUpgradeISM,
 *              and revised support for SiteDeactivationISM
 * 31-Dec-2008: ekoperda fixed bug #1911 that affected prevSeqNums on ISM's
 * 02-Jan-2009: ekoperda added initiateLabTransfer() and support for 
 *              LabTransferInitiateISM's
 * 02-Jan-2009: ekoperda added claimSampleIdBlock(), reactivateExistingSite(),
 *              and new versions of transferSampleIdBlock(), 
 *              replayAllMessages(), and pushNewMessages()
 * 25-Jan-2009: ekoperda added resetSeqNums() and support for SiteResetISM's.
 */

package org.recipnet.coordinator;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.recipnet.site.InvalidDataException;
import org.recipnet.site.OperationFailedException;
import org.recipnet.site.core.MessageDecodingException;
import org.recipnet.site.core.ResourceException;
import org.recipnet.site.core.ResourceNotFoundException;
import org.recipnet.site.core.agent.MessageFileAgent;
import org.recipnet.site.core.agent.TopologyAgent;
import org.recipnet.site.core.msg.ForceUpgradeISM;
import org.recipnet.site.core.msg.InterSiteMessage;
import org.recipnet.site.core.msg.LabActivationISM;
import org.recipnet.site.core.msg.LabTransferInitiateISM;
import org.recipnet.site.core.msg.LabUpdateISM;
import org.recipnet.site.core.msg.SampleDeactivationISM;
import org.recipnet.site.core.msg.SampleIdBlockISM;
import org.recipnet.site.core.msg.SiteActivationISM;
import org.recipnet.site.core.msg.SiteDeactivationISM;
import org.recipnet.site.core.msg.SiteGrantISM;
import org.recipnet.site.core.msg.SiteResetISM;
import org.recipnet.site.core.msg.SiteStatisticsRequestISM;
import org.recipnet.site.core.msg.SiteUpdateISM;
import org.recipnet.site.core.util.IsmExchanger;
import org.recipnet.site.core.util.MsgpakUtil;
import org.recipnet.site.shared.SoapUtil;
import org.recipnet.site.shared.db.LabInfo;
import org.recipnet.site.shared.db.SiteInfo;
import org.xml.sax.SAXException;

/**
 * The main Coordinator class. It keeps track of messages that have been sent
 * (by prior instances) and knows how to send a variety of messages.
 */
public class Coordinator {
    /** Our (the coordinator's) site id, as obtained from recipnet.sitegrant */
    protected int localSiteId;

    /**
     * The Signature object that can be used to sign messages, initialized with
     * our local private key
     */
    protected Signature sigEngine;

    /** The random number generator */
    protected SecureRandom random;

    /**
     * Helper object that represents the directory where we keep all messages
     * that have been sent from the coordinator previously.
     */
    protected MessageFileAgent messageFileAgent;

    protected long lastOldSeqNum;

    protected long highestSeqNum;

    protected Map<Integer, Long> highestPublicSeqNumByDest;
    protected Map<Integer, Long> highestPrivateSeqNumByDest;

    /**
     * The four variables below comprise our state table. They keep track of
     * which "distinguished resource numbers" the coordinator has issued
     * already.
     */
    protected Map<Integer, SiteInfo> issuedSites;

    protected Map<Integer, LabInfo> issuedLabs;

    protected Collection<Integer> issuedSampleIdBlocks;

    protected Collection<Integer> reservedSampleIdBlocks;

    protected boolean hasReadGrant;

    protected boolean hasReadOldMessages;

    protected File confDirectory;

    protected SiteGrantISM savedSiteGrant;

    /**
     * The one and only constructor. The first object should point to the the
     * 'conf/' directory in the coordinator's installation, and the second
     * object should point to the 'msgs-sent/' directory. The Coordinator
     * object will not be completely initialized until follow-up calls to
     * initializeFromGrant() and initializeFromOldMessages() are made.
     */
    public Coordinator(File confDirectory, File msgsSentDirectory)
            throws NoSuchAlgorithmException, OperationFailedException {
        random = SecureRandom.getInstance("SHA1PRNG");
        messageFileAgent = new MessageFileAgent(
                InterSiteMessage.RECIPROCAL_NET_COORDINATOR,
                 new TopologyAgent(
                        new SiteInfo[0],
                        InterSiteMessage.RECIPROCAL_NET_COORDINATOR, 0, null),
                null, msgsSentDirectory, null, null);
        issuedSites = new HashMap<Integer, SiteInfo>();
        issuedLabs = new HashMap<Integer, LabInfo>();
        issuedSampleIdBlocks = new HashSet<Integer>();
        reservedSampleIdBlocks = new HashSet<Integer>();
        hasReadGrant = false;
        hasReadOldMessages = false;
        this.confDirectory = confDirectory;
        savedSiteGrant = null;
        sigEngine = Signature.getInstance("SHA1withDSA");
        lastOldSeqNum = InterSiteMessage.INVALID_SEQ_NUM;
        highestSeqNum = InterSiteMessage.INVALID_SEQ_NUM;
        highestPublicSeqNumByDest = new HashMap<Integer, Long>();
	highestPrivateSeqNumByDest = new HashMap<Integer, Long>();
    }

    /**
     * Reads some of the Coordinator's configuration data from the root
     * 'recipnet.sitegrant' file. We obtain our site id and private encryption
     * key in this way. The Coordinator object has not been properly
     * initialized until this function has been called.
     */
    public void initializeFromGrant() throws InvalidDataException,
            InvalidKeyException, MessageDecodingException,
            OperationFailedException {
        SiteGrantISM ourSiteGrant = MsgpakUtil.readAndDecodeSiteGrant(new File(
                confDirectory, "recipnet.sitegrant"));
        this.localSiteId = ourSiteGrant.destSiteId;
        this.sigEngine.initSign(ourSiteGrant.privateKey);
        this.hasReadGrant = true;
    }

    /**
     * Alternative to initializeFromGrant() that may be invoked if you want to
     * create a new Coordinator rather than read from an existing one's
     * records.
     */
    public void initializeByCreatingGrant(String siteName,
            String siteShortName) throws InvalidKeyException, 
            NoSuchAlgorithmException, OperationFailedException, 
            ResourceException {
        this.messageFileAgent.deleteAll();

        /*
         * generate the coordinator's encryption key pair. We're only using
         * these keys to sign SHA1 message digests, so choose a 1024-bit
         * DSA-type key.
         */
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA");
        keyGen.initialize(1024, this.random);
        KeyPair pair = keyGen.generateKeyPair();
        PublicKey publicKey = pair.getPublic();
        PrivateKey privateKey = pair.getPrivate();
        this.sigEngine.initSign(privateKey);

        // Generate Coordinator ISM 0, a SiteActivationISM.
        SiteActivationISM saIsm = new SiteActivationISM(new SiteInfo(
                InterSiteMessage.RECIPROCAL_NET_COORDINATOR, siteName,
                siteShortName, publicKey));
        saIsm.sourceSeqNum = 0;
        saIsm.sourcePrevSeqNum = InterSiteMessage.INVALID_SEQ_NUM;
        String saIsmXml = this.messageFileAgent.generateSentMessage(saIsm,
                this.sigEngine);

        // Generate Coordinator ISM 1, a SiteGrantISM.
        SiteGrantISM sgIsm = new SiteGrantISM(
                InterSiteMessage.RECIPROCAL_NET_COORDINATOR, privateKey);
        sgIsm.sourceSeqNum = 1;
        sgIsm.sourcePrevSeqNum = InterSiteMessage.INVALID_SEQ_NUM;
        String sgIsmXml = this.messageFileAgent.generateSentMessage(sgIsm,
                this.sigEngine);

        // Create the Coordinator's sitegrant file for use during future
        // startups.
        MsgpakUtil.createAndPopulate(new File(this.confDirectory,
                "recipnet.sitegrant"), new String[] { saIsmXml, sgIsmXml },
                sgIsmXml);

        this.localSiteId = InterSiteMessage.RECIPROCAL_NET_COORDINATOR;
        this.hasReadGrant = true;
    }

    /**
     * Reads some of the Coordinator's state data from the 'msgs-sent'
     * directory, the place where we keep all messages that have been sent
     * previously. Builds an internal state table to keep track of which site
     * id's, lab id's, and sample id blocks have been issued previously. The
     * Coordinator object has not been properly initialized until this function
     * has been called.
     */
    public void initializeFromOldMessages() throws MessageDecodingException,
            OperationFailedException {
        initializeFromOldMessages(false);
    }

    public void initializeFromOldMessages(boolean verbose)
            throws MessageDecodingException, OperationFailedException {
        // Iterate through all ISM's the Coordinator has sent previously.
        // These are guaranteed to be in ascending sequence number order.
        for (InterSiteMessage ism :
                this.messageFileAgent.readAndDecodeAllSentMessages()) {
            // Update our sequence number state tables.
            this.highestSeqNum = ism.sourceSeqNum;
	    if (ism.isPublic()) {
		this.highestPublicSeqNumByDest.put(ism.destSiteId, 
                        ism.sourceSeqNum);
	    } else {
		this.highestPrivateSeqNumByDest.put(ism.destSiteId,
		        ism.sourceSeqNum);
	    }

            // Decipher the message type and update other state tables.
            if (ism instanceof SiteActivationISM) {
                SiteActivationISM msg = (SiteActivationISM) ism;
                this.issuedSites.put(msg.newSite.id, msg.newSite);
            } else if (ism instanceof SiteUpdateISM) {
                SiteUpdateISM msg = (SiteUpdateISM) ism;
                this.issuedSites.put(msg.updatedSite.id, msg.updatedSite);
            } else if (ism instanceof SiteDeactivationISM) {
                SiteDeactivationISM msg = (SiteDeactivationISM) ism;
		SiteInfo site = this.issuedSites.get(msg.oldSiteId);
		site.isActive = false;
            } else if (ism instanceof LabActivationISM) {
                LabActivationISM msg = (LabActivationISM) ism;
                this.issuedLabs.put(msg.newLab.id, msg.newLab);
            } else if (ism instanceof LabUpdateISM) {
                LabUpdateISM msg = (LabUpdateISM) ism;
                this.issuedLabs.put(msg.updatedLab.id, msg.updatedLab);
	    } else if (ism instanceof LabTransferInitiateISM) {
		LabTransferInitiateISM msg = (LabTransferInitiateISM) ism;
		LabInfo lab = this.issuedLabs.get(msg.labId);
		lab.homeSiteId = msg.newHomeSiteId;
            } else if (ism instanceof SampleIdBlockISM) {
                SampleIdBlockISM msg = (SampleIdBlockISM) ism;
                switch (msg.func) {
                    case SampleIdBlockISM.CLAIM:
                        this.reservedSampleIdBlocks.add(msg.blockId);
                        break;
                    case SampleIdBlockISM.TRANSFER_INITIATE:
                        this.reservedSampleIdBlocks.remove(msg.blockId);
                        issuedSampleIdBlocks.add(msg.blockId);
                        break;
                }
            } else if (ism instanceof SiteGrantISM) {
                // No need to update our state tables for a Site Grant message
            } else if (ism instanceof SiteStatisticsRequestISM) {
                // No need to update our state tables for a Statistics Request
            } else if (ism instanceof SampleDeactivationISM) {
                /*
                 * No need to update our state tables because we don't keep
                 * track of samples
                 */
            } else if (ism instanceof ForceUpgradeISM) {
		// No need to update our state tables for these ISM's.
	    } else if (ism instanceof SiteResetISM) {
		// No need to update our state tables for these ISM's.
	    } else {
                System.out.println("Unknown type");
                throw new RuntimeException("Unable to process message "
                        + ism.getSuggestedFileName() + " because it has an"
                        + " unknown type " + ism.getClass().getName());
            }

            if (verbose) {
                displayMessage(ism);
            }
        }

        this.hasReadOldMessages = true;
        this.lastOldSeqNum = this.highestSeqNum;
    }

    int getSitesIssued() {
        return issuedSites.size();
    }

    int getLabsIssued() {
        return issuedLabs.size();
    }

    int getSampleIdBlocksIssued() {
        return issuedSampleIdBlocks.size();
    }

    int getSampleIdBlocksReserved() {
        return reservedSampleIdBlocks.size();
    }

    /**
     * Generates the two messages that are necessary for creating a new site.
     * The first message (the SiteActivationISM) is written immediately, while
     * the second message (the SiteGrantISM) is saved to be written upon the
     * next call to finishCreatingNewSite(). The caller most likely will want
     * to generate several intervening messages between the call to
     * beginCreatingNewSite() and finishCreatingNewSite(). It is not possible
     * to overlap creation of two new sites; the first one must be "finished"
     * before the next one may be "begun". The new site's unique id and
     * public/private encryption keys are generated at random. Returns the new
     * site's id.
     */
    public int beginCreatingNewSite(String name, String shortName,
            String baseUrl, String repositoryUrl)
            throws NoSuchAlgorithmException, OperationFailedException {
        int siteId;
        SiteInfo site;

        // Generate a new site id at random. ensure it's unique.
        do {
            siteId = random.nextInt(32768);
        } while (issuedSites.get(new Integer(siteId)) != null);

        // Generate a new keypair
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA");
        keyGen.initialize(1024, random);
        KeyPair pair = keyGen.generateKeyPair();

        // Write the Site Activation message to file.
        site = new SiteInfo(siteId, name, shortName, baseUrl, repositoryUrl,
                pair.getPublic());
        this.generateMessage(new SiteActivationISM(site));
        issuedSites.put(new Integer(siteId), site);

        // Save the Site Grant message for a future use in
        // finishCreatingNewSite()
        savedSiteGrant = new SiteGrantISM(siteId, pair.getPrivate());
        return siteId;
    }

    /**
     * Follow-up call to beginCreatingNewSite() above. Closes out the site
     * creation process and writes a 'recipnet.sitegrant' file to the specified
     * location (the file may have any name).
     */
    public void finishCreatingNewSite(File recipnetSiteGrantFile)
            throws MessageDecodingException, OperationFailedException {
        if (savedSiteGrant == null) {
            throw new IllegalStateException();
        }

        recipnetSiteGrantFile.delete();
        String siteGrantAsXml = this.generateMessage(savedSiteGrant);
        Collection<String> ismsAsXml = new ArrayList<String>();
        messageFileAgent.readMessagesSuitableForRemoteSite(
                savedSiteGrant.destSiteId,
                InterSiteMessage.RECIPROCAL_NET_COORDINATOR,
                InterSiteMessage.INVALID_SEQ_NUM,
                InterSiteMessage.INVALID_SEQ_NUM, MessageFileAgent.NO_LIMIT,
                ismsAsXml);
        MsgpakUtil.createAndPopulate(recipnetSiteGrantFile,
                ismsAsXml.toArray(new String[0]), siteGrantAsXml);

        savedSiteGrant = null;
    }

    public int createNewLab(String name, String shortName,
            String directoryName, String homeUrl, int homeSiteId)
            throws OperationFailedException {
        int labId;
        LabInfo lab;

        // Generate a new lab id at random. Ensure it's unique.
        do {
            labId = random.nextInt(32766) + 1;
        } while (issuedLabs.get(new Integer(labId)) != null);

        // Write the Lab Activation message to file. It will be incorporated
        // into the outgoing recipnet.sitegrant file by the caller.
        lab = new LabInfo(labId, name, shortName, directoryName, homeUrl,
                homeSiteId);
        this.generateMessage(new LabActivationISM(lab));
        this.issuedLabs.put(labId, lab);

        return labId;
    }

    /**
     * Generates a SiteUpdateISM for the specified siteId. The SiteInfo nested
     * inside the SiteUpdateISM will be based upon any SiteInfo's that have
     * been broadcast previously from the Coordinator. The remaining parameters
     * to this function replace values in the corresponding fields in the
     * SiteInfo if they're not null. If the parameters to this function are
     * null, then the values that the Coordinator issued earlier are preserved.
     */
    public void updateExistingSite(int siteId, String name, String shortName,
            String baseUrl, String repositoryUrl, PublicKey publicKey)
            throws OperationFailedException {
        SiteInfo site = issuedSites.get(Integer.valueOf(siteId));
        if (site == null) {
            throw new RuntimeException("Site id " + siteId + " has not been"
                    + " issued.");
        }

        if (name != null) {
            site.name = name;
        }
        if (shortName != null) {
            site.shortName = shortName;
        }
        if (baseUrl != null) {
            site.baseUrl = baseUrl;
        }
        if (repositoryUrl != null) {
            site.repositoryUrl = repositoryUrl;
        }
        if (publicKey != null) {
            site.publicKey = publicKey;
        }

        this.generateMessage(new SiteUpdateISM(site));
        this.issuedSites.put(site.id, site);
    }

    public void deactivateExistingSite(int siteId, long finalSeqNum)
            throws OperationFailedException {
        SiteInfo site = issuedSites.get(siteId);

        if (site == null) {
            throw new RuntimeException("Site id " + siteId
                    + " has not been issued.");
        }
        this.generateMessage(new SiteDeactivationISM(siteId, finalSeqNum));
        this.issuedSites.remove(siteId);
    }

    public void reactivateExistingSite(int siteId) 
	    throws OperationFailedException, ResourceNotFoundException {
	SiteInfo site = this.issuedSites.get(siteId);
	if (site == null) {
	    throw new IllegalArgumentException();
	}

	site.isActive = true;

        this.generateMessage(new SiteActivationISM(site));
        issuedSites.put(new Integer(siteId), site);
    }

    public void updateExistingLab(int labId, int homeSiteId, boolean isActive,
            String name, String shortName, String directoryName,
            String homeUrl, String defaultCopyrightNotice)
            throws OperationFailedException {
        LabInfo lab = issuedLabs.get(Integer.valueOf(labId));
        if (lab == null) {
            throw new RuntimeException("Lab id " + labId + " has not been"
                    + " issued.");
        }

        if (homeSiteId != SiteInfo.INVALID_SITE_ID) {
            lab.homeSiteId = homeSiteId;
        }
        lab.isActive = isActive;
        if (name != null) {
            lab.name = name;
        }
        if (shortName != null) {
            lab.shortName = shortName;
        }
        if (directoryName != null) {
            lab.directoryName = directoryName;
        }
        if (homeUrl != null) {
            lab.homeUrl = homeUrl;
        }
        if (defaultCopyrightNotice != null) {
            lab.defaultCopyrightNotice = defaultCopyrightNotice;
        }

        this.generateMessage(new LabUpdateISM(lab,
                InterSiteMessage.RECIPROCAL_NET_COORDINATOR));
        this.issuedLabs.put(lab.id, lab);
    }

    /**
     * Generates private ISMs appropriate for deactivating an existing sample,
     * one to each existing site except the specified home site for the sample.
     * This behavior might be useful for recovery from an error situation in
     * which a site starts re-using sample IDs that it previously activated,
     * without having first deactivated them.  It is <em>essential</em> that
     * this method only be used for sample IDs that have been activated and not
     * subsequently deactivated, lest sites' further processing of
     * <strong>Coordinator</strong> messages be blocked.
     *  
     * @param sampleId the ID of the sample to deactivate
     * @param homeSiteId the ID of the sample's originating site 
     * @throws OperationFailedException
     */
    public void deactivateExistingSample(int sampleId, int homeSiteId)
            throws OperationFailedException {
        for (Integer siteId : issuedSites.keySet()) {
            if ((siteId.intValue() != homeSiteId) 
                    && (siteId.intValue()
                            != InterSiteMessage.RECIPROCAL_NET_COORDINATOR)) {
                SampleDeactivationISM ism = new SampleDeactivationISM(
                        InterSiteMessage.RECIPROCAL_NET_COORDINATOR, sampleId);
                
                ism.destSiteId = siteId.intValue();
                generateMessage(ism);
            }
        }
    }
    
    public int reserveSampleIdBlock() throws OperationFailedException {
        int blockId;
        do {
            // The odd numbers listed on the next line are constructed
            // so as to generate sample id's that are eight decimal digits
            // in length.
            blockId = this.random.nextInt(87886) + 9766;
        } while (this.reservedSampleIdBlocks.contains(blockId));
	this.claimSampleIdBlock(blockId);
	return blockId;
    }

    public void transferSampleIdBlock(int siteId)
            throws OperationFailedException {
        Integer blocks[] = reservedSampleIdBlocks.toArray(new Integer[0]);
        int i = this.random.nextInt(blocks.length);
        int blockId = blocks[i].intValue();
	//this.transferSampleIdBlock(blockId); -- yuma 2016-02-01 bug fix
	this.transferSampleIdBlock(siteId, blockId);
    }

    public void transferSampleIdBlock(int siteId, int blockId)
            throws OperationFailedException {
        this.generateMessage(SampleIdBlockISM.newTransferInitiate(
                InterSiteMessage.RECIPROCAL_NET_COORDINATOR, blockId, siteId));
        this.reservedSampleIdBlocks.remove(blockId);
        this.issuedSampleIdBlocks.add(blockId);
    }

    public void claimSampleIdBlock(int blockId) 
            throws OperationFailedException {
        this.reservedSampleIdBlocks.add(blockId);
        this.generateMessage(SampleIdBlockISM.newClaim(
                InterSiteMessage.RECIPROCAL_NET_COORDINATOR, blockId));
    }

    public void requestStatistics(int destSiteId, int collectionSiteId,
            long millisUntilExpiration, boolean resetCounters, 
            boolean forceBlankPrevSeqNum)
            throws OperationFailedException {
        this.generateMessage(new SiteStatisticsRequestISM(destSiteId,
                collectionSiteId, resetCounters, new Date(new Date().getTime()
		+ millisUntilExpiration)), forceBlankPrevSeqNum);
    }

    public void forceUpgrade(String version) throws OperationFailedException {
        this.generateMessage(new ForceUpgradeISM(version));
    }

    public void resetSeqNums(int destSiteId, int otherSiteId, 
	    long publicSeqNum, long privateSeqNum) 
            throws OperationFailedException {
	this.generateMessage(new SiteResetISM(
                InterSiteMessage.RECIPROCAL_NET_COORDINATOR, destSiteId, 
                otherSiteId, publicSeqNum, privateSeqNum));
    }

    public void initiateLabTransfer(int labId, int newHomeSiteId)
            throws OperationFailedException {
	LabInfo lab = this.issuedLabs.get(labId);
	if (lab == null) {
	    throw new IllegalArgumentException();
	}
	this.generateMessage(new LabTransferInitiateISM(labId, lab.homeSiteId,
		newHomeSiteId));
    }

    public static void printMsgpakSummary(File msgpakFile)
            throws InvalidDataException, MessageDecodingException,
            OperationFailedException {
        for (InterSiteMessage ism
                : MsgpakUtil.readAndDecodeAllMessages(msgpakFile)) {
            displayMessage(ism);
        }
    }

    public static void displayMessage(InterSiteMessage msg) {
        System.out.print(msg.sourceSeqNum + ": ");

        if (msg instanceof SiteGrantISM) {
            SiteGrantISM a = (SiteGrantISM) msg;
            System.out.println("SITE GRANT, site id " + a.destSiteId);
        } else if (msg instanceof SiteActivationISM) {
            SiteActivationISM b = (SiteActivationISM) msg;
            System.out.println("SITE ACTIVATION ANNC, site id " 
                    + b.newSite.id);
            System.out.println("        name='" + b.newSite.name + "'");
            System.out.println("        shortName='" + b.newSite.shortName
                    + "'");
            System.out.println("        baseUrl='" + b.newSite.baseUrl + "'");
            System.out.println("        repositoryUrl='"
                    + b.newSite.repositoryUrl + "'");
        } else if (msg instanceof SiteUpdateISM) {
            SiteUpdateISM b = (SiteUpdateISM) msg;
            System.out.println("SITE UPDATE ANNC, site id " 
                    + b.updatedSite.id);
            System.out.println("        name='" + b.updatedSite.name + "'");
            System.out.println("        shortName='" + b.updatedSite.shortName
                    + "'");
            System.out.println("        baseUrl='" + b.updatedSite.baseUrl
                    + "'");
            System.out.println("        repositoryUrl='"
                    + b.updatedSite.repositoryUrl + "'");
        } else if (msg instanceof SiteDeactivationISM) {
            SiteDeactivationISM b = (SiteDeactivationISM) msg;
            System.out.println("SITE DEACTIVATION ANNC, site id " 
                    + b.oldSiteId);
        } else if (msg instanceof LabActivationISM) {
            LabActivationISM c = (LabActivationISM) msg;
            System.out.println("LAB ACTIVATION ANNC, lab id " + c.newLab.id
                    + " at site id " + c.newLab.homeSiteId);
            System.out.println("        isActive=" + c.newLab.isActive);
            System.out.println("        name='" + c.newLab.name + "'");
            System.out.println("        shortName='" 
                    + c.newLab.shortName + "'");
            System.out.println("        directoryName='"
                    + c.newLab.directoryName + "'");
            System.out.println("        homeUrl='" + c.newLab.homeUrl + "'");
            System.out.println("        defaultCopyrightNotice='"
                    + c.newLab.defaultCopyrightNotice + "'");
        } else if (msg instanceof LabUpdateISM) {
            LabUpdateISM c = (LabUpdateISM) msg;
            System.out.println("LAB UPDATE ANNC, lab id " + c.updatedLab.id
                    + " at site id " + c.updatedLab.homeSiteId);
            System.out.println("        isActive=" + c.updatedLab.isActive);
            System.out.println("        name='" + c.updatedLab.name + "'");
            System.out.println("        shortName='" + c.updatedLab.shortName
                    + "'");
            System.out.println("        directoryName='"
                    + c.updatedLab.directoryName + "'");
            System.out.println("        homeUrl='" 
                    + c.updatedLab.homeUrl + "'");
            System.out.println("        defaultCopyrightNotice='"
                    + c.updatedLab.defaultCopyrightNotice + "'");
        } else if (msg instanceof LabTransferInitiateISM) {
	    LabTransferInitiateISM c = (LabTransferInitiateISM) msg;
	    System.out.println("LAB TRANSFER ANNC, lab id " + c.labId
		    + " transferred from site id " + c.previousHomeSiteId 
                    + " to " + c.newHomeSiteId);
	} else if (msg instanceof SampleIdBlockISM) {
            SampleIdBlockISM d = (SampleIdBlockISM) msg;
            switch (d.func) {
                case SampleIdBlockISM.CLAIM:
                    System.out.println("sample id block " + d.blockId
                            + " CLAIMED by site id " + d.sourceSiteId);
                    break;
                case SampleIdBlockISM.TRANSFER_INITIATE:
                    System.out.println("sample id block " + d.blockId
                            + " TRANSFER INITIATED by site id "
                            + d.sourceSiteId + " to site id " + d.otherSiteId);
                    break;
                default:
                    System.out.println("sample id block " + d.blockId
                            + " and func " + d.func);
            }
        } else if (msg instanceof SiteStatisticsRequestISM) {
            SiteStatisticsRequestISM ism = (SiteStatisticsRequestISM) msg;
            System.out.println("Site Statistics requested of site "
                    + ism.destSiteId + ", to be sent to site "
                    + ism.collectionSiteId + ".");
            System.out.println("Stats counters "
                    + (ism.resetCounters ? "WILL" : "WILL NOT") 
                    + " be reset.");
        } else if (msg instanceof ForceUpgradeISM) {
	    ForceUpgradeISM ism = (ForceUpgradeISM) msg;
	    System.out.println("forced upgrade to version '" 
                    + ism.version + "'");
	} else if (msg instanceof SiteResetISM) {
	    SiteResetISM ism = (SiteResetISM) msg;
	    System.out.println("reset sequence numbers at site id " 
                    + ism.destSiteId + " for third site id " + ism.otherSiteId
                    + " to " + ism.publicSeqNum + "," + ism.privateSeqNum);
	} else {
            System.out.println("unknown message type "
                    + msg.getClass().getName());
        }
    }

    public void pushNewMessages() throws OperationFailedException,
            ResourceException {
        System.out.println("Pushing newly-created messages to all partner"
                + " sites:");

        // Iterate through all the sites and push any new ISM's to each of
        // them in turn.
        for (SiteInfo site : this.issuedSites.values()) {
            if (site.baseUrl == null) {
                continue;
            }
            System.out.print("  " + site.name + "...");
	    this.pushNewMessages(site.id, site.baseUrl);
	}
    }

    public void pushNewMessages(int siteId) throws OperationFailedException {
	this.pushNewMessages(siteId, null);
    }

    public void pushNewMessages(int siteId, String baseUrl) 
	    throws OperationFailedException {
	ArrayList<String> ismsAsXml = new ArrayList<String>();
	this.messageFileAgent.readMessagesSuitableForRemoteSite(siteId,
                this.localSiteId, this.lastOldSeqNum, this.lastOldSeqNum,
                MessageFileAgent.NO_LIMIT, ismsAsXml);
	String ismsAsXmlArray[] = new String[ismsAsXml.size()];
	for (int i = 0; i < ismsAsXml.size(); i++) {
	    ismsAsXmlArray[i] = SoapUtil.dropXmlDocumentHeader(
                    ismsAsXml.get(i), "message");
	}
	if (baseUrl == null) {
	    baseUrl = this.issuedSites.get(siteId).baseUrl;
	}
	IsmExchanger exchanger = new IsmExchanger(10000, 10000);
	try {
	    exchanger.exchange(ismsAsXmlArray, baseUrl, true);
	    System.out.println("ok");
	} catch (RuntimeException ex) {
	    System.out.println("error: " + ex.toString());
	} catch (OperationFailedException ex) {
	    System.out.println("error pushing to site id " + siteId
                    + ": " + ex.toString());
        }
    }

    public void replayAllMessages(int targetSiteId) throws IOException,
            SAXException, Exception {
        SiteInfo targetSite = issuedSites.get(Integer.valueOf(targetSiteId));
        if (targetSite == null) {
            throw new RuntimeException("Unknown site id " + targetSiteId);
        }
	replayAllMessages(targetSite.id, targetSite.baseUrl);
    }

    public void replayAllMessages(int targetSiteId, String baseUrl)
	    throws IOException, SAXException, Exception {
        System.out.print("Pushing messages to site id " + targetSiteId + ", "
                + baseUrl + " ...");
        ArrayList<String> ismsAsXml = new ArrayList<String>();
        this.messageFileAgent.readMessagesSuitableForRemoteSite(targetSiteId,
                this.localSiteId, InterSiteMessage.INVALID_SEQ_NUM,
                InterSiteMessage.INVALID_SEQ_NUM, MessageFileAgent.NO_LIMIT,
                ismsAsXml);
        String ismsAsXmlArray[] = new String[ismsAsXml.size()];
        for (int i = 0; i < ismsAsXml.size(); i++) {
            ismsAsXmlArray[i] = SoapUtil.dropXmlDocumentHeader(
                    ismsAsXml.get(i), "message");
        }
        IsmExchanger exchanger = new IsmExchanger(10000, 10000);
        try {
            exchanger.exchange(ismsAsXmlArray, baseUrl, true);
        } catch (Exception ex) {
            System.out.println(exchanger.getLastServerErrorMessage());
            throw ex;
        }

        System.out.println(" done");
    }	

    private String generateMessage(InterSiteMessage ism)
	    throws OperationFailedException {
	return this.generateMessage(ism, false);
    }

    private String generateMessage(InterSiteMessage ism, 
            boolean forceBlankPrevSeqNum) throws OperationFailedException {
        // Modify the ISM's sequence number fields appropriately.
        ism.sourceSeqNum = highestSeqNum + 1;
	if (forceBlankPrevSeqNum) {
	    ism.sourcePrevSeqNum = InterSiteMessage.INVALID_SEQ_NUM;
	} else if (ism.isPublic() 
                && this.highestPublicSeqNumByDest.containsKey(
                ism.destSiteId)) {
            ism.sourcePrevSeqNum 
                    = this.highestPublicSeqNumByDest.get(ism.destSiteId);
	} else if (ism.isPrivate()
		&& this.highestPrivateSeqNumByDest.containsKey(
                ism.destSiteId)) {
	    ism.sourcePrevSeqNum
		    = this.highestPrivateSeqNumByDest.get(ism.destSiteId);
        } else {
            ism.sourcePrevSeqNum = InterSiteMessage.INVALID_SEQ_NUM;
        }

        // Update our state tables.
        this.highestSeqNum = ism.sourceSeqNum;
	if (ism.isPublic()) {
	    this.highestPublicSeqNumByDest.put(ism.destSiteId, 
                    ism.sourceSeqNum);
	} else {
	    this.highestPrivateSeqNumByDest.put(ism.destSiteId,
		    ism.sourceSeqNum);
	}

        // Cause the message to be generated. There's no need to invoke
        // MessageFileAgent.prepareSequenceNumbersForSentMessage() because we
        // just prepared the sequence numbers ourself.
        return this.messageFileAgent.generateSentMessage(ism, this.sigEngine);
    }
}
