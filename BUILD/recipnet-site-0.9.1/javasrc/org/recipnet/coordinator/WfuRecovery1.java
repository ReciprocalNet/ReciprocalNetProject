/*
 * Reciprocal Net project
 * WfuRecovery.java
 *
 * 02-Jan-2009: ekoperda wrote first draft
 */

package org.recipnet.coordinator;
import java.io.File;

/**
 * This console program is the first of a set of two designed to be executed 
 * against the production Reciprocal Net Site Network following release 0.9.1.
 * Together the two programs transmit a number of ISM's that, when processed by
 * the sites of the Site Network, will resolve the quagmire and confusion
 * related to the Wake Forest University site that was deactivated in 2005 (id
 * 29168).  The site software in effect at the time did not completely support
 * site deactivations, but such support was added in release 0.9.1.
 *
 * This program #1 should be run before program #2, as soon after release 0.9.1
 * and the Reciprocalnet.org's upgrade to 0.9.1 as desired.
 */
public class WfuRecovery1 {
    public static void main(String args[]) throws Exception {
	// Initialize the Coordinator object.
	System.out.println("Initializing...");
	Coordinator coordinator 
	        = new Coordinator(new File("conf/"), new File("msgs-sent/"));
        System.out.println("Reading coordinator's site grant...");
        coordinator.initializeFromGrant();
        System.out.println("Processing past sent messages...");
        coordinator.initializeFromOldMessages();
        System.out.println("    To date, the coordinator has issued "
                + coordinator.getSitesIssued() + " sites, "
                + coordinator.getLabsIssued() + " labs, and "
                + coordinator.getSampleIdBlocksIssued());
        System.out.println("    sample id blocks ("
                + coordinator.getSampleIdBlocksReserved() + " reserved).");

	// Transmit a ForceUpgradeISM.  Pre-0.9.1 sites will not be able to 
	// process this ISM and thus their processing will stall.
	System.out.println("Generating corrective ISM's...");
	coordinator.forceUpgrade("0.9.1-34");

	// Re-claim for ourselves the sample id blocks we transferred to WFU
	// several years ago.
	coordinator.claimSampleIdBlock(21859);
	coordinator.claimSampleIdBlock(35727);
	coordinator.claimSampleIdBlock(83281);
	coordinator.claimSampleIdBlock(97638);

	// Re-activate the site record.
	coordinator.reactivateExistingSite(29168);
	coordinator.updateExistingSite(29168, 
		"Wake Forest University 2004-2005 (deactivated)", 
                "WFU 2004-2005", null, null, null);

	// Transfer the WFU lab back to the WFU site and mark it inactive.
	coordinator.updateExistingLab(25044, 29168, false, null, null, null, 
	        null, null);

	// Re-transfer to WFU that same sample id blocks we did several years
	// ago.
	coordinator.transferSampleIdBlock(29168, 21859);
	coordinator.transferSampleIdBlock(29168, 35727);
	coordinator.transferSampleIdBlock(29168, 83281);
	coordinator.transferSampleIdBlock(29168, 97638);

	// Transmit the new ISM's.
	// TODO: revise the following statement to broadcast the ISM's to all
	//       sites.  The present code transmits them only to sites involved
	//       in debugging.
	System.out.print("Transmitting new ISM's... ");
	coordinator.pushNewMessages(11563, 
                "http://bl-chem-iumsc113.chem.indiana.edu/recipnet/");
	coordinator.pushNewMessages(395, 
                "http://129.79.85.114/recipnet/");
        //coordinator.pushNewMessages();

        System.out.println("Done.");
    }
}