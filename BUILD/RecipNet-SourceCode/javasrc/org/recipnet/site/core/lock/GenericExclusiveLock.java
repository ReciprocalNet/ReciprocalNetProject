/*
 * Reciprocal Net project
 * 
 * GenericExclusiveLock.java
 *
 * 27-May-2003: ekoperda wrote first draft
 * 30-May-2006: jobollin updated docs and reformatted the source
 */

package org.recipnet.site.core.lock;

/**
 * A subclass of {@code AbstractLock} that authorizes an operation with
 * exclusive access to all resources; <i>i.e.</i> this kind of lock "would be
 * disrupted by" any other lock.  This class is thread-safe.
 */
public class GenericExclusiveLock extends AbstractLock {
    
    /**
     * Initializes a new {@code GenericExclusiveLock}, specifying whether it
     * requires a DB connection.  Whether or not it asks for a connection has
     * no bearing on this lock's exclusivity
     * 
     * @param needsDbConnection {@code true} if this lock needs to be assigned a
     *        DB connection when granted, {@code false} if not
     */
    public GenericExclusiveLock(boolean needsDbConnection) {
        if (needsDbConnection) {
            setNeedsDbConnection();
        }
    }

    /**
     * {@inheritDoc}.  This version always returns {@code true}.
     */
    @Override
    protected boolean wouldBeDisruptedBy(
            @SuppressWarnings("unused") AbstractLock otherLock) {
        return true;
    }
}
