/*
 * Reciprocal Net project
 * 
 * VersionUpdater.java
 *
 * 23-Oct-2002: ekoperda wrote first draft
 * 04-Nov-2002: ekoperda added migration task 581 that generates missing ISM's
 *              and also added support for an upgrade-only task like this one
 * 06-Dec-2002: eisiorho added migration task 640 that fixes corrupt Provider
 *              records
 * 18-Feb-2003: ekoperda added migration task 688 that publicizes existing 
 *              sample records
 * 21-Feb-2003: ekoperda added exception support throughout
 * 29-May-2003: ekoperda added migration task 909 that registers existing 
 *              repository directories as "primary directories"
 * 10-Jul-2003: midurbin added static function, getCurrentVersion()
 * 18-Jul-2003: ekoperda modified doVersionUpdate1() and createDbAccounts() to
 *              utilize new config directive DbUrlForBootstrap
 * 28-Jul-2003: nsanghvi modified doInit() and createDbAccounts() and added 
 *              dropDbTables() to allow init script to be run as the user
 *              'recipnet'.  Also modified the logic in 
 *              changeConfigDbPasswords()
 * 07-Jan-2004: ekoperda changed package references to match source tree
 *              reorganization
 * 02-Apr-2004: jobollin added migration task 1133 that recodes character data
 *              in the database (as necessary) during an update from an earlier
 *              version to version 0.6.2
 * 07-May-2004: cwestnea added migration task 1211 that rebuilds searchAtoms
 * 22-Mar-2005: ekoperda added migration task 1563 to create and populate the
 *              'searchUnitCells' table
 * 12-Jul-2005: ekoperda added migration task 1632 to populate the 
 *              'searchSpaceGroups' table
 * 16-Sep-2005: midurbin replaced a call to UserInfo.setPassword() with a call
 *              to UserBL.setPassword()
 * 02-Feb-2006: ekoperda added migration task 1727 to populate the 'storedIsms'
 *              table
 * 11-Apr-2006: jobollin removed an inaccessible catch block and organized
 *              imports
 * 20-Apr-2006: jobollin reformatted the source, added generic type parameters
 *              where appropriate, and updated doInit() to clear the message
 *              directories specified in the config file (instead of always the
 *              default message directories) and to do it via the Java File API
 *              instead of by spawning an external process
 * 09-Nov-2006: jobollin made doSync report the cause chains of exceptions when
 *              it displays them to stdout
 * 16-Nov-2006: jobollin added support for drecipnet's 'setpasswords' option
 * 29-Dec-2007: ekoperda fixed bug #1842 in dropDbTables()
 * 04-Jan-2008: ekoperda added doCreateDbAccounts(), invocable from drecipnet
 * 04-Jul-2008: ekoperda commented-out online update tasks 581 and 688
 * 01-Jan-2008: ekoperda added migration task 1911 to update the local ISM
 *              sequence number table to correct an earlier Coordinator bug
 * 18-Mar-2009: ekoperda enhanced support for ISM suicide notes in doInit() and
 *              doSync()
 * 18-Mar-2009: ekoperda fixed bug #1898 in createDbAccounts()
 */

package org.recipnet.site.core;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.SortedMap;
import java.util.StringTokenizer;
import java.util.TreeMap;

import org.recipnet.site.InvalidDataException;
import org.recipnet.site.OperationFailedException;
import org.recipnet.site.shared.bl.UserBL;
import org.recipnet.site.shared.db.UserInfo;

/**
 * <p>
 * Called by CoreLoader during an 'init', 'update', or 'rebuild' operation.
 * Executes a sequence of pre-defined "migration tasks" necessary to migrate a
 * site software installation from one software version to another. Each
 * migration task is assigned a unique number (taken from the development
 * team's tasks database) and are executed in order. Migration tasks that have
 * already been applied to an installation (perhaps during a previous upgrade
 * operation) will not be applied again. A record of the most recent migration
 * task applied is stored in the 'version' table of the database.
 * </p><p>
 * Migration tasks may run either before the core is instantiated or
 * afterwards.  The former is appropriate if the task performs db schema
 * updates, while the latter is appropriate if it invokes methods on the core
 * modules. Most migration tasks are small snippets of code declared in the
 * method declareTasksHere() with one exception: SQL-only tasks (those that
 * execute SQL commands to alter database schema) can be defined simply by
 * creating a .sql file in the misc/sql/ directory of the source tree. Such
 * files are considered implicitely-declared migration tasks and are detected
 * and run automatically by this class.
 * </p><p>
 * The 'rebuild' operation simply performs all migration tasks that are flagged
 * as "rebuild" tasks, regardless of the current version of the site software
 * installation. Any explicitely-declared migration task may optionally be
 * flagged as a "rebuild" task, so long as it does not talk directly to the
 * database engine.
 * </p>
 */
