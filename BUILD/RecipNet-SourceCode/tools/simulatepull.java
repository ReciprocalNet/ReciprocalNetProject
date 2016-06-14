/**
 * Reciprocal Net project
 * @(#)simulatepull.java
 *
 * 28-Apr-2004: miduribn wrote first draft
 */

import java.io.File;
import java.security.PrivateKey;
import java.security.Signature;
import org.recipnet.site.core.agent.MessageFileAgent;
import org.recipnet.site.core.msg.SiteGrantISM;
import org.recipnet.site.core.msg.ReplayRequestISM;
import org.recipnet.site.core.util.IsmPuller;

public class simulatepull {

    /** Used to sign the ReplayRequestISMm, gleaned from the sitegrant */
    private static PrivateKey localPrivateKey;
    /** also gleaned from the sitegrant */
    private static int localSiteId;

    /** 2nd command line parameter */
    private static int targetSiteId;

    /** 3rd command line parameter */
    private static String targetUrl;

    /** 4th command line parameter (optional) */
    private static long publicSeqNum = ReplayRequestISM.INVALID_SEQ_NUM;

    /** 5th command line parameter (optional) */
    private static long privateSeqNum = ReplayRequestISM.INVALID_SEQ_NUM;

    /** Used to sign the message. */
    private static Signature sigEngine;

    private static ReplayRequestISM replayRequest;
    private static String requestAsXml;

    public static void main(String args[]) throws Throwable {
        System.err.println("Reciprocal Net site software");
        System.err.println("manual ISM pull debugger");
        System.err.println();

        if (args.length != 3 && args.length != 5) {
            System.out.println("  Usage: simulatepull (site grant file)"
                    + " (target site id) (target site base URL)");
            System.out.println("         (optional: public sequence number)");
            System.out.println("         (optional: private sequence number)");
            return;
        }

        System.out.println("Generation ReplayRequestISM:");

        // get info from sitegrant file
        try {
            SiteGrantISM siteGrantISM
                    = MessageFileAgent.readSiteGrantFromMsgpak(
                            new File(args[0]));
            localPrivateKey = siteGrantISM.privateKey;
            localSiteId = siteGrantISM.destSiteId;
            System.out.println("  ..from siteId " + localSiteId);
        } catch (Exception ex) {
            System.err.println("Error retrieving private key from sitegrant"
                    + " file (" + args[0] + ")!");
            ex.printStackTrace(System.err);
            return;
        }

        try {
            targetSiteId = Integer.parseInt(args[1]);
            System.out.println("  ..to siteId " + targetSiteId);
        } catch (Exception ex) {
            System.err.println("Error parsing \"" + args[1]
                    + "\" into a targetSiteId!");
            return;
        }
        try {
            publicSeqNum = Long.parseLong(args[3]);
            System.out.println("  ..requesting public messages starting"
                    + " with seqNum " + publicSeqNum);
        } catch (Exception ex) {
            System.out.println("  ..requesting all public messages");
        }
        try {
            privateSeqNum = Long.parseLong(args[4]);
            System.out.println("  ..requesting private messages starting"
                    + " with seqNum " + privateSeqNum);
        } catch (Exception ex) {
            System.out.println("  ..requesting all private messages");
        }

        replayRequest = new ReplayRequestISM(localSiteId, targetSiteId,
                privateSeqNum);

        replayRequest.addToRequest(targetSiteId, publicSeqNum);

        // sign message
        try {
            sigEngine = Signature.getInstance("SHA1withDSA");
        } catch (Exception ex) {
            System.err.println("Error initializing signature engine!");
            return;
        }
        try {
            sigEngine.initSign(localPrivateKey);
            requestAsXml = replayRequest.toXmlAddSignature(sigEngine);
        } catch (Exception ex) {
            System.err.println("Error signging ISM!");
            ex.printStackTrace(System.err);
            return;
        }

        System.out.println("  ..complete!");

        System.out.println();
        System.out.println(requestAsXml);

        targetUrl = args[2];

        IsmPuller ismPuller = new IsmPuller(-1, -1);
        System.out.println();
        System.out.println();
        System.out.println("Initiating pull request from site " + targetSiteId
                + ", from the address \"" + targetUrl + "\".");

        try {
            String msgs[] = ismPuller.pullMessages(requestAsXml, targetUrl);
            System.out.println("Success: " + msgs.length
                    + " messages received.");
            
        } catch (Exception ex) {
            System.err.println("Failure: ");
            System.err.println(ismPuller.getLastServerErrorMessage());
            System.err.println();
            ex.printStackTrace(System.err);
        }
    }

}
