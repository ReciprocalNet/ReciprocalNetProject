/*
 * Reciprocal Net project
 * @(#)ModRewriteAuthConnector.java
 *
 * 01-Jan-2008: ekoperda wrote first draft
 * 03-Jan-2008: ekoperda improved error handling in processOneRequest() and
 *              checkAccess()
 * 18-Mar-2009: ekoperda improved error handling in processOneRequest() to fix
 *              bug #1913
 */

package org.recipnet.site.wrapper;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.recipnet.site.OperationFailedException;
import org.recipnet.site.core.RepositoryManager;
import org.recipnet.site.core.RepositoryManagerRemote;

/**
 * ModRewriteAuthConnector is a console-driven, command-line program designed
 * to be invoked by Apache Httpd's mod_rewrite module.  Its function is to
 * control access to the repository at a site; Httpd consults this module to
 * determine whether a particular HTTP request for a file within the repository
 * should be allowed or denied.  In making its decision, this module in turn
 * consults Repository.verifyTicket() via an RMI connection to core.  The
 * functionality offered by this program replaces that formerly offered by
 * mod_recipnet_auth.
 *
 * In configuring Httpd's mod_rewrite, this program is designed to be
 * referenced in httpd.conf via a RewriteMap directive.  For example:
 *
 *     RewriteMap foo prg:/usr/bin/recipnet-authconnector
 *
 * The specific protocol obeyed by this program is the one described at
 * http://httpd.apache.org/docs/2.2/mod/mod_rewrite.html#rewritemap , under
 * "External Rewriting Program."  Specifically, this program listens on stdin
 * for one line at a line.  Each line contains a request path and query string,
 * for example:
 *
 *     /recipnet/data/lab111/47536963/50007.gif?ticket=1715262723
 * 
 * .  In response, this program writes one line to stdout.  That line is one of
 * two strings, either:
 *
 *     1. the same string that was received on stdin, if access is granted.
 *     2. the special string "RECIPNET_FORBIDDEN", if access is denied.
 *
 * It is expected that mod_rewrite will be configured in such a way as to
 * detect this program's emission of RECIPNET_FORBIDDEN and in turn send a 403
 * error code to the browser.
 *
 * If this program encounters any technical difficulties, access is denied and
 * appropriate error messages are written to stderr.  Typically, httpd then
 * routes those messages through its own error facilities, and they show up in
 * logfile /var/log/httpd/error_log .
 *
 * When invoking this program on the command line, several parameters are
 * required:
 *
 *     1: hostname of the RMI server (e.g. localHost)
 *     2: port number of the RMI server (e.g. 1099)
 *     3: RMI binding name
 *     4: maxSize of the cache, in elements (integer)
 *     5: maxAge for elements in the cache, in milliseconds (long)
 *
 * This program is designed to run forever, but it will exit gracefully if the
 * Java VM decides to shut down.
 */
public class ModRewriteAuthConnector {
    /**
     * This regular expression describes the format of messages expected to
     * be received from mod_rewrite on stdin.
     */
    private Pattern uriPattern = Pattern.compile("(.+)\\?.*ticket=([0-9]+).*");

    /**
     * Used for look-aside caching of authorization decisions in order to
     * reduce the number of RMI round-trips to core.
     */
    private LossyAgingSet<String> cache;

    private String rmiConnectionString;
    private RepositoryManagerRemote repositoryManager;
    private AtomicBoolean terminationSignal = new AtomicBoolean();

    /** Main program entry point. */
    public static void main(String args[]) {
	ModRewriteAuthConnector obj = new ModRewriteAuthConnector();
	obj.cache = new LossyAgingSet<String>(Integer.parseInt(args[3]),
	        Long.parseLong(args[4]));

	// Register a shutdown handler.
       	Runtime.getRuntime().addShutdownHook(new ShutdownHandler(
                obj.terminationSignal, Thread.currentThread()));

	// Set up the RMI connection.
	obj.rmiConnectionString = "//" + args[0] + ":" + args[1] + "/" 
                + args[2];
	obj.connect();

	// Process requests from mod_rewrite until the termination signal.
	BufferedReader reader 
                = new BufferedReader(new InputStreamReader(System.in));
	while (!obj.terminationSignal.get()) {
	    try {
		obj.processOneRequest(reader, System.out);
	    } catch (IOException ex) {
		obj.logMessage(null, null, ex);
		Thread.yield();
	    }
	}
    }

