/*
 * Reciprocal Net project
 * 
 * LockAgent.java
 *
 * 27-May-2003: ekoperda wrote first draft, borrowing from the old 
 *              core.util.SampleLockAgent
 * 09-Jun-2003: ekoperda added new version of acquireLock() that supports
 *              locks' new property 'ignoreConflictsWith'
 * 08-Jul-2003: ekoperda added deadlock logging support throughout
 * 21-Aug-2003: ekoperda fixed bug #1023 in promoteLock(), scheduler(), and 
 *              grantLock()
 * 26-Jan-2004: cwestnea fixed bug #1040 in grantLock()
 * 09-Feb-2004: cwestnea fixed bug #1030 in acquireLock() and promoteLock() 
 * 21-May-2004: ekoperda removed newSampleLock()
 * 18-Aug-2004: cwestnea modified addListener() and notifyListeners() to remove 
 *              synchronization on listeners
 * 07-Apr-2006: jobollin removed acquireLock(AbstractLock, boolean, AbstractLock)
 *              private; it was not used anywhere except in the other version
 *              of acquireLock().  Reformatted the code.  Corrected data
 *              race involving registering / notifying lock listeners.
 *              Corrected code for remembering assigned lock IDs until they go
 *              out of use.  Updated pending lock queue code to more reliably
 *              put pending promotions at the front of the queue while honoring
 *              registration time among both promotions and non-promotions;
 *              reorganized all code that acquires the scheduler lock to release
 *              it again in an appropriately positioned finally block; updated
 *              many method's docs. 
 */

package org.recipnet.site.core.lock;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.security.SecureRandom;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import org.recipnet.site.OperationFailedException;
import org.recipnet.site.core.DeadlockDetectedException;
import org.recipnet.site.core.ResourceNotFoundException;
import org.recipnet.site.core.SiteManager;
import org.recipnet.site.core.util.LogRecordGenerator;
import org.recipnet.site.core.util.MutexLock;

/**
 * <p>
 * A core-level agent class that manages locking for Sample Manager and
 * Repository Manager. Locks may be short-lived, like a temporary row-level lock
 * on a database table, or long-lived, like permission to download a particular
 * repository file.
 * </p><p>
 * Locks are representing by lock objects, descendants of the
 * {@code AbstractLock} class. The general calling sequence for using a lock is:
 * 1. construct a new lock object (subclass of {@code AbstractLock}). 2.
 * register the lock object with the lock agent. 3. attempt to acquire the lock,
 * blocking as necessary until resources are available. 4. do whatever operation
 * requires the lock. 5. release the lock.
 * </p><p>
 * A typical user of this class would create subclasses of {@code AbstractLock}
 * to particular resources or critical operations for which access needed to be
 * regulated or serialized. Lock objects generally contain logic that's
 * responsible for deciding whether or not the lock can coexist with another
 * lock, or whether the two locks would conflict. Properly using an instance of
 * this class guarantees that no two locks, where one lock would conflict with
 * the other lock, will be held simultaneously. Subject to this restriction,
 * lock requests are granted (i.e. locks are acquired) as rapidly as possible.
 * </p><p>
 * {@code LockAgent} maintains a pool of database {@code Connection} objects
 * that are loaned to locks that require them, as the locks are granted. The
 * caller should supply the appropriate number of {@code Connection} objects to
 * {@code LockAgent} at startup time by invoking {@code supplyConnection()}
 * repeatedly.
 * </p><p>
 * Other objects may subscribe to notifications from {@code LockAgent} regarding
 * locks granted and revoked. Those other objects should implement the
 * {@code LockListener} interface and register themselves by calling
 * {@code registerListener()}. Lock notifications always occur outside of
 * {@code LockAgent}'s critical sections, immediately after the lock has been
 * granted or revoked. In the case of a lock promotion or demotion, the listener
 * will be notified that the second lock was granted immediately prior to being
 * notified that the first lock was revoked.
 * </p><p>
 * Deadlock is the condition that results from programming errors, where two or
 * more threads have acquired conflicting locks are both are unable to proceed.
 * Such a condition would be indicated by a {@code DeadlockDetectedException}
 * thrown from any of {@code LockAgent}'s method, potentially to any caller (in
 * any thread). Deadlock is inherently difficult to detect directly. The current
 * implementation assumes that deadlock has occurred if a wait operation on the
 * "scheduler" lock takes longer than {@code SCHEDULER_TIMEOUT} milliseconds to
 * finish, or if any blocking lock acquisition takes longer than
 * {@code acquisitionTimeout} milliseconds (as specified at construction time).
 * </p><p>
 * This class is thread-safe.
 * </p>
 */
public class LockAgent {
    
