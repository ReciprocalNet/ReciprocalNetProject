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
import org.recipnet.site.shared.bl.*;
import org.recipnet.site.shared.db.*;
import org.recipnet.site.core.*;

/**
 * This is a test program intended for debugging use only.  Use
 * it to test the remote-accessible methods on the core modules.
 * Add your own test code to the doTestCase() method.
 */
public class coretest {
    static void doTestCase() throws Exception {

        ProviderInfo provider = siteManager.getEmptyProviderInfo();
        provider.name = "Automated batch provider";
        provider.labId = siteManager.getLocalLabs2()[0];
        int providerId = siteManager.writeUpdatedProviderInfo(provider);

        for (int i = 0; i < 1024; i ++) {
            SampleInfo sample = sampleManager.getSampleInfo();
            sample.labId = provider.labId;
            sample.dataInfo.providerId = providerId;
            sample.status = SampleWorkflowBL.PENDING_STATUS;
            sample = sampleManager.putSampleInfo(sample, 
                    SampleWorkflowBL.SUBMITTED, new Date(), 
                    UserInfo.INVALID_USER_ID, "coretest automated batch");

            sample.status = SampleWorkflowBL.REFINEMENT_PENDING_STATUS;
            sample = sampleManager.putSampleInfo(sample, 
                    SampleWorkflowBL.PRELIMINARY_DATA_COLLECTED, new Date(), 
                    UserInfo.INVALID_USER_ID, "coretest automated batch");

            sample.status = SampleWorkflowBL.COMPLETE_STATUS;
            sample = sampleManager.putSampleInfo(sample, 
                    SampleWorkflowBL.STRUCTURE_REFINED, new Date(), 
                    UserInfo.INVALID_USER_ID, "coretest automated batch");

            repositoryManager.createDataDirectory(sample.id, null, 
                    UserInfo.INVALID_USER_ID);

            sample.status = SampleWorkflowBL.COMPLETE_PUBLIC_STATUS;
            sample = sampleManager.putSampleInfo(sample, 
                    SampleWorkflowBL.RELEASED_TO_PUBLIC, new Date(), 
                    UserInfo.INVALID_USER_ID, "coretest automated batch");

            System.out.println("Creating sample " + i + ": id " + sample.id);
        }

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



