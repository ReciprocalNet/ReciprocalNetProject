/*
 * Reciprocal Net Project
 * 
 * CifErrorRecorder.java
 * 
 * 09-Feb-2005: jobollin wrote first draft
 */
 
package org.recipnet.common.files.cif;

import java.util.*;

/**
 * A simple CifErrorHandler implementation that just records all errors of
 * which it is notified in a {@code List}.
 * 
 * @author John C. Bollinger
 * @version 0.9.0
 */
public class CifErrorRecorder implements CifErrorHandler {
    
    /**
     * an internal {@code List} in which errors reported to this handler
     * are recorded
     */
    private List<CifError> errors = new ArrayList<CifError>();

    /**
     * Returns an unmodifiable view of the current list of errors, in the order
     * they were recorded.  Note that even though the returned {@code List} is
     * [externally] unmodifiable, it is not thread-safe, in that it may reflect
     * modifications to the list subsequently made by this
     * {@code CifErrorRecorder} itself (i.e. via its
     * {@link #handleError(CifError)} method)
     * 
     * @return an unmodifiable {@code List} view of the current list of
     *         errors
     */
    public List<CifError> getErrors() {
        return Collections.unmodifiableList(errors);
    }
    
    /**
     * Clears the current list of errors
     */
    public void clearErrors() {
        errors.clear();
    }

    /**
     * {@inheritDoc}.  This version simply records the error object in an
     * internal {@code List}
     * 
     * @throws NullPointerException if the argument is {@code null}
     */
    public void handleError(CifError error) {
        if (error == null) {
            throw new NullPointerException("Null argument");
        } else {
            errors.add(error);
        }
    }
}
