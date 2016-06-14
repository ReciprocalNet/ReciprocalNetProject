/*
 * Reciprocal Net Project
 *
 * ProcessWrapper
 *
 * 30-Oct-2002: jobollin wrote first draft
 * 13-Feb-2003: ekoperda fixed bug #722 in waitFor()
 * 12-Aug-2003: midurbin added timeouts to terminate stalled Process'es
 * 31-Dec-2003: ekoperda moved class from the org.recipnet.site.misc package 
 *              to org.recipnet.common; removed dependency on 
 *              OperationFailedException
 * 26-May-2006: jobollin reformatted the source, removed unused imports, rewrote
 *              the broken and overcomplicated WatchdogTimerThread nested class
 */

package org.recipnet.common;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * <p>
 * A class that wraps a {@code Process} object and optionally provides for
 * draining the wrapped Process' input and/or error stream.
 * </p><p>
 * A Process will not terminate normally as long as there is unread data in its
 * InputStream or ErrorStream. This Process subclass delegates the methods of
 * the Process class to an internal Process object, and provides for draining
 * off that Process' input stream and/or error stream data to either an
 * OutputStream or a bit bucket. This class can also handle closing the Process'
 * output stream if so desired.
 * </p><p>
 * This class can be used to set up complex I/O redirection without reliance on
 * any underlying OS I/O redirection support details.
 * </p><p>
 * This class provides optional safety features to protect against subprocesses
 * that take an stall or take an unanticipated amount of time/resources to
 * complete. These features include a {@code ProcessWaiterThread} that calls
 * {@code Process.waitFor()} and may be aborted after a arbitrary interval. A
 * caller can also invoke a watchdog timer that will terminate any running
 * threads and call {@code Process.destroy()} on the subprocess. This will cause
 * threads that have been blocking on one of the subprocess' streams to throw an
 * IOException. (usually with the message "Broken pipe")
 * </p><p>
 * It is not recommended to spawn processes that use the underlying OS's
 * output redirection; instead use multiple {@code ProcessWrapper} objects
 * and set one's {@code InputStream} (provided in the constructor) to the
 * {@code OutputStream} (procured using {@code ProcessWrapper.getOutputStream()})
 * of another.
 * </p><p>
 * Although instances of this class creates and uses its own threads in a
 * thread-safe manner, this class is not thread-safe with respect to multiple
 * external threads invoking methods on the same instance concurrently.
 * </p>
 */
public class ProcessWrapper extends Process {
    
    private final Process proc;

    private final boolean drainInput;

    private final DrainerThread inputDrainerThread;

    private final boolean drainError;

    private final DrainerThread errorDrainerThread;

    private final AtomicBoolean processWasAborted;

    private final Object watchdogLock = new Object();
    
    private WatchdogTimerThread watch;
    
    private boolean maySetWatchdog;

    /**
     * Initializes a new {@code ProcessWrapper}
     * 
     * @param p the {@code Process} wrapped by this {@code ProcessWrapper}
     * @param closeOut {@code true} if the {@code Process}'s output stream
     *        should be closed
     * @param drainIn {@code true} if the {@code Process}'s input stream should
     *        be drained
     * @param inDrain the OutputStream to which the {@code Process}'s input
     *        stream should be drained; if {@code null} then the drained data is
     *        discarded
     * @param drainErr {@code true} if the {@code Process}'s error stream
     *        should be drained
     * @param errDrain the OutputStream to which the {@code Process}'s error
     *        stream should be drained; if {@code null} then the drained data is
     *        discarded
     */
    public ProcessWrapper(Process p, boolean closeOut, boolean drainIn,
            OutputStream inDrain, boolean drainErr, OutputStream errDrain) {
        drainInput = drainIn;
        drainError = drainErr;
        proc = p;
        processWasAborted = new AtomicBoolean(false);
        if (closeOut) {
            try {
                proc.getOutputStream().close();
            } catch (IOException ioe) {
                // attempt to continue
            }
        }
        if (drainInput) {
            inputDrainerThread = new DrainerThread(proc.getInputStream(),
                    inDrain);
            inputDrainerThread.start();
        } else {
            inputDrainerThread = null;
        }
        if (drainError) {
            errorDrainerThread = new DrainerThread(proc.getErrorStream(),
                    errDrain);
            errorDrainerThread.start();
        } else {
            errorDrainerThread = null;
        }
        
        synchronized (watchdogLock) {
            this.maySetWatchdog = true;
            this.watch = null;
        }
    }

