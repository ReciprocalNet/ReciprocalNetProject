/*
 * Reciprocal Net project
 * 
 * SimpleLock.java
 *
 * 27-May-2003: ekoperda wrote first draft
 * 08-Jul-2003: ekoperda added toString()
 * 07-Jan-2004: ekoperda changed package references to match source tree
 *              reorganization
 * 11-May-2004: ekoperda added support for multiple simultaneous resourceId's
 *              throughout, added isEncompassedBy(), and simplified
 *              synchronization
 * 01-Mar-2006: jobollin updated wouldBeDisruptedBy() to use
 *              Collections.disjoint(); reformatted the source; added type
 *              parameters
 * 16-May-2006: jobollin fixed incorrect value of GEN_OP_BASE
 * 30-May-2006: jobollin simplified the construtor scheme
 */

package org.recipnet.site.core.lock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.recipnet.site.shared.db.UserInfo;

/**
 * <p>
 * A subclass of {@code AbstractLock} that authorizes operations using numeric
 * <em>operation codes</em>, typically defined by constants external to this
 * class. A {@code SimpleLock} is defined by the set of operations it is allowed
 * to perform and the set of operations that, if performed by another thread,
 * would disrupt its operations. It is assumed that the <em>operation codes</em>
 * defined elsewhere are 64-bit integers with exactly one bit set; this allows
 * operation flags to be combined and compared in a bitwise fashion for speed. A
 * maximum of 63 distinct operation codes may be utilized with this class.
 * </p>
 * <p>
 * <em>General</em> operation codes are the obvious type: lock A "would be
 * disrupted by" lock B exactly when a a general operation code is both on lock
 * B's {@code operationsUsed} list and on lock A's {@code conflictingOperations}
 * list. General operation codes have a numeric value greater than or equal to
 * {@code GEN_OP_BASE} and less than {@code SPEC_OP_BASE} (and also with exactly
 * one bit set, as specified earlier).
 * </p>
 * <p>
 * <em>Specific</em> operation codes are the other type, so-named because they
 * reference specific resources by integer id numbers. Lock A "would be
 * disrupted by" lock B exactly when: a) a specific operation code is on lock
 * B's {@code operationUsed} list, and... b) the same specific operation code is
 * on lock A's {@code conflictingOperations} list, and... c) any of lock A's
 * {@code resourceIds} values equal any of lock B's {@code resourceIds} values.
 * Specific operation codes have a numeric value greater than or equal to
 * {@code SPEC_OP_BASE} and less than {@code Long.MAX_VALUE}.
 * </p>
 * <p>
 * This class is thread-safe.
 * </p>
 */
public class SimpleLock extends AbstractLock {

    /**
     * The lowest-order bit used for general operation codes
     */
    public static final long GEN_OP_BASE = 1L;

    /**
     * The lowest-order bit number used for specific operation codes
     */
    public static final long SPEC_OP_BASE = 1L << 32;

    /**
     * A bit mask that selects all general operations from an operation code
     */
    private static final long GENERAL_OPERATION_MASK = 0xFFFFFFFFL;

    /**
     * A bit mask that selects all specific operations from an operation code
     */
    private static final long SPECIFIC_OPERATION_MASK = 0x7FFFFFFF00000000L;

    /**
     * Set at construction time, either null or a collection of zero or more
     * Integer objects.
     */
    private Collection<Integer> resourceIds;

    /** Set at construction time, or 0. */
    private long operationsUsed;

    /** Set at construction time, or 0. */
    private long conflictingOperations;

