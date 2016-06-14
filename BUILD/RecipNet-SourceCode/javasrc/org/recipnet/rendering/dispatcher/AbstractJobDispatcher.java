/*
 * Reciprocal Net project
 * rendering software
 * 
 * AbstractJobDispatcher.java
 *
 * 10-Feb-2003: ekoperda wrote first draft
 * 07-Jan-2004: ekoperda changed package references to match source tree
 *              reorganization
 * 06-Jun-2006: jobollin reformatted the source
 * 07-May-2008: ekoperda adjusted reportStatus() for compliance with Java 1.6
 */

package org.recipnet.rendering.dispatcher;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.recipnet.common.PerfTimer;
import org.recipnet.common.ProcessWrapper;

/**
 * <p>
 * Abstract class that knows how manage and track distributed computational
 * jobs, scheduling them and dispatching them to compute nodes according to
 * subclass-specific criteria. Decision-making logic (such as how many jobs of
 * each sort may run on a single node at a time, and how many nodes a single job
 * may occupy) is deliberately excluded from this class because of its
 * inherently extreme performance implications and liklihood for needing to be
 * changed in the future. This class contains just the generic plumbing for
 * distributed process management. To be useful, a subclass should override
 * {@code makeDispatchDecision()}, {@code getJobComparator()}, and
 * {@code getNodeComparator()}.
 * </p><p>
 * The class tracks a set of {@code ComputeNodes} that are available to perform
 * computational tasks; this set may change during the dispatcher's lifetime via
 * {@code addNode()} and {@code removeNode()}. Jobs are submitted via
 * {@code doJob()}, which may block until sufficient computational resources
 * are available, then spawns the job, and returns once it terminates.
 * </p><p>
 * It is assumed that the computational task is implemented by an external
 * process that may be spawned from the command line (on the local computer),
 * and that the external process has logic for invoking and distributing itself
 * among any specified subset of available nodes. This functionality is wrapped
 * by subclasses of {@code GenericJob}, which must implement code to generate
 * the compute process's necessary input files, read the compute process's
 * output files, and specify the manner in which the external process should be
 * invoked (on the command line).
 * </p>
 */
public abstract class AbstractJobDispatcher {
    // Components of the state table. No value may be get or set unless the
    // caller has synchronize()'d on dispatcherLock.
    private Object dispatcherLock;

    private Set<ComputeNode> availableNodes;

    private List<ComputeNode> nodesInOrder;

    private List<AbstractJob> pendingJobs;

    private Set<AbstractJob> activeJobs;

    private long nextJobIdToAssign;

    // Populated at construction time.
    private Comparator<AbstractJob> jobComparator;

    private Comparator<ComputeNode> nodeComparator;

    // Configuration options set at construction time.
    private File jobScratchArea;

    private boolean retainJobFiles;

    private long maxSchedulingDelay;

    /**
     * One and only constructor.
     * 
     * @param availableNodes a {@code Set} of {@code ComputeNode} that
     *        represents the initial set of nodes to which jobs may be assigned.
     *        The set of available nodes may be altered during this object's
     *        runtime via the addNode() and removeNode() methods. The supplied
     *        {@code Set} is copied and not modified, but the
     *        {@code ComputeNode} objects within it are not copied and must not
     *        be modified subsequently by the caller.
     * @param jobScratchArea a directory in the filesystem where temporary work
     *        directories for each job will be created (and destroyed). In a
     *        typical distributed environment this portion of the filesystem
     *        would be shared among all compute nodes.
     * @param retainJobFiles should be {@code false} for normal operation, but
     *        if true prevents temporary job files from being deleted once the
     *        job is complete. This is useful only for debugging purposes.
     * @param maxSchedulingDelay the approximate maximum number of milliseconds
     *        that may elapse during a call to doJob() before an exception is
     *        thrown. If 0, no limit is enforced and the call may block
     *        indefinitely (until compute resources are available to service the
     *        submitted job).
     */
    public AbstractJobDispatcher(Set<? extends ComputeNode> availableNodes,
            File jobScratchArea, boolean retainJobFiles, long maxSchedulingDelay) {
        this.dispatcherLock = new Integer(0);
        this.availableNodes = new TreeSet<ComputeNode>(availableNodes);
        recalculateNodesInOrder();
        this.pendingJobs = new LinkedList<AbstractJob>();
        this.activeJobs = new TreeSet<AbstractJob>();
        this.nextJobIdToAssign = 1;
        this.jobComparator = getJobComparator();
        this.nodeComparator = getNodeComparator();
        this.jobScratchArea = jobScratchArea;
        this.retainJobFiles = retainJobFiles;
        this.maxSchedulingDelay = maxSchedulingDelay;

        // Set all nodes to known-good status.
        for (ComputeNode node : availableNodes) {
            node.currentLoadSum = 0;
            node.currentJobCount = 0;
            node.timeLastJobAssigned = 0;
        }
    }