    /**
     * Indicates whether this {@code ProcessWrapper} is draining off its input
     * stream
     * 
     * @return {@code true} if the input stream is being drained
     */
    public boolean isDrainingInput() {
        return drainInput;
    }

    /**
     * Indicates whether this {@code ProcessWrapper} is draining off its error
     * stream
     * 
     * @return {@code true} if the error stream is being drained
     */
    public boolean isDrainingError() {
        return drainError;
    }

    /**
     * forcibly terminates the {@code Process} wrapped by this
     * {@code ProcessWrapper}
     */
    @Override
    public void destroy() {
        proc.destroy();
    }

    /**
     * obtains the exit value of the {@code Process} wrapped by this
     * {@code ProcessWrapper}, provided that the Process has terminated
     * 
     * @return the exit code
     * @throws IllegalThreadStateException if the Process has not yet terminated
     */
    @Override
    public int exitValue() {
        return proc.exitValue();
    }

    /**
     * obtains the wrapped {@code Process}'s error stream, provided that it is
     * not being drained by this {@code ProcessWrapper}
     * 
     * @return an InputStream from which data written to the subprocess'
     *         standard error can be read
     * @throws IllegalStateException if the error stream is being drained
     */
    @Override
    public InputStream getErrorStream() {
        if (drainError) {
            throw new IllegalStateException("The error stream is being drained");
        } else {
            return proc.getErrorStream();
        }
    }

    /**
     * obtains the wrapped {@code Process}'s input stream, provided that it is
     * not being drained by this {@code ProcessWrapper}
     * 
     * @return an InputStream from which data written to the subprocess'
     *         standard output can be read
     * @throws IllegalStateException if the error stream is being drained
     */
    @Override
    public InputStream getInputStream() {
        if (drainInput) {
            throw new IllegalStateException("The input stream is being drained");
        } else {
            return proc.getInputStream();
        }
    }

    /**
     * obtains the wrapped {@code Process}'s output stream, which, depending on
     * the constructor arguments, may have been closed during initialization of
     * this {@code ProcessWrapper}
     * 
     * @return an {@code OutputStream} via which (if open) data may be written
     *         to the subprocess's standard input
     */
    @Override
    public OutputStream getOutputStream() {
        return proc.getOutputStream();
    }

    /**
     * suspends the current thread until the subprocess finishes. In order to
     * prevent this function from blocking indefinitely, callers should ensure
     * that the process's output and error streams will continue to be serviced
     * in some fashion (perhaps by specifying at creation time that these
     * streams are to be auto-drained).  If auto-draining has been enabled then
     * this method will not return until all streams have been completely
     * drained.  If a watchdog timer has been enabled (in a prior call to
     * {@link #setWatchdogTimer(long)}) and the watchdog timer expires while the
     * caller's thread has blocked on this method, this method will usually
     * return an error code. If {@link #abort()} has been invoked (either by the
     * user or by an expired watchdog timer) then this function will throw an
     * {@code InterruptedException}. To determine the cause of this exception
     * the caller should invoke {@link #wasAborted()}.
     * 
     * @return the exit value of the Process
     */
    @Override
    public int waitFor() throws InterruptedException {
        if (processWasAborted.get()) {
            throw new InterruptedException();
        } else {

            // Wait for the subprocess to terminate.
            int rc = proc.waitFor();

            /*
             * If we're auto-draining the process's input or error streams then
             * give the drainers time to finish draining.
             */
            
            if (drainInput) {
                inputDrainerThread.waitForTermination();
            }
            if (drainError) {
                errorDrainerThread.waitForTermination();
            }

            return rc;
        }
    }