    /**
     * Constructor that takes the bare minimum arguments.
     * 
     * @param needsDbConnection the {@code needsDbConnection} property of the
     *        {@code AbstractLock} superclass is set to this value for
     *        convenience.
     * @param operationsUsed logical OR-ing of zero or more operation codes that
     *        together define the set of operations the lock holder will have
     *        authority to perform. No <i>specific</i> operation codes may be
     *        included in this value, for this constructor.
     * @param conflictingOperations logical OR-ing of zero or more operation
     *        codes that together define the set of operations that may not be
     *        performed by other threads while this lock is held. No <i>specific</i>
     *        operation codes may be included in this value, for this
     *        constructor.
     * @throws IllegalArgumentException if either {@code operationsUsed}
     *         {@code conflictingOperations} includes a <i>specific</i>
     *         operation code.
     */
    public SimpleLock(boolean needsDbConnection, long operationsUsed,
            long conflictingOperations) {
        this(null, needsDbConnection, operationsUsed, conflictingOperations,
                UserInfo.INVALID_USER_ID);
        if (((operationsUsed & SPECIFIC_OPERATION_MASK) != 0)
                || ((conflictingOperations & SPECIFIC_OPERATION_MASK) != 0)) {
            // Caller should have given us a resourceId if he wants to use
            // the operation flags he passed.
            throw new IllegalArgumentException();
        }
    }

    /**
     * Constructor that takes a resource id.
     * 
     * @param resourceId an integer that identifies the specific resource being
     *        accessed.
     * @param needsDbConnection the {@code needsDbConnection} property of the
     *        {@code AbstractLock} superclass is set to this value for
     *        convenience.
     * @param operationsUsed logical OR-ing of zero or more operation codes that
     *        together define the set of operations the lock holder will have
     *        authority to perform.
     * @param conflictingOperations logical OR-ing of zero or more operation
     *        codes that together define the set of operations that may not be
     *        performed by other threads while this lock is held.
     */
    public SimpleLock(int resourceId, boolean needsDbConnection,
            long operationsUsed, long conflictingOperations) {
        this(Collections.singleton(Integer.valueOf(resourceId)),
                needsDbConnection, operationsUsed, conflictingOperations,
                UserInfo.INVALID_USER_ID);
    }

    /**
     * Constructor that takes an array of resource id's.
     * 
     * @param resourceIds an array of {@code int}'s that identifies the
     *        specific resources being accessed.
     * @param needsDbConnection the {@code needsDbConnection} property of the
     *        {@code AbstractLock} superclass is set to this value for
     *        convenience.
     * @param operationsUsed logical OR-ing of zero or more operation codes that
     *        together define the set of operations the lock holder will have
     *        authority to perform.
     * @param conflictingOperations logical OR-ing of zero or more operation
     *        codes that together define the set of operations that may not be
     *        performed by other threads while this lock is held.
     */
    public SimpleLock(int resourceIds[], boolean needsDbConnection,
            long operationsUsed, long conflictingOperations) {
        this(createCollection(resourceIds), needsDbConnection, operationsUsed,
                conflictingOperations, UserInfo.INVALID_USER_ID);
    }

    /**
     * Constructor that takes an array of resource id's.
     * 
     * @param resourceIds a {@code Collection} of zero or more {@code Integer}
     *        objects that identify the specific resources being accessed;
     *        this collection is copied, so no reference to it is retained by
     *        the new lock; should not be {@code null} 
     * @param needsDbConnection the {@code needsDbConnection} property of the
     *        {@code AbstractLock} superclass is set to this value for
     *        convenience.
     * @param operationsUsed logical OR-ing of zero or more operation codes that
     *        together define the set of operations the lock holder will have
     *        authority to perform.
     * @param conflictingOperations logical OR-ing of zero or more operation
     *        codes that together define the set of operations that may not be
     *        performed by other threads while this lock is held.
     */
    public SimpleLock(Collection<Integer> resourceIds,
            boolean needsDbConnection, long operationsUsed,
            long conflictingOperations) {
        this(new ArrayList<Integer>(resourceIds), needsDbConnection,
                operationsUsed, conflictingOperations, UserInfo.INVALID_USER_ID);
    }

