/*
 * Reciprocal Net Project
 *
 * FileTracker.java
 *
 * 21-Oct-2002: jobollin wrote first draft
 * 20-Jun-2003: ajooloor added logging support 
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.content.servlet
 *              package to org.recipnet.site.wrapper; also changed package
 *              references to match source tree reorganization
 * 01-Jun-2004: cwestnea modified file throughout to use CoreConnector
 * 28-Jun-2004: ekoperda added the Listener interface and support for
 *              registered listeners throughout
 * 23-May-2006: jobollin reformatted the source, updated docs, fixed trackFile()
 *              to use the *recoverable* file length to update the
 *              currentFileSize
 */

package org.recipnet.site.wrapper;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.recipnet.site.UnexpectedExceptionException;
import org.recipnet.site.core.SiteManagerRemote;
import org.recipnet.site.shared.logevent.FileTrackerLogEvent;

/**
 * A class managing a collection of files and their types.  Instances have
 * configurable capacity and timeout, and automatically handle purging stale
 * entries and keeping the total size of tracked files within the configured
 * capacity. 
 * 
 * @author John C. Bollinger
 * @version 0.9.0
 * @since 0.5.2
 */
public class FileTracker implements Serializable, ServletContextListener {

    /**
     * The default maximum combined size of the files tracked that will be
     * tracked by a {@code FileTracker} instance 
     */
    public static final long DEFAULT_MAX_SIZE = 536870912L; // 512MB

    /**
     * The attribute name with which an instance binds itself to a servlet
     * context at context initialization when used as a
     * {@code ServletContextListener} 
     */
    public static final String DEFAULT_ATTRIBUTE_NAME
            = "org.recipnet.site.content.servlet.FileTracker";

    /**
     * The default entry timeout, in milliseconds
     */
    public static final long DEFAULT_TIMEOUT = 1800000L; // 30 minutes

    private static final long MAX_TIMEOUT_FREQUENCY = 60000L; // 1 minute

    private final LinkedHashMap<Long, TrackedFile> fileMap;

    private ServletContext context;

    private long maxFileSize;

    private long currentFileSize;

    private long timeoutMillis;

    private final SecureRandom random;

    private File tempDir;

    private long lastTimeoutSweep;

    // reference to servlet context's core connector
    private CoreConnector coreConnector;

    /**
     * Contains {@code Listener} objects that are to receive notifications.
     * Initialized by the constructor and populated by
     * {@code registerListener()}. Caller must synchronize on the
     * {@code FileTracker}'s monitor before accessing this collection to ensure
     * thread safety.
     */
    private final Collection<Listener> registeredListeners;

    /**
     * constructs a new FileTracker with default parameters
     */
    public FileTracker() {
        this(DEFAULT_MAX_SIZE, DEFAULT_TIMEOUT);
    }

    /**
     * constructs a {@code FileTracker} with the specified maximum total file
     * size and timeout
     * 
     * @param max the maximum combined file size, in bytes, that this
     *        {@code FileTracker} will permit before trimming the cache
     * @param timeout the maximum time since last use, in milliseconds, before a
     *        tracked file is considered eligible for removal because of its age
     */
    protected FileTracker(long max, long timeout) {
        /*
         * The fileMap's iterators proceed in access order, from least recently
         * accessed to most recently accessed entry
         */
        fileMap = new LinkedHashMap<Long, TrackedFile>(256, 0.75f, true);
        maxFileSize = max;
        currentFileSize = 0L;
        timeoutMillis = timeout;
        lastTimeoutSweep = 0L;
        registeredListeners = new ArrayList<Listener>();
        try {
            random = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException nsae) {
            throw new UnexpectedExceptionException(nsae);
        }
    }

    /**
     * obtains the {@code FileTracker} instance associated with the specified
     * {@code ServletContext}, if any
     * 
     * @param context the {@code ServletContext} for which to obtain a
     *        {@code FileTracker}
     * @return the {@code FileTracker}, if any, associated with {@code context};
     *         {@code null} if no {@code FileTracker} is associated
     */
    public static FileTracker getFileTracker(ServletContext context) {
        synchronized (context) {
            return (FileTracker) context.getAttribute(DEFAULT_ATTRIBUTE_NAME);
        }
    }

