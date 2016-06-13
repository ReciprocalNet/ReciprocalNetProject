/*
 * Reciprocal Net project
 * ResetSeqNums.java
 *
 * 17-Jan-2009: ekoperda wrote first draft
 */

package org.recipnet.coordinator;
import java.io.File;
import org.recipnet.site.core.msg.InterSiteMessage;
import org.recipnet.site.core.msg.SiteResetISM;

/**
 * Command-line utility that generates a new SiteResetISM..  The message
 * then is transmitted to one particular site.
 */
public class ResetSeqNums {
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
	int destSiteId = 19946;
        coordinator.resetSeqNums(destSiteId, 
				 27682,
				 59,
				 InterSiteMessage.INVALID_SEQ_NUM);
	
	// Transmit the new ISM's.
	System.out.println("Sending announcement...");
	coordinator.pushNewMessages(destSiteId, null);

        System.out.println("Done.");
    }
}
