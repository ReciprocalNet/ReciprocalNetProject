/*
 * Reciprocal Net project
 * @(#)rmibenchmarker.java
 *
 * 13-Oct-2003: midurbin wrote first draft
 * 05-Apr-2004: midurbin fixed import statements for new package structure
 */
import java.awt.*;
import java.io.*;
import java.rmi.*;
import java.util.*;
import javax.swing.*;
import org.recipnet.site.shared.*;
import org.recipnet.site.shared.db.*;
import org.recipnet.site.core.*;

/**
 * This is a test program intended for use by Reciprocal Net developers to
 * benchmark RMI calls.  The results can be used to gauge the performance of
 * RMI or the core functions it calls.
 *
 * For best results, run this program immediately after restarting the test
 * server and be sure that no periodic tasks start before it completes.
 */
public class rmibenchmarker {

    /**
     * Caller is responsible for making sure recipnetd, rmiregistry, and mysqld
     * are all running before this program is invoked.
     */
    public static void main(String args[]) {
	// Bind to core modules via RMI.
        SiteManagerRemote siteManager = null;
        SampleManagerRemote sampleManager = null;
        RepositoryManagerRemote repositoryManager = null;
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

	try {
            System.out.print("Loading TestEngine...");
            TestEngine tester = new TestEngine(siteManager, sampleManager,
                    repositoryManager);
            System.out.println("COMPLETE!");
            /*
             * Modify arguments to the follwing two calls to create the
             * benchmark test cases that you desire.
             */
            tester.setTests(
                    TestEngine.SEARCH_TEST
                    | TestEngine.FETCH_USER_INFO_TEST
                    | TestEngine.FETCH_SAMPLE_INFO_TEST
                    | TestEngine.FETCH_LAB_INFO_TEST
                    | TestEngine.FETCH_PROVIDER_INFO_TEST
                    | TestEngine.FETCH_FULLSAMPLE_INFO_TEST
                    , 100);

            tester.setFileTest(25, 1024 * 128, 16, 40);

            tester.performTests();
            // Output each datapoint to file-- must be done first because the
            // results get sorted as a side effect.
            if (args.length == 1) {
                FileOutputStream fos = new FileOutputStream(new File(args[0]));
                tester.outputResults(fos, TestEngine.DISPLAY_ALL_SAMPLES);
            } 

            // Output summary to screen
            tester.outputResults(System.out, TestEngine.DISPLAY_AVERAGE
                    | TestEngine.DISPLAY_MAX | TestEngine.DISPLAY_MIN
                    | TestEngine.DISPLAY_MEDIAN);
	} catch (Exception ex) {
	    ex.printStackTrace();
	    System.exit(2);
	}

	System.exit(0);
    }

    /** 
     * Crudely encapsulates a test server for RMI benchmarking
     * New test cases can be added to this object.
     */
    public static class TestEngine {
        public static int SEARCH_TEST = 1;
        public static int FETCH_USER_INFO_TEST = 2;
        public static int FETCH_SAMPLE_INFO_TEST = 4;
        public static int FETCH_LAB_INFO_TEST = 8;
        public static int FETCH_PROVIDER_INFO_TEST = 16;
        public static int FETCH_FULLSAMPLE_INFO_TEST = 32;

        public static int DISPLAY_NON_FILE_TEST_DETAILS = 1;
        public static int DISPLAY_FILE_TEST_DETAILS = 2;
        public static int DISPLAY_AVERAGE = 4;
        public static int DISPLAY_MEDIAN = 8;
        public static int DISPLAY_MAX = 16;
        public static int DISPLAY_MIN = 32;
        public static int DISPLAY_ALL_SAMPLES = 64;


        /** limits the number of objects retrieved from core */
        private static final int MAX_OBJECTS = 2500;

        /** the number of times each RMI call (excluding file upload test) */
        private int sampleCount;

        /** the size of the portion of the file being sent over RMI */
        private long fileBlockSize;

