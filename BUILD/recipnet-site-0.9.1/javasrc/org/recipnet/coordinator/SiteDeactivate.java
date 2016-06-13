/*
 * Reciprocal Net project
 * SiteDeactivate.java
 *
 * 05-May-2005: ekoperda wrote first draft
 * 06-Jun-2006: jobollin reformatted the source
 * 30-Nov-2008: ekoperda revised to match  new SiteDeactivationISM conventions
 */

package org.recipnet.coordinator;

import java.io.File;

/**
 * Command-line utility that generates a new LabUpdateISM and then a
 * SiteDeactivationISM.
 */
public class SiteDeactivate {
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

	/*
	 * Sometimes we may need to update a lab record, but the specifics are
	 * not clear at this time.
	 * TODO: revise this block upon completion of task #1597
        System.out.println("Creating lab update message...");
        coordinator.updateExistingLab(
                25044,
                395,
                false,
                "Wake Forest University X-Ray Facility, 2004-2005 (deactivated)",
                "WFUx05", "wfu-deactivated-2005", null, null);
	*/

        System.out.println("Creating site deactivation message...");
        coordinator.deactivateExistingSite(428, 23);

        coordinator.pushNewMessages();

	// TODO: shouldn't we transmit this ISM to the newly-deactivated site
	// somehow???

        System.out.println("Done.");
    }
}