    /*
     * TODO: 1. Add lots of logging support throughout, to assist in debugging.
     * 2. Consider replacing half of the DeadlockDetectedExceptions with
     * PessimisticLockingExceptions, since there are some (a few?) legitimate
     * cases in practice where a lock acquisition may need to block more than 60
     * seconds.
     */
    
    /**
     * The number of milliseconds that a thread will wait to acquire the
     * scheduler before assuming that deadlock has occurred.
     */
    private final static int SCHEDULER_TIMEOUT = 10000;
    
    /**
     * The initial number of locks that can be accommodated in the pending lock
     * queue at the same time; the queue will expand at need, so the exact value
     * of this parameter is not criticial. 
     */
    private final static int INITIAL_QUEUE_SIZE = 64;

    /** serializes access to most member variables */
    private final MutexLock schedulerLock;

    /**
     * A map of {@code Integer} objects representing lock id's to
     * {@code AbstractLock} objects representing those locks that are currently
     * active - they have been acquired but not yet released. Must hold the
     * {@code schedulerLock} to read or modify.
     */
    private final Map<Integer, AbstractLock> activeLocks;

    /**
     * A List of AbstractLock objects representing those locks that have been
     * requested, but that have not yet been acquired because currently active
     * locks would conflict with them. Calling threads whose locks are in this
     * list are blocked inside {@link #acquireLock(AbstractLock, boolean)},
     * waiting for their lock to be granted.  This set is always sorted in order
     * of priority, so that the most important (and probably soonest to be
     * granted) locks are at the front. Must hold the {@code schedulerLock} to
     * read or modify.
     */
    private final List<AbstractLock> pendingLocks;
    
    /**
     * A {@code Comparator} that determines the relative granting priority for
     * pending {@code AbstractLock}s
     */
    private final Comparator<AbstractLock> pendingLockComparator;

    /**
     * A collection of database Connection objects that were given to us by
     * Sample Manager at construction time. A Connection exists in this list
     * only if it is not currently assigned to an active lock; connections
     * assigned to active locks are removed from this list and added back in (to
     * the end) when the lock is released. Must hold the {@code schedulerLock}
     * to read or modify.
     */
    private final Queue<Connection> freeConnections;

    /**
     * Like freeConnections above, but Connection objects in this set currently
     * are assigned to an active lock and cannot be issued. Must hold the
     * {@code schedulerLock} to read or modify.
     */
    private final Collection<Connection> busyConnections;

    /**
     * Contains references to zero or more {@code LockListener} objects that
     * have registered to receive lock notifications.  This object can be
     * updated without explicit synchronization.
     */
    private final CopyOnWriteArrayList<LockListener> listeners;
    
    /**
     * A map from {@code Integer} objects representing the lock IDs that are
     * currently in use to LockIdReference objects referencing the corresponding
     * locks.  Lock IDs are "in use" from the time they are assigned to a lock
     * until the time the associated lock is garbage collected.  Synchronize on
     * this map to access or modify it.
     */
    private final Map<Integer, LockIdReference<?>> registeredLockIds;
    
    /**
     * A {@code ReferenceQueue} with which this agent receives notifications of
     * locks being garbage collected; their IDs can thereafter be recycled
     */
    private final ReferenceQueue<AbstractLock> lockReferenceQueue;

    /**
     * Used to generate random lock IDs. Synchronize on
     * {@code registeredLockIds} to use it.
     */
    private final Random randomGenerator;

    /**
     * Reference to Site Manager set at construction time; used to write log
     * messages.
     */
    private final SiteManager siteManager;

    /**
     * Configuration option set at construction time; if true, all lock objects
     * registered with this agent will be instructed to keep detailed stack
     * trace information that may be useful during debugging. The recommended
     * value for normal operation is false.
     */
    private final boolean enableStackTraces;

    /**
     * Configuration option set at construction time; determines the maximum
     * number of milliseconds a lock acquisition attempt may block before the
     * attempt is aborted.
     */
    private final int acquisitionTimeout;

    /**
     * Constructor; called only by Sample Manager.
     * 
     * @param siteManager a reference to Site Manager, used for logging only.
     * @param enableStackTraces if true, all lock objects registered with this
     *        agent will be instructed to keep detailed stack trace information
     *        that may be useful during debugging. The recommended value for
     *        normal operation is false.
     * @param acquisitionTimeout the maximum number of milliseconds a lock
     *        acquisition attempt may block before the attempt is aborted.
     */
    public LockAgent(SiteManager siteManager, boolean enableStackTraces,
            int acquisitionTimeout) {
        this.siteManager = siteManager;
        this.enableStackTraces = enableStackTraces;
        this.acquisitionTimeout = acquisitionTimeout;
        freeConnections = new LinkedList<Connection>();
        busyConnections = new ArrayList<Connection>();
        activeLocks = new HashMap<Integer, AbstractLock>();
        pendingLocks = new ArrayList<AbstractLock>(INITIAL_QUEUE_SIZE);
        pendingLockComparator = new PendingLockComparator();
        schedulerLock = new MutexLock();
        listeners = new CopyOnWriteArrayList<LockListener>();
        registeredLockIds = new HashMap<Integer, LockIdReference<?>>();
        lockReferenceQueue = new ReferenceQueue<AbstractLock>();
        randomGenerator = new SecureRandom();
    }