        /** the number of file portions being sent over RMI */
        private long fileBlockCount;
        /**
         * the minimum number of milliseconds before closeDataFile() is called
	 * for the file upload test.
         */
        private long msToWait;

        /** 
         *contains either the properties associated with the recipnet
         * configuration file, or NULL if it could not be loaded.
         */
        private Properties properties;

        /** A random number generator, for use by many testing functions */
        static Random random;

        /** SampleInfo objects for every sample on the DB */
        static SampleInfo samples[];

        /** UserInfo objects for every user on the DB */
        static UserInfo users[];

        /** ProviderInfo objects for every provider on the DB */
        static ProviderInfo providers[];

        /** LabInfo objects for every lab on the DB */
        static LabInfo labs[];

        /**
         * test results are stored here, and non-null values are indicators
         * that a test should be performed 
         */
        private int trials;
        private long getEmptySearchParamsTimes[];
        private long storeSearchParamsTimes[];
        private long getSearchResultsTimes[];
        private long getUserInfoTimes[];
        private long getSampleInfoTimes[];
        private long getLabInfoTimes[];
        private long getProviderInfoTimes[];
        private long getFullSampleInfoTimes[];

        private long initTimes[];
        private long blockTimes[];
        private long blockAverages[];
        private long completeTimes[];
        private long totalTransferTimes[];
        private long averageBytesPerSecond[];
        private long averageRates[];
        private byte buffer[];
        private long delayBeforeClose;
        private long blockCount;

        private SampleManagerRemote sampleManager;
        private SiteManagerRemote siteManager;
        private RepositoryManagerRemote repositoryManager;

        /**
         * Creates an instance associated with a particular RMI connection to
         * the three core modules.  All samples, providers, labs and users are
         * loaded into memory so faciliate random selection of valid data by
         * other calls.
         */
        public TestEngine(SiteManagerRemote siteManager,
                SampleManagerRemote sampleManager,
                RepositoryManagerRemote repositoryManager) throws Exception {

            random = new Random(System.currentTimeMillis());
            this.siteManager = siteManager;
            this.sampleManager = sampleManager;
            this.repositoryManager = repositoryManager;

            Properties properties = new Properties();
            try {
                properties.load(new BufferedInputStream(
                        new FileInputStream(
                                new File("/etc/recipnet/recipnetd.conf"))));
            } catch (Exception ex) {
                properties = null;
            }

            samples = sampleManager.getSearchResults(
                    sampleManager.storeSearchParams(
                            sampleManager.getEmptySearchParams()), 0,
                                    MAX_OBJECTS);
            users = siteManager.getAllUserInfo();
            labs = siteManager.getAllLabInfo();
            providers = siteManager.getAllProviderInfo(labs[0].id);
        }

        /**
         * Sets the simple RMI tests that are to be performed by subsequent
         * calls to <code>performTests()</code>.  If no call is made to this
         * function, then none of the applicable tests will be performed.
         * @param tests a bit map created by logically oring test constants
         *     indicating which tests should be performed
         * @param sampleCount indicates the number of times each sample should
         *     be performed
         */
        public void setTests(int tests, int sampleCount) {
            this.trials = sampleCount;
            if ((tests & SEARCH_TEST) != 0) {
                this.getEmptySearchParamsTimes = new long[sampleCount];
                this.storeSearchParamsTimes = new long[sampleCount];
                this.getSearchResultsTimes = new long[sampleCount];
            } else {
                this.getEmptySearchParamsTimes = null;
                this.storeSearchParamsTimes = null;
                this.getSearchResultsTimes = null;
            }
            if ((tests & FETCH_USER_INFO_TEST) != 0) {
                this.getUserInfoTimes = new long[sampleCount];
            } else {
                this.getUserInfoTimes = null;
            }
            if ((tests & FETCH_PROVIDER_INFO_TEST) != 0) {
                this.getProviderInfoTimes = new long[sampleCount];
            } else {
                this.getProviderInfoTimes = null;
            }
            if ((tests & FETCH_LAB_INFO_TEST) != 0) {
                this.getLabInfoTimes = new long[sampleCount];
            } else {
                this.getLabInfoTimes = null;
            }
            if ((tests & FETCH_SAMPLE_INFO_TEST) != 0) {
                this.getSampleInfoTimes = new long[sampleCount];
            } else {
                this.getSampleInfoTimes = null;
            }
            if ((tests & FETCH_FULLSAMPLE_INFO_TEST) != 0) {
                this.getFullSampleInfoTimes = new long[sampleCount];
            } else {
                this.getFullSampleInfoTimes = null;
            }
        }