    /**
     * Returns the return code of the process if it completes in the number of
     * milliseconds indicated. Identical to {@link #waitFor()} except that the
     * subprocess will be aborted (as if the caller invoked {@link #abort()})
     * after the specified number of miliseconds unless the process terminates
     * gracefully prior to that time. If a watchdog timer has been set, it is
     * cancelled.
     * 
     * @param timeout the number of milliseconds of processing time tolerated
     *        per thread. In the current implementation this timeout is enforced
     *        on a thread running the process, a thread draining the input
     *        stream and on a thread draining the error stream.
     * @return the return code for the underlying process
     * @throws InterruptedException if the Process did not complete before
     *         {@code timeout} milliseconds had elapsed.
     */
    public int waitFor(long timeout) throws InterruptedException {
        
        // Cancel the watchdog timer if one is active
        synchronized (watchdogLock) {
            if (this.watch != null) {
                this.watch.cancel();
                this.watch = null;
                this.maySetWatchdog = false;
            }
        }
        
        ProcessWaiterThread waiterThread = new ProcessWaiterThread(this.proc);
        
        if (waiterThread.waitForTermination(timeout)) {
            
            /*
             * If we're auto-draining the process's input or error streams, give the
             * drainers time to finish draining.
             */
            if (drainInput) {
                inputDrainerThread.waitForTermination();
            }
            if (drainError) {
                errorDrainerThread.waitForTermination();
            }
            
            return waiterThread.getReturnCode();
        } else {
            waiterThread.abort();
            if (drainInput) {
                inputDrainerThread.abort();
            }
            if (drainError) {
                errorDrainerThread.abort();
            }
            processWasAborted.set(true);
            
            throw new InterruptedException();
        }
    }

    /**
     * This function returns true if the underlying {@code Process} was aborted.
     * {@code ProcessWrapper} can be aborted by exired watchdog timers (set by
     * the user), by {@code waitFor()} not completing in the specified time
     * limit or by a call to {@code abort()}.
     */
    public boolean wasAborted() {
        return processWasAborted.get();
    }

    /**
     * Destroys the process and terminates any running threads, provided that
     * this process was not already aborted. If invoked while a thread is
     * blocked writing to the Process' {@code OutputStream} then an IOException
     * may be thrown by that write.
     */
    public void abort() throws InterruptedException {
        if (!this.processWasAborted.getAndSet(true)) {
            proc.destroy();
            if (inputDrainerThread != null) {
                inputDrainerThread.abort();
            }
            if (errorDrainerThread != null) {
                errorDrainerThread.abort();
            }
        }
    }

    /**
     * Starts a watchdog timer that will abort the {@code Process} after the
     * indicated number of milliseconds, unless {@code cancelWatchdogTimer()} or
     * {@code resetWatchdogTimer()} are invoked in the meantime.
     * 
     * @param timeout indicates the number of milliseconds to wait before
     *        aborting the subprocess
     * @throws IllegalArgumentException if {@code timeout} is nonpositive
     */
    public void setWatchdogTimer(long timeout) {
        synchronized (watchdogLock) {
            if (this.watch == null) {
                if (maySetWatchdog) {
                    this.watch = new WatchdogTimerThread(timeout);
                    this.watch.start();
                } else {
                    throw new IllegalStateException(
                            "Setting a watchdog timer is currently forbidden");
                }
            } else {
                this.watch.reset(timeout);
            }
        }
    }

    /**
     * Stops the currently running timer, preventing the {@code abort()} from
     * being called on the subprocess.
     * 
     * @throws IllegalStateException if the process was already aborted or the
     *         timer was never started
     */
    public void cancelWatchdogTimer() throws IllegalStateException {
        synchronized (watchdogLock) {
            if (watch == null) {
                throw new IllegalStateException("No active watchdog timer");
            } else {
                this.watch.cancel();
                this.watch = null;
            }
        }
    }

    /**
     * Resets the currently running timer. This is equivalent to calling
     * {@code setWatchdogTimer()} with the same timeout as the last call to it.
     * 
     * @throws IllegalStateException if {@code setWatchdogTimer()} has never
     *         been called
     */
    public void resetWatchdogTimer() throws IllegalStateException {
        synchronized (watchdogLock) {
            if (watch == null) {
                throw new IllegalStateException("No active watchdog timer");
            } else {
                this.watch.reset();
            }
        }
    }

