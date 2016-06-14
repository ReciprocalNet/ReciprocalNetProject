/*
 * Reciprocal Net project
 * 
 * DumpMsgs.java
 *
 * 27-Aug-2002: ekoperda wrote first draft
 * 06-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.coordinator;

import java.io.File;
import java.security.GeneralSecurityException;

import org.recipnet.site.RecipnetException;

/**
 * Command-line utility that displays a list of all the ISM's that the
 * coordinator has transmitted since the beginning of time (or of the Reciprocal
 * Net, whichever is later). If an optional command-line argument is specified
 * then messages are read from the specified msgpak file instead of from the
 * sent-msgs directory. This might be useful for debugging.
 */
public class DumpMsgs {
    public static void main(String args[]) throws RecipnetException,
            GeneralSecurityException {
        if (args.length > 0) {
            dumpFromMsgpak(args[0]);
        } else {
            dumpOldMessages();
        }
    }

    static void dumpOldMessages() throws GeneralSecurityException,
            RecipnetException {
        Coordinator coordinator;

        System.out.println("Initializing...");
        coordinator = new Coordinator(new File("conf/"), new File("msgs-sent/"));

        System.out.println("Reading coordinator's site grant...");
        coordinator.initializeFromGrant();

        System.out.println("Processing past sent messages...");
        coordinator.initializeFromOldMessages(true);

        System.out.println("    To date, the coordinator has issued "
                + coordinator.getSitesIssued() + " sites, "
                + coordinator.getLabsIssued() + " labs, and "
                + coordinator.getSampleIdBlocksIssued());
        System.out.println("    sample id blocks ("
                + coordinator.getSampleIdBlocksReserved() + " reserved).");
    }

    static void dumpFromMsgpak(String filespec) throws RecipnetException {
        Coordinator.printMsgpakSummary(new File(filespec));
    }
}