        /**
         * Sets up the parameter for a file transfer test that will be
         * performed by the next call to <code>performTests()</code>.  If no 
         * call is made to this function, no file transfer test will be 
         * performed.
         * @param samples indicates the number of transfers that should be 
         *     performed.
         * @param blockSize indicates the number of bytes to be transfered in 
         *     each single call to <code>RepositoryManager.writeToDataFile()
         *     </code>.
         * @param blockCount indicates the number of calls to
         *     <code>RepositoryManager.writeToDataFile()</code> that will be
         *     invoked.
         * @param delayBeforeClose indicates the minimum number of milliseconds
         *     that will elapse between sending the last portion of the file
         *     and causing closeDataFile().  Tests have shown that this affects
         *     the benchmark results and should be standardized.
         */
        public void setFileTest(int samples, long blockSize,
                long blockCount, long delayBeforeClose) {
            this.blockCount = blockCount;
            this.initTimes = new long[samples];
            this.blockTimes = new long[(int) blockCount];
            this.blockAverages = new long[samples];
            this.completeTimes = new long[samples];
            this.totalTransferTimes = new long[samples];
            this.averageBytesPerSecond = new long[samples];
            this.averageRates = new long[samples];
            this.buffer = new byte[(int) blockSize];
            this.delayBeforeClose = delayBeforeClose;
        }


