/*
 * Reciprocal Net project
 * 
 * RepositoryTicket.java
 *
 * 27-May-2003: ekoperda wrote first draft
 * 18-May-2006: jobollin added touch(); reformatted the source
 */

package org.recipnet.site.core.lock;

import java.sql.Connection;
import org.recipnet.site.OperationFailedException;
import org.recipnet.site.OperationNotPermittedException;
import org.recipnet.site.core.DeadlockDetectedException;

/**
 * <p>
 * A subclass of {@code AbstractLock} that itself is an abstract base class for
 * repository tickets. A {@code RepositoryTicket} is a special kind of lock that
 * is long-lived, in the sense that it's referenced across multiple RMI-calls to
 * Repository Manager. In such cases the RMI-client normally would be returned
 * the lock id in the first call and would then specify the same lock id in
 * subsequent calls. Because RMI-clients cannot be relied upon to cleanly close
 * a lock id when they're finished accessing it, all {@code RepositoryTicket}'s
 * utilize {@code AbstractLock}'s auto-expiration feature.
 * </p><p>
 * By convention, a {@code RepositoryTicket}'s constructor supplies all
 * information to the object that it will need throughout its lifetime.
 * Polymorphic open/close/abort/read/write functionality allows Repository
 * Manager to handle tickets generically once they've been constructed.
 * </p><p>
 * Subclasses at minimum should provide a constructor (which invokes
 * {@code super()}) and override {@code wouldBeDisruptedBy()}. Most subclasses
 * also will override at least some of: {@code notifyLockRevoked()},
 * {@code open()}, {@code close()}, {@code abort()}, {@code read()}, and
 * {@code write()}. It's common for subclasses to encapsulate file-access
 * operations within these six methods, and invoke {@code super.renew()} from
 * within each of them to keep the ticket from expiring while accesses still are
 * in progress.
 * </p><p>
 * This class is thread-safe.
 * </p>
 */
public abstract class RepositoryTicket extends AbstractLock {

    /**
     * For use by subclasses only: protected constructor.
     * 
     * @param userId the {@code userId} property of the {@code AbstractLock}
     *        superclass is set to this value for convenience.
     * @param timeUntilExpiration the approximate number of milliseconds after
     *        ticket granting that the ticket should expire. This value is used
     *        to invoke {@code AbstractLock.setExpiration()}.
     */
    protected RepositoryTicket(int userId, long timeUntilExpiration) {
        setUserId(userId);
        setExpiration(timeUntilExpiration);
    }

    /**
     * {@inheritDoc}. This version invokes {@link #notifyLockRevoked()}, then
     * delegates to {@code AbstractLock}.  Subclasses should override
     * {@code notifyLockRevoked()} (and not this method) if they need to take
     * any action when revoked.
     * 
     * @throws OperationFailedException if an error occurred while attempting to
     *         free the filesystem resources associated with this ticket.
     */
    @Override
    protected final Connection revoke() throws OperationFailedException {
        notifyLockRevoked();
        return super.revoke();
    }

    /**
     * Subclasses should override this method in order to receive notifications
     * whenever the ticket has been revoked. A typical subclass would use this
     * opportunity to release any filesystem resources it hadn't released
     * already. Subclass implementers are reminded that this method may be
     * called as part of a ticket close or abort, or may be called unexpectedly,
     * and their code must handle either case cleanly and in a thread-safe
     * manner. Overriden versions of this method need not delegate to the this
     * class (the superclass).
     * 
     * @throws OperationFailedException if an error occurred while attempting to
     *         free the filesystem resources associated with this ticket.
     */
    @SuppressWarnings("unused")
    protected void notifyLockRevoked() throws OperationFailedException {
        // This version does nothing
    }

