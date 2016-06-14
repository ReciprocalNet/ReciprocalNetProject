/*
 * Reciprocal Net project
 * 
 * RequestStats.java
 *
 * 04-May-2004: midurbin wrote first draft
 * 06-Jun-2006: jobollin reformatted the source
 * 25-Jan-2009: ekoperda added workaround for bug #1911 related to ISMs'
 *              sourcePrevSeqNum field
 */

package org.recipnet.coordinator;

import java.io.File;
import java.security.GeneralSecurityException;

import org.recipnet.site.RecipnetException;

/**
 * Command-line utility that generates a new SiteStatisticsRequestIsm. This ISM
 * is written to file, and pushed.
 */
public class RequestStats {
    public static void main(String args[]) throws GeneralSecurityException,
            RecipnetException {
        Coordinator coordinator;

        System.out.println("Initializing...");
        coordinator = new Coordinator(new File("conf/"), new File("msgs-sent/"));

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

        System.out.println("Create statistics request message...");
        coordinator.requestStatistics(

                // insert siteId from which you wish to request stats
                18058,

                // insert siteId to which you wish stats be sent
                395,

                /*
                 * the number of milliseconds from now until request expires
                 * (currently 1 day -- be careful about truncation to type int)
                 */
                1000 * 60 * 60 * 24,

                // indicate whether you wish the stats counters to be reset
                false,

		// force generation of an ISM with a blank prevSeqNum field.
		// This is useful when sending the first Coordinator-private
		// ISM to a site, and only until bugfix #1911 is deployed.
		false);

        coordinator.pushNewMessages(18058, null);

        System.out.println("Done.");
    }
}