        /**
         * Performs the tests that were specified by previous calls to <code>
	 * setTests()</code> and <code>setFileTest()</code>.
         */
        public void performTests() throws Exception {

            // The number of RMI tests to perform
            int tests = 6;

            // Test 0: search
            SearchParams sp = null;

            int searchId = 0;

            SampleInfo sis[] = null;

            // Test 1: fetch UserInfo
            UserInfo ui = null;
        
            // Test 2: fetch SampleInfo
            SampleInfo si = null;

            // Test 3: fetch LabInfo
            LabInfo li = null;

            // Test 4: fetch ProviderInfo
            ProviderInfo pi = null;

            // Test 5: fetch FullSampleInfo
            FullSampleInfo fsi = null;

            long starttime = 0;
            long stoptime = 0;

            if (this.trials != 0) {
                System.out.println("Performing tests:");
            }
            ProgressMeter progress
                    = new ProgressMeter(0, this.trials, 0, "Testing Progress");
            for (int i=0;i<this.trials;i++) {
                boolean done[] = new boolean[tests];
                for (int x=0;x<tests;x++) { done[x] = false; }
                for (int j=0;j<tests;j++) {
                    progress.setValue(i);
                    int nextTest = random.nextInt(tests-j);
                    for (int k=0;k<=nextTest;k++) {
                        if (done[k]) {
		            nextTest++;
                        }
                    }
                    done[nextTest] = true;

                    switch (nextTest) {
	            case 0:
                        if (getSearchResultsTimes != null) {
                            // get search params
                            starttime = System.currentTimeMillis();
                              sp = sampleManager.getEmptySearchParams();
                            stoptime = System.currentTimeMillis();
                            getEmptySearchParamsTimes[i]
                                    = stoptime - starttime;

                             // store search params
                            starttime = System.currentTimeMillis();
                              searchId = sampleManager.storeSearchParams(sp);
                            stoptime = System.currentTimeMillis();
                            storeSearchParamsTimes[i] = stoptime - starttime;

                            // get search results
                            starttime = System.currentTimeMillis();
                              sis = sampleManager.getSearchResults(searchId, 
                                      random.nextInt(samples.length - 10), 10);
                            stoptime = System.currentTimeMillis();
                            getSearchResultsTimes[i] = stoptime - starttime;
                        }
                        break;

                    case 1:
                        if (getSampleInfoTimes != null) {
                            // get sample info
                            starttime = System.currentTimeMillis();
                            si = sampleManager.getSampleInfo(
                                  samples[random.nextInt(samples.length)].id);
                            stoptime = System.currentTimeMillis();
                            getSampleInfoTimes[i] = stoptime - starttime;
                        }
                        break;

	             case 2:
                        if (getFullSampleInfoTimes != null) {
                            // get full sample info
                             starttime = System.currentTimeMillis();
	                     fsi = sampleManager.getFullSampleInfo(
                                  samples[random.nextInt(samples.length)].id);
                            stoptime = System.currentTimeMillis();
                            getFullSampleInfoTimes[i] = stoptime - starttime;
                        }
                        break;

                     case 3:
                        if (getUserInfoTimes != null) {
                            // get a random user info
                            starttime = System.currentTimeMillis();
	                      ui = siteManager.getUserInfo(
                                      users[random.nextInt(users.length)].id);
                            stoptime = System.currentTimeMillis();
                            getUserInfoTimes[i] = stoptime - starttime;
                        }
                        break;

                    case 4:
                        if (getLabInfoTimes != null) {
                            // get a random lab info
                            starttime = System.currentTimeMillis();
                            li = siteManager.getLabInfo(
                                      labs[random.nextInt(labs.length)].id);
                            stoptime = System.currentTimeMillis();
                            getLabInfoTimes[i] = stoptime - starttime;
                        }
                        break;

                    case 5:
                        if (getProviderInfoTimes != null) {
                            // get a random provider info
                            starttime = System.currentTimeMillis();
	                      pi = siteManager.getProviderInfo(providers[
                                      random.nextInt(providers.length)].id);
                            stoptime = System.currentTimeMillis();
                            getProviderInfoTimes[i] = stoptime - starttime;
                        }
                        break;
                    }
                }
            }
            progress.hide();

            int samples = (initTimes == null ? 0 : initTimes.length);
            if (samples != 0) {
                System.out.println("Beginning file transfer test:");
            }
            long startTime = 0;
            long endTime = 0;
            int userId = getFirstSiteAdmin();
            if (blockCount != 0) {
                progress = new ProgressMeter(0, (int)blockCount, 0,
                        "Upload Progress", 0,
                        (int) samples  * (int) blockCount, 0);
            }

            for (int i=0;i<samples;i++) {
                SampleInfo sampleInfo = generateNewSample();
                int ticketId = 0;
                startTime = System.currentTimeMillis();
                ticketId = repositoryManager.beginWritingDataFile(
                      sampleInfo, "test.junk", true, true,
                      SampleHistoryInfo.SUBSTITUTE_FILE_ADDED_OR_FILE_REPLACED,
                      userId, "Automatic upload."); 
                endTime = System.currentTimeMillis();
                initTimes[i] = endTime - startTime;
                for (int j=0;j<blockCount;progress.setValue(j++,
                        (i * (int)blockCount) + j)) {
                    startTime = System.currentTimeMillis();
                    repositoryManager.writeToDataFile(ticketId, buffer);
                    endTime = System.currentTimeMillis();
                    blockTimes[j] = endTime - startTime;
                    totalTransferTimes[i] += endTime - startTime;
                }
                progress.setValue((int)blockCount, (i+1) * (int)blockCount);

                startTime = System.currentTimeMillis();
                endTime = System.currentTimeMillis();
                while (endTime - startTime < delayBeforeClose) {
                    endTime = System.currentTimeMillis();
                    // Just keep spinning for the required delay
                }

                startTime = System.currentTimeMillis();
                repositoryManager.closeDataFile(ticketId);
                endTime = System.currentTimeMillis();
                completeTimes[i] = endTime - startTime;
                averageBytesPerSecond[i] = (buffer.length * 1000 * blockCount) 
                        / totalTransferTimes[i];
                averageRates[i] = (buffer.length * blockCount * 1000)
                        / (completeTimes[i] + initTimes[i]
                        + totalTransferTimes[i]);
            }
            progress.hide();
        }

