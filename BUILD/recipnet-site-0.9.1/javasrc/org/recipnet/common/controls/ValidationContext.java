/*
 * Reciprocal Net project
 * 
 * ValidationContext.java
 * 
 * 19-Feb-2004: midurbin wrote first draft
 * 05-Aug-2004: midurbin rewrote the javadocs to more clearly define the 
 *              usage and conventions.
 * 12-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.common.controls;

/**
 * A {@code ValidationContext} is meant to be implemented by a tag that to allow
 * its nested elements to determine whether all of their peers have valid
 * entries. A field that recognizes {@code ValidationContext} is expected to
 * report its validation errors to the most immediate {@code ValidationContext}
 * in which it is nested which will report the error to its most immediate
 * {@code ValidationContext} so that each one may maintain its own independent
 * validation status.
 */
public interface ValidationContext {

    /**
     * Signals this context that a nested tag has detected a validation error.
     * Such a condition should be reported before the beginning of the
     * {@code PROCESSING_PHASE}, otherwise it may be assumed that every field
     * contains a valid entry.
     */
    public void reportValidationError();

    /**
     * Determines whether all tags nested within and cooperating with this
     * context are valid, to the extent that that information has been reported
     * to this context. This method's result should not be assumed final until
     * after the end of the {@code FETCHING_PHASE}.
     * 
     * @return {@code true} if all fields are valid, or {@code false} if any
     *         calls have been made to this context's
     *         {@link #reportValidationError()} method
     */
    public boolean areAllFieldsValid();

}
