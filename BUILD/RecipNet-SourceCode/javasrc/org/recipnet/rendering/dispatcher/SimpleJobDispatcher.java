/*
 * Reciprocal Net project
 * rendering software
 * 
 * SimpleJobDispatcher.java
 *
 * 10-Feb-2003: ekoperda wrote first draft
 * 06-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.rendering.dispatcher;

import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class SimpleJobDispatcher extends AbstractJobDispatcher {
    private int maxNodesPerJob;

    private int maxJobsPerNode;

    public SimpleJobDispatcher(Set<? extends ComputeNode> availableNodes,
            File jobScratchArea, boolean retainJobFiles,
            long maxSchedulingDelay, int maxNodesPerJob, int maxJobsPerNode) {
        super(availableNodes, jobScratchArea, retainJobFiles,
                maxSchedulingDelay);
        adjustParameters(maxNodesPerJob, maxJobsPerNode);
    }

    public void adjustParameters(int maxNodes, int maxJobs) {
        this.maxNodesPerJob = maxNodes;
        this.maxJobsPerNode = maxJobs;
    }

    /**
     * Overrides function from AbstractJobDispatcher. If resources permit,
     * assigns the job to as many nodes as possible (up to
     * {@code maxNodesPerJob}) without exceeding {@code maxJobsPerNode} on any
     * single node. The job's cost (as stored in {@code AbstractJob.cost} is not
     * considered. Will assign the job to as few nodes as one.
     */
    @Override
    protected Set<ComputeNode> makeDispatchDecision(
            @SuppressWarnings("unused") AbstractJob job,
            List<ComputeNode> nodesInOrder) {
        Set<ComputeNode> nodesToUse = new TreeSet<ComputeNode>();

        for (ComputeNode node : nodesInOrder) {
            if (nodesToUse.size() >= this.maxNodesPerJob) {
                break;
            } else if (node.currentJobCount < this.maxJobsPerNode) {
                nodesToUse.add(node);
            }
        }

        return nodesToUse;
    }

    /**
     * Overrides function from AbstractJobDispatcher. Sorts jobs in simple
     * priority order, from highest to lowest, according to the value stored in
     * {@code AbstractJob.priority}.
     */
    protected static Comparator<AbstractJob> getJobComparator() {
        return new Comparator<AbstractJob>() {
            public int compare(AbstractJob x, AbstractJob y) {
                return y.priority - x.priority;
            }
        };
    }

    /**
     * Overrides function from AbstractJobDispatcher. Sorts nodes in order of
     * increasing load (as stored in {@code ComputeNode.currentLoadSum}) and
     * then the time at which a job was last assigned. The latter feature helps
     * assure round-robin scheduling on lightly-loaded systems.
     */
    protected static Comparator<ComputeNode> getNodeComparator() {
        return new Comparator<ComputeNode>() {
            public int compare(ComputeNode x, ComputeNode y) {
                if (y.currentLoadSum < x.currentLoadSum) {
                    return -1;
                } else if (y.currentLoadSum > x.currentLoadSum) {
                    return 1;
                } else {
                    if (x.timeLastJobAssigned < y.timeLastJobAssigned) {
                        return -1;
                    } else if (x.timeLastJobAssigned > y.timeLastJobAssigned) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
            }
        };
    }
}
