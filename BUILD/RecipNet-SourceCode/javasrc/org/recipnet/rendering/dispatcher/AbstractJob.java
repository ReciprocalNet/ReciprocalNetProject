/*
 * Reciprocal Net project
 * rendering software
 * 
 * AbstractJob.java
 *
 * 10-Feb-2003: ekoperda wrote first draft
 * 07-Jan-2004: ekoperda changed package references to match source tree
 *              reorganization
 * 06-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.rendering.dispatcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Set;
import org.recipnet.common.PerfTimer;

/**
 * <p>
 * Abstract base class that is a wrapper for a distributed computation process.
 * Job objects may be submitted and scheduled for execution by calling
 * {@code AbstractJobDispatcher.doJob()} -- most variables and methods in this
 * class are designed to be invoked by a job dispatcher rather than by an
 * external caller.
 * </p>
 * <p>
 * Computational jobs embodied by this class should exist as a separate
 * executable program that can be invoked from the command line on the local
 * computer. Generally such programs are designed for distributed execution, so
 * invoking the program on the local computer (the head node) somehow spawns
 * corresponding slave processes on a specified set of slave nodes. The program
 * on the local computer then terminates only once all its slave processes have
 * finished their computations. This distributed spawning is entirely
 * application-specific and is not controlled or monitored by the job
 * dispatching system, except that: the job dispatcher informs the job object
 * which nodes are available to execute the job (possibly a smaller set than all
 * available nodes in the cluster), and the relative speeds of each, and the job
 * object communicates this knowledge to the executable program in some fashion.
 * </p>
 * <p>
 * Interaction between caller, dispatcher object, and job object proceeds so:
 * <ol>
 * <li>Caller creates a new job object, a subclass of {@code AbstractJob} that
 * contains useful logic.</li>
 * <li>(<i>Optional.</i>) Caller may invoke
 * {@code AbstractJobDispatcher.assignJobId()} to assign a unique id to the job.
 * Dispatcher populates the {@code id} field of this job object.</li>
 * <li>Caller initializes the job object's subclass-specific arguments to
 * describe the computations that are to be performed.</li>
 * <li>Caller passes the job object to {@code AbstractJobDispatcher.doJob()}
 * and that method blocks. Control has been passed to the dispatcher.</li>
 * <li>(<i>Optional.</i>) Dispatcher may call {@code getPriority()} and
 * {@code getCost()}, potentially many times, to decide how the job should be
 * scheduled. The values returned by these functions must not change over time.</li>
 * <li>Dispatcher calls {@code setBaseDirectory()} to indicate where the job's
 * temporary files may be stored. Dispatcher sets the {@code nodesAssignedTo}
 * and {@code perfTimer} fields also; these values and their contents must not
 * be modified by the job.</li>
 * <li>Dispatcher calls {@code writeInputFiles()} and the job object writes the
 * temporary files that will be needed by the executable program to the
 * temporary file area.</li>
 * <li>Dispatcher calls {@code getInvocationInfo()} and the job object returns
 * the command line statement appropriate for invoking the executable program.</li>
 * <li>Dispatcher invokes the temporary program and waits for it to terminate.
 * Dispatcher sets the {@code errorCode} and possibly the {@code errorMessage}.
 * By convention, a nonzero error code from the program signals an error of some
 * sort.</li>
 * <li>Dispatcher calls {@code readOutputFiles()} and the job object reads the
 * temporary files that were created by the executable program in the temporary
 * file area.</li>
 * <li>(<i>Optional.</i>) Dispatcher may call {@code deleteFiles()} and the
 * job object deletes all input, output, and other files that may have been
 * created in the temporary file area.</li>
 * <li>Dispatcher returns control to the caller.</li>
 * </ol>
 * </p>
 */

public abstract class AbstractJob implements Cloneable, Comparable<AbstractJob> {
    /**
     * Set by {@code AbstractJobDispatcher.assignJobId()} or
     * {@code AbstractJobDispatcher.doJob()}. This value uniquely identifies
     * this job object within the lifetime of its dispatcher.
     */
    long id = 0;

    /**
     * Used by {@code AbstractJobDispatcher.doJob()} as it executes and
     * available for inspection afterwards; records the time for various steps
     * in the job execution process to complete.
     */
    PerfTimer perfTimer = null;

    /**
     * Set upon return of {@code AbstractJobDispatcher.doJob()}. This is the
     * error code returned by the external program; by convention a nonzero
     * value indicates an error of some sort.
     */
    int errorCode = 0;

    /**
     * May be set upon return of {@code AbstractJobDispatcher.doJob()};
     * contains text written by the external program to stderr. May be null if
     * no such text was written.
     */
    String errorMessage = null;

    /**
     * Used by {@code AbstractJobDispatcher.doJob()} as it executes and
     * available for inspection afterwards; records the nodes on which this
     * compute job was executed. This {@code Set} contains zero or more
     * {@code ComputeNode} objects. The set and its member objects must not be
     * modified.
     */
    Set<ComputeNode> nodesAssignedTo = null;

