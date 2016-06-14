/*
 * Reciprocal Net project
 * 
 * CoreLoader.java
 *
 * 06-Jun-2002: ekoperda wrote first draft
 * 12-Jul-2002: ekoperda added config file and bootstrap mode support
 * 30-Jul-2002: ekoperda did a major rework to support drecipnet's
 *              configuration mode
 * 30-Aug-2002: ekoperda wrote handler for 'dbupdate' mode.
 * 04-Sep-2002: ekoperda added logging support throughout
 * 11-Sep-2002: ekoperda added dbrebuild mode and code to invoke a rebuild
 *              as part of the dbupdate mode
 * 17-Sep-2002: ekoperda added dbupdate code to correct for bug #435 that
 *              caused corrupt level values in sampleAnnotations
 * 18-Sep-2002: ekoperda added dbupdate and dbrebuild code to call the new
 *              function SampleManager.rebuildSampleData()
 * 27-Sep-2002: ekoperda added dbupdate code to create the 'publicSeqNum' and
 *              'privateSeqNum' fields on the 'sites' table
 * 15-Oct-2002: ekoperda added dbupdate code to create and populate the
 *              'searchLocalHoldings' table
 * 23-Oct-2002: ekoperda moved all the init/update/rebuild code to the new
 *              class core.util.VersionUpdater 
 * 04-Nov-2002: ekoperda added new command-line option 'sync'
 * 15-Apr-2003: midurbin moved logging code to LogRecordGenerator
 * 28-Jul-2003: nsanghvi modified main() and startCore() to run an external
 *              program after core modules' startup
 * 07-May-2004: cwestnea modified startCore() to provide RMI registry
 * 26-May-2006: jobollin reformatted the source, updated docs
 * 16-Nov-2006: jobollin added support for the 'setpasswords' option
 * 04-Jan-2008: ekoperda added new mode 'createaccounts' and enhanced main()
 */

package org.recipnet.site.core;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.recipnet.site.OperationFailedException;
import org.recipnet.site.core.util.CoreProcessWrapper;
import org.recipnet.site.core.util.LogRecordGenerator;

/**
 * <p>
 * Implementation of the recipnetd daemon. This program is intended to be
 * called from the command line. This class in turn creates single instances of
 * Site Manager, Sample Manager, and Repository Manager that together comprise
 * the "core modules" for this particular site. Separate worker threads are
 * created in each core module. The core must be running for the other recipnet
 * modules at this site to function properly.
 * </p><p>
 * If started with one of the command-line arguments 'init', 'rebuild', 'sync',
 * or 'update' then a corresponding special configuration modes is entered;
 * the bulk of these operations are dispatched to the {@code VersionUpdater}
 * class.
 * </p>
 */
public class CoreLoader {
    
    /**
     * The configuration properties loaded from the Reciprocal Net Core's
     * configuration file
     */
    public Properties properties = null;

    /**
     * The {@code Logger} configured for the Core's use
     */
    private Logger logger = null;

    /**
     * The {@code SiteManager} instance for this Core instance
     */
    public SiteManager siteManager = null;

    /**
     * The {@code SampleManager} instance for this Core instance
     */
    public SampleManager sampleManager = null;

    /**
     * The {@code RepositoryManager} instance for this Core instance
     */
    public RepositoryManager repositoryManager = null;