public class VersionUpdater {
    /**
     * The code in this method defines every migration task (except SQL schema
     * changes, which are defined implicitely by the presence of a .sql file in
     * the misc/sql directory of the source tree). Add additional lines here to
     * define new migration tasks and rebuild tasks. Task numbers should be
     * taken from the development team's task list and must increate
     * monotonically over time.
     */
    private void declareTasksHere() {
        declareTask(174, true, true, false, "bootstrapping core modules",
                new UpdateTask() {
                    public void doTask(VersionUpdater updater)
                            throws Exception {
                        System.out.println("");
                        updater.siteManager.performBootstrapTasks();
                    }
                });
        declareTask(415, true, true, true,
                "populating data in table searchAtoms", new UpdateTask() {
                    public void doTask(VersionUpdater updater)
                            throws Exception {
                        updater.sampleManager.rebuildSearchAtoms();
                    }
                });
        declareTask(456, true, true, true,
                "regenerating auto-computed fields in table sampleData",
                new UpdateTask() {
                    public void doTask(VersionUpdater updater)
                            throws Exception {
                        updater.sampleManager.rebuildSampleData();
                    }
                });
        declareTask(563, true, true, true,
                "populating data in table searchLocalHoldings",
                new UpdateTask() {
                    public void doTask(VersionUpdater updater)
                            throws Exception {
                        updater.sampleManager.rebuildSearchLocalHoldings();
                    }
                });

	/**
	 * Task 581 was necessary during the upgrade from site software 0.5.1
	 * to site software 0.5.2.  However, the 0.5.2 upgrade is long passed 
	 * and this task is not useful on new sites.
         * declareTask(581, true, false, false,
         *       "generating previously omitted inter-site messages",
         *       new UpdateTask() {
         *           public void doTask(VersionUpdater updater)
         *                   throws Exception {
         *               updater.siteManager.generateInitialIsms();
         *               updater.sampleManager.generateInitialIsms();
         *           }
         *       });
	 */

        declareTask(640, true, false, false, "fixing corrupt provider records",
                new UpdateTask() {
                    public void doTask(VersionUpdater updater) 
                            throws Exception {
                        updater.siteManager.fixCorruptProviders();
                    }
                });

	/**
	 * Task 688 was necessary during the upgrade from site software 0.5.3 
	 * to 0.6.0.  However, the 0.6.0 upgrade is long passed and this task
	 * is not useful on new sites.
	 * declareTask(688, true, true, false,
         *       "announcing public samples to other sites", new UpdateTask() {
         *           public void doTask(VersionUpdater updater)
         *                   throws Exception {
         *               updater.sampleManager.publicizeExistingSamples();
         *           }
         *       });
	 */

        declareTask(909, true, true, false,
                "initializing versioning on repository files",
                new UpdateTask() {
                    public void doTask(VersionUpdater updater)
                            throws Exception {
                       updater.repositoryManager.registerExistingDirectories();
                    }
                });
        /*
         * NOTE WELL: This task must run before most of the above tasks and may
         * run before all of them, but it must run after lower-numbered tasks
         * that modify the DB structure. To achieve this it makes use of the
         * fact that "offline" tasks (those that do not require core or claim
         * to require it, such as this one, including all DB-structure tasks)
	 * are run before all "online" tasks.
         */
        declareTask(1133, false, false, false,
                "re-encoding non-ASCII characters in the database",
                new UpdateTask() {
                    public void doTask(
                            @SuppressWarnings("unused") VersionUpdater updater)
                            throws Exception {
                        Connection oldCon = DriverManager.getConnection(
                                properties.getProperty(
                                  "DbUrlForCharsetUpdate"),
                                properties.getProperty("SitDbUsername"),
                                properties.getProperty("SitDbPassword"));

                        DBCharsetUpdater.updateDBCharacters(oldCon, conn);
                    }
                });
	/* 
	 * Task 1211 does not happen during rebuild operations because task 415
	 * above already executed the same code.
	 */
        declareTask(1211, true, false, false, "rebuilding searchAtoms",
                new UpdateTask() {
                    public void doTask(VersionUpdater updater)
                            throws Exception {
                        updater.sampleManager.rebuildSearchAtoms();
                    }
                });
        declareTask(1563, true, false, true,
                "populating data in table searchUnitCells", new UpdateTask() {
                    public void doTask(VersionUpdater updater)
                            throws Exception {
                        updater.sampleManager.rebuildSearchUnitCells();
                    }
                });
        declareTask(1632, true, false, true,
                "populating data in table searchSpaceGroups", new UpdateTask(){
                    public void doTask(VersionUpdater updater)
                            throws Exception {
                        updater.sampleManager.rebuildSearchSpaceGroups();
                    }
                });
        declareTask(1727, true, true, true,
                "populating data in table storedIsms", new UpdateTask() {
                    public void doTask(VersionUpdater updater) 
                            throws Exception {
                        updater.siteManager.rebuildIsmIndex();
                    }
                });
        declareTask(1911, true, true, false,
                "marking site grant message as processed", new UpdateTask() {
                    public void doTask(VersionUpdater updater) 
                            throws Exception {
                        updater.siteManager.markSiteGrantIsmAsProcessed();
                    }
                });
    }

    /**
     * The constant below is defined by the 'recipnet-build' script at build
     * time. The string looks something like 'recipnet-0.5.2-40', depending on
     * the current version and build number.
     */
    public static final String buildName = "@BUILDVERSION@";

    /**
     * The task number of the last update that was applied to the previous
     * version of the site software. Any migration tasks with a task number
     * higher than this will be performed. The special value 0 means that all
     * migration tasks should be performed. The special value -1 means that the
     * previous task number has not yet been detected.
     */
    private int lastTask;

    /**
     * Maps Integer task numbers to UpdateTaskRecords. Only those tasks
     * with a task number higher than <code>lastTask</code> will be executed.
     */
    private SortedMap<Integer, UpdateTaskRecord> taskMap;

    private CoreLoader coreLoader;

    private boolean doingInit;

    // All of these values are supplied by CoreLoader
    private SiteManager siteManager;

    private SampleManager sampleManager;

    private RepositoryManager repositoryManager;

    private Properties properties;

    // This value is valid for any task that does not require that the core
    // be running.
    private Connection conn;