    /**
     * Supplies a database {@code Connection} to be included in the connection
     * pool. It is normal to call this method repeatedly, with a new object each
     * time, according to the number of database connections appropriate for the
     * caller's application.
     * 
     * @param conn the Connection to add to this agent's pool
     */
    public void addConnection(Connection conn) {
        boolean rc = schedulerLock.acquire(SCHEDULER_TIMEOUT);
        
        // FIXME: a mandatory test may be warranted here:
        assert rc : "scheduler not acquired for adding DB Connections";
        
        try {
            freeConnections.offer(conn);
        } finally {
            schedulerLock.release();
        }
    }

    /**
     * Registers an object as a lock listener and enables it to receive lock
     * notifications.
     * 
     * @param  listener the LockListener to register with this agent
     */
    public void registerListener(LockListener listener) {
        
        /*
         * This operation does not require explicit synchronization because the
         * listener automatically makes a copy of its internal list on which
         * to perform the update
         */
        listeners.add(listener);
    }

    /**
     * Normally called during system shutdown, this method releases all locks
     * forcefully, without waiting for callers to release them normally. Each
     * lock object is notified of the release by its {@code revoke()} method.
     * 
     * @throws DeadlockDetectedException if deadlock is detected.
     */
    public void releaseAllLocks() throws DeadlockDetectedException {
        List<AbstractLock> allLocks;

        do {
            if (!schedulerLock.acquire(SCHEDULER_TIMEOUT)) {
                DeadlockDetectedException ex = new DeadlockDetectedException();
                siteManager.recordLogRecord(
                        LogRecordGenerator.lockSchedulerTimeout(ex));
                throw ex;
            } else {
                try {
                    pendingLocks.clear();
                    allLocks
                            = new ArrayList<AbstractLock>(activeLocks.values());
                } finally {
                    schedulerLock.release();
                }

                for (AbstractLock lock : allLocks) {
                    try {
                        releaseLock(lock);
                    } catch (IllegalArgumentException ex) {
                        // The lock was active a moment ago, but apparently it's
                        // not active now. No big deal -- just ignore this
                        // error.
                    }
                }
            }
        } while (!allLocks.isEmpty());
    }

    /**
     * Normally called on a periodic basis, this method detects all "orphaned"
     * locks -- those that have expired but that have not been released. This
     * method forcefully releases these locks, notifying them by their
     * {@code revoke()} methods. Those locks that have not expired, or generally
     * do not expire, are not affected.
     * 
     * @throws DeadlockDetectedException if deadlock is detected.
     */
    public void releaseExpiredLocks() throws DeadlockDetectedException {
        long timeNow = System.currentTimeMillis();

        // Obtain a snapshot of all locks currently held.
        if (!schedulerLock.acquire(SCHEDULER_TIMEOUT)) {
            DeadlockDetectedException ex = new DeadlockDetectedException();
            siteManager.recordLogRecord(
                    LogRecordGenerator.lockSchedulerTimeout(ex));
            throw ex;
        } else {
            List<AbstractLock> allLocks;
            
            try {
                allLocks = new ArrayList<AbstractLock>(activeLocks.values());
            } finally {
                schedulerLock.release();
            }

            // Iterate through the snapshot and release those locks that have
            // expired.
            for (AbstractLock lock : allLocks) {
                if (lock.hasExpired(timeNow)) {
                    try {
                        releaseLock(lock);
                    } catch (IllegalArgumentException ex) {
                        // The lock was active a moment ago, but apparently it's
                        // not active now. No big deal -- just ignore this
                        // error.
                    }
                }
            }
        }
    }

    /**
     * Returns the lock object associated with the specified lock id, or throws
     * an exception if no such lock is active at the present time. Note that
     * pending locks and registered locks not active are not returned by this
     * function.
     * 
     * @param id the Integer ID of the desired lock; must not be {@code null}
     * 
     * @return the active AbstractLock associated with the specified ID
     * 
     * @throws DeadlockDetectedException if deadlock is detected.
     * @throws ResourceNotFoundException if the specified lock id could not be
     *         found within the set of active locks.
     */
    public AbstractLock getLock(Integer id) throws DeadlockDetectedException,
            ResourceNotFoundException {
        if (!schedulerLock.acquire(SCHEDULER_TIMEOUT)) {
            DeadlockDetectedException ex = new DeadlockDetectedException();
            siteManager.recordLogRecord(
                    LogRecordGenerator.lockSchedulerTimeout(ex));
            throw ex;
        } else {
            AbstractLock lock;

            try {
                lock = activeLocks.get(id);
            } finally {
                schedulerLock.release();
            }
            
            if ((lock == null) || lock.hasExpired(System.currentTimeMillis())) {
                throw new ResourceNotFoundException();
            } else {
                return lock;
            }
        }
    }