    /**
     * The entry point for this program.  Several specific functions are
     * available, as selected by the command-line argument(s); see the
     * {@link CoreLoader class-level docs} for more information.
     * 
     * @param args a {@code String[]} containing the command-line arguments
     *        with which this program was invoked
     */
    public static void main(String[] args) {

        /*
         * Dispatch to a "doer" method depending upon the first command-line
         * argument
         */
	boolean commandLineParsed = true;
        if (args.length < 1) {
	    commandLineParsed = false;
	} else if (args[0].equals("run")) {
            CoreLoader oneInstance = new CoreLoader();
            
            if (!oneInstance.startCore(false,
                    ((args.length >= 2) ? args[1] : null))) {
                System.exit(1);
            }
        } else if (args[0].equals("init")) {
            VersionUpdater updater = new VersionUpdater(new CoreLoader());
            System.exit(args.length >= 1 && updater.doInit() ? 0 : 2);
        } else if (args[0].equals("update")) {
            VersionUpdater updater = new VersionUpdater(new CoreLoader());
            System.exit(updater.doUpdate() ? 0 : 3);
        } else if (args[0].equals("rebuild")) {
            VersionUpdater updater = new VersionUpdater(new CoreLoader());
            System.exit(updater.doRebuild() ? 0 : 4);
        } else if (args[0].equals("sync")) {
            VersionUpdater updater = new VersionUpdater(new CoreLoader());
            System.exit(updater.doSync() ? 0 : 5);
        } else if (args[0].equals("setpasswords")) {
            VersionUpdater updater = new VersionUpdater(new CoreLoader());
            System.exit(updater.doSetDbPasswords() ? 0 : 6);
        } else if (args[0].equals("createaccounts")) {
            VersionUpdater updater = new VersionUpdater(new CoreLoader());
            System.exit(updater.doCreateDbAccounts() ? 0 : 7);
        } else {
	    commandLineParsed = false;
	}

	if (!commandLineParsed) {
            System.err.println("drecipnet: unknown option");
	    System.err.println("  drecipnet {start|stop|run|update|rebuild"
			       + "|setpasswords|createaccounts|");
	    System.err.println("             init|sync}");
            System.exit(1);
        }

        /*
         * The only way we can get here is if we were passed the 'run'
	 * parameter and the core started up successfully. The Java VM will
	 * continue to run after this thread terminates because the Core
	 * modules' worker threads are running.
         */
    }

    /**
     * Retrieves the configuration properties for the Core instance being
     * loaded
     * 
     * @return the configuration properties for the Core instance being loaded,
     *         as a {@code Properties}
     */
    public Properties getProperties() {
        return properties;
    }

    /**
     * Returns the Site Manager component of the Core instance being loaded
     * 
     * @return the {@code SiteManager} component
     */
    public SiteManager getSiteManager() {
        return siteManager;
    }

    /**
     * Returns the Sample Manager component of the Core instance being loaded
     * 
     * @return the {@code SampleManager} component
     */
    public SampleManager getSampleManager() {
        return sampleManager;
    }

    /**
     * Returns the Repository Manager component of the Core instance being
     * loaded
     * 
     * @return the {@code RepositoryManager} component
     */
    public RepositoryManager getRepositoryManager() {
        return repositoryManager;
    }

