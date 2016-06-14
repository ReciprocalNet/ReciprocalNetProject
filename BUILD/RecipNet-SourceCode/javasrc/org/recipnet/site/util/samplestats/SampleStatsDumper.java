/*
 * Reciprocal Net project
 *  
 * SampleStatsDumper.java
 *
 * 17-Dec-2002: ekoperda wrote first draft
 * 07-Jan-2004: ekoperda changed package references to match source tree
 *              reorganization
 * 05-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.site.util.samplestats;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.rmi.Naming;
import java.util.Properties;
import org.recipnet.site.core.SampleManagerRemote;
import org.recipnet.site.shared.SampleStats;

/**
 * <p>
 * A simple command-line utility that connects to Sample Manager via RMI and
 * fetches the most recent SampleStats container object (optionally resetting
 * Sample Manager's counters at the same time); dumps all the counter values to
 * stdout for users to examine.
 * </p><p>
 * Syntax is: samplestats --configfile=... [--reset]
 * </p>
 */
public class SampleStatsDumper {
    public static void main(String args[]) {
        try {
            
            /*
             * The first command-line parameter tells us where to find our
             * configuration file. It should always be of the form:
             *      --configfile=/etc/filename
             * The second command-line argument is optional, but if specified it
             * should be
             *      --reset
             */
            if (args.length < 1 || args.length > 2
                    || !args[0].startsWith("--configfile=")) {
                displaySyntax();
                System.exit(1);
            }
            String configFile = args[0].substring("--configfile=".length());
            boolean shouldResetCounters = false;
            if (args.length == 2) {
                if (!args[1].equals("--reset")) {
                    displaySyntax();
                    System.exit(1);
                }
                shouldResetCounters = true;
            }

            // Read and parse the configuration file.
            BufferedInputStream is = new BufferedInputStream(
                    new FileInputStream(configFile));
            Properties properties = new Properties();
            properties.load(is);
            is.close();

            // Connect to recipnetd via RMI.
            SampleManagerRemote sampleManager
                    = (SampleManagerRemote) Naming.lookup("//"
                            + properties.getProperty("SitHostName")
                            + "/"
                            + properties.getProperty("SamRmiName"));

            // Fetch and reset Sample Manager's statistics
            SampleStats stats = sampleManager.getStats();
            if (shouldResetCounters) {
                sampleManager.resetStats();
            }
            stats.printToStream(System.out);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(2);
        }
    }

    private static void displaySyntax() {
        System.out.println("Usage:"
                + " recipnet-samplestats --configfile=... [--reset]");
    }
}