    /**
     * Generally the first thing done with a new lock object, this method must
     * be called exactly once for each lock object to be used with this
     * {@code LockAgent}. Passing a new lock object to other methods before it
     * has been registered via this method may result in exceptions being
     * thrown.
     * 
     * @param lock the AbstractLock to register
     */
    public void registerLock(AbstractLock lock) {
        Integer lockId;
        
        // Find an unused lock id.
        synchronized (registeredLockIds) {
            
            /*
             * This should be performed inside the synchronized block because it
             * may (indirectly) acquire the 'registeredLockIds' Map's monitor
             * anyway, possibly multiple times:
             */
            clearReleasedLockIds();
            
            // Randomly generate a lock ID, and make sure it is not already used
            do {
                lockId = randomGenerator.nextInt(Integer.MAX_VALUE);
            } while (registeredLockIds.containsKey(lockId));

            // Remember the lock id as having been issued.
            registeredLockIds.put(lockId,
                    new LockIdReference<AbstractLock>(lock, lockId));
        }

        // Tell the lock about ourselves.
        lock.setStackTracesEnabled(this.enableStackTraces);
        lock.registerWithAgent(this, lockId.intValue());
    }

    /**
     * Polls this agent's queue of released lock references repeatedly until it
     * is empty, unregistering the lock IDs of the locks referenced by the
     * retrieved References.  For efficiency, this method should be invoked by
     * a thread already holding the monitor of the 'registeredLockIds' Map. 
     */
    private void clearReleasedLockIds() {
        for (Reference<? extends AbstractLock> ref = lockReferenceQueue.poll();
                ref != null;
                ref = lockReferenceQueue.poll()) {
            ((LockIdReference<?>) ref).unregisterLockId();
        }
    }

    /**
     * Attempts to lock the specified resources on behalf of the caller. If
     * {@code shouldBlock} is true, then the method will not return until the
     * lock has been granted, but may block for some time until required
     * resources become available. If {@code shouldBlock} is false then the
     * method will not block, but the specified lock may not have been acquired:
     * the caller can discover if this is the case by invoking
     * {@code lock.isActive()}.
     * 
     * @param lock an {@code AbstractLock} object previously registered via
     *        {@code registerLock()}.
     * @param shouldBlock if true, then this method will block until no
     *        conflicts exist before granting the lock. If false, this method
     *        may return without the lock having been granted.
     * @throws DeadlockDetectedException if the scheduler lock could not be
     *         obtained within {@code SCHEDULER_TIMEOUT} milliseconds, or if
     *         {@code shouldBlock} is true and {@code acquisitionTimeout}
     *         milliseconds (as specified at construction time) have elapsed
     *         without the lock having been acquired. In this case, the
     *         acquisition of {@code lock} will have been abandoned.
     */
    public void acquireLock(AbstractLock lock, boolean shouldBlock)
            throws DeadlockDetectedException {
        
        // Acquire the scheduler.
        if (!schedulerLock.acquire(SCHEDULER_TIMEOUT)) {
            DeadlockDetectedException ex = new DeadlockDetectedException();

            siteManager.recordLogRecord(
                    LogRecordGenerator.lockSchedulerTimeout(ex));
            throw ex;
        } else {
            boolean waitToGrant;

            try {
                // Decide whether this lock can be acquired immediately.
                if (canLockImmediately(lock)) {
                    // The lock can be acquired immediately; no need to block.
                    // Update
                    // all the state variables and return to the caller.
                    grantLock(lock);
                    waitToGrant = false;
                } else if (!shouldBlock) {
                    // Abort now, without blocking, like the caller requested.
                    return;
                } else {

                    // Put the lock in the pending lock queue
                    pendingLocks.add(-(1 + Collections.binarySearch(
                            pendingLocks, lock, pendingLockComparator)), lock);
                    waitToGrant = true;
                }
                
            // make sure to release the scheduler
            } finally {
                schedulerLock.release();
            }
            
            if (waitToGrant) {

                // Block until the lock can be granted by the scheduler.
                try {
                    lock.waitUntilGranted(acquisitionTimeout);
                } catch (DeadlockDetectedException ex) {
                    
                    /*
                     * Acquisition timed out; log pertinent details including
                     * the active lock list.
                     */
                    if (schedulerLock.acquire(SCHEDULER_TIMEOUT)) {
                        try {
                            siteManager.recordLogRecord(
                                    LogRecordGenerator.lockAcquisitionTimeout(
                                            ex, lock, null,
                                            activeLocks.values(),
                                            pendingLocks, freeConnections,
                                            busyConnections));
                            pendingLocks.remove(lock);
                        } finally {
                            schedulerLock.release();
                        }
                    }
                    throw ex;
                }
            }
            
            // The lock was granted; notify listeners
            notifyListeners(lock, null);
        }
    }

