/*
 * Reciprocal Net project
 * 
 * CoreMessageQueue.java
 *
 * 07-Jun-2002: ekoperda wrote first draft
 * 26-Sep-2002: ekoperda moved class to the core.util package; used to be in
 *              the core package
 * 08-Oct-2002: ekoperda added prioritization support by writing 
 *              getCMComparator()
 * 09-Oct-2002: ekoperda changed logic in send() to guarantee that messages
 *              within the same priority level are sorted in FIFO order.
 * 30-Oct-2002: ekoperda added sendSeveral() method and made locking logic
 *              more rigorous
 * 24-Apr-2006: jobollin rewrote much of this class around a PriorityQueue;
 *              added much documentation
 */

package org.recipnet.site.core.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import org.recipnet.site.core.msg.CoreMessage;
import org.recipnet.site.core.msg.InterSiteMessage;
import org.recipnet.site.core.msg.ProcessedIsmCM;
import org.recipnet.site.core.msg.SendIsmCM;

/**
 * <p>
 * A queue of {@link CoreMessage} objects awaiting processing by one of the core
 * module's worker threads.  Messages are prioritized by class, and within
 * classes by class-specific criteria; this improves the Site Manager's
 * efficiency.
 * </p><p>
 * This class is thread-safe for multiple senders, but <strong>only a single
 * thread should attempt to {@link #receive(int) receive()} messages from this
 * queue at a time.</strong>
 * </p>
 */
public class CoreMessageQueue {
    
    /**
     * A {@code Queue} of {@code MessageWrapper} objects in the order that their
     * associated messages should be delivered
     */
    private final Queue<MessageWrapper> queue;
    
    /**
     * A {@code Set} of {@code InterSiteMessage}s currently enqueued; used to
     * avoiding having the same ISM enqueued multiple times
     */
    private final Set<InterSiteMessage> queuedIsms;

    /**
     * An {@code EventSignal} used when a message is enqueued to signal any
     * thread waiting to receive a message that it may proceed
     */
    private final EventSignal signal;

    /**
     * A {@code MutexLock} used to synchronize manipulation of this class's
     * internal data structures
     */
    private final MutexLock mutex;
    
    /**
     * The relative sequence number to assign to the next message that is
     * enqueued.  Assigning sequence numbers allows ordering of messages by
     * their order of enqueueing.
     */
    private int nextSequenceNumber;

    /**
     * Initializes a new, empty {@code CoreMessageQueue}.
     */
    public CoreMessageQueue() {
        
        // MessageWrapper's natural ordering establishes the priority:
        queue = new PriorityQueue<MessageWrapper>();
        
        queuedIsms = new HashSet<InterSiteMessage>();
        signal = new EventSignal();
        mutex = new MutexLock();
        nextSequenceNumber = Integer.MIN_VALUE;
    }

    /**
     * "Sends" a message to this queue; that is, enqueues it for later
     * processing by the appropriate core worker thread
     * 
     * @param msg the {@code CoreMessage} to send
     */
    public void send(CoreMessage msg) {
        mutex.acquire();
        try {
            rawSend(msg);
        } finally {
            mutex.release();
        }
    }

    /**
     * Atomically "Sends" zero or more messages to this queue; that is, enqueues
     * them all for later processing by the appropriate core worker thread,
     * ensuring that all are enqueued (or dropped) without intervening sends or
     * receives by other threads. It is also somewhat more efficient to use this
     * method than to invoke the {@link #send(CoreMessage)} method for each of
     * serveral messages because the necessary internal locking can be performed
     * once for the whole batch instead of once for each message.
     * 
     * @param messages a {@code Collection} containing the {@code CoreMessages}
     *        to send; they will be enqueued in the order they are returned by
     *        the collection's iterator (which might not correspond to their
     *        processing order, depending on this queue's prioritization rules)
     */
    public void sendSeveral(Collection<? extends CoreMessage> messages) {
        mutex.acquire();
        try {
            for (CoreMessage msg : messages) {
                rawSend(msg);
            }
        } finally {
            mutex.release();
        }
    }

    /**
     * Enqueues the specified message and signals the arrival of a new message
     * to any threads waiting for one.  For thread safety, the thread invoking
     * this method should hold this queue's mutex.
     * 
     * @param msg the {@code CoreMessage} to enqueue
     */
    private void rawSend(CoreMessage msg) {
        
        // This test is safe even if the message is not an ISM:
        if (!queuedIsms.contains(msg)) {
            queue.add(new MessageWrapper(msg, nextSequenceNumber++));
            if (msg instanceof InterSiteMessage) {
                queuedIsms.add((InterSiteMessage) msg);
            }
            signal.send();
        }
    }

