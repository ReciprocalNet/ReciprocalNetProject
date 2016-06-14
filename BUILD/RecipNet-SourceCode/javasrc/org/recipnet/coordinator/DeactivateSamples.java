/*
 * Reciprocal Net Project
 *
 * DeactivateSamples.java
 *
 * 16-Jun-2006: jobollin wrote first draft
 */

package org.recipnet.coordinator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collection;

import org.recipnet.site.RecipnetException;

/**
 * A program to deactivate samples.  Because the Coordinator does not actually
 * have any samples of its own, this program is necessarilly using the
 * privileges of the Coordinator to cause samples belonging to some other site
 * to be deactivated.  Such an action is fraught with danger of site network
 * corruption; the only legitimate reason for using this program is to
 * repair <em>existing</em> site network corruption.
 * 
 * @author jobollin
 * @version 0.9.0
 */
public class DeactivateSamples {

    /**
     * This is the program entry point.  It interprets its first argument as the
     * ID of the site to which the samples to be deactivated belong, and its
     * second argument as the path to a file which lists the sample IDs to be
     * deactivated, one per line.  This program then generates and sends
     * a corresponding <em>private</em> site deactivation ISM for each listed
     * sample to each known site other than the one specified as owning the
     * samples.  Note that at this version, no RepositoryHoldingISMs are
     * generated; this may leave the site network in a slightly inconsistent
     * state, but it can probably be ignored.
     * 
     * @param args a {@code String[]} containing the arguments to the program
     * @throws GeneralSecurityException 
     * @throws RecipnetException 
     * @throws IOException 
     */
    public static void main(String[] args) throws GeneralSecurityException,
            RecipnetException, IOException {
        int sampleSiteId = Integer.parseInt(args[0]);
        File sampleIdFile = new File(args[1]);
        Collection<Integer> sampleIdList = new ArrayList<Integer>();
        
        if (!sampleIdFile.exists()) {
            System.err.println("File " + args[1] + " does not exist.");
            System.exit(1);
        } else {
            BufferedReader idReader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(sampleIdFile)));
            
            for (String line = idReader.readLine(); line != null;
                    line = idReader.readLine()) {
                sampleIdList.add(Integer.valueOf(line.trim()));
            }
        }
        
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

        System.out.println("Creating sample deactivation messages...");
        for (Integer sampleId : sampleIdList) {
            coordinator.deactivateExistingSample(sampleId, sampleSiteId);
        }

        System.out.println("Sending sample deactivation messages...");
        coordinator.pushNewMessages();

        System.out.println("Done.");
    }
}
