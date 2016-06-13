/*
 * Reciprocal Net project
 * 
 * AbstractLock.java
 *
 * 27-May-2003: ekoperda wrote first draft
 * 09-Jun-2003: ekoperda added property 'ignoreConflictsWith'
 * 08-Jul-2003: ekoperda added toString() and stack trace support throughout to
 *              assist in debugging
 * 07-Jan-2004: ekoperda changed package references to match source tree
 *              reorganization
 * 29-Mar-2004: midurbin fixed bug #1041 in wouldBeDisruptedBy()
 * 23-May-2004: ekoperda removed clone() and added copy() in its place
 * 06-Apr-2006: jobollin reformatted the source; organized imports; removed the
 *              ignoreConflictsWith variable and associated methods
 * 30-May-2006: jobollin updated docs
 * 06-Jan-2008: ekoperda fixed getPromotedVersion() to tolerate very long
 *              promotion chains
 */

package org.recipnet.site.core.lock;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;

import org.recipnet.site.OperationFailedException;
import org.recipnet.site.UnexpectedExceptionException;
import org.recipnet.site.core.DeadlockDetectedException;
import org.recipnet.site.core.util.EventSignal;
import org.recipnet.site.shared.db.UserInfo;

/**
 * <p>
 * Abstract base class for locks; more specific types of locks descend from this
 * one. {@code AbstractLock} and {@code LockAgent} together form a typical
 * child/parent object-oriented locking arrangement.
 * </p><p>
 * An {@code AbstractLock} object goes through several phases in its life:
 * <ol>
 * <li>A subclass of {@code AbstractLock} is instantiated by the lock user. At
 * this point the lock object is considered to be <i>inactive</i>. Typically
 * the lock user's choice of subclass would be based upon the operations he
 * intends to perform while he holds the lock.</li>
 * <li>The lock user <i>registers</i> his new lock object with an existing
 * {@code LockAgent} by calling {@code LockAgent.registerLock()}. This step may
 * be performed either before or after step 3, below.</li>
 * <li>The lock user calls zero or more methods on his lock object to
 * <i>define</i> the nature of the lock. The definable properties vary from
 * {@code AbstractLock} subclass to {@code AbstractLock} subclass, but
 * {@code AbstractLock} itself provides a few definable properties that
 * subclasses may elect to expose. This step may be performed either before or
 * after step 2, above.</li>
 * <li>The lock user attempts to <i>acquire</i> the lock by calling
 * {@code LockAgent.acquireLock()}. The lock agent may block for a time (in
 * order to avoid locking conflicts) before <i>granting</i> the lock and
 * returning control to the caller. At this point the lock object is considered
 * to be <i>active</i>.</li>
 * <li>The lock user performs whatever operations he needs to, under the
 * authority and protection of his active lock.</li>
 * <li>The lock user <i>releases</i> the lock by calling
 * {@code LockAgent.releaseLock()}. The lock agent, in response, will
 * <i>revoke</i> the lock and return control to the caller. At this point the
 * lock object is considered to be <i>inactive</i> again.</li>
 * </ol>
 * </p><p>
 * A common requirement of locked operations (step 5, above) is access to a
 * database {@code Connection} object. To facilitate this, {@code LockAgent}
 * maintains a pool of database connections and is able to loan a
 * {@code Connection} to a lock object for the duration that it's active. Lock
 * objects indicate to {@code LockAgent} that they expect a database connection
 * at <i>grant</i> time by setting their {@code needsDbConnection} property.
 * Subclasses may signal the {@code AbstractLock} base class to behave this way
 * by calling {@code setNeedsDbConnection()}.
 * </p><p>
 * Another common requirement of locks is that they expire after a certain
 * amount of time has passed (presumably without activity). Lock objects
 * indicate to {@code LockAgent} that they have expired (timed out) and should
 * be revoked by returning {@code true} from {@code hasExpired()}. Subclasses
 * may signal the {@code AbstractLock} base class to behave this way by calling
 * {@code setExpiration()} and optionally {@code renew()}.
 * </p><p>
 * A minimal subclass of {@code AbstractLock} will provide a constructor and
 * override {@code wouldBeDisruptedBy()}. More advanced subclasses would
 * override other methods as well. Subclasses must ensure that their
 * implementations are thread-safe and pose no deadlock risk to
 * {@code LockAgent}. This class is thread-safe.
 * </p>
 */
