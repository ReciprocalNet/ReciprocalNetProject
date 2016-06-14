/**
 * Reciprocal Net project 
 * @(#)signism.java
 *
 * 14-Sep-2003: midurbin wrote first draft
 * 05-Apr-2004: midurbin fixed import statements for new package structure
 * 02-May-2008: ekoperda did maintenance coding to accommodate new class
 *              MsgpakUtils
 */

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.PrivateKey;
import java.security.Signature;
import org.recipnet.site.core.msg.InterSiteMessage;
import org.recipnet.site.core.util.MsgpakUtil;
import org.recipnet.site.shared.DomUtil;

public class signism {

    /**
     * The XML file that is to be signed.  This file will be overwritten by 
     * a digitally signed version of the same XML file.
     */
    private static File messageFile;

    /** Used to sign the message. */
    private static Signature sigEngine;

    /** 
     * The private key, retrieved from the sitegrant file, used to sign the
     * ISM.
     */
    private static PrivateKey localPrivateKey;

    public static void main(String args[]) throws Throwable {
        System.out.println("Reciprocal Net site software");
        System.out.println("manual ISM signing utility");
        System.out.println();
        if (args.length != 2) {
            System.out.println("  Usage: signism (ismfile.xml)"
                    + " (siteGrantfile)");
            return;
        }

        try {
            sigEngine = Signature.getInstance("SHA1withDSA");
        } catch (Exception ex) {
            System.err.println("Error initializing signature engine!");
            return;
        }

        // get private key from site grant
        try {
            localPrivateKey = MsgpakUtil.readAndDecodeSiteGrant(
                    new File(args[1])).privateKey;
        } catch (Exception ex) {
            System.err.println("Error retrieving private key from sitegrant"
                    + " file (" + args[1] + ")!");
            ex.printStackTrace(System.err);
            return;
        }

        String xmlDoc = null;
        try {
            messageFile = new File(args[0]);
            xmlDoc = readStringFromFile(messageFile);
	    xmlDoc = xmlDoc.substring(xmlDoc.indexOf("<message"));
        } catch (Exception ex) {
            System.err.println("Error reading XML from file '" + args[0]
                    + "'!");
            return;
        }
        InterSiteMessage msg = null;
        try {
            msg = InterSiteMessage.fromXml(xmlDoc);
        } catch (Exception ex) {
            System.err.println("Error generating ISM from XML!");
            ex.printStackTrace(System.err);
            return;
        }

        try {
            sigEngine.initSign(localPrivateKey);
            xmlDoc = msg.toXmlAddSignature(sigEngine);
            writeStringToFile(xmlDoc, messageFile);
        } catch (Exception ex) {
            System.err.println("Error signging ISM!");
            ex.printStackTrace(System.err);
            return;
        }
        System.out.println("Completed!");
    }

    /**
     * Helper function that reads the specified file into memory (in entirety)
     * and returns a String that contains the file's data (decoded using UTF-8
     * charset).  (This function is copied from MessageFileAgent)
     */
    private static String readStringFromFile(File f) throws IOException {
	FileInputStream is = new FileInputStream(f);
	byte data[] = new byte[(int) f.length()];
	int bytesRead = 0;
	do {
	    bytesRead += is.read(data, bytesRead, data.length - bytesRead);
	} while (bytesRead < data.length);
	is.close();
	return new String(data, "UTF-8");
    }

    /**
     * Helper function that writes the specified string into the specified new
     * file (encoded using UTF-8 character set). (This function is copied from
     * MessageFileAgent)
     */
    private static void writeStringToFile(String s, File f)
            throws IOException {
	BufferedOutputStream os = new BufferedOutputStream(
                new FileOutputStream(f));
	os.write(s.getBytes("UTF-8"));
	os.close();
    }
}
