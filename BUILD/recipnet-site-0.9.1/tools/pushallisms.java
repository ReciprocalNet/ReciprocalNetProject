/**
 * Reciprocal Net project
 * pushallisms.java
 *
 * 29-Aug-2003: miduribn wrote first draft
 * 05-Apr-2004: midurbin fixed import statements for new package structure
 * 17-Jan-2009: ekoperda adjusted to accommodate new class IsmExchanger and run
 *              against 0.9.1 codebase
 */

import java.lang.StringBuffer;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import org.recipnet.site.shared.SoapUtil;
import org.recipnet.site.shared.db.SiteInfo;
import org.recipnet.site.OperationFailedException;
import org.recipnet.site.core.MessageDecodingException;
import org.recipnet.site.core.ResourceException;
import org.recipnet.site.core.msg.InterSiteMessage;
import org.recipnet.site.core.util.IsmExchanger;;
import org.xml.sax.SAXException;

public class pushallisms {
    /** 
     * The directory from which messages will be scanned and possibly sent.
     * This variable is set to equal the FIRST command line parameter.
     */
    private static String directory;

    /**
     * The site to which all messages will be sent.  This is used to determine
     * whether a particular message should be sent.
     * This variable is set to equal the SECOND command line parameter.
     */
    private static long targetSiteId;

    /**
     * This is the URL to which the ISM's should be pushed.
     * This variable is set to equal the THIIRD command line parameter.
     */
    private static String targetUrl;

    public static void main(String args[]) throws Throwable {
        System.err.println("Reciprocal Net site software");
        System.err.println("managed ISM push utility");
        System.err.println();
        if (args.length != 3) {
            System.err.println();
            System.err.println("  Usage: pushallisms (directory name)"
                    + " (siteId) (baseUrl)");
            return;
        }

        directory = args[0];
        targetSiteId = Long.parseLong(args[1]);
        targetUrl = args[2];

	StringBuffer buf = new StringBuffer();
        long msgsIncluded = 0;

	// Iterate through all sent-message files and build a collection of
	// ISM's to be sent.
	System.out.println();
	System.out.println("Scanning filesystem...");
	Collection<String> ismsToSend = new ArrayList<String>();
	for (File file : new File(directory).listFiles()) {
	    // Read and parse the file as an ISM
	    String msgAsXml = null;
	    InterSiteMessage msg;
	    try {
		msgAsXml = readStringFromFile(file);
		msg = InterSiteMessage.fromXml(msgAsXml);
	    } catch (IOException ex) {
		// Error reading this message file
		throw new ResourceException(file, ex);
	    } catch (SAXException ex) {
		// Error parsing this XML into an InterSiteMessage
		throw new MessageDecodingException(SiteInfo.INVALID_SITE_ID,
		        file.getName(), msgAsXml, ex);
	    }

	    // Skip ahead if this ISM is not needed by the target site
            if (msg.destSiteId != targetSiteId
                    && msg.destSiteId != InterSiteMessage.ALL_SITES) {
		continue;
	    }

	    // This is a good message.
	    ismsToSend.add(
                    SoapUtil.dropXmlDocumentHeader(msgAsXml, "message"));
            System.out.println("  " + file.getName() + " added.");
            msgsIncluded++;
	}

        if (msgsIncluded > 0) {
            System.out.println("Pushing " + msgsIncluded + " messages to site "
                    + targetSiteId + ".");
	    String[] ismsToSendAsArray = ismsToSend.toArray(new String[0]);
	    IsmExchanger ismExchanger = new IsmExchanger(30000, 30000);
            ismExchanger.exchange(ismsToSendAsArray, targetUrl, true);
        }
    }

    /**
     * Helper function that reads the specified file into memory (in entirety)
     * and returns a String that contains the file's data (decoded using UTF-8
     * charset).
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
}
