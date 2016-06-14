/*
 * Reciprocal Net project
 * 
 * EventSignal.java
 *
 * 07-Jun-2002: ekoperda wrote first draft
 * 27-Jun-2002: ekoperda added isSet() function
 * 26-Sep-2002: ekoperda moved class to the core.util package; used to be in
 *              the core package
 * 10-May-2006: jobollin reformatted the source and added and updated many docs
 */

package org.recipnet.site.core.util;

/**
 * This is a simple utility class that allows one thread to block until another
 * thread signals it. This is similar to an event object from Win32.
 */
public class EventSignal {
    private boolean status;

    private final boolean autoReset;

    /**
     * Initializes an {@code EventSignal} that is configured to reset itself
     * automatically when received and which is initially not set
     */
    public EventSignal() {
        this(false, true);
    }

    /**
     * Initializes an {@code EventSignal} that is configured to reset itself
     * automatically when received and that has the specified initial status
     * 
     * @param initialStatus {@code true} if this signal should initially be set,
     *        otherwise {@code false}
     */
    public EventSignal(boolean initialStatus) {
        this(initialStatus, true);
    }

    /**
     * Initializes an {@code EventSignal} with the specified parameters
     * 
     * @param initialStatus {@code true} if this signal should initially be set,
     *        otherwise {@code false}
     * @param autoReset {@code true} if this signal should automatically reset
     *        itself when received, otherwise {@code false}
     */
    public EventSignal(boolean initialStatus, boolean autoReset) {
        this.status = initialStatus;
        this.autoReset = autoReset;
    }

    /**
     * Wait indefinately for another thread to signal this object. Returns true
     * if the signal was actually received.
     * 
     * @return {@code true} if this signal was received, or {@code false} if
     *         this thread attempting to receive it was interrupted before doing
     *         so
     */
    public synchronized boolean receive() {
        return receive(0);
    }

    /**
     * Wait up to the specified number of milliseconds for another thread to
     * signal this object.
     * 
     * @param  milliseconds the maximum number of milliseconds to wait to
     *         receive this signal, or 0 to wait indefinitely
     * @return {@code true} if the signal was actually received; {@code false}
     *         if the specified time elapses without receiving the signal, or if
     *         this thread attempting to receive it was interrupted before doing
     *         so
     */
    public synchronized boolean receive(int milliseconds) {
        long startingTime = System.currentTimeMillis();
        long currentTime = System.currentTimeMillis();

        while ((status == false)
                && (((currentTime - startingTime) < milliseconds)
                        || (milliseconds == 0))) {
            try {
                if (milliseconds == 0) {
                    wait(0);
                } else {
                    wait(milliseconds - (currentTime - startingTime));
                }
            } catch (InterruptedException e) {
                return false;
            }
            currentTime = System.currentTimeMillis();
        }
        if (status == true) {
            if (autoReset == true) {
                status = false;
            }
            return true;
        }
        return false;
    }

    /**
     * Set this object to the signalled state. If auto-reset has been enabled,
     * only a singal thread will receive the signal. (Exactly which thread
     * receives the signal is not specified and is dependent upon the Java VM
     * and underlying operating system.) If auto-reset is disabled, all threads
     * attempting to receive this signal will receive it, until this object has
     * been reset manually.
     */
    public synchronized void send() {
        status = true;
        notifyAll();
    }

    /**
     * Determines (without waiting) whether this signal is currently set
     * 
     * @return {@code true} if the event signal is currently set, {@code false}
     *         if not.
     */
    public synchronized boolean isSet() {
        return status;
    }

    /**
     * Set this object to the non-signalled state. This is useful only if
     * auto-reset is disabled.
     */
    public synchronized void reset() {
        status = false;
    }
}
