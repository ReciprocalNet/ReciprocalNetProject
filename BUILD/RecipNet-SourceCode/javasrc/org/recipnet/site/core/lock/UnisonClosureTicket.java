/*
 * Reciprocal Net project
 * @(#)UnisonClosureTicket.java
 *
 * 12-Jul-2004: ekoperda wrote first draft
 * 11-Feb-2005: ekoperda clarified javadocs
 */

package org.recipnet.site.core.lock;
import org.recipnet.site.OperationFailedException;

/**
 * An interface that makes sense only when implemented on a 
 * <code>RepositoryTicket</code> or subclass thereof.  A ticket that implements
 * this interface is capable of being closed in unison with other tickets in
 * order to enable a limited kind of transaction functionality.  The ticket may
 * be selective about the other tickets with which it will support a unison
 * closure; requiring them to be of a specific Java class is one common
 * limitation. <p>
 *
 * A typical use case involves the caller acquiring by some means a batch of
 * two or more tickets that are to be closed simultaneously.  The caller
 * selects one ticket in the batch to be the <i>master ticket</i> according to
 * some undefined algorithm.  (It is recommended, though, that for any
 * supported batch of tickets every ticket within the batch be capable of being
 * designated the <i>master ticket</i>.)  Then,
 *   1. The caller invokes <code>supportsClosureInUnisonWith()</code> on the 
 *      <i>master ticket</i>, passing an array that contains all other tickets
 *      in the batch as an argument to the function.  The <i>master ticket</i>
 *      decides whether a unison closure is supported with the supplied batch
 *      of tickets and returns either <code>true</code> or <code>false</code>
 *      accordingly.
 *   2. Assuming the <i>master ticket</i> returned true, the caller invokes
 *      <code>beforeUnisonClosure()</code> on every ticket in the batch 
 *      (including the <i>master ticket</i>) in some undefined order.
 *   3. The caller invokes <code>closeInUnisonWith()</code> on the <i>master
 *      ticket</i>, passing an array that contains all other tickets in the
 *      batch as an argument to the function.
 *   4. The caller invokes <code>afterUnisonClosure()</code> on every ticket
 *      in the batch (including the <i>master ticket</i>) in some undefined
 *      order.
 * The unison closure has then finished.
 */
public interface UnisonClosureTicket {
    /**
     * To be invoked on the master ticket during step 1 described at the class
     * level.  Should be invoked prior to <code>closeInUnisonWith()</code>.
     * @return true if a unison closure with the specified batch of tickets is
     *     supported, or false if not.
     * @param otherTickets an array containing references to the other tickets
     *     in the batch to be closed, not including the master ticket.
     */
    public boolean supportsClosureInUnisonWith(
            UnisonClosureTicket otherTickets[]);

    /**
     * To be invoked on every ticket in the batch (including the <i>master
     * ticket</i>) during step 2 described at the class level.  Should be 
     * invoked prior to <code>closeInUnisonWith()</code>.  Should not be
     * invoked unless <code>supportsClosureInUnisonWith()</code> returned true
     * previously for the batch.
     * @throws OperationFailedException on low-level error.
     */
    public void beforeUnisonClosure() throws OperationFailedException;

    /**
     * To be invoked on the master ticket during step 3 described at the class
     * level.  Should not be invoked unless
     * <code>supportsClosureInUnisonWith()</code> returned true previously for
     * the same set of <code>otherTickets</code>.
     * @param otherTickets an array containing references to the other tickets
     *     in the batch to be closed, not including the master ticket.
     * @throws OperationFailedException on low-level error.
     */
    public void closeInUnisonWith(UnisonClosureTicket otherTickets[])
            throws OperationFailedException;

    /**
     * To be invoked on every ticket in the batch (including the <i>master
     * ticket</i> during step 4 described at the class level.  Should be
     * invoked after <code>closeInUnisonWith()</code>.  Should not be invoked
     * unless <code>supportsClosureInUnisonWith()</code> returned true
     * previously for the batch.
     */
    public void afterUnisonClosure() throws OperationFailedException;
}
