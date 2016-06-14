/*
 * Reciprocal Net project
 * 
 * ForceUpgrade.java
 *
 * 28-Nov-2008: ekoperda wrote first draft
 */

package org.recipnet.coordinator;
import java.io.File;

/**
 * Command-line utility that generates a new ForceUpgradeISM.  The message
 * then is transmitted to all sites.
 */
public class ForceUpgrade {
    public static void main(String args[]) throws Exception {
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
        coordinator.forceUpgrade("0.9.1-900");
	
	// Transmit the new ISM's.
	System.out.println("Sending announcement...");
	coordinator.pushNewMessages();

        System.out.println("Done.");
    }
}