        /**
         * Writes the results of the tests.
         * @param os the stream to which the results will be written
         * @param options an or-ed bitmap of display options indicating which
         *     fields should be displayed. 
         */
        public void outputResults(OutputStream os, int options) {
            displayNonFileTestInformation(os);
            displayResults(getEmptySearchParamsTimes, 
                    "sampleManager.getEmptySearchParams()", options, os);
            displayResults(storeSearchParamsTimes, 
                    "sampleManager.storeSearchParams()", options, os);
            displayResults(getSearchResultsTimes, 
                    "sampleManager.getSearchResults()", options, os);
            displayResults(getSampleInfoTimes, 
                    "sampleManager.getSampleInfo()", options, os);
            displayResults(getFullSampleInfoTimes,
                    "sampleManager.getFullSampleInfo()", options, os);
            displayResults(getProviderInfoTimes,
                    "siteManager.getProviderInfo()", options, os);
            displayResults(getLabInfoTimes, "siteManager.getLabInfo()",
                    options, os);
            displayResults(getUserInfoTimes, "siteManager.getUserInfo()",
                    options, os); 

            displayFileTestInformation(os);
            displayResults(initTimes, "beginWritingDataFile()", options, os);
            displayResults(totalTransferTimes, "writeToDataFile()",
                    options, os);
            displayResults(averageBytesPerSecond, "average rate for"
                   + " writeToDataFile()", options, os);
            displayResults(completeTimes, "closeDataFile()", options, os);
            displayResults(averageRates, "Overall rate", options, os);
        }

        private void displayNonFileTestInformation(OutputStream os) {
            if (sampleCount == 0) { return; }
            PrintStream ps = new PrintStream(os);
            ps.println("RMI Test Trials: " + sampleCount);

            ps.println("Sample Pool:     " + samples.length
                    + ((properties != null) ? " (cache size: " 
                   + getCacheSize(properties.getProperty("SamSampleCache"))
                      + ")" : ""));

            ps.println("User Pool:     " + users.length
                    + ((properties != null) ? " (cache size: " 
                    + getCacheSize(properties.getProperty("SitUserCache"))
                    + ")" : ""));

            ps.println("Lab Pool:      " + labs.length 
                    + ((properties != null) ? " (cache size: " 
                    + getCacheSize(properties.getProperty("SitLabCache"))
                    + ")" : ""));

            ps.println("Provider Pool: " + providers.length
                    + ((properties != null) ? " (cache size: " 
                    + getCacheSize(
                        properties.getProperty("SitProviderCache"))
                    + ")" : ""));
            ps.println();
        }
        private void displayFileTestInformation(OutputStream os) {
            if (this.initTimes != null) {
                PrintStream ps = new PrintStream(os);
                ps.println("File Transfer Test Trials: " + initTimes.length);
                ps.println("File Block Count:          "+ blockCount);
                ps.println("File Block Size:           " + buffer.length
                        + "bytes");
                ps.println("Minimum ms before close(): " + delayBeforeClose);
            }
        }