    /**
     * <p>
     * Promotes one lock to another; that is, alters the resources locked by the
     * caller in an atomic fashion. Generally {@code newLock} would describe
     * "more" resources than {@code oldLock}; thus the term "promote". Atomic
     * lock promotion is an inherent deadlock risk -- the caller should use this
     * method sparingly and with great care.
     * </p><p>
     * If both {@code oldLock} and {@code newLock} require database connections
     * (as returned by {@code AbstractLock.getNeedsDbConnection()}), then the
     * database {@code Connection} object loaned to {@code newLock} will be the
     * one that had been loaned to {@code oldLock}.
     * </p>
     * 
     * @param oldLock an {@code AbstractLock} object previously acquired by the
     *        caller and currently active. On successful return this lock will
     *        have been revoked.
     * @param newLock an {@code AbstractLock} object previously registered but
     *        not currently active. On successful return this lock will have
     *        been granted.
     * @throws DeadlockDetectedException if the scheduler lock could not be
     *         obtained within {@code SCHEDULER_TIMEOUT} milliseconds, or if
     *         {@code acquisitionTimeout} milliseconds (as specified at
     *         construction time) have elapsed without the lock having been
     *         promoted. In this case, the acqusition of {@code newLock} will
     *         have been abandoned and {@code oldLock} will have been revoked.
     * @throws IllegalArgumentException if either of the locks has a state that
     *         would prohibit it from being promoted.
     */
    public void promoteLock(AbstractLock oldLock, AbstractLock newLock)
            throws DeadlockDetectedException {
        promoteLock(oldLock, newLock, true);
    }

    /**
     * <p>
     * Promotes one lock to another; that is, alters the resources locked by the
     * caller in an atomic fashion. Generally {@code newLock} would describe
     * "more" resources than {@code oldLock}; thus the term "promote". Atomic
     * lock promotion is an inherent deadlock risk -- the caller should use this
     * method sparingly and with great care.
     * </p><p>
     * If both {@code oldLock} and {@code newLock} require database connections
     * (as returned by {@code AbstractLock.getNeedsDbConnection()}), then the
     * database {@code Connection} object loaned to {@code newLock} will be the
     * one that had been loaned to {@code oldLock}.
     * </p>
     * 
     * @param oldLock an {@code AbstractLock} object previously acquired by the
     *        caller and currently active. On successful return this lock will
     *        have been revoked.
     * @param newLock an {@code AbstractLock} object previously registered. On
     *        successful return this lock will have been granted.
     * @param shouldBlock see description for {@code acquireLock()}.
     * @throws DeadlockDetectedException if the scheduler lock could not be
     *         obtained within {@code SCHEDULER_TIMEOUT} milliseconds, or if
     *         {@code shouldBlock} is true and {@code acquisitionTimeout}
     *         milliseconds (as specified at construction time) have elapsed
     *         without the lock having been promoted. In this case, the
     *         acqusition of {@code newLock} will have been abandoned and
     *         {@code oldLock} will have been revoked.
     * @throws IllegalArgumentException if either of the locks has a state that
     *         would prohibit it from being promoted.
     */
    public void promoteLock(AbstractLock oldLock, AbstractLock newLock,
            boolean shouldBlock) throws DeadlockDetectedException {
        
        // Sanity check
        if (!oldLock.isActive() || newLock.isActive()
                || (oldLock.getNextInChain() != null)
                || (newLock.getPrevInChain() != null)
                || (newLock.getNextInChain() != null)) {
            throw new IllegalArgumentException();
        }

        // Acquire the scheduler lock
        if (!schedulerLock.acquire(SCHEDULER_TIMEOUT)) {
            DeadlockDetectedException ex = new DeadlockDetectedException();

            siteManager.recordLogRecord(
                    LogRecordGenerator.lockSchedulerTimeout(ex));
            throw ex;
        } else {
            boolean waitToPromote;

            // The scheduler lock is released in the finally of this try block:
            try {
                
                // tell the new and old locks about each other
                oldLock.setNextInChain(newLock);
                newLock.setPrevInChain(oldLock);
                
                // determine whether granting the new lock must be deferred
                if (canLockImmediately(newLock)) {
                    
                    /*
                     * The lock can be acquired immediately; no need to block.
                     */
                    grantLock(newLock);

                    /*
                     * The promotion may have released resources that another
                     * thread is waiting on; run the scheduler to notify any
                     * threads that can now proceed.
                     */
                    scheduler();
                    waitToPromote = false;
                } else if (!shouldBlock) {
                    // Roll back and abort without blocking, as the caller
                    // requested
                    oldLock.setNextInChain(null);
                    newLock.setPrevInChain(null);
                    return;
                } else {

                    /*
                     * Put the new lock in the pending lock queue. The queue
                     * ordering will probably place it at or near the front
                     * because its promotion is pending, but other promotions
                     * that were registered earlier will tend to be granted
                     * first
                     */
                    if (pendingLocks.isEmpty()) {
                        pendingLocks.add(newLock);
                    } else {
                        ListIterator<AbstractLock> pendingLockIterator
                                = pendingLocks.listIterator();

                        while (pendingLockIterator.hasNext()) {
                            AbstractLock otherLock = pendingLockIterator.next();
                            
                            if (pendingLockComparator.compare(
                                    newLock, otherLock) <= 0) {
                                break;
                            }
                        }
                        pendingLocks.add(
                                pendingLockIterator.previousIndex(), newLock);
                    }
                    waitToPromote = true;
                }
                
            // Ensure that the scheduler lock is released
            } finally {
                schedulerLock.release();
            }

            if (waitToPromote) {

                // Block until the lock can be granted by the scheduler.
                try {
                    newLock.waitUntilGranted(acquisitionTimeout);
                } catch (DeadlockDetectedException ex) {
                    /*
                     * Acquisition timed out; log pertinent details including
                     * the active lock list.
                     */
                    if (schedulerLock.acquire(SCHEDULER_TIMEOUT)) {
                        try {
                            siteManager.recordLogRecord(
                                    LogRecordGenerator.lockAcquisitionTimeout(
                                            ex, newLock, null,
                                            activeLocks.values(),
                                            pendingLocks, freeConnections,
                                            busyConnections));
                            pendingLocks.remove(newLock);
                            revokeLock(oldLock, true);
                        } finally {
                            schedulerLock.release();
                        }
                    }
                    throw ex;
                }
            }

            notifyListeners(newLock, oldLock);
        }
    }