    /**
     * An extention on the Thread class that allows a {@code Proccess}'s
     * {@code waitFor()} method to be interrupted. This class does not handle
     * the {@code Process} configured on it in a thread-safe manner; the user is
     * expected to relinquish control of the {@code Process} object.
     */
    private static class ProcessWaiterThread extends Thread {
        
        /** the Process on which this Thread waits */
        private final Process proc;

        /** the value returned by the process */
        private int returnCode;

        /** indicates whether the process terminated gracefully */
        private final AtomicBoolean terminatedGracefully;

        /**
         * internal variable set by abort() that signals the loop in run() to
         * destroy the process and stop running.
         */
        private final AtomicBoolean destroyProcess;

        /**
         * Initializes a new {@code ProcessWaiterThread} to wait for the
         * execution of the specified external {@code Process} to complete.
         * 
         * @param  process the {@code Process} to wait for
         */
        public ProcessWaiterThread(Process process) {
            this.proc = process;
            this.returnCode = -1;
            this.terminatedGracefully = new AtomicBoolean(false);
            this.destroyProcess = new AtomicBoolean(false);
        }

        /**
         * Waits for the Process until it completes naturally, is aborted or
         * runs out of time.
         */
        @Override
        public void run() {
            while (!terminatedGracefully.get()) {
                if (destroyProcess.get()) {
                    /*
                     * Here we assume that Process.destroy() actually destroys
                     * the external process synchronously. Such behavior is not
                     * guaranteed by the Java 1.4.2 API, however. If our
                     * assumption is incorrect then we may have a thread safety
                     * issue here because the ProcessWaiterThread might
                     * terminate while the external process still exists.
                     */
                    proc.destroy();
                    try {
                        proc.waitFor();
                    } catch (InterruptedException ie) {
                        // allow the process to terminate asynchronously
                    }
                    
                    break;
                } else {
                    try {
                        this.returnCode = proc.waitFor();
                        terminatedGracefully.set(true);
                    } catch (InterruptedException ie) {
                        // do nothing in particular
                    }
                }
            }
        }

        /**
         * Waits {@code timeout} milliseconds for the process to terminate. For
         * convenience if the process has not been started it is started by this
         * function.
         */
        public boolean waitForTermination(long timeout)
                throws InterruptedException {
            if (getState() == Thread.State.NEW) {
                start();
            }
            join(timeout);
            
            return terminatedGracefully.get();
        }

        /**
         * Aborts the external process and blocks until this Thread terminates
         */
        public void abort() throws InterruptedException {
            destroyProcess.set(true);
            interrupt();
            join();
        }

        /**
         * If {@link #waitForTermination(long)} returned {@code true} then this
         * method returns the exit code for the {@code Process}; otherwise it
         * throws an {@code IllegalStateException}.
         * 
         * @return the exit code of the {@code Process}, as obtained from
         *         {@code Process.waitFor()}
         * @throws IllegalStateException if the process did not exit normally
         *         prior to this method running; if the process is still running
         *         then at some time in the future this method might return
         *         normally instead of throwing this exception 
         */
        public int getReturnCode() {
            if (terminatedGracefully.get()) {
                return this.returnCode;
            } else {
                throw new IllegalStateException();
            }
        }
    }

    /**
     * A Thread that asynchronously drains an {@code InputStream}, optionally
     * copying the data to an {@code OutputStream}. It closes the
     * {@code InputStream} when an EOF is detected. This class is intended for
     * use with the error and/or input streams of subprocesses spawned by
     * {@code Runtime.exec()}, but is not inherently restricted to such use.
     */
    private static class DrainerThread extends Thread {

        /**
         * An {@code OutputStream} supplied during initialization, to which the
         * data from {@code the InputStream} should be redirected, or
         * {@code null} to indicate that input should be dropped.
         */
        private final OutputStream output;

        /** the {@code InputStream} to be drained */
        private final InputStream input;

        /** indicates to run() that abort() has been called */
        private final AtomicBoolean aborted;

        /** indicates that an IOException was encountered by run() */
        private final AtomicBoolean fatalExceptionEncountered;
        