    /**
     * <p>
     * Opens/acquires/locks whatever filesystem resources will be used by this
     * ticket during future reads and writes.
     * </p><p>
     * Subclasses should override this method and use the opportunity to acquire
     * whatever filesystem resources they'll need to perform future reads and
     * writes. Subclass implementations must delegate back to this method before
     * doing anything else.
     * </p>
     * 
     * @throws OperationFailedException if an error occurred while attempting to
     *         acquire the filesystem resources associated with this ticket.
     */
    @SuppressWarnings("unused")
    public synchronized void open() throws OperationFailedException {
        if (!isActive()) {
            throw new IllegalStateException();
        }
    }

    /**
     * Performs a "graceful" close at the conclusion of a ticketed operation; in
     * the case of a typical write this might cause the written data to be
     * committed or persisted. The current implementation causes the lock to be
     * released as well.
     * <p>
     * Subclasses should override this method and use the opportunity to commit
     * any changes made during the ticket's lifetime. Subclass implementations
     * must delegate back to this method before returning.
     * 
     * @throws DeadlockDetectedException if deadlock was detected while
     *         releasing the lock.
     * @throws OperationFailedException if an error occurred while attempting to
     *         free the filesystem resources associated with this ticket.
     */
    @SuppressWarnings("unused")
    public synchronized void close() throws DeadlockDetectedException,
            OperationFailedException {
        if (!isActive()) {
            throw new IllegalStateException();
        }
        getLockAgent().releaseLock(this);
    }

    /**
     * Performs an "abrupt" close some time before a ticket operation's natural
     * end; in the case of a typical write this might cause the written data to
     * be discarded or rolled back. The current implementation causes the lock
     * to be released as well.
     * <p>
     * Subclasses should override this method and use the opportunity to
     * abort/abandon any changes made during the ticket's lifetime. Subclass
     * implementations must delegate back to this method before returning.
     * 
     * @throws DeadlockDetectedException if deadlock was detected while
     *         releasing the lock.
     * @throws OperationFailedException if an error occurred while attempting to
     *         free the filesystem resources associated with this ticket.
     */
    @SuppressWarnings("unused")
    public synchronized void abort() throws DeadlockDetectedException,
            OperationFailedException {
        if (!isActive()) {
            throw new IllegalStateException();
        }
        getLockAgent().releaseLock(this);
    }

    /**
     * "Touches" this ticket to extend its time before expiration by the
     * configured expiration interval.  This method does not interact directly
     * with any {@code LockAgent}, therefore it is much less of a deadlock risk
     * than many of the other methods are
     * 
     * @throws IllegalStateException if expiration has not been enabled for this
     *         ticket, or if this ticket is not active (including if it has
     *         already expired)
     */
    public synchronized void touch() {
        renew();
    }

    /**
     * Reads some data from the open file represented by this ticket.
     * <p>
     * The current implementation always throws an
     * {@code OperationNotPermittedException}. Subclasses may override this
     * function at will.
     * 
     * @return the number of bytes read from the file, or -1 on EOF.
     * @param buffer a caller-specified byte array in which read data should be
     *        stored.
     * @param offset identifies the index within {@code buffer} where data
     *        storage should begin.
     * @param length identifies the maximum number of bytes that should be read
     *        from the open file.
     * @throws OperationFailedException if the read failed due to a low-level
     *         error.
     * @throws OperationNotPermittedException if this kind of ticket does not
     *         support reading.
     */
    @SuppressWarnings("unused")
    public int read(byte[] buffer, int offset, int length)
            throws OperationFailedException, OperationNotPermittedException {
        throw new OperationNotPermittedException();
    }

    /**
     * Writes some data from the open file represented by this ticket.
     * <p>
     * The current implementation always throws an
     * {@code OperationNotPermittedException}. Subclasses may override this
     * function at will.
     * 
     * @param buffer a byte array whose complete contents are to be written to
     *        the open file
     * @throws OperationFailedException if the write failed due to a low-level
     *         error
     * @throws OperationNotPermittedException if this kind of ticket does not
     *         support writing.
     */
    @SuppressWarnings("unused")
    public void write(byte[] buffer) throws OperationFailedException,
            OperationNotPermittedException {
        throw new OperationNotPermittedException();
    }
}