    /**
     * Behaves similarly to {@code promoteLock()} above. Generally
     * {@code newLock} would describe "fewer" resources than {@code oldLock};
     * thus the term "demote". Lock demotions generally are not a deadlock risk.
     * 
     * @param oldLock the active AbstractLock to be demoted
     * @param newLock the inactive AbstractLock to be demoted to
     * 
     * @throws DeadlockDetectedException if the scheduler lock cannot be
     *         acquired within the configured scheduler timeout, or if the new
     *         lock cannot be acquired within the acquisition timeout
     */
    public void demoteLock(AbstractLock oldLock, AbstractLock newLock)
            throws DeadlockDetectedException {
        /*
         * TODO: perhaps in the future this method's implementation could be
         * different from promoteLock() above by using the promotion/demotion
         * hint to avoid deadlock by creative lock scheduling.
         */
        promoteLock(oldLock, newLock);
    }

    /**
     * Releases the resources locked by a previously-acquired lock object.
     * 
     * @param lock an active {@code AbstractLock} object acquired previously by
     *        a call to {@code acquireLock()}, {@code promoteLock()}, or
     *        {@code demoteLock()}.
     * @throws DeadlockDetectedException if the scheduler lock could not be
     *         obtained within {@code SCHEDULER_TIMEOUT} milliseconds. Because
     *         releases cannot cause deadlock themselves, this exception always
     *         indicates that another lock (or several other locks) have
     *         deadlocked and are monopolizing the scheduler.
     * @throws IllegalArgumentException if the specified lock is in a state that
     *         prevents it from being released (perhaps because the lock never
     *         was acquired in the first place, or has been promoted/demoted
     *         since)
     */
    public void releaseLock(AbstractLock lock)
            throws DeadlockDetectedException {
        // Quick sanity check.
        if (!lock.isActive() || (lock.getNextInChain() != null)) {
            throw new IllegalArgumentException();
        }

        // Acquire the scheduler lock
        if (!schedulerLock.acquire(SCHEDULER_TIMEOUT)) {
            DeadlockDetectedException ex = new DeadlockDetectedException();
            siteManager.recordLogRecord(
                    LogRecordGenerator.lockSchedulerTimeout(ex));
            throw ex;
        }
        try { 
            
            // Remove the lock from the list of active locks.
            if (!revokeLock(lock, true)) {
                throw new IllegalArgumentException();
            } else {

                /*
                 * The lock was successfully revoked; invoke the scheduler and
                 * let it grant any pending locks that it now can do.
                 */
                scheduler();
            }
            
        // Ensure that the scheduler lock is released
        } finally {
            schedulerLock.release();
        }
        
        notifyListeners(null, lock);
    }