        /**
         * Constructs a {@code DrainerThread} for the given {@code InputStream}.
         * 
         * @param is the InputStream to drain
         * @param os an OutputStream to which the data drained from {@code is}
         *        should be written. The data is discarded if {@code os} is
         *        {@code null}; otherwise {@code os }is wrapped in a
         *        BufferedOutputStream before use.
         */
        public DrainerThread(InputStream is, OutputStream os) {
            if (is == null) {
                throw new IllegalArgumentException(
                        "The InputStream must not be null");
            }
            this.input = is;
            this.output = ((os == null) ? null : new BufferedOutputStream(os));
            this.aborted = new AtomicBoolean(false);
            this.fatalExceptionEncountered = new AtomicBoolean(false);
        }

        /**
         * Drains all available input until EOF is detected, an exception is
         * thrown, or abort() is called.
         */
        @Override
        public void run() {
            byte[] buffer = new byte[4096];
            int countBytesRead = 0;
            
            do {
                try {
                    countBytesRead = this.input.read(buffer);
                    if ((this.output != null) && (countBytesRead > 0)) {
                        this.output.write(buffer, 0, countBytesRead);
                    }
                } catch (IOException ioe) {
                    fatalExceptionEncountered.set(true);
                    break;
                }
                Thread.yield();
            } while ((countBytesRead >= 0) && !aborted.get());

            // clean up
            try {
                this.input.close();
            } catch (IOException ioe) {
                this.fatalExceptionEncountered.set(true);
            }
            try {
                if (this.output != null) {
                    this.output.flush();
                    this.output.close();
                }
            } catch (IOException ioe) {
                this.fatalExceptionEncountered.set(true);
            }
        }

        /**
         * Interrupts the running thread, forcing it to close any open streams,
         * and waits for it to terminate
         */
        public void abort() throws InterruptedException {
            aborted.set(true);
            
            /*
             * FIXME: will this actually work if we're blocked on I/O?  Is
             * there anything that would?
             */
            interrupt();
            
            join();
        }

        /**
         * Waits indefinitely for the thread to die. Note that the thread cannot
         * die unless it is {@code start()}ed, though that may be done either
         * before or after this method is invoked.
         * 
         * @return {@code true} if the process finished normally (possibly
         *         emitting a result code indicative of a process-specific
         *         error), or {@code false} if the process was forcibly aborted
         *         or died because of an exception
         */
        public boolean waitForTermination() throws InterruptedException {
            // waits forever
            return waitForTermination(0);
        }

        /**
         * Waits {@code timeout} milliseconds for the thread to die. Note that
         * the thread cannot die within the specified period unless it is
         * {@code start()}ed before or during the period.
         * 
         * @param timeout the number of milliseconds to wait for termination
         *        before aborting the process, or zero to wait indefinitely
         * @return {@code true} if the wait finished without the thread being
         *         aborted or dying because of a fatal exception; the end of the
         *         thread being drained may or may not have been reached
         */
        public boolean waitForTermination(long timeout)
                throws InterruptedException {
            join(timeout);
            
            return (!(aborted.get() || fatalExceptionEncountered.get()));
        }
    }

    /**
     * This Thread implements a "watchdog timer" function. That is, it can be
     * set to call {@code ProcessWrapper.abort()} after a specified delay
     * unless cancelled or reset in the interim. This is useful in terminating
     * unintentionally long-running external processes. This class is
     * thread-safe.
     */
    private class WatchdogTimerThread extends Thread {

        /**
         * The timeout interval.  Initially set in the constructor, and
         * updated by {@link #reset(long)}; used by {@link #reset()}, and
         * indirectly by {@code reset(long)}.
         */
        private long interval;

        /**
         * An {@code Object} whose monitor is used to synchronize access to and
         * modification of the {@code interval}
         */
        private final Object intervalLock = new Object();
        
        /**
         * the time, expressed in miliseconds from epoch, when this process
         * should be forcibly terminated.
         */
        private final AtomicLong expirationDate;

        /**
         * A flag that is raised to indicate that this thread should terminate
         * without aborting the process it is watching
         */
        private final AtomicBoolean shouldCancel;