    /**
     * Reads one request from reader and writes the corresponding response to
     * writer.  This method is potentially long-running, as it will block if
     * no requests are ready to read.  It returns gracefully if the reader is
     * closed by some external force.  If an end-of-stream is encountered on
     * the reader, this method sets the object's terminationSignal and then 
     * returns, without sending any output to the writer.
     */
    private void processOneRequest(BufferedReader reader, PrintStream writer)
            throws IOException {
	String uri = reader.readLine();
	if (uri == null) {
	    // The end of stream has been encountered.  Inform everybody that
	    // we ought to shut down.
            this.terminationSignal.set(true);
	    return;
	}

	// Make an authorization decision, utilizing the cache as possible.
	boolean allowAccess = this.cache.contains(uri);
	if (!allowAccess) {
	    try {
		allowAccess = this.checkAccess(uri);
	    } catch (Exception ex) {
		// Catch-all to assure that all exceptions are reported and
		// none of them cause this module to hang.
		this.logMessage(null, null, ex);
		assert !allowAccess;
	    }
	    if (allowAccess) {
		this.cache.add(uri);
	    }
	}

	// Deliver our decision to mod_rewrite.
	writer.println(allowAccess ? uri : "RECIPNET_FORBIDDEN");
	writer.flush();
    }

    /**
     * To be invoked on cache failure; consults RepositoryManager for an 
     * authorization decision.  A true return value indicates that access has
     * been granted.  A false value indicates that either access has been
     * denied or some failure occurred.
     */
    private boolean checkAccess(String uri) {
	// Parse the URI.
	Matcher matcher = uriPattern.matcher(uri);
	if (!matcher.matches()) {
	    // The URI wasn't of the form we expected.
	    this.logMessage("Unparsable request URI ", uri, null);
	    return false;
	}
	String url = matcher.group(1);
	String ticket = matcher.group(2);

	// Consult RepositoryManager via RMI.
	if (this.repositoryManager == null) {
	    if (!this.connect()) {
		// connect() already wrote an error message, no need to repeat.
		return false;
	    }
	}
	try {
	    return this.repositoryManager.verifyTicket(
                    Integer.parseInt(ticket), url);
	} catch (NumberFormatException ex) {
	    this.logMessage(null, null, ex);
	    return false;
	} catch (RemoteException ex) {
	    this.logMessage(null, null, ex);
	    return false;
	} catch (OperationFailedException ex) {
	    this.logMessage(null, null, ex);
	    return false;
	}
    }

    /**
     * Attempts to bind to RepositoryManager via RMI.  On failure, informative
     * error messages are logged.
     */
    private boolean connect() {
	try {
            this.repositoryManager = (RepositoryManagerRemote) Naming.lookup(
                    this.rmiConnectionString);
	    return true;
        } catch (NotBoundException ex) {
	    this.repositoryManager = null;
            this.logMessage("RMI connection failure", null, ex);
	    return false;
	} catch (RemoteException ex) {
	    this.repositoryManager = null;
            this.logMessage("RMI connection failure", null, ex);
	    return false;
	} catch (MalformedURLException ex) {
	    this.repositoryManager = null;
	    this.logMessage("RMI connection failure", null, ex);
	    return false;
	}
    }

    private void logMessage(String message, String data, Exception ex) {
	StringBuilder sb = new StringBuilder();
	sb.append(new Date());
	sb.append(" ModRewriteAuthConnector: ");
	if (message != null) {
	    sb.append(message);
	}
	if (data != null) {
	    sb.append(" '");
	    sb.append(data);
	    sb.append("'");
	}
	if (ex != null) {
	    sb.append(" exception: ");
	    sb.append(ex.toString());
	}
	System.err.println(sb);
    }

    private static class ShutdownHandler extends Thread {
        private AtomicBoolean signalToSet;
        private Thread threadToInterrupt;

        public ShutdownHandler(AtomicBoolean signalToSet, 
                Thread threadToInterrupt) {
            this.signalToSet = signalToSet;
            this.threadToInterrupt = threadToInterrupt;
        }

        public void run() {
            this.signalToSet.set(true);
            threadToInterrupt.interrupt();
        }
    }

    /**
     * A quick utility class inspired by the Set interface.  The number of 
     * elements stored in this set is limited by a maxSize specified at
     * construction time.  Once the set's capacity limit is reached, any
     * further additions will cause a corresponding removal in a first-in,
     * first-out pattern.  Elements age out of the set also, with a maximum
     * lifetime (in milliseconds) specified at construction time.  Elements
     * automatically disappear from the set once they reach the maxAge age
     * limit.
     */
    private static class LossyAgingSet<T> {
	private LinkedHashMap<T, Long> map;
	private int maxSize;
	private long maxAge;

	public LossyAgingSet(int maxSize, long maxAge) {
	    this.map = new LinkedHashMap<T, Long>(maxSize);
	    this.maxSize = maxSize;
	    this.maxAge = maxAge;
	}

	public void add(T value) {
	    // Do a quick prune operation.
	    if (this.map.size() == maxSize) {
		Iterator<Map.Entry<T, Long>> it 
                        = this.map.entrySet().iterator();
		it.next();
		it.remove();
	    }

	    // Do the put.
	    this.map.put(value, System.currentTimeMillis());
	}

	public boolean contains(T value) {
	    Long timeCreated = this.map.get(value);
	    if (timeCreated != null) {
		if (System.currentTimeMillis() - timeCreated < this.maxAge) {
		    return true;
		}
		this.map.remove(value);
	    }
	    return false;   
	}
    }
}