    /**
     * "Receives" a {@code CoreMessage} message from this queue, if one is
     * available within the specified number of milliseconds.  Receiving a
     * message means dequeueing and returning the highest-priority message
     * currently in the queue.
     * 
     * @param milliseconds the maximum number of milliseconds to wait to receive
     *        a message from this queue, or 0 to wait indefinitely
     *         
     * @return the {@code CoreMessage} received, or {@code null} if no message
     *        was received within the specified time
     */
    public CoreMessage receive(int milliseconds) {
        mutex.acquire();
        try {
            if (!queue.isEmpty()) {
                CoreMessage rval = queue.remove().getMessage();
                
                // This is safe even if the message is not an ISM:
                queuedIsms.remove(rval);
                
                return rval;
            } else {
                
                /*
                 * If we detect an empty queue then we can safely reset the
                 * internal enqueueing sequence number.  It is desirable to do
                 * this when safe, so as to prevent the sequence number from
                 * wrapping.  This approach is not *certain* to reset it before
                 * it overflows, but it will do so long as fewer than
                 * 2^32 messages are enqueued before the queue
                 * empties long enough that the receiving thread notices.
                 * 
                 * (Note: there is about 4 billion messages' worth of head room
                 * here, and much fewer messages than that have ever been sent
                 * by the whole Reciprocal Net at the time of this writing.)
                 */
                nextSequenceNumber = Integer.MIN_VALUE;
            }
        } finally {
            mutex.release();
        }

        if (!signal.receive(milliseconds)) {
            return null;
        }

        mutex.acquire();
        try {
            if (queue.isEmpty()) {
                
                /*
                 * This should not happen.  If it does anyway, then there is  
                 * no need to reset the sequence number again.
                 */
                
                return null;
            } else {
                CoreMessage rval = queue.remove().getMessage();
                
                // This is safe even if the message is not an ISM:
                queuedIsms.remove(rval);
                
                return rval;
            }
        } finally {
            mutex.release();
        }
    }

    /**
     * For debugging use only: returns a {@code String} representation of this
     * queue showing the currently-enqueued messages in order
     * 
     * @return a {@code String} representation of this queue and its contents
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        mutex.acquire();
        try {
            MessageWrapper[] messages = new MessageWrapper[queue.size()];
            String separator = System.getProperty("line.separator");
            int index = 0;
            
            queue.toArray(messages);
            Arrays.sort(messages);
            
            sb.append("    Message queue contains ");
            sb.append(queue.size());
            sb.append(" items:");
            sb.append(separator);
            
            for (MessageWrapper wrapper : messages) {
                CoreMessage msg = wrapper.getMessage();
                
                sb.append("     ");
                sb.append(++index);
                sb.append(": ");
                sb.append(msg.getClass().getName());
                if (msg instanceof InterSiteMessage) {
                    sb.append(" ");
                    sb.append(((InterSiteMessage) msg).getSuggestedFileName());
                }
                sb.append(separator);
            }
        } finally {
            mutex.release();
        }

        return sb.toString();
    }

    /**
     * A class representing a {@code CoreMessage} plus the information necessary
     * to determine its processing priority. The processing priority determined
     * in this way establish a natural ordering for instances. Users should note
     * that the natural ordering established in this way <strong>is not
     * consistent with equals()</strong> because this class does not override
     * {@code Object.equals()} and therefore expresses object identity
     * semantics.
     * 
     * @author jobollin
     * @version 0.9.0
     */
    private static class MessageWrapper implements Comparable<MessageWrapper> {

        /**
         * The {@code CoreMessage} wrapped by this wrapper
         */
        private final CoreMessage message;
        
        /**
         * The message's priority class
         */
        private final MessageClass messageClass;
        
        /**
         * The relative enqueueing sequence number for the associated message
         */
        private final int sequenceNumber;
        
        /**
         * Initializes a {@code MessageWrapper} with the specified message and
         * sequence number.
         * 
         * @param message the {@code CoreMessage} wrapped by this wrapper;
         *        subsequently available via {@link #getMessage()}
         * @param number the sequence number of this message
         */
        public MessageWrapper(CoreMessage message, int number) {
            this.message = message;
            messageClass = MessageClass.forMessage(message);
            sequenceNumber = number;
        }
        
        /**
         * Returns the message wrapped by this wrapper
         * 
         * @return the {@code CoreMessage} wrapped by this wrapper
         */
        public CoreMessage getMessage() {
            return message;
        }

