/*
 * Reciprocal Net Project
 * 
 * OperationPersister.java
 *
 * 08-Jul-2004: ekoperda wrote first draft
 * 31-Aug-2004: ekoperda fixed bug #1374 in closeExpiredOperations()
 * 11-Apr-2006: jobollin switched to initializing object caches via
 *              ObjectCache.newInstance()
 */

package org.recipnet.site.wrapper;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.recipnet.common.ObjectCache;
import org.recipnet.site.core.ResourceNotFoundException;

/**
 * <p>
 * Wrapper-level class that tracks {@code PersistedOperation}'s that may span
 * several HTTP round-trips. Intended to be instantiated at webapp startup time
 * and live in the web application's "servlet context". Note that storage of
 * {@code PersistedOperation}'s is intentionally imperfect: operations may be
 * lost if they expire due to inactivity (configurable) or if the maximum count
 * of simultaneous operations is exceeded via a fair disposal algorithm. For
 * this reason callers should anticipate that some calls to
 * {@code getOperation()} may not succeed in retrieving a previously registered
 * {@code PersitedOperation} object.
 * </p><p>
 * A typical use case would involve the following steps:
 * <ol>
 * <li>Content layer instantiates a subclass of {@code PersistedOperation}
 * specific to the long-running operation it is initiating. It may optionally
 * specify an inactivity expiration interval.</li>
 * <li>Content layer invokes {@code registerOperation()}.</li>
 * <li>Content layer passes operation id number (as returned by
 * {@code registerOperation()}) to client-side browser for persistance.</li>
 * <li>Content layer receives new HTTP request from client side that references
 * an issued operation id. Content layer invokes {@code getOperation()} to
 * retrieve its stored state and optionally updates the state. The operation
 * inactivity timer is reset.</li>
 * <li>Content layer finishes its long-running operation and invokes
 * {@code closeOperation()}.</li>
 * </ol>
 * </p><p>
 * The class maintains a bidirectional interface with {@code FileTracker},
 * another wrapper-level singleton object that lives in the web application's
 * servlet context, in order to unify the expiration and cleanup of server-side
 * persisted resources.
 * </p><p>
 * The class expects to be registered as a servlet context listener in the
 * web.xml file and additional depends upon the value of the context
 * configuration parameter called 'persistedOperationsCache'. The value of this
 * parameter should be a string that describes the cache parameters that this
 * class should use, formatted as defined by the constructor for
 * {@code ObjectCache}.
 * </p><p>
 * This class is thread-safe.
 * </p>
 */