    /**
     * Constructor that takes a resource id and a user id.
     * 
     * @param resourceId an integer that identifies the specific resource being
     *        accessed.
     * @param needsDbConnection the {@code needsDbConnection} property of the
     *        {@code AbstractLock} superclass is set to this value for
     *        convenience.
     * @param operationsUsed logical OR-ing of zero or more operation codes that
     *        together define the set of operations the lock holder will have
     *        authority to perform.
     * @param conflictingOperations logical OR-ing of zero or more operation
     *        codes that together define the set of operations that may not be
     *        performed by other threads while this lock is held.
     * @param userId the {@code userId} property of the {@code AbstractLock}
     *        superclass is set to this value for convenience.
     */
    public SimpleLock(int resourceId, boolean needsDbConnection,
            long operationsUsed, long conflictingOperations, int userId) {
        this(Collections.singleton(Integer.valueOf(resourceId)),
                needsDbConnection, operationsUsed, conflictingOperations,
                userId);
    }

    /**
     * Constructor that takes a resource id and a user id.
     * 
     * @param resourceIds a {@code Collection} of zero or more {@code Integer}
     *        objects that identify the specific resources being accessed, or
     *        {@code null} if no specific resources are associated with this
     *        lock; the new {@code SimpleLock} retains a reference to this
     *        {@code Collection}, so it should not be modified externally
     * @param needsDbConnection the {@code needsDbConnection} property of the
     *        {@code AbstractLock} superclass is set to this value for
     *        convenience.
     * @param operationsUsed logical OR-ing of zero or more operation codes that
     *        together define the set of operations the lock holder will have
     *        authority to perform.
     * @param conflictingOperations logical OR-ing of zero or more operation
     *        codes that together define the set of operations that may not be
     *        performed by other threads while this lock is held.
     * @param userId the {@code userId} property of the {@code AbstractLock}
     *        superclass is set to this value for convenience.
     */
    private SimpleLock(Collection<Integer> resourceIds,
            boolean needsDbConnection, long operationsUsed,
            long conflictingOperations, int userId) {
        if (needsDbConnection) {
            setNeedsDbConnection();
        }
        this.resourceIds = resourceIds;
        this.operationsUsed = operationsUsed;
        this.conflictingOperations = conflictingOperations;
        setUserId(userId);
    }

    /**
     * Creates a collection of {@code Integer}s containing representations of
     * all the specified ints
     * 
     * @param ints the zero or more {@code int}s for which a collection is
     *        requested, or an array of the same
     * @return a {@code Collection} of {@code Integer}s representing the
     *         argument(s)
     */
    private static Collection<Integer> createCollection(int... ints) {
        Collection<Integer> collection = new ArrayList<Integer>(ints.length);

        for (int i : ints) {
            collection.add(Integer.valueOf(i));
        }

        return collection;
    }

    /**
     * {@inheritDoc}. This version returns {@code true} if the superclass's
     * implementation would, or additionally if {@code OtherLock} is a
     * {@code SimpleLock} and
     * <ul>
     * <li>this lock's {@code conflictingOperations} value contains the same
     * general operation code as {@code otherLock}'s {@code operationsUsed}
     * value, or</li>
     * <li>this lock's {@code conflictingOperations} value contains the same
     * specific operation code as {@code otherLock}'s {@code operationsUsed}
     * value and any of the values in this lock's {@code resourceIds} collection
     * equal any of the values in {@code otherLock}'s {@code resourceIds}
     * collection.</li>
     * </ul>
     * This method is not synchronized because the overrdden method on
     * {@code AbstractLock} is not.
     */
    @Override
    public boolean wouldBeDisruptedBy(AbstractLock otherLock) {
        if (otherLock instanceof SimpleLock) {
            SimpleLock otherSimpleLock = (SimpleLock) otherLock;
            if ((this.conflictingOperations & otherSimpleLock.operationsUsed & GENERAL_OPERATION_MASK) != 0) {
                // "General" operation constants conflict; no need to consult
                // resourceIds.
                return true;
            }
            if (((this.conflictingOperations & otherSimpleLock.operationsUsed & SPECIFIC_OPERATION_MASK) != 0)
                    && (this.resourceIds != null)
                    && (otherSimpleLock.resourceIds != null)
                    && !Collections.disjoint(this.resourceIds,
                            otherSimpleLock.resourceIds)) {
                /*
                 * "Specific" operation constants overlap and at least one
                 * resource id is referenced by both locks
                 */
                return true;
            }
        }

        // No disruption criterion specific to this class is met
        return super.wouldBeDisruptedBy(otherLock);
    }

