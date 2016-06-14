/*
 * Reciprocal Net project
 * 
 * CoreShutdown.java
 *
 * 07-Jun-2002: ekoperda wrote first draft
 * 17-Jun-2002: ekoperda modified shutdown sequence to utilize 
 *              RepositoryManager.stop() and SampleManager.stop() methods.
 * 12-Jul-2002: ekoperda added configurable shutdown time support
 * 04-Sep-2002: ekoperda added logging support throughout
 * 15-Apr-2003: midurbin moved logging code to LogRecordGenerator
 * 26-May-2006: jobollin reformatted the source, reorganized it to rely on
 *              a CoreLoader for access to Core components and configuration,
 *              and made this class Runnable without being a Thread
 */

package org.recipnet.site.core;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.recipnet.site.core.util.LogRecordGenerator;

/**
 * This is the core shutdown thread. It gets registered as a "Shutdown Hook" in
 * the VM by the CoreLoader class. This thread is activated -- its run() method
 * is called -- immediately before the Java VM terminates. This class is
 * responsible for initiating and managing the core's shutdown. This includes
 * signalling worker threads in the three Core modules to terminate.
 */
public class CoreShutdown implements Runnable {
    private final Logger logger;
    
    private final CoreLoader coreLoader;

    /**
     * Initializes a {@code CoreShutdown} appropriate for shutting down the Core
     * instance started by the specified {@code CoreLoader}
     * 
     * @param loader the {@code CoreLoader} that started the Core instance that
     *        this {@code CoreShutdown} is intended to shut down; a reference
     *        to this object is retained, and this {@code CoreShutdown} relies
     *        on it at run time to obtain the main core components and
     *        configuration the determine what to shut down and how
     */
    public CoreShutdown(CoreLoader loader) {
        coreLoader = loader;
        logger = Logger.getLogger(getClass().getName());
        logger.setLevel(Level.parse(
                loader.getProperties().getProperty("LogLevel")));
    }
    
    public void run() {
        SiteManager siteManager = coreLoader.getSiteManager();
        SampleManager sampleManager = coreLoader.getSampleManager();
        RepositoryManager repositoryManager = coreLoader.getRepositoryManager();
        Properties properties = coreLoader.getProperties();
        int shutdowntime;

        logger.log(LogRecordGenerator.daemonShuttingDown());

        // Signal each of the three Core modules to shut down
        repositoryManager.stop();
        shutdowntime = Integer.parseInt(properties.getProperty(
                "RepWorkerThreadShutdownTime", "5000"));
        repositoryManager.shutdownSignal.receive(shutdowntime);

        sampleManager.stop();
        shutdowntime = Integer.parseInt(properties.getProperty(
                "SamWorkerThreadShutdownTime", "5000"));
        sampleManager.shutdownSignal.receive(shutdowntime);

        siteManager.stop();
        shutdowntime = Integer.parseInt(properties.getProperty(
                "SitWorkerThreadShutdownTime", "5000"));
        siteManager.shutdownSignal.receive(shutdowntime);

        // All done
        logger.log(LogRecordGenerator.daemonStopped());
    }
}
