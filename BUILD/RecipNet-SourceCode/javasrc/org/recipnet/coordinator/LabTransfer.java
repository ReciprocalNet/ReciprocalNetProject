/*
 * Reciprocal Net project
 * LabTransfer.java
 *
 * 02-Jan-2008: ekoperda wrote first draft
 */

package org.recipnet.coordinator;
import java.io.File;

/**
 * Command-line utility that generates a new LabTransferInitiateISM.  The 
 * message then is transmitted to all sites.
 */
public class LabTransfer {
    public static void main(String args[]) throws Exception {
	// Initialize the Coordinator.
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

	// Do something useful.
        coordinator.initiateLabTransfer(16716, 18744);
	
	// Transmit the new ISM's.
	System.out.println("Sending announcement...");
	coordinator.pushNewMessages();

        System.out.println("Done.");
    }
}