    /**
     * The one and only constructor; called by CoreLoader with a pointer to
     * itself
     */
    public VersionUpdater(CoreLoader coreLoader) {
        this.coreLoader = coreLoader;
        taskMap = new TreeMap<Integer, UpdateTaskRecord>();
        doingInit = false;

        // Generate task records for all the SQL schema updates that need to
        // be performed.
        try {
            detectSqlTasks();
        } catch (IOException ex) {
            System.out.println("ERROR!");
            ex.printStackTrace();
        } catch (ResourceNotFoundException ex) {
            System.out.println("ERROR!");
            ex.printStackTrace();
        }

        // Generate task records for all the user-specified tasks.
        declareTasksHere();
    }

    /**
     * Called by CoreLoader during an 'init' operation, this method performs
     * all new-site initialization necessary to bring the site online. It
     * communicates with the user via the console and returns true on success.
     * <p>
     * This method connects to the database engine to create the necessary user
     * accounts and perform all offline migration tasks, then calls back to
     * CoreLoader to instantiate the core modules in bootstrap mode, performs
     * all online migration tasks, creates a single site admin user account,
     * and finally calls back to CoreLoader and shuts down the core.
     */
    public boolean doInit() {
        boolean rc;

        // This is a new site initialization, so force every migration task to
        // be run.
        lastTask = 0;
        doingInit = true;

        /*
         * Display a warning banner
         */
        String banner;
        try {
            banner = getResourceString("configure-banner.txt");
        } catch (ResourceNotFoundException ex) {
            System.out.println("failed; exact error=");
            ex.printStackTrace();
            return false;
        }
        System.out.println(banner);

        /*
         * Prompt for and obtain user consent
         */
        System.out.print("Continue? {y, n}: ");
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                System.in));
        try {
            String answer = reader.readLine().toLowerCase();
            if (!"y".equals(answer)) {
                return true;
            }
        } catch (IOException ex) {
            System.out.println("failed; exact error=");
            ex.printStackTrace();
            return false;
        }

        /*
         * Prompt for the desired site admin username and password
         */
        String siteAdminUsername, siteAdminPassword;
        try {
            System.out.print("Please specify the username of the site admin"
                    + " account to be created: ");
            siteAdminUsername = reader.readLine();
            System.out.print("  Desired password: ");
            siteAdminPassword = reader.readLine();
        } catch (IOException ex) {
            System.out.println("failed; exact error=");
            ex.printStackTrace();
            return false;
        }

        /*
         * Prompt for and obtain the database root password
         */
        String dbPassword;
        try {
            System.out.print("Please enter the database engine's root"
                    + " password: ");
            dbPassword = reader.readLine();
        } catch (IOException ex) {
            System.out.println("failed; exact error=");
            ex.printStackTrace();
            return false;
        }

        /*
         * Display the first status message
         */
        System.out.println("\nInitializing new site:");

        /*
         * Read properties from recipnetd.conf
         */
        System.out.print("  loading configuration file... ");
        rc = coreLoader.loadConfigFile();
        if (!rc) {
            System.out.println("failed");
            return false;
        }
        this.properties = coreLoader.properties;
        System.out.println("ok");

        /*
         * Drop any existing db tables
         */
        System.out.print("  dropping old db tables... ");
        rc = dropDbTables("root", dbPassword);
        if (!rc) {
            return false;
        }
        System.out.println("ok");

        /*
         * Remove any existing message files
         */
        System.out.print("  deleting old message files... ");
        for (String propName : new String[] { "SitMsgsSentDir",
                "SitMsgsRecvDir", "SitMsgsHeldDir" }) {
            String dirName = properties.getProperty(propName);

            if (dirName != null) {
                File dirFile = new File(dirName);

                if (!dirFile.isAbsolute()) {
                    System.out.print("message directory name not absolute: '");
                    System.out.print(dirName);
                    System.out.println("'");

                    return false;
                } else if (!dirFile.exists()) {
                    continue;
                } else if (!dirFile.isDirectory()) {
                    System.out.print("message directory name refers to a "
                            + "non-directory: '");
                    System.out.print(dirName);
                    System.out.println("'");

                    return false;
                } else {
                    
                    /*
                     * Note: this version does not descend subdirectories,
                     * whereas the previous (find-based) version did.  That
                     * should be OK because the site software doesn't use
                     * subdirectories of the message file directories.
                     */
                    
                    File[] files = dirFile.listFiles();

                    if (files == null) {
                        System.out.print(
                                "could not list message directory: '");
                        System.out.print(dirName);
                        System.out.println("'");

                        return false;
                    } else {
                        for (File file : files) {
                            if (file.isFile() && !file.delete()) {
                                System.out.print("could not delete file: '");
                                System.out.print(file);
                                System.out.println("'");

                                return false;
                            }
                        }
                    }
                }
            }
        }
        System.out.println("ok");

        /*
         * Remove any ISM suicide note on the filesystem.
         */
        System.out.print("  deleting old suicide notes... ");
	File suicideNote = new File(properties.getProperty("IsmSuicideNote"));
	suicideNote.delete();
        System.out.println("ok");

        /*
         * Create db user accounts
         */
        System.out.print("  creating database accounts... ");
        rc = createDbAccounts("root", dbPassword);
        if (!rc) {
            return false;
        }
        System.out.println("ok");

        /*
         * Read properties from recipnetd.conf again; this is necessary to
         * allow us to detect the newly-updated db passwords
         */
        System.out.print("  reloading configuration file... ");
        rc = coreLoader.loadConfigFile();
        if (!rc) {
            System.out.println("failed");
            return false;
        }
        this.properties = coreLoader.properties;
        System.out.println("ok");

        /*
         * Execute the migration tasks that don't depend on core. These connect
         * to the database using Site Manager's credentials.
         */
        // Status messages get written by each migration task
        rc = doVersionUpdate1();
        if (!rc) {
            return false;
        }

        /*
         * Instantiate the core modules
         */
        System.out.print("  starting core modules in bootstrap mode... ");
        rc = coreLoader.startCore(true, null);
        if (!rc) {
            System.out.println("failed; check syslog for more information");
            return false;
        }
        this.siteManager = coreLoader.siteManager;
        this.sampleManager = coreLoader.sampleManager;
        this.repositoryManager = coreLoader.repositoryManager;
        System.out.println("ok");

        /*
         * Execute the migration tasks that do depend on the core
         */
        // Status messages get written by each migration task
        rc = doVersionUpdate2();
        if (!rc) {
            coreLoader.stopCore();
            return false;
        }

        /*
         * Create site admin account
         */
        System.out.println("  creating site admin account... ");
        rc = createAdminAccount(siteAdminUsername, siteAdminPassword);
        if (!rc) {
            coreLoader.stopCore();
            return false;
        }

        /*
         * Stop core
         */
        System.out.print("  stopping core modules... ");
        coreLoader.stopCore();
        System.out.println("ok");

        System.out.println("Done!");
        return true;
    }

    /**
     * Called by CoreLoader during an 'update' operation, this method connects
     * to the database engine using Site Manager's credentials, detects the
     * previous database schema's version, performs any new offline update
     * tasks that haven't been performed yet, calls back to CoreLoader to
     * instantiate the core in bootstrap mode, performs any online update tasks
     * that haven't been performed yet, and finally calls back to CoreLoader to
     * shut down the core. This method sends status updates to the console.
     */
    public boolean doUpdate() {
        boolean rc;

        // This is a version migration, so cause the last previously-executed
        // migration task to be auto-detected.
        lastTask = -1;

        System.out.println("Performing update tasks:");

        System.out.print("  reading configuration file... ");
        rc = coreLoader.loadConfigFile();
        if (!rc) {
            System.out.println("failed");
            return false;
        }
        this.properties = coreLoader.properties;
        System.out.println("ok");

        // Status messages are displayed for each individual task
        rc = doVersionUpdate1();
        if (!rc) {
            return false;
        }

        System.out.print("  starting core modules in bootstrap mode... ");
        rc = coreLoader.startCore(true, null);
        if (!rc) {
            System.out.println("failed; check syslog for more information");
            return false;
        }
        this.siteManager = coreLoader.siteManager;
        this.sampleManager = coreLoader.sampleManager;
        this.repositoryManager = coreLoader.repositoryManager;
        System.out.println("ok");

        // Status messages are displayed for each individual task
        rc = doVersionUpdate2();
        if (!rc) {
            coreLoader.stopCore();
            return false;
        }

        System.out.print("  stopping core modules... ");
        coreLoader.stopCore();
        System.out.println("ok");

        System.out.println("Done!");
        return true;
    }

    /**
     * Called by CoreLoader during a 'rebuild' operation, this method calls
     * back to CoreLoader to instantiate the core in bootstrap mode, performs
     * any online "migration" tasks that have been flagged as "rebuild"
     * operations, and finally calls back to CoreLoader to shut down the core.
     * This method sends status updates to the console.
     */
    public boolean doRebuild() {
        boolean rc;

        System.out.println("Performing rebuild tasks:");

        // Start the core modules in bootstrap mode
        System.out.print("  starting core modules in bootstrap mode... ");
        rc = coreLoader.startCore(true, null);
        if (!rc) {
            System.out.println("  failed; check syslog for more information.");
            return false;
        }
        this.siteManager = coreLoader.siteManager;
        this.sampleManager = coreLoader.sampleManager;
        this.repositoryManager = coreLoader.repositoryManager;
        System.out.println("ok");

        /*
         * Run every task that has been flagged for inclusion in a rebuild
         * operation, in order by ascending task number.  Each individual task
         * will write its own status message.
         */
        for (UpdateTaskRecord rec : taskMap.values()) {
            if (rec.partOfRebuild) {
                System.out.print("  " + rec.message + "... ");
                try {
                    rec.task.doTask(this);
                } catch (Exception ex) {
                    System.out.println("failed; exact error=");
                    ex.printStackTrace();
                    coreLoader.stopCore();
                    return false;
                }
                System.out.println("ok");
            }
        }

        // Stop the core modules
        System.out.print("  stopping core modules... ");
        coreLoader.stopCore();
        System.out.println("ok");

        System.out.println("Done!");
        return true;
    }

    /**
     * Called by CoreLoader during a 'sync' operation, this method calls back
     * to CoreLoader to instantiate the core in bootstrap mode and then invokes
     * SiteManager.synchronizeWithSiteNetwork(). When that method returns this
     * method calls back to CoreLoader to shut down the core. Sends verbose
     * status updates to the console.
     */
    public boolean doSync() {
        boolean rc;

        System.out.println("Performing synchronization tasks (please be"
                + " patient):");

	// Load the config file.
        System.out.print("  loading configuration file... ");
        rc = coreLoader.loadConfigFile();
        if (!rc) {
            System.out.println("failed");
            return false;
        }
        this.properties = coreLoader.properties;
        System.out.println("ok");

	// Check for any suicide note.
	File suicideNote = new File(properties.getProperty("IsmSuicideNote"));
	if (suicideNote.exists()) {
            System.out.println("  Synchronization was ABORTED because this"
                + " site's messaging subsystem is halted.  This server is NOT"
                + " synchronized with the Site Network.  There is a serious"
                + " replication problem.  Please contact Reciprocal Net"
		+ " technical support.  Additional debugging information may"
		+ " be found in " + suicideNote.getPath() + " .");
	    return false;
	}	    

        // Start the core modules in bootstrap mode
        System.out.print("  starting core modules in bootstrap mode... ");
        rc = coreLoader.startCore(true, null);
        if (!rc) {
            System.out.println("  failed; check syslog for more information.");
            return false;
        }
        this.siteManager = coreLoader.siteManager;
        this.sampleManager = coreLoader.sampleManager;
        this.repositoryManager = coreLoader.repositoryManager;
        System.out.println("ok");

        // Invoke the method on Site Manager
        try {
            siteManager.synchronizeWithSiteNetwork();
        } catch (Exception ex) {
            System.out.println("failed; exact error=");
            ex.printStackTrace(System.err);
            for (Throwable t = ex.getCause(); t != null; t = t.getCause()) {
                System.err.println("Caused by:");
                t.printStackTrace(System.err);
            }

            coreLoader.stopCore();
            return false;
        }

        // Stop the core modules
        System.out.print("  stopping core modules... ");
        coreLoader.stopCore();
        System.out.println("ok");

        System.out.println("Done!");
        return true;
    }

    /**
     * Implements CoreLoader's 'setpasswords' function by reading the
     * Reciprocal Net configuration file, connecting to the database, and
     * setting the passwords and privileges for the Core modules.
     *
     * @return {@code true} if successful, {@code false} if not
     */
    public boolean doSetDbPasswords() {
        BufferedReader reader
                = new BufferedReader(new InputStreamReader(System.in));
        
        System.out.println("Updating the Reciprocal Net database with system "
                + "passwords from the Reciprocal Net configuration file...");
        System.out.println();
        
        /*
         * Prompt for and obtain the database root password
         */
        String dbPassword;
        
        try {
            System.out.print("Please enter the database engine's root"
                    + " password: ");
            dbPassword = reader.readLine();
        } catch (IOException ex) {
            System.out.println("failed; exact error=");
            ex.printStackTrace();
            return false;
        }
    
        /*
         * Read properties from recipnetd.conf
         */
        System.out.print("  loading configuration file... ");
        if (!coreLoader.loadConfigFile()) {
            System.out.println("failed");
            return false;
        }
        this.properties = coreLoader.properties;
        System.out.println("ok");
        
        /*
         * Set the passwords in the DB
         */
        System.out.print("  updating the database... ");
        if (!createDbAccounts("root", dbPassword,
                properties.getProperty("SitDbPassword"),
                properties.getProperty("SamDbPassword"),
                properties.getProperty("RepDbPassword"))) {
            System.out.println("failed");
            return false;
        }
        System.out.println("ok");

        System.out.println("Done!");
        return true;
    }

    /**
     * Implements CoreLoader's 'createaccounts' function by inventing three
     * passwords at random, connecting to the database, creating new database
     * accounts with appropriate privileges, and then writing the passwords
     * to recipnetd.conf .
     *
     * @return {@code true} if successful, {@code false} if not
     */
    public boolean doCreateDbAccounts() {
	// Prompt for and obtain the database root password.
        String dbPassword;
        BufferedReader reader
                = new BufferedReader(new InputStreamReader(System.in));        
        try {
            System.out.print("Please enter the database engine's root"
                    + " password: ");
            dbPassword = reader.readLine();
        } catch (IOException ex) {
            System.out.println("failed; exact error=");
            ex.printStackTrace();
            return false;
        }
       
        System.out.println("Creating database user accounts and passwords "
                + "for use by recipnetd...");

        /*
         * Read properties from recipnetd.conf
         */
        System.out.print("  loading configuration file... ");
        if (!coreLoader.loadConfigFile()) {
            System.out.println("failed");
            return false;
        }
        this.properties = coreLoader.properties;
        System.out.println("ok");
        
        /*
         * Set the passwords in the DB
         */
        System.out.print("  updating the database... ");
        if (!createDbAccounts("root", dbPassword)) {
            System.out.println("failed");
            return false;
        }
        System.out.println("ok");

        System.out.println("Done!");
        return true;
    }

    /**
     * Common code shared by the doInit() and doUpdate() methods, this executes
     * every needed migration task that can be performed offline. The member
     * variable <code>lastTask</code> should be set to -1 if the current state
     * of the database should be detected by querying the 'version' table, or 0
     * if all migration tasks should be performed. This code connects to the
     * database engine via JDBC, using Site Manager's credentials.
     */
    private boolean doVersionUpdate1() {
        // Establish a connection to the database that all the migration tasks
        // can use.
        System.out.print("  connecting to database... ");
        try {
            Class.forName(properties.getProperty("DbDriverClassName"));
            conn = DriverManager.getConnection(
                    properties.getProperty("DbUrlForBootstrap"),
                    properties.getProperty("SitDbUsername"),
                    properties.getProperty("SitDbPassword"));
        } catch (Exception ex) {
            System.err.println("Error connecting to the database.  Exact"
                    + " error=" + ex.toString());
            return false;
        }
        System.out.println("ok");

        // Detect what version of the site software is already installed (if
        // any, and only if needed)
        if (lastTask == -1) {
            System.out.print("  detecting previous version... ");
            try {
                Statement cmd = conn.createStatement();
                ResultSet rs = cmd.executeQuery("SELECT * FROM version;");
                rs.next();
                lastTask = rs.getInt("highestTask");
                System.out.println(lastTask);
            } catch (SQLException ex) {
                // 456 is the number of the highest task performed during an
                // upgrade/installation of recipnet-0.5.1-41, the last site
                // software release that did not support the 'version' table.
                lastTask = 456;
                System.out.println("assuming " + lastTask);
            }
        }

        // Run every task that's supposed to run before the core gets
        // instantiated, in order by ascending task number
        boolean didSomething = false;
        
        for (Map.Entry<Integer, UpdateTaskRecord> e : taskMap.entrySet()) {
            UpdateTaskRecord rec = e.getValue();

            if (!rec.needsCore && (e.getKey() > lastTask)
                    && (rec.partOfInit || !doingInit)) {
                System.out.print("  " + rec.message + "... ");
                try {
                    rec.task.doTask(this);
                } catch (Exception ex) {
                    System.out.println("failed; exact error=");
                    ex.printStackTrace();
                    return false;
                }
                System.out.println("ok");
                didSomething = true;
            }
        }

        if (!didSomething) {
            System.out.println("  no offline tasks to execute -- db schema is"
                    + " up to date");
        }

        return true;
    }

    /**
     * Common code shared by the doInit() and doUpdate() methods, this executes
     * every needed migration task that can be performed with the core online.
     * This method may be called only after a prior, successful call to
     * doVersionUpdate2 because it depends on class state being set properly.
     * The migration tasks executed by this method all invoke methods on the
     * core modules; thus, the caller should ensure that the core modules are
     * running in bootstrap mode prior to invoking this method.
     */
    private boolean doVersionUpdate2() {
        int highestTask = 0;
        boolean didSomething = false;
        
        // Run every task that's supposed to run after the core gets
        // instantiated, in order by ascending task number
        for (Map.Entry<Integer, UpdateTaskRecord> e : taskMap.entrySet()) {
            int taskNumber = e.getKey();
            UpdateTaskRecord rec = e.getValue();
            
            highestTask = taskNumber;
            if (rec.needsCore && (taskNumber > lastTask)
                    && (rec.partOfInit || !doingInit)) {
                System.out.print("  " + rec.message + "... ");
                try {
                    rec.task.doTask(this);
                } catch (Exception ex) {
                    System.out.println("failed; exact error=");
                    ex.printStackTrace();
                    return false;
                }
                System.out.println("ok");
                didSomething = true;
            }
        }

        if (!didSomething) {
            System.out.println("  no online tasks to execute -- system is"
                    + " up to date");
        }

        // Update the version table to indicate that more updates have been
        // processed. Then close the db connection.
        System.out.print("  updating version record... ");
        try {
            String sql = "UPDATE version" + " SET buildname='" + buildName
                    + "'," + "     highestTask=" + highestTask + ";";
            Statement cmd = conn.createStatement();
            cmd.executeUpdate(sql);
            cmd.close();
            conn.close();
        } catch (SQLException ex) {
            System.out.println("failed; exact error=");
            ex.printStackTrace();
            return false;
        }
        System.out.println("ok");

        return true;
    }

    /**
     * Internal function used during doInit() operation that connects to the
     * database engine and drops all the tables in 'recipnet' database. It
     * retrieves a list of tables and drops them one by one. 
     * 
     * @param dbAccount the database login name.
     * @param dbPassword the database password for user dbAccount
     */
    private boolean dropDbTables(String dbAccount, String dbPassword) {
        
        /*
         * TODO: make this method independent of the underlying database
	 * engine; 'SHOW TABLE' and 'FLUSH TABLES' syntax is not universal.
         */
        
        Connection localConn = null;
        
        try {
            // Connect to the database
            Class.forName(properties.getProperty("DbDriverClassName"));
            localConn = DriverManager.getConnection(
                    properties.getProperty("DbUrlForBootstrap"), dbAccount,
                    dbPassword);
        } catch (Exception ex) {
            System.err.println("Error connecting to the database.  Exact"
                    + " error=" + ex.toString());
            return false;
        }
        try {
            // Retrieve the list of tables
            Statement cmd = localConn.createStatement();
            ResultSet rs = cmd.executeQuery("SHOW TABLES;");
            while (rs.next()) {
                // Drop the tables one by one
		localConn.createStatement().executeUpdate(
                        "DROP TABLE IF EXISTS " + rs.getString(1) + ";");
            }

            // Force the database engine to activate the change immediately.
            localConn.createStatement().executeUpdate("FLUSH TABLES;");
        } catch (SQLException ex) {
            System.out.println("failed; exact error=");
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Internal function used during a doInit() operation that connects to the
     * database engine via JDBC and creates three new database user accounts.
     * The caller must supply to this function the "root" credentials to the
     * database.  By default the three accounts are called 'recipnet_site', 
     * 'recipnet_sample', 'recipnet_repos', although exact values are read
     * from configuration properties.  Each account is given a
     * randomly-generated password. Next, these new accounts are activated.
     * Then, the passwords are written to the recipnetd.conf file in the
     * default location. Normally this call would be used during initial site
     * configuration, during a 'drecipnet init' operation. The 'recipnet'
     * database (catalog) must already exist before this function is called.
     * 
     * @param dbAccount the database login name.
     * @param dbPassword the database password for user dbAccount
     * 
     * @return {@code true} if successful, {@code false} on any failure; in the
     *         latter case the system may have been left in an inconsistent
     *         state
     */
    private boolean createDbAccounts(String dbAccount, String dbPassword) {
        String sitePw = generateRandomPassword();
        String samplePw = generateRandomPassword();
        String reposPw = generateRandomPassword();
        boolean rval;

        rval = createDbAccounts(dbAccount, dbPassword, sitePw, samplePw,
                reposPw);
        try {
            
            // Save the passwords to the config file for future use
            changeConfigDbPasswords(sitePw, samplePw, reposPw);
            
            return rval;
        } catch (ResourceNotAccessibleException rnae) {
            System.out.println("failed; exact error=");
            rnae.printStackTrace();
            return false;
        } catch (IOException ioe) {
            System.out.println("failed; exact error=");
            ioe.printStackTrace();
            return false;
        }
    }
    
    /**
     * Updates the database passwords for the Core modules' database accounts
     * with the specfied strings, creating the appropriate users if necessary
     *
     * @param adminAccount the name of an account with DB admin privileges, to
     *        be used for granting the appropriate permissions
     * @param adminPassword the password to be used to connect to the named
     *        admin account
     * @param sitePw the password to set for Site Manager
     * @param samplePw the password to set for Sample Manager
     * @param reposPw the password to set for Repository Manager
     * 
     * @return {@code true} if successful, {@code false} on any failure; in the
     *         latter case the database may have been left in an inconsistent
     *         state
     */
    private boolean createDbAccounts(String adminAccount, String adminPassword,
            String sitePw, String samplePw, String reposPw) {
        /*
         * TODO: make this call independent of the underlying database engine;
         * 'SET PASSWORD' and 'FLUSH PRIVILEGES' syntax is not universal.
         */
        
        try {
            // Connect to the database and prepare a batch of SQL statements.
            Class.forName(properties.getProperty("DbDriverClassName"));
            Connection localConn = DriverManager.getConnection(
                    properties.getProperty("DbUrlForBootstrap"), adminAccount,
                    adminPassword);
            Statement cmd = localConn.createStatement();            

	    // Tell the db engine to create three accounts.
            cmd.addBatch("GRANT ALL ON recipnet.* TO " 
                    + properties.getProperty("SitDbUsername") + ";");
            cmd.addBatch("SET PASSWORD FOR " 
                    + properties.getProperty("SitDbUsername") 
                    + "@'%' = PASSWORD('" + sitePw + "');");
            cmd.addBatch("GRANT ALL ON recipnet.* TO " 
                    + properties.getProperty("SamDbUsername") + ";");
            cmd.addBatch("SET PASSWORD FOR " 
                    + properties.getProperty("SamDbUsername") 
                    + "@'%' = PASSWORD('" + samplePw + "');");
            cmd.addBatch("GRANT ALL ON recipnet.* TO " 
                    + properties.getProperty("RepDbUsername") + ";");
            cmd.addBatch("SET PASSWORD FOR " 
                    + properties.getProperty("RepDbUsername") 
                    + "@'%' = PASSWORD('" + reposPw + "');");

            // Force the database engine to activate the new accounts
            // immediately.
            cmd.addBatch("FLUSH PRIVILEGES;");

            // Perform the database transactions.
            cmd.executeBatch();
            cmd.close();
            localConn.close();
            
            return true;
        } catch (SQLException ex) {
            System.out.println("failed; exact error=");
            ex.printStackTrace();
            return false;
        } catch (ClassNotFoundException ex) {
            System.out.println("failed; exact error=");
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Internal function used during a doInit() operation that connects to a
     * fully-initialized Site Manager and creates a new user record that has
     * the specified name and password. The new account belongs to the first
     * lab hosted at the site and posesses site admin privileges.
     * 
     * @param username the name of the account to create.
     * @param password the password to assign the new account.
     */
    private boolean createAdminAccount(String username, String password) {
        try {
            // Figure out which site this is and which labs are hosted here.
            // Choose the first lab.
            // siteId = siteManager.localSiteId;
            int labId = siteManager.getLocalLabs()[0];

            // Build a UserInfo object that has the specified credentials
            UserInfo user = siteManager.getEmptyUserInfo();
            user.labId = labId;
            user.creationDate = new Date();
            user.isActive = true;
            user.username = username;
            user.fullName = "Default site administrator";
            user.globalAccessLevel = UserInfo.SITE_ADMIN_ACCESS
                    | UserInfo.LAB_SCIENTIFIC_USER_ACCESS;
            UserBL.setPassword(user, password);

            // Actually create the user account
            user.id = siteManager.writeUpdatedUserInfo(user);

            System.out.println("    created site admin account '"
                    + user.username + "' for lab id " + user.labId);
        } catch (OperationFailedException ex) {
            System.out.println("failed; exact error=");
            ex.printStackTrace();
            return false;
        } catch (DuplicateDataException ex) {
            System.out.println("failed; exact error=");
            ex.printStackTrace();
            return false;
        } catch (InvalidDataException ex) {
            System.out.println("failed; exact error=");
            ex.printStackTrace();
            return false;
        } catch (InvalidModificationException ex) {
            System.out.println("failed; exact error=");
            ex.printStackTrace();
            return false;
        } catch (WrongSiteException ex) {
            System.out.println("failed; exact error=");
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    private void declareTask(int taskNumber, boolean needsCore,
            boolean partOfInit, boolean partOfRebuild, String message,
            UpdateTask task) {
        UpdateTaskRecord rec = new UpdateTaskRecord();
        
        rec.needsCore = needsCore;
        rec.partOfInit = partOfInit;
        rec.partOfRebuild = partOfRebuild;
        rec.message = message;
        rec.task = task;
        taskMap.put(Integer.valueOf(taskNumber), rec);
    }

    /**
     * Internal function that creates task records for every SQL db schema
     * migration task and inserts them into the <code>taskMap</code>. The
     * tasks are detected a) by the presence of a .sql in the parent JAR file,
     * provided that b) the .sql file name is listed in the text file named
     * 'sql.filelist', also contained in the parent JAR file. Each .sql file
     * represents one implicitely-defined task; the task number is taken from
     * the first six characters of the filename.
     * 
     * @throws IOException
     * @throws ResourceNotFoundException
     */
    private void detectSqlTasks() throws IOException,
            ResourceNotFoundException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                ClassLoader.getSystemClassLoader().getResourceAsStream(
                        "sql.filelist")));
        for (String sqlFileName = reader.readLine();
                sqlFileName != null;
                sqlFileName = reader.readLine()) {
            String sql = getResourceString(sqlFileName);
            Integer taskNumber = new Integer(sqlFileName.substring(0, 6));
            UpdateTaskRecord rec = new UpdateTaskRecord();
            
            rec.needsCore = false;
            rec.partOfInit = true;
            rec.partOfRebuild = false;

            // Parse the "message" from the sql string. It's on the third line
            // of the file beginning with the fourth character
            StringTokenizer tok = new StringTokenizer(sql, "\n");
            tok.nextToken();
            tok.nextToken();
            rec.message = tok.nextToken().substring(3);

            rec.task = new sqlUpdateTask(sql);
            taskMap.put(taskNumber, rec);
        }
    }

    private String generateRandomPassword() {
        SecureRandom r = new SecureRandom();
        StringBuilder sb;
        
        for (sb = new StringBuilder(8); sb.length() < 8; ) {
            sb.append((char) (r.nextInt(26) + 97));
        }
        
        return sb.toString();
    }

    private void changeConfigDbPasswords(String sitDbPassword,
            String samDbPassword, String repDbPassword) throws IOException,
            ResourceNotAccessibleException {
        File confFile = new File("/etc/recipnet/recipnetd.conf");
        if (!confFile.isFile() || !confFile.canWrite()) {
            throw new ResourceNotAccessibleException("cannot write to file "
                    + confFile.getPath() + " ; check file permissions.",
                    confFile);
        }

        // Read the contents of the file into a list of Strings.
        BufferedReader oldReader = new BufferedReader(new InputStreamReader(
                new FileInputStream(confFile)));
        List<String> linesOfFile = new ArrayList<String>();
        
        for (String line = oldReader.readLine();
                line != null;
                line = oldReader.readLine()) {
            linesOfFile.add(line);
        }
        oldReader.close();

        // Now copy the contents of the old config file to a new one.
        // We will replace the three database passwords as we go.
        PrintWriter newWriter = new PrintWriter(new BufferedOutputStream(
                new FileOutputStream(confFile)));
        
        for (String fileLine : linesOfFile) {
            if (fileLine.startsWith("SitDbPassword" + "=")) {
                fileLine = "SitDbPassword" + "=" + sitDbPassword;
            }
            if (fileLine.startsWith("SamDbPassword" + "=")) {
                fileLine = "SamDbPassword" + "=" + samDbPassword;
            }
            if (fileLine.startsWith("RepDbPassword" + "=")) {
                fileLine = "RepDbPassword" + "=" + repDbPassword;
            }

            newWriter.println(fileLine);
        }
        newWriter.flush();
        newWriter.close();
    }

    private static String getResourceString(String filename)
            throws ResourceNotFoundException {
        StringWriter sw = new StringWriter();

        try {
            InputStream is 
                    = ClassLoader.getSystemClassLoader().getResourceAsStream(
                    filename);
            
            if (is == null) {
                throw new ResourceNotFoundException(filename);
            }
            
            Reader isr = new InputStreamReader(is, "UTF-8");
            char[] buf = new char[4096];
            
            for (int charsRead = isr.read(buf, 0, buf.length);
                    charsRead > 0;
                    charsRead = isr.read(buf, 0, buf.length)) {
                sw.write(buf, 0, charsRead);
            }
            isr.close();
            sw.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        
        return sw.toString();
    }

    /**
     * Retrieves the current version from the database.
     * 
     * @param conn a connection to the database
     * @return a <code>String</code> containing the build number of the
     *         version of Recipnet that is running.
     */
    public static String getCurrentVersion(Connection conn)
            throws SQLException {
        Statement cmd = conn.createStatement();
        ResultSet rs = cmd.executeQuery("SELECT * FROM version;");
        rs.first();
        return rs.getString("buildname");
    }

    /*
     * FIXME: The use of this interface is consistent with always implementing
     * it via inner VersionUpdater inner classes, in which case it would never
     * be necessary to pass the containing VersionUpdater instance
     */
    
    static interface UpdateTask {
        void doTask(VersionUpdater updater) throws Exception;
    }

    static class UpdateTaskRecord {
        public boolean needsCore;

        public boolean partOfInit;

        public boolean partOfRebuild;

        public String message;

        public UpdateTask task;
    }

    static class sqlUpdateTask implements UpdateTask {
        private String sql;

        public sqlUpdateTask(String sql) {
            this.sql = sql;
        }

        public void doTask(VersionUpdater updater) throws Exception {
            Statement cmd = updater.conn.createStatement();

            // Split the SQL string into zero or more single SQL statements
            // TODO: this logic should be modified so that it doesn't choke
            // on semi-colons in non-significant places like comments
            // and quoted strings
            StringTokenizer tok = new StringTokenizer(sql, ";");
            while (tok.hasMoreTokens()) {
                String sqlStatement = tok.nextToken();

                // quick test to see if the sql statement contains nothing
                // but whitespace
                if (sqlStatement.trim().length() != 0) {
                    sqlStatement += ";";
                    cmd.executeUpdate(sqlStatement);
                }
            }
            cmd.close();
        }
    }

}