    /**
     * Internal function that returns true if the caller may obtain the
     * requested lock right away (without blocking) and false if the caller must
     * register the lock as pending and then block. This includes an awareness
     * of the number of available database connections. The caller is
     * responsible for acquiring the schedulerLock before this method is
     * invoked.  An active lock that is also referenced by the proposed lock's
     * prevInChain field will never conflict with the proposed lock; this
     * behavior is necessary to support lock promotions/demotions.
     * 
     * @param lock the AbstractLock to be tested
     * 
     * @return {@code true} if the specified lock can be granted immediately
     */
    private boolean canLockImmediately(AbstractLock lock) {
        if (lock.getNeedsDbConnection() && freeConnections.isEmpty()
                && ((lock.getPrevInChain() == null)
                        || (!lock.getPrevInChain().getNeedsDbConnection()))) {
            // the number of database connections is insufficient
            return false;
        } else {
    
            /*
             * Check whether the lock would conflict with any active locks
             */
            
            for (AbstractLock activeLock : activeLocks.values()) {
                if (activeLock.getNextInChain() == lock) {
                    /*
                     * We're trying to promote / demote from this lock to the
                     * specified one; conflicts are not relevant because the
                     * two are logically the same lock 
                     */
                    continue;
                } else if (lock.wouldBeDisruptedBy(activeLock)
                        || activeLock.wouldBeDisruptedBy(lock)) {
                    // A conflict exists with activeLock.
                    return false;
                }
            }

            /*
             * If we get here, we may assume that no lock in the pending queue
             * should be granted before the one under consideration because the
             * current lock is eligible to be granted immediately and all locks
             * in the pending queue are not eligible to be granted immediately
             * (otherwise they would already have been granted).
             */
            
            return true;
        }
    }

    /**
     * Scans all the pending locks and awakens as many of
     * them as possible, avoiding any locking conflicts. This includes an
     * awareness of the number of available database connections. The caller is
     * respondible for acquiring the schedulerLock before this method is
     * invoked.
     */
    private void scheduler() {
        //  TODO: change this code to optimize things even more
        
        boolean grantedSomeLock;

        /*
         * Repeatedly iterate over the pending locks, in descending priority
         * order. On each iteration, signal as many of them to be granted as
         * possible, so long as they won't conflict with another lock already
         * granted or signalled. Stop after the first pass in which no locks are
         * granted. The double iteration is necessary because newly-granted
         * locks may have had previous locks in their chains; these would have
         * been revoked, possibly freeing resources required by pending locks
         * that couldn't be granted before.
         */
        do {
            grantedSomeLock = false;
            
            for (Iterator<AbstractLock> it = pendingLocks.iterator();
                    it.hasNext(); ) {
                AbstractLock pendingLock = it.next();
    
                if (canLockImmediately(pendingLock)) {
                    it.remove();
                    grantLock(pendingLock);
                    grantedSomeLock = true;
                }
            }
        } while (grantedSomeLock);
    }

    /**
     * Internal function that "grants" a new lock, including adding it to the
     * active set, giving it a db connection if needed, and removing its
     * predecessor (if any) from the active set. If {@code lock} is a promotion
     * from a previously-granted lock, the old lock is revoked, and any database
     * connection reference is transferred from the old lock to the new one.
     * This method does not remove the lock from the pending set; that is the
     * caller's responsibility. The caller must hold the {@code schedulerLock}
     * and have gotten a {@code true} result from {@code canLockImmediately()}
     * immediately prior to calling this method.
     * 
     * @param lock the AbstractLock to grant 
     */
    private void grantLock(AbstractLock lock) {
        AbstractLock prevLock = lock.getPrevInChain();
        boolean assignConnection = lock.getNeedsDbConnection();
        Connection conn;

        activeLocks.put(lock.getId(), lock);
        
        if (prevLock != null) {
            // The lock is promoting/demoting another one. Remove the old one.
            conn = (assignConnection && prevLock.getNeedsDbConnection())
                    ? prevLock.getConnection()
                    : null;
            revokeLock(prevLock, !assignConnection);
        } else {
            conn = null;
        }
        
        // Assign a new connection from the free connection pool if required
        if (assignConnection && (conn == null)) {
            conn = freeConnections.remove();
            busyConnections.add(conn);
        }
        
        lock.grant(conn);
    }

    /**
     * Internal function that "revokes" an active lock, including removing it
     * from the active set, and reclaiming its db connection (if any) if
     * directed to do so. Notifies the lock object of the revocation by invoking
     * {@link AbstractLock#revoke()}.  The caller must hold the schedulerLock
     * before invoking this method.
     * 
     * @param lock the AbstractLock to revoke
     * @param reclaimConnection {@code true} if this method should reclaim any
     *        connection held by the specified lock, {@code false} if not (for
     *        instance, if the lock is being promoted to a new one that will
     *        take over the connection)
     * @return {@code true} if the lock is successfully revoked, {@code false}
     *        if not (meaning it was not found among the currently active locks)
     */
    private boolean revokeLock(AbstractLock lock, boolean reclaimConnection) {
        if (activeLocks.remove(lock.getId()) == null) {
            return false;
        } else {
            try {
                Connection newlyFreeConnection = lock.revoke();
                
                if ((newlyFreeConnection != null) && reclaimConnection) {
                    busyConnections.remove(newlyFreeConnection);
                    freeConnections.add(newlyFreeConnection);
                }
            } catch (OperationFailedException ex) {
                // The lock subclass threw this exception while trying to clean
                // up its resources. Just log the error - there's nothing more
                // we can do.
                siteManager.recordLogRecord(
                        LogRecordGenerator.lockExceptionOnRevoke(lock, ex));
            }
            
            return true;
        }
    }

