/*
 * Reciprocal Net project
 * 
 * CreateSiteGrant.java
 *
 * 11-Jul-2002: ekoperda wrote first draft
 * 30-Sep-2002: ekoperda updated whole class to support XML-format ISM's and 
 *              corresponding restructuring in the site software
 * 06-Jun-2006: jobollin reformatted the source
 * 04-Jul-2008: ekoperda removed sample id block transfer logic
 */

package org.recipnet.coordinator;
import java.io.File;
import java.security.GeneralSecurityException;
import org.recipnet.site.RecipnetException;

/**
 * Command-line utility that creates a new site grant file
 * ('recipnet.sitegrant') for a new site. This only works once the Reciprocal
 * Net has been initialized and this coordinator has initialized itself by
 * calling CreateCoordinatorSiteGrant. <p>
 *
 * Several logical arguments are hard-coded as strings within this class,
 * including the site's name(64 chars), short name(20 chars), base URL (128
 * chars), and repository URL (128 chars). The recipnet.sitegrant file is
 ( written to the current directory, which should be the base directory of some
 * Coordinator data files. <p>
 *
 * Note that the current implementation does not transfer any sample id blocks
 * from the Coordinator to the new site.  This is because, as of site software
 * 0.9.1, newly-initiated sites are able to borrow and propose sample id
 * blocks on their own.
 */
public class CreateSiteGrant {
    public static void main(String args[]) throws GeneralSecurityException,
            RecipnetException {
        System.out.println("Initializing...");
        Coordinator coordinator = new Coordinator(new File("conf/"), 
                new File("msgs-sent/"));

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

        System.out.println("Creating site grant...");
        int newSiteId = coordinator.beginCreatingNewSite("Test Server 113",
                "testserver113",
                "http://129.114.18.19/recipnet/",
                "http://129.114.18.19/recipnet/data/");

        System.out.println("Creating lab grant...");
        coordinator.createNewLab("Lab Main", "labmain", "labmain",
                "http://www.iusmc.indiana.edu/", newSiteId);

	// Optionally give the new site some sample id blocks.
	for (int i = 0; i < 4; i ++) {
	    coordinator.transferSampleIdBlock(newSiteId);
	}

        System.out.println("Packing messages into file"
                + " 'recipnet.sitegrant'...");
        coordinator.finishCreatingNewSite(new File("recipnet.sitegrant"));

        System.out.println("Sending announcement...");
        coordinator.pushNewMessages();

        System.out.println("Done.");
    }
}
