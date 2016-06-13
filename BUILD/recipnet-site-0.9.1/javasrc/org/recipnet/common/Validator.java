/*
 * Reciprocal Net project
 * 
 * Validator.java
 * 
 * 27-Feb-2004: cwestnea wrote first draft
 * 11-Jan-2005: jobollin moved this class to package org.recipnet.common
 * 13-Jan-2005: ekoperda clarified comments regarding null obj's
 * 24-May-2006: jobollin reformatted the source
 */

package org.recipnet.common;

/**
 * <p>
 * Implementors of {@code Validator} must implement the {@link #isValid(Object)}
 * method to return {@code true} if the object is a valid object, or
 * {@code false} if the object is invalid. Classes that implement
 * {@code Validator} must be thread-safe.
 * </p><p>
 * Classes that implement {@code Validator} may handle {@code null} references
 * to objects in one of two ways: they may throw a {@code NullPointerException}
 * or they may return a boolean value like usual. Consult the documentation on
 * the particular class of interest to learn the specifics of that class's
 * implementation.
 * </p>
 */
public interface Validator {
    
    /**
     * Determines whether a specified object is valid
     * 
     * @param obj the object to be evaluated. Consult documentation on the
     *        implementing class for more information about the particular types
     *        of obj that are considered valid and for a description of the
     *        function's behavior if obj is null.
     * @return {@code true} if the object is valid, {@code false} otherwise.
     * @throws NullPointerException if obj is null, possibly, at the option of
     *         the implementing class.
     */
    public boolean isValid(Object obj);
}