    /**
     * registers the specified file and type with this {@code FileTracker};
     * equivalent to invoking {@code trackFile(f, type, false)}.  Note
     * especially that files registered for tracking via this method do not
     * count against this tracker's file size limit.
     * 
     * @param file the {@code File}
     * @param type a {@code String} containing the type information
     * @return a {@code long} key by which the tracked file can be accessed.
     * @throws IOException if could not connect to sitemanager
     */
    public long trackFile(File file, String type) throws IOException {
        return trackFile(file, type, false);
    }

    /**
     * registers the specified file and type with this {@code FileTracker}, and
     * optionally flags the file for deletion from the filesystem upon removal
     * from tracking; files not flagged for auto-deletion do not count against
     * this tracker's aggregate file size limit
     * 
     * @param file the {@code File}
     * @param type a {@code String} containing the type information
     * @param deleteOnInvalidation indicates whether or not {@code file} should
     *        be deleted when it is removed from tracking
     * @return a {@code long} key by which the tracked file can be accessed
     * @throws IOException if could not connect to Site Manager
     */
    public long trackFile(File file, String type, boolean deleteOnInvalidation)
            throws IOException {
        SiteManagerRemote siteManager = this.coreConnector.getSiteManager();
        TrackedFile tf = new TrackedFile(file, type, deleteOnInvalidation);
        Long key;

        if (random == null) {
            throw new IllegalStateException(
                    "Random number generator not initialized");
        }
        siteManager.recordLogEvent(new FileTrackerLogEvent(
                FileTrackerLogEvent.REQUEST_RECEIVED, file.toString(), type,
                deleteOnInvalidation));

        synchronized (this) {
            for (key = Long.valueOf(random.nextLong());
                    fileMap.containsKey(key);
                    key = Long.valueOf(random.nextLong())) { /* do nothing */ }
            fileMap.put(key, tf);

            currentFileSize += tf.getRecoverableLength();
            trimToSize();
            forgetExpiredFiles();
        }
        siteManager.recordLogEvent(new FileTrackerLogEvent(
                FileTrackerLogEvent.ASSIGN_KEY, file.toString(),
                key.longValue()));

        return key.longValue();
    }

    /**
     * Removes enough files from this tracker (minimum zero) to bring the
     * aggregate size of the tracked files below the maximum size
     * 
     * @throws IOException if could not connect to Site Manager
     */
    private synchronized void trimToSize() throws IOException {
        if (currentFileSize > maxFileSize) {
            Iterator<Entry<Long, TrackedFile>>
                    entryIterator = fileMap.entrySet().iterator();
            
            while ((currentFileSize > maxFileSize)
                    && entryIterator.hasNext()) {
                Entry<Long, TrackedFile> entry = entryIterator.next();
                
                entryIterator.remove();
                forgetFile(entry.getValue());
            }
        }
    }

    /**
     * removes from tracking those files whose timeout has expired, by invoking
     * forgetFile on them
     * 
     * @throws IOException if could not connect to sitemanager
     */
    private synchronized void forgetExpiredFiles() throws IOException {
        long time = System.currentTimeMillis();

        // Timeouts are checked at most once a minute
        if (time - lastTimeoutSweep >= MAX_TIMEOUT_FREQUENCY) {
            lastTimeoutSweep = time;

        for (Iterator<Entry<Long, TrackedFile>>
                entryIterator = fileMap.entrySet().iterator();
                entryIterator.hasNext(); ) {
            TrackedFile tf = entryIterator.next().getValue();
            if (tf.getRelativeAge(time) <= timeoutMillis) {
                break;
            } else {
                entryIterator.remove();
                forgetFile(tf);
            }
        }
        }
    }

    /**
     * removes the specified file from tracking. If the {@code key} is not
     * recognized then this method has no effect and returns gracefully.
     * 
     * @param key a {@code long} key corresponding to the file to forget
     * @throws IOException if could not connect to sitemanager
     */
    public synchronized void forgetFile(long key) throws IOException {
        SiteManagerRemote siteManager = this.coreConnector.getSiteManager();
        
        siteManager.recordLogEvent(new FileTrackerLogEvent(
                FileTrackerLogEvent.TRACKING_REQUEST_RECEIVED, key));
        forgetFile(fileMap.remove(Long.valueOf(key)));
    }

