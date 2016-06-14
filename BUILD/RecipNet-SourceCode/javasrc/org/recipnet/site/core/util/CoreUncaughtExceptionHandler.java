/*
 * Reciprocal Net project
 * 
 * CoreUncaughtExceptionHandler.java
 *
 * 15-Feb-2006: ekoperda wrote first draft
 * 01-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.site.core.util;

import org.recipnet.site.core.SiteManager;

/**
 * A simple utility class designed to serve as a thread's uncaught exception
 * handler, as described by {@code Thread.setUncaughtExceptionHandler()}. The
 * present implementation, upon receiving notice of an uncaught exception,
 * connects to {@code SiteManager} and writes the details of the exception to
 * the log. Actual formatting of the log message is delegated to
 * {@code LogRecordGenerator}.
 */
public class CoreUncaughtExceptionHandler implements
        Thread.UncaughtExceptionHandler {
    private final Object component;

    private final SiteManager siteManager;

    /**
     * Initializes a new {@code CoreUncaughtExceptionHandler} with the specified
     * parameters
     * 
     * @param component a reference to the core module that will be the source
     *        of any uncaught exceptions received. This module may be identified
     *        in any log messages that are written later.
     * @param siteManager a reference to Site Manager, used for writing log
     *        messages.
     */
    public CoreUncaughtExceptionHandler(Object component,
            SiteManager siteManager) {
        this.component = component;
        this.siteManager = siteManager;
    }

    public void uncaughtException(
            @SuppressWarnings("unused") Thread thread, Throwable ex) {
        this.siteManager.recordLogRecord(LogRecordGenerator.threadException(
                this.component, ex));
    }
}
