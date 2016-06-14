/**
 * Reciprocal Net project
 * @(#)HttpPummeller.java
 *
 * 1-Nov-2004: ekoperda wrote first draft
 */

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A quick and dirty program for testing that initiates a bunch of HTTP
 * requests to a specific URL that the user specifies on the command line.  The
 * current implementation uses 10 simultaneous threads in an attempt to 
 * overwhelm the remote HTTP server.
 */
public class HttpPummeller {
    public static void main(String args[]) {
	if (args.length != 1) {
	    System.out.println("Usage: java [...] HttpPummeller <url>");
	    System.exit(1);
	}

	for (int i = 0; i < 10; i ++) {
	    Thread thread = new PummelThread(args[0]);
	    thread.start();
	}
	
	System.out.println("All threads launched.  Press Ctrl-C to exit.");
    }

    private static class PummelThread extends Thread {
	private String url;

	PummelThread(String url) {
	    this.url = url;
	}

	public void run() {
	    try {
		URL x = new URL(url);
		byte[] buffer = new byte[16394];
		while (true) {
		    long startingTime = System.currentTimeMillis();
		    HttpURLConnection conn 
			    = (HttpURLConnection) x.openConnection();
		    InputStream is = conn.getInputStream();
		    int rc;
		    int byteCount = 0;
		    do {
			rc = is.read(buffer);
			if (rc > 0) {
			    byteCount += rc;
			}
		    } while (rc > 0);
		    is.close();
		    int responseCode = conn.getResponseCode();
		    conn.disconnect();
		    long endingTime = System.currentTimeMillis();	    
		    System.out.println("Code " + responseCode + "; read "
                            + byteCount + " bytes in " 
                            + (endingTime - startingTime) + " ms");    
		}
	    } catch (Exception ex) {
		ex.printStackTrace();
	    }
	}
    }
}