    /**
     * Internal value set by {@code setBaseDirectory}, as provided by the
     * dispatcher. This is the temporary file area reserved for the exclusive
     * use of this job.
     */
    protected File baseDirectory = null;

    /**
     * Set by the constructor at creation time and examined by some job
     * dispatchers. Must not be modified. Relatively higher values in this field
     * generally indicate to a dispatcher that this job is a higher priority and
     * should be scheduled sooner.
     */
    int priority;

    /**
     * Set by the constructor at creation time and examined by some job
     * dispatchers. Must not be modified. This value should grow roughly
     * linearly with estimated processing time required to complete this job, if
     * it executed on a single node.
     */
    long cost;

    /**
     * The one and only constructor.
     * 
     * @param priority a value 0 or higher that will be examined by the job
     *        dispatcher (depending upon its implementation). Relatively higher
     *        values in this field indicate that this job should be scheduled
     *        sooner.
     * @param cost a value 0 or higher than will be examined by the job
     *        dispatcher (depending upon its implementation). The value should
     *        approximate the actual processing time this job will require to
     *        finish, if it executed on a single node, times some arbitrary
     *        positive scaling factor defined by the caller.
     */
    AbstractJob(int priority, long cost) {
        if (priority < 0) {
            throw new IllegalArgumentException();
        }
        this.priority = priority;
        System.out.println(cost);
        if (cost < 0) {
            throw new IllegalArgumentException();
        }
        this.cost = cost;
    }

    /**
     * Must be overridden by subclasses. Used by the dispatcher to discover the
     * manner in which the executable program should be invoked on the command
     * line.
     * 
     * @return an array of {@code String}'s that describes the command- line
     *         statement that will invoke the desired executable program. The
     *         string array should be of the form that {@code Runtime.exec()}
     *         requires.
     */
    abstract String[] getInvocationInfo();

    /**
     * Must be overriden by subclasses. Called by the dispatcher before the
     * program is invoked; subclasses should implement code here to generate the
     * "input files" that the external program will require.
     * 
     * @param timer a {@code PerfTimer} with which to record timing
     *        information about this operation
     * @throws IOException if an I/O-related error occurs; this will force the
     *         dispatcher to abort the job.
     */
    abstract void writeInputFiles(PerfTimer timer) throws IOException;

    /**
     * Must be overriden by subclasses. Called by the dispatcher after the
     * program has terminated; subclasses should implement code here to read the
     * "output files" that the external program has generated.
     * 
     * @param timer a {@code PerfTimer} with which to record timing
     *        information about this operation
     * @throws IOException if an I/O-related error occurs; this will force the
     *         dispatcher to abort the job.
     */
    abstract void readOutputFiles(PerfTimer timer) throws IOException;

    /**
     * Must be overriden by subclasses. Called by the dispatcher after the
     * program has terminated and its output files read; subclasses should
     * implement code here to delete any "input files" and "output files", plus
     * other "temporary files" that might be related to the external program's
     * execution.
     * 
     * @param timer a {@code PerfTimer} with which to record timing
     *        information about this operation
     */
    abstract void deleteFiles(PerfTimer timer) throws IOException;

    /** Getter function for use by anyone */
    public long getId() {
        return id;
    }

    /**
     * Getter function for use by anyone. Returns a clone of the real
     * {@code perfTimer} for security.
     */
    public PerfTimer getPerfTimer() {
        return perfTimer.clone();
    }

    /** Getter function for use by anyone */
    public int getErrorCode() {
        return errorCode;
    }

    /** Getter function for use by anyone */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Getter function for use by anyone. Returns an unmodifable version of the
     * real {@code nodesAssignedTo} for security.
     */
    public Set<ComputeNode> getNodesAssignedTo() {
        return Collections.unmodifiableSet(nodesAssignedTo);
    }

    /** From interface Comparable; ordering is by job id */
    public int compareTo(AbstractJob o) {
        if (this.id < o.id) {
            return -1;
        } else if (this.id == o.id) {
            return 0;
        } else {
            return 1;
        }
    }

    /** Setter method use by the job dispatcher only */
    void setBaseDirectory(File baseDirectory) {
        this.baseDirectory = baseDirectory;
    }

    /**
     * Utility function for use by subclasses. Creates the specified file and
     * fills it with the specified data bytes.
     */
    protected static void bytesToFile(File dest, byte source[])
            throws IOException {
        OutputStream os = new FileOutputStream(dest);
        os.write(source);
        os.close();
    }

    /**
     * Utility function for use by subclasses. Reads the specified file and
     * returns its contents as a byte array.
     */
    protected static byte[] bytesFromFile(File source) throws IOException {
        int totalBytes = (int) source.length(); // hope the file isn't bigger
                                                // than 2gig
        byte buffer[] = new byte[totalBytes];
        InputStream is = new FileInputStream(source);
        int bytesRead = 0;
        do {
            bytesRead += is.read(buffer, bytesRead, totalBytes - bytesRead);
        } while (bytesRead < totalBytes);
        is.close();
        return buffer;
    }
}