public class OperationPersister implements FileTracker.Listener, Serializable,
        ServletContextListener {
    
    private static final int MIN_EXPIRED_OPERATION_SWEEP_INTERVAL = 60000;

    /**
     * A cache of {@code PersistedOperation} objects that are being tracked by
     * this persister. Initialized by the constructor and modified by
     * registerOperation() and cleanupAfterOperation().
     */
    private ObjectCache<PersistedOperation> ops;

    /**
     * Random source used to generate id numbers for persisted operations.
     * Initialized by the constructor.
     */
    private Random random;

    /**
     * A reference to the web application's instance of {@code FileTracker}.
     * Set by {@code contextInitialized()} at webapp startup.
     */
    private FileTracker fileTracker;

    /**
     * A {@code Map} of {@code TrackedFile} objects to
     * {@code PersistedOperation} objects. This is used to enable efficient
     * handling of tracked file notifications sent by {@code FileTracker}.
     * Initialized by the constructor, populated by
     * {@code notifyTrackedFileBound()}, and pruned by
     * {@code notifyTrackedFileForgotten()}.
     */
    private Map<TrackedFile, PersistedOperation>
            trackedFilesToPersistedOperations;

    /**
     * System clock value at the time closeExpiredOperations() last was invoked.
     * Set by the constructor and modified by closeExpiredOperations().
     */
    private long timeOfLastExpiredOperationSweep;

    /** Initializes a new {@code OperationPersister} */
    public OperationPersister() {
        this.ops = null;
        this.random = new SecureRandom();
        this.fileTracker = null;
        this.trackedFilesToPersistedOperations
                = new HashMap<TrackedFile, PersistedOperation>();
        this.timeOfLastExpiredOperationSweep = System.currentTimeMillis();
    }

    /**
     * Registers some server-side state for persistance within the web
     * application. Registered state is retrievable by calling
     * {@code getOperation()} subsequently and specifying the id number returned
     * by this function as an argument. The persisted state may be lost if it
     * expires due to inactivity (configurable on a per-operation basis) or if
     * the cache of {@code PersistedOperation}'s becomes full. The caller
     * should invoke {@code closeOperation()} once the operation completes and
     * the registered server-side state is no longer needed.
     * 
     * @param op the {@code PersistedOperation} to be persisted.
     * @return the id number assigned to the newly-registered operation.
     * @throws IOException if FileHelper encountered an error while talking
     *         to core.
     */
    public synchronized int registerOperation(PersistedOperation op)
            throws IOException {
        int opId;
        
        // Perform routine maintenance on our tables.
        closeExpiredOperations();

        // Select an id number to assign to op.
        do {
            opId = this.random.nextInt(Integer.MAX_VALUE);
        } while (ops.get(opId) != null);

        // Update our state.
        ops.put(opId, op);
        op.register(opId, this);

        return opId;
    }

    /**
     * Retrieves server-side persisted state associated with a specified id.
     * This also has the effect of resetting the operation's inactivity
     * expiration timer, if enabled. Note that persisted state may have been
     * lost since it was registered if it expired due to inactivity
     * (configurable on a per-operation basis) or if the cache of
     * {@code PersistedOperation}'s becomes full.
     * 
     * @param id unique identifier for the operation, as returned by a previous
     *        invocation of {@code registerOperation()}.
     * @return a reference to a {@code PersistedOperation} object that was
     *         registered previously.
     * @throws IOException if FileHelper encountered an error while talking
     *         to core.
     * @throws ResourceNotFoundException if no persisted operation with the
     *         specified {@code id} is known.
     */
    public synchronized PersistedOperation getOperation(int id)
            throws IOException, ResourceNotFoundException {
        // Perform routine maintenance on our tables.
        closeExpiredOperations();

        PersistedOperation op = this.ops.get(id);
        
        if (op == null) {
            throw new ResourceNotFoundException();
        }
        if (op.isExpired()) {
            cleanupAfterOperation(op);
            throw new ResourceNotFoundException();
        }
        op.notifyAccess();
        
        return op;
    }

    /**
     * Clears resources associated with a previously-registered
     * {@code PersistedOperation}.
     * 
     * @param id unique identifier for the operation, as returned by a previous
     *        invocation of {@code registerOperation()}.
     * @throws IOException if FileHelper encountered an error while talking
     *         to core.
     */
    public synchronized void closeOperation(int id) throws IOException {
        // Perform routine maintenance on our tables.
        closeExpiredOperations();

        try {
            cleanupAfterOperation(getOperation(id));
        } catch (ResourceNotFoundException ex) {
            /*
             * No operation with the specified id is known. There's nothing to
             * clean up. Just swallow the error silently because no special
             * handling on the caller's part is required.
             */
        }
    }

    /**
     * Implements {@code FileTracker.Listener}; should be invoked only by
     * {@code FileTracker}. Informs this object that a {@code TrackedFile}
     * tracked by {@code FileTracker} has ceased to be tracked.
     * 
     * @param tf the {@code TrackedFile} that has ceased to be tracked.
     * @throws IOException if FileHelper encountered an error while talking
     *         to core.
     */
    public synchronized void notifyTrackedFileForgotten(TrackedFile tf)
            throws IOException {
        PersistedOperation op = this.trackedFilesToPersistedOperations.get(tf);
        
        if (op != null) {
            /*
             * This tracked file is (or used to be) associated with one of our
             * PersistedOperation's. Remove the entry from the map because we
             * won't be notified about this same TrackedFile again.
             */
            this.trackedFilesToPersistedOperations.remove(tf);

            if (op.isRegisteredOrExpired()) {
                /*
                 * The associated PersistedOperation is still active. It won't
                 * be very useful to us now that its TrackedFile has
                 * disappeared, so terminate the operation prematurely.
                 */
                cleanupAfterOperation(op);
            }
        }
    }

    /**
     * Should be invoked by {@code PersistedOperation} only; informs this
     * Persister that a {@code FileTracker}-tracked file is being used by one
     * of its registered operations.
     * 
     * @param key the key number for the tracked file, as returned by
     *        {@code FileTracker.trackFile()}.
     * @param op the {@code PersistedOperation} now associated with the
     *        specified tracked file, as passed to a previous invocation of
     *        {@code registerOperation()}.
     * @throws IllegalArgumentException if {@code key} is not recognized or
     *         {@code op} is not recognized.
     * @throws IOException is FileTracker encounters an error while
     *         communicating with core.
     */
    protected synchronized void notifyTrackedFileBound(long key,
            PersistedOperation op) throws IOException {
        // Validate key.
        TrackedFile tf = this.fileTracker.getTrackedFile(key);
        
        if (tf == null) {
            // Must have been a bad key number.
            throw new IllegalArgumentException();
        }

        // Validate op.
        if (this.ops.get(op.getId()) == null) {
            // Op has an unrecognized id number.
            throw new IllegalArgumentException();
        }

        // Update our state.
        this.trackedFilesToPersistedOperations.put(tf, op);
    }

    /**
     * Internal helper method that cleans up state, etc. at the end of a
     * persisted operation's life, whether the end was a result of the caller's
     * request or the result of an inactivity timeout.
     * 
     * @param  op the {@code PersistedOperation} to clean up
     * @throws IOException if FileHelper encountered an error while talking
     *         to core.
     */
    private synchronized void cleanupAfterOperation(PersistedOperation op)
            throws IOException {
        if (!op.isRegisteredOrExpired()) {
            throw new IllegalArgumentException();
        }

        // Remove this op from our big cache.
        this.ops.invalidate(op.getId());

        // Give the operation a chance to clean up after itself.
        op.unregister(this.fileTracker);

        /*
         * There is no need to update the 'trackedFilesToPersistedOperations'
         * table here. Our invocation of op.unregister() above was sufficient
         * because op.unregister() will invoke FileTracker.forgetFile(), which
         * in turn will invoke our notifyTrackedFileForgotten() method. Our
         * notifyTrackedFileForgotten() method takes care of cleaning up the
         * 'trackedFilesToPersistedOperations' table for us.
         */
    }

    /**
     * Internal helper method that unregisters and cleans up after any persisted
     * operations that have expired due to inactivity. It is designed to be
     * invoked on a routine and frequent basis. Internal logic tracks
     * invocations and sometimes exits prematurely in order to prevent a full
     * sweep (which can be costly in terms of processing time) from being
     * performed too frequently.
     * 
     * @throws RemoteException if FileHelper encountered an error while talking
     *         to core.
     */
    private synchronized void closeExpiredOperations() throws IOException {
        // Track this method's invocations to prevent it from being invoked
        // too frequently.
        long now = System.currentTimeMillis();
        
        if (now - this.timeOfLastExpiredOperationSweep
                < MIN_EXPIRED_OPERATION_SWEEP_INTERVAL) {
            // The last invocation of this method wasn't too long ago. For
            // efficiency's sake, just return control to the caller without
            // taking any action.
            return;
        }
        this.timeOfLastExpiredOperationSweep = now;

        // Iterate through all the persisted operations.
        for (PersistedOperation op : this.ops.getAll(PersistedOperation.class)) {
            if (op.isExpired()) {
                // Found an expired operation. Clean up after it.
                cleanupAfterOperation(op);
            }
        }
    }

    /**
     * Implements {@code ServletContextListener}; invoked by the servlet
     * container during webapp startup.
     * 
     * @param ev a {@code ServletContextEvent} describing the context
     *        initialization that is in progress
     */
    public void contextInitialized(ServletContextEvent ev) {
        ServletContext sc = ev.getServletContext();
        
        ops = ObjectCache.newInstance(
                sc.getInitParameter("persistedOperationsCache"));
        sc.setAttribute(OperationPersister.class.getName(), this);

        this.fileTracker = FileTracker.getFileTracker(sc);
        this.fileTracker.registerListener(this);
    }

    /**
     * Implements {@code ServletContextListener}; invoked by the servlet
     * container during webapp shutdown.
     * 
     * @param ev a {@code ServletContextEvent} describing the context shutdown
     *        that is in progress
     */
    public void contextDestroyed(
            @SuppressWarnings("unused") ServletContextEvent ev) {
        // Nothing much to do...
    }

    /**
     * Static function that returns a reference to the
     * {@code OperationPersister} within the specified {@code ServletContext}.
     * 
     * @param sc the {@code ServletContext} whose {@code OperationPersister} is
     *        desired
     * @return the {@code OperationPersister}, if any, associated with the
     *        specified context; may be {@code null}
     */
    public static OperationPersister extract(ServletContext sc) {
        return (OperationPersister) sc.getAttribute(
                OperationPersister.class.getName());
    }
}