    /**
     * Overrides function on {@code AbstractLock} -- for debugging use only.
     * Synchronized because the corresponding method on {@code AbstractLock} is.
     * 
     * @return a {@code String} representation of this lock
     */
    @Override
    public synchronized String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append(super.toString());
        sb.append(" ops=");
        for (int i = 0; i < 63; i++) {
            long flag = 1L << i;

            if ((this.operationsUsed & flag) != 0) {
                sb.append(i).append(',');
            }
        }
        sb.setLength(sb.length() - 1);

        sb.append(" conflicts=");
        for (int i = 0; i < 63; i++) {
            long flag = 1L << i;

            if ((this.conflictingOperations & flag) != 0) {
                sb.append(i).append(',');
            }
        }
        sb.setLength(sb.length() - 1);

        if ((this.resourceIds != null) && !this.resourceIds.isEmpty()) {
            sb.append(" resourceIds=");
            for (Integer resourceId : this.resourceIds) {
                sb.append(resourceId).append(',');
            }
        }
        sb.setLength(sb.length() - 1);

        return sb.toString();
    }

    /**
     * <p>
     * Determines whether a specified {@code otherLock}, if held by the caller,
     * would authorize him to perform all of the same operations that this lock
     * would (if held). Normally {@code otherLock} would be of type
     * {@code SimpleLock}, and for this function to return true in that case,
     * {@code otherLock} would need to enumerate all of this lock's
     * {@code operationsUsed}, {@code conflictingOperations}, and
     * {@code resourceIds} values. Additionally, the current implementation
     * supports {@code GenericExclusiveLock}s in the obvious fashion and
     * {@code MultiLock}s with one caveat, but always returns {@code false} for
     * {@code otherLocks}s of other types.
     * </p><p>
     * For an {@code otherLock} of type {@code MultiLock}, the current
     * implementation of this method invokes itself recursively to evaluate each
     * of {@code otherLock}'s children. If any of {@code otherLock}'s children
     * encompass this lock then {@code true} is returned; otherwise the result
     * is false. Note that this evaluation specifically is not cumulative with
     * respect to {@code otherLock}'s children: each child is evaluated
     * individually.
     * </p><p>
     * This method does not require synchronization because all variables
     * accessed are final.
     * </p>
     * 
     * @param otherLock the {@code AbstractLock} to compare to this lock
     * @return {@code true} if {@code otherLock} is determined to permit at
     *         least all of the operations permitted by this lock; {@code false}
     *         if not
     */
    public boolean isEncompassedBy(AbstractLock otherLock) {
        if (getNeedsDbConnection() && !otherLock.getNeedsDbConnection()) {
            return false;
        } else if (otherLock instanceof SimpleLock) {
            // Look in detail at the other SimpleLock's operations.
            SimpleLock otherSimpleLock = (SimpleLock) otherLock;

            return ((this.operationsUsed & otherSimpleLock.operationsUsed)
                    == this.operationsUsed)
                    && ((this.conflictingOperations
                            & otherSimpleLock.conflictingOperations)
                            == this.conflictingOperations)
                    && ((this.resourceIds == null)
                            || ((otherSimpleLock.resourceIds != null)
                                    && otherSimpleLock.resourceIds.containsAll(
                                            this.resourceIds)));
        } else if (otherLock instanceof MultiLock) {
            // Special handling for a MultiLock: check each of its children.
            for (AbstractLock lock : ((MultiLock) otherLock).getChildren()) {
                if (isEncompassedBy(lock)) {
                    return true;
                }
            }
            return false;
        } else if (otherLock instanceof GenericExclusiveLock) {
            /*
             * By definition, a GenericExclusiveLock can do anything, so the
             * determination is obvious.
             */
            return true;
        } else {
            /*
             * otherLock is not of a recognized type, so we assume it doesn't
             * encompass our functionality.
             */
            return false;
        }
    }
}