    /**
     * invalidates the provided {@code TrackedFile} and frees the associated
     * space in this {@code FileTracker}. If {@code tf} is null, this method
     * has no effect and returns gracefully.
     * 
     * @param tf a {@code TrackedFile} to forget, or null.
     * @throws IOException if could not connect to sitemanager
     */
    protected synchronized void forgetFile(TrackedFile tf)
            throws IOException {
        if (tf != null) {
            SiteManagerRemote siteManager = this.coreConnector.getSiteManager();
            
            siteManager.recordLogEvent(new FileTrackerLogEvent(
                    FileTrackerLogEvent.FILE_REMOVED, tf.getFile().toString()));
            tf.invalidate();
            currentFileSize -= tf.getRecoverableLength();

            // Notify all listeners about the forgotten file.
            for (Listener listener : registeredListeners) {
                listener.notifyTrackedFileForgotten(tf);
            }
        }
    }

    /**
     * retrieves the {@code TrackedFile} associated with the provided key, if
     * any
     * 
     * @param key a {@code long} identifying the requested tracked file
     * @return the {@code TrackedFile} associated with {@code key}, or
     *         {@code null} if there is none
     * @throws IOException if could not connect to sitemanager
     */
    public synchronized TrackedFile getTrackedFile(long key)
            throws IOException {
        TrackedFile tf = fileMap.get(Long.valueOf(key));
        SiteManagerRemote siteManager = this.coreConnector.getSiteManager();

        if (tf != null) {
            siteManager.recordLogEvent(new FileTrackerLogEvent(
                    FileTrackerLogEvent.RETRIEVE_SUCCESS, key));
            tf.setTimestamp(System.currentTimeMillis());
        } else {
            siteManager.recordLogEvent(new FileTrackerLogEvent(
                    FileTrackerLogEvent.RETRIEVE_FAILURE, key));
        }

        forgetExpiredFiles();
        return tf;
    }

    /**
     * updates this {@code FileTracker} to reflect changes made to the tracked
     * file, if any, associated with the specified key
     * 
     * @param key a {@code long} identifying the updated tracked file
     * @throws RemoteException if could not connect to sitemanager
     */
    public synchronized void recordFileUpdate(long key) throws RemoteException {
        TrackedFile tf = fileMap.get(Long.valueOf(key));
        SiteManagerRemote siteManager = this.coreConnector.getSiteManager();

        if (tf != null) {
            siteManager.recordLogEvent(new FileTrackerLogEvent(
                    FileTrackerLogEvent.METADATA, key));
            currentFileSize -= tf.getRecoverableLength();
            tf.updateFileLength();
            currentFileSize += tf.getRecoverableLength();

            tf.setTimestamp(System.currentTimeMillis());
        } else {
            siteManager.recordLogEvent(new FileTrackerLogEvent(
                    FileTrackerLogEvent.INVALID, key));
        }
    }

    /**
     * invalidates all files tracked by this {@code FileTracker} and removes
     * them from tracking
     * 
     * @throws RemoteException if could not connect to sitemanager
     */
    public synchronized void purgeFiles() throws IOException {
        for (Iterator<Entry<Long, TrackedFile>>
                entryIterator = fileMap.entrySet().iterator();
                entryIterator.hasNext(); ) {
            Entry<Long, TrackedFile> entry = entryIterator.next();
            
            entryIterator.remove();
            forgetFile(entry.getValue());
        }
    }

    /**
     * Creates a new, empty file on behalf of the invoker, in a directory chosen
     * by this {@code FileTracker}. This is essentially the same facility as
     * provided by {@code File.createTempFile(String, String)}, with a
     * different choice of the directory in which the temp file is created
     * 
     * @param prefix a String containing a file name prefix to use in creating
     *        the new file's name; must contain at least three characters
     * @param suffix a String containing a file name suffix to use in creating
     *        the new file's name; may be {@code null}, in which case the
     *        suffix ".tmp" will be used
     * @return a {@code File} representing a temporary file that the can be used
     *         by the invoker, who bears the responsibility for disposing of the
     *         underlying file when it is no longer needed
     * @throws IllegalArgumentException if the prefix argument contains fewer
     *         than three characters
     * @throws IOException if a file could not be created
     * @throws SecurityException if a security manager exists and its
     *         {@code checkWrite(String)} method does not allow a file to be
     *         created
     */
    public File createTempFile(String prefix, String suffix) throws IOException {
        return File.createTempFile(prefix, suffix, tempDir);
    }

