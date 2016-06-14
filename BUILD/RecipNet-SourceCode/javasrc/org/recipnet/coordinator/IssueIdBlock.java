/*
 * Reciprocal Net project
 * 
 * IssueIdBlock.java
 *
 * 29-Oct-2002: ekoperda wrote first draft
 * 07-Jan-2004: ekoperda changed package references to match source tree
 *              reorganization
 * 06-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.coordinator;

import java.io.File;
import java.security.GeneralSecurityException;

import org.recipnet.site.RecipnetException;

/**
 * Command-line utility that issues one sample id block to a specified site.
 */
public class IssueIdBlock {
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

        System.out.println("Create sample id block message...");
        coordinator.transferSampleIdBlock(14854);
        coordinator.pushNewMessages();

        System.out.println("Done.");

    }
}
