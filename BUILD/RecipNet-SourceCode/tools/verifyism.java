/**
 * Reciprocal Net project 
 * @(#)verifyism.java
 *
 * 10-Jan-2008: ekoperda wrote first draft
 */

import java.io.*;
import java.security.*;
import org.recipnet.site.core.msg.*;
import org.recipnet.site.core.agent.MessageFileAgent;

public class verifyism {
    public static void main(String args[]) throws Throwable {
        System.out.println("Reciprocal Net site software");
        System.out.println("manual ISM verification utility");
        System.out.println();
        if (args.length != 2) {
            System.out.println("  Usage: verifyism SiteActivationISM.xml"
                    + " OtherISM.xml");
            return;
        }

        try {
	    // Read the SiteGrantISM to glean the public key.
	    PublicKey publicKey = readSiteActivationIsm(new File(args[0]));
	    
	    // Dump the public key to file for verification purposes.
	    FileOutputStream fos = new FileOutputStream("publicKey.bin");
	    fos.write(publicKey.getEncoded());
	    fos.flush();
	    fos.close();

	    // Prepare the signature engine.
	    Signature sigEngine = Signature.getInstance("SHA1withDSA");
	    sigEngine.initVerify(publicKey);

	    // Read the target ISM from file.
	    String xmlDoc = readStringFromFile(new File(args[1]));
	    xmlDoc = xmlDoc.substring(xmlDoc.indexOf("<message"));

	    // Verify the signature.
	    InterSiteMessage targetIsm 
                   = InterSiteMessage.fromXmlCheckSignature(xmlDoc, sigEngine);
	    System.out.println("Decoded and validated " + targetIsm);
	    System.out.println("Complete!");
        } catch (Exception ex) {
	    ex.printStackTrace();
        }
    }

    private static PublicKey readSiteActivationIsm(File f) throws Exception {
	String xmlDoc = readStringFromFile(f);
	xmlDoc = xmlDoc.substring(xmlDoc.indexOf("<message"));
        InterSiteMessage msg = InterSiteMessage.fromXml(xmlDoc);
	SiteActivationISM siteActivationIsm = (SiteActivationISM) msg;
	return siteActivationIsm.newSite.publicKey;
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
