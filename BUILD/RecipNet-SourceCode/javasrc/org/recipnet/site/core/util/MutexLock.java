/*
 * Reciprocal Net project
 * 
 * MutexLock.java
 *
 * 07-Jun-2002: ekoperda wrote first draft
 * 26-Sep-2002: ekoperda moved class to the core.util package; used to be in
 *              the core package
 * 07-Apr-2006: jobollin rewrote this class as a thin wrapper around a
 *              java.util.concurrent.Semaphore
 */

package org.recipnet.site.core.util;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * This is a simple utility class that allows multiple threads to synchronize
 * their activities. At most a specified number of threads (default 1) can
 * "hold" the mutex at the same time. Another name for this type of
 * synchronization aid is "semaphore", and the main difference between this
 * class and the {@code Semaphore} class of {@code java.util.concurrent} is that
 * this class's {@code acquire()} methods return {@code false} in situations
 * where {@code Semaphore} would throw InterruptedException.
 */
public class MutexLock {
    
    /**
     * A Semaphore held by this object, with which it implements its behavior.
     */
    private final Semaphore sem;
    
    /**
     * Initializes a {@code MutexLock} with to allow only single-thread access
     */
    public MutexLock() {
        this(1);
    }
    
    /**
     * Initializes a {@code MutexLock} to allow concurrent access by the
     * specified number of threads
     * 
     * @param threads the number of threads that may concurrently hold this
     *        Mutex
     */
    public MutexLock(int threads) {
        sem = new Semaphore(threads);
    }

    /** 
     * Wait indefinitely to acquire a lock.
     * 
     * @return {@code true} if the lock was actually acquired (normal), or
     *         {#code false} if the thread was interrupted while trying to
     *         acquire the lock
     */  
    public boolean acquire() {
        try {
            sem.acquire();
            return true;
        } catch (InterruptedException ie) {
            return false;
        }
    }

    /**
     * Wait up to the specified number of milliseconds to acquire a lock.
     * 
     * @param milliseconds the number of milliseconds to wait
     * @return {@code true} if the mutex was acquired within the specified time,
     *         {@code false} if not (including if the thread was interrupted)
     */
    public boolean acquire(int milliseconds) {
        try {
            return sem.tryAcquire(milliseconds, TimeUnit.MILLISECONDS);
        } catch (InterruptedException ie) {
            return false;
        }
    }

    /** Release a previously acquired lock */
    public void release() {
        sem.release();
    }
}
