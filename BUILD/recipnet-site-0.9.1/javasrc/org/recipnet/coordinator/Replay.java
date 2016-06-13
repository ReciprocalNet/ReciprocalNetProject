/*
 * Reciprocal Net project
 * Replay.java
 *
 * 11-Sep-2002: ekoperda wrote first draft
 * 06-Jun-2006: jobollin reformatted the source
 * 31-Dec-2008: ekoperda added handling of command-line arguments
 */

package org.recipnet.coordinator;
import java.io.File;

/**
 * A command-line utility program that sends all previously-generated ISM's to
 * a specified remote site or some other URL.  This might be useful if that
 * site missed these ISM's the first time, or for debugging.
 */
public class Replay {
    public static void main(String args[]) throws Exception {
	// Validate the arguments.
	if (args.length < 1 || args.length > 2) {
	    System.out.println("Improper usage!  Try one of these:");
	    System.out.println("    Replay <site id>");
	    System.out.println("    Replay <site id> <base url>");
	    return;
	}

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

	// Replay some ISM's.
	int siteId = Integer.parseInt(args[0]);
	if (args.length == 1) {
	    coordinator.replayAllMessages(siteId);
	} else {
	    String baseUrl = args[1];
	    coordinator.replayAllMessages(siteId, baseUrl);
	}

        System.out.println("Done.");
    }
}