    /**
     * Instantiates the core modules and, if bootstrapMode is false (normal
     * operation), registers CoreShutdown as a shutdown handler and reports the
     * successful daemon startup to syslog. Also, if {@code commandToExecute} 
     * is specified, the external program identified by this argument is
     * invoked once startup has finished successfully -- a typical use for this
     * facility would be to signal an external caller that startup has
     * completed. The method also loads the properties file and initializes the
     * logging subsystem.
     * 
     * @param bootstrapMode boolean variable indicating the startup mode.
     * @param commandToExecute the command to be used to signal the process
     *        initiating the CoreLoader, or null.
     *        
     * @return {@code true} if the core was successfully started, or
     *         {@code false} if not
     */
    public boolean startCore(boolean bootstrapMode, String commandToExecute) {

        /*
         * This sets the member variable named 'properties' by reading the file
         * recipnetd.conf.
         */
        if (!loadConfigFile()) {
            return false;
        }

        // Initialize the logging subsystem
        logger = Logger.getLogger(CoreLoader.class.getName());
        logger.setLevel(Level.parse(properties.getProperty("LogLevel")));

        /*
         * Initialize RMI registry. It gets angry and dies if there is already
         * one running
         */
        try {
            int rmiPort = Integer.parseInt(
                    properties.getProperty("GenRmiPort"));
            LocateRegistry.createRegistry(rmiPort);
        } catch (RemoteException ex) {
            logger.log(LogRecordGenerator.rmiRegistryStartupException(ex));
            return false;
        }

        // Instantiate the core modules and tell them about one another
        try {
            siteManager = new SiteManager(properties, bootstrapMode);
            sampleManager = new SampleManager(properties, bootstrapMode);
            repositoryManager 
                    = new RepositoryManager(properties, bootstrapMode);
        } catch (RemoteException ex) {
            logger.log(LogRecordGenerator.daemonFailedToStart(ex));
            return false;
        }
        siteManager.sampleManager = sampleManager;
        siteManager.repositoryManager = repositoryManager;
        sampleManager.siteManager = siteManager;
        sampleManager.repositoryManager = repositoryManager;
        repositoryManager.siteManager = siteManager;
        repositoryManager.sampleManager = sampleManager;

        // Tell each core module to start itself
        if (!siteManager.start()) {
            logger.log(LogRecordGenerator.componentFailedToStart(siteManager));
            
            return false;
        }
        if (!sampleManager.start()) {
            logger.log(LogRecordGenerator.componentFailedToStart(
                    sampleManager));
            siteManager.stop();
            siteManager.shutdownSignal.receive(5000);
            
            return false;
        }
        if (!repositoryManager.start()) {
            logger.log(LogRecordGenerator.componentFailedToStart(
                    repositoryManager));
            sampleManager.stop();
            sampleManager.shutdownSignal.receive(5000);
            siteManager.stop();
            siteManager.shutdownSignal.receive(5000);
            
            return false;
        }

        /*
         * Report success by invoking the external program specified by the
         * caller, if one was specified.
         */
        if (commandToExecute != null) {
            try {
                CoreProcessWrapper.exec(commandToExecute, true, this.logger);
            } catch (OperationFailedException ex) {
                logger.log(LogRecordGenerator.daemonStartupSignalException(
                        commandToExecute, ex));
                repositoryManager.stop();
                repositoryManager.shutdownSignal.receive(5000);
                sampleManager.stop();
                sampleManager.shutdownSignal.receive(5000);
                siteManager.stop();
                siteManager.shutdownSignal.receive(5000);
                
                return false;
            }
        }

        /*
         * TODO: we really ought to wait to receive a startup signal from each
         * module's worker thread before we return
         */

        // Register a shutdown handler unless we're in bootstrap mode
        if (!bootstrapMode) {
            Runtime.getRuntime().addShutdownHook(
                    new Thread(new CoreShutdown(this)));
        }

        // Write a success message to syslog unless we're in bootstrap mode
        if (!bootstrapMode) {
            logger.log(LogRecordGenerator.daemonStarted());
        }

        return true;
    }

    /**
     * Instructs the core modules started by this {@code CoreLoader} to shut
     * down cleanly by invoking each one's {@code stop()} method.  It is not
     * necessary to invoke this method unless the core modules were started in
     * bootstrap mode.
     * 
     * @return {@code true} if the Core was shut down successfully,
     *         {@code false} if not.  (<i>N.b.: this version always returns
     *         {@code true}, or possibly throws an exception.</i>)
     */
    public boolean stopCore() {
        repositoryManager.stop();
        sampleManager.stop();
        siteManager.stop();

        siteManager = null;
        sampleManager = null;
        repositoryManager = null;

        return true;
    }

    /**
     * Reads the Core's configuration file and parses it into this
     * {@code CoreLoader}'s {@code Properties} object.  Errors that occur
     * during this operation are printed to {@code System.err} rather than
     * being written to the system log because the Java logging subsystem is
     * not configured until after the config file has been parsed.
     * 
     * @return {@code true} if the configuration properties were successfully
     *         loaded, {@code false} if not
     */
    public boolean loadConfigFile() {
        File recipnetdConf = new File("/etc/recipnet/recipnetd.conf");

        if (!recipnetdConf.canRead() || !recipnetdConf.isFile()) {
            System.err.println("File not found while starting recipnetd:"
                    + " The configuration file " + recipnetdConf.getName()
                    + " could not be read.");
            return false;
        }
        
        try {
            InputStream is = new BufferedInputStream(
                    new FileInputStream(recipnetdConf));
            
            try {
                properties = new Properties();
                properties.load(is);
            } finally {
                try {
                    is.close();
                } catch (IOException ioe) {
                    // ignore it
                }
            }
            
            return true;
        } catch (IOException ex) {
            System.err.println("File I/O error reading conf file while"
                    + " starting recipnetd: " + ex.toString());
            return false;
        }
    }
}
