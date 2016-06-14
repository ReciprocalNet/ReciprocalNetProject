/*
 * Reciprocal Net Project
 * 
 * CifErrorIgnorer.java
 * 
 * 31-Jan-2005: jobollin wrote first draft
 */
 
package org.recipnet.common.files.cif;

/**
 * A simple CifErrorHandler implementation that just ignores all errors of
 * which it is notified.
 * 
 * @author John C. Bollinger
 * @version 0.9.0
 */
public class CifErrorIgnorer implements CifErrorHandler {

    /**
     * {@inheritDoc}.  This implementation does nothing.
     *
     * @throws NullPointerException if the argument is {@code null}
     */
    public void handleError(CifError error) {
        if (error == null) {
            throw new NullPointerException("Null argument");
        }
    }
}
