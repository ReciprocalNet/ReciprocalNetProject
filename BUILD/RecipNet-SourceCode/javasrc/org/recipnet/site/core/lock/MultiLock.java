/*
 * Reciprocal Net project
 * 
 * MultiLock.java
 *
 * 27-May-2003: ekoperda wrote first draft
 * 08-Jul-2003: ekoperda added toString()
 * 07-Jan-2004: ekoperda changed package references to match source tree
 *              reorganization
 * 26-May-2004: ekoperda removed getSampleLock(), added 5-param constructor,
 *              and simplified synchronization
 * 04-Aug-2005: midurbin added new constructor
 * 07-Apr-2006: jobollin converted to generics and enhanced for loops;
 *              reformatted the source, added override of copy() along the lines
 *              recommended by ekoperda (to fix bug #1733)
 */

package org.recipnet.site.core.lock;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import org.recipnet.site.OperationFailedException;
import org.recipnet.site.shared.db.UserInfo;

/**
 * A special-purpose subclass of {@code AbstractLock} that allows multiple child
 * locks to be combined. Child {@code AbstractLock} objects are registered with
 * a {@code MultiLock} during <i>definition</i> time by a lock user. The group
 * of locks then is granted as a group and later revoked as a group by the
 * {@code LockAgent}.
 * <p>
 * The function {@code AbstractLock.wouldBeDisruptedBy()} contains special logic
 * that ensures appropriate semantics when a {@code MultiLock} is tested for
 * possible disruption to any other lock: a conflict exists if the lock would be
 * disrupted by any of the {@code MultiLock}'s children.
 * <p>
 * This class is thread-safe.
 */
public class MultiLock extends AbstractLock {
    
    /**
     * A {@code Collection} of zero or more {@code AbstractLock} objects that
     * have been registered as children of this {@code MultiLock}. Set by the
     * constructor and modified by {@code AddLock}.
     */
    private final List<AbstractLock> childLocks;

    /**
     * Constructor that initializes the {@code MultiLock} with an arbitrary
     * number of child locks. Note that all of the locks must be distinct.
     * 
     * @param  locks the AbstractLock objects that collectively describe
     *         the resources locked by this MultiLock
     */
    public MultiLock(AbstractLock... locks) {
        childLocks = new ArrayList<AbstractLock>(locks.length);
        Collections.addAll(childLocks, locks);
    }

    /**
     * Returns an unmodifiable collection of all the child locks comprised by
     * this {@code MultiLock}
     *  
     * @return a {@code Collection} of {@code AbstractLock} objects representing
     *         the complete set of children comprised by this {@code MultiLock}.
     *         The returned collection will not change and cannot be modified.
     */
    public Collection<AbstractLock> getChildren() {
        return Collections.unmodifiableCollection(childLocks);
    }

    /**
     * {@inheritDoc}.  This version tests whether any of this lock's child locks
     * would be disrupted by the specified other lock.
     * 
     * @see AbstractLock#wouldBeDisruptedBy(AbstractLock)
     */
    @Override
    protected boolean wouldBeDisruptedBy(AbstractLock otherLock) {
        for (AbstractLock lock : childLocks) {
            if (lock.wouldBeDisruptedBy(otherLock)) {
                return true;
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}.  This version accepts the lock grant then forwards the
     * grant message to its child locks, in sequence.
     * 
     * @see AbstractLock#grant(Connection)
     */
    @Override
    protected synchronized void grant(Connection connection) {
        super.grant(connection);

        for (AbstractLock lock : childLocks) {
            lock.grant(connection);
        }
    }

    /**
     * {@inheritDoc}.  This version forwards the revocation message to each of
     * this lock's child locks, then performs its own revocation.
     * 
     * @see AbstractLock#revoke()
     */
    @Override
    protected synchronized Connection revoke() throws OperationFailedException {
        for (AbstractLock lock : childLocks) {
            lock.revoke();
        }

        return super.revoke();
    }

    /**
     * {@inheritDoc}.  This version tests the child locks in addition to this
     * lock's own configuration.
     * 
     * @return {@code true} if this lock or any of its child locks is configured
     *         to require a database connection
     *         
     * @see AbstractLock#getNeedsDbConnection()
     */
    @Override
    protected boolean getNeedsDbConnection() {
        if (super.getNeedsDbConnection()) {
            return true;
        } else {
            for (AbstractLock lock : childLocks) {
                if (lock.getNeedsDbConnection()) {
                    return true;
                }
            }
            
            return false;
        }
    }

    /**
     * {@inheritDoc}.  This version registers each of this {@code MultiLock}'s
     * children after it registers itself.
     * 
     * @see AbstractLock#registerWithAgent(LockAgent, int)
     */
    @Override
    protected synchronized void registerWithAgent(LockAgent lockAgent, int id) {
        super.registerWithAgent(lockAgent, id);

        for (AbstractLock lock : childLocks) {
            lock.registerWithAgent(lockAgent, id);
        }
    }

    /**
     * {@inheritDoc}. This version checks all the child locks for expiration
     * in addition to checking for expiration specifically of this MultiLock
     * itself.  Returns {@code true} if this lock specifically or any of its
     * children has expired.
     * 
     * @see AbstractLock#hasExpired(long)
     */
    @Override
    protected synchronized boolean hasExpired(long timeNow) {
        if (super.hasExpired(timeNow)) {
            return true;
        } else {
            for (AbstractLock lock : childLocks) {
                if (lock.hasExpired(timeNow)) {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * {@inheritDoc}.  If no user ID is set directly on this {@code MultiLock}
     * then the child locks are tested in sequence until one is found that
     * specifies a user ID, or all have been examined.
     * 
     * @return the user ID discovered on this lock or among its child locks, or
     *         UserInfo.INVALID_USER_ID if none is found
     *         
     * @see AbstractLock#getUserId()
     */
    @Override
    public synchronized int getUserId() {
        int myUserId = super.getUserId();
        
        if (myUserId != UserInfo.INVALID_USER_ID) {
            return myUserId;
        } else {
            for (AbstractLock lock : childLocks) {
                int childsUserId = lock.getUserId();
                
                if (childsUserId != UserInfo.INVALID_USER_ID) {
                    return childsUserId;
                }
            }
            return UserInfo.INVALID_USER_ID;
        }
    }

    /**
     * {@inheritDoc}  The child locks held by the copy are distinct objects from
     * those held by the original.
     * 
     * @see AbstractLock#copy()
     */
    @Override
    public synchronized MultiLock copy() {
        MultiLock copy = (MultiLock) super.copy();
        
        for (ListIterator<AbstractLock> childIt = childLocks.listIterator();
                childIt.hasNext(); ) {
            AbstractLock child = childIt.next();
            
            childIt.set(child.copy());
        }
        
        return copy;
    }

    /**
     * {@inheritDoc}.  This version includes string representations of this
     * lock's children in its output.
     * 
     * @return the {@code String} representation of this lock
     * 
     * @see AbstractLock#toString()
     */
    @Override
    public synchronized String toString() {
        StringBuilder buf = new StringBuilder();
        
        buf.append(super.toString());
        buf.append(" children=[");
        for (AbstractLock lock : childLocks) {
            buf.append("[");
            buf.append(lock.toString());
            buf.append("]");
        }
        buf.append("]");
        
        return buf.toString();
    }
}