        /**
         * Compares this {@code MessageWrapper} to another to determine the
         * relative processing priority of their associated messages; this
         * establishes their natural order
         * 
         * @param other the {@code MessageWrapper} to compare with this one
         *  
         * @return an {@code int} less than, equal to, or greater than zero
         *         depending on whether this wrapper should come earlier, at
         *         the same place, or after the specified other wrapper in their
         *         natural order
         * 
         * @see java.lang.Comparable#compareTo(Object)
         */
        public int compareTo(MessageWrapper other) {
            if (this == other) {
                return 0;
            } else if (this.messageClass == other.messageClass) {
                return messageClass.compare(this.message,
                        this.sequenceNumber, other.message,
                        other.sequenceNumber);
            } else {
                return this.messageClass.compareTo(other.messageClass);
            }
        }

        /**
         * An enumeration of the classes of messages distinguinguished by this
         * queue. Order of declaration determines the relative priority of
         * messages of these classes, and the
         * {@link #compare(CoreMessage, int, CoreMessage, int) compare()} method
         * establishes priorities within classes. The base compare()
         * implementation uses only the messages' enqueueing sequence numbers,
         * but some enum members override this behavior and consider also the
         * details of the messages.
         */
        private enum MessageClass {
            
            /**
             * The priority (highest) of ProcessedIsmCM messages
             */
            PROCESSED_ISM,
            
            /**
             * The priority (high) of most types of messages 
             */
            DEFAULT,
            
            /**
             * The priority (low) of InterSiteMessage messages
             */
            INTER_SITE{
                
                /**
                 * {@inheritDoc}.  This version relies on the natural ordering
                 * of the messages.
                 */
                @Override
                public int compare(CoreMessage msg1,
                        @SuppressWarnings("unused") int sequence1,
                        CoreMessage msg2,
                        @SuppressWarnings("unused") int sequence2) {
                    return((InterSiteMessage) msg1).compareTo(
                            (InterSiteMessage) msg2);
                }
            },
            
            /**
             * The priority (lowest) of SendIsmCM messages
             */
            SEND_ISM {
                
                /**
                 * {@inheritDoc}.  This version considers first the natural
                 * ordering of the messages, and falls back to the superclass's
                 * ordering if necessary.
                 */
                @Override
                public int compare(CoreMessage msg1, int sequence1,
                        CoreMessage msg2, int sequence2) {
                    int rval = ((SendIsmCM) msg1).compareTo((SendIsmCM) msg2);
                    
                    return ((rval != 0)
                            ? rval
                            : super.compare(msg1, sequence1, msg2, sequence2));
                }
            };
            
            /**
             * Compares two {@code CoreMessage}s and their relative enqueueing
             * sequence numbers to determine the messages' relative priority
             * within this message class.  The results are meaningful only if
             * both of the specified messages are of this message class, as
             * established by {@link #forMessage(CoreMessage)}; if this is not
             * the case then this method may (but is not guaranteed to) throw
             * an exception
             * 
             * @param msg1 the first {@code CoreMessage} to compare; should not
             *        be null
             * @param sequence1 the relative enqueueing sequence number of the
             *        first message
             * @param msg2 the second {@code CoreMessage} to compare; should not
             *        be null
             * @param sequence2 the relative enqueueing sequence number of the
             *        second message
             *        
             * @return an {@code int} less than, equal to, or greater than 0 as
             *         the first message has higher, equal, or lower priority
             *         for processing than the second
             */
            public int compare(@SuppressWarnings("unused") CoreMessage msg1,
                    int sequence1, @SuppressWarnings("unused") CoreMessage msg2,
                    int sequence2) {
                if (sequence1 < sequence2) {
                    return -1;
                } else if (sequence1 > sequence2) {
                    return 1;
                } else {
                    return 0;
                }
            }
            
            /**
             * Determines the {@code MessageClass} that pertains to the
             * specified message.
             * 
             * @param message the {@code CoreMessage} for which a message
             *        priority class is requested
             *        
             * @return the {@code MessageClass} representing the specified
             *         message's priority class
             */
            public static MessageClass forMessage(CoreMessage message) {
                
                /*
                 * ProcessedIsmCMs always have the highest priority,
                 * because their timely processing is crucial to database
                 * integrity.
                 */
                if (message instanceof ProcessedIsmCM) {
                    return PROCESSED_ISM;
        
                /*
                 * SendIsmCMs always have the lowest priority because they
                 * are a very costly operation, and if postponed long
                 * enough frequently can be batched together in groups.
                 */
                } else if (message instanceof SendIsmCM) {
                    return SEND_ISM;
        
                /*
                 * ISMs have lower priority than other, non-ISM core
                 * messages.
                 */
                } else if (message instanceof InterSiteMessage) {
                    return INTER_SITE;
                
                /*
                 * Other CMs get a default priority
                 */
                } else {
                    return DEFAULT;
                }
            }
        }
    }
}
