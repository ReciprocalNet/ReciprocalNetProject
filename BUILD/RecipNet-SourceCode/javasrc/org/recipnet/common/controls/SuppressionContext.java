/*
 * Reciprocal Net project
 * 
 * SuppressionContext.java
 * 
 * 26-Aug-2004: midurbin wrote first draft
 * 12-Jun-2006: jobollin performed minor cleanup
 */

package org.recipnet.common.controls;

/**
 * <p>
 * Due to the nature of {@code HtmlPageElement}'s phase-based evaluation every
 * tag that extends from it must be evaluated for every phase. In cases where a
 * tag wishes to suppress its body based on information that is not available
 * immediately, there must be a way to continue to evaluate the body but prevent
 * any output, or response-altered actions from being performed.
 * </p><p>
 * This interface allows tags to optionally, on a phase-by-phase basis, indicate
 * to nested tags that they must suppress any such actions.
 * </p>
 */
public interface SuppressionContext {

    /**
     * A method available to nested tags so they can determine whether or not
     * they must suppress certain actions. This method must be called before any
     * nested tag performs a response-altering actions such as a redirect or
     * forward.
     * 
     * @return {@code true} if the body (and all nested tags) of this
     *         {@code SupressionContext} is currently suppressed, {@code false}
     *         if not
     */
    public boolean isTagsBodySuppressedThisPhase();

}
