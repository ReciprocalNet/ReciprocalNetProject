/*
 * Reciprocal Net Project
 * WorkflowActionPersistedOperation.java
 *
 * 27-Jan-2005: midurbin wrote first draft
 */
package org.recipnet.site.wrapper;
import org.recipnet.site.shared.db.SampleInfo;

/**
 * A <code>PersistedOperation</code> for workflow actions.  This class contains
 * a <code>SampleInfo</code> object that contains changes performed during
 * various HTTP roundtrips and the user id of the user who may further modify
 * the <code>SampleInfo</code> on a workflow action page.
 */
public class WorkflowActionPersistedOperation extends PersistedOperation {

    /**
     * The sample that is being modified by this multiple round-trip workflow
     * action.  
     */
    private SampleInfo sampleInfo;

    /** The comments for this multiple round-trip workflow action. */
    private String comments;

    /**
     * The id of the user that initiated this persisted operation.  The caller
     * is responsible for providing this value when a new
     * <code>PersistedOperation</code> is created and for verifying that only
     * that use may later access or modify it.
     */
    private int userId;

    /**
     * A constructor to initialize the internal variables.
     * @param expirationInterval the number of milliseconds past an access that
     *     this operation should expire if not accessed again.  May be set to
     *     the special value <code>NO_TIME</code>, in which case this operation
     *     will never expire.  This value should not be less than zero.
     * @param si a <code>SampleInfo</code> for this operation
     * @param comments the comments for this workflow action
     *     (<code>String</code>) 
     * @param userId the userId associated with this operation 
     */
    public WorkflowActionPersistedOperation(long expirationInterval,
            SampleInfo si, String comments, int userId) {
        super(expirationInterval);
        this.sampleInfo = si;
        this.comments = comments;
        this.userId = userId;
    }

    /**
     * @return the <code>SampleInfo</code> object being modified for the
     *     workflow action associated with this persisted operation
     */
    public SampleInfo getSampleInfo() {
        return this.sampleInfo;
    }

    /**
     * @return a <code>String</code> representation of any comments entered
     *     for the workflow action associated with this persisted operation, or
     *     null if none have been entered.
     */
    public String getComments() {
        return this.comments;
    }

    /**
     * Allows comments to be updated.
     * @param comments the comments for this workflow action
     */
    public void setComments(String comments) {
        this.comments = comments;
    }

    /**
     * @return the userid of the user that initiated and may continue the
     *     persisted operation
     */
    public int getUserId() {
        return this.userId;
    }
}
