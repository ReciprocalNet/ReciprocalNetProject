/*
 * Reciprocal Net Project
 * rendering software
 * 
 * ComputeNode.java
 *
 * 10-Feb-2003: ekoperda wrote first draft
 * 06-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.rendering.dispatcher;

public class ComputeNode implements Comparable<ComputeNode> {
    public final String displayName;

    public final String networkName;

    public long speedFactor = 1;

    // Maniuplated by AbstractJobDispatcher and subclasses.
    long currentLoadSum = 0;

    int currentJobCount = 0;

    long timeLastJobAssigned = 0;

    public ComputeNode(String displayName, String networkName) {
        this.displayName = displayName;
        this.networkName = networkName;
    }

    public int compareTo(ComputeNode node) {
        return this.networkName.compareTo(node.networkName);
    }
}
