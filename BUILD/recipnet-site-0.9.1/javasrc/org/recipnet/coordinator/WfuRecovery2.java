/*
 * Reciprocal Net project
 * WfuRecovery.java
 *
 * 02-Jan-2009: ekoperda wrote first draft
 */

package org.recipnet.coordinator;
import java.io.File;
import org.recipnet.site.shared.db.SiteInfo;

/**
 * This console program is the second of a set of two designed to be executed 
 * against the production Reciprocal Net Site Network following release 0.9.1.
 * Together the two programs transmit a number of ISM's that, when processed by
 * the sites of the Site Network, will resolve the quagmire and confusion
 * related to the Wake Forest University site that was deactivated in 2005 (id
 * 29168).  The site software in effect at the time did not completely support
 * site deactivations, but such support was added in release 0.9.1.
 *
 * This program #2 should be run after program #1 finishes, and after the
 * replication state at Reciprocalnet.org indicates that site has re-processed
 * all the old WFU site's messages.  (The publicSeqNum should be 28, the magic
 * number.)  This program is partitioned from program #1 in order to allow time
 * for ReciprocalNet.org to pull and re-process WFU's old messages, as this is
 * a crucial step in the corrective process.
 */
public class WfuRecovery2 {
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

	// Re-transfer the WFU lab to Reciprocalnet.org, this time using
	// new-for-0.9.1 semantics.
	coordinator.initiateLabTransfer(25044, 395);

	// Re-deactivate the lab record.  (Why does the active flag sometimes
	// flip back to true, seemingly spontaneously?)
        coordinator.updateExistingLab(25044, SiteInfo.INVALID_SITE_ID, false,
                null, null, null, null, null);

	// Re-deactivate the site record, this time using new-for-0.9.1
	// semantics.
	coordinator.deactivateExistingSite(29168, 28);

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