        /**
         * Initializes a new {@code WatchdogTimerThread} to watch the specified
         * process with the specified initial timeout interval.
         * 
         * @param timeout the initial timeout interval in milliseconds; if this
         *        thread runs for the specified interval without being reset
         *        then it will abort the watched process
         */
        public WatchdogTimerThread(long timeout) {
            if (timeout <= 0) {
                throw new IllegalArgumentException("Non-positive timeout");
            }
            synchronized (intervalLock) {
                this.interval = timeout;
            }
            expirationDate = new AtomicLong(Long.MIN_VALUE);
            shouldCancel = new AtomicBoolean(false);
        }

        /**
         * Starts this thread
         */
        @Override
        public void start() {
                
            /*
             * Set the timer expiration timestamp BEFORE super.start() is
             * invoked:
             */
            reset();
            
            super.start();
        }
        
        /**
         * <p>
         * Performs the main work of this thread; in particular, waits for the
         * expiration date to arrive or for the thread to be cancelled,
         * whichever comes first. If the timeout interval elapses without this
         * watchdog being cancelled then this thread will both terminate and
         * abort the process it is watching; otherwise it will simply terminate.
         * </p><p>
         * Extensions to the expiration date (such as performed by
         * {@link #reset()} and in some cases by {@link #reset(long)}) will be
         * recognized appropriately by this method, provided that they are
         * performed before the configured timeout interval elapses.
         * Contractions of the expiration date might not be recognized in a
         * timely manner; interrupting this thread after updating the expiration
         * date is the most likely way to make it notice the earlier expiration
         * date.  The {@code reset(long)} method does this.
         * </p><p>
         * Clients should not invoke this method.
         * </p>
         */
        @Override
        public void run() {
            for (long time = expirationDate.get() - System.currentTimeMillis();
                    (time > 0) && !shouldCancel.get();
                    time = expirationDate.get() - System.currentTimeMillis()) {
                try {
                    Thread.sleep(time);
                } catch (InterruptedException ie) {
                    // do nothing in particular
                }
            }
            
            /*
             * If control gets here without this watchdog having been cancelled,
             * then abort the containing ProcessWrapper. The shouldCancel flag
             * is raised to indicate that the cancel() method does not need to
             * (and shouldn't) take any further action if it is subsequently
             * invoked. If this watchdog is cancelled after its time expires but
             * before it performs the following test then it will honor the
             * cancellation request rather than aborting the containing process.
             */
            if (!shouldCancel.getAndSet(true)) {
                try {
                    abort();
                } catch (InterruptedException ie) {
                    // Do nothing
                }
            }
        }

        /**
         * Sets / resets the watchdog timer thread to {@code abort()} the
         * containing {@code ProcessWrapper} at the indicated number of
         * milliseconds after the current system time.  Subsequent invocations
         * of the other {@link #reset()} method will extend the timeout by the
         * number of milliseconds specified to this method.
         * 
         * @param timeout the number of milliseconds to wait before
         *        aborting the subprocess; must be positive
         * @throws IllegalArgumentException if {@code timeout} is nonpositive
         */
        public void reset(long timeout) throws IllegalArgumentException {
            if (timeout <= 0) {
                throw new IllegalArgumentException("Non-positive timeout");
            } else if (!isAlive()) {
                throw new IllegalStateException("watchdog thread not running");
            } else {
                synchronized (intervalLock) {
                    interval = timeout;
                    reset();
                    interrupt();
                }
            }
        }

        /**
         * Resets the expiration timer of this thread.  The new expiration
         * deadline will occur at the configured number of milliseconds after
         * the current system time. 
         */
        public void reset() throws IllegalStateException {
            synchronized (intervalLock) {
                expirationDate.set(System.currentTimeMillis() + interval);
            }
        }

        /**
         * Causes this timer to terminate without aborting the containing
         * {@code ProcessWrapper} (provided that it has not already been
         * aborted).  If it has not yet been {@link #start()}ed when this method
         * is invoked, then this {@code Thread} will terminate normally very
         * soon after it is subsequently started (if ever it is) without
         * aborting the containing {@code ProcessWrapper}.  
         */
        public void cancel() {
            
            /*
             * Ensure that the cancellation flag is raised, and interrupt this
             * Thread if it wasn't already raised
             */
            if (!shouldCancel.getAndSet(true)) {
                interrupt();
            }
        }
    }
}