public abstract class AbstractLock implements Cloneable,
        Comparable<AbstractLock> {
    
    public static final int INVALID_LOCK_ID = -1;

    public static final long INVALID_TIME = -1L;

    /** Records the system clock at the time this object was constructed. */
    private long timeCreated = System.currentTimeMillis();

    /**
     * Becomes signalled when the lock is granted; signalled by
     * {@link #grant(Connection)} and accessed by
     * {@link #waitUntilGranted(int)}. Supports internal cross-thread operations
     * in the lock agent.
     */
    private EventSignal signal = new EventSignal();

    /**
     * Indicates whether the lock agent should supply a database
     * {@code Connection} object to the lock at grant time; set by
     * {@link #setNeedsDbConnection()}.
     */
    private boolean needsDbConnection = false;

    /**
     * If positive, indicates the expiration interval, in milliseconds, that
     * lock agents should enforce for this lock (if negative, this lock should
     * not auto-expire); set by {@link #setExpiration(long)}.
     */
    private long expirationInterval = INVALID_TIME;

    /**
     * Indicates to the lock agent the user on whose behalf this lock object was
     * created (for informational purposes only), or {@code INVALID_USER_ID} if
     * this information is not available; set by {@link #setUserId(int)}.
     */
    private int userId = UserInfo.INVALID_USER_ID;

    /**
     * Reference to this lock's associated lock agent; set by invokes
     * {@code registerWithAgent()}.
     */
    private LockAgent lockAgent = null;

    /**
     * Records the system clock at the time this object was registered with a
     * lock agent; set by  {@code  registerWithAgent()} .
     */
    private long timeRegistered = INVALID_TIME;

    /**
     * Indicates whether this lock is actively held; set to {@code true} by
     * {@link #grant(Connection)} and to {@code false} by {@link #revoke()}.
     */
    private boolean currentlyActive = false;

    /**
     * The unique id assigned to this lock by its lock agent; set by
     * {@code registerWithAgent()} .
     */
    private int id = INVALID_LOCK_ID;

    /**
     * While the lock object is active, this is a reference to a valid database
     * {@code Connection} object that has been loaned to the lock's owner, or
     * {@code null} if no database connection is required; set by
     * {@code grant()} and again by {@code revoke()}.
     */
    private Connection connection = null;

    /**
     * Records the system clock at the time this lock object was granted by a
     * lock agent; set by {@code grant()}.
     */
    private long timeGranted = INVALID_TIME;

    /**
     * If {@code shouldExpire} is true, records the anticipated system clock
     * value at the time this lock object will auto-expire; set by
     * {@code grant()} and {@code renew()}.
     */
    private long timeToExpire = INVALID_TIME;

    /**
     * For a lock that has since been promoted to another lock by a lock agent,
     * this is a reference to the lock object that was promoted from this lock
     * object; set by {@code setNextInChain()}.
     */
    private AbstractLock nextInChain = null;

    /**
     * For a lock that was promoted from another lock by a lock agent, this is a
     * reference to the lock object from which this lock object was promoted;
     * set by {@code setPrevInChain()}.
     */
    private AbstractLock prevInChain = null;

    /**
     * Property that controls whether particular methods in this object record
     * stack traces as they're executed. Set by {@code setStackTracesEnabled()}.
     */
    private boolean stackTracesEnabled = false;

    /**
     * Contains a string representation of the call stack at the time
     * {@code registerWithAgent()} was invoked, if {@code stackTracesEnabled}
     * was true. Set by {@code registerWithAgent()}. This is intended for
     * debugging use only.
     */
    private String registerWithAgentStackTrace = null;

    /**
     * Contains a string representation of the call stack at the time
     * {@code grant()} was invoked, if {@code stackTracesEnabled} was true. Set
     * by {@code grant()}. This is intended for debugging use only.
     */
    private String grantStackTrace = null;

    /**
     * Contains a string representation of the call stack at the time
     * {@code revoke()} was invoked, if {@code stackTracesEnabled} was true. Set
     * by {@code revoke()}. This is intended for debugging use only.
     */
    private String revokeStackTrace = null;

    /**
     * For use by lock agents and subclasses: gets the value of this lock
     * object's {@code nextInChain} property. This implementation does not need
     * to be {@code synchronized} because of its trivial, inherently thread-safe
     * implementation.
     */
    protected AbstractLock getNextInChain() {
        return nextInChain;
    }

    /**
     * For use by lock agents only: sets the {@code nextInChain} property of
     * this lock object, as during a lock promotion or demotion. This
     * implementation does not need to be {@code synchronized} because of its
     * trivial, inherently thread-safe implementation.
     */
    protected void setNextInChain(AbstractLock lock) {
        nextInChain = lock;
    }

    /**
     * For use by lock agents and subclasses: gets the value of this lock
     * object's {@code prevInChain} property. This implementation does not need
     * to be {@code synchronized} because of its trivial, inherently thread-safe
     * implementation.
     */
    protected AbstractLock getPrevInChain() {
        return prevInChain;
    }

    /**
     * For use by lock agents only: sets the {@code prevInChain} property of
     * this lock object, as during a lock promotion or demotion. This
     * implementation does not need to be {@code synchronized} because of its
     * trivial, inherently thread-safe implementation.
     */
    protected void setPrevInChain(AbstractLock lock) {
        prevInChain = lock;
    }

    /**
     * Determines whether this lock is configured to record stack traces at the
     * times of significant events in its life cycle.  This method does not need
     * to be synchronized because it is only invoked by other methods of this
     * class that are themselves synchronized.  Any read of the
     * {@code stackTracesEnabled} property must be synchronized (as those other
     * methods already ensure) so that every thread observes changes to the
     * property made by threads other than themselves.
     * 
     * @return {@code true} if stack traces are enabled, {@code false} if not
     */
    private boolean isStackTracesEnabled() {
        return stackTracesEnabled;
    }

    /**
     * For use by lock agents only: configures whether this lock should record
     * stack traces at the times of significant events in its life cycle. This
     * implementation needs to be {@code synchronized} so that all threads will
     * observe the state change it effects.
     * 
     * @param stackTracesEnabled {@code true} to enable stack traces,
     *        {@code false} to disable them
     */
    protected synchronized void setStackTracesEnabled(
            boolean stackTracesEnabled) {
        this.stackTracesEnabled = stackTracesEnabled;
    }

    /**
     * For use by lock agents and subclasses: gets the value of this lock
     * object's {@code needsDbConnection} property.
     * 
     * @return {@code true} if this lock needs a DB connection, {@code false} if
     *         it doesn't
     */
    protected synchronized boolean getNeedsDbConnection() {
        return needsDbConnection;
    }

    /**
     * For use by subclasses only: defines that this lock will require that it
     * be loaned a database connection at grant time.
     * 
     * @throws IllegalStateException if this lock has already been granted.
     */
    protected synchronized void setNeedsDbConnection() {
        if (this.timeGranted != INVALID_TIME) {
            throw new IllegalStateException();
        }
        needsDbConnection = true;
    }

    /**
     * For use by lock agents only: detects whether this lock object has
     * auto-expired. The current implementation always returns false if
     * auto-expiration has not been enabled on this lock object via a prior call
     * to {@code setExpiration()}.
     * 
     * @return true if this lock has expired and should be revoked, false
     *         otherwise.
     * @param timeNow the current value of the system clock (as returned by by
     *        {@code System.currentTimeMillis()}, presumably cached by the
     *        caller for increased performance.
     */
    protected synchronized boolean hasExpired(long timeNow) {
        return (shouldExpire() && isActive() && (timeNow > timeToExpire));
    }

    /**
     * For use by lock agents only: used to detect potential conflicts between
     * lock objects. This knowledge should be used by a lock agent to detect and
     * avoid lock conflicts. Note that the semantics of this function imply a
     * one-way test: it is possible, and oftentimes desirable, for lock A to be
     * disrupted by lock B but lock B not to be disrupted by lock A. In such a
     * case the lock agent should consider a conflict to exist between locks A
     * and B.
     * <p>
     * Most subclasses will override this function, but any subclass
     * implementation may return {@code true} only; return values of
     * {@code false} are reserved for this base class. Subclass implementations
     * should delegate to the base class's version of this function, typically
     * by a line something like:
     * {@code return super.wouldBeDisruptedBy(otherLock);} .
     * <p>
     * This base class implementation provides appropriate semantics for
     * {@code otherLock}'s of type {@code MultiLock} and
     * {@code GenericExclusiveLock}, so a typical subclass may safely ignore
     * lock objects of these types.
     * 
     * @return true if the operations performed by a lock user by authority of
     *         this lock object (if the lock were active) would be or might be
     *         disrupted by the operations associated with {@code otherLock},
     *         false if no such disruption might occur.
     * @param otherLock the {@code AbstractLock} object for which disruption of
     *        this lock object should be tested.
     */
    protected boolean wouldBeDisruptedBy(AbstractLock otherLock) {
        if (otherLock instanceof MultiLock) {
            MultiLock multilock = (MultiLock) otherLock;
            
            for (AbstractLock otherSingleLock : multilock.getChildren()) {
                if (wouldBeDisruptedBy(otherSingleLock)) {
                    return true;
                }
            }
            return false;
        } else if (otherLock instanceof GenericExclusiveLock) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * For use by lock agents only: informs this lock object that is has just
     * been granted. The current implementation updates state variables
     * appropriately to signal that the lock now is active.
     * <p>
     * Subclasses may override this method, but any subclass implementation must
     * invoke {@code super.grant()} before doing anything else. Subclass
     * implementations should be short-running because lock agents typically
     * invoke this method from within their critical sections. This method is
     * guaranteed to be called by a lock agent no more than once per lock
     * object.
     * 
     * @param grantedConnection the database {@code Connection} being loaned to
     *        this lock for its active duration, or {@code null} if no such
     *        connection is being loaned.
     */
    protected synchronized void grant(Connection grantedConnection) {
        if (timeGranted != INVALID_TIME) {
            throw new IllegalStateException();
        } else {
            if (isStackTracesEnabled()) {
                grantStackTrace = generateStackTrace();
            }
        
            long timeNow = System.currentTimeMillis();
        
            timeGranted = timeNow;
            if (shouldExpire()) {
                timeToExpire = timeNow + expirationInterval;
            }
            currentlyActive = true;
            connection = grantedConnection;
        
            signal.send();
        }
    }

    /**
     * For use by lock agents only: informs this lock object that it has just
     * been revoked. The current implementation updates state variables
     * appropriately to signal that the lock is now inactive.
     * <p>
     * Subclasses may override this function, but any subclass implementation
     * should return the value of {@code super.revoke()}. This method is
     * guaranteed to be called by a lock agent exactly once per lock object that
     * has ever been granted. Subclasses that acquire and/or lock external
     * resources (e.g. files, etc.) should release these locks when this method
     * is invoked if they haven't already. Subclasses are cautioned that lock
     * agents may invoke this function unexpectedly (as a result of expiration,
     * or resource shortage, or imminent shutdown) and they must handle such
     * unexpected invocations cleanly and in a thread-safe manner. Subclasses
     * are cautioned further that a typical lock agent will invoke this method
     * from within its critical section: the method should be short-running and
     * must not make calls back to the lock agent.
     * 
     * @return the database {@code Connection} object that was loaned to this
     *         lock when it was granted, or null if no connection was loaned.
     */
    @SuppressWarnings("unused")
    protected synchronized Connection revoke() throws OperationFailedException {
        if (timeGranted == INVALID_TIME) {
            throw new IllegalStateException();
        }
        
        this.currentlyActive = false;
        if (isStackTracesEnabled()) {
            this.revokeStackTrace = generateStackTrace();
        }
        
        Connection c = this.connection;
        
        this.connection = null;
        
        return c;
    }

    /**
     * For use by lock agents only: convenience function that blocks until
     * {@code grant()} is invoked by another thread. This implementation does
     * not need to be {@code synchronized} because its
     * {@code EventSignal}-based implementation is thread-safe.
     * 
     * @param timeout the approximate number of milliseconds the method should
     *        block before throwing an exception.
     * @throws DeadlockDetectedException if {@code timeout} milliseconds elapse
     *         and {@code grant()} still has not been invoked.
     */
    protected void waitUntilGranted(int timeout)
            throws DeadlockDetectedException {
        /*
         * TODO: perhaps instead of DeadlockDetectedException, throwing some
         * sort of pessimistic locking exception would make more sense.
         */
        if (!signal.receive(timeout)) {
            throw new DeadlockDetectedException();
        }
    }

    /**
     * For use by lock agents only: informs this lock object that it has just
     * been registered with a lock agent. The current implementation updates
     * state variables appropriately to signal that the lock is now registered.
     * <p>
     * Subclass implementations may override this method, but must invoke
     * {@code super.registerWithAgent()} before doing anything else.
     * 
     * @param agent reference to the lock agent object doing the
     *        registering.
     * @param assignedId a unique lock id assigned by the lock agent.
     */
    protected synchronized void registerWithAgent(LockAgent agent,
            int assignedId) {
        if (this.lockAgent != null) {
            throw new IllegalStateException();
        }
        if (isStackTracesEnabled()) {
            this.registerWithAgentStackTrace = generateStackTrace();
        }
    
        this.lockAgent = agent;
        this.id = assignedId;
        timeRegistered = System.currentTimeMillis();
    }

    /**
     * For use by subclasses only: extends the time at which the lock will
     * auto-expire to {@code expirationInterval} milliseconds from now. This
     * makes sense only for a lock on which {@code setExpiration()} has been
     * invoked previously.
     * 
     * @throws IllegalStateException if expiration has not been enabled for this
     *         lock object.
     */
    protected synchronized void renew() {
        if (!isActive() || !shouldExpire()) {
            throw new IllegalStateException();
        }
        timeToExpire = System.currentTimeMillis() + expirationInterval;
    }

    /**
     * For use by subclasses only: defines that this lock will auto-expire a
     * specified number of milliseconds after being granted. After being
     * granted, the moment of expiration may be postponed by
     * {@code interval} milliseconds by invoking {@code renew()}, but the
     * expiration interval cannot be changed.
     * 
     * @param interval the number of milliseconds after lock grant or
     *        renewal that this lock object should auto-expire.
     * @throws IllegalStateException if this lock has already been granted.
     */
    protected synchronized void setExpiration(long interval) {
        if (interval <= 0) {
            throw new IllegalArgumentException(
                    "The lock expiration interval must be positive");
        }
        if (timeGranted != INVALID_TIME) {
            throw new IllegalStateException(
                    "Cannot change the lock expiration interval after the lock "
                    + "has already been granted");
        }
        expirationInterval = interval;
    }
    
    /**
     * Determines whether this lock is configured to auto-expire; this is the
     * case if and only if an expiration interval has previously been set on
     * this lock. This method does not need to be synchronized because it is
     * only invoked by other methods of this class that are themselves
     * synchronized. Any read of the {@code expirationInterval} property must be
     * synchronized (as those other methods already ensure) so that every thread
     * observes changes to the property made by threads other than themselves.
     * 
     * @return {@code true} if this lock should auto-expire, {@code false} if
     *         not
     */
    private boolean shouldExpire() {
        return (expirationInterval > 0);
    }

    /**
     * @return id of the user to whom this lock belongs, or
     *         {@code INVALID_USER_ID} if unknown. Callers may use this
     *         knowledge for information purposes only.
     */
    public synchronized int getUserId() {
        return userId;
    }

    /**
     * For use by subclasses only: defines that this lock "belongs" to a
     * specified user.  Must be invoked before this lock is granted.
     * 
     * @param userId identifies the user this lock belongs to, or
     *        {@code INVALID_USER_ID} if unknown.
     */
    protected synchronized void setUserId(int userId) {
        if (timeGranted != INVALID_TIME) {
            throw new IllegalStateException();
        }
        this.userId = userId;
    }

    /**
     * @return unique id of this lock object, as assigned by its associated lock
     *         agent, or {@code INVALID_LOCK_ID} if the lock has not yet been
     *         registered.
     */
    public synchronized int getId() {
        return id;
    }

    /**
     * @return a reference to the {@code LockAgent} object with which this lock
     *         has been registered.
     * @throws IllegalStateException if this lock has not yet been registered
     *         with a lock agent.
     */
    public LockAgent getLockAgent() {
        if (lockAgent == null) {
            throw new IllegalStateException();
        }
        return lockAgent;
    }

    /**
     * @return the system clock at the moment this lock object was created
     *         (constructed).
     */
    public long getTimeCreated() {
        return timeCreated;
    }

    /**
     * @return the system clock at the moment this lock object was registered
     *         with a lock agent.
     * @throws IllegalStateException if this lock object has not yet been
     *         registered with a lock agent.
     */
    public long getTimeRegistered() {
        if (timeRegistered == INVALID_TIME) {
            throw new IllegalStateException();
        }
        return timeRegistered;
    }

    /**
     * @return the system clock at the moment this lock object was granted by by
     *         a lock agent.
     * @throws IllegalStateException if this lock object has not yet been
     *         granted by a lock agent.
     */
    public synchronized long getTimeGranted() {
        if (timeGranted == INVALID_TIME) {
            throw new IllegalStateException();
        }
        return timeGranted;
    }

    /**
     * @return the anticipated system clock at the moment this lock object is
     *         expected to expire.
     * @throws IllegalStateException if this lock object has not yet been
     *         configured for auto-expiration.
     */
    public synchronized long getTimeToExpire() {
        if (!shouldExpire() || (timeToExpire == INVALID_TIME)) {
            throw new IllegalStateException();
        }
        return timeToExpire;
    }

    /**
     * @return true if this lock is active (has been granted by a lock agent and
     *         not yet revoked), or false otherwise.
     */
    public synchronized boolean isActive() {
        return currentlyActive;
    }

    /**
     * @return true if this lock has been registered with a lock agent, or false
     *         otherwise.
     */
    public synchronized boolean hasBeenRegistered() {
        return (lockAgent != null);
    }

    /**
     * @return the database {@code Connection} object that was loaned to this
     *         lock object by the lock agent at the time it was granted.
     * @throws IllegalStateException if this lock is not currently active, or if
     *         no database connection was loaned to this lock.
     */
    public synchronized Connection getConnection() {
        if (!isActive() || (connection == null)) {
            throw new IllegalStateException();
        }
        return connection;
    }

    /**
     * Returns the newest "version" of this lock. When an acquired lock is
     * promoted or demoted, a new SampleLock object replaces the old one. The
     * old lock continues to link to the new object, however, and this function
     * returns the newest lock in the chain. (More than two lock objects would
     * be present in the chain if the same logical lock were promoted/demoted
     * several times.)
     */
    public synchronized AbstractLock getPromotedVersion() {
	AbstractLock a = this;
	AbstractLock b = null;
	do {
	    b = a;
	    a = b.getNextInChain();
        } while (a != null);
        return b;
    }

    /**
     * Convenience method that simply delegates to
     * {@code LockAgent.acquireLock()}.
     */
    public void acquire() throws DeadlockDetectedException {
        acquire(true);
    }

    public void acquire(boolean shouldBlock) throws DeadlockDetectedException {
        getLockAgent().acquireLock(this, shouldBlock);
    }

    /**
     * Convenience method that simply delegates to
     * {@code LockAgent.releaseLock()}.
     */
    public void release() throws DeadlockDetectedException {
        getLockAgent().releaseLock(getPromotedVersion());
    }

    /**
     * Convenience method that simply delegates to
     * {@link LockAgent#promoteLock(AbstractLock, AbstractLock)}.
     */
    public void promoteFrom(AbstractLock oldLock)
            throws DeadlockDetectedException {
        assert (getPromotedVersion() == this);
        getLockAgent().promoteLock(oldLock.getPromotedVersion(), this);
    }

    /**
     * Convenience method that simply delegates to
     * {@link LockAgent#demoteLock(AbstractLock, AbstractLock)}.
     */
    public void demoteFrom(AbstractLock oldLock)
            throws DeadlockDetectedException {
        assert (getPromotedVersion() == this);
        getLockAgent().demoteLock(oldLock.getPromotedVersion(), this);
    }

    /**
     * Overrides function on {@code Object} like a good class should. Lock
     * objects are sorted according to their id values.
     */
    public int compareTo(AbstractLock lock) {
        return getId() - lock.getId();
    }

    /*
     * FIXME: Does this class really need to override equals() and hashCode()?
     */
    
    /**
     * Overrides function on {@code Object} like a good class should. Two lock
     * objects are equal if they have the same id.
     */
    @Override
    public boolean equals(Object o) {
        return ((o instanceof AbstractLock)
                && (getId() == ((AbstractLock) o).getId()));
    }

    /** Overrides function on {@code Object} like a good class should. */
    @Override
    public int hashCode() {
        return getId();
    }

    /**
     * Returns a copy of the current lock that describes the same access
     * privileges and conflicts, references the same logical resources, etc., as
     * the current lock. However, the returned copy has not been registered with
     * any lock agent. A caller wishing to access controlled resources should
     * should first register the returned copy with a lock agent and then
     * acquire it.
     * 
     * @return a copy of this lock
     */
    public synchronized AbstractLock copy() {
        try {
            AbstractLock x = (AbstractLock) super.clone();
            
            x.timeCreated = System.currentTimeMillis();
            x.signal = new EventSignal();
            x.lockAgent = null;
            x.timeRegistered = INVALID_TIME;
            x.currentlyActive = false;
            x.id = INVALID_LOCK_ID;
            x.connection = null;
            x.timeGranted = INVALID_TIME;
            x.timeToExpire = INVALID_TIME;
            x.nextInChain = null;
            x.prevInChain = null;
            
            return x;
        } catch (CloneNotSupportedException ex) {
            // Can't happen because this class is cloneable.
            throw new UnexpectedExceptionException(ex);
        }
    }

    /**
     * Helper function that returns a string representation of the current call
     * stack; useful for debugging purposes.
     * 
     * @return the stack trace as a {@code String}
     */
    protected String generateStackTrace() {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        
        new Throwable("Stack trace").printStackTrace(pw);
        pw.flush();
        
        return sw.toString();
    }

    /**
     * Returns a string representation of the current lock object, including
     * values for many of its key properties. This is intended for debugging use
     * only.
     * 
     * @return a {@code String} representation of this lock
     */
    @Override
    public synchronized String toString() {
        long now = System.currentTimeMillis();
        StringBuilder buf = new StringBuilder();

        buf.append(getClass().getName().substring(
                getClass().getPackage().getName().length() + 1));
        if (id != INVALID_LOCK_ID) {
            buf.append(" id=" + id);
        }
        buf.append(" active=" + currentlyActive);
        if (userId != UserInfo.INVALID_USER_ID) {
            buf.append(" userId=" + userId);
        }
        buf.append(" db=" + getNeedsDbConnection());
        if (prevInChain != null) {
            buf.append(" prev=" + prevInChain.id);
        }
        if (nextInChain != null) {
            buf.append(" next=" + nextInChain.id);
        }
        if (timeRegistered != INVALID_TIME) {
            buf.append(" regAgo=" + (now - timeRegistered));
        }
        if (timeGranted != INVALID_TIME) {
            buf.append(" gntAgo=" + (now - timeGranted));
        }
        if (timeToExpire != INVALID_TIME) {
            buf.append(" expIn=" + (timeToExpire - now));
        }
        if (this.registerWithAgentStackTrace != null) {
            buf.append(" regStack=[" + this.registerWithAgentStackTrace + "]");
        }
        if (this.grantStackTrace != null) {
            buf.append(" gntStack=[" + this.grantStackTrace + "]");
        }
        if (this.revokeStackTrace != null) {
            buf.append(" revStack=[" + this.revokeStackTrace + "]");
        }

        return buf.toString();
    }
}