    /**
     * Invoke this method to register another object to receive notifications
     * from this {@code FileTracker}.
     * 
     * @param listener an object implementing this class's {@code Listener}
     *        interface.
     */
    public synchronized void registerListener(Listener listener) {
        registeredListeners.add(listener);
    }

    /**
     * Implements {@code ServletContextListener}; invoked by the servlet
     * container during webapp shutdown. The current implementation deletes all
     * tracked files.
     * 
     * @param sce a {@code ServletContextEvent} describing the context shutdown
     *        that is in progress
     */
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            SiteManagerRemote siteManager = this.coreConnector.getSiteManager();
            
            siteManager.recordLogEvent(new FileTrackerLogEvent(
                    FileTrackerLogEvent.SHUTDOWN));
            sce.getServletContext().removeAttribute(DEFAULT_ATTRIBUTE_NAME);
            siteManager.recordLogEvent(new FileTrackerLogEvent(
                    FileTrackerLogEvent.ATTRIBUTE_REMOVED));
            purgeFiles();
            siteManager.recordLogEvent(new FileTrackerLogEvent(
                    FileTrackerLogEvent.PURGE));
        } catch (RemoteException re) {
            // all we can do is report it to core connector
            this.coreConnector.reportRemoteException(re);
        } catch (IOException ioe) {
            // ignore it
        }
    }

    /**
     * Implements {@code ServletContextListner}; invoked by the servlet
     * container during webapp startup.
     * 
     * @param sce a {@code ServletContextEvent} describing the context
     *        initialization that is in progress
     */
    public void contextInitialized(ServletContextEvent sce) {
        context = sce.getServletContext();
        this.coreConnector = CoreConnector.extract(context);

        String tempDirPath = context.getInitParameter("tempDir");

        tempDir = ((tempDirPath == null) || (tempDirPath.trim().length() == 0))
                ? (File) context.getAttribute("javax.servlet.context.tempdir")
                : new File(tempDirPath);
        try {
            maxFileSize = Long.parseLong(
                    context.getInitParameter("maxFileCacheSize"))
                    * 1024L * 1024L;
        } catch (NumberFormatException nfe) {
            // proceed with the default
        } catch (NullPointerException npe) {
            // proceed with the default
        }

        try {
            timeoutMillis = Long.parseLong(
                    context.getInitParameter("fileCacheTimeout")) * 1000L;
        } catch (NumberFormatException nfe) {
            // proceed with the default
        } catch (NullPointerException npe) {
            // proceed with the default
        }

        context.setAttribute(DEFAULT_ATTRIBUTE_NAME, this);
        try {
            SiteManagerRemote siteManager = this.coreConnector.getSiteManager();
            
            siteManager.recordLogEvent(new FileTrackerLogEvent(
                    FileTrackerLogEvent.INSTALL));
            siteManager.recordLogEvent(new FileTrackerLogEvent(
                    FileTrackerLogEvent.TEMP_DIR, tempDir));
            siteManager.recordLogEvent(new FileTrackerLogEvent(
                    FileTrackerLogEvent.CACHE_SIZE, maxFileSize));
            siteManager.recordLogEvent(new FileTrackerLogEvent(
                    FileTrackerLogEvent.INACTIVITY, timeoutMillis));
            siteManager.recordLogEvent(new FileTrackerLogEvent(
                    FileTrackerLogEvent.ACCESSIBLE, DEFAULT_ATTRIBUTE_NAME));
        } catch (RemoteException re) {
            // all we can do is report it to core connector
            this.coreConnector.reportRemoteException(re);
        }
    }

    /**
     * An interface representing objects that can receive notifications from
     * {@code FileTracker} instances about file tracking events.
     */
    public interface Listener {

        /**
         * Invoked by each {@code FileTracker} exactly once on each of its
         * registered listeners for each {@code TrackedFile} object as it is
         * being forgotten.
         * 
         * @param tf the {@code TrackedFile} that was forgotten
         * @throws IOException
         */
        public void notifyTrackedFileForgotten(TrackedFile tf)
                throws IOException;
    }
}