    /**
     * Notifies listeners that a grant, revocation, or promotion/demotion has
     * occurred. Do not invoke while holding the {@code schedulerLock}!
     * 
     * @param grantedLock the newly-granted lock, or {@code null} if there is
     *        none
     * @param revokedLock the just-revoked lock, or {@code null} if there is
     *        none 
     */
    private void notifyListeners(AbstractLock grantedLock,
            AbstractLock revokedLock) {
        /*
         * No explicit synchronization is required because the 'listeners'
         * object's class ensures that there is no data race between
         * iterating over this collection (or obtaining an iterator) and
         * updating the collection.
         */
        for (LockListener listener : listeners) {
            if (grantedLock != null) {
                listener.notifyLockGranted(grantedLock);
            }
            if (revokedLock != null) {
                listener.notifyLockRevoked(revokedLock);
            }
        }
    }

    /**
     * Interface that other classes may implement if they wish to receive
     * notifications of lock grants and revocations.
     * 
     * @see LockAgent#registerListener(LockListener)
     */
    public static interface LockListener {
        
        /**
         * Notifies this listener that the specified lock has been granted
         * 
         * @param grantedLock the {@code AbstractLock} that was granted; will
         *        not be {@code null}
         */
        public void notifyLockGranted(AbstractLock grantedLock);
        
        /**
         * Notifies this listener that the specified lock has been revoked
         * 
         * @param revokedLock the {@code AbstractLock} that was revoked; will
         *        not be {@code null}
         */
        public void notifyLockRevoked(AbstractLock revokedLock);
    }

    /**
     * Decides the priority order for granting (pending) locks.  This version
     * prefers locks representing promotions before other locks, and,
     * secondarily, favors locks with earlier registration times.
     */
    private static class PendingLockComparator
            implements Comparator<AbstractLock> {

        /**
         * {@inheritDoc}
         */
        public int compare(AbstractLock x, AbstractLock y) {
            
            /*
             * The only time a pending lock should have a previous lock in its
             * chain is when it represents a promoted version of that lock.
             * Such promoted locks are granted preferentially over other locks. 
             */
            if (x.getPrevInChain() != null) {
                if (y.getPrevInChain() == null) {
                    return -1;
                }
            } else if (y.getPrevInChain() != null) {
                return 1;
            }
            
            /*
             * Both locks either are or aren't promotions; prefer to grant
             * whichever was registered earlier first
             */
            long xt = x.getTimeRegistered();
            long yt = y.getTimeRegistered();
            
            if (xt < yt) {
                return -1;
            } else if (xt > yt) {
                return -1;
            } else {
                return 0;
            }
        }
    }
    
    /**
     * A PhantomReference to an AbstractLock that records the lock's ID so that
     * it can be retrieved after this reference has been enqueued
     * 
     * @author jobollin
     * @version 0.9.0
     */
    private class LockIdReference<T extends AbstractLock>
            extends PhantomReference<T> {
        
        /**
         * A copy of the ID of the referenced lock
         */
        private final Integer lockId;

        /**
         * Initializes a {@code LockIdReference} with the specified lock and
         * lock id.  The lock may not have yet been notified of its assigned ID,
         * so the ID needs to be provided explicitly instead of obtained from
         * the lock itself.
         * 
         * @param lock the AbstractLock referenced by the reference
         * @param id the Integer id of the lock referenced by this reference
         */
        public LockIdReference(T lock, Integer id) {
            super(lock, lockReferenceQueue);
            assert (id != null) : "null lock ID";
            lockId = id;
        }
        
        /**
         * Unregisters the ID of the lock that is referenced by this reference
         * by removing the corresponding entry from the 'registeredLockIds' map.
         * This method assumes that the ID of the lock referenced by this
         * reference is associated in that map with this reference object.
         * Users should note that this method synchronizes on the map before
         * removing the entry.
         */
        void unregisterLockId() {
            synchronized (registeredLockIds) {
                Reference<?> ref = registeredLockIds.remove(lockId);
                
                assert (ref != null) : "The lock's ID was already deregistered";
                assert (ref == this)
                        : "The wrong reference was associated with the lock ID";
            }
        }
    }
}
