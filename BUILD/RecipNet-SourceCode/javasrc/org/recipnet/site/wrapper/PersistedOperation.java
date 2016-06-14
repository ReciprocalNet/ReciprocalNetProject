/*
 * Reciprocal Net Project
 * 
 * PersistedOperation.java
 *
 * 08-Jul-2004: ekoperda wrote first draft
 * 20-Jun-2005: ekoperda made ID_NOT_ASSIGNED constant public
 * 19-May-2006: jobollin reformatted the source
 * 12-Jun-2006: jobollin fixed NPE reversion in unregister(FileTracker)
 * 27-Dec-2007: ekoperda added toString() to aid in debugging 
 */

package org.recipnet.site.wrapper;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 * Abstract base class for operations that make use of the persistence
 * mechanism. {@code PersistedOperation}s are designed to carry
 * operation-specific state information through multiple HTTP round-trips to the
 * web application. The web client references the current operation by a simple
 * key number assigned by {@code OperationPersister}.
 * {@code PersistedOperation}s are designed to be registered with a single
 * webapp-wide {@code OperationPersister} that remembers the operation-specific
 * information. See {@code OperationPersister} for more details.
 * </p><p>
 * Special support is present in this class for tracked files (as managed by
 * {@code FileTracker}) in order to unify the two auto-expiration mechanisms
 * for greater efficiency. Any subclasses that make use of or rely upon
 * {@code TrackedFile}'s should invoke {@code notifyFileTracked()} immediately
 * after invoking {@code FileTracker.trackFile()}.
 * </p><p>
 * This class is thread-safe.
 * </p>
 */
public abstract class PersistedOperation implements Serializable {
    
    /** Possible values for {@code status}. */
    private static final int INITIAL = 1;

    private static final int REGISTERED = 2;

    private static final int EXPIRED = 3;

    private static final int UNREGISTERED = 4;

    /** Possible value for {@code id}. */
    public static final int ID_NOT_ASSIGNED = -1;

    /** Possible value for {@code timeToExpire}. */
    public static final long NO_TIME = Long.MAX_VALUE;

    /**
     * Unique id number assigned to this operation by the Persister. Initialized
     * to {@code ID_NOT_ASSIGNED} by the constructor and set by
     * {@code register()}.
     */
    private int id;

    /**
     * Status code used for internal state tracking. Possible values are
     * {@code INITIAL}, {@code REGISTERED}, {@code EXPIRED}, and
     * {@code UNREGISTERED}.
     */
    private int status;

    /**
     * The system clock value (as returned by {@code System.currentTimeMillis()})
     * at which this operation will have expired if not renewed before then. May
     * have the special value {@code NO_TIME}, in which case this operation
     * will never expire. Initialized by the constructor and modified by
     * {@code register()} and {@code notifyAccess()}.
     */
    private long timeToExpire;

    /**
     * The number of milliseconds past an access that this operation should
     * expire if not accessed again. May have the special value {@code NO_TIME},
     * in which case this operation will never expire. Set at construction time.
     */
    private final long expirationInterval;

    /**
     * A collection of {@code Long}'s representing "keys" to tracked files as
     * obtained from {@code FileTracker}. Initialized late and populated by
     * {@code notifyFileTracked()}. May be null if never needed.
     */
    private final Set<Long> trackedFileKeys;

    /**
     * A reference to the {@code OperationPersister} with which this operation
     * object is affiliated. May be null if this operation has not yet been
     * registered. Initialized by the constructor and set by {@code register()}.
     */
    private OperationPersister persister;

    /**
     * No-arg constructor; creates an operation that does not automatically
     * expire.
     */
    public PersistedOperation() {
        this(NO_TIME);
    }

    /**
     * Constructor.
     * 
     * @param expirationInterval the number of milliseconds past an access that
     *        this operation should expire if not accessed again. The special
     *        value {@code NO_TIME} indicates that the operation should not
     *        expire automatically.
     */
    public PersistedOperation(long expirationInterval) {
        this.id = ID_NOT_ASSIGNED;
        this.status = INITIAL;
        this.timeToExpire = NO_TIME;
        this.expirationInterval = expirationInterval;
        this.trackedFileKeys = new HashSet<Long>();
        this.persister = null;
    }

    /**
     * Simple getter method.
     * 
     * @return the id number assigned to this operation by the Persister.
     * @throws IllegalStateException if {@code register()} has not been invoked
     *         or {@code unregister()} has been invoked.
     */
    public synchronized int getId() {
        if ((this.status != REGISTERED) && (this.status != EXPIRED)) {
            throw new IllegalStateException();
        } else {
            return this.id;
        }
    }

    /**
     * @return {@code true} if {@code register()} has been invoked but
     *         {@code unregister()} has not. The return value is not affected by
     *         whether this operation has expired or not.
     */
    public synchronized boolean isRegisteredOrExpired() {
        return ((this.status == REGISTERED) || (this.status == EXPIRED));
    }