    /**
     * Allows the caller to modify the internal status of this dispatcher. The
     * three supplied collection objects are cleared and then filled with
     * container objects internal to the dispatcher. These {@code ComputeNode}
     * and {@code AbstractJob} objects may be examined by the caller but must
     * not be modified.
     * 
     * @param pendingJobs a caller-provided {@code Collection} that contains
     *        zero or more {@code AbstractJob} objects upon return. Job objects
     *        are in the projected order of execution.
     * @param activeJobs a caller-provided {@code Collection} that contains
     *        zero or more {@code AbstractJob} objects upon return. These are
     *        the jobs currently being executed on one or more compute nodes.
     * @param availableNodes a caller-provided {@code Collection} that contains
     *        one or more {@code ComputeNode} objects upon return. These
     *        represent the current set of available compute nodes.
     */
    public void reportStatus(Collection<? super AbstractJob> pendingJobs,
            Collection<? super AbstractJob> activeJobs,
            Collection<? super ComputeNode> availableNodes) {
        pendingJobs.clear();
        pendingJobs.addAll(this.pendingJobs);
        activeJobs.clear();
        activeJobs.addAll(this.activeJobs);
        availableNodes.clear();
        availableNodes.addAll(this.availableNodes);
    }

    /**
     * Used to modify the set of available nodes during operation - makes a new
     * node available for job scheduling.
     */
    public void addNode(ComputeNode node) {
        // Set the node to known-good status.
        node.currentLoadSum = 0;
        node.currentJobCount = 0;
        node.timeLastJobAssigned = 0;

        synchronized (dispatcherLock) {
            availableNodes.add(node);
            recalculateNodesInOrder();
            dispatcherLock.notifyAll();
        }
    }

    /**
     * Used to modified the set of available nodes during operation - removes a
     * previously-available node from scheduling. Note that this has no effect
     * on jobs already assigned to this node and presently executing -- the
     * caller should poll reportStatus() to detect when the last job finally
     * terminates.
     */
    public void removeNode(ComputeNode node) {
        synchronized (dispatcherLock) {
            availableNodes.remove(node);
            recalculateNodesInOrder();
            dispatcherLock.notifyAll();
        }
    }

    /**
     * Assigns a unique id to the caller-provided job object in advance of the
     * job being submitted to doJob(). This is an optional step, and is useful
     * only if the caller makes extensive use of the job object in its own code,
     * and a unique id would facilitate its tracking or error reporting.
     */
    public void assignJobId(AbstractJob job) {
        synchronized (dispatcherLock) {
            job.id = nextJobIdToAssign++;
        }
    }

