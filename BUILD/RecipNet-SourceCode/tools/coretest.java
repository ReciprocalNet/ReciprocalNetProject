/*
 * Reciprocal Net project
 * @(#)coretest.java
 *
 * 24-Jun-2002: ekoperda wrote first draft
 * 04-Feb-2003: ekoperda modernized code by adding import lists, streamlining
 *              exceptions, and adding core module references
 * 04-Mar-2004: midurbin adapted code to handle new build script
 */
import java.rmi.*;
import java.util.*;
import org.recipnet.site.shared.*;
import org.recipnet.site.shared.db.*;
import org.recipnet.site.core.*;

/**
 * This is a test program intended for debugging use only.  Use
 * it to test the remote-accessible methods on the core modules.
 * Add your own test code to the doTestCase() method.
 */
public class coretest {
    static void doTestCase() throws Exception {
	/*
	 * Insert your code here.  You may find the class references to
	 * siteManager, sampleManager, and repositoryManager convenient.
	 */
    }

    /**
     * Caller is responsible for making sure recipnetd, rmiregistry, and mysqld
     * are all running before this program is invoked.
     */
    public static void main(String args[]) {
	// Bind to core modules via RMI.
	try {
	    siteManager = (SiteManagerRemote) 
                    Naming.lookup("RecipnetSiteManager");
	    sampleManager = (SampleManagerRemote) 
                    Naming.lookup("RecipnetSampleManager");
	    repositoryManager = (RepositoryManagerRemote) 
                    Naming.lookup("RecipnetRepositoryManager");
	} catch (Exception ex) {
	    System.err.println("Error while binding to core modules!");
            ex.printStackTrace();
	    System.exit(1);
        }
	System.out.println("Core modules bound.");

	try {
	    doTestCase();
	} catch (Exception ex) {
	    ex.printStackTrace();
	    System.exit(2);
	}

	System.out.println("Core test complete");
	System.exit(0);
    }

    static SiteManagerRemote siteManager;
    static SampleManagerRemote sampleManager;
    static RepositoryManagerRemote repositoryManager;
}