    /**
     * Determines whether the current operation has expired due to inactivity.
     * Once an operation has expired, it stays expired.
     * 
     * @return true if the operation has expired, or false if it has not. Always
     *         returns false if no {@code expirationInterval} was specified at
     *         construction time.
     * @throws IllegalStateException if {@code register()} has not been invoked
     *         previously, or if {@code unregister()} has been invoked
     *         previously.
     */
    public synchronized boolean isExpired() {
        switch (this.status) {
            case EXPIRED:
                return true;
            case REGISTERED:
                if (this.timeToExpire < System.currentTimeMillis()) {
                    /*
                     * Apparently this operation expired since the last time we
                     * checked. Update our own status and inform the caller.
                     */
                    this.status = EXPIRED;
                    return true;
                } else {
                    return false;
                }
            case INITIAL:
            case UNREGISTERED:
            default:
                throw new IllegalStateException();
        }
    }

    /** for debugging only */
    public String toString() {
	StringBuilder sb = new StringBuilder();
	sb.append(this.id);
	sb.append(" ");
        switch (this.status) {
            case EXPIRED:
                sb.append("EXPIRED");
		break;
            case REGISTERED:
		sb.append("REGISTERED");
		break;
            case INITIAL:
		sb.append("INITIAL");
		break;
            case UNREGISTERED:
		sb.append("UNREGISTERED");
		break;
	}
	sb.append(" ");
	sb.append(this.getClass().getName());
	return sb.toString();
    }

    /**
     * To be invoked by the Persister only: informs this operation that it is
     * now being persisted. Subclasses may override this method but must
     * delegate back to this superclass.
     * 
     * @param id the unique id number assigned to this operation by the
     *        Persister.
     * @param persister a reference to the Persister that this operation will
     *        retain.
     * @throws IllegalStateException if this method has been invoked previously.
     */
    protected synchronized void register(@SuppressWarnings("hiding") int id,
            @SuppressWarnings("hiding") OperationPersister persister) {
        if (this.status != INITIAL) {
            throw new IllegalStateException();
        }

        // Update our state.
        this.id = id;
        this.status = REGISTERED;
        this.persister = persister;

        // Begin counting down to an expiration time, if one is configured.
        notifyAccess();
    }

    /**
     * To be invoked by the Persister only: informs this operation that it has
     * just been accessed and that it should reset its inactivity expiration
     * timer.
     * 
     * @throws IllegalStateException if {@code register()} has not been invoked
     *         previously, or {@code unregister()} has been invoked previously,
     *         or this operation is expired.
     */
    protected synchronized void notifyAccess() {
        if (this.status != REGISTERED) {
            throw new IllegalStateException();
        }

        if (this.expirationInterval != NO_TIME) {
            this.timeToExpire = System.currentTimeMillis()
                    + this.expirationInterval;
        }
    }

    /**
     * To be invoked by the Persister only: informs this operation that it has
     * stopped being persisted and should clean up its resources. Subclasses may
     * override this method but must delegate back to this superclass.
     * 
     * @param fileTracker a reference to the {@code FileTracker}; this
     *        operation may invoke {@code FileTracker.forgetFile()} as it is
     *        cleaning up.
     * @throws IllegalStateException {@code register()} has not been invoked
     *         previously, or if this method has been invoked previously.
     * @throws IOException if FileHelper encountered an error while talking
     *         to core.
     */
    protected synchronized void unregister(FileTracker fileTracker)
            throws IOException {
        if ((this.status != REGISTERED) && (this.status != EXPIRED)) {
            throw new IllegalStateException();
        }

        // Update our state.
        this.status = UNREGISTERED;

        /*
         * Clean up any tracked files we might have had. It is important that
         * our status be UNREGISTERED before we do this, because otherwise we
         * might cause an infinite loop from here to FileTracker to
         * OperationPersister to here again.
         */
        for (Long key : trackedFileKeys) {
            fileTracker.forgetFile(key.longValue());
        }
    }

    /**
     * To be invoked by subclasses only: notifies this base class that a tracked
     * file has been associated with this operation. This establishes a
     * bidirectional binding: if the tracked file should be forgotten while this
     * operation is still active, the Persister will unregister this operation.
     * Likewise, if this operation should be unregistered while the tracked file
     * is still remembered, the tracked fill will be forgotten. The caller
     * should avoid calling {@code FileTracker.forgetFile()} for this tracked
     * file after this method has been invoked.
     * 
     * @param key the key assigned to the tracked file, as returned by
     *        {@code FileTracker.trackFile()}.
     * @throws IllegalStateException if {@code register()} has not been invoked
     *         previously, or if {@code unregister()} has been invoked
     *         previously, or if this operation is expired.
     * @throws IOException if FileHelper encountered an error while talking
     *         to core.
     */
    protected synchronized void notifyFileTracked(long key)
            throws IOException {
        if (this.status != REGISTERED) {
            throw new IllegalStateException();
        }

        // Update our own state tables.
        this.trackedFileKeys.add(key);

        // Notify the Persister so he can update his state tables.
        this.persister.notifyTrackedFileBound(key, this);
    }
}