    /**
     * Submits a job for distributed execution and waits for it to finish. The
     * job should be a subclass of {@code GenericJob} that contains intelligence
     * for generating input files, invoking the compute process on the command
     * line, and reading output files. The job's {@code id} and
     * {@code baseDirectory} fields are populated prior to any of the job's
     * functions being invoked. Upon return, the {@code id} {@code timeQueued},
     * {@code timeClearedToStart}, {@code timeStarted}, {@code timeTerminated},
     * {@code timeCompleted}, {@code errorCode}, and {@code errorMessage} are
     * available for the caller's examination and possibly logging.
     * <p>
     * Subclasses need not override this method.
     * 
     * @throws RuntimeException if {@code maxSchedulingDelay} was set at
     *         construction time and approximately that number of milliseconds
     *         have elapsed without the job having been scheduled (due to the
     *         compute resources being busy).
     * @throws RuntimeException if the temporary scratch directory could not be
     *         created.
     */
    public void doJob(AbstractJob job) throws IOException {
        job.perfTimer = new PerfTimer("doJob()");
        job.perfTimer.newChild("queued");

        synchronized (dispatcherLock) {
            if (job.id == 0) {
                // Assign a job id if one hasn't been assigned already.
                job.id = nextJobIdToAssign++;
            }

            // Put the new job into the pending-jobs list.
            int insertionPos = -(Collections.binarySearch(pendingJobs, job,
                    jobComparator) + 1);
            if (insertionPos == 0) {
                // The job went to the head of the pending job list; check with
                // the dispatcher to see if it's eligible to run now.
                job.nodesAssignedTo = makeDispatchDecision(job, nodesInOrder);
            }
            if ((job.nodesAssignedTo == null) || job.nodesAssignedTo.isEmpty()) {
                // The job is not eligible to run now; block until it is.
                pendingJobs.add(insertionPos, job);
                while ((((job.nodesAssignedTo == null)
                        || job.nodesAssignedTo.isEmpty())
                        && ((this.maxSchedulingDelay != 0)
                                && (job.perfTimer.currentChild.elapsed()
                                < this.maxSchedulingDelay)))
                        || (this.maxSchedulingDelay == 0)) {
                    try {
                        dispatcherLock.wait(this.maxSchedulingDelay == 0 ? 0
                                : this.maxSchedulingDelay
                                        - job.perfTimer.currentChild.elapsed());
                    } catch (InterruptedException ex) {
                        // no need to take action; just iterate again.
                    }
                    if (pendingJobs.get(0) == job) {
                        // The job is at the head of the pending job list;
                        // check with the dispatcher to see if it's eligible to
                        // run now.
                        job.nodesAssignedTo = makeDispatchDecision(job,
                                nodesInOrder);
                    }
                }
                if (job.perfTimer.currentChild.elapsed()
                        > this.maxSchedulingDelay) {
                    throw new RuntimeException(
                            "Compute resources busy -- scheduling delay exceeded");
                }

                // The job is eligible to run now; remove it from the pending
                // list.
                pendingJobs.remove(0);
                dispatcherLock.notifyAll();
            }

            // If we get here, the job has been cleared to run. Record it as
            // being active.
            job.perfTimer.stopChild();
            job.perfTimer.newChild("preparing");
            activeJobs.add(job);
            for (ComputeNode node : job.nodesAssignedTo) {
                node.currentLoadSum += job.cost;
                node.currentJobCount++;
                node.timeLastJobAssigned = job.perfTimer.currentChild.startTime;
            }
            recalculateNodesInOrder();
        }

        // Create a base directory for this job's files; let the job prepare
        // its input files.
        NumberFormat nf = NumberFormat.getIntegerInstance();
        nf.setMinimumIntegerDigits(8);
        nf.setGroupingUsed(false);
        File baseDirectory = new File(jobScratchArea, "job" + nf.format(job.id));
        boolean rc = baseDirectory.mkdir();
        if (!rc) {
            throw new IOException("Unable to create scratch directory "
                    + baseDirectory.getPath());
        }
        job.setBaseDirectory(baseDirectory);
        job.writeInputFiles(job.perfTimer.currentChild);

        // Invoke the job's executable and let it finish.
        // TODO: capture the text from stderr
        ByteArrayOutputStream procErrorStream = new ByteArrayOutputStream();
        job.perfTimer.stopChild();
        job.perfTimer.newChild("executing");
        Process proc = new ProcessWrapper(Runtime.getRuntime().exec(
                job.getInvocationInfo()), true, true, null, true,
                procErrorStream);
        try {
            job.errorCode = proc.waitFor();
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
        job.errorMessage = new String(procErrorStream.toByteArray());

        // Let the job read its output files and then delete the scratch area.
        job.perfTimer.stopChild();
        job.perfTimer.newChild("finalizing");
        job.readOutputFiles(job.perfTimer.currentChild);
        if (!retainJobFiles) {
            job.deleteFiles(job.perfTimer.currentChild);
            baseDirectory.delete();
        }

        // Remove the job from the active list.
        synchronized (dispatcherLock) {
            activeJobs.remove(job);
            for (ComputeNode node : job.nodesAssignedTo) {
                node.currentLoadSum -= job.cost;
                node.currentJobCount--;
                // Deliberately leave the node in the set so that users can
                // discover which nodes their jobs ran on if desired.
            }
            recalculateNodesInOrder();
            dispatcherLock.notifyAll();
        }
        job.perfTimer.stopChild();
        job.perfTimer.stop();

        // Possibly log something here.
    }

    /**
     * Internal function that clears the member variable {@code nodesInOrder}
     * and repopulates it from the {@code availableNodes} list. This is a fairly
     * expensive operation - it requires a sort - and thus should be called only
     * when the list of available nodes, or the jobs assigned to them, has
     * changed.
     */
    private void recalculateNodesInOrder() {
        if (nodesInOrder == null) {
            nodesInOrder = new ArrayList<ComputeNode>(availableNodes);
        } else {
            nodesInOrder.clear();
            nodesInOrder.addAll(availableNodes);
        }
        Collections.sort(nodesInOrder, nodeComparator);
    }

    /**
     * Internal function that must be overridden by subclasses. Function decides
     * whether the specified {@code job} (which may be assumed to sit at the
     * head of the queue of pending jobs) may run now, and if so, which compute
     * nodes it may run on. The function must make a decision based upon the
     * list of nodes provided and should not access any member variables of
     * {@code JobDispatcher}. The function must not make changes to any value
     * inside either of its arguments.
     * 
     * @return a {@code Set} of {@code ComputeNode} objects to indicate the
     *         nodes on which the job may run. May return either an empty set or
     *         {@code null} to indicate the job may not run.
     * @param job the {@code GenericJob} object that is being considered for
     *        scheduling.
     * @param nodesInOrder a {@code List} of one or more {@code ComputeNode}
     *        objects that includes all available nodes. The nodes are sorted
     *        according to getNodeComparator(), presumably in order from "least
     *        busy at the moment" to "most busy at the moment".
     */
    protected abstract Set<ComputeNode> makeDispatchDecision(AbstractJob job,
            List<ComputeNode> nodesInOrder);

    /**
     * Internal function that must be overriden by subclasses.
     * 
     * @return a {@code Comparator} suitable for comparing one
     *         {@code GenericJob} to another. The ordering should go from
     *         highest priority (scheduled first) to lowest priority (scheduled
     *         last). This, in turn, means that compare() should return a
     *         negative number if job A should be scheduled before job B.
     */
    protected static Comparator<AbstractJob> getJobComparator() {
        return null;
    }

    /**
     * Internal function that must be overriden by subclasses.
     * 
     * @return a {@code Comparator} suitable for comparing one
     *         {@code ComputeNode} to another. The ordering should go from
     *         "least busy at the moment" to "most busy at the moment", which in
     *         turn means that compare() should return a negative number if node
     *         A is less busy than node B.
     */
    protected static Comparator<ComputeNode> getNodeComparator() {
        return null;
    }
}