        /**
         * Writes summary information about the supplied data.  As a side 
         * effect, the values are sorted.
         * @param values an array of test result values
         * @param testName descriptive information to identify the dataset in
         *    question
         * @param options an or-ed bitmap of display options, used to indicate
         *    which values should be included.
         * @param os the stream to which the data should be written
         */
        public static void displayResults(long values[], String testName,
                int options, OutputStream os) {

            PrintStream ps = new PrintStream(os); 
            if (values == null) {
                return;
            }

            ps.println(testName + ":");
            if ((options & DISPLAY_ALL_SAMPLES) != 0) {
                ps.print("data points: ");
                for (int i=0;i<values.length;i++) {
                    ps.print(values[i] + " ");
                }
                ps.println();
            }
            if ((options & DISPLAY_AVERAGE) != 0) {
                ps.println("Average: " + sortAndReturnAverage(values));
            } else {
                sortAndReturnAverage(values);
            }
            if ((options & DISPLAY_MEDIAN) != 0) {
                ps.println("Median:  " + values[values.length / 2]);
            }
            if ((options & DISPLAY_MIN) != 0) {
                ps.println("Min:     " + values[0]);
            }
            if ((options & DISPLAY_MAX) != 0) {
                ps.println("Max:     " + values[values.length - 1]);
            }
            ps.println();
        }

        public static long sortAndReturnAverage(long values[]) {
            long total = 0;
            Arrays.sort(values);
            for (int i=0;i<values.length;i++) {
                total += values[i];
            }
            return ((total  + (values.length / 2)) / values.length);
        }

        /**
         * A convenience function that retrives the first active site admin
         * userId from the database.
         */
        private int getFirstSiteAdmin() {
            for (int i=0;i<users.length; i++) {
                if (((users[i].globalAccessLevel & UserInfo.SITE_ADMIN_ACCESS)
                        != 0) && users[i].isActive) {
                    return users[i].id;
                }
            }
            return UserInfo.INVALID_USER_ID;
        }

        /**
         * A convenience function to get a Provider and it's lab for sample
         * submission.
         */
        private ProviderInfo getFirstActiveProvider() {
            for (int i=0;i<providers.length;i++) {
                if (providers[i].isActive) {
                    return providers[i];
                }
            }
            return null;
        }

        /**
         * extracts and returns the size of the cache from the config string
         */
        public static int getCacheSize(String str) {
            StringTokenizer t;
            t = new StringTokenizer(str, ",");
            return Integer.parseInt(t.nextToken());
        }

        /**
         * Submits a new sample to the database with a random 8 character
         * numeric localLabId and creates a repository directory in the default
         * location.
         */
         private SampleInfo generateNewSample() throws Exception {
            SampleInfo sampleInfo = sampleManager.getSampleInfo();
            ProviderInfo provider = this.getFirstActiveProvider();
            sampleInfo.labId = provider.labId;
            sampleInfo.localLabId = randomString(8);
            sampleInfo.providerId = provider.id;
            sampleInfo.status = SampleInfo.PENDING;
            int userId = this.getFirstSiteAdmin();
            sampleInfo = sampleManager.putSampleInfo(sampleInfo,
                    SampleHistoryInfo.SUBMITTED, userId,
                    "Automatic submission");
            repositoryManager.createDataDirectory(sampleInfo.id, "", userId);
            return sampleManager.getSampleInfo(sampleInfo.id);
        }

        /** 
         * Generates a string containing the indicated number of characters.
         * The characters are chosen to be numbers 0-9 selected in a psuedo-
         * random fashon.  A class level Random object called random must be
         * available and initialized.
         */
        public static String randomString(int length) throws Exception {
            StringBuffer sb = new StringBuffer(length);
            for (int i=0;i<length;i++) {
                sb.append((new Integer(random.nextInt(9)).toString()));
            }
            return sb.toString();
        }

    }

    /**
     * A class that draws a progress bar either using Java Swing window
     * components, or using plain text if no graphical environment is
     * available.  There are two constructors, one indicates a single progress
     * bar, the other is for a double progress bar that displays progress on
     * the whole operation as well as progress on a smaller portion of it.
     */
    public static class ProgressMeter {

