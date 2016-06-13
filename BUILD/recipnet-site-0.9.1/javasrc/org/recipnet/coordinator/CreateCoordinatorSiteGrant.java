/*
 * Reciprocal Net project
 * 
 * CreateCoordinatorSiteGrant.java
 *
 * 11-Jul-2002: ekoperda wrote first draft
 * 30-Sep-2002: ekoperda updated whole class to support XML-format ISM's and
 *              corresponding restructuring in the site software
 * 07-Jan-2004: ekoperda changed package references to match source tree
 *              reorganization
 * 06-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.coordinator;

import java.io.File;
import java.security.GeneralSecurityException;

import org.recipnet.site.RecipnetException;

/**
 * Command-line utility that creates a recipnet.sitegrant file for the
 * Reciprocal Net coordinator to use. This is the very first operation that must
 * be undertaken when a Reciprocal Net is first created, and shouldn't ever be
 * performed more than once. Be very careful using this!!!! This utility should
 * be run from the coordinator's base directory. The 'msgs-sent' directory is
 * cleared and the 'recipnet.sitegrant' file in the 'conf' directory is
 * overwritten.
 */
public class CreateCoordinatorSiteGrant {
    public static void main(String args[]) throws GeneralSecurityException,
            RecipnetException {
        System.out.println("Initializing...");
        Coordinator coordinator
                = new Coordinator(new File("conf/"), new File("msgs-sent/"));

        System.out.println("Creating coordinator's site grant...");
        coordinator.initializeByCreatingGrant("Reciprocal Net coordinator",
                "Coordinator");

        System.out.println("Processing past sent messages...");
        coordinator.initializeFromOldMessages();

        /*
         * Create a bunch of message that specify which sample id blocks the
         * coordinator is reserving for itself. These blocks can be assigned to
         * other sites at a later time, as those are created.
         */
        System.out.println("Reserving initial sample id blocks...");
        for (int i = 0; i < 128; i++) {
            coordinator.reserveSampleIdBlock();
        }

        System.out.println("Done.");
    }
}