        private JFrame frame;
        private boolean hasGUI;
        private int width;
        private int max;
        private int bigMax;
        private JProgressBar progressBar;
        private JProgressBar bigPicture;

        /** constructor for a double progress bar */
        public ProgressMeter(int min, int max, int startValue, String label,
			     int min2, int max2, int startValue2) {
            if (GraphicsEnvironment.isHeadless()) {
                hasGUI = false;
                this.max = max;
                this.bigMax = max2;
                this.width = 20;
            } else {
                try {
                    frame = new JFrame(label);
 
                    progressBar = new JProgressBar(min, max);
                    progressBar.setValue(startValue);
                    bigPicture = new JProgressBar(min2, max2);
                    bigPicture.setValue(startValue2);
            
                    JPanel panel = new JPanel();
                    panel.setLayout(new BorderLayout());
                    panel.add(progressBar, BorderLayout.NORTH);
                    panel.add(bigPicture, BorderLayout.CENTER);

                    frame.setContentPane(panel);
                    frame.pack();
                    frame.setVisible(true);
                    hasGUI = true;
                } catch (Throwable ex) {
                    hasGUI = false;
                    this.max = max;
                    this.bigMax = max2;
                    this.width = 20;
                }
            }
            this.setValue(startValue);
        }

        /** constructor for a single progress bar */
        public ProgressMeter(int min, int max, int startValue, String label) {
            if (GraphicsEnvironment.isHeadless()) {
                hasGUI = false;
                width = 20;
                this.max = max;
            } else {
                try {
                    frame = new JFrame(label);
 
                    progressBar = new JProgressBar(min, max);
                    progressBar.setValue(startValue);
            
                    JPanel panel = new JPanel();
                    panel.add(progressBar);

                    frame.setContentPane(panel);
                    frame.pack();
                    frame.setVisible(true);
                    hasGUI = true;
                } catch (Throwable ex) {
                    hasGUI = false;
                    width = 20;
                    this.max = max;
                }
            }
            this.setValue(startValue);
        }

        /** sets the value of the single progress bar and redisplays it */
        public void setValue(int value) {
            if (hasGUI) {
                progressBar.setValue(value);
            } else {
                int percent = ((value * 100) / this.max);
                for (int i=0;i<this.width + 8;i++) {
                    System.out.print("\b");
                }
                System.out.print(" [");
                for (int i=0;i<this.width;i++) {
                    if ((percent * this.width / 100) >= i) {
                        System.out.print("*");
                    } else {
                        System.out.print(" ");
                    }
                }
                System.out.print("]");
                System.out.print("(" + (percent) + "%)");
            }
        }

        /** sets the values of the double progress bar and redisplays it */
        public void setValue(int val1, int val2) {
            if (hasGUI) {
                progressBar.setValue(val1);
                bigPicture.setValue(val2);
            } else {
                int percent1 = ((val1 * 100) / this.max);
                int percent2 = ((val2 * 100) / this.bigMax);
                for (int i=0;i<this.width + 8;i++) {
                    System.out.print("\b");
                }
                System.out.print(" [");
                for (int i=0;i<this.width;i++) {
                    if ((percent1 * this.width / 100) >= i) {
                        if ((percent2 * this.width / 100) >= i) {
                            System.out.print(":");
                        } else {
                            System.out.print(".");
                        }
                    } else if ((percent2 * this.width / 100) >= i) {
                        System.out.print(".");
                    } else {
                        System.out.print(" ");
                    }
                }
                System.out.print("]");
                System.out.print("(" + (percent2) + "%)");
            }
        }

        /** removes the progress bar from the screen */
        public void hide() {
            if (hasGUI) {
                frame.setVisible(false);
            } else {
                for (int i=0;i<this.width + 8;i++) {
                    System.out.print("\b");
                }
            }
        }
        
    }

}



